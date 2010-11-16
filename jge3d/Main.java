package jge3d;

//LWJGL input
import java.applet.Applet;
import java.awt.EventQueue;

import javax.swing.SwingWorker;

import jge3d.controller.Controller;
//import jge3d.gui.Window;
import jge3d.physics.Physics;
import jge3d.render.Renderer;
import jge3d.window.Window;

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
		//Applet applet = new Main();
		//applet.init();

	    Window.init();
		
        Display.destroy();
	}
	
	public void init() {
		try{
			Level level;
			Player player;
			
			loadModules();
			
			SwingWorker window_thread = new SwingWorker() {
				@Override
				protected Object doInBackground() throws Exception {
					Controller.getInstance().start();
					//Window.getInstance().updateCanvas();
					return null;
				} 
			};
			
			//Create an empty level
			level = new Level();
			
			//Read in a level
			Display.makeCurrent();
			level.setLevel();
			Display.releaseContext();
			
			//Controller.getInstance().start();
			EventQueue.invokeLater(window_thread);

			//Make a player
			player = new Player();
			Input.getInstance().setPlayer(player);

		} catch(Exception e) {
			System.out.print("\nError Occured.  Exiting." + e.toString());
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public void loadModules()
	{
		EntityList.getInstance();
		//Window.getInstance();
		Renderer.getInstance();
		Physics.getInstance();
		TextureList.getInstance();
		Input.getInstance();
		Editor.getInstance();
		Camera.getInstance();
	}
}