package jge3d;

//LWJGL input
import java.applet.Applet;

import jge3d.controller.Controller;
import jge3d.gui.FPSView;
import jge3d.gui.LevelView;
import jge3d.gui.Window;
import jge3d.physics.Physics;
import jge3d.render.Renderer;

import org.lwjgl.opengl.Display;


public class Main extends Applet {
	private static final long serialVersionUID = 1L;

	private static  Main uniqueInstance = new Main();
	
	private long frames=0;
	
	public static Main getInstance(){ 
		return uniqueInstance; 
	}
	
	//the game always runs (except when it doesn't)
	final boolean isRunning = true;
	
	public static void main(String args[])
	{
		Applet applet = new Main();
		applet.init();
	}
	
	public void init() {
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
			
			Controller.getInstance().start();
			
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
				//Physics.getInstance().clientUpdate();

				//Camera check versus player position
				//Camera.getInstance().moveToPlayerLocation(player);
				
				if(Controller.getInstance().hasQueuedItems()) {
					Controller.getInstance().run_queue();
				}
				
				//Draw world
				//Renderer.getInstance().draw();

				frames++;
				FPSView.getInstance().updateFPS();
				System.out.print(frames+"\n");
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
	
	public long getFrames() {
		return frames;
	}
	
	public void resetFrames() {
		frames=0;
	}
}