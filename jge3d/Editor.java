package jge3d;

import javax.vecmath.Vector3f;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;

public class Editor {
	Vector3f current_position_vector;
	Renderer render;
	public Editor() {
		current_position_vector = new Vector3f();
		render = new Renderer();
	}
	
	public void setCurrentBlock(int mouseX, int mouseY, float zPlane, Camera camera) throws LWJGLException {
		 current_position_vector = camera.getRayToPlane(mouseX, mouseY, 0); 
	}
	
	public void renderCurrentBlock() {
		GL11.glPushMatrix();
			current_position_vector.x = (float)Math.floor(current_position_vector.x);
			current_position_vector.y = (float)Math.floor(current_position_vector.y);
			
			//Z needs to be replaced with -layer * cube_size
			current_position_vector.z = (float)Math.floor(current_position_vector.z);
			GL11.glTranslatef(current_position_vector.x, current_position_vector.y, 0);
			render.drawcube(5, 1.0f);
		GL11.glPopMatrix();
	}
	
	public Entity getCurrentBlock() {
		String texture="";
		char type=1;
		return new Entity(type, current_position_vector, texture,true);
	}
}
