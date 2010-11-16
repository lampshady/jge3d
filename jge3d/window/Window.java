package jge3d.window;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import de.matthiasmann.twl.DesktopArea;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.FPSCounter;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ThemeInfo;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import de.matthiasmann.twl.theme.ThemeManager;

public class Window extends DesktopArea {
	private boolean quit=false;
    private final FPSCounter fpsCounter;
    private final Label mouseCoords;

    private MainMenu mainMenu;

    public Window() {
        fpsCounter = new FPSCounter();
        mouseCoords = new Label();

        add(fpsCounter);
        add(mouseCoords);
        
        mainMenu = new MainMenu();
        add(mainMenu);
        
        mainMenu.setSize(400, 200);
        mainMenu.setPosition(0, 0);
    }
    
	public static void init() {
	    try {
	        Display.setDisplayMode(new DisplayMode(800, 600));
	        Display.create();
	        Display.setTitle("JGE3d");
	        Display.setVSyncEnabled(true);
	
	        LWJGLRenderer renderer = new LWJGLRenderer();
	        Window gameUI = new Window();
	        GUI gui = new GUI(gameUI, renderer);
	
	        ThemeManager theme = ThemeManager.createThemeManager(
	        	Window.class.getResource("themes/default.xml"), renderer
	        );
	        gui.applyTheme(theme);
	
	        while(!Display.isCloseRequested() && !gameUI.quit) {
	            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
	
	            gui.update();
	            Display.update();
	
	            //Reduce input lag
	        	GL11.glGetError();          // this call will burn the time between vsyncs
	        	Display.processMessages();  // process new native messages since Display.update();
	        	Mouse.poll();               // now update Mouse events
	        	Keyboard.poll();            // and Keyboard too
	        }
	
	        gui.destroy();
	        theme.destroy();
	    } catch (Exception ex) {
	        showErrMsg(ex);
	    }
	}

    @Override
    protected void layout() {
        super.layout();

        // fpsCounter is bottom right
        fpsCounter.adjustSize();
        fpsCounter.setPosition(
                getInnerWidth() - fpsCounter.getWidth(),
                getInnerHeight() - fpsCounter.getHeight());

        mouseCoords.adjustSize();
        mouseCoords.setPosition(0, getInnerHeight() - fpsCounter.getHeight());
    }

    @Override
    protected void applyTheme(ThemeInfo themeInfo) {
        super.applyTheme(themeInfo);
    }

    @Override
    protected boolean handleEvent(Event evt) {
        if(evt.isMouseEvent()) {
            mouseCoords.setText("x: " + evt.getMouseX() + "  y: " + evt.getMouseY());
        }
        return super.handleEvent(evt) || evt.isMouseEventNoWheel();
    }
	
    public static void showErrMsg(Throwable ex) {
        ex.printStackTrace();
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        pw.flush();
        Sys.alert("Error!", sw.toString());
    }
}
