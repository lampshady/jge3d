package jge3d;

//LWJGL input
import jge3d.GUI.Window;

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
			TextureList texture;
			EntityList entity;
			Player player;


			//Make some physics
			physics = new Physics();
			
			//Create a texture holder
			texture = new TextureList();
			
			//A list for storing all the entities
			entity = new EntityList(physics);
			
			//Create an empty  level
			level = new Level(texture, entity);
			
			//Renderer for drawing stuff
			render = new Renderer(level, physics, texture, entity);
			
			//create the window and all that jazz
 			window = new Window(level, texture);

			//setup the initial perspective
			render.initGL(window);
		
			//Camera
			camera = new Camera(level.getHeight(), level.getWidth());
			
			//Make an editor
			editor = new Editor(render, window);
			
			//Make a player
			player = new Player(physics);
			
			//Create inputs
			input = new Input(camera, window, physics, editor, entity, player);

			//Renderer also needs references to the editor and camera
			render.addReferences(editor, camera);
			
			//Read in a level 
			Display.makeCurrent();
			level.setLevel(render, window);
			Display.releaseContext();
			
			//Just to show off the physics
			physics.dropBox(17,15,0,1.0f);

			//window.get
			while (isRunning) {
				if(window.getLevelView().getLoadLevel()) {
					level.load();
					System.out.println("You loaded the level\n");
				}
				
				//Check if textureList has been altered since last frame
				if(texture.hasChanged()) {
					texture.loadQueuedTexture();
				}
				
				//read keyboard and mouse
				input.handleMouse();
				input.handleKeyboard();

				//Check to make sure none of the entities are marked as dead
				entity.pruneEntities();
				
				//Update the world's physical layout
				physics.clientUpdate();

				//Draw world
				render.draw();

				//Print FPS to title bar
				window.updateFPS();
				
				//Check if it's time to close
				if (Display.isCloseRequested()) {
					isRunning=false;
					//Display.destroy();
                    //System.exit(0);
				}
			}
		} catch(Exception e) {
			System.out.print("\nError Occured.  Exiting." + e.toString());
			e.printStackTrace();
			System.exit(-1);
		}
	}

}