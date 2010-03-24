package jge3d;

import java.io.BufferedReader;
import java.io.IOException;

import org.lwjgl.opengl.GL11;

public class LevelParser {
	float cube_size = 1.0f;
	int row_length=0;
	int col_length=0;
	int map[][];
	private int objectlist;
	
	public LevelParser(BufferedReader ref) {
		loadlevel(ref);
		opengldrawtolist();
		cleanup();
	}
	
	private void cleanup() {

	}
	
	private void loadlevel(BufferedReader br) {
		String newline;
		int linecounter = 0;
		try {
			String type[] = new String [1];
			br.mark(8192);

			while(((newline = br.readLine()) != null)) {
				newline = newline.trim();
				if(newline.replaceAll("\t", "").length() > row_length) {
					row_length=newline.replaceAll("\t", "").length();
				}
				++col_length;
			}
			br.reset();
			
			map = new int[col_length][row_length];
			
			while (((newline = br.readLine()) != null)) {
				newline = newline.trim();
				type = newline.split("\\s+");
				if (newline.length() > 0) {
					for(int j=0;j<row_length;j++){
						map[linecounter][j] = Integer.parseInt(type[j]);
					}
				}
				linecounter++;
			}
			
		} catch (IOException e) {
			System.out.println("Failed to read file: " + br.toString());
			//System.exit(0);			
		} catch (NumberFormatException e) {
			System.out.println("Malformed level input (on line " + linecounter + "): " + br.toString() + "\r \r" + e.getMessage());
			//System.exit(0);
		}
	}
	
	
	public void opengldrawtolist() {
		
		this.objectlist = GL11.glGenLists(1);
		
		GL11.glNewList(objectlist,GL11.GL_COMPILE);
		for (int i=0;i<col_length;i++) {
			GL11.glPushMatrix();
			for(int j=0;j<row_length;j++){
				drawcube(map[i][j]);
				GL11.glTranslatef(1, 0, 0);
			}
			GL11.glPopMatrix();
			GL11.glTranslatef(0,-1,0);
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
	        GL11.glVertex3f(-1.0f, -1.0f, 1.0f); // Bottom Left Of The Texture and Quad
	        GL11.glVertex3f(1.0f, -1.0f, 1.0f); // Bottom Right Of The Texture and Quad
	        GL11.glVertex3f(1.0f, 1.0f, 1.0f); // Top Right Of The Texture and Quad
	        GL11.glVertex3f(-1.0f, 1.0f, 1.0f); // Top Left Of The Texture and Qua        
	        // Back Face
	        GL11.glVertex3f(-1.0f, -1.0f, -1.0f); // Bottom Right Of The Texture and Quad
	        GL11.glVertex3f(-1.0f, 1.0f, -1.0f); // Top Right Of The Texture and Quad
	        GL11.glVertex3f(1.0f, 1.0f, -1.0f); // Top Left Of The Texture and Quad
	        GL11.glVertex3f(1.0f, -1.0f, -1.0f); // Bottom Left Of The Texture and Quad
	        // Top Face
	        GL11.glVertex3f(-1.0f, 1.0f, -1.0f); // Top Left Of The Texture and Quad
	        GL11.glVertex3f(-1.0f, 1.0f, 1.0f); // Bottom Left Of The Texture and Quad
	        GL11.glVertex3f(1.0f, 1.0f, 1.0f); // Bottom Right Of The Texture and Quad
	        GL11.glVertex3f(1.0f, 1.0f, -1.0f); // Top Right Of The Texture and Quad
	        // Bottom Face
	        GL11.glVertex3f(-1.0f, -1.0f, -1.0f); // Top Right Of The Texture and Quad
	        GL11.glVertex3f(1.0f, -1.0f, -1.0f); // Top Left Of The Texture and Quad
	        GL11.glVertex3f(1.0f, -1.0f, 1.0f); // Bottom Left Of The Texture and Quad
	        GL11.glVertex3f(-1.0f, -1.0f, 1.0f); // Bottom Right Of The Texture and Quad
	        // Right face
	        GL11.glVertex3f(1.0f, -1.0f, -1.0f); // Bottom Right Of The Texture and Quad
	        GL11.glVertex3f(1.0f, 1.0f, -1.0f); // Top Right Of The Texture and Quad
	        GL11.glVertex3f(1.0f, 1.0f, 1.0f); // Top Left Of The Texture and Quad
	        GL11.glVertex3f(1.0f, -1.0f, 1.0f); // Bottom Left Of The Texture and Quad
	        // Left Face
	        GL11.glVertex3f(-1.0f, -1.0f, -1.0f); // Bottom Left Of The Texture and Quad
	        GL11.glVertex3f(-1.0f, -1.0f, 1.0f); // Bottom Right Of The Texture and Quad
	        GL11.glVertex3f(-1.0f, 1.0f, 1.0f); // Top Right Of The Texture and Quad
	        GL11.glVertex3f(-1.0f, 1.0f, -1.0f); // Top Left Of The Texture and Quad
		GL11.glEnd();
	}
	
	public void opengldraw() {
		GL11.glCallList(objectlist);
	}
	
}