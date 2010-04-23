package jge3d;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3f;


public class EntityList {
	private List<Entity> entities;
	private Entity latest_ent;
	//private int level_size = 0;
	//private boolean level_changed;
	private boolean list_changed;
	
	public EntityList() {
		entities = new ArrayList<Entity>();
	}

	public void clear() {
		entities.clear();
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
	
	public Vector3f getEntityPosition(int index) {
		return entities.get(index).getPosition();
	}
	
	public String getEntityTextureName(int index) {
		return entities.get(index).getTextureName();
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
	
	public String getByIndex(int index) {
		return entities.get(index).toString();
	}
	
	public boolean getListChanged() {
		if(list_changed) {
			list_changed=false;
			return true;
		} else {
			return list_changed;
		}
	}
	
	public float getEntitySize(int index) {
		return entities.get(index).getSize();
	}
/*
	public int getLevelSize() {
		level_size = 0;
		
		for(int i=0; i < entities.size();i++) {
			if(entities.get(i).getType() == 'L') {
				++level_size;
			}
		}

		return level_size;
	}
	
	public List<Entity> getLevelEntities() {
		List<Entity> level_ents = new ArrayList<Entity>();
		
		for(int i=0; i < entities.size();i++) {
			if(entities.get(i).getType() == 'L') {
				level_ents.add(entities.get(i));
			}
		}
		
		return level_ents;
	}
*/
}