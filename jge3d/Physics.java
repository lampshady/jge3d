package jge3d;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.constraintsolver.ConstraintSolver;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

public class Physics {
	private static Physics uniqueInstance = new Physics();
	
	//World Definitions
	private DefaultCollisionConfiguration collisionConfiguration;
	private CollisionDispatcher dispatcher;
	private Vector3f worldAabbMin;
	private Vector3f worldAabbMax;
	private BroadphaseInterface overlappingPairCache;
	private ConstraintSolver solver;
	private DynamicsWorld dynamicsWorld;
	private List<CollisionShape> collisionShapes = new ArrayList<CollisionShape>();
	float deltaT;

	public static Physics getInstance()
	{
		return uniqueInstance;
	}
	
	//For holding the previous time in microseconds to calculate deltaT
	private long prev_time;
	
	private Physics() {		
		//Default collision constructor
		collisionConfiguration = new DefaultCollisionConfiguration();
		
		//Creates a dispatcher thread for sending processing physics calculations
		dispatcher = new CollisionDispatcher(collisionConfiguration);
		
		//Min and Max collision boundaries for world (needs changing)
		worldAabbMin = new Vector3f(-10,-10,-10);
		worldAabbMax = new Vector3f(10,10,10);
		
		//algorithm for finding collision proximity (there are better ones)
		overlappingPairCache = new AxisSweep3(worldAabbMin, worldAabbMax);
		
		//Type of solver to be used for solving physics (look into threading for parallel)
		solver = new SequentialImpulseConstraintSolver();
		
		//Create the dynamics world and set default options
		dynamicsWorld = new DiscreteDynamicsWorld(dispatcher, overlappingPairCache, solver, collisionConfiguration);
		dynamicsWorld.setGravity(new Vector3f(0,-30,0));
		dynamicsWorld.getDispatchInfo().allowedCcdPenetration = 0f;
		
		//Preset the previous time so deltaT isn't enormous on first run
		prev_time = System.nanoTime();
	}
		
	public RigidBody addLevelBlock(float x, float y, float z, float cube_size) {
		//Levels are static since we don't want them moving due to interactions with
		//other physics objects; Static objects must have a mass of 0 and no inertia
		int mass=0;
		Vector3f localInertia = new Vector3f(0, 0, 0);
		
		//Collision shape is a box since a level is just a bunch of boxes
		CollisionShape colShape = new BoxShape(new Vector3f(cube_size, cube_size, cube_size));
		collisionShapes.add(colShape);
	
		// Create level transformation and default position
		Transform startTransform = new Transform();
		startTransform.setIdentity();
		startTransform.origin.set(cube_size*x, cube_size*y, cube_size*z);

		//provides interpolation capabilities and only synchronizes 'active' objects
		DefaultMotionState motion_state = new DefaultMotionState(startTransform);
		RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo(mass, motion_state, colShape, localInertia);
		RigidBody body = new RigidBody(rbInfo);
	
		dynamicsWorld.addRigidBody(body);
		
		return body;
	}
	
