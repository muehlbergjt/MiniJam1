package dojodev.rogueshooter;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import dojodev.components.CollisionComponent;
import dojodev.components.DisplayComponent;
import dojodev.components.PositionComponent;
import dojodev.components.VelocityComponent;
import dojodev.map.TestMap;

public class RogueShooter extends ApplicationAdapter 
{
	Texture img;
	RSEngine eng;
	
	TestMap map;
	
	@Override
	public void create () 
	{
		eng = new RSEngine();

		map = new TestMap(eng, 100, 50);
		
		eng.camera.position.set(map.upStairsX * 16, map.upStairsY * 16, 0);
		eng.camera.update();
		
		img = new Texture("link2.png");
		
		Entity player = new Entity();
		player.add(new PositionComponent(map.upStairsX * 16, map.upStairsY * 16));
		player.add(new VelocityComponent(0, 0));
		player.add(new CollisionComponent(map.upStairsX * 16, map.upStairsY * 16, 16, 16));
		player.add(new DisplayComponent(img));
		eng.addEntity(player);
		eng.player = player;
	}

	@Override
	public void render () 
	{
		VelocityComponent vc = eng.player.getComponent(VelocityComponent.class);
		
		Vector3 mouse = eng.getMousePosition();
		
		float angle = -eng.getTheta(eng.getEntityVector(eng.player), mouse);
		
		DisplayComponent dc = eng.player.getComponent(DisplayComponent.class);
		dc.rotation = angle;
		
		if (Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A))
		{
			vc.vx = -8;
		}
		else if (Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D))
		{
			vc.vx = 8;
		}
		else
		{
			vc.vx = 0;
		}
		
		if (Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.W))
		{
			vc.vy = 8;
		}
		else if (Gdx.input.isKeyPressed(Keys.DOWN) || Gdx.input.isKeyPressed(Keys.S))
		{
			vc.vy = -8;
		}
		else
		{
			vc.vy = 0;
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.SPACE))
		{
			eng.createBullet(eng.player, eng.getMousePosition(), 5);
		}
		
		eng.update(Gdx.graphics.getDeltaTime());
	}
	
	@Override
	public void dispose () 
	{
		eng.dispose();
		img.dispose();
	}
}
