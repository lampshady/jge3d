package jge3d;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.vecmath.Vector3f;

import jge3d.physics.Physics;

import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

public class Entity {
	private String name = "";
	private String type = "";
	private float mass = 0.0f;
	private Vector3f position = new Vector3f();
	private Vector3f gravity = new Vector3f();
	private String texture_name = "";
	private boolean collidable=true;
	private float size=1.0f;
	protected RigidBody phys_body = null;
	private int ttl=0;
	private long created_at = 0;
	private boolean transparent = false;
	private float alpha=1.0f;
	
	//publicly available keys
	protected static String[] keys = {"name","type","positionX","positionY","positionZ","gravityX","gravityY","gravityZ","mass","transparent","alpha","texture_name","collidable","size","TTL"};
	
	protected static int num_entities=0;
	
	public Entity() {
		num_entities++;
		name="ent" + num_entities;
		created_at = System.nanoTime();
	}
	
	public Entity(String _name) {
		num_entities++;
		name=_name;
		created_at = System.nanoTime();
	}

	public void addToPhysics() {
		//Only add the entity to the physics list if it's collidable
		if(collidable == true) {
			//Set rigidbody with the one returned from the physics engine
			setRigidBody(
				//Create a physics object for the entity
				Physics.getInstance().addLevelBlock(
					position.x,
					position.y,
					position.z,
					size
				)
			);
		}
	}
	
