package jge3d;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class TextureData {
	//private String format;
	private String group;
	private String name;
	private Texture texture;
	private String path;
	
	public TextureData(String _group, String _name, String _path) throws FileNotFoundException, IOException, LWJGLException {
		//We should probably try to get rid of this makeCurrent()
		Display.makeCurrent();
		
		texture = TextureLoader.getTexture("png", new FileInputStream(_path));

		name = _name;
		group = _group;
		path = _path;
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public String getName() {
		return name;
	}
	
	public String getGroup() {
		return group;
	}
	
	public String getPath() {
		return path;
	}
	
	public String toString() {
		return "T;" + group + ";" + name + ";" + path;
	}
}
