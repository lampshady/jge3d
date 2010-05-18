package jge3d.GUI;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jge3d.Level;

public class LevelView extends JPanel {
	private static final long serialVersionUID = -6074813268227221065L;
	
	//Level controls
	private JLabel levelLabel;
	private JButton levelLoadButton;
	private JButton levelSaveButton;
	
	//Window queue (to avoid stepping on another threads context)
	private boolean load_level;
	
	//Holds the reference to the level (for saving and loading)
	Level level;
	
	public LevelView(Level _level) {
		//LevelView
		levelLabel = new JLabel("Level Options");
		levelLoadButton = new JButton("Load");
		levelSaveButton = new JButton("Save");
		
		level = _level;
		
		//layout the Level panel
		setupLevelView();
	}	
	
	//Contains editor commands pertaining to the level (currently saving and loading)
	public void setupLevelView() {
		this.setBorder(BorderFactory.createLineBorder(Color.green));
		//this.setPreferredSize(new Dimension(super.getWidth()-10, 200));
		this.add(levelLabel);
		this.add(levelLoadButton);
		this.add(levelSaveButton);
		
        levelSaveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Execute when button is pressed
            	try {
					level.save();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
                System.out.println("You saved the level\n");
            }
        });  

        levelLoadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
				load_level = true;
            }
        });  
	}


	public boolean getLoadLevel() {
		if (load_level){
			load_level = false; 
			return true;
		} else {
			return false;
		}
	}
	public void setLoadLevel(boolean _load_level) {
		load_level = _load_level;
	}
}
