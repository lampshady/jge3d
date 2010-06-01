package jge3d.render;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.vecmath.Vector3f;

import jge3d.Camera;
import jge3d.Editor;
import jge3d.Entity;
import jge3d.EntityList;
import jge3d.Level;
import jge3d.Physics;
import jge3d.TextureList;
import jge3d.GUI.EntityView;
import jge3d.GUI.Window;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.opengl.TextureImpl;

import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

public class Renderer {
	public static Renderer uniqueInstance = new Renderer();
	
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
	
    private Renderer(){
    	try {
			initGL();
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
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
	public void drawcube(Entity ent) throws FileNotFoundException, IOException {		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		if(ent.getTransparent()) {
			//GL11.glEnable(GL11.GL_BLEND);
			//GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glEnable(GL11.GL_ALPHA_TEST); // allows alpha channels or transperancy
			//GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
			GL11.glAlphaFunc(GL11.GL_GREATER,0.1f); // sets alpha function
			GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		} else {
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glDisable(GL11.GL_BLEND);
		}

		//bind a texture for drawing
		TextureList.getInstance().getByName(ent.getTextureName()).bind();
		
        GL11.glBegin(GL11.GL_QUADS);
        	// Front Face
        	GL11.glNormal3f( 0.0f, 0.0f, 1.0f);
	        GL11.glTexCoord2f(0.0f, 0.0f); GL11.glVertex3f(-1.0f, -1.0f,  1.0f);   // Bottom Left Of The Texture and Quad
	        GL11.glTexCoord2f(1.0f, 0.0f); GL11.glVertex3f( 1.0f, -1.0f,  1.0f);   // Bottom Right Of The Texture and Quad
	        GL11.glTexCoord2f(1.0f, 1.0f); GL11.glVertex3f( 1.0f,  1.0f,  1.0f);   // Top Right Of The Texture and Quad
	        GL11.glTexCoord2f(0.0f, 1.0f); GL11.glVertex3f(-1.0f,  1.0f,  1.0f);   // Top Left Of The Texture and Quad
	
	        // Back Face
	        GL11.glNormal3f( 0.0f, 0.0f, -1.0f);
	        GL11.glTexCoord2f(1.0f, 0.0f); GL11.glVertex3f(-1.0f, -1.0f, -1.0f);   // Bottom Right Of The Texture and Quad
	        GL11.glTexCoord2f(1.0f, 1.0f); GL11.glVertex3f(-1.0f,  1.0f, -1.0f);   // Top Right Of The Texture and Quad
	        GL11.glTexCoord2f(0.0f, 1.0f); GL11.glVertex3f( 1.0f,  1.0f, -1.0f);   // Top Left Of The Texture and Quad
	        GL11.glTexCoord2f(0.0f, 0.0f); GL11.glVertex3f( 1.0f, -1.0f, -1.0f);   // Bottom Left Of The Texture and Quad
	
	        // Top Face
	        GL11.glNormal3f( 0.0f, 1.0f, 0.0f);
	        GL11.glTexCoord2f(0.0f, 1.0f); GL11.glVertex3f(-1.0f,  1.0f, -1.0f);   // Top Left Of The Texture and Quad
	        GL11.glTexCoord2f(0.0f, 0.0f); GL11.glVertex3f(-1.0f,  1.0f,  1.0f);   // Bottom Left Of The Texture and Quad
	        GL11.glTexCoord2f(1.0f, 0.0f); GL11.glVertex3f( 1.0f,  1.0f,  1.0f);   // Bottom Right Of The Texture and Quad
	        GL11.glTexCoord2f(1.0f, 1.0f); GL11.glVertex3f( 1.0f,  1.0f, -1.0f);   // Top Right Of The Texture and Quad
	
	        // Bottom Face
	        GL11.glNormal3f( 0.0f, -1.0f, 0.0f);
	        GL11.glTexCoord2f(1.0f, 1.0f); GL11.glVertex3f(-1.0f, -1.0f, -1.0f);   // Top Right Of The Texture and Quad
	        GL11.glTexCoord2f(0.0f, 1.0f); GL11.glVertex3f( 1.0f, -1.0f, -1.0f);   // Top Left Of The Texture and Quad
	        GL11.glTexCoord2f(0.0f, 0.0f); GL11.glVertex3f( 1.0f, -1.0f,  1.0f);   // Bottom Left Of The Texture and Quad
	        GL11.glTexCoord2f(1.0f, 0.0f); GL11.glVertex3f(-1.0f, -1.0f,  1.0f);   // Bottom Right Of The Texture and Quad
	
	        // Right face
	        GL11.glNormal3f( 1.0f, 0.0f, 0.0f);
	        GL11.glTexCoord2f(1.0f, 0.0f); GL11.glVertex3f( 1.0f, -1.0f, -1.0f);   // Bottom Right Of The Texture and Quad
	        GL11.glTexCoord2f(1.0f, 1.0f); GL11.glVertex3f( 1.0f,  1.0f, -1.0f);   // Top Right Of The Texture and Quad
	        GL11.glTexCoord2f(0.0f, 1.0f); GL11.glVertex3f( 1.0f,  1.0f,  1.0f);   // Top Left Of The Texture and Quad
	        GL11.glTexCoord2f(0.0f, 0.0f); GL11.glVertex3f( 1.0f, -1.0f,  1.0f);   // Bottom Left Of The Texture and Quad
	
	        // Left Face
	        GL11.glNormal3f( -1.0f, 0.0f, 0.0f);
	        GL11.glTexCoord2f(0.0f, 0.0f); GL11.glVertex3f(-1.0f, -1.0f, -1.0f);   // Bottom Left Of The Texture and Quad
	        GL11.glTexCoord2f(1.0f, 0.0f); GL11.glVertex3f(-1.0f, -1.0f,  1.0f);   // Bottom Right Of The Texture and Quad
	        GL11.glTexCoord2f(1.0f, 1.0f); GL11.glVertex3f(-1.0f,  1.0f,  1.0f);   // Top Right Of The Texture and Quad
	        GL11.glTexCoord2f(0.0f, 1.0f); GL11.glVertex3f(-1.0f,  1.0f, -1.0f);   // Top Left Of The Texture and Quad
        GL11.glEnd();

        //This has to be run or slick caches the 
        //texture and reuses it indefinitely
        //The slick library doesn't mention this once
        TextureImpl.unbind();
	}
	
	public void drawPhysCube(String name, float size) throws FileNotFoundException, IOException {		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);		// Turn Blending On
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.0f);
		//GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		//bind a texture for drawing
		TextureList.getInstance().getByName(name).bind();
		
        GL11.glBegin(GL11.GL_QUADS);
        	// Front Face
        	GL11.glNormal3f( 0.0f, 0.0f, 1.0f);
	        GL11.glTexCoord2f(0.0f, 0.0f); GL11.glVertex3f(-1.0f, -1.0f,  1.0f);   // Bottom Left Of The Texture and Quad
	        GL11.glTexCoord2f(1.0f, 0.0f); GL11.glVertex3f( 1.0f, -1.0f,  1.0f);   // Bottom Right Of The Texture and Quad
	        GL11.glTexCoord2f(1.0f, 1.0f); GL11.glVertex3f( 1.0f,  1.0f,  1.0f);   // Top Right Of The Texture and Quad
	        GL11.glTexCoord2f(0.0f, 1.0f); GL11.glVertex3f(-1.0f,  1.0f,  1.0f);   // Top Left Of The Texture and Quad
	
	        // Back Face
	        GL11.glNormal3f( 0.0f, 0.0f, -1.0f);
	        GL11.glTexCoord2f(1.0f, 0.0f); GL11.glVertex3f(-1.0f, -1.0f, -1.0f);   // Bottom Right Of The Texture and Quad
	        GL11.glTexCoord2f(1.0f, 1.0f); GL11.glVertex3f(-1.0f,  1.0f, -1.0f);   // Top Right Of The Texture and Quad
	        GL11.glTexCoord2f(0.0f, 1.0f); GL11.glVertex3f( 1.0f,  1.0f, -1.0f);   // Top Left Of The Texture and Quad
	        GL11.glTexCoord2f(0.0f, 0.0f); GL11.glVertex3f( 1.0f, -1.0f, -1.0f);   // Bottom Left Of The Texture and Quad
	
	        // Top Face
	        GL11.glNormal3f( 0.0f, 1.0f, 0.0f);
	        GL11.glTexCoord2f(0.0f, 1.0f); GL11.glVertex3f(-1.0f,  1.0f, -1.0f);   // Top Left Of The Texture and Quad
	        GL11.glTexCoord2f(0.0f, 0.0f); GL11.glVertex3f(-1.0f,  1.0f,  1.0f);   // Bottom Left Of The Texture and Quad
	        GL11.glTexCoord2f(1.0f, 0.0f); GL11.glVertex3f( 1.0f,  1.0f,  1.0f);   // Bottom Right Of The Texture and Quad
	        GL11.glTexCoord2f(1.0f, 1.0f); GL11.glVertex3f( 1.0f,  1.0f, -1.0f);   // Top Right Of The Texture and Quad
	
	        // Bottom Face
	        GL11.glNormal3f( 0.0f, -1.0f, 0.0f);
	        GL11.glTexCoord2f(1.0f, 1.0f); GL11.glVertex3f(-1.0f, -1.0f, -1.0f);   // Top Right Of The Texture and Quad
	        GL11.glTexCoord2f(0.0f, 1.0f); GL11.glVertex3f( 1.0f, -1.0f, -1.0f);   // Top Left Of The Texture and Quad
	        GL11.glTexCoord2f(0.0f, 0.0f); GL11.glVertex3f( 1.0f, -1.0f,  1.0f);   // Bottom Left Of The Texture and Quad
	        GL11.glTexCoord2f(1.0f, 0.0f); GL11.glVertex3f(-1.0f, -1.0f,  1.0f);   // Bottom Right Of The Texture and Quad
	
	        // Right face
	        GL11.glNormal3f( 1.0f, 0.0f, 0.0f);
	        GL11.glTexCoord2f(1.0f, 0.0f); GL11.glVertex3f( 1.0f, -1.0f, -1.0f);   // Bottom Right Of The Texture and Quad
	        GL11.glTexCoord2f(1.0f, 1.0f); GL11.glVertex3f( 1.0f,  1.0f, -1.0f);   // Top Right Of The Texture and Quad
	        GL11.glTexCoord2f(0.0f, 1.0f); GL11.glVertex3f( 1.0f,  1.0f,  1.0f);   // Top Left Of The Texture and Quad
	        GL11.glTexCoord2f(0.0f, 0.0f); GL11.glVertex3f( 1.0f, -1.0f,  1.0f);   // Bottom Left Of The Texture and Quad
	
	        // Left Face
	        GL11.glNormal3f( -1.0f, 0.0f, 0.0f);
	        GL11.glTexCoord2f(0.0f, 0.0f); GL11.glVertex3f(-1.0f, -1.0f, -1.0f);   // Bottom Left Of The Texture and Quad
	        GL11.glTexCoord2f(1.0f, 0.0f); GL11.glVertex3f(-1.0f, -1.0f,  1.0f);   // Bottom Right Of The Texture and Quad
	        GL11.glTexCoord2f(1.0f, 1.0f); GL11.glVertex3f(-1.0f,  1.0f,  1.0f);   // Top Right Of The Texture and Quad
	        GL11.glTexCoord2f(0.0f, 1.0f); GL11.glVertex3f(-1.0f,  1.0f, -1.0f);   // Top Left Of The Texture and Quad
        GL11.glEnd();

        //This has to be run or slick caches the 
        //texture and reuses it indefinitely
        //The slick library doesn't mention this once
        TextureImpl.unbind();
	}
	
