package jge3d;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.vecmath.Vector3f;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class Level {
	float cube_size = 1.0f;
	int row_length=0;
	int col_length=0;
	private int objectlist;
	String nextline;
	char type;
	List<Entity> level_ents;
	Physics physics;
	Window window;
	
	public static String newline = System.getProperty("line.separator");
	Renderer render;
	
	public Level() {
		
	}
	
	public Level(BufferedReader ref, Physics _physics, Renderer _render, Window _window) throws IOException, LWJGLException {
		objectlist = GL11.glGenLists(1);
		physics = _physics;
		render = _render;
		window = _window;
		level_ents = new ArrayList<Entity>();
		loadlevel(ref);
		opengldrawtolist();
		cleanup();
	}
	
	public void setLevel(BufferedReader ref, Physics _physics, Renderer _render, Window _window) throws IOException, LWJGLException {
		objectlist = GL11.glGenLists(2);
		render = _render;
		window = _window;
		physics = _physics;
		level_ents = new ArrayList<Entity>();
		loadlevel(ref);
		opengldrawtolist();
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
						render.setTexture(splitString[1], splitString[2], splitString[3]);
						break;
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
						level_ents.add(new Entity(type,position,texture,true));break;
					default: System.out.print("FUCKSHIT level parsing error");break;
				}
			}
		}
	}
	

	public void opengldrawtolist() throws LWJGLException, FileNotFoundException, IOException {
		Display.makeCurrent();
		
		Vector3f position;

		GL11.glNewList(objectlist,GL11.GL_COMPILE);
			for(int i=0;i<level_ents.size();i++) {
				GL11.glPushMatrix();
				position=level_ents.get(i).getPosition();

				if(level_ents.get(i).getCollidable() == true) {
					physics.addLevelBlock(position.x,position.y,position.z,cube_size);
				}
				
				GL11.glTranslatef(position.x*cube_size,position.y*cube_size,-position.z*cube_size);
				render.drawcube(level_ents.get(i).getTextureName(), cube_size);
				GL11.glPopMatrix();
			
			}
		GL11.glEndList();
		
		Display.releaseContext();
	}
	
	public void opengladdtolist(Entity newEnt) throws LWJGLException, FileNotFoundException, IOException {
		Display.makeCurrent();
		
		//Variable to hold position of new level piece
		Vector3f position=newEnt.getPosition();

		//Replace old display list with new one containing new level object
		GL11.glPushMatrix();
		GL11.glNewList(objectlist,GL11.GL_COMPILE);
			for(int i=0;i<level_ents.size();i++) {
				GL11.glPushMatrix();
				position=level_ents.get(i).getPosition();
				
				GL11.glTranslatef(position.x*cube_size,position.y*cube_size,-position.z*cube_size);
				render.drawcube(level_ents.get(i).getTextureName(), cube_size);
				GL11.glPopMatrix();
			}
		GL11.glEndList();
		GL11.glPopMatrix();
		
		//Add physics just for the new object
		if(newEnt.getCollidable() == true) {
			physics.addLevelBlock(position.x,position.y,position.z,cube_size);
		}
		Display.releaseContext();
	}
	
	public void opengldraw() {
		GL11.glPushMatrix();
			GL11.glCallList(objectlist);
		GL11.glPopMatrix();
	}
	
	public int getHeight() {
		return col_length;
	}
	
	public int getWidth() {
		return row_length;
	}
	
	public Entity addEntity(Entity ent) {
		level_ents.add(ent);
		return ent;
	}
	
	public void load() throws IOException, LWJGLException {
		final JFileChooser fc_level = new JFileChooser("lib/Levels/");
		fc_level.showOpenDialog(window.getWindow());
		BufferedReader levelfile = new BufferedReader(new FileReader(fc_level.getSelectedFile()));
		loadlevel(levelfile);
		opengldrawtolist();
		cleanup();
	}
	
	public void save() throws IOException {
		//Open a file
		final JFileChooser fc_level = new JFileChooser("lib/Levels/");
		fc_level.showOpenDialog(window.getWindow());
		BufferedWriter bw = new BufferedWriter(new FileWriter(fc_level.getSelectedFile()));
		
		//Create header consisting of textures and stuff
		bw.write("header\n");
		for(String key: render.getHash().keySet())
			bw.write("\t" + render.getHash().get(key) + "\n");
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

}