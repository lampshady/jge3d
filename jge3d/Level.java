package jge3d;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3f;

import org.lwjgl.opengl.GL11;


public class Level {
	float cube_size = 1f;
	int row_length=0;
	int col_length=0;
	int max_col_length=0;
	int layer=0;
	int layer_count=0;
	int linecounter = 0;
	int map[][][];
	private int objectlist;
	String nextline;
	char type;
	List<Entity> level_ents;
	public static String newline = System.getProperty("line.separator");
	
	public Level(BufferedReader ref, Physics physics) throws IOException {
		level_ents = new ArrayList<Entity>();
		loadlevel(ref);
		opengldrawtolist(physics);
		cleanup();
	}
	
	private void cleanup() {

	}

	private void loadlevel(BufferedReader br) throws IOException {
		while (((nextline = br.readLine()) != null)) {
			if(nextline == "header") 
				parseHeader(br);
			else if (nextline == "level")
				parseLevel(br);
		}
		map = new int[row_length][col_length][layer_count];
	}
	
	private void parseHeader(BufferedReader br) throws IOException {
		while ((nextline = br.readLine()) != "/header") {

		}
	}
	
	private void parseLevel(BufferedReader br) throws NumberFormatException, IOException {
		String nextline;
		String[] splitString;
		String texture;
		String[] split_position = new String[3];
		
		while (((nextline = br.readLine()) != null)) {
			if(nextline.length() > 0) {
				nextline = nextline.trim();
				//if(newline.charAt(0) == 'l')
				//	layer=newline.charAt(1);
				type = (nextline.charAt(0));
				splitString = nextline.split(";");
				
				for(int i=0; i<splitString.length;++i) {
					
					switch(type) {
						case 'L':
							split_position = splitString[i].split(",");
							texture = splitString[2];
							Vector3f position = new Vector3f(
									Integer.parseInt(split_position[0]),
									Integer.parseInt(split_position[1]),
									Integer.parseInt(split_position[2])
							);
							level_ents.add(new Entity(type,position,texture));break;
						default: System.out.print("FUCKSHIT level parsing error");break;
					}
					
				}
			}
			linecounter++;
		}
	}
	

	public void opengldrawtolist(Physics physics) {
		
		this.objectlist = GL11.glGenLists(1);
		
		GL11.glNewList(objectlist,GL11.GL_COMPILE);
		for(int k=0;k<layer_count;k++) {
			GL11.glPushMatrix();
			for (int j=0;j<max_col_length-1;j++) {
				GL11.glPushMatrix();
				for(int i=0;i<row_length;i++){
					if(map[i][j][k] != 9) {
						drawcube(map[i][j][k]);
						physics.addLevelBlock(i,-j,-k,cube_size);
					}
					GL11.glTranslatef(2*cube_size, 0, 0);
				}
				GL11.glPopMatrix();
				GL11.glTranslatef(0,-2*cube_size,0);
			}
			GL11.glPopMatrix();
			GL11.glTranslatef(0, 0, -2*cube_size);
		}
		
		GL11.glEndList();
	}
	
	
	private void drawcube(int type) {
		//GL11.glTranslatef(x*cube_size, y*cube_size, z*cube_size);
		GL11.glBegin(GL11.GL_QUADS);
			switch(type) {
				case 0: GL11.glColor3f(0.0f,0.0f,0.0f);break;
				case 1: GL11.glColor3f(1.0f,0.0f,0.0f);break;
				case 2: GL11.glColor3f(0.0f,1.0f,0.0f);break;
				case 3: GL11.glColor3f(0.0f,0.0f,1.0f);break;
				case 4: GL11.glColor3f(1.0f,1.0f,1.0f);break;
				default: GL11.glColor3f(1.0f,1.0f,1.0f);
						 System.out.print("ohshit...");
						 break;
			}
	        // Front Face
	        GL11.glVertex3f(-cube_size, -cube_size, cube_size); // Bottom Left Of The Texture and Quad
	        GL11.glVertex3f(cube_size, -cube_size, cube_size); // Bottom Right Of The Texture and Quad
	        GL11.glVertex3f(cube_size, cube_size, cube_size); // Top Right Of The Texture and Quad
	        GL11.glVertex3f(-cube_size, cube_size, cube_size); // Top Left Of The Texture and Qua        
	        // Back Face
	        GL11.glVertex3f(-cube_size, -cube_size, -cube_size); // Bottom Right Of The Texture and Quad
	        GL11.glVertex3f(-cube_size, cube_size, -cube_size); // Top Right Of The Texture and Quad
	        GL11.glVertex3f(cube_size, cube_size, -cube_size); // Top Left Of The Texture and Quad
	        GL11.glVertex3f(cube_size, -cube_size, -cube_size); // Bottom Left Of The Texture and Quad
	        // Top Face
	        GL11.glVertex3f(-cube_size, cube_size, -cube_size); // Top Left Of The Texture and Quad
	        GL11.glVertex3f(-cube_size, cube_size, cube_size); // Bottom Left Of The Texture and Quad
	        GL11.glVertex3f(cube_size, cube_size, cube_size); // Bottom Right Of The Texture and Quad
	        GL11.glVertex3f(cube_size, cube_size, -cube_size); // Top Right Of The Texture and Quad
	        // Bottom Face
	        GL11.glVertex3f(-cube_size, -cube_size, -cube_size); // Top Right Of The Texture and Quad
	        GL11.glVertex3f(cube_size, -cube_size, -cube_size); // Top Left Of The Texture and Quad
	        GL11.glVertex3f(cube_size, -cube_size, cube_size); // Bottom Left Of The Texture and Quad
	        GL11.glVertex3f(-cube_size, -cube_size, cube_size); // Bottom Right Of The Texture and Quad
	        // Right face
	        GL11.glVertex3f(cube_size, -cube_size, -cube_size); // Bottom Right Of The Texture and Quad
	        GL11.glVertex3f(cube_size, cube_size, -cube_size); // Top Right Of The Texture and Quad
	        GL11.glVertex3f(cube_size, cube_size, cube_size); // Top Left Of The Texture and Quad
	        GL11.glVertex3f(cube_size, -cube_size, cube_size); // Bottom Left Of The Texture and Quad
	        // Left Face
	        GL11.glVertex3f(-cube_size, -cube_size, -cube_size); // Bottom Left Of The Texture and Quad
	        GL11.glVertex3f(-cube_size, -cube_size, cube_size); // Bottom Right Of The Texture and Quad
	        GL11.glVertex3f(-cube_size, cube_size, cube_size); // Top Right Of The Texture and Quad
	        GL11.glVertex3f(-cube_size, cube_size, -cube_size); // Top Left Of The Texture and Quad
		GL11.glEnd();
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
		return max_col_length;
	}
	
	public int getWidth() {
		return row_length;
	}
}