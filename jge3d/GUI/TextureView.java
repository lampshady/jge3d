package jge3d.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import jge3d.TextureList;


public class TextureView extends JPanel{
	JLabel textureLabel;
	JLabel preview;
	JScrollPane treeScroll;
	JTree textureTree;
	JButton textureAddButton;
	JButton textureDelButton;
	private TextureList textureList;
	
	int width;
	int height;
	
	//JTree controls
	private DefaultMutableTreeNode textureRootNode;
	private DefaultTreeModel textureTreeModel;
	private int texture_index=0;
	private String textureTreeCurrentSelection="cube1";
		
	public void clear()
	{
		textureRootNode.removeAllChildren();
		texture_index = 0;
	}
	
	TextureView(int w, int h, TextureList tlist)
	{
		textureList = tlist;
		width = w;
		height = h;
		
		//TextureView
		textureLabel = new JLabel("Texture Viewer");
		textureAddButton = new JButton("Add");
		textureDelButton = new JButton("Remove");
		preview = new JLabel();
		initTree();
		
		setupTextureView();
		
		/*
		textureLabel.setVisible(true);
		textureAddButton.setVisible(true);
		textureDelButton.setVisible(true);
		preview.setVisible(true);
		*/
	}
	
	public void setupTextureView() 
	{
		this.setBorder(BorderFactory.createLineBorder(Color.green));
		//textureView.setPreferredSize(new Dimension(RightPane.getWidth(), 500));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(textureLabel);
		this.add(preview);
		this.add(Box.createRigidArea(new Dimension(0, 5)));
		this.add(treeScroll);
		this.add(Box.createRigidArea(new Dimension(0, 5)));
		this.add(textureAddButton);
		this.add(textureDelButton);
		preview.setPreferredSize(new Dimension(128, 128));
		preview.setIcon(new ImageIcon("lib/Textures/cube1.png"));

		textureAddButton.addActionListener(new ActionListener() 
		{
            public void actionPerformed(ActionEvent e) 
            {
                final JFileChooser fc_texture = new JFileChooser("lib/Textures/");
				fc_texture.showOpenDialog(getRootPane());
                System.out.println("You loaded the texture:" + fc_texture.getSelectedFile().getPath() + "\n");

                //Draw image centered in the middle of the panel    
                preview.setIcon(new ImageIcon(fc_texture.getSelectedFile().getPath()));
                insertTexture(fc_texture.getSelectedFile().getName());
            }
        });
		
		textureDelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("You deleted the texture\n");
            }
        });  
        
    	textureTree.addTreeSelectionListener(new TreeSelectionListener() {
    	    public void valueChanged(TreeSelectionEvent e) {
    	        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
    	        textureTree.getLastSelectedPathComponent();

    	    /* if nothing is selected */ 
    	        if (node == null) return;

    	        if (node.toString() == textureRootNode.toString()) return;
    	        
    	    /* retrieve the node that was selected */ 
    	        //Object nodeInfo = node.getUserObject();
    	        preview.setIcon(new ImageIcon(textureList.getDataByName(node.toString()).getPath()));
    	        textureTreeCurrentSelection = node.toString();
    	    }
    	});
	}
	
	private void initTree() {
		textureRootNode = new DefaultMutableTreeNode("Textures");
		textureTreeModel = new DefaultTreeModel(textureRootNode);
		textureTreeModel.addTreeModelListener(new textureTreeModelListener());

		textureTree = new JTree(textureTreeModel);
		textureTree.setEditable(true);
		textureTree.getSelectionModel().setSelectionMode
		        (TreeSelectionModel.SINGLE_TREE_SELECTION);
		textureTree.setShowsRootHandles(true);
		
		treeScroll = new JScrollPane(textureTree);
		treeScroll.setPreferredSize(new Dimension(width, 100));
	}
	
	public void insertTexture(String name) {
		textureTreeModel.insertNodeInto(
			new DefaultMutableTreeNode(name),
			textureRootNode,
			texture_index
		);
		++texture_index;
	}
	
	public String getSelectedTexture() {
		return textureTreeCurrentSelection;
	}
	
	//Defines the actions taken when something happens to the tree
	class textureTreeModelListener implements TreeModelListener {
	    public void treeNodesChanged(TreeModelEvent e) {
	        DefaultMutableTreeNode node;
	        node = (DefaultMutableTreeNode)
	                 (e.getTreePath().getLastPathComponent());
	        /*
	         * If the event lists children, then the changed
	         * node is the child of the node we have already
	         * gotten.  Otherwise, the changed node and the
	         * specified node are the same.
	         */
	        try {
	            int index = e.getChildIndices()[0];
	            node = (DefaultMutableTreeNode)
	                   (node.getChildAt(index));
	        } catch (NullPointerException exc) {}

	        System.out.println("New value: " + node.getUserObject());
	    }
	    public void treeNodesInserted(TreeModelEvent e) {
	    }
	    public void treeNodesRemoved(TreeModelEvent e) {
	    }
	    public void treeStructureChanged(TreeModelEvent e) {
	    }
	}
}
