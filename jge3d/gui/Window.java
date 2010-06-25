package jge3d.gui;

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

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;


public class Window extends JFrame{
	private static final long serialVersionUID = 1L;
	private static Window uniqueInstance = new Window();
	
	//window components
	private Canvas GLView;
	private JPanel RightPane;
	
	//holds the current monitor sizing mode
	private DisplayMode chosenMode = null;

	private Window()
	{	
		createAndShowGUI();
	}
	
	public static Window getInstance()
	{
		return uniqueInstance;
	}
	
	public void createAndShowGUI() {
		// Set the target size of the window.
		int targetWidth = 1024;
		int targetHeight = 768;
		
		//Create pieces that make up the layout of the window
		GLView = new Canvas();
		RightPane = new JPanel();
		
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
		RightPane.add(FPSView.getInstance());
		RightPane.add(Box.createRigidArea(new Dimension(0, 5)));
		RightPane.add(TextureView.getInstance());
		RightPane.add(Box.createRigidArea(new Dimension(0, 5)));
		RightPane.add(LevelView.getInstance());
		RightPane.add(Box.createRigidArea(new Dimension(0, 5)));
		RightPane.add(EditorView.getInstance());
		RightPane.add(Box.createRigidArea(new Dimension(0, 5)));
		RightPane.add(EntityView.getInstance());
		
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
	
	//public static void setTableEntity(Entity ent) {
	//	EntityTableModel.getInstance().setEntity(ent);
	//}
}
