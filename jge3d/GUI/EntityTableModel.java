package jge3d.gui;

import java.lang.reflect.InvocationTargetException;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

import jge3d.EntityList;

public class EntityTableModel extends AbstractTableModel{ //implements Observer{
	private static final long serialVersionUID = 1L;
	public static EntityTableModel uniqueInstance= new EntityTableModel();
	public static final int KEY = 0;
    public static final int VALUE = 1;
    private static final String[] columnNames = {"Key", "Value"};
    protected Vector<Object> dataVector;

    private EntityTableModel() {
        dataVector = new Vector<Object>();
        //EntityComboBox.getInstance().registerObserver(this);
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
				//name, value, object
				o = EntityList.getInstance().getValue(
					EntityComboBox.getInstance().getValue(),
					dataVector.get(row).toString()					
				);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
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
				//name, value, object
				EntityList.getInstance().setValue(
					EntityComboBox.getInstance().getValue(),
					dataVector.get(row).toString(),
					value
				);
				//ent.updateAll();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
        } else {
        	System.out.print("EntityTable value error(row not found)\n");
        }
        fireTableCellUpdated(row, column);
        
        //Shouldn't the controller handle this???
        //EntityList.getInstance().setListChanged(true);
    }

    public int getRowCount() {
        return dataVector.size();
    }

    public int getColumnCount() {
        return columnNames.length;
    }
   
    /*
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
	*/
    
    public boolean isCellEditable(int row, int column) {
        if (column == VALUE) 
        	return true;
        else {
        	System.out.print("You can't edit that dumbass\n");
        	return false;
        }
    }

    
	//@Override
	//public void update(String s) {
	//	if(s != null)
	//		setEntity( EntityList.getInstance().getEntityByName(s));
	//}
}

