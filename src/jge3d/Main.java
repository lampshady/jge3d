package jge3d;

import java.awt.Canvas;

import java.io.BufferedReader;
import java.io.FileReader;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;

//import jge3d.ObjParser;
import jge3d.LevelParser;

public class Main {
	static JFrame window;
	static JSplitPane mainSplit;
	static JPanel TopPane;
	static JPanel LeftPane;
	static Canvas GLView;
	static JPanel RightPane;
	static JPanel TextureView;
	static JPanel TreeView;
	static boolean isRunning = true;
	//static float[] CameraPosition = {7.5f,-5,21}; // x, y, z
	static long prev_time=0;
	static int frames=0;
	static DisplayMode chosenMode = null;
	static Camera camera = new Camera(7.5f, -5.0f, 21.0f);
	
	public static void main(String[] args) throws LWJGLException {
		try{		
			//create the window and all that jazz
			initWindow();
			 
			//Add Level parser
			//Create a file chooser
			final JFileChooser fc_level = new JFileChooser("lib/Levels/");
			fc_level.showOpenDialog(window);
			BufferedReader levelfile = new BufferedReader(new FileReader(fc_level.getSelectedFile()));	//use the line below if you don't want to have to click everytime
			LevelParser level = new LevelParser(levelfile);

			//Add Object parser
			//Create a file chooser
			//final JFileChooser fc_model = new JFileChooser("lib/Models/");
			//fc_model.showOpenDialog(window);
			//BufferedReader objfile = new BufferedReader(new FileReader(fc_model.getSelectedFile()));
			//ObjParser model = new ObjParser(objfile, true);
			
			//setup the initial perspective
			initGL();
			
			//Setup Inputs
			setupInputs();
			
			draw(level);
			while (isRunning) {
				
				handleMouse();
				handleKeyboard();
				
				draw(level);
			}
		} catch(Exception e) {
			System.out.print("\nError Occured.  Exiting." + e.toString());
			System.exit(-1);
		}
	}
	
	protected static void initWindow()
	{
		// we're aiming for an 800x600 display.
		int targetWidth = 800;
		int targetHeight = 600;
		
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
	
	public static void initGL(){
		//initialize the view
		setPerspective();
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glClearColor(0.0f, 0.0f, 1.0f, 0.0f);
		GL11.glClearDepth(1.0f);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
	}
	
	public static void setPerspective()
	{
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(45.0f, (float) GLView.getWidth() / (float) GLView.getHeight(), 0.01f, 20000.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	
	public static void setupInputs() throws LWJGLException
	{
		Mouse.create();
		Mouse.setNativeCursor(null);
		Keyboard.create();
	}
	
	public static void draw(LevelParser level) throws LWJGLException
	{
		Display.makeCurrent();
	    // perform game logic updates here

		// render using OpenGL 
	    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // Clear The Screen And The Depth Buffer
		GL11.glLoadIdentity();
		
		GLU.gluLookAt(
				camera.getPositionX(), camera.getPositionY(), camera.getPositionZ(),
				camera.getFocusX(), camera.getFocusY(), camera.getFocusZ(),
				0				 ,	 1,						 0
		);
		
		GL11.glPushMatrix();
        //render level
        level.opengldraw();

        GL11.glPopMatrix();
		GL11.glFlush();
		 
		// now tell the screen to update
		Display.update();
		Display.releaseContext();
		
		frames++;
		if ( (System.currentTimeMillis()-prev_time) >= 1000 ) {
			window.setTitle("Fps: " + ((frames*1000)/(System.currentTimeMillis()-prev_time)) );
			prev_time=System.currentTimeMillis();
			frames = 0;
		}
	}
	
	public static void handleMouse() throws LWJGLException
	{
		int deltaX, deltaY; //Changes in X and Y directions
		
		//Handle Mouse Events here
		while(Mouse.next())
		{
			
			Mouse.poll();
			
			//update the changes in position
			deltaX = Mouse.getEventDX();
			deltaY = Mouse.getEventDY();
			
			switch(Mouse.getEventButton())
			{
			case -1://Mouse Movement
				if(Mouse.isInsideWindow())
				{
					if(Mouse.isButtonDown(0))
					{
						//Pan camera Z
						if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) )
						{
							camera.incrementDistance(-0.1f*deltaY);	
						}else{
							camera.moveFocus( new Vector3f(-0.1f*deltaX, -0.1f*deltaY, 0.0f) );
						}
					}
					
					if(Mouse.isButtonDown(1))
					{
						//Change angle of camera

					}
					
					if(Mouse.isButtonDown(2))
					{
						//Change Perspective

					}
				}
				break;
			case 0://Left Button
				if( Mouse.isButtonDown(0) )
				{
				}else
				{
				}
				break;
			case 1://Right Button
				break;
			case 2://Middle Button
				break;
			}
		}
	}
	
	public static void handleKeyboard() throws LWJGLException
	{
		while(Keyboard.next())
		{
			Keyboard.poll();
			System.out.print("!!!" + Keyboard.getEventKey() + "!!!");
			switch(Keyboard.getEventCharacter())
			{
			case 'w':	
				break;
			case 'a':
				break;
			case 's':
				break;
			case 'd':
				break;
			}
		}
	}
}