package jge3d;

import javax.vecmath.Vector3f;

public class Entity {
	char type;
	Vector3f position = new Vector3f();
	String texture_name;
	boolean collidable;
	Renderer render = new Renderer();
	
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
}
