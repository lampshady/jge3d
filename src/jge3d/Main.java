package jge3d;

import java.awt.Canvas;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
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
	
	static DisplayMode chosenMode = null;
	
	public static void main(String[] args) throws LWJGLException {
		//create the window and all that jazz
		initWindow();
		 
		//setup the initial perspective
		initGL();
		 
		boolean isRunning = true;
		float pos = 0;
		float xrot = 0;
		float yrot = 0;
		float zrot = 0;
		
		//render
		while (isRunning) {
			Display.makeCurrent();
		    // perform game logic updates here
		    pos += 0.01f;    
		    
		    // render using OpenGL 
		    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // Clear The Screen And The Depth Buffer
			GL11.glLoadIdentity();
			
	        GL11.glTranslatef(0.0f, 0.0f, -10.0f); // Move Into The Screen 5 Units
	        GL11.glRotatef(xrot, 1.0f, 0.0f, 0.0f); // Rotate On The X Axis
	        GL11.glRotatef(yrot, 0.0f, 1.0f, 0.0f); // Rotate On The Y Axis
	        GL11.glRotatef(zrot, 0.0f, 0.0f, 1.0f); // Rotate On The Z Axis
	        
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

	        xrot += 0.03f; // X Axis Rotation
	        yrot += 0.02f; // Y Axis Rotation
	        zrot += 0.04f; // Z Axis Rotation

			GL11.glFlush();
			 
			// now tell the screen to update
			Display.update();
			Display.releaseContext();
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
		
		setupInputs();
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
		GL11.glViewport(0, 0, GLView.getWidth(), GLView.getHeight());
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GLU.gluPerspective(45.0f, (float) GLView.getWidth() / (float) GLView.getHeight(), 1f, 200.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);	
	}
	
	public static void setupInputs()
	{
		Sys.alert("Called", "Input Setup");
		window.addMouseListener( new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				System.out.print("Mouse Clicked");
			}
		});
		
		GLView.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				System.out.print("Mouse Dragged");
			}
		});
		
	}
}