	public void transparentcube(float alpha, float cube_size) throws FileNotFoundException, IOException {		
		GL11.glEnable(GL11.GL_BLEND);		// Turn Blending On
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);
		
		GL11.glBegin(GL11.GL_QUADS);
	        // Front Face
			GL11.glNormal3f( 0.0f, 0.0f, 1.0f);
	        GL11.glVertex3f(-1.0f, -1.0f,  1.0f);   // Bottom Left Of The Texture and Quad
	        GL11.glVertex3f( 1.0f, -1.0f,  1.0f);   // Bottom Right Of The Texture and Quad
	        GL11.glVertex3f( 1.0f,  1.0f,  1.0f);   // Top Right Of The Texture and Quad
	        GL11.glVertex3f(-1.0f,  1.0f,  1.0f);   // Top Left Of The Texture and Quad
	
	        // Back Face
	        GL11.glNormal3f( 0.0f, 0.0f, -1.0f);
	        GL11.glVertex3f(-1.0f, -1.0f, -1.0f);   // Bottom Right Of The Texture and Quad
	        GL11.glVertex3f(-1.0f,  1.0f, -1.0f);   // Top Right Of The Texture and Quad
	        GL11.glVertex3f( 1.0f,  1.0f, -1.0f);   // Top Left Of The Texture and Quad
	        GL11.glVertex3f( 1.0f, -1.0f, -1.0f);   // Bottom Left Of The Texture and Quad
	
