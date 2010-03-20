package jge3d;

import java.awt.Canvas;

import java.lang.Math;

import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
import org.lwjgl.util.Dimension;
import org.lwjgl.util.glu.GLU;

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
	static float pos = 0;
	static float[] CameraRotation = {0,0}; //Angle up/down, side-to-side
	static float[] CameraPosition = {0,0,0}; // x, y, z
	static float CameraDistance = 10;
	static float[] Rotation = {0,0,0};// x, y, z
	static float[] Translation = {0,0,-10};// x, y, z
	static float zCameraTrans = 0;
	
	
	static DisplayMode chosenMode = null;
	
	public static void main(String[] args) throws LWJGLException {
		try{		//create the window and all that jazz
		initWindow();
		 
		//setup the initial perspective
		initGL();
		
		//Setup Inputs
		setupInputs();
		
		draw();
		while (isRunning) {
			
			handleMouse();
			//handleKeyboard();
			
			//Only draw if the display is in the foreground
			//System.out.print(Display.isActive());
			//if( !Display.isActive() )
			//{
				draw();
			//}
			
		}
		}catch(Exception e)
		{
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
		 
		//
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
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GL11.glClearDepth(1.0f);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
		GL11.glClearColor(0,0,0,0);
	}
	
	public static void setPerspective()
	{
		//GL11.glViewport(0, 0, GLView.getWidth(), GLView.getHeight());
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(45.0f, (float) GLView.getWidth() / (float) GLView.getHeight(), 1f, 200.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	
	public static void setupInputs() throws LWJGLException
	{
		Mouse.create();
		Mouse.setNativeCursor(null);
		Keyboard.create();
	}
	
	public static void draw() throws LWJGLException
	{
		Display.makeCurrent();
	    // perform game logic updates here
	    pos += 0.01f;  
	    
		// render using OpenGL 
	    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // Clear The Screen And The Depth Buffer
		GL11.glLoadIdentity();
		GLU.gluLookAt(CameraPosition[0],CameraPosition[1],CameraPosition[2],
				0,0,-5,
				0,1,0);
		GL11.glTranslatef(5.0f, 0.0f, 0.0f);
        GL11.glTranslatef(Translation[0], Translation[1], Translation[2]); // Move Into The Screen 5 Units
        GL11.glRotatef(Rotation[0], 0.0f, 1.0f, 0.0f); // Rotate On The Y Axis
        GL11.glRotatef(Rotation[1], 1.0f, 0.0f, 0.0f); // Rotate On The X Axis
        //GL11.glRotatef(zrot, 0.0f, 0.0f, 1.0f); // Rotate On The Z Axis
        
        GL11.glBegin(GL11.GL_QUADS);
	        // Front Face
        	GL11.glColor3f(1.0f,0.0f,0.0f);
	        GL11.glVertex3f(-1.0f, -1.0f, 1.0f); // Bottom Left Of The Texture and Quad
	        GL11.glVertex3f(1.0f, -1.0f, 1.0f); // Bottom Right Of The Texture and Quad
	        GL11.glVertex3f(1.0f, 1.0f, 1.0f); // Top Right Of The Texture and Quad
	        GL11.glVertex3f(-1.0f, 1.0f, 1.0f); // Top Left Of The Texture and Qua        
	        // Back Face
	        GL11.glColor3f(0.0f,1.0f,0.0f);
	        GL11.glVertex3f(-1.0f, -1.0f, -1.0f); // Bottom Right Of The Texture and Quad
	        GL11.glVertex3f(-1.0f, 1.0f, -1.0f); // Top Right Of The Texture and Quad
	        GL11.glVertex3f(1.0f, 1.0f, -1.0f); // Top Left Of The Texture and Quad
	        GL11.glVertex3f(1.0f, -1.0f, -1.0f); // Bottom Left Of The Texture and Quad
	        // Top Face
	        GL11.glColor3f(0.0f,0.0f,1.0f);
	        GL11.glVertex3f(-1.0f, 1.0f, -1.0f); // Top Left Of The Texture and Quad
	        GL11.glVertex3f(-1.0f, 1.0f, 1.0f); // Bottom Left Of The Texture and Quad
	        GL11.glVertex3f(1.0f, 1.0f, 1.0f); // Bottom Right Of The Texture and Quad
	        GL11.glVertex3f(1.0f, 1.0f, -1.0f); // Top Right Of The Texture and Quad
	        // Bottom Face
	        GL11.glColor3f(1.0f,0.0f,1.0f);
	        GL11.glVertex3f(-1.0f, -1.0f, -1.0f); // Top Right Of The Texture and Quad
	        GL11.glVertex3f(1.0f, -1.0f, -1.0f); // Top Left Of The Texture and Quad
	        GL11.glVertex3f(1.0f, -1.0f, 1.0f); // Bottom Left Of The Texture and Quad
	        GL11.glVertex3f(-1.0f, -1.0f, 1.0f); // Bottom Right Of The Texture and Quad
	        // Right face
	        GL11.glColor3f(1.0f,1.0f,0.0f);
	        GL11.glVertex3f(1.0f, -1.0f, -1.0f); // Bottom Right Of The Texture and Quad
	        GL11.glVertex3f(1.0f, 1.0f, -1.0f); // Top Right Of The Texture and Quad
	        GL11.glVertex3f(1.0f, 1.0f, 1.0f); // Top Left Of The Texture and Quad
	        GL11.glVertex3f(1.0f, -1.0f, 1.0f); // Bottom Left Of The Texture and Quad
	        // Left Face
	        GL11.glColor3f(0.0f,1.0f,1.0f);
	        GL11.glVertex3f(-1.0f, -1.0f, -1.0f); // Bottom Left Of The Texture and Quad
	        GL11.glVertex3f(-1.0f, -1.0f, 1.0f); // Bottom Right Of The Texture and Quad
	        GL11.glVertex3f(-1.0f, 1.0f, 1.0f); // Top Right Of The Texture and Quad
	        GL11.glVertex3f(-1.0f, 1.0f, -1.0f); // Top Left Of The Texture and Quad
        GL11.glEnd();
        
        //xrot += 0.03f; // X Axis Rotation
        //yrot += 0.02f; // Y Axis Rotation
        //zrot += 0.04f; // Z Axis Rotation

		GL11.glFlush();
		 
		// now tell the screen to update
		Display.update();
		Display.releaseContext();
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
						//Move Object
						if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) 
								|| Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) )//shift
						{
							//Move on Y-Z axis
							Translation[0] += 0.01 * deltaX;
							Translation[2] += -0.01 * deltaY;
						}else
						{
							//Move on  axis
							Translation[0] += 0.01 * deltaX;
							Translation[1] += 0.01 * deltaY;
						}
					}
					
					if(Mouse.isButtonDown(1))
					{
						//Change Rotation of Object
						Translation[1] += deltaX/2;
						Translation[2] += -deltaY/2;
					}
					
					boolean temp = Mouse.isButtonDown(2);
					if(Mouse.isButtonDown(2))
					{
						//Change Perspective
						CameraRotation[0] += deltaY/2;//Up-Down
						CameraRotation[1] += deltaX/2;//Side-to-Side
						
						float a = 0;
						
						CameraPosition[1] = (float) ((CameraPosition[2] * Math.tan(CameraRotation[1])));
						a = (float) (CameraDistance * Math.sin(CameraRotation[0]));
						CameraPosition[0] = (float) (Translation[0]+(a*Math.sin(CameraRotation[1])));
						CameraPosition[2] = (float) (Translation[2]+(a*Math.cos(CameraRotation[1])));
						CameraPosition[1] = (float) (CameraPosition[1] + Translation[1]);
						System.out.print(CameraPosition[0]+ ","+CameraPosition[1]+","+CameraPosition[2]+"\n");
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
}