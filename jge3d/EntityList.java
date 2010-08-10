package jge3d;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.vecmath.Vector3f;

import com.bulletphysics.dynamics.RigidBody;

public class EntityList {
	private HashMap<String,Entity> names;
	private HashMap<String,ArrayList<Entity>> types;
	
	static EntityList uniqueInstance = new EntityList();
	private ArrayList<Entity> changed;
	private Entity latest_ent;
	
	public static EntityList getInstance()
	{
		return uniqueInstance;
	}
	
	private EntityList()
	{
		names = new HashMap<String,Entity>();
		types = new HashMap<String,ArrayList<Entity>>();
		setChanged(new ArrayList<Entity>());
	}
	
	public Entity addEntity(Entity entityToAdd )
	{
		//make certain an entity with that name doesn't already exist
		if(names.containsKey(entityToAdd.getName()) == false)
		{
			names.put(entityToAdd.getName(), entityToAdd);
			if( (types.get(entityToAdd.getType())) == null)
			{
				types.put(entityToAdd.getType(), new ArrayList<Entity>());
			}
			types.get(entityToAdd.getType()).add(entityToAdd);
			latest_ent = names.get(entityToAdd.getName());
			return names.get(entityToAdd.getName());
		}else
		{
			System.out.println("That entity already exists.");
			return null;
		}
	}
	
	private void removeEntity(Entity entity) {
		removeEntity(entity.getName(), entity.getType());
	}
	
	public boolean removeEntity(String entityName, String entityType)
	{
		if(names.get(entityName) != null)
		{
			//remove from types first
			types.get(entityType).remove(names.get(entityName));
			
			//then from names
			names.remove(entityName);
		
			//check if any entities of that type still exist
			if(types.get(entityType).size() == 0)
			{
				types.remove(entityType);
			}
			
			return true;
		}else
		{
			System.out.println("Tried to delete a nonexistant entity");
			return false;
		}
	}
	
	public boolean removeEntityByName( String entityName)
	{
		if(names.get(entityName) != null)
		{
			return removeEntity(entityName, names.get(entityName).getType());
		}else
		{
			System.out.println("Tried to remove a non-existant entity");
			return false;
		}
	}
	
	public Entity getEntityByName( String entityName )
	{
		return names.get(entityName);
	}
	
	public ArrayList<Entity> getEntitiesByType( String entityType )
	{
		return types.get(entityType);
	}
	
	public void clear()
	{
		names.clear();
		types.clear();
	}
	
	public Vector3f getEntityPosition(String entityName)
	{
		return names.get(entityName).getPosition();
	}
	
	public String getEntityTextureName(String entityName) {
		return names.get(entityName).getTexture_name();
	}
	
	public int getEntityCount() {
		return names.size();
	}
	
	public boolean getEntityCollidable(String entityName) {
		return names.get(entityName).getCollidable();
	}
	
	public void setRigidBodyByID(String entityName, RigidBody rigidBody) {
		names.get(entityName).setRigidBody(rigidBody);
	}
	
	public float getEntitySize(String entityName) {
		return names.get(entityName).getSize();
	}
	
	public void deleteByPosition(Vector3f position) {
		for( Map.Entry<String, Entity> entity : names.entrySet())
		{
			if( entity.getValue().getPosition().equals(position))
			{
				
				System.out.println("Found it: Deleting...");
				removeEntity(entity.getValue());
				System.out.println("Done.");
			}
		}
	}
	
	public void pruneEntities() {
		for( Map.Entry<String, Entity> entity : names.entrySet())
		{
			if( entity.getValue().isDead())
			{
				System.out.println("Found it: Deleting...");
				removeEntity(entity.getValue());
				System.out.println("Done.");
			}
		}
	}
	
	public String toString()
	{
		String listAsString = "";
		
		for( Map.Entry<String, Entity> entity : names.entrySet())
		{
			listAsString += entity.getValue().toString();
		}
		return listAsString;
	}
	
	public ArrayList<Entity> getEntities()
	{
		ArrayList<Entity> temp = new ArrayList<Entity>();
		for(Map.Entry<String, Entity> entry: names.entrySet())
		{
			temp.add(entry.getValue());
		}
		return temp;
	}
	
	public Entity getLatestEntity() {
		return latest_ent;
	}