	        // Top Face
	        GL11.glNormal3f( 0.0f, 1.0f, 0.0f);
	        GL11.glVertex3f(-1.0f,  1.0f, -1.0f);   // Top Left Of The Texture and Quad
	        GL11.glVertex3f(-1.0f,  1.0f,  1.0f);   // Bottom Left Of The Texture and Quad
	        GL11.glVertex3f( 1.0f,  1.0f,  1.0f);   // Bottom Right Of The Texture and Quad
	        GL11.glVertex3f( 1.0f,  1.0f, -1.0f);   // Top Right Of The Texture and Quad
	
	        // Bottom Face
	        GL11.glNormal3f( 0.0f, -1.0f, 0.0f);
	        GL11.glVertex3f(-1.0f, -1.0f, -1.0f);   // Top Right Of The Texture and Quad
	        GL11.glVertex3f( 1.0f, -1.0f, -1.0f);   // Top Left Of The Texture and Quad
	        GL11.glVertex3f( 1.0f, -1.0f,  1.0f);   // Bottom Left Of The Texture and Quad
	        GL11.glVertex3f(-1.0f, -1.0f,  1.0f);   // Bottom Right Of The Texture and Quad
	
	        // Right face
	        GL11.glNormal3f( 1.0f, 0.0f, 0.0f);
	        GL11.glVertex3f( 1.0f, -1.0f, -1.0f);   // Bottom Right Of The Texture and Quad
	        GL11.glVertex3f( 1.0f,  1.0f, -1.0f);   // Top Right Of The Texture and Quad
	        GL11.glVertex3f( 1.0f,  1.0f,  1.0f);   // Top Left Of The Texture and Quad
	        GL11.glVertex3f( 1.0f, -1.0f,  1.0f);   // Bottom Left Of The Texture and Quad
	
