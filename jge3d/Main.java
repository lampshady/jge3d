package jge3d;

//LWJGL input
import java.applet.Applet;
import java.io.FileNotFoundException;
import java.io.IOException;

import jge3d.GUI.LevelView;
import jge3d.GUI.Window;
import jge3d.physics.Physics;
import jge3d.render.Renderer;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;


public class Main extends Applet {
	private static final long serialVersionUID = 1L;

	public static void main(String args[])
	{
		Applet applet = new Main();
		applet.init();
	}
	
	public void init() {
		//the game always runs (except when it doesn't)
		boolean isRunning = true;
		Thread render_thread = new Thread(new Runnable(){
			@Override
			public void run() {
				while (true) 
				{
					//read keyboard and mouse
					try {
						Input.getInstance().updateInput();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (LWJGLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		try{
			Level level;

			Player player;
			
			loadModules();
			
			//Create an empty  level
			level = new Level();
			
			//Read in a level
			Display.makeCurrent();
			level.setLevel();
			Display.releaseContext();
			
			render_thread.start();
			
			//Make a player
			player = new Player();
			Input.getInstance().setPlayer(player);

			while (isRunning) 
			{
				if(LevelView.getInstance().getLoadLevel()) {
					level.load();
					System.out.println("You loaded the level\n");
				}
				
				//Check if textureList has been altered since last frame
				if(TextureList.getInstance().hasChanged()) {
					TextureList.getInstance().loadQueuedTexture();
				}
				


				//Check to make sure none of the entities are marked as dead
				EntityList.getInstance().pruneEntities();
				
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
	
	public void loadModules()
	{
		EntityList.getInstance();
		Window.getInstance();
		Renderer.getInstance();
		Physics.getInstance();
		TextureList.getInstance();
		Input.getInstance();
		Editor.getInstance();
		Camera.getInstance();
	}
}