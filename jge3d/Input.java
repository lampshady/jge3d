package jge3d;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.vecmath.Vector3f;

import jge3d.GUI.EditorView;
import jge3d.GUI.LevelView;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

class Input {
	private static Input uniqueInstance = new Input();
	private int deltaX, deltaY;	
	private Player player;
	//private EntityList entity;
	private Level level;
	
	public static Input getInstance()
	{
		return uniqueInstance;
	}
	
	private Input()
	{
		player = new Player();
		
		
		try {
			Mouse.create();
			Mouse.setNativeCursor(null);
			Keyboard.create();
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void setPlayer(Player _player) {
		player = _player;
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
						Editor.getInstance().setCurrentBlock(Mouse.getX(), Mouse.getY(), EditorView.getInstance().getLayer());
						if(Mouse.isButtonDown(0)) {
							//Pan camera Z
							if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) ) {
								Camera.getInstance().incrementDistance(-1.0f*deltaY);	
							} else {
								Camera.getInstance().moveFocus( new Vector3f(-0.1f*deltaX, -0.1f*deltaY, 0.0f) );
							}
						}
						
						if(Mouse.isButtonDown(1)) {
							//Change angle of camera
	
						}
						
						if(Mouse.isButtonDown(2)) {
							//Change Perspective
							Camera.getInstance().incrementDeclination(-deltaY*.01f);
							Camera.getInstance().incrementRotation(-deltaX*.01f);
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
						EntityList.getInstance().addEntity(Editor.getInstance().getCurrentBlock());
					}
					break;
				case 2://Middle Button
					if( !(Mouse.isButtonDown(1)) ) {

					}
					break;
			}

			switch(Mouse.getDWheel()) {
				case -120: 
					//move focus
					
					//update current layer
					EditorView.getInstance().incrementLayer(-1);
					Editor.getInstance().setCurrentBlock(Mouse.getX(), Mouse.getY(), EditorView.getInstance().getLayer());
					
					break;
				case  120: 
					//update current layer
					EditorView.getInstance().incrementLayer(1);
					Editor.getInstance().setCurrentBlock(Mouse.getX(), Mouse.getY(), EditorView.getInstance().getLayer());
					break;
			}
		}
	}
	
	public void handleKeyboard() throws LWJGLException, IOException {

		//while(Keyboard.next()) {

			//get focus if mouse is inside of window
			if(Mouse.isInsideWindow()) {
				Keyboard.poll();

				player.activate();
				
				if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
					System.out.print("w down\n");
					player.move(new Vector3f(0,10,0),20);
				} else {
					//player.slow(new Vector3f(0,-10,0),20);
				}
				if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
					System.out.print("a down\n");
					player.move(new Vector3f(-1,0,0),20);
				} else {
					//player.slow(new Vector3f(1,0,0),20);
				}
				if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
					System.out.print("s down\n");
					player.move(new Vector3f(0,-1,0),20);
				} else {
					//player.slow(new Vector3f(0,1,0),20);
				}
				if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
					System.out.print("d down\n");
					player.move(new Vector3f(1,0,0),20);
				} else {
					//player.slow(new Vector3f(-1,0,0),20); 
				}
				if(Keyboard.isKeyDown(Keyboard.KEY_X)) {
					EntityList.getInstance().deleteByPosition(Editor.getInstance().getCurrentPosition());
				}
				if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
					Vector3f ray = Camera.getInstance().getRayToPlane(Mouse.getX(), Mouse.getY(), new Vector3f(0,0,1), new Vector3f(0,0,0));
					EntityList.getInstance().addEntity(Physics.getInstance().dropBox(ray.x,ray.y,ray.z,1));
				}
				if(Keyboard.isKeyDown(Keyboard.KEY_F1)) {
					level.save();
				}   
				if(Keyboard.isKeyDown(Keyboard.KEY_F2)) {
					LevelView.getInstance().setLoadLevel(true);
				}
			}
			/*
			switch(Keyboard.getEventCharacter()) {
				case 'w':
					player_velocity.y = 200;
					break;
				case 'a':
					player_velocity.x = -20;
					break;
				case 's':
					player_velocity.y = -10;
					break;
				case 'd':
					player_velocity.x = 20;
					break;
				case 'x':
					entity.deleteByPosition(editor.getCurrentPosition());
					break;
				case ' ':
					Vector3f ray = camera.getRayToPlane(Mouse.getX(), Mouse.getY(), new Vector3f(0,0,1), new Vector3f(0,0,0));
					entity.addEntity(physics.dropBox(ray.x,ray.y,ray.z,1));
					break;
				case Keyboard.KEY_F1:
					level.save();
					break;   
				case Keyboard.KEY_F2:
					window.getLevelView().setLoadLevel(true);
					break;
			}
			System.out.print(player.getVelocity().x + " " + player.getVelocity().y + " " + player.getVelocity().z + "\n");
			
			if(player.getVelocity().x > 15 || player.getVelocity().x < -15)
				player_velocity.x=0;
			if(player.getVelocity().y > 15 || player.getVelocity().y < -15)
				player_velocity.y=0;
			if(player.getVelocity().z > 15 || player.getVelocity().z < -15)
				player_velocity.z=0;
			
			player.move(
				new Vector3f(
					player_velocity.x,
					player_velocity.y,
					player_velocity.z
				)
				, 10
			);
			*/
			//player_velocity.x=0;
			//player_velocity.y=0;
			//player_velocity.z=0;
			/*
			switch(Keyboard.getEventKey()) {
				case Keyboard.KEY_W:	
					player.move(new Vector3f(0,200,0), 10);
					System.out.print("up\n");
					break;
				case Keyboard.KEY_A:
					player.move(new Vector3f(-10,0,0), 10);
					System.out.print("left\n");
					break;
				case Keyboard.KEY_S:
					player.move(new Vector3f(0,-10,0), 10);
					System.out.print("down\n");
					break;
				case Keyboard.KEY_D:
					player.move(new Vector3f(10,0,0), 10);
					System.out.print("right\n");
					break;
				case Keyboard.KEY_SPACE:
					Vector3f ray = camera.getRayToPlane(Mouse.getX(), Mouse.getY(), 0);
					physics.dropBox(ray.x,ray.y,ray.z,1);
					break;
				case Keyboard.KEY_F1:
					level.save();
					break;
				case Keyboard.KEY_F2:
					window.setLoadLevel(true);
					break;
			}
			*/
			
		}
	//}
}