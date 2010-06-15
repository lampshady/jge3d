package jge3d.GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jge3d.Entity;



public class EntityView extends JPanel {
	private static final long serialVersionUID = 1L;
	private static EntityView uniqueInstance = new EntityView();
	
	
	//public static EntityList entity;
	//public static EntityComboBox combo_box;
	//public EntityTable table;
	
    protected JScrollPane table_scroller;
    protected JScrollPane combo_scroller;
	
    public static EntityView getInstance()
    {
    	return uniqueInstance;
    }
    
	private EntityView(){
		
		EntityComboBox  combo_box = EntityComboBox.getInstance();
		EntityTable table = EntityTable.getInstance();
		
		this.add(combo_box, BorderLayout.PAGE_START);
		this.add(table);
		
		this.setLayout(new BorderLayout());
		
		table_scroller = new javax.swing.JScrollPane(table);
        combo_scroller = new javax.swing.JScrollPane(combo_box);
        
        table.setPreferredScrollableViewportSize(new Dimension(super.getWidth()-2, 100));
        table.setPreferredScrollableViewportSize(new Dimension(super.getWidth()-2, 100));
        
        this.add(combo_scroller, BorderLayout.NORTH);
        this.add(table_scroller, BorderLayout.CENTER);
	}
	
	public String getComboValue() {
		return (String)EntityComboBox.getInstance().getValue();
	}
	
	public void updateComboBox() {
		EntityComboBox.getInstance().update();
	}
	
	public void setTableEntity(Entity ent) {
		EntityTableModel.getInstance().setEntity(ent);
	}
}
