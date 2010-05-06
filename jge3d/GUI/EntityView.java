package jge3d.GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jge3d.EntityList;

public class EntityView extends JPanel {
	private EntityList entity;
	private EntityComboBox combo_box;
	private EntityTable table;
	
    protected JScrollPane table_scroller;
    protected JScrollPane combo_scroller;
	
	public EntityView(EntityList _entity){
		entity = _entity;
		
		combo_box = new EntityComboBox(entity);
		table = new EntityTable(entity);
		
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
		return (String)combo_box.getValue();
	}
}
