package jge3d;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

public class TextureView extends JPanel{

	JLabel preview;
	
	JFrame window;
	JLabel textureLabel;
	JButton textureAddButton;
	JButton textureDelButton;
	JList textureListBox;
	JPanel texturePreview;
		
	TextureView(final JFrame attachedTo) 
	{
		//textureView.removeAll();
		//layout TextureView
		
		window = attachedTo;
		textureLabel = new JLabel("Texture Viewer");
		textureAddButton = new JButton("Add");
		textureDelButton = new JButton("Remove");
		textureListBox = new JList();
		texturePreview = new JPanel();
		
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setAlignmentX(BoxLayout.Y_AXIS);
		this.add(textureLabel);
		this.add(texturePreview);
		this.add(textureListBox);
		this.add(textureAddButton);
		this.add(textureDelButton);
		

		texturePreview.setSize(128, 128);
		texturePreview.setVisible(true);
		
		textureAddButton.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e)
            {
                final JFileChooser fc_level = new JFileChooser("lib/Textures/");
				fc_level.showOpenDialog(window);
				Image image = Toolkit.getDefaultToolkit().getImage(fc_level.getSelectedFile().getPath());
                System.out.println("You loaded the texture:" +fc_level.getSelectedFile().getPath() + "\n");
                
                // Use the image width & height to find the starting point
                int imgX = 128/2 - 128;//image.getWidth(IO);
                int imgY = 128/2 - 128;//image.getHeight(IO);

                //Draw image centered in the middle of the panel    
                texturePreview.getGraphics().drawImage (image, imgX, imgY, texturePreview);
            }
        });  
        
        textureDelButton.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e)
            {
            	
                System.out.println("You deleted the texture\n");
            }
        });  
		
		
	}
}
