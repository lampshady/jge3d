package jge3d;

import javax.vecmath.Vector3f;

import jge3d.GUI.Window;

import org.lwjgl.LWJGLException;

public class Editor {
	private Vector3f current_position_vector;
	private Window window;
	
	public Editor(Renderer _render, Window _window) {
		current_position_vector = new Vector3f();
		window = _window;
	}
	
	public void setCurrentBlock(int mouseX, int mouseY, float zPlane, Camera camera) throws LWJGLException {
		 current_position_vector = camera.getRayToPlane(mouseX, mouseY, new Vector3f(0,0,1), new Vector3f(0,0,0));
				// window.getEditorView().getLayerNormal(), window.getEditorView().getLayerPoint()); 
	}
	
	public Vector3f getCurrentPosition() {
		return current_position_vector;
	}
	
	public Entity getCurrentBlock() {
		return new Entity('L',
			current_position_vector,
			window.getTextureView().getSelectedTexture(),
			true,
			0	//replace with TTL from ent browser
		);
	}
}
