package jge3d;

import java.io.BufferedReader;
import java.io.IOException;

import org.lwjgl.opengl.GL11;

public class LevelParser {
	float cube_size = 10.0f;
	int row_length=23;
	int col_length=14;
	int map[][] = new int[col_length][row_length];
	
	public LevelParser(BufferedReader ref) {
		loadobject(ref);
		//opengldrawtolist();
		cleanup();
	}
	
	private void cleanup() {

	}
	
	private void loadobject(BufferedReader br) {
		int linecounter = 0;
		try {
			
			String type[] = new String [1];
			String newline;
			newline = br.readLine().trim();
			row_length = Integer.parseInt(newline);
			newline = br.readLine().trim();
			col_length = Integer.parseInt(newline);
			newline = br.readLine();
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
		
		//this.objectlist = GL11.glGenLists(1);
		
		//GL11.glNewList(objectlist,GL11.GL_COMPILE);
		for (int i=0;i<col_length;i++) {
			for(int j=0;j<row_length;j++){
				drawcube((float)i*cube_size,(float)j*cube_size,1.0f,map[i][j]);
			}
		}
		//GL11.glEndList();
	}
	
	private void drawcube(float x, float y, float z, int type) {
		GL11.glBegin(GL11.GL_QUADS);
			switch(type) {
				case 0: GL11.glColor3f(0.0f,0.0f,0.0f);break;
				case 1: GL11.glColor3f(1.0f,0.0f,0.0f);break;
				case 2: GL11.glColor3f(0.0f,1.0f,0.0f);break;
				case 3: GL11.glColor3f(0.0f,0.0f,1.0f);break;
				case 4: GL11.glColor3f(1.0f,1.0f,1.0f);break;
				default: System.out.print("ohshit...");break;
			}

	        GL11.glVertex3f(x*cube_size,y*cube_size,z*cube_size);		// Top Right Of The Quad (Top)
	        GL11.glVertex3f(x,y*cube_size,z*cube_size);					// Top Left Of The Quad (Top)
	        GL11.glVertex3f(x,y*cube_size,z);							// Bottom Left Of The Quad (Top)
	        GL11.glVertex3f(x*cube_size,y*cube_size, z);				// Bottom Right Of The Quad (Top)

	        GL11.glVertex3f(x*cube_size,y,z);							// Top Right Of The Quad (Bottom)
	        GL11.glVertex3f(x,y,z);										// Top Left Of The Quad (Bottom)
	        GL11.glVertex3f(x,y,z*cube_size);							// Bottom Left Of The Quad (Bottom)
	        GL11.glVertex3f(x*cube_size,y,z*cube_size);					// Bottom Right Of The Quad (Bottom)
	        
	        GL11.glVertex3f(x*cube_size,y*cube_size, z);				// Top Right Of The Quad (Front)
	        GL11.glVertex3f(x, y*cube_size, z);							// Top Left Of The Quad (Front)
	        GL11.glVertex3f(x,y,z);										// Bottom Left Of The Quad (Front)
	        GL11.glVertex3f(x*cube_size,y, z);							// Bottom Right Of The Quad (Front)

	        GL11.glVertex3f(x*cube_size,y*cube_size,z*cube_size);		// Top Right Of The Quad (Back)
	        GL11.glVertex3f(x,y*cube_size,z*cube_size);					// Top Left Of The Quad (Back)
	        GL11.glVertex3f(x,y,z*cube_size);							// Bottom Left Of The Quad (Back)
	        GL11.glVertex3f(x*cube_size,y,z*cube_size);					// Bottom Right Of The Quad (Back)

	        GL11.glVertex3f(x,y*cube_size,z);							// Top Right Of The Quad (Left)
	        GL11.glVertex3f(x,y*cube_size,z*cube_size);					// Top Left Of The Quad (Left)
	        GL11.glVertex3f(x,y,z*cube_size);							// Bottom Left Of The Quad (Left)
	        GL11.glVertex3f(x,y,z);							// Bottom Right Of The Quad (Left)

	        GL11.glVertex3f(x*cube_size,y*cube_size,z*cube_size);		// Top Right Of The Quad (Right)
	        GL11.glVertex3f(x*cube_size,y*cube_size,z);					// Top Left Of The Quad (Right)
	        GL11.glVertex3f(x*cube_size,y,z);							// Bottom Left Of The Quad (Right)
	        GL11.glVertex3f(x*cube_size,y,z*cube_size);					// Bottom Right Of The Quad (Right)
		GL11.glEnd();
	}
	
	public void opengldraw() {
		//GL11.glCallList(objectlist);
		for (int i=0;i<col_length;i++) {
			for(int j=0;j<row_length;j++){
				drawcube((float)i*cube_size,(float)j*cube_size,1.0f,map[i][j]);
			}
		}
	}
	
}