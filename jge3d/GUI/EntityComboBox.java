package jge3d.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


import javax.swing.JComboBox;


import jge3d.EntityList;
import jge3d.monitoring.Observer;
import jge3d.monitoring.Subject;

public class EntityComboBox extends JComboBox implements Subject{
	private static final long serialVersionUID = 1L;
	private static EntityComboBox uniqueInstance = new EntityComboBox();
	
	ArrayList<Observer> listOfObservers = new ArrayList<Observer>();
	//private EntityList entity;
	
	
	public static EntityComboBox getInstance()
	{
		return uniqueInstance;
	}
	
	private EntityComboBox() {

		this.addItem("None Selected");
		
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setSelectedItem(getSelectedItem());
				//getParent().getParent().getParent().updateTable(entity.getByName(getSelectedItem().toString()));
				//System.out.print(e.getSource().getClass().toString());
				
				notifyObservers();
			}
		});
	}
	
	public String getValue() {
		return (String)this.getSelectedItem();
	}
	
	public void update() {
		this.removeAllItems();
		
		for(int i=0; i<EntityList.getInstance().getListSize();i++) {
			this.addItem(EntityList.getInstance().get(i).getName());
		}
	}
	
	/*private void updateTable() {
		//getParent().setTableEntity(entity.getByName(getSelectedItem().toString()));
		notifyObservers();
		System.out.print(getParent().getClass().toString());
	}*/

	@Override
	public void notifyObservers() {
		for(Observer o : listOfObservers)
		{
			o.update(this.getValue());
		}
	}

	@Override
	public void registerObserver(Observer o) {
		listOfObservers.add(o);
	}

	@Override
	public void removeObserver(Observer o) {
		listOfObservers.remove(o);
	}
}
