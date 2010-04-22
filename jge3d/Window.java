package jge3d;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Window {
	//window components
	private JFrame window;
	private Canvas GLView;
	private JPanel RightPane;
	JPanel textureView;
	private DisplayMode chosenMode = null;
	
	//TextureView controls
	private JLabel textureLabel;
	private JButton textureAddButton;
	private JButton textureDelButton;
	private JTree textureTree;
	private JScrollPane textureTreeScroll;
	private JLabel texturePreview;
	
	//Tree controls
	private DefaultMutableTreeNode textureRootNode;
	private DefaultTreeModel textureTreeModel;
	private int texture_index=0;
	private String textureTreeCurrentSelection="cube1";
	
	//Level controls
	private JPanel levelView;
	private JLabel levelLabel;
	private JButton levelLoadButton;
	private JButton levelSaveButton;
	
	//EditorView
	private JPanel editorView;
	private JButton editorLayerNext;
	private JButton editorLayerPrev;
	private JTextField editorLayerField;
	private JLabel editorLabel;
	private int current_layer=0;
	
	//frame rate calculations
	private long prev_time=0;
	private int frames=0;
	
	//local references to other classes
	private Level level;
	private TextureList texture;
	
	//Window queue (to avoid stepping on another threads context)
	private boolean load_level;
	
	public Window(Level _level, TextureList _texture) {
		level = _level;
		texture = _texture;
		
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
		window = new JFrame();
		GLView = new Canvas();
		RightPane = new JPanel();
		
		textureView = new TextureView(RightPane.getWidth(), RightPane.getHeight(), texture);
		
		//LevelView
		levelView = new JPanel();
		levelLabel = new JLabel("Level Options");
		levelLoadButton = new JButton("Load");
		levelSaveButton = new JButton("Save");
		
		//EditorView
		editorView = new JPanel();
		editorLayerNext = new JButton("=>");
		editorLayerPrev = new JButton("<=");
		editorLayerField = new JTextField(String.valueOf(current_layer));
		editorLabel = new JLabel("Editor");		
		
		//layout the window
		chosenMode = new DisplayMode(targetWidth, targetHeight);
		window.setLayout(new FlowLayout());
		window.setSize( chosenMode.getWidth(), chosenMode.getHeight());
		window.add(GLView);
		window.add(RightPane);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
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
		RightPane.setLayout(new BorderLayout());
		RightPane.setBackground(new Color(0,0,0));
		RightPane.add(textureView, BorderLayout.NORTH);
		RightPane.add(Box.createRigidArea(new Dimension(0, 5)));
		RightPane.add(levelView, BorderLayout.WEST);
		RightPane.add(Box.createRigidArea(new Dimension(0, 5)));
		RightPane.add(editorView, BorderLayout.SOUTH);
		RightPane.setBorder(BorderFactory.createLineBorder(Color.red));
		
		//layout the Level panel
		setupLevelView();
		
		//layout the editor panel
		setupEditorView();
		
		window.validate();
		window.pack();
		window.setVisible(true);
		
		//create a display instance in GLView
		try {
			Display.setParent(GLView);
			Display.create();
		} catch (LWJGLException e) {
		    Sys.alert("Unable to create display.", e.toString());
		    System.exit(0);
		}

		//Force minimum size of window
		window.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent event) {
				final int initialWidth = (int) window.getSize().getWidth();
				final int initialHeight = (int) window.getSize().getHeight();
				System.out.print(window.getSize().getWidth() + " " + window.getSize().getHeight());
				window.setSize(
					Math.max(initialWidth, window.getWidth()),
					Math.max(initialHeight, window.getHeight())
				);
			}
		});
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
	
	
        
        
	
	//Contains editor commands pertaining to the level (currently saving and loading)
	public void setupLevelView() {
		//levelView.setLayout(new FlowLayout());
		//levelView.setAlignmentX(Component.LEFT_ALIGNMENT);
		levelView.setBorder(BorderFactory.createLineBorder(Color.green));
		levelView.setPreferredSize(new Dimension(RightPane.getWidth()-2, 200));
		levelView.add(levelLabel);
		levelView.add(levelLoadButton);
		levelView.add(levelSaveButton);
		
        levelSaveButton.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e)
            {
                //Execute when button is pressed
            	try {
					level.save();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
                System.out.println("You saved the level\n");
            }
        });  
        
        //http://forums.sun.com/thread.jspa?threadID=490317
        //http://mindprod.com/jgloss/swingthreads.html
        levelLoadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
				load_level = true;
            }
        });  
	}
	
	//Contains all current editor commands (currently layer)
	private void setupEditorView()
	{
		editorView.setBorder(BorderFactory.createLineBorder(Color.green));
		editorView.setPreferredSize(new Dimension(RightPane.getWidth()-2, 25));
		editorView.setLayout(new BoxLayout(editorView, BoxLayout.X_AXIS));
		editorView.add(editorLabel);
		editorView.add(editorLayerPrev);
		editorView.add(editorLayerField);
		editorView.add(editorLayerNext);

		editorLayerPrev.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                current_layer--;
                editorLayerField.setText(String.valueOf(current_layer));
            }
        });  
        
		editorLayerNext.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	current_layer++;
            	editorLayerField.setText(String.valueOf(current_layer));
            }
        });  
	}
	
	public JFrame getWindow() {
		return window;
	}
	
	public int getLayer() {
		return current_layer;
	}
	

	
	public boolean getLoadLevel() {
		if (load_level){
			load_level = false;
			return true;
		} else {
			return false;
		}
	}
}
