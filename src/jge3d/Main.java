package jge3d;

import jge3d.Window;
import net.java.games.*;

public class Main {
	public static void main(String[] args) {
		//Create window
		Window window = new Window();
		
		//stuff
		boolean done=true;
		
		//Timer related stuff
		final long tick_ms = 32;
		long dt = 0;
		long phys_timer_prev = System.currentTimeMillis();
		long phys_timer_curr = phys_timer_prev;
		long game_timer = System.currentTimeMillis();
		
		do{
			phys_timer_curr = System.currentTimeMillis();
			
			//DO YALL PHYSICS BOOOOYYYYY srsly
			//mWorld->stepSimulation(((float)(phys_timer_curr - phys_timer_prev))/1000.0, 10);
			
			phys_timer_prev = phys_timer_curr;
			
			//Compare to last time
			dt = System.currentTimeMillis() - game_timer;
			while( dt >= tick_ms ) {
				dt -= tick_ms;
				game_timer += tick_ms;
				
				//Grab user input

				//Do stuff that needs to occur every tick
			}
		} while(!done);
	}
}