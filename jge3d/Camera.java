package jge3d;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.vecmath.Vector3f;

import jge3d.gui.Window;
import jge3d.render.Renderer;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;

public class Camera {
	private static Camera uniqueInstance = new Camera();
	private Vector3f position;					//x, y, z
	private float declination;					//Angle up and down
	private float rotation;						//Angle left and right
	private Vector3f focus;						//x, y, z of target
	private float distance;						//distance from focus
	private Vector3f up_vector;					//vector pointing up
	Vector3f min_window_bounds;
	Vector3f max_window_bounds;
	
	//Don't flip over, its confusing.
	private float maximum_declination = (float) (Math.PI/2.0f) - 0.01f;
	private float minimum_declination = (float) ((float) -1.0f*((Math.PI/2.0f) - 0.01f));
	private float minimum_distance = 0.001f;
	private float maximum_distance = 100.0f;
	
	private Camera(){
		position = new Vector3f(0,0,0);
		focus = new Vector3f(0,0,0);
		declination = 0;
		rotation = 0;
		distance = 45.0f;
		setUpVector( 0, 1, 0 );
		updatePosition();
	};
	
	public static Camera getInstance(){
		return uniqueInstance;
	};
	
	private Camera (float height, float width, Window _window){
		//initial setup (float about 0,0,0 I guess?
		position = new Vector3f(0,0,0);
		focus = new Vector3f(0,0,0);
		declination = 0;
		rotation = 0;
		distance = 45.0f;
		setUpVector( 0, 1, 0 );
		goToStart(height, width);
		updatePosition();
	}
	
	private Camera (float x, float y, float z, float height, float width)
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
	
	public float getPositionX()
	{
		return position.x + focus.x;
	}
	
	public float getPositionY()
	{
		return position.y + focus.y;
	}
	
	public float getPositionZ()
	{
		return position.z + focus.z;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public Vector3f getUp()
	{
		return up_vector;
	}
	
	public float getUpVectorX()
	{
		return up_vector.x;
	}
	
	public float getUpVectorY()
	{
		return up_vector.y;
	}
	
	public float getUpVectorZ()
	{
		return up_vector.z;
	}
	
	public float getFocusX()
	{
		return focus.x;
	}
	
	public float getFocusY()
	{
		return focus.y;
	}
	
	public float getFocusZ()
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
		float temp = distance + change;
		if( temp > maximum_distance)
		{
			distance = maximum_distance;
		}else
		{
			if(temp < minimum_distance)
			{
				distance = minimum_distance;
			}else
			{
				distance = temp;
			}
		}
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
		updatePosition();
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
	
	public void moveToPlayerLocation(Player player) throws LWJGLException {
		min_window_bounds = Renderer.getInstance().findRay.getRayToPlane(0, 0, new Vector3f(0,0,1), new Vector3f(0,0,0));
		max_window_bounds = Renderer.getInstance().findRay.getRayToPlane(Window.getInstance().getGLWidth(), Window.getInstance().getGLHeight(), new Vector3f(0,0,1), new Vector3f(0,0,0));
		
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
}
