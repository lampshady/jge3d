package jge3d;

import javax.vecmath.Vector3f;

import com.bulletphysics.dynamics.RigidBody;

//The player(s)' class
public class Player extends Character {
	//private Entity player;
	private RigidBody player_physics;
	float max_velocity;
	Vector3f current_velocity;
	
	public Player(Physics _physics) {
		//player = new Entity('P',new Vector3f(17,15,0), "cube1", true);
		player_physics = _physics.createPlayer((int)10, (int)5, (int)0, 1.0f);
		current_velocity = new Vector3f();
	}
	
	public void move(Vector3f impulse, float velocity) {
		player_physics.applyImpulse(impulse, new Vector3f((int)0, (int)0, (int)0));
	}
	
	public void activate() {
		player_physics.activate();
	}
	
	public Vector3f getVelocity() {
		return player_physics.getLinearVelocity(current_velocity);
	}
}