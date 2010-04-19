package jge3d;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.opengl.Texture;

class Renderer {
	private HashMap<String, TextureList> textures;
	private Level level;
	private Editor editor;
	private Physics physics;
	private Camera camera;
	
	public Renderer() {
		textures = new HashMap<String, TextureList>();
	}
	
	public void reconstruct(Level _level, Editor _editor, Physics _physics, Camera _camera) {
		level=_level;
		editor=_editor;
		physics=_physics;
		camera=_camera;
	}
	
	public void drawcube(String texture_name, float cube_size) throws FileNotFoundException, IOException {		
		//bind a texture for drawing
		getTextureByName(texture_name).bind();

        GL11.glBegin(GL11.GL_QUADS);
	        // Front Face
	        GL11.glTexCoord2f(0.0f, 0.0f);
	        GL11.glVertex3f(-1.0f, -1.0f,  1.0f);   // Bottom Left Of The Texture and Quad
	        GL11.glTexCoord2f(1.0f, 0.0f);
	        GL11.glVertex3f( 1.0f, -1.0f,  1.0f);   // Bottom Right Of The Texture and Quad
	        GL11.glTexCoord2f(1.0f, 1.0f);
	        GL11.glVertex3f( 1.0f,  1.0f,  1.0f);   // Top Right Of The Texture and Quad
	        GL11.glTexCoord2f(0.0f, 1.0f);
	        GL11.glVertex3f(-1.0f,  1.0f,  1.0f);   // Top Left Of The Texture and Quad
	
	        // Back Face
	        GL11.glTexCoord2f(1.0f, 0.0f);
	        GL11.glVertex3f(-1.0f, -1.0f, -1.0f);   // Bottom Right Of The Texture and Quad
	        GL11.glTexCoord2f(1.0f, 1.0f);
	        GL11.glVertex3f(-1.0f,  1.0f, -1.0f);   // Top Right Of The Texture and Quad
	        GL11.glTexCoord2f(0.0f, 1.0f);
	        GL11.glVertex3f( 1.0f,  1.0f, -1.0f);   // Top Left Of The Texture and Quad
	        GL11.glTexCoord2f(0.0f, 0.0f);
	        GL11.glVertex3f( 1.0f, -1.0f, -1.0f);   // Bottom Left Of The Texture and Quad
	
	        // Top Face
	        GL11.glTexCoord2f(0.0f, 1.0f);
	        GL11.glVertex3f(-1.0f,  1.0f, -1.0f);   // Top Left Of The Texture and Quad
	        GL11.glTexCoord2f(0.0f, 0.0f);
	        GL11.glVertex3f(-1.0f,  1.0f,  1.0f);   // Bottom Left Of The Texture and Quad
	        GL11.glTexCoord2f(1.0f, 0.0f);
	        GL11.glVertex3f( 1.0f,  1.0f,  1.0f);   // Bottom Right Of The Texture and Quad
	        GL11.glTexCoord2f(1.0f, 1.0f);
	        GL11.glVertex3f( 1.0f,  1.0f, -1.0f);   // Top Right Of The Texture and Quad
	
	        // Bottom Face
	        GL11.glTexCoord2f(1.0f, 1.0f);
	        GL11.glVertex3f(-1.0f, -1.0f, -1.0f);   // Top Right Of The Texture and Quad
	        GL11.glTexCoord2f(0.0f, 1.0f);
	        GL11.glVertex3f( 1.0f, -1.0f, -1.0f);   // Top Left Of The Texture and Quad
	        GL11.glTexCoord2f(0.0f, 0.0f);
	        GL11.glVertex3f( 1.0f, -1.0f,  1.0f);   // Bottom Left Of The Texture and Quad
	        GL11.glTexCoord2f(1.0f, 0.0f);
	        GL11.glVertex3f(-1.0f, -1.0f,  1.0f);   // Bottom Right Of The Texture and Quad
	
	        // Right face
	        GL11.glTexCoord2f(1.0f, 0.0f);
	        GL11.glVertex3f( 1.0f, -1.0f, -1.0f);   // Bottom Right Of The Texture and Quad
	        GL11.glTexCoord2f(1.0f, 1.0f);
	        GL11.glVertex3f( 1.0f,  1.0f, -1.0f);   // Top Right Of The Texture and Quad
	        GL11.glTexCoord2f(0.0f, 1.0f);
	        GL11.glVertex3f( 1.0f,  1.0f,  1.0f);   // Top Left Of The Texture and Quad
	        GL11.glTexCoord2f(0.0f, 0.0f);
	        GL11.glVertex3f( 1.0f, -1.0f,  1.0f);   // Bottom Left Of The Texture and Quad
	
	        // Left Face
	        GL11.glTexCoord2f(0.0f, 0.0f);
	        GL11.glVertex3f(-1.0f, -1.0f, -1.0f);   // Bottom Left Of The Texture and Quad
	        GL11.glTexCoord2f(1.0f, 0.0f);
	        GL11.glVertex3f(-1.0f, -1.0f,  1.0f);   // Bottom Right Of The Texture and Quad
	        GL11.glTexCoord2f(1.0f, 1.0f);
	        GL11.glVertex3f(-1.0f,  1.0f,  1.0f);   // Top Right Of The Texture and Quad
	        GL11.glTexCoord2f(0.0f, 1.0f);
	        GL11.glVertex3f(-1.0f,  1.0f, -1.0f);   // Top Left Of The Texture and Quad
        GL11.glEnd();
	}
	
	public void draw() throws LWJGLException, FileNotFoundException, IOException
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
				camera.getUpVectorX()	, camera.getUpVectorY()	,	camera.getUpVectorZ()
		);
		
        //render level
        level.opengldraw();
        
        //render physics
        physics.render();
        
		editor.renderCurrentBlock();
        
        GL11.glFlush();
		Display.update();// now tell the screen to update
		
		Display.releaseContext();
	}
	

	public void initGL(Window window) {
		//initialize the view
		setPerspective(window);
		GL11.glEnable(GL11.GL_TEXTURE_2D);     
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
	
	public Texture getTextureByName(String key) {
		return textures.get(key).getTexture();
	}
	
	public void setTexture(String group, String name, String path) throws FileNotFoundException, IOException {
		textures.put(name, new TextureList(group,name,path));
	}
	
	public int length() {
		return textures.size();
	}
	
	public HashMap<String, TextureList> getHash() {
		return textures;
	}
	
	public void clearTextureList() {
		textures.clear();
	}
	
	public boolean hasKey(String key) {
		return textures.containsKey(key);
	}
}