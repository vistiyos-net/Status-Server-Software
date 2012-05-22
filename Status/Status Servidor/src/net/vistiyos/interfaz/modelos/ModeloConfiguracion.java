package net.vistiyos.interfaz.modelos;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class ModeloConfiguracion extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private ArrayList<String> columnsName;
	private ArrayList<ArrayList<Object>> data;
	
	public ModeloConfiguracion(){
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
		return super.getColumnClass(columnIndex);
	}

	public void addColumn(String name){
		columnsName.add(name);
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		if(col == 0){
			return false;
		}
		return true;
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
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
