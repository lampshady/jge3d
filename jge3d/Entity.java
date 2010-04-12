package jge3d;

import javax.vecmath.Vector3f;

import org.lwjgl.opengl.GL11;

public class Entity {
	char type;
	Vector3f position = new Vector3f();
	String texture;
	private int[] objectlist;
	boolean collidable;
	Renderer render = new Renderer();
	
	public Entity(char _type, Vector3f _pos, String _texture, boolean _collidable) {
		type=_type;
		position=_pos;
		texture=_texture;
		collidable=_collidable;
	}
	
	public void Render(float cube_size) {
		objectlist[0] = GL11.glGenLists(1);
		
		GL11.glNewList(objectlist[0],GL11.GL_COMPILE);
			GL11.glPushMatrix();
				if(collidable == false) {
					render.drawcube(type, cube_size);
				}
				GL11.glPopMatrix();
				GL11.glTranslatef(position.x,-position.y,-position.z);
		GL11.glEndList();
	}

	public Vector3f getPosition() {
		return position;
	}
	
	public boolean getCollidable() {
		return collidable;
	}
}
