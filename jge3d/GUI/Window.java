package jge3d.GUI;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import jge3d.EntityList;
import jge3d.Level;
import jge3d.TextureList;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Window extends JFrame{
	private static final long serialVersionUID = 1L;
	
	//window components
	private Canvas GLView;
	private JPanel RightPane;
	
	//Panels that we're going to use for the right pane
	private TextureView textureView;
	private EditorView editorView;
	private LevelView levelView;
	private static EntityView entityView;
	
	//holds the current monitor sizing mode
	private DisplayMode chosenMode = null;

	//frame rate calculations
	private long prev_time=0;
	private int frames=0;
	
	//local references to other classes
	private Level level;
	private TextureList texture;
	private EntityList entity;
		
	public Window(Level _level, TextureList _texture, EntityList _entity) {
		level = _level;
		texture = _texture;
		entity = _entity;
		
		//One of these days we'll get this thing running in the correct
		//thread so we don't need all those stupid hooks to avoid makeCurrent errors 
		//javax.swing.SwingUtilities.invokeLater(new Runnable() {
        //    public void run() {
                createAndShowGUI();
        //    }
        //});
	}
	
	public void createAndShowGUI() {
		// Set the target size of the window.
		int targetWidth = 1024;
		int targetHeight = 768;
		
		//Create pieces that make up the layout of the window
		GLView = new Canvas();
		RightPane = new JPanel();
		
		//Construct panels for right pane
		textureView = new TextureView(texture);
		editorView = new EditorView();
		levelView = new LevelView(level);
		entityView = new EntityView(entity);
		
		//layout the window
		chosenMode = new DisplayMode(targetWidth, targetHeight);
		this.setLayout(new FlowLayout());
		this.setSize( chosenMode.getWidth(), chosenMode.getHeight());
		this.add(GLView);
		this.add(RightPane);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Size the components in the window
		GLView.setPreferredSize(
			new Dimension( 
				((int)(chosenMode.getWidth()*(.725))),
				chosenMode.getHeight()
			)
		);
		RightPane.setPreferredSize(
				new Dimension( 
					((int)(chosenMode.getWidth()*(.275))),
					chosenMode.getHeight()
				)
		);
		GLView.setBounds(
			0,
			0,
			((int)(chosenMode.getWidth()*(.725))),
			chosenMode.getHeight()
		);
		RightPane.setBounds(
			((int)(chosenMode.getWidth()*(.725))),
			0,
			((int)(chosenMode.getWidth()*(.275))),
			chosenMode.getHeight()
		);

		//Layout right pane
		RightPane.setLayout(new BoxLayout(RightPane, BoxLayout.Y_AXIS));
		RightPane.setBackground(new Color(0,0,0));
		RightPane.add(textureView);
		RightPane.add(Box.createRigidArea(new Dimension(0, 5)));
		RightPane.add(levelView);
		RightPane.add(Box.createRigidArea(new Dimension(0, 5)));
		RightPane.add(editorView);
		RightPane.add(Box.createRigidArea(new Dimension(0, 5)));
		RightPane.add(entityView);
		
		RightPane.setBorder(BorderFactory.createLineBorder(Color.red));
				
		this.validate();
		this.pack();
		this.setVisible(true);
		
		//create a display instance in GLView
		try {
			Display.setParent(GLView);
			Display.create();
		} catch (LWJGLException e) {
		    Sys.alert("Unable to create display.", e.toString());
		    System.exit(0);
		}
		
		//Force minimum size of window
		this.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent event) {
				final int initialWidth = (int) getSize().getWidth();
				final int initialHeight = (int) getSize().getHeight();
				System.out.print(getSize().getWidth() + " " + getSize().getHeight());
				setSize(
					Math.max(initialWidth, getWidth()),
					Math.max(initialHeight, getHeight())
				);
			}
		});
	}

	public void updateFPS() {
		//Increment frame counter
		frames++;
		
		//Update the frame-counter in the title bar if it has been a second or more
		if ( (System.currentTimeMillis()-prev_time) >= 1000 ) {
			this.setTitle("Fps: " + ((frames*1000)/(System.currentTimeMillis()-prev_time)) );
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
	
	public boolean mouseInGLView() {
		System.out.print(GLView.getMousePosition() + "\n");
		if(GLView.getMousePosition() != null)
			return true;
		else
			return false;
	}
	
	//Window Component Getters
	//main window
	public JFrame getWindow() {	return this; }
	//Texture panel
	public TextureView getTextureView() { return textureView; }
	//Editor panel
	public EditorView getEditorView() {	return editorView; }
	//Level panel
	public LevelView getLevelView() {	return levelView; }
	//Entity panel
	public static EntityView getEntityView() {	return entityView;	}
}
