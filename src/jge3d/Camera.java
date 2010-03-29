package jge3d;

import javax.vecmath.Vector3f;

//Maybe we'll use it, maybe we won't?
public class Camera {
	float[] position;					//x, y, z
	float declination;					//Angle up and down
	float rotation;						//Angle left and right
	float[] focus;						//x, y, z of target
	float distance;						//distance from focus
	Vector3f up_vector;					//vector pointing up
	
	//Don't flip over, its confusing. lulz
	float maximum_declination = 89.0f;
	float minimum_declination = 0.1f;
	
	
	Camera(){
		//initial setup (float about 0,0,0 I guess?
		
		position = new float[3];
		focus = new float[3];
		
		for( int i = 0; i<3; i++ )
			 focus[i] = 0;
		
		declination = 0;
		rotation = 0;
		distance = 1;
		setUpVector( 0, 1, 0 );
		updatePosition();
	}
	
	Camera(float x, float y, float z)
	{
		position = new float[3];
		focus = new float[3];
		
		focus[0] = x;
		focus[1] = y;
		focus[2] = z;
		
		declination = 0;
		rotation = 0;
		distance = 1;
		setUpVector( 0, 1, 0 );
		updatePosition();
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
		 
		 /*
		 //Adjust relative to focus
		 for( int i = 0; i<3; i++ )
			 position[i] += focus[i];
		 */
		 
		 //System.out.print(position[0]+ ","+position[1]+","+position[2]+"\n");
	}
	
	private void setUpVector(float x, float y, float z)
	{
		setUpVector( new Vector3f( x, y, z ) );
	}
	
	private void setUpVector( Vector3f newUp )
	{
		up_vector = newUp;
	}
	
	
	//System.out.print("Cam = X:" + CameraPosition[0] + "Y:" + CameraPosition[1] + "Z:" + CameraPosition[2] + "\n");
	//System.out.print("Look= X:" + CameraPosition[0] + "Y:" + CameraPosition[1] + "Z:" + CameraPosition[2] + "\n");
	//System.out.print("Up  = X:" + CameraPosition[0] + "Y:" + CameraPosition[1] + "Z:" + CameraPosition[2] + "\n");
}
