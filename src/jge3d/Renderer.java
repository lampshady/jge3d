package jge3d;

import org.lwjgl.opengl.GL11;

class Renderer {
	public Renderer() {
		
	}
	
	public void drawcube(int type, float cube_size) {
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
	
}