	        // Left Face
	        GL11.glNormal3f( -1.0f, 0.0f, 0.0f);
	        GL11.glVertex3f(-1.0f, -1.0f, -1.0f);   // Bottom Left Of The Texture and Quad
	        GL11.glVertex3f(-1.0f, -1.0f,  1.0f);   // Bottom Right Of The Texture and Quad
	        GL11.glVertex3f(-1.0f,  1.0f,  1.0f);   // Top Right Of The Texture and Quad
	        GL11.glVertex3f(-1.0f,  1.0f, -1.0f);   // Top Left Of The Texture and Quad
        GL11.glEnd();
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
		if(EntityList.getInstance().getListChanged()) {
			addToLevelList(EntityList.getInstance().getLatestEntity());
			EntityView.getInstance().updateComboBox();
		}
		
        //render level
        renderLevelList();
        
        //render physics
        renderPhysics();
        
        //Render the editor box
		renderEditorBlock();
        
        GL11.glFlush();
		Display.update();	// now tell the screen to update
		
		Display.releaseContext();
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
		
		Display.releaseContext();
	}
	
	public void setPerspective() {
		//Calculate the shape of the screen and notify OpenGL
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(
				45.0f, 
				(float) Window.getInstance().getGLWidth() / (float) Window.getInstance().getGLHeight(), 
				1f, 
				10000.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	
	public void renderEditorBlock() throws FileNotFoundException, IOException {
		Vector3f current_position_vector=Editor.getInstance().getCurrentPosition();
		GL11.glPushMatrix();
			current_position_vector.x = (float)Math.floor(current_position_vector.x);
			current_position_vector.y = (float)Math.floor(current_position_vector.y);
			
			//Z needs to be replaced with -layer * cube_size
			current_position_vector.z = (float)Math.floor(current_position_vector.z);
			GL11.glTranslatef(current_position_vector.x, current_position_vector.y, current_position_vector.z);
			
			//Draw a transparent cube
			transparentcube(1.0f, 1.0f);
			
		GL11.glPopMatrix();
	}
	
	public void makeLevelList() throws LWJGLException, FileNotFoundException, IOException {
		Vector3f position;

		objectlist = GL11.glGenLists(1);
		GL11.glNewList(objectlist,GL11.GL_COMPILE);
		//for each level entity 
			for(int i=0;i<EntityList.getInstance().getListSize();i++) {
				GL11.glPushMatrix();
				position=EntityList.getInstance().getEntityPosition(i);

				//Only add the entity to the physics list if it's collidable
				if(EntityList.getInstance().getEntityCollidable(i) == true) {
					//Add newly created level entity to entitylist
					EntityList.getInstance().setRigidBodyByID(i,
						//Create a physics object for the entity
						Physics.getInstance().addLevelBlock(
							position.x,
							position.y,-position.z,
							EntityList.getInstance().getEntitySize(i)
						)
					);
				}
				
				//Shift object to correct position
				GL11.glTranslatef(
					position.x*EntityList.getInstance().getEntitySize(i),
					position.y*EntityList.getInstance().getEntitySize(i),
					-position.z*EntityList.getInstance().getEntitySize(i)
				);
				
				drawcube(EntityList.getInstance().get(i));
				GL11.glPopMatrix();
			}
		GL11.glEndList();
	}

	public void addToLevelList(Entity newEnt) throws LWJGLException, FileNotFoundException, IOException {
		//Variable to hold position of new level piece
		Vector3f position=newEnt.getPosition();

		//Replace old display list with new one containing new level object
		GL11.glNewList(objectlist,GL11.GL_COMPILE);
		for(int i=0;i<EntityList.getInstance().getListSize();i++) {
			GL11.glPushMatrix();
			position=EntityList.getInstance().getEntityPosition(i);
			
			GL11.glTranslatef(
				position.x*EntityList.getInstance().getEntitySize(i),
				position.y*EntityList.getInstance().getEntitySize(i),
				-position.z*EntityList.getInstance().getEntitySize(i)
			);
			drawcube(EntityList.getInstance().get(i));
			GL11.glPopMatrix();
		}
		GL11.glEndList();

		//Add physics just for the new object
		if(newEnt.getCollidable() == true) {
			Physics.getInstance().addLevelBlock(position.x,position.y,-position.z,newEnt.getSize());
		}
	}
	
	public void renderLevelList() {
		GL11.glCallList(objectlist);
	}

	//Physics transforms
	float[] body_matrix = new float[16];	//hold matrix of box
	FloatBuffer buf = BufferUtils.createFloatBuffer(16);
	private final Transform m = new Transform();	//wtf is this for
	public void renderPhysics() throws FileNotFoundException, IOException {
		if (Physics.getInstance().getDynamicsWorld() != null) {
			int numObjects = Physics.getInstance().getDynamicsWorld().getNumCollisionObjects();
			for (int i = 0; i < numObjects; i++) {
				CollisionObject colObj = Physics.getInstance().getDynamicsWorld().getCollisionObjectArray().get(i);
				RigidBody body = RigidBody.upcast(colObj);
				
				if(body.getInvMass() != 0) {
					if (body != null && body.getMotionState() != null) {
						DefaultMotionState myMotionState = (DefaultMotionState) body.getMotionState();
						m.set(myMotionState.graphicsWorldTrans);
					}
					else {
						colObj.getWorldTransform(m);
					}
	
					GL11.glPushMatrix();
						m.getOpenGLMatrix(body_matrix);
						
						//Put all this matrix shit in a float buffer
						buf.put(body_matrix);
						buf.flip();
						
						GL11.glMultMatrix(buf);
						buf.clear();
		
						//Testing code
						//ObjectPool<Vector3f> vectorsPool = ObjectPool.get(Vector3f.class);
						//BoxShape boxShape = (BoxShape) body.getCollisionShape();
						//Vector3f halfExtent = boxShape.getHalfExtentsWithMargin(vectorsPool.get());
						//GL11.glScalef(1.0f * halfExtent.x, 1.0f * halfExtent.y, 1.0f * halfExtent.z);
						
						//Draw cube at matrix
						drawPhysCube("cube1", 1.0f);
						
						//More testing code
						//vectorsPool.release(halfExtent);
					GL11.glPopMatrix();
				}
			}
		}
	}
}