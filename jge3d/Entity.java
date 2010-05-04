package jge3d;

import javax.vecmath.Vector3f;

import com.bulletphysics.dynamics.RigidBody;

public class Entity {
	private String name;
	private String type;
	private Vector3f position = new Vector3f();
	private String texture_name;
	private boolean collidable;
	private float size;
	private RigidBody phys_body;
	private int ttl;
	private long created_at;
	private Physics physics;
	private static String[] keys = {"type", "postionX","positionY","positionZ","texture_name","collidable","size","ttl"};
	public Entity(String _type, Vector3f _pos, String _texture_name, boolean _collidable, int _ttl) {
		type=_type;
		position=_pos;
		texture_name=_texture_name;
		collidable=_collidable;
		size=1.0f;
		ttl=_ttl;
		created_at=System.currentTimeMillis();
	}

	
	public Entity(String _type, Vector3f _pos, String _texture_name, boolean _collidable, Physics _physics, RigidBody rb, int _ttl) {
		physics=_physics;
		type=_type;
		position=_pos;
		texture_name=_texture_name;
		collidable=_collidable;
		size=1.0f;
		phys_body=rb;
		ttl=_ttl;
		created_at=System.currentTimeMillis();
	}

	public Vector3f getPosition() {
		return position;
	}
	public float getPositionX() {
		return position.x;
	}
	public float getPositionY() {
		return position.y;
	}
	public float getPositionZ() {
		return position.z;
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
	
	public float getSize() {
		return size;
	}
	
	public String getType() {
		return type;
	}
	
	public int getTTL() {
		return ttl;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String _name) {
		name=_name;
	}
	
	public void deletePhysics() {
		physics.getDynamicsWorld().removeRigidBody(phys_body);
	}
	
	public boolean isDead() {
		if( (System.currentTimeMillis() >= (created_at+ttl)) && (ttl != 0) ) {
			//System.out.print("RIP\n===\nBorn: " + created_at +
			//"\nDied:" +  System.currentTimeMillis() +  "\n" + "Lived: " +
			//((System.currentTimeMillis()-created_at)/1000.0f) + " sec\n");
			return true;
		} else {
			return false;
		}
	}
	
	public void setPositionX(Float x) {
		position.x = x;
	}
	
	public void setPositionY(Float y) {
		position.x = y;
	}
	
	public void setPositionZ(Float z) {
		position.x = z;
	}
	
	public void setTextureName(String name) {
		texture_name = name;
	}
	
	public void setCollidable(Boolean _collidable) {
		collidable = _collidable;
	}
	
	public void setType(String _type) {
		type=_type;
	}
	
	public void setMass(float mass) {
		phys_body.setMassProps(mass, new Vector3f(0,0,0));
	}
	
	public void setDamping(float lin_damping, float ang_damping) {
		phys_body.setDamping(lin_damping, ang_damping);
	}
	
	public void setFriction(float friction) {
		phys_body.setFriction(friction);
	}
	
	public void setSize(float _size) {
		size = _size;
	}
	
	public void setTTL(Integer _ttl) {
		ttl=_ttl;
	}
	
	public static String[] getKeys() {
		return keys;
	}
	//private Vector3f axisLimits;
	//private Vector3f angleLimits;
	
}
