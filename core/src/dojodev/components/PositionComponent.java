package dojodev.components;

import com.badlogic.ashley.core.Component;

public class PositionComponent implements Component
{
	public float x, y;
	
	public PositionComponent()
	{
		x = 0;
		y = 0;
	}
	
	public PositionComponent(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
}
