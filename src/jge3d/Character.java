package jge3d;

import org.lwjgl.util.vector.Vector3f;

//Parent class of Players, NPCs, Monsters, etc.
public class Character extends Object{
	float[] position = {0.0f,0.0f,0.0f};
	//inventory; //Linked list of objects
	//equipment; //Linked list of objects
	
	//Sprite?
	
	//Stats?
	
	Character()
	{
	}
	
	public void move( Vector3f direction )
	{
		//Apply impulse in proper direction
	}
	
	public boolean equip()
	{
		//Check if the character can equip such an item
		
		//if so, do so
		return true;
		//if not, don't
	}
	
	public void chase()
	{
		//follow target, attacking when within range
	}
	
	public void attack()
	{
		//normal attack for this class
	}
}
