package jge3d.GUI;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import jge3d.EntityList;

public class EntityView extends JPanel {
	private EntityList entity;
	private JComboBox combo_box;
	private EntityTable table;
	
	public EntityView(EntityList _entity){
		entity = _entity;
		
		combo_box = new JComboBox();
		table = new EntityTable(entity);
		
		this.add(combo_box);
		this.add(table);
		
	}
}
