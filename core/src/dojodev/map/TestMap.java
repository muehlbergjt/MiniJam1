package dojodev.map;

import java.util.ArrayList;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dojodev.components.CollisionComponent;
import dojodev.components.DisplayComponent;
import dojodev.rogueshooter.RSEngine;
import dojodev.components.PositionComponent;

public class TestMap extends GameMap
{
	public class Room
	{
		public int x, y;
		public int width, height;
		
		public Room(int x, int y)
		{
			this(x, y, 10, 10);
		}
		
		public Room(int x, int y, int width, int height)
		{
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}
		
		public int getCenterX()
		{
			return (x + getRight()) / 2;
		}
		
		public int getCenterY()
		{
			return (y + getBottom()) / 2;
		}
		
		public int getBottom()
		{
			return y + height - 1;
		}
		
		public int getRight()
		{
			return x + width - 1;
		}
		
		public boolean collidesWith(Room other)
		{
			if (y > other.getBottom() || getBottom() < other.y || x > other.getRight() || getRight() < other.x)
			{
				return false;
			}
			return true;
		}
	}

	ArrayList<Room> rooms;
	public int upStairsX;
	public int upStairsY;
	
	public TestMap(RSEngine engine)
	{
		this(engine, 40, 30, 16, 16);
	}
	
	public TestMap(RSEngine engine, int width, int height)
	{
		this(engine, width, height, 16, 16);
	}
	
	public TestMap(RSEngine engine, int w, int h, int tW, int tH)
	{
		eng = engine;
		width = w;
		height = h;
		tileWidth = tW;
		tileHeight = tH;
		tiles = new Entity[width][height];
		tileIDs = new int[width][height];
		
		initDungeon();
	}
	
	public void initDungeon()
	{
		createTileSprites();
		initMap();
		//generateDungeon();
		testDungeon();
	}
	
	public void createTileSprites()
	{
		spriteSheet = new Texture("overworldtiles.png");
		tileSprites = new TextureRegion[5];
		
		tileSprites[0] = new TextureRegion(spriteSheet, 69, 18, 16, 16);
		tileSprites[1] = new TextureRegion(spriteSheet, 18, 52, 16, 16);
		tileSprites[2] = new TextureRegion(spriteSheet, 273, 86, 16, 16);
		tileSprites[3] = new TextureRegion(spriteSheet, 1, 1, 16, 16);
		tileSprites[4] = new TextureRegion(spriteSheet, 205, 1, 16, 16);
		fixBleed();
	}
	
	private void fixBleed()
	{
		float fix = 0.01f;
		
		for (TextureRegion tr : tileSprites)
		{
			float x = tr.getRegionX();
			float y = tr.getRegionY();
			float width = tr.getRegionWidth();
			float height = tr.getRegionHeight();
			float invTexWidth = 1f / tr.getTexture().getWidth();
			float invTexHeight = 1f / tr.getTexture().getHeight();
			tr.setRegion((x + fix) * invTexWidth, (y + fix) * invTexHeight, (x + width - fix) * invTexWidth, (y + height - fix) * invTexHeight);
		}
	}
	
	private void initMap()
	{
		for (int x = 0; x < width; x++)
		{
			for (int y = 0; y < height; y++)
			{
				Entity e = new Entity();
				e.add(new PositionComponent(x * tileWidth, y * tileWidth));
				e.add(new DisplayComponent(tileSprites[0]));
				eng.addEntity(e);
				
				tiles[x][y] = e;
				tileIDs[x][y] = 0;
			}
		}
	}
	
	private void generateDungeon()
	{
		rooms = new ArrayList<Room>();
		
		int maxFails = 10;
		while (rooms.size() < 15 && maxFails > 0)
		{
			int roomWidth = (int)(Math.random() * 10 + 6);
			int roomHeight = (int)(Math.random() * 6 + 6);
			
			int roomLeft = (int)(Math.random() * (width - roomWidth));
			int roomTop = (int)(Math.random() * (height - roomHeight));
			
			Room room = new Room(roomLeft, roomTop, roomWidth, roomHeight);
			
			if (!roomCollides(room))
			{
				rooms.add(room);
				makeRoom(room);
			}
			else
			{
				maxFails--;
			}
		}
		
		connectRooms();
		makeWalls();
		spawnStairs();
	}
	
	private void spawnStairs()
	{
		int upRoom = (int)(Math.random() * rooms.size());
		int downRoom = (int)(Math.random() * rooms.size());
		
		while (downRoom == upRoom)
		{
			downRoom = (int)(Math.random() * rooms.size());
		}
		
		int xPos = rooms.get(upRoom).getCenterX();
		int yPos = rooms.get(upRoom).getCenterY();
		
		tiles[xPos][yPos].getComponent(DisplayComponent.class).texRegion = tileSprites[3];
		tileIDs[xPos][yPos] = 3;
		upStairsX = xPos;
		upStairsY = yPos;
		
		xPos = rooms.get(downRoom).getCenterX();
		yPos = rooms.get(downRoom).getCenterY();
		
		tiles[xPos][yPos].getComponent(DisplayComponent.class).texRegion = tileSprites[4];
		tileIDs[xPos][yPos] = 4;
	}
	
