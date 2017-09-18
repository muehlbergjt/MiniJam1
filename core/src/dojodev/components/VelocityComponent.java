package dojodev.components;

import com.badlogic.ashley.core.Component;

public class VelocityComponent implements Component
{
	public float vx, vy;
	
	public VelocityComponent()
	{
		vx = 0;
		vy = 0;
	}
	
	public VelocityComponent(float xVel, float yVel)
	{
		vx = xVel;
		vy = yVel;
	}
}
