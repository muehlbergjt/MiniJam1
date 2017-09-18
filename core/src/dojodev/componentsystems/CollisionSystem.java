package dojodev.componentsystems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Rectangle;

import dojodev.components.CollisionComponent;
import dojodev.components.VelocityComponent;

public class CollisionSystem extends EntitySystem
{
	private ImmutableArray<Entity> entities;
	private ImmutableArray<Entity> sentities;
	
	private ComponentMapper<CollisionComponent> cm = ComponentMapper.getFor(CollisionComponent.class);
	
	public CollisionSystem(){}
	
	public void addedToEngine(Engine engine)
	{
		sentities = engine.getEntitiesFor(Family.all(CollisionComponent.class).get());
		entities = engine.getEntitiesFor(Family.all(VelocityComponent.class, CollisionComponent.class).get());
	}
	
	public void update(float deltaTime)
	{
		for (int i = 0; i < entities.size(); i++)
		{
			Entity e1 = entities.get(i);
			VelocityComponent vc = e1.getComponent(VelocityComponent.class);
			
			if ((vc.vx != 0 || vc.vy != 0) && vc != null)
			{
				CollisionComponent cc1 = cm.get(e1);
				
				for (int j = 0; j < sentities.size(); j++)
				{
					Entity e2 = sentities.get(j);
					
					if (!e1.equals(e2))
					{
						CollisionComponent cc2 = cm.get(e2);

						Rectangle e2Bounds = cc2.getBounds();
						Rectangle e1PostX = cc1.getBounds();
						Rectangle e1PostY = cc1.getBounds();
						
						e1PostX.x += vc.vx;
						e1PostY.y += vc.vy;
						
						if (e1PostY.overlaps(cc2.getBounds()))
						{
							System.out.println("Collision - Player:"+cc1.x+"x"+cc1.y+"y/"+cc1.width+"x"+cc1.height+" Object:"+cc2.x+"x"+cc2.y+"y/"+cc2.width+"x"+cc2.height);
							vc.vy = 0;
							continue;
						}
						if (e1PostX.overlaps(e2Bounds))
						{
							System.out.println("Collision - Player:"+cc1.x+"x"+cc1.y+"y/"+cc1.width+"x"+cc1.height+" Object:"+cc2.x+"x"+cc2.y+"y/"+cc2.width+"x"+cc2.height);
							vc.vx = 0;
							continue;
						}
					}
				}
			}
		}
	}
}
