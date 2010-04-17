package jge3d;

import javax.vecmath.Vector3f;

public class Entity {
	private char type;
	private Vector3f position = new Vector3f();
	private String texture_name;
	private boolean collidable;
	
	public Entity(char _type, Vector3f _pos, String _texture_name, boolean _collidable) {
		type=_type;
		position=_pos;
		texture_name=_texture_name;
		collidable=_collidable;
	}

	public Vector3f getPosition() {
		return position;
	}
	
	public boolean getCollidable() {
		return collidable;
	}
	
	public String getTextureName() {
		return texture_name;
	}
	
	public String toString() {
		return type + ";" + (int)position.x + "," + (int)position.y + "," + (int)position.z + ";" + texture_name;
	}
}
