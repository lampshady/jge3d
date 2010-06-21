package jge3d.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.Queue;

import jge3d.Input;
import jge3d.physics.Physics;
import jge3d.render.Renderer;

import org.lwjgl.LWJGLException;

public class Controller {
	//the game always runs (except when it doesn't)
	final boolean isRunning = true;
	
	private static Controller uniqueInstance = new Controller();
	Queue<Command> controller_queue = new LinkedList<Command>();
	
	//Create the Input Listening thread
	Thread input_thread = new Thread(new Runnable(){
		@Override
		public void run() {
			while (isRunning) 
			{
				//read keyboard and mouse
				try {
					Input.getInstance().updateInput();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (LWJGLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	},"Input");
	
	//Create the Physics Listening thread
	Thread physics_thread = new Thread(new Runnable(){
		@Override
		public void run() {
			while (isRunning) 
			{
				//Update the physics world
				Physics.getInstance().clientUpdate();
			}
		}
	},"Physics");
	
	//Create the vidya thread
	Thread render_thread = new Thread(new Runnable(){
		@Override
		public void run() {
			while (isRunning) 
			{
				//Update the physics world
				try {
					Renderer.getInstance().draw();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (LWJGLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	},"Renderer");
	
	
	public static Controller getInstance()
	{
		return uniqueInstance;
	}
	
	public void priorityRun(Object classInstance, String methodToInvoke) {
		
	}
	
	public void enqueue(Object classInstance, String methodToInvoke) {
		controller_queue.add(new Command(classInstance,methodToInvoke));
	}
	
	public Boolean hasQueuedItems() {
		if(controller_queue.size() == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	public void start() {
		input_thread.start();
		physics_thread.start();
		render_thread.start();
		input_thread.setPriority(3);
		physics_thread.setPriority(5);
		render_thread.setPriority(6);
	}
	
	public void run_queue() {
		Command commandToRunCommand;
		try {
			for(int i=0; i < controller_queue.size(); i++) {
				commandToRunCommand=controller_queue.poll();
				Method methodToInvoke = commandToRunCommand.getClassInstance().getClass().getDeclaredMethod(commandToRunCommand.getMethodToInvoke());
				methodToInvoke.invoke(commandToRunCommand.getClassInstance());
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public void monitor()
	{
		run_queue();
		check_entities();
	}

	private void check_entities() {

	}
	
}
