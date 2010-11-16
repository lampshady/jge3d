package jge3d.window;

import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.ScrollPane;
import de.matthiasmann.twl.TextArea;
import de.matthiasmann.twl.textarea.HTMLTextAreaModel;

public class MainMenu extends ResizableFrame {
    private final StringBuilder sb;
    private final HTMLTextAreaModel textAreaModel;
    private final TextArea textArea;
    private final EditField editField;
    private final ScrollPane scrollPane;
    private int curColor;

    public MainMenu() {
        setTitle("TEST");

        this.sb = new StringBuilder();
        this.textAreaModel = new HTMLTextAreaModel();
        this.textArea = new TextArea(textAreaModel);
        this.editField = new EditField();

        editField.addCallback(new EditField.Callback() {
            public void callback(int key) {
                if(key == Event.KEY_RETURN) {
                    // cycle through 3 different colors/font styles
                    appendRow("color"+curColor, editField.getText());
                    editField.setText("");
                    curColor = (curColor + 1) % 3;
                }
            }
        });

        scrollPane = new ScrollPane(textArea);
        scrollPane.setFixed(ScrollPane.Fixed.HORIZONTAL);

        DialogLayout l = new DialogLayout();
        l.setTheme("content");
        l.setHorizontalGroup(l.createParallelGroup(scrollPane, editField));
        l.setVerticalGroup(l.createSequentialGroup(scrollPane, editField));

        add(l);

        appendRow("default", "Do some testing in the testbox for the test program to test it for testing's sake:");
    }

    private void appendRow(String font, String text) {
        sb.append("<div style=\"font-family: ").append(font).append("; \">");
        // not efficient but simple
        for(int i=0,l=text.length() ; i<l ; i++) {
            char ch = text.charAt(i);
            switch(ch) {
                case '<': sb.append("&lt;"); break;
                case '>': sb.append("&gt;"); break;
                case '&': sb.append("&amp;"); break;
                case '"': sb.append("&quot;"); break;
                case ':':
                    if(text.startsWith(":)", i)) {
                        sb.append("<img src=\"smiley\" alt=\":)\"/>");
                        i += 1; // skip one less because of i++ in the for loop
                        break;
                    }
                    sb.append(ch);
                    break;
                    // fall through:
                default:
                    sb.append(ch);
            }
        }
        sb.append("</div>");

        boolean isAtEnd = scrollPane.getMaxScrollPosY() == scrollPane.getScrollPositionY();

        textAreaModel.setHtml(sb.toString());

        if(isAtEnd) {
            scrollPane.validateLayout();
            scrollPane.setScrollPositionY(scrollPane.getMaxScrollPosY());
        }
    }
}
