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

public class Main {
	//the game always runs (except when it doesn't)
	static boolean isRunning = true;

	public static void main(String[] args) throws LWJGLException {
		try{		
			//create the window and all that jazz
			Window window = new Window();
			
			//Create inputs
			Input input = new Input();
			
			//Make some physics
			Physics physics = new Physics();
			
			//Prompt for level and instantiate a level parser 
			BufferedReader levelfile = new BufferedReader(new FileReader("lib/Levels/new.map"));
			Level level = new Level(levelfile, physics);

			//setup the initial perspective
			initGL(window);
			
			//Setup Camera
			Camera camera = new Camera(0,0,0,level.getHeight(), level.getWidth());
			camera.goToStart(level.getHeight(), level.getWidth());

			physics.dropBox(17,15,0,1.0f);
			while (isRunning) {
				//read keyboard and mouse
				input.handleMouse(camera, window, physics);
				input.handleKeyboard();
				
				//Update the world's physical layout
				physics.clientUpdate();

				//Draw world
				draw(level, camera, physics);
				
				//Print FPS to title bar
				window.updateFPS();

			}
		} catch(Exception e) {
			System.out.print("\nError Occured.  Exiting." + e.toString());
			System.exit(-1);
		}
	}
	
	public static void initGL(Window window){
		//initialize the view
		setPerspective(window);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glClearColor(0.0f, 0.0f, 1.0f, 0.0f);
		GL11.glClearDepth(1.0f);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
	}
	
	public static void setPerspective(Window window)
	{
		//Calculate the shape of the screen and notify OpenGL
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(45.0f, (float) window.getGLWidth() / (float) window.getGLHeight(), 0.1f, 20000.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	
	public static void draw(Level level, Camera camera, Physics physics) throws LWJGLException
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