package jge3d;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

import com.sun.opengl.util.Animator;

public class Main implements GLEventListener{
	static JFrame window = new JFrame();
	static GLCanvas canvas = new GLCanvas();
	static Animator animator = new Animator(canvas);
	static GLU glu = new GLU();
	
	public static void main(String[] args) {
		canvas.addGLEventListener(new Main());
		window.add(canvas);
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
		
		//gl.glBegin();
		//gl.glEnd();
		
		gl.glFlush();
		
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
		
		glu.gluPerspective(50.0f, aspectRatio, 1.0, 1000.0);
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
}