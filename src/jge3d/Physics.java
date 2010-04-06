package jge3d;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3f;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionObject;
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
	//World Definitions
	private DefaultCollisionConfiguration collisionConfiguration;
	private CollisionDispatcher dispatcher;
	private Vector3f worldAabbMin;
	private Vector3f worldAabbMax;
	private BroadphaseInterface overlappingPairCache;
	private ConstraintSolver solver;
	private DynamicsWorld dynamicsWorld;
	private List<CollisionShape> collisionShapes = new ArrayList<CollisionShape>();
	
	//Test stuff
	float[] body_matrix = new float[16];	//hold matrix of box
	FloatBuffer buf;
	Renderer render = new Renderer();
	private final Transform m = new Transform();	//wtf is this for

	//For holding the previous time in microseconds to calculate deltaT
	private long prev_time;
	
	public Physics() {
		//Default collision constructor
		collisionConfiguration = new DefaultCollisionConfiguration();
		
		//Creates a dispatcher thread for sending processing physics calculations
		dispatcher = new CollisionDispatcher(collisionConfiguration);
		
		//Min and Max collision boundaries for world (needs changing)
		worldAabbMin = new Vector3f(-10000,-10000,-10000);
		worldAabbMax = new Vector3f(10000,10000,10000);
		
		//algorithm for finding collision proximity (there are better ones)
		overlappingPairCache = new AxisSweep3(worldAabbMin, worldAabbMax);
		
		//Type of solver to be used for solving physics (look into threading for parallel)
		solver = new SequentialImpulseConstraintSolver();
		
		//Create the dynamics world and set default options
		dynamicsWorld = new DiscreteDynamicsWorld(dispatcher, overlappingPairCache, solver, collisionConfiguration);
		dynamicsWorld.setGravity(new Vector3f(0,-10,0));
		dynamicsWorld.getDispatchInfo().allowedCcdPenetration = 0f;
		
		//Preset the previous time so deltaT isn't enormous on first run
		prev_time = System.nanoTime();
		
		buf = BufferUtils.createFloatBuffer(16);
	}
		
	public void addLevelBlock(int x, int y, int z, float cube_size) {
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
		startTransform.origin.set(cube_size*x*2, cube_size*y*2, cube_size*z*2);

		//provides interpolation capabilities and only synchronizes 'active' objects
		DefaultMotionState motion_state = new DefaultMotionState(startTransform);
		RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo(mass, motion_state, colShape, localInertia);
		RigidBody body = new RigidBody(rbInfo);
	
		dynamicsWorld.addRigidBody(body);
	}
	
	public RigidBody dropBox(float x, float y, float z, float cube_size){
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
		/*
		body.setFriction(0.5f);
		body.setDamping(0.2f, 0.1f);
		body.setGravity(new Vector3f(0,0,0));
		body.setMassProps(1.0f, new Vector3f(0.0f,0.0f,0.0f));
		body.setCollisionFlags(0);
		*/
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

	public void render() {
		if (dynamicsWorld != null) {
			int numObjects = dynamicsWorld.getNumCollisionObjects();
			for (int i = 0; i < numObjects; i++) {
				CollisionObject colObj = dynamicsWorld.getCollisionObjectArray().get(i);
				RigidBody body = RigidBody.upcast(colObj);
				
				if(body.getInvMass() != 0) {
					if (body != null && body.getMotionState() != null) {
						DefaultMotionState myMotionState = (DefaultMotionState) body.getMotionState();
						m.set(myMotionState.graphicsWorldTrans);
					}
					else {
						colObj.getWorldTransform(m);
					}
	
					GL11.glPushMatrix();
					m.getOpenGLMatrix(body_matrix);
					
					//Put all this matrix shit in a float buffer
					buf.put(body_matrix);
					buf.flip();
					
					GL11.glMultMatrix(buf);
					buf.clear();
	
					//Testing code
					//ObjectPool<Vector3f> vectorsPool = ObjectPool.get(Vector3f.class);
					//BoxShape boxShape = (BoxShape) body.getCollisionShape();
					//Vector3f halfExtent = boxShape.getHalfExtentsWithMargin(vectorsPool.get());
					//GL11.glScalef(1.0f * halfExtent.x, 1.0f * halfExtent.y, 1.0f * halfExtent.z);
					
					//Draw cube at matrix
					render.drawcube(1, 1);
					
					//More testing code
					//vectorsPool.release(halfExtent);
						
					GL11.glPopMatrix();
				}
			}
		}
	}
	
	public void clientUpdate() {
		// simple dynamics world doesn't handle fixed-time-stepping
		float deltaT = (System.nanoTime()-prev_time);
		prev_time = System.nanoTime();
		
		// step the simulation
		if (dynamicsWorld != null) {
			dynamicsWorld.stepSimulation(deltaT / 1000000000f);
			// optional but useful: debug drawing
			//dynamicsWorld.debugDrawWorld();
		}
	}
}
