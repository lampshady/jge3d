package jge3d;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.vecmath.Vector3f;

import jge3d.GUI.Window;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;

public class Camera {
	private Vector3f position;					//x, y, z
	private float declination;					//Angle up and down
	private float rotation;						//Angle left and right
	private Vector3f focus;						//x, y, z of target
	private float distance;						//distance from focus
	private Vector3f up_vector;					//vector pointing up
	private Window window;
	Vector3f min_window_bounds;
	Vector3f max_window_bounds;
	
	//Don't flip over, its confusing.
	private float maximum_declination = 89.9f;

	private float minimum_declination = 0.1f;
	
	public Camera(float height, float width, Window _window){
		//initial setup (float about 0,0,0 I guess?
		window = _window;
		position = new Vector3f(0,0,0);
		focus = new Vector3f(0,0,0);
		declination = 0;
		rotation = 0;
		distance = 45.0f;
		setUpVector( 0, 1, 0 );
		goToStart(height, width);
		updatePosition();
	}
	
	public Camera(float x, float y, float z, float height, float width)
	{
		position = new Vector3f(0,0,0);
		focus = new Vector3f(x, y, z);
		declination = 0;
		rotation = 0;
		distance = 45.0f;
		setUpVector( 0, 1, 0 );
		goToStart(height, width);
		updatePosition();
	}
	
	float getPositionX()
	{
		return position.x + focus.x;
	}
	
	float getPositionY()
	{
		return position.y + focus.y;
	}
	
	float getPositionZ()
	{
		return position.z + focus.z;
	}
	
	Vector3f getUp()
	{
		return up_vector;
	}
	
	float getUpVectorX()
	{
		return up_vector.x;
	}
	
	float getUpVectorY()
	{
		return up_vector.y;
	}
	
	float getUpVectorZ()
	{
		return up_vector.z;
	}
	
	float getFocusX()
	{
		return focus.x;
	}
	
	float getFocusY()
	{
		return focus.y;
	}
	
	float getFocusZ()
	{
		return focus.z;
	}
	
	public void changeFocus(float x, float y, float z)
	{
		focus.x = x;
		focus.y = y;
		focus.z = z;
		updatePosition();
	}
	
	public void incrementDistance( float change )
	{
		distance += change;
		updatePosition();
	}
	
	public void incrementDeclination(float angle)
	{
		declination += angle;
		if( declination > maximum_declination ){
			declination = maximum_declination;
		}
		if( declination < minimum_declination ){
			declination = minimum_declination;
		}
		updatePosition();
	}
	
	public void incrementRotation(float angle)
	{
		rotation += angle;
	}
	
	public void moveFocus( Vector3f vector3f )
	{
		//focus.set( vector3f.x, vector3f.y, vector3f.z );
		focus.add(vector3f);
		updatePosition();
	}
	
	private void updatePosition()
	{
		 float a = 0;
		 
		 //calculate positions from angles as if focus were (0,0,0)
		 position.y = (float) ((distance * Math.sin(declination)));
		 a = (float) ((distance * Math.cos(declination)));
		 position.x = (float) (a*Math.sin(rotation));
		 position.z = (float) (a*Math.cos(rotation));
	}
	
	private void setUpVector(float x, float y, float z)
	{
		setUpVector( new Vector3f( x, y, z ) );
	}
	
	private void setUpVector( Vector3f newUp )
	{
		up_vector = newUp;
	}
	
	//hard coded, ugh!  Magic numbers~
	public void goToStart(float height, float width) {
		//
		focus.set(18.0f, 8.0f, 0.0f);
		position.set(0.0f, 0.0f, 45.0f);
		setUpVector(0,1,0);
	}

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
		
		ray.set(ray.x - position.x, ray.y - position.y, ray.z-position.z);
		
		//Don't want to hold on to the context as the renderer will need it
		Display.releaseContext();
		float t = ( (-1*d) - position.dot(normal) )/( ray.dot(normal) );
		 
		ray = new Vector3f(fuckingStupidRounding(this.getPositionX() + t * ray.x),
							fuckingStupidRounding(this.getPositionY() + t * ray.y),
							this.getPositionZ() + t * ray.z); 
		
		return ray;
	}
	
	public void moveToPlayerLocation(Player player) throws LWJGLException {
		min_window_bounds = getRayToPlane(0, 0, new Vector3f(0,0,1), new Vector3f(0,0,0));
		max_window_bounds = getRayToPlane(window.getGLWidth(), window.getGLHeight(), new Vector3f(0,0,1), new Vector3f(0,0,0));
		
		if(player.getLocation().x > -10+max_window_bounds.x)
			focus.x += 0.05;
		if(player.getLocation().x < 10+min_window_bounds.x)
			focus.x -= 0.05;		
		if(player.getLocation().y > -10+max_window_bounds.y)
			focus.y += 0.05;
		if(player.getLocation().y < 10+min_window_bounds.y)
			focus.y -= 0.05;
		
		updatePosition();
		/*
		System.out.print(
			"Win topright:\n" +
			max_window_bounds.x + " " +
			max_window_bounds.y + " " +
			max_window_bounds.z + "\n"
		);
		
		System.out.print(
			"Win bottomleft:\n" +
			min_window_bounds.x + " " +
			min_window_bounds.y + " " +
			min_window_bounds.z + "\n"
		);
		
		System.out.print(
			"Player:\n" +
			player.getLocation().x + " " +
			player.getLocation().y + " " +
			player.getLocation().z + "\n"
		);
		*/
	}
	
	public void debug() {
		//Debug the camera
		//System.out.print("Height:		" + height 	+ "	Width:	" + width + "\n");
		System.out.print("Camera = X:	" + position.x + "	Y:	" + position.y + "	Z:	" + position.z + "\n");
		System.out.print("Focus  = X:	" + focus.x 	+ "	Y:	" + focus.y 	+ "	Z:	" + focus.z 	+ "\n");
		System.out.print("Up     = X:	" + up_vector.x + "	Y:	" + up_vector.y + "	Z:	" + up_vector.z + "\n\n");	
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