	private void connectRooms()
	{
		ArrayList<Room> connected = new ArrayList<Room>();
		ArrayList<Room> separated = (ArrayList<Room>)rooms.clone();
		
		connected.add(separated.get(0));
		separated.remove(0);
		
		int distance;
		int closestIndex;
		int connectedIndex;
		
		while (separated.size() > 0)
		{
			distance = width + height;
			closestIndex = 0;
			connectedIndex = 0;
			
			int dist = 0;
			
			for (int i = 0; i < separated.size(); i++)
			{
				for (int j = 0; j < connected.size(); j++)
				{
					dist = Math.abs(connected.get(j).getCenterX() - separated.get(i).getCenterX()) + Math.abs(connected.get(j).getCenterY() - separated.get(i).getCenterY());
					if (dist < distance)
					{
						closestIndex = i;
						connectedIndex = j;
						distance = dist;
					}
				}
			}
			
			makeCorridor(connected.get(connectedIndex), separated.get(closestIndex));
			connected.add(separated.get(closestIndex));
			separated.remove(closestIndex);
		}
	}
	
	private void makeWalls()
	{
		for (int x = 0; x < width; x++)
		{
			for (int y = 0; y < height; y++)
			{
				if (tileIDs[x][y] == 0 && hasAdjacentFloor(x, y))
				{
					tiles[x][y].getComponent(DisplayComponent.class).texRegion = tileSprites[1];
					tiles[x][y].add(new CollisionComponent(x * 16, y * 16, 16, 16));
					tileIDs[x][y] = 1;
				}
			}
		}
	}
	
	private boolean hasAdjacentFloor(int x, int y)
	{
		if (x > 0 && tileIDs[x - 1][y] == 2)
		{
			return true;
		}
		if (x < width - 1 && tileIDs[x + 1][y] == 2)
		{
			return true;
		}
		if (y > 0 && tileIDs[x][y - 1] == 2)
		{
			return true;
		}
		if (y < height - 1 && tileIDs[x][y + 1] == 2)
		{
			return true;
		}
		if (x > 0 && y > 0 && tileIDs[x - 1][y - 1] == 2)
		{
			return true;
		}
		if (x < width - 1 && y > 0 && tileIDs[x + 1][y - 1] == 2)
		{
			return true;
		}
		if (x > 0 && y < height - 1 && tileIDs[x - 1][y + 1] == 2)
		{
			return true;
		}
		if (x < width - 1 && y < height - 1 && tileIDs[x + 1][y + 1] == 2)
		{
			return true;
		}
		return false;
	}
	
	private void makeCorridor(Room roomA, Room roomB)
	{
		int x = roomA.getCenterX();
		int y = roomA.getCenterY();
		
		while (x != roomB.getCenterX())
		{
			tiles[x][y].getComponent(DisplayComponent.class).texRegion = tileSprites[2];
			tileIDs[x][y] = 2;
			tiles[x][y].remove(CollisionComponent.class);
			x += x < roomB.getCenterX() ? 1 : -1;
		}
		
		while (y != roomB.getCenterY())
		{
			tiles[x][y].getComponent(DisplayComponent.class).texRegion = tileSprites[2];
			tileIDs[x][y] = 2;
			tiles[x][y].remove(CollisionComponent.class);
			y += y < roomB.getCenterY() ? 1 : -1;
		}
	}
	
	private boolean roomCollides(Room room)
	{
		for (Room r: rooms)
		{
			if (room.collidesWith(r))
			{
				return true;
			}
		}
		return false;
	}
	
	private void makeRoom(Room room)
	{
		for (int x = 0; x < room.width; x++)
		{
			for (int y = 0; y < room.height; y++)
			{
				if (x == 0 || x == room.width - 1 || y == 0 || y == room.height - 1)
				{
					tiles[room.x + x][room.y + y].getComponent(DisplayComponent.class).texRegion = tileSprites[1];
					tiles[room.x + x][room.y + y].add(new CollisionComponent((room.x + x) * 16, (room.y + y) * 16, 16, 16));
					tileIDs[room.x + x][room.y + y] = 1;
				}
				else
				{
					tiles[room.x + x][room.y + y].getComponent(DisplayComponent.class).texRegion = tileSprites[2];
					tileIDs[room.x + x][room.y + y] = 2;
				}
			}
		}
	}
	
	private void testDungeon()
	{
		for (int x = 0; x < width; x++)
		{
			for (int y = 0; y < height; y++)
			{
				Entity e = new Entity();
				e.add(new PositionComponent(x * tileWidth, y * tileWidth));
				
				if (x == 0 || x == width - 1 || y == 0 || y == height - 1)
				{
					e.add(new DisplayComponent(tileSprites[1]));
					e.add(new CollisionComponent(x*16,y*16,16,16));
				}
				else
				{
					e.add(new DisplayComponent(tileSprites[2]));
				}
				
				eng.addEntity(e);
			}
		}
		
		upStairsX = 50;
		upStairsY = 25;
	}
}
