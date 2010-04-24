package jge3d;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.lwjgl.LWJGLException;
import org.newdawn.slick.opengl.Texture;

public class TextureList {
	private HashMap<String, TextureData> textures;
	
	//Queue for texture loading
	String texture_load_string = "";
	boolean textures_changed;
	
	public TextureList() {
		textures = new HashMap<String, TextureData>();
	}
	
	public Texture getByName(String key) {
		return textures.get(key).getTexture();
	}
	
	public void set(String group, String name, String path) throws FileNotFoundException, IOException, LWJGLException {
		textures.put(name, new TextureData(group,name,path));
	}
	
	public TextureData getDataByName(String key) {
		return textures.get(key);
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
	
	public void queueTextureForLoading(String load) {
		String[] split_texture = load.split("\\,");;
        String filename = load;
        String filename_noextension = filename.split("\\.")[0];
		textures.set(
				"groupnotimplemented",
				filename_noextension,
				"lib/Textures/" + filename
			);
		texture_load_string = load;
	}
	
	public boolean getLoadTexture() {
		if (textures_changed){
			textures_changed = false; 
			return true;
		} else {
			return false;
		}
	}
}
