package jge3d;

import javax.vecmath.Vector3f;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;

public class Editor {
	Vector3f editor_origin;
	Renderer render;
	public Editor() {
		editor_origin = new Vector3f();
		render = new Renderer();
	}
	
	public void setCurrentBlock(int x, int y, Camera camera) throws LWJGLException {
		editor_origin = camera.getRayTo(x, y);
	}
	
	public void renderCurrentBlock() {
		GL11.glPushMatrix();
			editor_origin.x = (float)Math.floor(editor_origin.x);
			editor_origin.y = (float)Math.floor(editor_origin.y);
			
			//Z needs to be replaced with -layer * cube_size
			editor_origin.z = (float)Math.floor(editor_origin.z);
			
			GL11.glTranslatef(editor_origin.x, editor_origin.y, 0);
			System.out.print(editor_origin.x + " " + editor_origin.y + " " + 0 + "\n");
			render.drawcube(5, 1.0f);
		GL11.glPopMatrix();
	}
}