	public boolean getListChanged() {
		if(getChanged().size()>0)
			return true;
		else
			return false;
	}
	
	public void setValue(String entityName, String valueName, Object value) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		if( !valueName.equals("Type") )
		{
			Method methodToCall = Entity.class.getDeclaredMethod(
				"set" + valueName.substring(0, 1).toUpperCase() + valueName.substring(1), 
				value.getClass()
			);
			methodToCall.invoke(names.get(entityName), value);
			getChanged().add(names.get(entityName));
		}else
		{
			types.get(names.get(entityName).getType()).remove(names.get(entityName));
			names.get(entityName).setType(value);
			types.get(names.get(entityName).getType()).add(names.get(entityName));
			getChanged().add(names.get(entityName));
		}
	}
	
	public Object getValue(String entityName, String valueName) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException 
	{
		Method methodToCall = Entity.class.getDeclaredMethod(
				"get" + valueName.substring(0, 1).toUpperCase() + valueName.substring(1)
			);
		return methodToCall.invoke(names.get(entityName));
	}

	public void setEntityName(String currentName, String newName) {
		names.get(currentName).setName(newName);
		getChanged().add(names.get(newName));
	}
	
	public void setEntityType(String entityName, String newType) {
		types.get(names.get(entityName).getType()).remove(names.get(entityName));
		names.get(entityName).setType(newType);
		types.get(names.get(entityName).getType()).add(names.get(entityName));
		getChanged().add(names.get(entityName));
	}
	
	public void setPositionX(String entityName, Object x) {
		names.get(entityName).setPositionX(x);
		getChanged().add(names.get(entityName));
	}
	public void setPositionY(String entityName, Object y) {
		names.get(entityName).setPositionY(y);
		getChanged().add(names.get(entityName));
	}
	public void setPositionZ(String entityName, Object z) {
		names.get(entityName).setPositionZ(z);
		getChanged().add(names.get(entityName));
	}
	public void setGravityX(String entityName, Object x) {
		names.get(entityName).setGravityX(x);
		getChanged().add(names.get(entityName));
	}
	public void setGravityY(String entityName, Object y) {
		names.get(entityName).setGravityY(y);
		getChanged().add(names.get(entityName));
	}
	public void setGravityZ(String entityName, Object z) {
		names.get(entityName).setGravityZ(z);
		getChanged().add(names.get(entityName));
		
	}
	public void setType(String entityName, Object type) {
		names.get(entityName).setType(type);
		getChanged().add(names.get(entityName));
	}
	public void setTexture_name(String entityName, Object name) {
		names.get(entityName).setTexture_name(name);
		getChanged().add(names.get(entityName));
	}
	public void setCollidable(String entityName, Object _collidable) {
		names.get(entityName).setCollidable(_collidable);
		getChanged().add(names.get(entityName));
	}
	public void setMass(String entityName, Object _mass) {
		names.get(entityName).setMass(_mass);
		getChanged().add(names.get(entityName));
	}
	public void setDamping(String entityName, float lin_damping, float ang_damping) {
		names.get(entityName).setDamping(lin_damping, ang_damping);
		getChanged().add(names.get(entityName));
	}
	public void setFriction(String entityName, Object friction) {
		names.get(entityName).setFriction(friction);
		getChanged().add(names.get(entityName));
	}
	public void setRigidBody(String entityName, RigidBody rb) {
		names.get(entityName).setRigidBody(rb);
		getChanged().add(names.get(entityName));
	}
	public void setSize(String entityName, Object _size) {
		names.get(entityName).setSize(_size);
		getChanged().add(names.get(entityName));
	}
	public void setTTL(String entityName, Integer _ttl) {
		names.get(entityName).setTTL(_ttl);
		getChanged().add(names.get(entityName));
	}
	public void setTransparent(String entityName, Boolean _transparent) {
		names.get(entityName).setTransparent(_transparent);
		getChanged().add(names.get(entityName));
	}
	public void setAlpha(String entityName, Float _alpha) {
		names.get(entityName).setAlpha(_alpha);
		getChanged().add(names.get(entityName));
	}

	public void setChanged(ArrayList<Entity> changed) {
		this.changed = changed;
	}

	public ArrayList<Entity> getChanged() {
		return changed;
	}
} 