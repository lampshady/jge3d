package jge3d;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.newdawn.slick.opengl.Texture;

public class TextureAnimation extends TextureData{
	private ArrayList<TextureData> animations;
	
	int currentFrame;
	
	public TextureAnimation(String _group, String _name) throws FileNotFoundException, IOException, LWJGLException {
		super();
		animations = new ArrayList<TextureData>();
		setName(_name);
		setGroup(_group);
	}
	 
	public void addFrames(ArrayList<TextureData> textureFrames)
	{
		for(int i = 0; i < textureFrames.size(); i++)
		{
			animations.add(textureFrames.get(i));
		}
	}
	
	public void setFrames(ArrayList<TextureData> textureFrames)
	{
		animations.removeAll(animations);
		animations = textureFrames;
	}
	
	@Override
	public Texture getTexture() {
		currentFrame++;
		return animations.get(currentFrame).getTexture();
	}
}
