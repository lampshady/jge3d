package jge3d;

//LWJGL input
import java.applet.Applet;
import java.lang.reflect.Field;

import jge3d.GUI.LevelView;
import jge3d.GUI.Window;
import jge3d.render.Renderer;

import org.lwjgl.opengl.Display;

public class Main extends Applet {
	private static final long serialVersionUID = 1L;

	public static void main(String args[])
	{
		Applet applet = new Main();
		applet.init();
	}
	
	public void init() {
		try{
			//the game always runs (except when it doesn't)
			boolean isRunning = true;
			
			//Make game "pieces"
			/*
			Camera camera;
			Editor editor;
			Window window;
			Physics physics;
			Input input;
			Renderer render;*/
			Level level;
			
			TextureList texture;
			EntityList entity;
			Player player;
			
			//Create a texture holder
			texture = new TextureList();
			
			//A list for storing all the entities
			entity = new EntityList();
			
			//Create an empty  level
			level = new Level();
			
			for (Field field : entity.getClass().getDeclaredFields())
			{
				System.out.print(field + "\n");
			}
			
			//Renderer for drawing stuff
			//render = new Renderer(level, physics, texture, entity);
			
			//create the window and all that jazz
 			//window = new Window(level, texture, entity);

			//setup the initial perspective
			Renderer.getInstance().initGL();
		
			//Camera
			//camera = new Camera(level.getHeight(), level.getWidth(), window);
			
			//Make an editor
			//editor = new Editor(render, window);
			
			//Make a player
			player = new Player();
			
			//Create inputs
			//input = new Input(camera, window, physics, editor, entity, player);

			//Renderer also needs references to the editor and camera
			//render.addReferences(editor, camera);
			
			//Read in a level 
			Display.makeCurrent();
			level.setLevel();
			Display.releaseContext();

			while (isRunning) {
				if(LevelView.getInstance().getLoadLevel()) {
					level.load();
					System.out.println("You loaded the level\n");
				}
				
				//Check if textureList has been altered since last frame
				if(texture.hasChanged()) {
					texture.loadQueuedTexture();
				}
				
				//read keyboard and mouse
				Input.getInstance().handleMouse();
				Input.getInstance().handleKeyboard();

				//Check to make sure none of the entities are marked as dead
				entity.pruneEntities();
				
				//Update the world's physical layout
				Physics.getInstance().clientUpdate();

				//Camera check versus player position
				Camera.getInstance().moveToPlayerLocation(player);
				
				//Draw world
				Renderer.getInstance().draw();

				//Print FPS to title bar
				Window.getInstance().updateFPS();
				
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