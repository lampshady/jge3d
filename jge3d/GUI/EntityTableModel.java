package jge3d.gui;

import java.lang.reflect.InvocationTargetException;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;


import jge3d.Entity;
import jge3d.EntityList;
import jge3d.monitoring.Observer;

public class EntityTableModel extends AbstractTableModel implements Observer{
	private static final long serialVersionUID = 1L;
	public static EntityTableModel uniqueInstance= new EntityTableModel();
	public static final int KEY = 0;
    public static final int VALUE = 1;
    private static final String[] columnNames = {"Key", "Value"};
    protected Vector<Object> dataVector;
    public static Entity ent;

    private EntityTableModel() {
        dataVector = new Vector<Object>();
        EntityComboBox.getInstance().registerObserver(this);
    }
    
    public static EntityTableModel getInstance()
    {
    	return uniqueInstance;
    }

    public String getColumnName(int column) {
        return columnNames[column];
    }

    public Object getValueAt(int row, int column) {
    	Object o = null;
        if(column == 0)
        	return dataVector.get(row);
        else {
			try {
				o = ent.getFromMethod(
					"get" + 
					dataVector.get(row).toString().substring(0,1).toUpperCase() + 
					dataVector.get(row).toString().substring(1), ent	
				);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			return o;
        }
    }

    public void setValueAt(Object value, int row, int column) {
        if(column == 0)
        	System.out.print("You can't set that dumbass\n");
        else if(column == 1) {
			try {
				ent.setFromMethod(
					"set" + 
					dataVector.get(row).toString().substring(0,1).toUpperCase() + 
					dataVector.get(row).toString().substring(1),
					ent,
					value
				);
				ent.updateAll();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
        } else {
        	System.out.print("EntityTable value error(row not found)\n");
        }
    	/*
    	 if(column == 0)
        	System.out.print("You can't set that dumbass\n");
        else if(column == 1) {
        	if(dataVector.get(row).equals("name"))
        		ent.setName((String)value);
        	else if(dataVector.get(row).equals("type"))
	        	ent.setType((String)value);
	        else if(dataVector.get(row).equals("positionX"))
	        	ent.setPositionX(Float.valueOf(value.toString()));
	    	else if(dataVector.get(row).equals("positionY"))
	    		ent.setPositionY(Float.valueOf(value.toString()));
			else if(dataVector.get(row).equals("positionZ"))
				ent.setPositionZ(Float.valueOf(value.toString()));
			else if(dataVector.get(row).equals("gravityX"))
	        	ent.setGravityX(Float.valueOf(value.toString()));
	    	else if(dataVector.get(row).equals("gravityY"))
	    		ent.setGravityY(Float.valueOf(value.toString()));
			else if(dataVector.get(row).equals("gravityZ"))
				ent.setGravityZ(Float.valueOf(value.toString()));
			else if(dataVector.get(row).equals("mass"))
				ent.setMass(Float.valueOf(value.toString()));
			else if(dataVector.get(row).equals("transparent"))
				ent.setTransparent(Boolean.valueOf(value.toString()));
			else if(dataVector.get(row).equals("alpha"))
				ent.setAlpha(Float.valueOf(value.toString()));
			else if(dataVector.get(row).equals("texture_name"))
				ent.setTexture_name((String)value);
			else if(dataVector.get(row).equals("collidable"))		
				ent.setCollidable(Boolean.valueOf(value.toString()));
			else if(dataVector.get(row).equals("size"))
				ent.setSize(Float.valueOf(value.toString()));
			else if(dataVector.get(row).equals("ttl"))
				ent.setTTL(Integer.valueOf(value.toString()));
			else {
				System.out.print("EntityTable value error(row not found)\n");
			}
			*/
        fireTableCellUpdated(row, column);
        EntityList.getInstance().setListChanged(true);
    }

    public int getRowCount() {
        return dataVector.size();
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    
    public void setEntity(Entity _entity) {
    	ent =_entity;
    	dataVector.clear();
    	for(String key: Entity.getKeys()) {
    		dataVector.add(key);
    	}
    	fireTableRowsInserted(
			0,
			dataVector.size()-1
    	);
    }

    public boolean isCellEditable(int row, int column) {
        if (column == VALUE) 
        	return true;
        else {
        	System.out.print("You can't edit that dumbass\n");
        	return false;
        }
    }

    
	@Override
	public void update(String s) {
		if(s != null)
			setEntity( EntityList.getInstance().getByName(s));
	}
}

