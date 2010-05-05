package jge3d.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import jge3d.EntityList;

public class EntityComboBox extends JComboBox {
	private EntityList entity;
	
	public EntityComboBox(EntityList _entity) {
		entity=_entity;
		
		for(int i=0; i<entity.getListSize();i++) {
			this.addItem(entity.get(i).getName());
		}
		this.addItem("asdf");
		
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox jcmbType = (JComboBox) e.getSource();
				String cmbType = (String) jcmbType.getSelectedItem();

				System.out.print("\n\n\n" + cmbType + "\n\n\n" + e.getActionCommand() + "\n\n\n");
			}
		});
	}
	
	public void update() {
		this.removeAllItems();
		
		for(int i=0; i<entity.getListSize();i++) {
			this.addItem(entity.get(i).getName());
		}
	}
}
