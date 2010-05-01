package jge3d;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;

import javax.vecmath.Vector3f;

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

class Renderer {
	private Editor editor;
	private Physics physics;
	private Camera camera;
	private TextureList texture;
	private EntityList entity;
	private int objectlist;
	
	//Notifier for adding textures
	boolean textures_changed = false;	
	
	public Renderer(Level _level, Physics _physics, TextureList _texture, EntityList _entity) {
		physics = _physics;
		texture = _texture;
		entity = _entity;
		
		//Buffer to hold LWJGL matrix transformations (for physics rendering)
		buf = BufferUtils.createFloatBuffer(16);
	}
	
	public void addReferences(Editor _editor, Camera _camera) throws LWJGLException {
		editor=_editor;
		camera=_camera;
	}
	
	public void drawcube(String texture_name, float cube_size) throws FileNotFoundException, IOException {		
		GL11.glEnable(GL11.GL_TEXTURE_2D);    
		GL11.glDisable(GL11.GL_BLEND);		// Turn Blending On
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		
		//bind a texture for drawing
		texture.getByName(texture_name).bind();
		
        GL11.glBegin(GL11.GL_QUADS);
        	// Front Face
        	GL11.glNormal3f( 0.0f, 0.0f, -1.0f);
	        GL11.glTexCoord2f(0.0f, 0.0f); GL11.glVertex3f(-1.0f, -1.0f,  1.0f);   // Bottom Left Of The Texture and Quad
	        GL11.glTexCoord2f(1.0f, 0.0f); GL11.glVertex3f( 1.0f, -1.0f,  1.0f);   // Bottom Right Of The Texture and Quad
	        GL11.glTexCoord2f(1.0f, 1.0f); GL11.glVertex3f( 1.0f,  1.0f,  1.0f);   // Top Right Of The Texture and Quad
	        GL11.glTexCoord2f(0.0f, 1.0f); GL11.glVertex3f(-1.0f,  1.0f,  1.0f);   // Top Left Of The Texture and Quad
	
	        // Back Face
	        GL11.glNormal3f( 0.0f, 0.0f, 1.0f);
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
			GL11.glNormal3f( 0.0f, 0.0f, -1.0f);
	        GL11.glVertex3f(-1.0f, -1.0f,  1.0f);   // Bottom Left Of The Texture and Quad
	        GL11.glVertex3f( 1.0f, -1.0f,  1.0f);   // Bottom Right Of The Texture and Quad
	        GL11.glVertex3f( 1.0f,  1.0f,  1.0f);   // Top Right Of The Texture and Quad
	        GL11.glVertex3f(-1.0f,  1.0f,  1.0f);   // Top Left Of The Texture and Quad
	
	        // Back Face
	        GL11.glNormal3f( 0.0f, 0.0f, 1.0f);
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
				camera.getPositionX()	, camera.getPositionY()	,	camera.getPositionZ(),
				camera.getFocusX()		, camera.getFocusY()	,	camera.getFocusZ(),
				camera.getUpVectorX()	, camera.getUpVectorY()	,	camera.getUpVectorZ()
		);
		
		//Check if level has been altered since last frame
		if(entity.getListChanged()) {
			addToLevelList(entity.getLatestEntity());
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

	public void initGL(Window window) throws LWJGLException {
		Display.makeCurrent();
		
		//initialize the view
		setPerspective(window);
		GL11.glEnable(GL11.GL_TEXTURE_2D);     
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glClearColor(0.0f, 0.0f, 1.0f, 0.0f);
		GL11.glClearDepth(1.0f);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
		
		Display.releaseContext();
	}
	
	public void setPerspective(Window window) {
		//Calculate the shape of the screen and notify OpenGL
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(45.0f, (float) window.getGLWidth() / (float) window.getGLHeight(), 1f, 10000.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	
	public void renderEditorBlock() throws FileNotFoundException, IOException {
		Vector3f current_position_vector=editor.getCurrentPosition();
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
			for(int i=0;i<entity.getListSize();i++) {
				GL11.glPushMatrix();
				position=entity.getEntityPosition(i);

				if(entity.getEntityCollidable(i) == true) {
					physics.addLevelBlock(position.x,position.y,-position.z,entity.getEntitySize(i));
				}
				
				GL11.glTranslatef(
					position.x*entity.getEntitySize(i),
					position.y*entity.getEntitySize(i),
					-position.z*entity.getEntitySize(i)
				);
				
				drawcube(entity.getEntityTextureName(i), entity.getEntitySize(i));
				GL11.glPopMatrix();
			}
		GL11.glEndList();
	}

	public void addToLevelList(Entity newEnt) throws LWJGLException, FileNotFoundException, IOException {
		//Variable to hold position of new level piece
		Vector3f position=newEnt.getPosition();

		//Replace old display list with new one containing new level object
		GL11.glNewList(objectlist,GL11.GL_COMPILE);
		for(int i=0;i<entity.getListSize();i++) {
			GL11.glPushMatrix();
			position=entity.getEntityPosition(i);
			
			GL11.glTranslatef(
				position.x*entity.getEntitySize(i),
				position.y*entity.getEntitySize(i),
				-position.z*entity.getEntitySize(i)
			);
			drawcube(entity.getEntityTextureName(i), entity.getEntitySize(i));
			GL11.glPopMatrix();
		}
		GL11.glEndList();

		//Add physics just for the new object
		if(newEnt.getCollidable() == true) {
			physics.addLevelBlock(position.x,position.y,-position.z,newEnt.getSize());
		}
	}
	
	public void renderLevelList() {
		GL11.glCallList(objectlist);
	}

	//Physics transforms
	float[] body_matrix = new float[16];	//hold matrix of box
	FloatBuffer buf;
	private final Transform m = new Transform();	//wtf is this for
	public void renderPhysics() throws FileNotFoundException, IOException {
		if (physics.getDynamicsWorld() != null) {
			int numObjects = physics.getDynamicsWorld().getNumCollisionObjects();
			for (int i = 0; i < numObjects; i++) {
				CollisionObject colObj = physics.getDynamicsWorld().getCollisionObjectArray().get(i);
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
						drawcube("cube1", 1);
						
						//More testing code
						//vectorsPool.release(halfExtent);
					GL11.glPopMatrix();
				}
			}
		}
	}
}