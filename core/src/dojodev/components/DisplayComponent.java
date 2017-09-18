package dojodev.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class DisplayComponent implements Component
{
	public TextureRegion texRegion;
	public float rotation;
	
	public DisplayComponent()
	{
		texRegion = new TextureRegion();
		rotation = 0;
	}
	
	public DisplayComponent(Texture t)
	{
		texRegion = new TextureRegion(t);
		rotation = 0;
	}
	
	public DisplayComponent(TextureRegion r)
	{
		texRegion = r;
		rotation = 0;
	}
	
	public TextureRegion getTexture()
	{
		return texRegion;
	}
}
