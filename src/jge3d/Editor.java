package jge3d;

import javax.vecmath.Vector3f;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;

public class Editor {
	Vector3f editor_origin;
	
	public Editor() {
		editor_origin = new Vector3f();
	}
	
	public void setCurrentBlock(int x, int y, Camera camera) throws LWJGLException {
		editor_origin = camera.getRayTo(x, y);
	}
	
	public void renderCurrentBlock() {
		GL11.glPushMatrix();
			GL11.glTranslatef(editor_origin.x, editor_origin.y, editor_origin.z);
			//render.drawcube(1, 1.0f);
		GL11.glPopMatrix();
	}
}
