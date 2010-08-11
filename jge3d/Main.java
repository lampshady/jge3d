package jge3d;

//LWJGL input
import java.applet.Applet;

import jge3d.controller.Controller;
import jge3d.gui.Window;
import jge3d.physics.Physics;
import jge3d.render.Renderer;

import org.lwjgl.opengl.Display;


public class Main extends Applet {
	private static final long serialVersionUID = 1L;

	private static  Main uniqueInstance = new Main();
	
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

			//Create the Physics Listening thread
			Thread controller_thread = new Thread(new Runnable(){
				@Override
				public void run() {
					while (isRunning) 
					{
						Controller.getInstance().monitor();
					}
				}
			},"Controller");
			
			controller_thread.start();
			controller_thread.setPriority(6);

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