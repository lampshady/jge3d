package jge3d;

import org.lwjgl.util.vector.Vector3f;


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
		for( int i = 0; i<3; i++ )
			 position[i] = 0;
		
		declination = 0;
		rotation = 0;
		distance = 1;
		setUpVector( 0, 0, 0 );
	}
	
	float[] getPosition()
	{
		return position;
	}
	
	Vector3f getUp()
	{
		return up_vector;
	}
	
	float[] getFocus()
	{
		return focus;
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
	
	private void updatePosition()
	{
		 float a = 0;
		 
		 //calculate positions from angles as if focus were (0,0,0)
		 position[1] = (float) ((distance * Math.sin(declination)));
		 a = (float) ((distance * Math.cos(declination)));
		 position[0] = (float) (a*Math.sin(rotation));
		 position[2] = (float) (a*Math.cos(rotation));
		 
		 //Adjust relative to focus
		 for( int i = 0; i<3; i++ )
			 position[i] += focus[i];
		 
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