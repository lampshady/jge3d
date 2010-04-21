package jge3d;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.vecmath.Vector3f;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

public class Level {
	private float cube_size = 1.0f;
	private int row_length=0;
	private int col_length=0;
	private String nextline;
	private char type;
	private List<Entity> level_ents;
	private Window window;
	private Renderer render;
	private TextureList texture;
	private Entity latest_ent;
	private boolean level_changed=false;
	//private static String newline = System.getProperty("line.separator");
	
	public Level(TextureList _texture) {
		texture = _texture;
	}

	public void setLevel(Renderer _render, Window _window) throws IOException, LWJGLException {
		render = _render;
		window = _window;
		
		level_ents = new ArrayList<Entity>();
		BufferedReader levelfile;
		levelfile = new BufferedReader(new FileReader("lib/Levels/newParserTest.map"));
		loadlevel(levelfile);
		render.makeLevelList();
		cleanup();
	}
	
	private void cleanup() {

	}

	private void loadlevel(BufferedReader br) throws IOException {
		while (((nextline = br.readLine()) != null)) {
			if(nextline.compareToIgnoreCase("header") == 0) 
				parseHeader(br);
			else if (nextline.compareToIgnoreCase("level") == 0)
				parseLevel(br);
		}
	}
	
	private void parseHeader(BufferedReader br) throws IOException {
		String nextline;
		String[] splitString;
		while ((nextline = br.readLine()).compareToIgnoreCase("/header") != 0) {
			if(nextline.length() > 0) {
				nextline = nextline.trim();
				type = (nextline.charAt(0));
				splitString = nextline.split(";");
				
				switch(type) {
					case 'T':
						if(texture.hasKey(splitString[2]) == false)
							texture.set(splitString[1], splitString[2], splitString[3]);
						break;
					case 'L':
							//render.setLight();
					default: System.out.print("FUCKSHIT level parsing error");break;
				}
			}
		}
	}
	
	private void parseLevel(BufferedReader br) throws NumberFormatException, IOException {
		String nextline;
		String[] splitString;
		String texture;
		String[] split_position = new String[3];
		
		while ((nextline = br.readLine()).compareToIgnoreCase("/level") != 0) {
			if(nextline.length() > 0) {
				nextline = nextline.trim();
				type = (nextline.charAt(0));
				splitString = nextline.split(";");
				
				switch(type) {
					case 'L':
						split_position = splitString[1].split(",");
						
						texture = splitString[2];
												
						Vector3f position = new Vector3f(
								Integer.parseInt(split_position[0]),
								Integer.parseInt(split_position[1]),
								Integer.parseInt(split_position[2])
						);
						level_ents.add(new Entity(type,position,texture,true));
						break;
					default: System.out.print("FUCKSHIT level parsing error");
					break;
				}
			}
		}
	}
	
	public int getHeight() {
		return col_length;
	}
	
	public int getWidth() {
		return row_length;
	}
	
	public void addEntity(Entity ent) {
		level_ents.add(ent);
		latest_ent = ent;
		level_changed = true;
	}
	
	public void load() throws IOException, LWJGLException {
		final JFileChooser fc_level = new JFileChooser("lib/Levels/");
		fc_level.showOpenDialog(window.getWindow());
		BufferedReader levelfile = new BufferedReader(new FileReader(fc_level.getSelectedFile()));
		//BufferedReader levelfile = new BufferedReader(new FileReader("lib/Levels/temp.map"));
		level_ents.clear();
		loadlevel(levelfile);
		
		Display.makeCurrent();
		render.makeLevelList();
		Display.releaseContext();
		
		cleanup();
	}
	
	public void save() throws IOException {
		//Open a file
		final JFileChooser fc_level = new JFileChooser("lib/Levels/");
		fc_level.showOpenDialog(window.getWindow());
		BufferedWriter bw = new BufferedWriter(new FileWriter(fc_level.getSelectedFile()));
		
		//Create header consisting of textures and stuff
		bw.write("header\n");
		for(String key: texture.getHash().keySet())
			bw.write("\t" + texture.getHash().get(key) + "\n");
		bw.write("/header\n");
		bw.newLine();
		
		//Create level body defining block positions
		bw.write(("level\n"));
		for(int i=0; i<level_ents.size();i++)
			bw.write("\t" + level_ents.get(i).toString() + "\n");
		bw.write(("/level\n"));
		
		//Close buffers
		bw.flush();
		bw.close();
	}
	
	public int getLevelSize() {
		return level_ents.size();
	}
	
	public Vector3f getLevelEntityPosition(int index) {
		return level_ents.get(index).getPosition();
	}
	
	public String getLevelEntityTextureName(int index) {
		return level_ents.get(index).getTextureName();
	}
	
	public float getLevelEntitySize() {
		return cube_size;
	}
	
	public boolean getLevelEntityCollidable(int index) {
		return level_ents.get(index).getCollidable();
	}
	
	public boolean getLevelChanged() {
		if(level_changed) {
			level_changed=false;
			return true;
		} else {
			return level_changed;
		}
	}
	
	public Entity getLatestEntity() {
		return latest_ent;
	}
}