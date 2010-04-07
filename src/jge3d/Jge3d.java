package jge3d;

//Required for file reading
import java.io.BufferedReader;
import java.io.FileReader;

//LWJGL input
import org.lwjgl.LWJGLException;

//LWJGL 3d
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import jge3d.Jge3d;
import jge3d.Camera;
import jge3d.Level;
import jge3d.Physics;
import jge3d.Renderer;
import jge3d.Input;
import jge3d.Editor;

public class Jge3d {
	//the game always runs (except when it doesn't)
	static boolean isRunning = true;
	private Camera camera;
	private Renderer render;
	private Editor editor;
	private Window window;
	private Physics physics;
	private Input input;
	private Level level;

	public static void main(String[] args) throws LWJGLException {
		final Jge3d main = new Jge3d();
		main.init();
	}
	
	public Jge3d() {
	
	}
	
	public void init() {
		try{
			//create the window and all that jazz
			window = new Window();
			
			//setup the initial perspective
			initGL();

			//Make some physics
			physics = new Physics();
			
			//Prompt for level and instantiate a level parser 
			BufferedReader levelfile;
			
			levelfile = new BufferedReader(new FileReader("lib/Levels/new.map"));
			level = new Level(levelfile, physics);

			
			//Camera
			camera = new Camera(0,0,0,level.getHeight(), level.getWidth());
			camera.goToStart(level.getHeight(), level.getWidth());
			
			//Renderer
			render = new Renderer();

			//Create inputs
			input = new Input();
			
			//Make an editor
			editor = new Editor();

			physics.dropBox(17,15,0,1.0f);
			while (isRunning) {
				//read keyboard and mouse
				input.handleMouse(camera, window, physics);
				input.handleKeyboard();
				
				//Update the world's physical layout
				physics.clientUpdate();
	
				//Draw world
				draw();
				
				//Print FPS to title bar
				window.updateFPS();
	
			}
		} catch(Exception e) {
			System.out.print("\nError Occured.  Exiting." + e.toString());
			System.exit(-1);
		}
	}

	public void initGL(){
		//initialize the view
		setPerspective();
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glClearColor(0.0f, 0.0f, 1.0f, 0.0f);
		GL11.glClearDepth(1.0f);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
	}
	
	public void setPerspective()
	{
		//Calculate the shape of the screen and notify OpenGL
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(45.0f, (float) window.getGLWidth() / (float) window.getGLHeight(), 0.1f, 20000.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	
	public void draw() throws LWJGLException
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
        
		//Flush the GL buffer
        GL11.glFlush();
		 
		// now tell the screen to update
		Display.update();
		Display.releaseContext();
	}
}