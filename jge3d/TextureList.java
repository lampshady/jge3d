package jge3d;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.newdawn.slick.opengl.Texture;

public class TextureList {
	private HashMap<String, TextureData> textures;
	
	public TextureList() {
		textures = new HashMap<String, TextureData>();
	}
	
	public Texture getByName(String key) {
		return textures.get(key).getTexture();
	}
	
	public void set(String group, String name, String path) throws FileNotFoundException, IOException {
		textures.put(name, new TextureData(group,name,path));
	}
	
	public int length() {
		return textures.size();
	}
	
	public HashMap<String, TextureData> getHash() {
		return textures;
	}
	
	public void clearList() {
		textures.clear();
	}
	
	public boolean hasKey(String key) {
		return textures.containsKey(key);
	}
	
}
