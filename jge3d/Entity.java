package jge3d;

import javax.vecmath.Vector3f;

public class Entity {
	char type;
	Vector3f position = new Vector3f();
	String texture;
	
	public Entity(char _type, Vector3f _pos, String _texture) {
		type=_type;
		position=_pos;
		texture=_texture;
	}

	
}
