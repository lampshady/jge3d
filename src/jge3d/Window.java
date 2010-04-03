package jge3d;

import java.awt.Canvas;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Window {
	//window components
	static JFrame window;
	static JSplitPane mainSplit;
	static JPanel TopPane;
	static JPanel LeftPane;
	static Canvas GLView;
	static JPanel RightPane;
	static JPanel TextureView;
	static JPanel TreeView;
	static DisplayMode chosenMode = null;
	
	//frame rate calculations
	static long prev_time=0;
	static int frames=0;
	
	public Window()
	{
		// Set the target size of the window.
		int targetWidth = 1024;
		int targetHeight = 768;
		
		//Create pieces that make up the layout of the window
		window = new JFrame();
		GLView = new Canvas();
		RightPane = new JPanel();
		
		//Make it so closing the window closes the program
		window.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		
		//Embed display into left pane
		try{
			Display.setParent(GLView);
		}catch(LWJGLException e)
		{
			Sys.alert("Unable to set parent.", e.toString());
		    System.exit(0);
		}
		 
		chosenMode = new DisplayMode(targetWidth, targetHeight);
	
		GLView.setSize((int)(chosenMode.getWidth()*(.725)), chosenMode.getHeight());
		RightPane.setSize((int)(chosenMode.getWidth()*(.225)), chosenMode.getHeight());
		
		window.setLayout(null);
		
		//layout the window
		window.add(GLView);
		window.add(RightPane);
		window.setSize( chosenMode.getWidth(), chosenMode.getHeight());
		window.setVisible(true);
		//window.setResizable(false);
		
		try {
		    Display.create();
		} catch (LWJGLException e) {
		    Sys.alert("Unable to create display.", e.toString());
		    System.exit(0);
		}
	}
	
	public void updateFPS() {
		//Increment frame counter
		frames++;
		
		//Update the frame-counter in the title bar if it has been a second or more
		if ( (System.currentTimeMillis()-prev_time) >= 1000 ) {
			window.setTitle("Fps: " + ((frames*1000)/(System.currentTimeMillis()-prev_time)) );
			prev_time=System.currentTimeMillis();
			frames = 0;
		}
	}
	
	public int getGLWidth() {
		return GLView.getWidth();
	}
	public int getGLHeight() {
		return GLView.getHeight();
	}
}