	public String getName() {
		return name;
	}
	public String getType() {
		return type;
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
	public float getGravityX() {
		return gravity.x;
	}
	public float getGravityY() {
		return gravity.y;
	}
	public float getGravityZ() {
		return gravity.z;
	}
	public float getMass() {
		return mass;
	}
	public boolean getCollidable() {
		return collidable;
	}
	public String getTexture_name() {
		return texture_name;
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
	public int getTTL() {
		return ttl;
	}

	public void setName(String _name) {
		try {
			name=_name;
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.print("Incorrect data type for name, must be String\n");
		}
	}
	public void setType(Object _type) {
		try {
			type=_type.toString();
			if(type.equals("level")) {
				addToPhysics();
			}
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.print("Incorrect data type for type, must be String\n");
		}
	}
	public void setPositionX(Object x) {
		try {
			position.x = (float)Float.parseFloat(x.toString());
			updatePosition();
		} catch (Exception e) {
			System.out.print(position.x + "<<Incorrect data type for positionX, must be Float\n");
			e.printStackTrace();
		}
	}
	public void setPositionY(Object y) {
		try {
			position.y = (float)Float.parseFloat(y.toString());
			updatePosition();
		} catch (Exception e) {
			System.out.print(position.y + "<<Incorrect data type for positionY, must be Float\n");
			e.printStackTrace();
		}
	}
	public void setPositionZ(Object z) {
		try {
			position.z = (float)Float.parseFloat(z.toString());
			updatePosition();
		} catch (Exception e) {
			System.out.print(position.z + "<<Incorrect data type for positionZ, must be Float\n");
			e.printStackTrace();
		}
	}
	public void setGravityX(Object x) {
		try {
			gravity.x = (float)Float.parseFloat(x.toString());
			updatePhysics();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.print("Incorrect data type for gravityX, must be Float\n");
		}
		
	}
	public void setGravityY(Object y) {
		try {
			gravity.y = (float)Float.parseFloat(y.toString());
			updatePhysics();
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.print("Incorrect data type for gravityY, must be Float\n");
		}
	}
	public void setGravityZ(Object z) {
		try {
			gravity.z = (float)Float.parseFloat(z.toString());
			updatePhysics();
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.print("Incorrect data type for gravityZ, must be Float\n");
		}
	}
	public void setTexture_name(Object name) {
		try {
			texture_name = name.toString();
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.print("Incorrect data type for name, must be String\n");
		}
	}
	public void setCollidable(Object _collidable) {
		try {
			collidable = Boolean.parseBoolean(_collidable.toString());
			addToPhysics();
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.print("Incorrect data type for collidable, must be Boolean\n");
		}
	}
	public void setMass(Object _mass) {
		try {
			mass = Float.parseFloat(_mass.toString());
			updatePhysics();
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.print("Incorrect data type for mass, must be Float\n");
		}
	}
	public void setDamping(float lin_damping, float ang_damping) {
		try {
			phys_body.setDamping(lin_damping, ang_damping);
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.print("Incorrect data type for damping, must be Float,Float\n");
		}
	}
	public void setFriction(Object friction) {
		try {
			phys_body.setFriction(Float.valueOf(friction.toString()));
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.print("Incorrect data type for friction, must be Float\n");
		}
	}
	public void setRigidBody(RigidBody rb) {
		phys_body = rb;
	}
	public void setSize(Object _size) {
		try {
			size = Float.valueOf(_size.toString());
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.print("Incorrect data type for size, must be Float\n");
		}
	}
	public void setTTL(Integer _ttl) {
		try {
			ttl=Integer.valueOf(_ttl.toString());
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.print("Incorrect data type for TTL, must be Integer(seconds)\n");
		}
	}
	public void setTransparent(Boolean _transparent) {
		try {
			transparent=Boolean.parseBoolean(_transparent.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void setAlpha(Float _alpha) {
		try {
			alpha=Float.parseFloat(_alpha.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void finalize() {
		addToPhysics();
		updatePosition();
		addToPhysics();
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

	//Arbitrary functions
	//Needs replaced.
	public void set(String key, String value) {
		if(key.equals("name"))
			setName(value);
		else if(key.equals("type"))
        	setType(value);
        else if(key.equals("positionX"))
        	setPositionX(Float.valueOf(value));
    	else if(key.equals("positionY"))
    		setPositionY(Float.valueOf(value));
		else if(key.equals("positionZ"))
			setPositionZ(Float.valueOf(value));
        else if(key.equals("gravityX"))
        	setGravityX(Float.valueOf(value));
    	else if(key.equals("gravityY"))
    		setGravityY(Float.valueOf(value));
		else if(key.equals("gravityZ"))
			setGravityZ(Float.valueOf(value));
		else if(key.equals("mass"))
			setMass(Float.valueOf(value));
		else if(key.equals("transparent"))
			setTransparent(Boolean.valueOf(value));
		else if(key.equals("alpha"))
			setAlpha(Float.valueOf(value));
		else if(key.equals("texture_name"))
			setTexture_name(value);
		else if(key.equals("collidable"))		
			setCollidable(Boolean.valueOf(value));
		else if(key.equals("size"))
			setSize(Float.valueOf(value));
		else if(key.equals("TTL"))
			setTTL(Integer.valueOf(value));
		else {
			System.out.print("SSHHIIITTTTT!!!! Entity parsing error [" + key + "," + value + "]\n");
		}
	}
	
	public void deletePhysics() {
		Physics.getInstance().getDynamicsWorld().removeRigidBody(phys_body);
	}
	
	public String toString() {
		Class<?> c;
		//Field[] allFields = null;
		//String fname;
		String output = new String(); 
		try {
			c = Class.forName("jge3d.Entity");
			output="\tEntity=" + name + "\n";
			for(String key: keys) {
				String method_string = 
					"get" + 
					key.substring(0,1).toUpperCase() + 
					key.substring(1);

				Method m = c.getDeclaredMethod(method_string);
				output = output + "\t\t" + key + "=" + m.invoke(this).toString() + "\n";
			}
			output=output + "\t/Entity\n";
			
			/* //Skynet version
			allFields = c.getDeclaredFields();
			for (Field f : allFields) {
				if(f.getModifiers()==Modifier.PRIVATE) {
					fname = f.getName();
					String method_string = 
						"get" + 
						fname.substring(0,1).toUpperCase() + 
						fname.substring(1);
					System.out.print(method_string + "\n");
					Method m = c.getDeclaredMethod(method_string);
					output = m.invoke(this).toString();				
				}
			}*/
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		return output;
		/*
		String out = 
			"\tEntity=" + name + "\n" +
				"\t\t" + "type=" + type + "\n" +
				"\t\t" + "positionX=" + position.x + "\n" +
				"\t\t" + "positionY=" + position.y + "\n" +
				"\t\t" + "positionZ=" + position.z + "\n" +
				"\t\t" + "gravityX=" + gravity.x + "\n" +
				"\t\t" + "gravityY=" + gravity.y + "\n" +
				"\t\t" + "gravityZ=" + gravity.z + "\n" +
				"\t\t" + "mass=" + mass + "\n" +
				"\t\t" + "transparent=" + transparent + "\n" +
				"\t\t" + "alpha=" + alpha + "\n" +
				"\t\t" + "texture_name=" + texture_name + "\n" +
				"\t\t" + "collidable=" + collidable + "\n" +
				"\t\t" + "size=" + size + "\n" +
				"\t\t" + "TTL=" + ttl + "\n" +
			"\t/Entity\n";
			
		return out;
		*/
	}
	
	public void updateAll() {
		updatePosition();
		updatePhysics();
	}
	
	public void updatePosition() {
		if(phys_body != null) {
			Transform trans = new Transform();
			trans.setIdentity();
			trans.origin.set(position);
			DefaultMotionState myMotionState = new DefaultMotionState(trans);
			if(phys_body != null) 
				phys_body.setMotionState(myMotionState);
		}
	}
	
	public void updatePhysics() {
		if(phys_body != null) {
			phys_body.setGravity(gravity);
			phys_body.setMassProps(mass, new Vector3f(0,0,0));
		}
	}
	
	public static String[] getKeys() {
		return keys;
	}
}
