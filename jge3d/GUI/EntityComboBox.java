package jge3d.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import jge3d.EntityList;

public class EntityComboBox extends JComboBox {
	private static final long serialVersionUID = 1L;
	
	private EntityList entity;
	
	public EntityComboBox(EntityList _entity) {
		entity=_entity;

		this.addItem("None Selected");
		
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setSelectedItem(getSelectedItem());
				//Window.setTableEntity(entity.getByName(getSelectedItem().toString()));
			}
		});
	}
	
	public String getValue() {
		return (String)this.getSelectedItem();
	}
	
	public void update() {
		this.removeAllItems();
		
		for(int i=0; i<entity.getListSize();i++) {
			this.addItem(entity.get(i).getName());
		}
	}
}
