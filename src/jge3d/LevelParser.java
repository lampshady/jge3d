package jge3d;

import java.io.BufferedReader;
import java.io.IOException;

import org.lwjgl.opengl.GL11;

public class LevelParser {
	float cube_size = 1.0f;
	int row_length=0;
	int col_length=0;
	int max_col_length=0;
	int layer=0;
	int layer_count=0;
	int map[][][];
	private int objectlist;
	public static String newline = System.getProperty("line.separator");
	
	public LevelParser(BufferedReader ref) {
		loadlevel(ref);
		opengldrawtolist();
		cleanup();
	}
	
	private void cleanup() {

	}
	
	private void loadlevel(BufferedReader br) {
		String nextline;
		int linecounter = 0;
		try {
			String type[] = new String [1];
			br.mark(8192);

			while(((nextline = br.readLine()) != null)) {
				nextline = nextline.trim();
				if(nextline.length() != 0) {
					if(nextline.charAt(0) == 'l') {
						++layer_count;
						if(col_length > max_col_length) {
							max_col_length = col_length;
							col_length = 0;
						}
					}
					if(nextline.replaceAll("\t", "").length() > row_length) {
						row_length=nextline.replaceAll("\t", "").length();
					}
					++col_length;
				}
			}
			br.reset();
			
			map = new int[max_col_length][row_length][layer_count];
			
			while (((nextline = br.readLine()) != null)) {
				nextline = nextline.trim();
				//if(newline.charAt(0) == 'l')
				//	layer=newline.charAt(1);
				type = nextline.split("\\s+");
				if (nextline.length() > 0) {
					if(nextline.charAt(0) == 'l') {
						layer=Integer.parseInt(String.valueOf(nextline.charAt(1)));
						nextline=br.readLine().trim();
						type = nextline.split("\\s+");
						linecounter=0;
					}
					for(int j=0;j<row_length;j++){
						try {
							map[linecounter][j][layer] = Integer.parseInt(type[j]);
						} catch (Exception e) {
							System.out.print(e);
						}
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
		for(int k=0;k<layer_count;k++) {
			GL11.glPushMatrix();
			for (int i=0;i<max_col_length-1;i++) {
				GL11.glPushMatrix();
				for(int j=0;j<row_length;j++){
					if(map[i][j][k] != 9)
						drawcube(map[i][j][k]);
					GL11.glTranslatef(1, 0, 0);
				}
				GL11.glPopMatrix();
				GL11.glTranslatef(0,-1,0);
			}
			GL11.glPopMatrix();
			GL11.glTranslatef(0, 0, -1);
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
	        GL11.glVertex3f(-0.5f, -0.5f, 0.5f); // Bottom Left Of The Texture and Quad
	        GL11.glVertex3f(0.5f, -0.5f, 0.5f); // Bottom Right Of The Texture and Quad
	        GL11.glVertex3f(0.5f, 0.5f, 0.5f); // Top Right Of The Texture and Quad
	        GL11.glVertex3f(-0.5f, 0.5f, 0.5f); // Top Left Of The Texture and Qua        
	        // Back Face
	        GL11.glVertex3f(-0.5f, -0.5f, -0.5f); // Bottom Right Of The Texture and Quad
	        GL11.glVertex3f(-0.5f, 0.5f, -0.5f); // Top Right Of The Texture and Quad
	        GL11.glVertex3f(0.5f, 0.5f, -0.5f); // Top Left Of The Texture and Quad
	        GL11.glVertex3f(0.5f, -0.5f, -0.5f); // Bottom Left Of The Texture and Quad
	        // Top Face
	        GL11.glVertex3f(-0.5f, 0.5f, -0.5f); // Top Left Of The Texture and Quad
	        GL11.glVertex3f(-0.5f, 0.5f, 0.5f); // Bottom Left Of The Texture and Quad
	        GL11.glVertex3f(0.5f, 0.5f, 0.5f); // Bottom Right Of The Texture and Quad
	        GL11.glVertex3f(0.5f, 0.5f, -0.5f); // Top Right Of The Texture and Quad
	        // Bottom Face
	        GL11.glVertex3f(-0.5f, -0.5f, -0.5f); // Top Right Of The Texture and Quad
	        GL11.glVertex3f(0.5f, -0.5f, -0.5f); // Top Left Of The Texture and Quad
	        GL11.glVertex3f(0.5f, -0.5f, 0.5f); // Bottom Left Of The Texture and Quad
	        GL11.glVertex3f(-0.5f, -0.5f, 0.5f); // Bottom Right Of The Texture and Quad
	        // Right face
	        GL11.glVertex3f(0.5f, -0.5f, -0.5f); // Bottom Right Of The Texture and Quad
	        GL11.glVertex3f(0.5f, 0.5f, -0.5f); // Top Right Of The Texture and Quad
	        GL11.glVertex3f(0.5f, 0.5f, 0.5f); // Top Left Of The Texture and Quad
	        GL11.glVertex3f(0.5f, -0.5f, 0.5f); // Bottom Left Of The Texture and Quad
	        // Left Face
	        GL11.glVertex3f(-0.5f, -0.5f, -0.5f); // Bottom Left Of The Texture and Quad
	        GL11.glVertex3f(-0.5f, -0.5f, 0.5f); // Bottom Right Of The Texture and Quad
	        GL11.glVertex3f(-0.5f, 0.5f, 0.5f); // Top Right Of The Texture and Quad
	        GL11.glVertex3f(-0.5f, 0.5f, -0.5f); // Top Left Of The Texture and Quad
		GL11.glEnd();
	}
	
	public void opengldraw() {
		GL11.glCallList(objectlist);
	}
	
}