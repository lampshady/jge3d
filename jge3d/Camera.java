package jge3d;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.vecmath.Vector3f;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;


//Maybe we'll use it, maybe we won't?
public class Camera {
	float[] position;					//x, y, z
	float declination;					//Angle up and down
	float rotation;						//Angle left and right
	float[] focus;						//x, y, z of target
	float distance;						//distance from focus
	Vector3f up_vector;					//vector pointing up
	
	//Don't flip over, its confusing.
	float maximum_declination = 89.9f;
	float minimum_declination = 0.1f;
	
	//field of view
	double fovy;
	double fovx;
	double x;
	double y;
	
	float screen_height;
	float screen_width;
	
	
	Camera(float height, float width){
		//initial setup (float about 0,0,0 I guess?
		
		position = new float[3];
		focus = new float[3];
		
		for( int i = 0; i<3; i++ )
			 position[i] = 0;
		
		for( int i = 0; i<3; i++ )
			 focus[i] = 0;
		
		declination = 0;
		rotation = 0;
		distance = -63.0f;
		setUpVector( 0, 1, 0 );
		updatePosition();
		
		//Feild of View calculations
		screen_height = height;
		screen_width = width;
		fovx = (float)Math.PI / 4;
		fovy = (height/width) * fovx;
	}
	
	Camera(float x, float y, float z, float height, float width)
	{
		position = new float[3];
		focus = new float[3];
		
		focus[0] = x;
		focus[1] = y;
		focus[2] = z;
		
		declination = 0;
		rotation = 0;
		distance = 63.0f;
		setUpVector( 0, 1, 0 );
		updatePosition();
		
		//FOV calculations
		screen_height = height;
		screen_width = width;
		fovx = (float)Math.PI / 4;
		fovy = (height/width) * fovx;
	}
	
	float getPositionX()
	{
		return position[0] + focus[0];
	}
	
	float getPositionY()
	{
		return position[1] + focus[1];
	}
	
	float getPositionZ()
	{
		return position[2] + focus[2];
	}
	
	Vector3f getUp()
	{
		return up_vector;
	}
	
	float getFocusX()
	{
		return focus[0];
	}
	
	float getFocusY()
	{
		return focus[1];
	}
	
	float getFocusZ()
	{
		return focus[2];
	}
	
	public void changeFocus(float x, float y, float z)
	{
		focus[0] = x;
		focus[1] = y;
		focus[2] = z;
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
		focus[0] += vector3f.x;
		focus[1] += vector3f.y;
		focus[2] += vector3f.z;
		updatePosition();
	}
	
	private void updatePosition()
	{
		 float a = 0;
		 
		 //calculate positions from angles as if focus were (0,0,0)
		 position[1] = (float) ((distance * Math.sin(declination)));
		 a = (float) ((distance * Math.cos(declination)));
		 position[0] = (float) (a*Math.sin(rotation));
		 position[2] = (float) (a*Math.cos(rotation));
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
		focus[0] = 24.0f;
		focus[1] = -11.0f;
		focus[2] = 0.0f;
		
		position[0] = 0.0f;
		position[1] = 0.0f;
		position[2] = 63.0f;
		
		setUpVector(0,1,0);
		
		//debug();
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
	
	public Vector3f getRayToPlane(int mouseX, int mouseY, float zPlane) throws LWJGLException {
		//We need exclusive access to the window
		Display.makeCurrent();
		
		//Create stupid floatbuffers for LWJGL
		IntBuffer viewport = BufferUtils.createIntBuffer(16);
		FloatBuffer modelview = BufferUtils.createFloatBuffer(16);
		FloatBuffer projection = BufferUtils.createFloatBuffer(16);
		FloatBuffer position = BufferUtils.createFloatBuffer(3);
		Vector3f ray = new Vector3f();
		
		//Get some information about the viewport, modelview, and projection matrix
		GL11.glGetFloat( GL11.GL_MODELVIEW_MATRIX, modelview );
		GL11.glGetFloat( GL11.GL_PROJECTION_MATRIX, projection );
		GL11.glGetInteger( GL11.GL_VIEWPORT, viewport );

		//get the position in 3d space by casting a ray from the mouse
		//coords to the first contacted point in space
		GLU.gluUnProject(mouseX, mouseY, 1, modelview, projection, viewport, position);
		
		//Make a vector out of the silly float buffer LWJGL forces us to use
		ray.set(position.get(0), position.get(1), position.get(2));
		
		//Don't want to hold on to the context as the renderer will need it
		Display.releaseContext();

		float t = (zPlane - this.getPositionZ())/ray.z;
		 
		 ray = new Vector3f(this.getPositionX() + t * ray.x,
				 								this.getPositionY() + t * ray.y,
				 								this.getPositionZ() + t * ray.z); 
		
		return ray;
	}
	
	public void debug() {
		//Debug the camera
		//System.out.print("Height:		" + height 	+ "	Width:	" + width + "\n");
		System.out.print("Camera = X:	" + position[0] + "	Y:	" + position[1] + "	Z:	" + position[2] + "\n");
		System.out.print("Focus  = X:	" + focus[0] 	+ "	Y:	" + focus[1] 	+ "	Z:	" + focus[2] 	+ "\n");
		System.out.print("Up     = X:	" + up_vector.x + "	Y:	" + up_vector.y + "	Z:	" + up_vector.z + "\n\n");	
	}
	
}
