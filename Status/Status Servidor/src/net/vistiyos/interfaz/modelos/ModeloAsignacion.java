package net.vistiyos.interfaz.modelos;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class ModeloAsignacion extends AbstractTableModel {
	

	private static final long serialVersionUID = 8910472308558565743L;
	
	private ArrayList<String> columnsName;
	private ArrayList<ArrayList<Object>> data;
	
	public ModeloAsignacion(){
		columnsName = new ArrayList<String>();
		data = new ArrayList<ArrayList<Object>>();
	}

	@Override
	public int getColumnCount() {
		return columnsName.size();
	}

	@Override
	public int getRowCount() {
		return data.size();
	}
	
	public String getColumnName(int col) {
	        return columnsName.get(col);
	}

	@Override
	public Object getValueAt(int row, int col) {
		Object objeto = data.get(row).get(col);
		return objeto;
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 3) {
            return Boolean.class;
        } else {
            return super.getColumnClass(columnIndex);
        }
    }
	
	public void addColumn(String name){
		columnsName.add(name);
	}
	
	@Override
	public boolean isCellEditable(int row, int col) {
		if (col == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	@Override
	public void setValueAt(Object value, int row, int col) {
		if(col == 3){
			String barra = String.valueOf(data.get(row).get(2));
			for(int i = 0 ; i < row; i++){
				if(barra.equals(String.valueOf(data.get(i).get(2)))){
					data.get(i).set(3, new Boolean(false));
				}
			}
			for(int i = row+1 ; i < data.size() ; i++){
				if(barra.equals(String.valueOf(data.get(i).get(2)))){
					data.get(i).set(3, new Boolean(false));
				}
			}
		}
		data.get(row).set(col, value);
		fireTableDataChanged();
	}
	
	
	public void addRow(Object [] datos){
		ArrayList<Object> auxiliar = new ArrayList<Object>();
		for(Object dato : datos){
			auxiliar.add(dato);
		}
		data.add(auxiliar);
	}
	
	public ArrayList<ArrayList<Object>> getData(){
		return this.data;
	}

}
