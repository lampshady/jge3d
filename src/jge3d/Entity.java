package jge3d;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

public class Entity {
	private RigidBody body; 
	private Transform body_trans;
	private DefaultMotionState box_motion;
	private Renderer render;
	float[] body_matrix = new float[16];	//hold matrix of box
	FloatBuffer buf;
	
	public Entity(RigidBody new_body) {
		body = new_body;
		body_trans = new Transform();
		box_motion = new DefaultMotionState();
		buf = BufferUtils.createFloatBuffer(16);
		render = new Renderer();
	}

	public void update_physics() {
		box_motion = (DefaultMotionState)body.getMotionState();	//get box motion state
		body_trans.set(box_motion.graphicsWorldTrans);
		body_trans.getOpenGLMatrix(body_matrix);
		
		//The next line may or may not be faster...
		//FloatBuffer buf = ByteBuffer.allocateDirect(16*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		
		buf.put(body_matrix);
		buf.flip();
		
		
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glColor3f(1.0f, 0.0f, 0.0f);
		GL11.glLoadMatrix(buf);
		render.drawcube(1, 1);
		
		//debug the matrix
		for(int i=0;i<16;i++){
			System.out.print(buf.get() + "\n");
		}
		System.out.print("\n");
		buf.clear();
	}
	
	public void render() {
		
	}
}
