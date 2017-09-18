package dojodev.map;

import java.util.HashMap;

public enum TileType 
{
	FLOOR(1, false, "Floor"),
	WALL(2, true, "Wall"),
	DOOR(3, false, "Door"),
	STAIRS(4, false, "Stairs");
	
	private int id;
	private boolean collidable;
	private String name;
	private float damage;
	
	public static int TILE_SIZE = 16;
	
	private TileType(int id, boolean collidable, String name)
	{
		this(id, collidable, name, 0);
	}
	
	private TileType(int id, boolean collidable, String name, float damage)
	{
		this.id = id;
		this.collidable = collidable;
		this.name = name;
		this.damage = damage;
	}
	
	public int getID()
	{
		return id;
	}
	
	public boolean isCollidable()
	{
		return collidable;
	}
	
	public String getName()
	{
		return name;
	}
	
	public float getDamage()
	{
		return damage;
	}
	
	private static HashMap<Integer, TileType> tileMap;
	
	static
	{
		tileMap = new HashMap<Integer, TileType>();
		
		for (TileType type : TileType.values())
		{
			tileMap.put(type.getID(), type);
		}
	}
	
	public static TileType getTileTypeByID(int id)
	{
		return tileMap.get(id);
	}
}
