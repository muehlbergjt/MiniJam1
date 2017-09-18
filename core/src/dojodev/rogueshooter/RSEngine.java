package dojodev.rogueshooter;

import java.util.LinkedList;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

import dojodev.components.CollisionComponent;
import dojodev.components.DisplayComponent;
import dojodev.components.IDComponent;
import dojodev.components.PositionComponent;
import dojodev.components.VelocityComponent;
import dojodev.componentsystems.CollisionSystem;
import dojodev.componentsystems.DisplaySystem;
import dojodev.componentsystems.MovementSystem;

public class RSEngine 
{
	public Engine engine;
	public OrthographicCamera camera;
	public SpriteBatch batch;
	private int nextID;
	public LinkedList<Entity> NPCs;
	public Entity player;
	
	public RSEngine()
	{
		batch = new SpriteBatch();
		System.out.println("Initializing Engine");
		nextID = 0;
		engine = new Engine();
		camera = new OrthographicCamera();
		camera.setToOrtho(false);
		engine.addSystem(new CollisionSystem());
		engine.addSystem(new MovementSystem());
		engine.addSystem(new DisplaySystem(this));
		NPCs = new LinkedList<Entity>();
		System.out.println("Engine Initialized");
	}
	
	public void update(float deltaTime)
	{
		engine.update(deltaTime);
		camera.update();
	}
	
	private int getNextID()
	{
		nextID += 1;
		return nextID;
	}
	
	public void addEntity(Entity e)
	{
		e.add(new IDComponent(getNextID()));
		engine.addEntity(e);
	}
	
	public void removeEntity(Entity e)
	{
		engine.removeEntity(e);
	}
	
	public float getTheta(Vector3 a, Vector3 b)
	{
		double dx = a.x - b.x;
		double dy = a.y - b.y;
		
		return (float)(MathUtils.radiansToDegrees * Math.atan2(dx, dy));
	}
	
	public Vector3 getEntityVector(Entity a)
	{
		PositionComponent pc = a.getComponent(PositionComponent.class);
		CollisionComponent cc = a.getComponent(CollisionComponent.class);
		
		if (cc == null)
		{
			return null;
		}
		
		Vector3 va = new Vector3();
		va.x = pc.x + cc.width / 2;
		va.y = pc.y + cc.height / 2;
		return va;
	}
	
	public Vector3 getMousePosition()
	{
		return camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
	}
	
	public void createBullet(Entity e, Vector3 direction, float speed)
	{
		PositionComponent pc = e.getComponent(PositionComponent.class);
		DisplayComponent dc = e.getComponent(DisplayComponent.class);
		CollisionComponent cc = e.getComponent(CollisionComponent.class);
		
		float dx = (float) (speed * Math.sin(dc.rotation * MathUtils.degreesToRadians));
		float dy = (float) -(speed * Math.cos(dc.rotation * MathUtils.degreesToRadians));
		
		float x = (float)(21 * MathUtils.sin(dc.rotation * MathUtils.degreesToRadians) + pc.x + cc.width / 2);
		float y = (float)(21 * -MathUtils.cos(dc.rotation * MathUtils.degreesToRadians) + pc.y + cc.height / 2);
		
		Entity bullet = new Entity();
		bullet.add(new VelocityComponent(dx, dy)).add(new PositionComponent(x, y)).add(new DisplayComponent(new Texture("bullet.png")));
		bullet.add(new CollisionComponent(x, y, 8, 8));
		addEntity(bullet);
	}
	
	public void dispose()
	{
		System.out.println("Shutting Down");
		System.out.println("Disposing Graphical Assets");
		for (Entity e : engine.getEntities())
		{
			DisplayComponent dc = e.getComponent(DisplayComponent.class);
			
			if (dc != null)
			{
				dc.texRegion.getTexture().dispose();
			}
		}
		
		System.out.println("Disposing Batch");
		batch.dispose();
		System.out.println("Exiting");
	}
}