	public Entity dropBox(float x, float y, float z, float cube_size){
		//Give this thing some mass
		float mass = 10f;
		
		//Create a shape for the object
		CollisionShape boxShape = new BoxShape(new Vector3f(cube_size, cube_size, cube_size));
		collisionShapes.add(boxShape);
		
		//Transform relative to initial position
		Transform startTransform = new Transform();
		startTransform.setIdentity();
		startTransform.origin.set(new Vector3f(x,y,z));

		//Create a rigid body to represent the object
		RigidBody body = createRigidBody(mass, startTransform, boxShape);
		
		//Setup objects properties [ a lot of this is probably redundant]
		body.setFriction(0.5f);
		//body.setDamping(0.2f, 0.2f);
		//body.setGravity(new Vector3f(0,1,0));
		//body.setMassProps(1.0f, new Vector3f(0.0f,0.0f,0.0f));
		//body.setCollisionFlags(0);
		
		//Find inital velocity
		//Vector3f initial_velocity = new Vector3f(0.0f,0.0f,0.0f);	//Initial direction
		//initial_velocity.normalize();	//transform to this position		
		//initial_velocity.scale(0.0f);	//Initial speed
		
		/*
		//Transform relative to world
		Transform worldTrans = body.getWorldTransform(new Transform());
		Vector3f world_pos = new Vector3f(10.0f,10.0f,0.0f);
		worldTrans.origin.set(world_pos);
		worldTrans.setRotation(new Quat4f(0f, 0f, 0f, 1f));
		body.setWorldTransform(worldTrans);
		*/
		
		//body.setLinearVelocity(initial_velocity);
		//body.setAngularVelocity(new Vector3f(0f, 0f, 0f));
		Entity ent = new Entity();
		ent.setType(null);
		ent.setPositionX(x);
		ent.setPositionY(y);
		ent.setPositionZ(z);
		ent.setGravityX(0.0);
		ent.setGravityY(0.0);
		ent.setGravityZ(0.0);
		ent.setMass(1.0);
		ent.setTransparent(false);
		ent.setAlpha(1.0f);
		ent.setTexture_name("brick1");
		ent.setCollidable(true);
		ent.setSize(1.0);
		ent.setTTL(5);
		
		return ent;
	}
		
	public RigidBody createPlayer(float x, float y, float z, float cube_size){
		//Give this thing some mass
		float mass = 10f;
		
		//Create a shape for the object
		CollisionShape boxShape = new BoxShape(new Vector3f(cube_size, cube_size, cube_size));
		collisionShapes.add(boxShape);
		
		//Transform relative to initial position
		Transform startTransform = new Transform();
		startTransform.setIdentity();
		startTransform.origin.set(new Vector3f(x,y,z));

		//if you give the object 0 local inertia it doesn't rotate around the z axis
		Vector3f localInertia = new Vector3f(0f, 0f, 0f);
		
		//set motion state (keeps track of objects motion, durr)
		DefaultMotionState myMotionState = new DefaultMotionState(startTransform);
		RigidBodyConstructionInfo cInfo = new RigidBodyConstructionInfo(mass, myMotionState, boxShape, localInertia);
		RigidBody body = new RigidBody(cInfo);

		//Setup objects properties
		body.setFriction(0.99f);
		body.setDamping(0.1f, 1.0f);
		body.setAngularVelocity(new Vector3f(0,0,0));
		//body.setGravity(new Vector3f(0,1,0));
		//body.setMassProps(1.0f, new Vector3f(0.0f,0.0f,0.0f));
		//body.setCollisionFlags(0);
		
		dynamicsWorld.addRigidBody(body);
		
		return body;
	}
	
	public RigidBody createRigidBody(float mass, Transform startTransform, CollisionShape shape) {
		// rigid body is dynamic if and only if mass is non zero, otherwise static
		boolean isDynamic = (mass != 0f);

		Vector3f localInertia = new Vector3f(0f, 0f, 0f);
		if (isDynamic) {
			shape.calculateLocalInertia(mass, localInertia);
		}

		//set motion state (keeps track of objects motion, durr)
		DefaultMotionState myMotionState = new DefaultMotionState(startTransform);
		RigidBodyConstructionInfo cInfo = new RigidBodyConstructionInfo(mass, myMotionState, shape, localInertia);
		RigidBody body = new RigidBody(cInfo);

		dynamicsWorld.addRigidBody(body);

		return body;
	}
	
	public DynamicsWorld getDynamicsWorld() {
		return dynamicsWorld;
	}
	
	public void clientUpdate() {
		// simple dynamics world doesn't handle fixed-time-stepping
		deltaT = (System.nanoTime()-prev_time);
		prev_time = System.nanoTime();
		
		// step the simulation
		if (dynamicsWorld != null) {
			dynamicsWorld.stepSimulation(deltaT / 1000000000f);
		}
	}
	
	public float getDeltaT() {
		return deltaT;
	}
}
