package dojodev.componentsystems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import dojodev.components.CollisionComponent;
import dojodev.components.DisplayComponent;
import dojodev.components.PositionComponent;
import dojodev.rogueshooter.RSEngine;

public class DisplaySystem extends EntitySystem
{
	private ImmutableArray<Entity> entities;
	
	private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<DisplayComponent> dm = ComponentMapper.getFor(DisplayComponent.class);
	
	private RSEngine eng;
	
	public DisplaySystem(RSEngine engine)
	{
		eng = engine;
	}
	
	public void addedToEngine(Engine engine)
	{
		entities = engine.getEntitiesFor(Family.all(PositionComponent.class, DisplayComponent.class).get());
	}
	
	public void update(float deltaTime)
	{
		Gdx.gl.glClearColor(1, 1, 1, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		eng.batch.setProjectionMatrix(eng.camera.combined);
		eng.batch.begin();
		
		for (Entity e : entities)
		{
			PositionComponent pc = pm.get(e);
			DisplayComponent dc = dm.get(e);
			CollisionComponent cc = e.getComponent(CollisionComponent.class);
			
			if (cc != null)
			{
				eng.batch.draw(dc.getTexture(), pc.x, pc.y, cc.width / 2, cc.height / 2, dc.getTexture().getRegionWidth(), dc.getTexture().getRegionHeight(), 1.f, 1.f, dc.rotation);	
			}
			else
			{
				eng.batch.draw(dc.getTexture(), pc.x, pc.y);
			}
		}
		
		eng.batch.end();
		PositionComponent pc = eng.player.getComponent(PositionComponent.class);
		eng.camera.position.set(pc.x, pc.y, 0);
	}
}
