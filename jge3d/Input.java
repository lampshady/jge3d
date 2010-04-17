package jge3d;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.vecmath.Vector3f;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

class Input {
	private int deltaX, deltaY;	
	private Camera camera;
	private Window window;
	private Physics physics;
	private Editor editor;
	private Level level;
	
	public Input(Camera _camera, Window _window, Physics _physics, Editor _editor, Level _level) throws LWJGLException {
		camera=_camera;
		window=_window;
		physics=_physics;
		editor=_editor;
		level=_level;
		
		Mouse.create();
		Mouse.setNativeCursor(null);
		Keyboard.create();
	}

	public void handleMouse() throws LWJGLException, FileNotFoundException, IOException {
		//Handle Mouse Events here
		while(Mouse.next())	{
			Mouse.poll();
			
			//update the changes in position
			deltaX = Mouse.getEventDX();
			deltaY = Mouse.getEventDY();

			switch(Mouse.getEventButton()) {
				case -1://Mouse Movement
					if(Mouse.isInsideWindow()) {
						editor.setCurrentBlock(Mouse.getX(), Mouse.getY(), window.getLayer(), camera);
						if(Mouse.isButtonDown(0)) {
							//Pan camera Z
							if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) ) {
								camera.incrementDistance(-1.0f*deltaY);	
							} else {
								camera.moveFocus( new Vector3f(-0.1f*deltaX, -0.1f*deltaY, 0.0f) );
							}
						}
						
						if(Mouse.isButtonDown(1)) {
							//Change angle of camera
	
						}
						
						if(Mouse.isButtonDown(2)) {
							//Change Perspective
	
						}
					}
					break;
				case 0://Left Button
					if( Mouse.isButtonDown(0) )	{
						
					} else {
						
					}
					break;
				case 1://Right Button
					if( !(Mouse.isButtonDown(1)) ) {
						level.opengladdtolist(level.addEntity(editor.getCurrentBlock()));
					}
					break;
				case 2://Middle Button
					if( !(Mouse.isButtonDown(1)) ) {
						Vector3f ray = camera.getRayToPlane(Mouse.getX(), Mouse.getY(), 0);
						physics.dropBox(ray.x,ray.y,ray.z,1);
					}
					break;
			}

			switch(Mouse.getDWheel()) {
				case -120: camera.incrementDistance(5.0f); break;
				case  120: camera.incrementDistance(-5.0f); break;
			}
		}
	}
	
	public void handleKeyboard() throws LWJGLException, IOException {
		while(Keyboard.next()) {
			Keyboard.poll();
			
			switch(Keyboard.getEventCharacter()) {
			case 'w':	
				break;
			case 'a':
				break;
			case 's':
				level.save();
				break;
			case 'd':
				level.load();
				break;
			}
		}
	}
}