package jge3d;

import javax.vecmath.Vector3f;

import jge3d.render.Renderer;


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
	
	public Vector3f getCurrentPosition() throws LWJGLException {
		if(Input.getInstance().mouseInWindow()) {
			current_position_vector = 
				Renderer.getInstance().findRay.getRayToPlane(
					Input.getInstance().getMouseX(), 
					Input.getInstance().getMouseY(), 
					new Vector3f(0,0,1), 
					new Vector3f(0,0,0)
				);
		}
		return current_position_vector;
	}
	
	public Entity getCurrentBlock() {
		EntityList.getInstance().setPositionX("editor", current_position_vector.x);
		EntityList.getInstance().setPositionY("editor", current_position_vector.y);
		EntityList.getInstance().setPositionZ("editor", current_position_vector.z);
		EntityList.getInstance().setType("editor", (String)"level");
		EntityList.getInstance().setTexture_name("editor", "dirt2");
		EntityList.getInstance().setCollidable("editor", true);
		EntityList.getInstance().setTTL("editor", 0);
		
		return EntityList.getInstance().getEntityByName("editor");
	}
}
