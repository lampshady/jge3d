package jge3d;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;

import jge3d.GUI.TextureView;
import jge3d.GUI.Window;
import jge3d.render.Renderer;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

public class Level {
	private int row_length=0;
	private int col_length=0;
	private String nextline;
	//private Window window;
	//private Renderer render;
	//private EntityList entity;

	//private static String newline = System.getProperty("line.separator");
	
	public Level() {
		
	}

	public void setLevel() throws IOException, LWJGLException {
		BufferedReader levelfile;
		levelfile = new BufferedReader(new FileReader("lib/Levels/newformattest.map"));
		loadlevel(levelfile);
		Renderer.getInstance().makeLevelList();
		cleanup();
	}
	
	private void cleanup() {

	}

	private void loadlevel(BufferedReader br) throws IOException, LWJGLException {
		while (((nextline = br.readLine()) != null)) {
			if(nextline.compareToIgnoreCase("header") == 0) 
				parseHeader(br);
			else if (nextline.compareToIgnoreCase("level") == 0)
				parseLevel(br);
		}
	}
	
	private void parseHeader(BufferedReader br) throws IOException, LWJGLException {
		String nextline;
		String[] splitString;
		String type;
		while ((nextline = br.readLine()).compareToIgnoreCase("/header") != 0) {
			if(nextline.length() > 0) {
				nextline = nextline.trim();
				splitString = nextline.split(";");
				type = (nextline.substring(0, 1));
				if(type.equals("T")) {
					if(TextureList.getInstance().hasKey(splitString[1]) == false) {
						TextureList.getInstance().set(splitString[1], splitString[2], splitString[3]);
						
						TextureView.getInstance().insertTexture(splitString[2]);
					} else {
						//this is for when we implement texture groups
					}
				} else if (type.equals("L")) {
					//render.setLight();
				} else {
					System.out.print("FUCKSHIT level parsing error");break;
				}
			}
		}
	}

	private void parseLevel(BufferedReader br) throws NumberFormatException, IOException {
		String nextline;
		String[] splitString;
		String key;
		String value;
		Entity current_entity;
		
		while ((nextline = br.readLine()).compareToIgnoreCase("/level") != 0) {
			if(nextline.length() > 0) {
				nextline = nextline.trim();
				splitString = nextline.split("=");
				key = splitString[0];
				value = splitString[1];
				
				if(key.equals("Entity")) {
					current_entity = EntityList.getInstance().addEntity(new Entity(value));
					while ((nextline = br.readLine()).compareToIgnoreCase("/Entity") != 0) {
						nextline = nextline.trim();
						splitString = nextline.split("=");
						key = splitString[0];
						value = splitString[1];
						//System.out.print(key + "=" + value + "\n");
						current_entity.set(key, value);
					}
				}
				else {
					System.out.print("FUCKSHIT level parsing error");
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

	public void load() throws IOException, LWJGLException {
		final JFileChooser fc_level = new JFileChooser("lib/Levels/");
		fc_level.showOpenDialog(Window.getInstance());
		BufferedReader levelfile = new BufferedReader(new FileReader(fc_level.getSelectedFile()));
		//BufferedReader levelfile = new BufferedReader(new FileReader("lib/Levels/temp.map"));
		
		EntityList.getInstance().clear();
		TextureView.getInstance().clear();

		loadlevel(levelfile);
		
		Display.makeCurrent();
		Renderer.getInstance().makeLevelList();
		Display.releaseContext();
		
		cleanup();
	}
	
	public void save() throws IOException {
		//Open a file
		final JFileChooser fc_level = new JFileChooser("lib/Levels/");
		fc_level.showOpenDialog(Window.getInstance());
		BufferedWriter bw = new BufferedWriter(new FileWriter(fc_level.getSelectedFile()));
		
		//Create header consisting of textures and stuff
		bw.write("header\n");
		for(String key: TextureList.getInstance().getHash().keySet())
			bw.write("\t" + TextureList.getInstance().getHash().get(key) + "\n");
		bw.write("/header\n");
		bw.newLine();
		
		//Create level body defining block positions
		bw.write(("level\n"));
		for(int i=0; i<EntityList.getInstance().getListSize();i++)
			bw.write(EntityList.getInstance().getByIndex(i).toString() + "\n");
		bw.write(("/level\n"));
		
		//Close buffers
		bw.flush();
		bw.close();
	}
}