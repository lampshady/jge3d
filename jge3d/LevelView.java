package jge3d;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.lwjgl.LWJGLException;

public class LevelView extends JPanel{
	static JPanel levelView;
	static JLabel levelLabel;
	static JButton levelLoadButton;
	static JButton levelSaveButton;
	
	LevelView(Level level)
	{
		this.setLayout(new FlowLayout());
		this.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.add(levelLabel);
		this.add(levelLoadButton);
		this.add(levelSaveButton);
		
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
        
        levelLoadButton.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e)
            {
                //Execute when button is pressed
            	try {
					level.load();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (LWJGLException e1) {
					e1.printStackTrace();
				}
                System.out.println("You loaded the level\n");
            }
        });  
	}
}
