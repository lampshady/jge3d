package jge3d;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;


public class Window {
	//opening the window
	public Window()
	{
		JFrame window = new JFrame();
		JPanel leftPane = new JPanel();
		JPanel topRightPane = new JPanel();
		JPanel bottomRightPane = new JPanel();
		
		Toolkit toolkit = window.getToolkit();
		
		JSplitPane verticalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topRightPane, bottomRightPane);
		JSplitPane horizontalSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, leftPane, verticalSplit);

		horizontalSplit.setRightComponent(verticalSplit);
		verticalSplit.setRightComponent(bottomRightPane);
		
		Dimension screenSize = toolkit.getScreenSize();
		
		verticalSplit.setDividerLocation((int)(screenSize.getHeight())/2);
		horizontalSplit.setDividerLocation((int)(screenSize.getWidth()*0.75));
		
		window.add(horizontalSplit);
		
		window.setExtendedState(Frame.MAXIMIZED_BOTH);
		
		window.setVisible(true);
	}
}
