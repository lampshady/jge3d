package jge3d;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class TextureList {
	private String format;
	private String group;
	private String name;
	private Texture texture;
	
	public TextureList(String path, String _name) throws FileNotFoundException, IOException {
		texture = TextureLoader.getTexture("PNG", new FileInputStream(path));
		name = _name;
	}
	
	public Texture getTexture(String name) {
		return texture;
	}
}
