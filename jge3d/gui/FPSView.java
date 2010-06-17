package jge3d.gui;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jge3d.Input;
import jge3d.physics.Physics;
import jge3d.render.Renderer;

public class FPSView extends JPanel {
	private static final long serialVersionUID = 1L;
	private static FPSView uniqueInstance = new FPSView();
	
	long prev_time_render;
	long prev_time_physics;
	long prev_time_input;
	long prev_time_main;
	
	//Level controls
	private JLabel rendererFPS;
	private JLabel physicsFPS;
	private JLabel inputFPS;
	private JLabel mainFPS;

	public static FPSView getInstance(){ 
		return uniqueInstance; 
	}
	
	private FPSView() {
		//LevelView
		rendererFPS = new JLabel("Renderer");
		physicsFPS = new JLabel("Physics");
		inputFPS = new JLabel("Input");
		mainFPS = new JLabel("Main");

		//layout the Level panel
		setupFPSView();
	}	
	
	//Contains editor commands pertaining to the level (currently saving and loading)
	public void setupFPSView() {
		this.setBorder(BorderFactory.createLineBorder(Color.green));
		//this.setPreferredSize(new Dimension(super.getWidth()-10, 200));
		this.add(rendererFPS);
		this.add(physicsFPS);
		this.add(inputFPS);
		this.add(mainFPS);
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}

	public void updateFPS() {
		//Update the frame-counter in the title bar if it has been a second or more
		if ( (System.currentTimeMillis()-prev_time_render) >= 1000 ) {
			rendererFPS.setText("RenderFPS:\t" + ((Renderer.getInstance().getFrames()*1000)/(System.currentTimeMillis()-prev_time_render)) );
			prev_time_render=System.currentTimeMillis();
			Renderer.getInstance().resetFrames();
		}
		if ( (System.currentTimeMillis()-prev_time_physics) >= 1000 ) {
			physicsFPS.setText("PhysicsFPS:\t" + ((Physics.getInstance().getFrames()*1000)/(System.currentTimeMillis()-prev_time_physics)) );
			prev_time_physics=System.currentTimeMillis();
			Physics.getInstance().resetFrames();
		}
		if ( (System.currentTimeMillis()-prev_time_input) >= 1000 ) {
			inputFPS.setText("InputFPS:\t" + ((Input.getInstance().getFrames()*1000)/(System.currentTimeMillis()-prev_time_input)) );
			prev_time_input=System.currentTimeMillis();
			Input.getInstance().resetFrames();
		}
		//if ( (System.currentTimeMillis()-prev_time_main) >= 1000 ) {
		//	mainFPS.setText("MainFPS:\t" + ((Main.getInstance().getFrames()*1000)/(System.currentTimeMillis()-prev_time_main)) );
		//	prev_time_main=System.currentTimeMillis();
		//	frames = 0;
		//}
	}
}
