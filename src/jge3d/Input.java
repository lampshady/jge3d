package jge3d;

import javax.vecmath.Vector3f;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

class Input {
	int deltaX, deltaY;
	int mouseX, mouseY;
	
	public Input() throws LWJGLException
	{
		Mouse.create();
		Mouse.setNativeCursor(null);
		Keyboard.create();
	}

	public void handleMouse(Camera camera, Window window, Physics physics) throws LWJGLException 
	{
		//Handle Mouse Events here
		while(Mouse.next())
		{
			
			Mouse.poll();
			
			//update the changes in position
			deltaX = Mouse.getEventDX();
			deltaY = Mouse.getEventDY();

			mouseX = Mouse.getX();
			mouseY = Mouse.getY();
			
			//editor.setCurrentBlock(mouseX, mouseY, camera);
			
			switch(Mouse.getEventButton())
			{
				case -1://Mouse Movement
					if(Mouse.isInsideWindow())
					{
						if(Mouse.isButtonDown(0))
						{
							//Pan camera Z
							if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) )
							{
								camera.incrementDistance(-0.1f*deltaY);	
							}else{
								camera.moveFocus( new Vector3f(-0.1f*deltaX, -0.1f*deltaY, 0.0f) );
							}
						}
						
						if(Mouse.isButtonDown(1))
						{
							//Change angle of camera
	
						}
						
						if(Mouse.isButtonDown(2))
						{
							//Change Perspective
	
						}
					}
					break;
				case 0://Left Button
					if( Mouse.isButtonDown(0) )
					{
					}else
					{
					}
					break;
				case 1://Right Button
					Vector3f ray = camera.getRayTo(Mouse.getX(), Mouse.getY());
					physics.dropBox(ray.x,ray.y,ray.z,1);
					break;
				case 2://Middle Button
					break;
			}

			switch(Mouse.getDWheel()) {
				case -120: camera.incrementDistance(1.0f); break;
				case  120: camera.incrementDistance(-1.0f); break;
			}
		}
	}
	
	public void handleKeyboard() throws LWJGLException
	{
		while(Keyboard.next())
		{
			Keyboard.poll();
			System.out.print("!!!" + Keyboard.getEventKey() + "!!!");
			switch(Keyboard.getEventCharacter())
			{
			case 'w':	
				break;
			case 'a':
				break;
			case 's':
				break;
			case 'd':
				break;
			}
		}
	}
}