package jge3d.render;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.vecmath.Vector3f;

import jge3d.Camera;
import jge3d.Editor;
import jge3d.Entity;
import jge3d.EntityList;
import jge3d.Level;
import jge3d.TextureList;
import jge3d.controller.Controller;
import jge3d.gui.EntityComboBox;
import jge3d.gui.Window;
import jge3d.physics.Physics;
import jge3d.render.primitives.Cube;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.util.ObjectPool;


public class Renderer {
	public static Renderer uniqueInstance = new Renderer();
	
	long frames=0;
	
	//private Editor editor;
	//private Physics physics;
	//private Camera camera;
	//private TextureList texture;
	//private EntityList entity;
	private int objectlist;
	
	//Notifier for adding textures
	boolean textures_changed = false;	
	
	//Default light (needs turning into an entity
    private float lightAmbient[]={ 0.5f, 0.5f, 0.5f, 1.0f };    // Ambient Light Values ( NEW )
    private float lightDiffuse[]={ 1.0f, 1.0f, 1.0f, 1.0f };    // Diffuse Light Values ( NEW )
    private float lightPosition[]={ 0.0f, 0.0f, 2.0f, 1.0f };   // Light Position ( NEW )
	
    public Ray findRay = new Ray();
    
    private Renderer(){
    	try {
			initGL();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
    };
    
    public static Renderer getInstance(){
    	return uniqueInstance;
    }
    
	private Renderer(Level _level, Physics _physics, TextureList _texture, EntityList _entity) {
		//physics = _physics;
		//texture = _texture;
		//entity = _entity;
		
		//Buffer to hold LWJGL matrix transformations (for physics rendering)
		buf = BufferUtils.createFloatBuffer(16);
	}
	public void draw() throws LWJGLException, FileNotFoundException, IOException {
		//Make sure that the screen is active
		//(doing this every frame slows stuff down)
		Display.makeCurrent();

		// render using OpenGL 
	    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // Clear The Screen And The Depth Buffer
		GL11.glLoadIdentity();
		
		//Place the camera
		GLU.gluLookAt(
				Camera.getInstance().getPositionX()	, Camera.getInstance().getPositionY()	,	Camera.getInstance().getPositionZ(),
				Camera.getInstance().getFocusX(), Camera.getInstance().getFocusY()	,	Camera.getInstance().getFocusZ(),
				Camera.getInstance().getUpVectorX()	, Camera.getInstance().getUpVectorY()	,	Camera.getInstance().getUpVectorZ()
		);

		//Check if level has been altered since last frame
		/*
		if(EntityList.getInstance().getListChanged()) {
			addToLevelList(EntityList.getInstance().getLatestEntity());
			EntityView.getInstance().updateComboBox();
		}*/
		
		//
		
        //render level
        renderLevelList();
        
        //render physics
        renderPhysics();
        
        //Render the editor box
		renderEditorBlock();
        
        GL11.glFlush();
		Display.update();	// now tell the screen to update
		
		Display.releaseContext();
		
		frames++;
	}

	public void initGL() throws LWJGLException {
		Display.makeCurrent();
		
		//initialize the view
		setPerspective();
		GL11.glEnable(GL11.GL_TEXTURE_2D);     
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glClearColor(0.0f, 0.0f, 1.0f, 0.5f);
		GL11.glClearDepth(1.0f);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
		
		//Initialize default settings
        ByteBuffer temp = ByteBuffer.allocateDirect(16);
        temp.order(ByteOrder.nativeOrder());
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_AMBIENT, (FloatBuffer)temp.asFloatBuffer().put(lightAmbient).flip());              // Setup The Ambient Light
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, (FloatBuffer)temp.asFloatBuffer().put(lightDiffuse).flip());              // Setup The Diffuse Light
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_POSITION,(FloatBuffer)temp.asFloatBuffer().put(lightPosition).flip());         // Position The Light
        GL11.glEnable(GL11.GL_LIGHT1);    
		GL11.glEnable(GL11.GL_LIGHTING);
		
        GL11.glEnable(GL11.GL_NORMALIZE);
        GL11.glEnable(GL11.GL_POLYGON_SMOOTH_HINT);
		
		Display.releaseContext();
	}
	
	public void setPerspective() {
		//Calculate the shape of the screen and notify OpenGL
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(
				45.0f, 
				(float) Window.getInstance().getGLWidth() / (float) Window.getInstance().getGLHeight(), 
				0.1f, 
				1000.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	
	public void renderEditorBlock() throws FileNotFoundException, IOException, LWJGLException {
		Vector3f current_position_vector=Editor.getInstance().getCurrentPosition();
		
		//What the shit, seriously
		Display.makeCurrent();
		
		GL11.glPushMatrix();
			current_position_vector.x = (float)Math.floor(current_position_vector.x);
			current_position_vector.y = (float)Math.floor(current_position_vector.y);
			
			//Z needs to be replaced with -layer * cube_size
			current_position_vector.z = (float)Math.floor(current_position_vector.z);
			GL11.glTranslatef(current_position_vector.x, current_position_vector.y, current_position_vector.z);
			
			//Draw a transparent cube
			Cube.transparentcube(1.0f, 1.0f);
			
		GL11.glPopMatrix();
	}
	
	public void makeLevelList() throws LWJGLException, FileNotFoundException, IOException {
		Vector3f position;

		objectlist = GL11.glGenLists(1);
		GL11.glNewList(objectlist,GL11.GL_COMPILE);
		
		//for each level entity 
		String[] levelTypes = {"Player", "Camera", "World", "Triggers"};
		
		for(String type : levelTypes)
		{
			ArrayList<Entity> entities = EntityList.getInstance().getEntitiesByType(type);
			if(entities != null)
			{
				for(int i=0;i<entities.size();i++) {
					if(entities.get(i).getMass() == 0) {
						GL11.glPushMatrix();
						position=entities.get(i).getPosition();
			
						//Shift object to correct position
						GL11.glTranslatef(
							position.x*entities.get(i).getSize(),
							position.y*entities.get(i).getSize(),
							position.z*entities.get(i).getSize()
						);
						
						Cube.drawcube(entities.get(i));
						GL11.glPopMatrix();
					}
				}
			}
		}

		

		GL11.glEndList();
	}

	public void addToLevelList(Entity newEnt) throws LWJGLException, FileNotFoundException, IOException {
		//Variable to hold position of new level piece
		Vector3f position=newEnt.getPosition();

		//Replace old display list with new one containing new level object
		String[] levelTypes = {"Player", "Camera", "World", "Triggers"};
		
		GL11.glNewList(objectlist,GL11.GL_COMPILE);
		for(String type : levelTypes)
		{
			ArrayList<Entity> entities = EntityList.getInstance().getEntitiesByType(type);
			
			for(int i=0;i<entities.size();i++) {
				GL11.glPushMatrix();
				position=entities.get(i).getPosition();
				
				GL11.glTranslatef(
					position.x*entities.get(i).getSize(),
					position.y*entities.get(i).getSize(),
					position.z*entities.get(i).getSize()
				);
				Cube.drawcube(entities.get(i));
				GL11.glPopMatrix();
			}
		}
		GL11.glEndList();
	}
	
	public void renderLevelList() {
		GL11.glCallList(objectlist);
	}

	//Physics transforms
	float[] body_matrix = new float[16];	//hold matrix of object
	FloatBuffer buf = BufferUtils.createFloatBuffer(16);
	private final Transform transformMatrix = new Transform();
	public void renderPhysics() throws FileNotFoundException, IOException {
		if (Physics.getInstance().getDynamicsWorld() != null) {
			int numObjects = Physics.getInstance().getDynamicsWorld().getNumCollisionObjects();
			for (int i = 0; i < numObjects; i++) {
				CollisionObject colObj = Physics.getInstance().getDynamicsWorld().getCollisionObjectArray().get(i);
				RigidBody body = RigidBody.upcast(colObj);
				
				if(body.getInvMass() != 0) {
					if (body != null && body.getMotionState() != null) {
						DefaultMotionState myMotionState = (DefaultMotionState) body.getMotionState();
						transformMatrix.set(myMotionState.graphicsWorldTrans);
					}
					else {
						colObj.getWorldTransform(transformMatrix);
					}
	
					GL11.glPushMatrix();
						transformMatrix.getOpenGLMatrix(body_matrix);
						
						//Put all this matrix shit in a float buffer
						buf.put(body_matrix);
						buf.flip();

						GL11.glMultMatrix(buf);
						buf.clear();
						
						//Testing code
						ObjectPool<Vector3f> vectorsPool = ObjectPool.get(Vector3f.class);
						BoxShape boxShape = (BoxShape) body.getCollisionShape();
						Vector3f halfExtent = boxShape.getHalfExtentsWithMargin(vectorsPool.get());
						GL11.glScalef(1.0f * halfExtent.x, 1.0f * halfExtent.y, 1.0f * halfExtent.z);
						
						//Draw cube at matrix
						Cube.drawPhysCube("cube1", 1.0f);
						
						//More testing code
						//vectorsPool.release(halfExtent);
					GL11.glPopMatrix();
				}
			}
		}
	}
	
	public long getFrames() {
		return frames;
	}
	
	public void resetFrames() {
		frames=0;
	}
}