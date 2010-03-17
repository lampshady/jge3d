package jge3d;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.sun.opengl.util.Animator;

public class Main implements GLEventListener{
	static JFrame window = new JFrame();
	static GLCanvas canvas = new GLCanvas();
	
	static JPanel topRightPane = new JPanel();
	static JPanel bottomRightPane = new JPanel();
	
	static JSplitPane innerPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topRightPane, bottomRightPane); 
	static JSplitPane mainPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, canvas, innerPane);
	
	static Animator animator = new Animator(canvas);
	static GLU glu = new GLU();
	
	static float rotationAngle = 0.0f;
	static long previousTime = 0;
	
	public static void main(String[] args) {
		canvas.addGLEventListener(new Main());
		canvas.setPreferredSize(new Dimension(0, 0));
		window.add(mainPane);
		window.setSize(640, 480);
		window.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		
		window.setVisible(true);
		
		animator.start();
			/* |
			 * |-> Creates a GLAutoDrawable
			 * |		\-> Creates GL
			 * |-> Throws event for init(), passes GLAutoDrawable
			 * |-> Loops event for display(), passes GLAutoDrawable
			 */
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		final GL gl = drawable.getGL();
		
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
		
		gl.glLoadIdentity();
		gl.glTranslatef( 0.0f, 0.0f, -5.0f);
		
		gl.glRotatef(rotationAngle, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(rotationAngle, 0.0f, 1.0f, 0.0f);
		gl.glRotatef(rotationAngle, 0.0f, 0.0f, 1.0f);
		gl.glRotatef(rotationAngle, 0.0f, 1.0f, 0.0f);
	 
		gl.glBegin(GL.GL_TRIANGLES);
	 
		// Front
		gl.glColor3f(0.0f, 1.0f, 1.0f); 
		gl.glVertex3f(0.0f, 1.0f, 0.0f);
		gl.glColor3f(0.0f, 0.0f, 1.0f); 
		gl.glVertex3f(-1.0f, -1.0f, 1.0f);
		gl.glColor3f(0.0f, 0.0f, 0.0f); 
		gl.glVertex3f(1.0f, -1.0f, 1.0f);
	 
		// Right Side Facing Front
		gl.glColor3f(0.0f, 1.0f, 1.0f); 
		gl.glVertex3f(0.0f, 1.0f, 0.0f);
		gl.glColor3f(0.0f, 0.0f, 1.0f); 
		gl.glVertex3f(1.0f, -1.0f, 1.0f);
		gl.glColor3f(0.0f, 0.0f, 0.0f); 
		gl.glVertex3f(0.0f, -1.0f, -1.0f);
	 
		// Left Side Facing Front
		gl.glColor3f(0.0f, 1.0f, 1.0f); 
		gl.glVertex3f(0.0f, 1.0f, 0.0f);
		gl.glColor3f(0.0f, 0.0f, 1.0f); 
		gl.glVertex3f(0.0f, -1.0f, -1.0f);
		gl.glColor3f(0.0f, 0.0f, 0.0f); 
		gl.glVertex3f(-1.0f, -1.0f, 1.0f);
	 
		// Bottom
		gl.glColor3f(0.0f, 0.0f, 0.0f); 
		gl.glVertex3f(-1.0f, -1.0f, 1.0f);
		gl.glColor3f(0.1f, 0.1f, 0.1f); 
		gl.glVertex3f(1.0f, -1.0f, 1.0f);
		gl.glColor3f(0.2f, 0.2f, 0.2f); 
		gl.glVertex3f(0.0f, -1.0f, -1.0f);
	 
		gl.glEnd();

		
		gl.glFlush();
		
		rotationAngle += 0.2f;
		
	}

	@Override
	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
			boolean deviceChanged) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		GL gl = drawable.getGL();
		gl.glShadeModel(GL.GL_SMOOTH);
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDepthFunc(GL.GL_LEQUAL);
		gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, 
			  GL.GL_NICEST);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GL gl = drawable.getGL();
		if(height <= 0){
			height = 1;
		}
		float aspectRatio = (float)width / (float)height;
		
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		
		glu.gluPerspective(90.0f, aspectRatio, 1.0, 200.0);
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
}