package jge3d;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	List<TextureList> textures;
	public static String newline = System.getProperty("line.separator");
	Renderer render = new Renderer();
	
	public Level(BufferedReader ref, Physics physics) throws IOException, LWJGLException {
		level_ents = new ArrayList<Entity>();
		textures =new ArrayList<TextureList>();
		loadlevel(ref);
		opengldrawtolist(physics);
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
						textures.add(new TextureList(splitString[1],splitString[2],splitString[3]));break;
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
						level_ents.add(new Entity(type,position,texture, true));break;
					default: System.out.print("FUCKSHIT level parsing error");break;
				}
			}
		}
	}
	

	public void opengldrawtolist(Physics physics) throws LWJGLException, FileNotFoundException, IOException {
		Display.makeCurrent();
		
		Vector3f position;
		GL11.glDeleteLists(objectlist, 1);
		objectlist = GL11.glGenLists(1);

		GL11.glNewList(objectlist,GL11.GL_COMPILE);
			for(int i=0;i<level_ents.size();i++) {
				GL11.glPushMatrix();
				position=level_ents.get(i).getPosition();

				if(level_ents.get(i).getCollidable() == true) {
					physics.addLevelBlock(position.x,position.y,position.z,cube_size);
				}
				
				GL11.glTranslatef(position.x*cube_size,position.y*cube_size,position.z*cube_size);
				render.drawcube(level_ents.get(i).getTexture(), cube_size);
				GL11.glPopMatrix();
			
			}
		GL11.glEndList();
		
		Display.releaseContext();
	}
	
	public void chooseALevel() {
		//final JFileChooser fc_level = new JFileChooser("lib/Levels/");
		//fc_level.showOpenDialog(window);
		//BufferedReader levelfile = new BufferedReader(new FileReader(fc_level.getSelectedFile()));
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
	
	public void addEntity(Entity ent) {
		level_ents.add(ent);
	}
	
	public void save() {
			
	}
}