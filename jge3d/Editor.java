package jge3d;

import javax.vecmath.Vector3f;


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
				Camera.getInstance().getRayToPlane(
					Input.getInstance().getMouseX(), 
					Input.getInstance().getMouseY(), 
					new Vector3f(0,0,1), 
					new Vector3f(0,0,0)
				);
		}
		return current_position_vector;
	}
	
	public Entity getCurrentBlock() {
		Entity ent = new Entity();
		ent.setPositionX(current_position_vector.x);
		ent.setPositionY(current_position_vector.y);
		ent.setPositionZ(current_position_vector.z);
		ent.setType("level");
		ent.setTexture_name("dirt2");
		ent.setCollidable(true);
		ent.setTTL(0);
		
		return ent;
	}
}
