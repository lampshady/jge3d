package jge3d;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.lwjgl.LWJGLException;
import org.newdawn.slick.opengl.Texture;

public class TextureList {
	private static TextureList uniqueInstance = new TextureList();
	
	private HashMap<String, TextureData> textures;
	
	//Queue for texture loading
	String texture_load_string = "";
	boolean textures_changed;
	
	private TextureList() {
		textures = new HashMap<String, TextureData>();
	}
	
	public static TextureList getInstance()
	{
		return uniqueInstance;
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
		texture_load_string = load;
		textures_changed = true;
	}
	
	public void loadQueuedTexture() throws FileNotFoundException, IOException, LWJGLException {
		String[] split_string = texture_load_string.split("\\,");
		textures.put(split_string[1],
				new TextureData(
					split_string[0],
					split_string[1],
					split_string[2]
			));
	}
	
	public boolean hasChanged() {
		if (textures_changed){
			textures_changed = false; 
			return true;
		} else {
			return false;
		}
	}
}
