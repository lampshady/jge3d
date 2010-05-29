package jge3d;

import java.util.HashMap;
import java.util.Map;

public class TextureAnimation {
	private HashMap<String, TextureList> animations;
	
	//Queue for texture loading
	String texture_load_string = "";
	boolean textures_changed;
	
	public TextureAnimation() {
		animations = new HashMap<String, TextureList>();
	}
	
	public void getNextFrame() {
		for(Map.Entry<String,TextureList> entry: animations.entrySet()) {
			//Really busted; don't trust it
			entry.getValue().getByName("");
		}
	}
}
