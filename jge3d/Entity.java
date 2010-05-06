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
	private boolean transparent = false;
	private float alpha=1.0f;
	private static String[] keys = {"name","type","positionX","positionY","positionZ","transparent","alpha","texture_name","collidable","size","ttl"};
	protected static int num_entities=0;
	
	public Entity() {
		name="ent" + num_entities++;
	}
	
	public Entity(String _type, Vector3f _pos, String _texture_name, boolean _collidable, int _ttl) {
		name="ent" + num_entities++;
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
		String out = 
			"Entity=" + name + "\n" +
				"\t" + "positionX=" + position.x + "\n" +
				"\t" + "positionY=" + position.y + "\n" +
				"\t" + "positionZ=" + position.z + "\n" +
				"\t" + "transparent=" + transparent + "\n" +
				"\t" + "alpha=" + alpha + "\n" +
				"\t" + "texture_name=" + texture_name + "\n" +
				"\t" + "collidable=" + collidable + "\n" +
				"\t" + "size=" + size + "\n" +
			"/Entity\n";
			
		return out;
	}
	
	public Boolean getTransparent() {
		return transparent;
	}
	
	public float getAlpha() {
		return alpha;
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
		position.y = y;
	}
	
	public void setPositionZ(Float z) {
		position.z = z;
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
	
	public void setTransparent(Boolean _transparent) {
		transparent=_transparent;
	}
	
	public void setAlpha(Float _alpha) {
		alpha=_alpha;
	}
	/*
	public void set(String key, Object value) {
		if(key.equals("name"))
			setName((String)name);
		else if(key.equals("type"))
        	setType((String)value);
        else if(key.equals("positionX"))
        	setPositionX((Float)value);
    	else if(key.equals("positionY"))
    		setPositionY((Float)value);
		else if(key.equals("positionZ"))
			setPositionZ((Float)value);
		else if(key.equals("transparent"))
			setTransparent((Boolean)value);
		else if(key.equals("alpha"))
			setAlpha((Float)value);
		else if(key.equals("texture_name"))
			setTextureName((String)value);
		else if(key.equals("collidable"))		
			setCollidable((Boolean)value);
		else if(key.equals("size"))
			setSize((Float)value);
		else if(key.equals("ttl"))
			setTTL((Integer)value);
		else {
			System.out.print("SSHHIIITTTTT!!!! Entity parsing error");
		}
	}
	*/
	public void set(String key, String value) {
		if(key.equals("name"))
			setName(name);
		else if(key.equals("type"))
        	setType(value);
        else if(key.equals("positionX"))
        	setPositionX(Float.valueOf(value));
    	else if(key.equals("positionY"))
    		setPositionY(Float.valueOf(value));
		else if(key.equals("positionZ"))
			setPositionZ(Float.valueOf(value));
		else if(key.equals("transparent"))
			setTransparent(Boolean.valueOf(value));
		else if(key.equals("alpha"))
			setAlpha(Float.valueOf(value));
		else if(key.equals("texture_name"))
			setTextureName(value);
		else if(key.equals("collidable"))		
			setCollidable(Boolean.valueOf(value));
		else if(key.equals("size"))
			setSize(Float.valueOf(value));
		else if(key.equals("ttl"))
			setTTL(Integer.valueOf(value));
		else {
			System.out.print("SSHHIIITTTTT!!!! Entity parsing error");
		}
	}
	
	public static String[] getKeys() {
		return keys;
	}
	//private Vector3f axisLimits;
	//private Vector3f angleLimits;
	
}
