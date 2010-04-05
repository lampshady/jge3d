package jge3d;

import java.nio.FloatBuffer;

import javax.vecmath.Vector3f;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.util.ObjectPool;

public class Entity {
	private RigidBody body; 
	private Transform body_trans;
	private DefaultMotionState body_motion;
	private Renderer render;
	float[] body_matrix = new float[16];	//hold matrix of box
	FloatBuffer buf;
	
	public Entity(RigidBody new_body) {
		//place the newly created physics object in a local variable
		body = new_body;
		
		//create a new jBullet transform to store the next transformation in 
		body_trans = new Transform();
		
		//to store the MotionState (very important) for the body
		body_motion = new DefaultMotionState();
		
		//Allocate a 16 position FloatBuffer because OpenGL needs it
		buf = BufferUtils.createFloatBuffer(16);
		
		//Create a render instance
		render = new Renderer();
	}

	public void update_physics() {
		//update the physics position relative to world
		body_motion = (DefaultMotionState)body.getMotionState();	//get box motion state
		body_trans.set(body_motion.graphicsWorldTrans);
		
		//retrieve the OpenGL matrix from jBullet
		body_trans.getOpenGLMatrix(body_matrix);
		
		//The next line may or may not be faster...
		//FloatBuffer buf = ByteBuffer.allocateDirect(16*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		
		//Copy the contents of the matrix in a FloatBuffer for glLoadMatrix
		buf.put(body_matrix);
		
		//Make sure to flip the buffer as the values are reversed
		buf.flip();
	}
	
	public void render() {
		//Allow a colored cube to be drawn
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glColor3f(1.0f, 0.0f, 0.0f);
		
		//Take the matrix in the float buffer and load it into a matrix
		GL11.glMultMatrix(buf);
		
		//Testing code
		ObjectPool<Vector3f> vectorsPool = ObjectPool.get(Vector3f.class);
		BoxShape boxShape = (BoxShape) body.getCollisionShape();
		Vector3f halfExtent = boxShape.getHalfExtentsWithMargin(vectorsPool.get());
		GL11.glScalef(2f * halfExtent.x, 2f * halfExtent.y, 2f * halfExtent.z);
				
		//Draw cube at matrix
		render.drawcube(1, 1);
		
		//More testing code
		vectorsPool.release(halfExtent);
		
		//debug();

		//Clear the buffer before update_physics runs
		buf.clear();
	}
	
	private void debug() {
		for(int i=0; i<16;++i) 
			System.out.print(buf.get()+"\n");
		System.out.print("\n");		
	}
}
