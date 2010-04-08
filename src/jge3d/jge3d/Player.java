package jge3d;

import java.nio.FloatBuffer;

import javax.vecmath.Vector3f;

import org.lwjgl.opengl.GL11;

import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.util.ObjectPool;

//The player(s)' class
public class Player extends Character {
	private static float[] playerMatrix = new float[16];
	
	public Player() {
		
	}
	
	public void update() {
		//update physics
		
		//render
		
	}
	
	public void render(Transform player_trans, CollisionShape shape) {
		//ObjectPool<Transform> transformsPool = ObjectPool.get(Transform.class);
		ObjectPool<Vector3f> vectorsPool = ObjectPool.get(Vector3f.class);
		
		GL11.glPushMatrix();
		player_trans.getOpenGLMatrix(playerMatrix);
		GL11.glMultMatrix(FloatBuffer.wrap(playerMatrix));
		
		BoxShape boxShape = (BoxShape) shape;
		Vector3f halfExtent = boxShape.getHalfExtentsWithMargin(vectorsPool.get());
		GL11.glScalef(2f * halfExtent.x, 2f * halfExtent.y, 2f * halfExtent.z);
		
		
		
		
		//Draw a cube (it's the player for now...)
		float player_size = 1.0f; 
		GL11.glBegin(GL11.GL_QUADS);
	        // Front Face
	        GL11.glVertex3f(-player_size, -player_size, player_size); // Bottom Left Of The Texture and Quad
	        GL11.glVertex3f(player_size, -player_size, player_size); // Bottom Right Of The Texture and Quad
	        GL11.glVertex3f(player_size, player_size, player_size); // Top Right Of The Texture and Quad
	        GL11.glVertex3f(-player_size, player_size, player_size); // Top Left Of The Texture and Qua        
	        // Back Face
	        GL11.glVertex3f(-player_size, -player_size, -player_size); // Bottom Right Of The Texture and Quad
	        GL11.glVertex3f(-player_size, player_size, -player_size); // Top Right Of The Texture and Quad
	        GL11.glVertex3f(player_size, player_size, -player_size); // Top Left Of The Texture and Quad
	        GL11.glVertex3f(player_size, -player_size, -player_size); // Bottom Left Of The Texture and Quad
	        // Top Face
	        GL11.glVertex3f(-player_size, player_size, -player_size); // Top Left Of The Texture and Quad
	        GL11.glVertex3f(-player_size, player_size, player_size); // Bottom Left Of The Texture and Quad
	        GL11.glVertex3f(player_size, player_size, player_size); // Bottom Right Of The Texture and Quad
	        GL11.glVertex3f(player_size, player_size, -player_size); // Top Right Of The Texture and Quad
	        // Bottom Face
	        GL11.glVertex3f(-player_size, -player_size, -player_size); // Top Right Of The Texture and Quad
	        GL11.glVertex3f(player_size, -player_size, -player_size); // Top Left Of The Texture and Quad
	        GL11.glVertex3f(player_size, -player_size, player_size); // Bottom Left Of The Texture and Quad
	        GL11.glVertex3f(-player_size, -player_size, player_size); // Bottom Right Of The Texture and Quad
	        // Right face
	        GL11.glVertex3f(player_size, -player_size, -player_size); // Bottom Right Of The Texture and Quad
	        GL11.glVertex3f(player_size, player_size, -player_size); // Top Right Of The Texture and Quad
	        GL11.glVertex3f(player_size, player_size, player_size); // Top Left Of The Texture and Quad
	        GL11.glVertex3f(player_size, -player_size, player_size); // Bottom Left Of The Texture and Quad
	        // Left Face
	        GL11.glVertex3f(-player_size, -player_size, -player_size); // Bottom Left Of The Texture and Quad
	        GL11.glVertex3f(-player_size, -player_size, player_size); // Bottom Right Of The Texture and Quad
	        GL11.glVertex3f(-player_size, player_size, player_size); // Top Right Of The Texture and Quad
	        GL11.glVertex3f(-player_size, player_size, -player_size); // Top Left Of The Texture and Quad
		GL11.glEnd();
		
		
		
		
		
		
		
		vectorsPool.release(halfExtent);
		GL11.glPopMatrix();
		
		
	}
}