package jge3d;

//Required for file reading
import java.io.BufferedReader;
import java.io.FileReader;

//LWJGL input
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

public class Main {
	public static void main(String[] args) throws LWJGLException {
		try{
			//the game always runs (except when it doesn't)
			boolean isRunning = true;
			
			//Make game "pieces"
			Camera camera;
			Editor editor;
			Window window;
			Physics physics;
			Input input;
			Level level;
			Renderer render;
			
			level = new Level();
			
			//Renderer for drawing stuff
			render = new Renderer();
			
			//create the window and all that jazz
 			window = new Window(level, render);

			//setup the initial perspective
			render.initGL(window);

			//Make some physics
			physics = new Physics(render);
			
			//Read in a level 
			BufferedReader levelfile;
			levelfile = new BufferedReader(new FileReader("lib/Levels/newParserTest.map"));
			level.setLevel(levelfile, physics, render, window);
		
			//Camera
			camera = new Camera(0,0,0,level.getHeight(), level.getWidth());
			camera.goToStart(level.getHeight(), level.getWidth());
			
			//Make an editor
			editor = new Editor(render, window);
			
			//Create inputs
			input = new Input(camera, window, physics, editor, level);

			//Just to show off the physics
			physics.dropBox(17,15,0,1.0f);
			
			render.reconstruct(level, editor, physics, camera);
			
			while (isRunning) {
				Display.makeCurrent();
				//read keyboard and mouse
				input.handleMouse();
				input.handleKeyboard();
				
				//Update the world's physical layout
				physics.clientUpdate();

				//Draw world
				render.draw();

				//Print FPS to title bar
				window.updateFPS();
				
				//Check if it's time to close
				if (Display.isCloseRequested())
					isRunning=false;
				
				Display.releaseContext();
			}
		} catch(Exception e) {
			System.out.print("\nError Occured.  Exiting." + e.toString());
			e.printStackTrace();
			System.exit(-1);
		}
	}
}