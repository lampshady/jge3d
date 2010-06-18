package jge3d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.vecmath.Vector3f;

import com.bulletphysics.dynamics.RigidBody;

public class EntityList {
	private HashMap<String,Entity> names;
	private HashMap<String,ArrayList<Entity>> types;
	
	static EntityList uniqueInstance = new EntityList();
	
	public static EntityList getInstance()
	{
		return uniqueInstance;
	}
	
	private EntityList()
	{
		names = new HashMap<String,Entity>();
		types = new HashMap<String,ArrayList<Entity>>();
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
	
	/*
	public Entity getLatestEntity() {
		return latest_ent;
	}
	
	public boolean getListChanged() {
		if(list_changed) {
			list_changed=false;
			return true;
		} else {
			return list_changed;
		}
	}
	
	public void setListChanged(boolean changed) {
		list_changed = changed;
	}
	
		public void deleteByPosition(Vector3f position) {
		for(int i=0; i<entities.size(); i++) {
			if( position.equals(entities.get(i).getPosition()) ) {
				entities.get(i).deletePhysics();
				entities.remove(i);
				System.out.print("Found it: Deleting...\n Done. \n");
				list_changed = true;
			}
		}
	}
	*/
} 