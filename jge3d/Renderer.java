package jge3d;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

class Renderer {
	public Renderer() {
		
	}
	
	public void drawcube(Texture texture, float cube_size) {
		//GL11.glTranslatef(x*cube_size, y*cube_size, z*cube_size);
		
		//Allow a colored cube to be drawn
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glColor3f(0.0f,1.0f,0.0f);
		//GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		//GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		GL11.glBegin(GL11.GL_QUADS);
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
	
	public void draw(Level level, Editor editor, Physics physics, Camera camera) throws LWJGLException
	{
		//Make sure that the screen is active
		Display.makeCurrent();

		// render using OpenGL 
	    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // Clear The Screen And The Depth Buffer
		GL11.glLoadIdentity();
		
		//Place the camera
		GLU.gluLookAt(
				camera.getPositionX()	, camera.getPositionY()	,	camera.getPositionZ(),
				camera.getFocusX()		, camera.getFocusY()	,	camera.getFocusZ(),
				camera.up_vector.x		, camera.up_vector.y	,	camera.up_vector.z
		);
		
        //render level
        level.opengldraw();
        
        //render physics
        physics.render();
        
		editor.renderCurrentBlock();
        
		//Flush the GL buffer
        GL11.glFlush();
		 
		// now tell the screen to update
		Display.update();
		Display.releaseContext();
	}
	

	public void initGL(Window window) {
		//initialize the view
		setPerspective(window);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glClearColor(0.0f, 0.0f, 1.0f, 0.0f);
		GL11.glClearDepth(1.0f);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
	}
	
	public void setPerspective(Window window) {
		//Calculate the shape of the screen and notify OpenGL
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(45.0f, (float) window.getGLWidth() / (float) window.getGLHeight(), 0.1f, 20000.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	
}