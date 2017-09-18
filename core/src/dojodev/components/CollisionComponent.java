package dojodev.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;

public class CollisionComponent implements Component
{
	public float x, y, width, height;
	
	public CollisionComponent()
	{
		x = 0;
		y = 0;
		width = 0;
		height = 0;
	}
	
	public CollisionComponent(float x, float y, float w, float h)
	{
		this.x = x;
		this.y = y;
		width = w;
		height = h;
	}
	
	public Rectangle getBounds()
	{
		return new Rectangle(x, y, width, height);
	}
}
