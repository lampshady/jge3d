package jge3d;

import java.io.FileInputStream;

import org.newdawn.slick.opengl.TextureLoader;

public class Texture {
	private Texture texture;
	
	public Texture(String path) {
		texture = TextureLoader.getTexture("PNG", new FileInputStream(path));
	}
}
