package jge3d;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
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
	static JPanel textureView;
	static JPanel TreeView;
	static DisplayMode chosenMode = null;
	
	//TextureView controls
	static JButton textureAddButton;
	static JButton textureDelButton;
	static JList textureListBox;
	static Canvas texturePreview;
	
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
		
		//TextureView
		textureView = new JPanel();
		textureAddButton = new JButton("Add");
		textureDelButton = new JButton("Remove");
		textureListBox = new JList();
		texturePreview = new Canvas();
		
		//Make it so closing the window closes the program
		window.addWindowListener(new WindowAdapter(){
			@Override
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
		RightPane.setSize((int)(chosenMode.getWidth()*(.275)), chosenMode.getHeight());
		
		window.setLayout(new FlowLayout());
		RightPane.setLayout(new BorderLayout());
		RightPane.setBackground(new Color(0,0,0));
		RightPane.add(textureView, BorderLayout.PAGE_START);
		
		//layout the window
		window.add(GLView);
		window.add(RightPane);
		window.setSize( chosenMode.getWidth(), chosenMode.getHeight());
		window.setVisible(true);
		
		//layout the texture panel
		setupTextureView();
		
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
	
	public void setupTextureView()
	{
		//textureView.removeAll();
		//layout TextureView
		
		textureView.setLayout(new BoxLayout(textureView, BoxLayout.PAGE_AXIS));
		
		textureView.add(new JLabel("Texture Viewer"));
		textureView.add(texturePreview);
		textureView.add(textureListBox);
		textureView.add(textureAddButton);
		textureView.add(textureDelButton);
		
		RightPane.setLayout(new FlowLayout());
		texturePreview.setSize(128, 128);
		textureView.setVisible(true);
	}
}
