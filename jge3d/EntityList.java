package jge3d;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3f;

import com.bulletphysics.dynamics.RigidBody;

public class EntityList {
	private List<Entity> entities;
	private static EntityList  uniqueInstance = new EntityList();
	
	private Entity latest_ent;
	//private int level_size = 0;
	//private boolean level_changed;
	private boolean list_changed;
	//private Physics physics;
	
	public static EntityList getInstance()
	{
		return uniqueInstance;
	}
	
	private EntityList() {
		entities = new ArrayList<Entity>();
	}

	public void clear() {
		entities.clear();
	}

	public Entity addEntityByParams(String _type, Vector3f _pos, String _texture_name, boolean _collidable, RigidBody rb, int _ttl) {
		latest_ent = new Entity(_type,_pos,_texture_name,_collidable,rb,_ttl);
		entities.add(latest_ent);
		list_changed=true;

		return latest_ent;
	}
	
	public Entity addEntity(Entity ent) {
		entities.add(ent);
		latest_ent = ent;
		list_changed=true;

		return ent;
	}
	
	public void removeEntity(int index) {
		list_changed=true;
		entities.remove(index);
	}
	
	public Entity get(int index) {
		return entities.get(index);
	}
	
	public Vector3f getEntityPosition(int index) {
		return entities.get(index).getPosition();
	}
	
	public String getEntityTextureName(int index) {
		return entities.get(index).getTexture_name();
	}
	
	public float getListSize() {
		return entities.size();
	}
	
	public boolean getEntityCollidable(int index) {
		return entities.get(index).getCollidable();
	}

	public Entity getLatestEntity() {
		return latest_ent;
	}
	
	public Entity getByName(String name) {
		for(int i=0; i<entities.size();i++) {
			if(entities.get(i).getName().equals(name)) {
				return entities.get(i);
			}
		}
		return new Entity();
	}
	
	public String getByIndex(int index) {
		return entities.get(index).toString();
	}
	
	public void setRigidBodyByID(int index, RigidBody rb) {
		entities.get(index).setRigidBody(rb);
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
	
	public float getEntitySize(int index) {
		return entities.get(index).getSize();
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
	
	public void pruneEntities() {
		for(int i=0; i<entities.size(); i++) {
			if( entities.get(i).isDead() ) {
				entities.get(i).deletePhysics();
				entities.remove(i);
				System.out.print("Found a dead one: Deleting...\n Done. \n");
				list_changed = true;
			}
		}
	}
}