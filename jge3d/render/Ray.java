package jge3d.render;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.vecmath.Vector3f;

import jge3d.Camera;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class Ray {
	public Vector3f getRayTo(int mouseX, int mouseY) throws LWJGLException {
		//We need exclusive access to the window
		Display.makeCurrent();
		
		//Create stupid floatbuffers for LWJGL
		IntBuffer viewport = BufferUtils.createIntBuffer(16);
		FloatBuffer modelview = BufferUtils.createFloatBuffer(16);
		FloatBuffer projection = BufferUtils.createFloatBuffer(16);
		FloatBuffer winZ = BufferUtils.createFloatBuffer(1);
		FloatBuffer position = BufferUtils.createFloatBuffer(3);
		Vector3f pos = new Vector3f();
		
		//Get some information about the viewport, modelview, and projection matrix
		GL11.glGetFloat( GL11.GL_MODELVIEW_MATRIX, modelview );
		GL11.glGetFloat( GL11.GL_PROJECTION_MATRIX, projection );
		GL11.glGetInteger( GL11.GL_VIEWPORT, viewport );

		//Find the depth to 
		GL11.glReadPixels(mouseX, mouseY, 1, 1, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, winZ);

		//get the position in 3d space by casting a ray from the mouse
		//coords to the first contacted point in space
		//GLU.gluUnProject(mouseX, mouseY, winZ.get(), modelview, projection, viewport, position);
		GLU.gluUnProject(mouseX, mouseY, winZ.get(), modelview, projection, viewport, position);

		//Make a vector out of the silly float buffer LWJGL forces us to use
		pos.set(position.get(0), position.get(1), position.get(2));
		
		//Don't want to hold on to the context as the renderer will need it
		Display.releaseContext();

		return pos;
	}
	
	public Vector3f getRayToPlane(int mouseX, int mouseY, Vector3f normal, Vector3f planePoint) throws LWJGLException {
		//We need exclusive access to the window
		Display.makeCurrent();
		
		//Create stupid floatbuffers for LWJGL
		IntBuffer viewport = BufferUtils.createIntBuffer(16);
		FloatBuffer modelview = BufferUtils.createFloatBuffer(16);
		FloatBuffer projection = BufferUtils.createFloatBuffer(16);
		FloatBuffer mousePosition = BufferUtils.createFloatBuffer(3);
		Vector3f ray = new Vector3f();
		
		//Get some information about the viewport, modelview, and projection matrix
		GL11.glGetFloat( GL11.GL_MODELVIEW_MATRIX, modelview );
		GL11.glGetFloat( GL11.GL_PROJECTION_MATRIX, projection );
		GL11.glGetInteger( GL11.GL_VIEWPORT, viewport );

		//get the position in 3d space by casting a ray from the mouse  
		//coords to the first contacted point in space
		GLU.gluUnProject(mouseX, mouseY, 1, modelview, projection, viewport, mousePosition);
		
		//Make a vector out of the silly float buffer LWJGL forces us to use
		float d = -1.0f * (float)(normal.dot(planePoint));
		
		//Make ray a vector from origin to point
		ray.set(mousePosition.get(0), 
				mousePosition.get(1), 
				mousePosition.get(2));
		Vector3f position=Camera.getInstance().getPosition();
		ray.set(ray.x - position.x, ray.y - position.y, ray.z-position.z);
		
		//Don't want to hold on to the context as the renderer will need it
		Display.releaseContext();
		float t = ( (-1*d) - position.dot(normal) )/( ray.dot(normal) );
		 
		ray = new Vector3f(fuckingStupidRounding(position.x + t * ray.x),
							fuckingStupidRounding(position.y + t * ray.y),
							position.z + t * ray.z); 
		
		return ray;
	}
	
	public float fuckingStupidRounding(float fuckingStupidNumber)
	{
		float fuckingStupidDivByTwo = (float)Math.floor(fuckingStupidNumber) % 2.0f;
		
		if( fuckingStupidDivByTwo == 1)
			return (float)Math.floor(fuckingStupidNumber);
		else
			return (float)Math.floor(fuckingStupidNumber) + 1.0f;
	}
}
