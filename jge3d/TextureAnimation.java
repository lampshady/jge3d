package jge3d;

import java.util.HashMap;

public class TextureAnimation {
	private HashMap<String, TextureList> animations;
	
	//Queue for texture loading
	String texture_load_string = "";
	boolean textures_changed;
	
	public TextureAnimation() {
		animations = new HashMap<String, TextureList>();
	}
}
