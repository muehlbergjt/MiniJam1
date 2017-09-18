package dojodev.map;

import java.util.ArrayList;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import dojodev.rogueshooter.RSEngine;

public abstract class GameMap 
{
	public int width, height;
	public Entity[][] tiles;
	public int[][] tileIDs;
	
	protected RSEngine eng;
	
	public Texture spriteSheet;
	public TextureRegion[] tileSprites;
	public int tileWidth, tileHeight;
}
