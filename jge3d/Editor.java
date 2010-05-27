package jge3d;

import javax.vecmath.Vector3f;

import jge3d.GUI.TextureView;

import org.lwjgl.LWJGLException;

public class Editor {
	private Vector3f current_position_vector;
	private static Editor uniqueInstance = new Editor();
	
	public static Editor getInstance()
	{
		return uniqueInstance;
	}
	
	private Editor(){
		current_position_vector = new Vector3f();
	}
	
	public void setCurrentBlock(int mouseX, int mouseY, float zPlane) throws LWJGLException {
		 current_position_vector = Camera.getInstance().getRayToPlane(mouseX, mouseY, new Vector3f(0,0,1), new Vector3f(0,0,0));
				// window.getEditorView().getLayerNormal(), window.getEditorView().getLayerPoint()); 
	}
	
	public Vector3f getCurrentPosition() {
		return current_position_vector;
	}
	
	public Entity getCurrentBlock() {
		return new Entity("level",
			current_position_vector,
			TextureView.getInstance().getSelectedTexture(),
			true,
			0	//replace with TTL from ent browser
		);
	}
}
