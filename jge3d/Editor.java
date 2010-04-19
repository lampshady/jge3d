package jge3d;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.vecmath.Vector3f;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;

public class Editor {
	private Vector3f current_position_vector;
	private Renderer render;
	private Window window;
	
	public Editor(Renderer _render, Window _window) {
		current_position_vector = new Vector3f();
		render = _render;
		window = _window;
	}
	
	public void setCurrentBlock(int mouseX, int mouseY, float zPlane, Camera camera) throws LWJGLException {
		 current_position_vector = camera.getRayToPlane(mouseX, mouseY, window.getLayer()); 
	}
	
	public void renderCurrentBlock() throws FileNotFoundException, IOException, LWJGLException {
		GL11.glPushMatrix();
			current_position_vector.x = (float)Math.floor(current_position_vector.x);
			current_position_vector.y = (float)Math.floor(current_position_vector.y);
			
			//Z needs to be replaced with -layer * cube_size
			current_position_vector.z = (float)Math.floor(current_position_vector.z);
			GL11.glTranslatef(current_position_vector.x, current_position_vector.y, -current_position_vector.z);
			render.drawcube("cube1", 1.0f);
		GL11.glPopMatrix();
	}
	
	public Entity getCurrentBlock() {
		String texture="brick2";
		char type='L';
		return new Entity(type, current_position_vector, texture,true);
	}
}
