package net.vistiyos.interfaz;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.AbstractCellEditor;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

import net.vistiyos.db.MySQL;
import net.vistiyos.interfaz.modelos.ModeloAsignacion;
import net.vistiyos.interfaz.modelos.ModeloConfiguracion;
import net.vistiyos.interfaz.modelos.ModeloEntradas;
import net.vistiyos.util.PDF;

public class Contenido extends JPanel implements KeyListener,ActionListener, ItemListener{

	private static final long serialVersionUID = 1L;
	private Ventana padre;
	private Map<String,JPanel> contenido;
	private JPanel inicio;
	private Font displayFont;
	private Font titleFont;
	private int idturno;
	
	Contenido(Ventana padre) throws SQLException{
		this.padre = padre;
		init();
		initInicio();
		initContenido();
		setContenido("inicio");
	}
	
	private void init(){
		contenido = new HashMap<String,JPanel>();
		displayFont = new Font("Serif", Font.BOLD, 18);
        titleFont = new Font("Serif", Font.BOLD, 25);
	}
	
	private void initInicio(){
		inicio = new JPanel();
		inicio.setLayout(new GridBagLayout());
		GridBagConstraints estilo = new GridBagConstraints();
		estilo.gridx = 0;
        estilo.gridy = 0;
        estilo.fill = GridBagConstraints.BOTH;
        estilo.gridwidth = 1;
        estilo.gridheight = 1;
        estilo.weightx = 1.0;
        estilo.weighty = 1.0;
		JLabel inicio = new JLabel("Seleccione una opción");
		inicio.setFont(titleFont);
		this.inicio.add(inicio,estilo);
	}
	
	private void initAltaProductos(){
		JPanel panel  = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		GridBagConstraints estilo = new GridBagConstraints();
		estilo.gridx = 0;
        estilo.gridy = 0;
        estilo.fill = GridBagConstraints.BOTH;
        estilo.gridwidth = 3;
        estilo.gridheight = 1;
        estilo.weightx = 1.0;
        estilo.weighty = 1.0;
        estilo.insets = new Insets(15,0,15,0);
        
        JLabel inicio = new JLabel("Rellene el formulario para dar de alta un nuevo producto");
        inicio.setFont(titleFont);
        panel.add(inicio,estilo);
        
        estilo.gridwidth = 1;
        estilo.gridx = 0;
        estilo.gridy = 1;
		JLabel ean13L = new JLabel("Código de Barras:");
		ean13L.setFont(displayFont);
		panel.add(ean13L,estilo);
		
		JTextField ean13T = new JTextField();
		ean13T.setName("F-EAN13");
		ean13T.requestFocus();
		ean13T.setFont(displayFont);
		ean13T.addKeyListener(this);
		estilo.gridx = 1;
		estilo.ipadx = 400;
		estilo.gridwidth = 3;
		panel.add(ean13T,estilo);
		
		JLabel nombreL = new JLabel("Nombre del Producto:");
		estilo.gridwidth = 1;
        estilo.gridx = 0;
        estilo.gridy = 2;
        estilo.ipadx = 0;
        nombreL.setFont(displayFont);
        panel.add(nombreL,estilo);
        
        JTextField nombreT = new JTextField();
        nombreT.setEnabled(false);
		nombreT.setName("NOMBRE");
		nombreT.setFont(displayFont);
		nombreT.addKeyListener(this);
		estilo.gridx = 1;
		estilo.ipadx = 400;
		estilo.gridwidth = 3;
		panel.add(nombreT,estilo);
        
		estilo.gridx = 0;
		estilo.gridy++;
		estilo.ipadx = 0;
		estilo.gridwidth = 1;
		JLabel cantidad = new JLabel("Cantidad:");
		cantidad.setFont(displayFont);
		panel.add(cantidad,estilo);
		estilo.gridx = 1;
		ArrayList<Entry<Integer,Float>> cantidades = MySQL.getCantidades();
		ButtonGroup grupoBotonesOpcion = new ButtonGroup();
		JRadioButton[] cantidadesR = new JRadioButton[cantidades.size()];
		for(int i = 0 ; i < cantidades.size() ; i++){
			cantidadesR[i] = new JRadioButton(cantidades.get(i).getValue().toString()+" L");
			cantidadesR[i].setName("CANT-"+cantidades.get(i).getKey().toString());
			cantidadesR[i].setSelected(true);
			cantidadesR[i].setEnabled(false);
			grupoBotonesOpcion.add(cantidadesR[i]);
			estilo.insets = new Insets(0,0,0,0);
			panel.add(cantidadesR[i],estilo);
			estilo.gridx++;
		}
		
		JButton guardar = new JButton("Guardar");
		guardar.setFont(displayFont);
		guardar.setName("alta-de-productos");
		guardar.addActionListener(this);
		guardar.setEnabled(false);
		estilo.gridx = 1;
		estilo.gridy++;
		estilo.gridwidth = 2;
		panel.add(guardar,estilo);
		
		this.contenido.put("alta-de-productos", panel);
	}
	
	private void initAltaEmpleados(){
		JPanel panel  = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		GridBagConstraints estilo = new GridBagConstraints();
		estilo.gridx = 0;
        estilo.gridy = 0;
        estilo.fill = GridBagConstraints.BOTH;
        estilo.gridwidth = 3;
        estilo.gridheight = 1;
        estilo.weightx = 1.0;
        estilo.weighty = 1.0;
        estilo.insets = new Insets(15,0,15,0);
        
        JLabel inicio = new JLabel("Rellene el formulario para dar de alta un nuevo empleado");
        inicio.setFont(titleFont);
        panel.add(inicio,estilo);
        
        estilo.gridwidth = 1;
        estilo.gridx = 0;
        estilo.gridy = 1;
		JLabel DNIL = new JLabel("DNI:");
		DNIL.setFont(displayFont);
		panel.add(DNIL,estilo);
		
		JTextField DNIT = new JTextField();
		DNIT.setName("DNI");
		DNIT.requestFocus();
		DNIT.setFont(displayFont);
		estilo.gridx = 1;
		estilo.ipadx = 400;
		estilo.gridwidth = 3;
		panel.add(DNIT,estilo);
		
		JLabel nombreL = new JLabel("Nombre:");
		estilo.gridwidth = 1;
        estilo.gridx = 0;
        estilo.gridy = 2;
        estilo.ipadx = 0;
        nombreL.setFont(displayFont);
        panel.add(nombreL,estilo);
        
        JTextField nombreT = new JTextField();
		nombreT.setName("NOMBRE");
		nombreT.setFont(displayFont);
		estilo.gridx = 1;
		estilo.ipadx = 400;
		estilo.gridwidth = 3;
		panel.add(nombreT,estilo);
        
		estilo.gridx = 0;
		estilo.gridy++;
		estilo.ipadx = 0;
		estilo.gridwidth = 1;
		JLabel telefonoL = new JLabel("Telefono:");
		telefonoL.setFont(displayFont);
		panel.add(telefonoL,estilo);
		estilo.gridx = 1;
		
		
		JTextField telefonoT = new JTextField();
		telefonoT.setFont(displayFont);
		telefonoT.setName("TELEFONO");
		panel.add(telefonoT,estilo);
		
		
		JButton guardar = new JButton("Guardar");
		guardar.setFont(displayFont);
		guardar.setName("alta-de-empleados");
		guardar.addActionListener(this);
		estilo.gridx = 0;
		estilo.gridy++;
		estilo.gridwidth = 2;
		panel.add(guardar,estilo);
		
		this.contenido.put("alta-de-empleados", panel);
	}
	
	private void initGestionStock(){
		JPanel panel  = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		GridBagConstraints estilo = new GridBagConstraints();
		estilo.gridx = 0;
        estilo.gridy = 0;
        estilo.fill = GridBagConstraints.BOTH;
        estilo.gridwidth = 3;
        estilo.gridheight = 1;
        estilo.weightx = 1.0;
        estilo.weighty = 1.0;
        estilo.insets = new Insets(15,0,15,0);
        
        JLabel inicio = new JLabel("Pase el código de barras para aumentar el stock");
        inicio.setFont(titleFont);
        panel.add(inicio,estilo);
        
        estilo.gridwidth = 1;
        estilo.gridx = 0;
        estilo.gridy = 1;
		JLabel ean13L = new JLabel("Código de Barras:");
		ean13L.setFont(displayFont);
		panel.add(ean13L,estilo);
		
		JTextField ean13T = new JTextField();
		ean13T.setName("S-EAN13");
		ean13T.requestFocus();
		ean13T.setFont(displayFont);
		ean13T.addKeyListener(this);
		estilo.gridx = 1;
		estilo.ipadx = 400;
		estilo.gridwidth = 3;
		panel.add(ean13T,estilo);
		
		JLabel nombreL = new JLabel("Nombre del Producto:");
		estilo.gridwidth = 1;
        estilo.gridx = 0;
        estilo.gridy = 2;
        estilo.ipadx = 0;
        nombreL.setFont(displayFont);
        panel.add(nombreL,estilo);
        
        JTextField nombreT = new JTextField();
        nombreT.setEnabled(false);
		nombreT.setName("NOMBRE");
		nombreT.setFont(displayFont);
		nombreT.addKeyListener(this);
		estilo.gridx = 1;
		estilo.ipadx = 400;
		estilo.gridwidth = 3;
		panel.add(nombreT,estilo);
        
		estilo.gridx = 0;
		estilo.gridy++;
		estilo.ipadx = 0;
		estilo.gridwidth = 1;
		JLabel cantidad = new JLabel("Cantidad:");
		cantidad.setFont(displayFont);
		panel.add(cantidad,estilo);
		estilo.gridx = 1;
		ArrayList<Entry<Integer,Float>> cantidades = MySQL.getCantidades();
		ButtonGroup grupoBotonesOpcion = new ButtonGroup();
		JRadioButton[] cantidadesR = new JRadioButton[cantidades.size()];
		for(int i = 0 ; i < cantidades.size() ; i++){
			cantidadesR[i] = new JRadioButton(cantidades.get(i).getValue().toString()+" L");
			cantidadesR[i].setName("CANT-"+cantidades.get(i).getKey().toString());
			cantidadesR[i].setEnabled(false);
			grupoBotonesOpcion.add(cantidadesR[i]);
			estilo.insets = new Insets(0,0,0,0);
			panel.add(cantidadesR[i],estilo);
			estilo.gridx++;
		}
		
		JButton guardar = new JButton("Confirmar");
		guardar.setFont(displayFont);
		guardar.setName("gestionar-stock");
		guardar.setEnabled(false);
		guardar.addActionListener(this);
		estilo.gridx = 1;
		estilo.gridy++;
		estilo.gridwidth = 2;
		panel.add(guardar,estilo);
		
		this.contenido.put("gestionar-stock", panel);
	}
	
	private void initCorreccionStock(){
		JPanel panel  = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		GridBagConstraints estilo = new GridBagConstraints();
		estilo.gridx = 0;
        estilo.gridy = 0;
        estilo.fill = GridBagConstraints.BOTH;
        estilo.gridwidth = 1;
        estilo.gridheight = 1;
        estilo.weightx = 1.0;
        estilo.weighty = 1.0;
        estilo.insets = new Insets(15,0,15,0);
        
        JLabel inicio = new JLabel("Edite el stock de los productos dados de alta");
        inicio.setFont(titleFont);
        panel.add(inicio,estilo);
        ArrayList<Map<String,String>> datos = MySQL.getProductos();
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("EAN13");
        modelo.addColumn("Nombre");
        modelo.addColumn("Capacidad");
        modelo.addColumn("Stock");
        JTable tabla = new JTable(){
        	
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int rowIndex, int colIndex) {
        		  if(colIndex == 1 || colIndex == 2 || colIndex == 3){
        			  return true;
        		  }
        		  else{
        			  return false;
        		  }
        	}
        };

        final class SpinnerEditor extends AbstractCellEditor implements TableCellEditor {

			private static final long serialVersionUID = 1L;
			final JComboBox<String> spinner = new JComboBox<String>();
			
        	public SpinnerEditor() {
        		ArrayList<Entry<Integer,Float>> datos = MySQL.getCantidades();
        		Iterator<Entry<Integer,Float>> it = datos.iterator();
        		while(it.hasNext()){
        			Entry<Integer,Float> entrada = it.next();
        			spinner.addItem(entrada.getValue().toString()+" L");
        		}
        	}
        	
        	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        		return spinner;
        	}

        	public boolean isCellEditable(EventObject evt) {
        		if (evt instanceof MouseEvent) {
        			return ((MouseEvent)evt).getClickCount() >= 2;
        		}
        		return true;
        	}

        	public Object getCellEditorValue() {
        		return spinner.getSelectedItem();
        	}
        }
        
        Iterator<Map<String,String>> it = datos.iterator();
        while(it.hasNext()){
        	Map<String,String> row = it.next();
        	Object[] fila = new Object[4];
        	fila[0] = row.get("ean13");
        	fila[1] = row.get("nombre");
        	fila[2] = row.get("capacidad") + " L";
        	fila[3] = row.get("cantidad");
        	modelo.addRow(fila);
        }
        tabla.setModel(modelo);
        TableColumn col = tabla.getColumnModel().getColumn(2);
        col.setCellEditor(new SpinnerEditor());
        estilo.gridy++;
		estilo.gridwidth = 3;
		panel.add(new JScrollPane(tabla),estilo);
		
		JButton guardar = new JButton("Modificar");
		guardar.setFont(displayFont);
		guardar.setName("correción-de-stock");
		guardar.addActionListener(this);
		estilo.gridy++;
		estilo.gridwidth = 1;
		estilo.ipady = 0;
		panel.add(guardar,estilo);
		
		this.contenido.put("correción-de-stock", panel);
	}
	
	private void initDatosEmpleados(){
		JPanel panel  = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		GridBagConstraints estilo = new GridBagConstraints();
		estilo.gridx = 0;
        estilo.gridy = 0;
        estilo.fill = GridBagConstraints.BOTH;
        estilo.gridwidth = 1;
        estilo.gridheight = 1;
        estilo.weightx = 1.0;
        estilo.weighty = 1.0;
        estilo.insets = new Insets(15,0,15,0);
        
        JLabel inicio = new JLabel("Edite los datos de los empleados");
        inicio.setFont(titleFont);
        panel.add(inicio,estilo);
        ArrayList<Map<String,String>> datos = MySQL.getEmpleados();
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("DNI");
        modelo.addColumn("NOMBRE");
        modelo.addColumn("TELEFONO");
        JTable tabla = new JTable(){

			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int rowIndex, int colIndex) {
        		  if(colIndex == 1 || colIndex == 2 || colIndex == 3){
        			  return true;
        		  }
        		  else{
        			  return false;
        		  }
        	}
        };
        
        Iterator<Map<String,String>> it = datos.iterator();
        while(it.hasNext()){
        	Map<String,String> row = it.next();
        	Object[] fila = new Object[4];
        	fila[0] = row.get("idcamarero");
        	fila[1] = row.get("dni");
        	fila[2] = row.get("nombre");
        	fila[3] = row.get("telefono");
        	modelo.addRow(fila);
        }
        tabla.setModel(modelo);
        estilo.gridy++;
		estilo.gridwidth = 3;
		panel.add(new JScrollPane(tabla),estilo);
		
		JButton guardar = new JButton("Modificar");
		guardar.setFont(displayFont);
		guardar.setName("editar-datos-de-empleados");
		guardar.addActionListener(this);
		estilo.gridy++;
		estilo.gridwidth = 1;
		estilo.ipady = 0;
		panel.add(guardar,estilo);
		
		this.contenido.put("editar-datos-de-empleados", panel);
	}
	
	private void initAsignaEmpleados(){
		JPanel panel  = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		GridBagConstraints estilo = new GridBagConstraints();
		estilo.gridx = 0;
        estilo.gridy = 0;
        estilo.fill = GridBagConstraints.BOTH;
        estilo.gridwidth = 3;
        estilo.gridheight = 1;
        estilo.weightx = 1.0;
        estilo.weighty = 1.0;
        estilo.insets = new Insets(15,0,15,0);
        
        JLabel inicio = new JLabel("Asigne los empleados a sus puestos");
        inicio.setFont(titleFont);
        panel.add(inicio,estilo);
        ModeloAsignacion modelo = new ModeloAsignacion();
        modelo.addColumn("NOMBRE");
        modelo.addColumn("SALARIO");
        modelo.addColumn("BARRA");
        modelo.addColumn("ENCARGADO");
        modelo.addColumn("OBSERVACIONES");
        JTable tabla = new JTable();
        ArrayList<Entry<Integer,String>> datosBarras = MySQL.getBarras();
        JComboBox<String> listaBarras = new JComboBox<String>();
        Iterator<Entry<Integer,String>> it2 = datosBarras.iterator();
        while(it2.hasNext()){
        	Entry<Integer,String> entrada = it2.next();
        	listaBarras.addItem(entrada.getValue());
        }
             
        ArrayList<Map<String,String>> datos = MySQL.getEmpleados();
        Iterator<Map<String,String>> it = datos.iterator();
        while(it.hasNext()){
        	Map<String,String> row = it.next();
        	Map<String,String> asignacion = MySQL.getAsignacion(row.get("idcamarero"));
        	Object[] fila = new Object[5];
        	String barra = "";
        	fila[0] = row.get("nombre");
        	fila[1] = asignacion.get("salario");
        	it2 = datosBarras.iterator();
        	while(it2.hasNext()){
        		Entry<Integer,String> entrada = it2.next();
        		if(entrada.getKey().toString().equals(asignacion.get("idbarra"))){
        			barra = entrada.getValue();
        		}
        	}
        	fila[2] = barra;
        	int encargado = Integer.parseInt(asignacion.get("encargado"));
        	if(encargado == 0){
        		fila[3] = new Boolean(false);
        	}
        	else{
        		fila[3] = new Boolean(true);
        	}
        	fila[4] = asignacion.get("observaciones");
        	modelo.addRow(fila);
        }
        tabla.setModel(modelo);
        TableColumn barrasColumn = tabla.getColumnModel().getColumn(2);
        barrasColumn.setCellEditor(new DefaultCellEditor(listaBarras));
        estilo.gridy++;
		estilo.gridwidth = 3;
		panel.add(new JScrollPane(tabla),estilo);

		JButton guardar = new JButton("Guardar");
		guardar.setFont(displayFont);
		guardar.setName("asignación-de-empleados");
		guardar.addActionListener(this);
		estilo.gridx = 0;
		estilo.gridy++;
		estilo.gridwidth = 1;
		panel.add(guardar,estilo);
		
		JButton informe = new JButton("Obtener Informe");
		informe.setFont(displayFont);
		informe.setName("informe-de-empleados");
		informe.addActionListener(this);
		estilo.gridx++;
		panel.add(informe,estilo);
		this.contenido.put("asignación-de-empleados", panel);
	}
	
	private void initGestionPreciosEntrada(){
		JPanel panel  = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		GridBagConstraints estilo = new GridBagConstraints();
		estilo.gridx = 0;
        estilo.gridy = 0;
        estilo.fill = GridBagConstraints.BOTH;
        estilo.gridwidth = 3;
        estilo.gridheight = 1;
        estilo.weightx = 1.0;
        estilo.weighty = 1.0;
        estilo.insets = new Insets(15,0,15,0);
        
        JLabel inicio = new JLabel("Seleccione un turno para editar");
        inicio.setFont(titleFont);
        panel.add(inicio,estilo);
        
        estilo.gridy++;
        estilo.gridy++;
        estilo.gridx = 1;
        estilo.gridwidth = 1;
        JComboBox<String> turnos = new JComboBox<String>();
        turnos.addItem(" Seleccione un turno ");
        Iterator<String> it = MySQL.getTurnos().iterator();
        while(it.hasNext()){
        	turnos.addItem(it.next());
        }
        panel.add(turnos,estilo);
        turnos.addItemListener(this);
        turnos.setSelectedIndex(0);
        ModeloEntradas modelo = new ModeloEntradas();
        modelo.addColumn("NOMBRE");
        modelo.addColumn("BEBIDA");
        modelo.addColumn("PRECIO");
        JTable tabla = new JTable();
        ArrayList<String> datosBebidas = MySQL.getBebidas();
        JComboBox<String> listaBebidas = new JComboBox<String>();
        Iterator<String> it2 = datosBebidas.iterator();
        while(it2.hasNext()){
        	listaBebidas.addItem(it2.next());
        }
        tabla.setModel(modelo);
        TableColumn barrasColumn = tabla.getColumnModel().getColumn(1);
        barrasColumn.setCellEditor(new DefaultCellEditor(listaBebidas));
        estilo.gridy++;
        estilo.gridx = 0;
		estilo.gridwidth = 3;
		panel.add(new JScrollPane(tabla),estilo);

		JButton guardar = new JButton("Guardar");
		guardar.setFont(displayFont);
		guardar.setName("gestión-de-precios-entrada");
		guardar.addActionListener(this);
		estilo.gridx = 1;
		estilo.gridy++;
		estilo.gridwidth = 1;
		panel.add(guardar,estilo);

		this.contenido.put("gestión-de-precios-entrada", panel);
	}
	
	private void initActivarTurno(){
		JPanel panel  = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		GridBagConstraints estilo = new GridBagConstraints();
		ArrayList<String> miturnos = MySQL.getTurnos();
		int columnas = (int)Math.sqrt(miturnos.size())+1;
		estilo.gridx = 0;
        estilo.gridy = 0;
        estilo.fill = GridBagConstraints.BOTH;
        estilo.gridwidth = columnas; 
        estilo.gridheight = 1;
        estilo.weightx = 1.0;
        estilo.weighty = 1.0;
        estilo.insets = new Insets(15,15,15,15);
        estilo.ipadx = 100;
        estilo.ipady = 100;
        
        JLabel inicio = new JLabel("Seleccione el turno a activar:");
        inicio.setFont(titleFont);
        panel.add(inicio,estilo);
        
        estilo.gridy++;
        estilo.gridx = 0;
        estilo.gridwidth = 1;
        
        Iterator<String> it = miturnos.iterator();
        while(it.hasNext()){
        	JButton boton = new JButton(it.next());
        	boton.setName("activar-turno");
        	boton.addActionListener(this);
        	panel.add(boton, estilo);
        	estilo.gridx++;
        	if(estilo.gridx == columnas){
        		estilo.gridx = 0;
        		estilo.gridy++;
        	}
        }
		this.contenido.put("activar-turno", panel);
	}
	
	private void initGestorConfiguracion(){
		JPanel panel  = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		GridBagConstraints estilo = new GridBagConstraints();
		estilo.gridx = 0;
        estilo.gridy = 0;
        estilo.fill = GridBagConstraints.BOTH;
        estilo.gridwidth = 3;
        estilo.gridheight = 1;
        estilo.weightx = 1.0;
        estilo.weighty = 1.0;
        estilo.insets = new Insets(15,0,15,0);
        
        JLabel inicio = new JLabel("Modifique las opciones de Configuración");
        inicio.setFont(titleFont);
        panel.add(inicio,estilo);
        
        ModeloConfiguracion modelo = new ModeloConfiguracion();
        modelo.addColumn("PARAMETRO");
        modelo.addColumn("VALOR");
        JTable tabla = new JTable();
        ArrayList<Entry<String,String>> datosConfiguracion = MySQL.getConfiguracion();
        Iterator<Entry<String,String>> it2 = datosConfiguracion.iterator();
        while(it2.hasNext()){
        	Entry<String,String> entrada = it2.next();
        	Object [] array = new Object[2];
        	array[0] = entrada.getKey();
        	array[1] = entrada.getValue();
        	modelo.addRow(array);
        }
        tabla.setModel(modelo);
        estilo.gridy++;
        estilo.gridx = 0;
		estilo.gridwidth = 3;
		panel.add(new JScrollPane(tabla),estilo);

		JButton guardar = new JButton("Guardar");
		guardar.setFont(displayFont);
		guardar.setName("gestor-de-configuración");
		guardar.addActionListener(this);
		estilo.gridx = 1;
		estilo.gridy++;
		estilo.gridwidth = 1;
		panel.add(guardar,estilo);

		this.contenido.put("gestor-de-configuración", panel);
	}
	
	
	public void setContenido(String id){
		this.removeAll();
		if(id.equals("inicio")){
			this.add(inicio);
		}
		else{
			initContenido();
			this.add(contenido.get(id));
			JPanel panel = contenido.get(id);
			for(int i = 0 ; i < panel.getComponentCount() ; i++){
				if(panel.getComponent(i).getName() != null && panel.getComponent(i).getName().startsWith("F-")){
					panel.getComponent(i).requestFocus();
				}
			}
		}
		padre.revalidate();
	}

	@Override
	public void keyPressed(KeyEvent event) {
	}

	@Override
	public void keyReleased(KeyEvent event) {
		JTextField source = (JTextField) event.getSource();
		if(source.getName().equals("F-EAN13")){
			if(source.getText().length() == 13){ //Ya se ha introducido toda la cadena
				//Mostrar pantalla de comprobación de datos.
				if(MySQL.existeArticulo(source.getText()).equals("")){
					JPanel panel = (JPanel) source.getParent();
					for(int i = 0 ; i < panel.getComponentCount() ; i++){
						if(panel.getComponent(i).getName() != null && panel.getComponent(i).getName().equals("NOMBRE")){
							((JTextField)panel.getComponent(i)).setEnabled(true);
						}
						else if(panel.getComponent(i).getName() != null && panel.getComponent(i).getName().startsWith("CANT-")){
							((JRadioButton) panel.getComponent(i)).setEnabled(true);
						}
						else if(panel.getComponent(i).getName() != null && panel.getComponent(i).getName().equals("alta-de-productos")){
							((JButton)panel.getComponent(i)).setEnabled(true);
						}
					}
				}
				else{
					System.out.println("Existe");
				}
			}
			else{
				JPanel panel = (JPanel) source.getParent();
				for(int i = 0 ; i < panel.getComponentCount() ; i++){
					if(panel.getComponent(i).getName() != null && panel.getComponent(i).getName().equals("NOMBRE")){
						((JTextField)panel.getComponent(i)).setText("");
					}
					else if(panel.getComponent(i).getName() != null && panel.getComponent(i).getName().startsWith("CANT-")){
						((JRadioButton) panel.getComponent(i)).setSelected(false);
					}
					else if(panel.getComponent(i).getName() != null && panel.getComponent(i).getName().equals("alta-de-productos")){
						((JButton)panel.getComponent(i)).setEnabled(false);
					}
				}
			}
		}
		else if(source.getName().equals("S-EAN13")){
			if(source.getText().length() == 13){
				try {
					Map<String,String> datos = MySQL.getDatosArticulo(source.getText());
					if(!datos.isEmpty()){
						JPanel panel = (JPanel) source.getParent();
						for(int i = 0 ; i < panel.getComponentCount() ; i++){
							if(panel.getComponent(i).getName() != null && panel.getComponent(i).getName().equals("NOMBRE")){
								((JTextField)panel.getComponent(i)).setText(datos.get("nombre"));
							}
							else if(panel.getComponent(i).getName() != null && panel.getComponent(i).getName().startsWith("CANT-")){
								String[] cantidades = panel.getComponent(i).getName().split("-");
								if(cantidades[1].equals(datos.get("idcapacidad"))){
									((JRadioButton) panel.getComponent(i)).setSelected(true);
								}
							}
							else if(panel.getComponent(i).getName() != null && panel.getComponent(i).getName().equals("gestionar-stock")){
								((JButton)panel.getComponent(i)).setEnabled(true);
							}
						}
					}
					else{
						System.out.println("No Existe");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			else{
				JPanel panel = (JPanel) source.getParent();
				for(int i = 0 ; i < panel.getComponentCount() ; i++){
					if(panel.getComponent(i).getName() != null && panel.getComponent(i).getName().equals("NOMBRE")){
						((JTextField)panel.getComponent(i)).setText("");
					}
					else if(panel.getComponent(i).getName() != null && panel.getComponent(i).getName().startsWith("CANT-")){
						((JRadioButton) panel.getComponent(i)).setSelected(false);
					}
					else if(panel.getComponent(i).getName() != null && panel.getComponent(i).getName().equals("gestionar-stock")){
						((JButton)panel.getComponent(i)).setEnabled(false);
					}
				}
			}
		}	
	}

	@Override
	public void keyTyped(KeyEvent event) {	
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		JButton accion = (JButton) event.getSource();
		if(accion.getName().equals("alta-de-productos")){
			JPanel panel = (JPanel) accion.getParent();
			String ean13 = "";
			String nombre = "";
			int idcapacidad = -1;
			for(int i = 0; i < panel.getComponentCount(); i++){
				Component componente = panel.getComponent(i);
				if(componente.getName() != null && componente.getName().equals("F-EAN13")){
					JTextField ean13T = (JTextField) componente;
					ean13 = ean13T.getText();
					ean13T.setText("");
					ean13T.requestFocus();
				}
				else if(componente.getName() != null && componente.getName().equals("NOMBRE")){
					JTextField nombreT = (JTextField) componente;
					nombre = nombreT.getText();
					nombreT.setText("");
					nombreT.setEnabled(false);
				}
				else if(componente.getName() != null && componente.getName().startsWith("CANT-")){
					JRadioButton boton = (JRadioButton) componente;
					if(boton.isSelected()){
						String[] datos = boton.getName().split("-");
						idcapacidad = Integer.parseInt(datos[1]);
						boton.setSelected(false);
					}
					boton.setEnabled(false);
				}
			}
			MySQL.introducirArticulo(nombre, ean13, idcapacidad);
			new VentanaInfo(this.padre,nombre+" se ha insertado correctamente",VentanaInfo.INFO);
		}
		else if(accion.getName().equals("gestionar-stock")){
			JPanel panel = (JPanel) accion.getParent();
			String ean13 = "";
			for(int i = 0; i < panel.getComponentCount(); i++){
				if(panel.getComponent(i).getName() != null && panel.getComponent(i).getName().equals("S-EAN13")){
					ean13 = ((JTextField) panel.getComponent(i)).getText();
					((JTextField)panel.getComponent(i)).requestFocus();
				}
			}
			MySQL.aumentarStock(ean13);
			panel = (JPanel) accion.getParent();
			for(int i = 0; i < panel.getComponentCount(); i++){
				if(panel.getComponent(i).getClass().toString().contains("JTextField")){
					((JTextField) panel.getComponent(i)).setText("");
				}
				else if(panel.getComponent(i).getClass().toString().contains("JTextField")){
					((JRadioButton) panel.getComponent(i)).setSelected(false);
				}
			}
			new VentanaInfo(this.padre,"Se ha actualizado el stock correctamente",VentanaInfo.INFO);
		}
		else if(accion.getName().equals("correción-de-stock")){
			JPanel panel = (JPanel) accion.getParent();
			for(int i = 0; i < panel.getComponentCount(); i++){
				if(panel.getComponent(i).getClass().toString().contains("JScrollPane")){
					JScrollPane pane = (JScrollPane) panel.getComponent(i);
					JViewport puerto = (JViewport) pane.getComponent(3);
					JTableHeader cabecera = (JTableHeader) puerto.getComponent(0);
					JTable tabla = cabecera.getTable();
					DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
					for(int j = 0 ; j < modelo.getRowCount() ; j++){
						String ean13 = "";
						String nombre = "";
						String capacidad = "";
						String stock = "";
						for( int k = 0 ; k < modelo.getColumnCount() ; k++){
							if(modelo.getColumnName(k).equals("EAN13")){
								ean13 = modelo.getValueAt(j, k).toString();								
							}
							else if(modelo.getColumnName(k).equals("Nombre")){
								nombre = modelo.getValueAt(j, k).toString();
							}
							else if(modelo.getColumnName(k).equals("Capacidad")){
								capacidad = modelo.getValueAt(j, k).toString();
								String[] aux = capacidad.split(" ");
								capacidad = aux[0];
							}
							else if(modelo.getColumnName(k).equals("Stock")){
								stock = modelo.getValueAt(j, k).toString();
							}	
						}
						MySQL.actualizarProducto(ean13, nombre, capacidad, stock);
						new VentanaInfo(this.padre,nombre+" se ha actualizado",VentanaInfo.INFO);
					}
				}
			}
		}
		else if(accion.getName().equals("alta-de-empleados")){
			JPanel panel = (JPanel) accion.getParent();
			String dni = "";
			String nombre = "";
			String telefono = "";
			for(int i = 0; i < panel.getComponentCount(); i++){
				Component componente = panel.getComponent(i);
				if(componente.getName() != null && componente.getName().equals("DNI")){
					JTextField DNIT = (JTextField) componente;
					dni = DNIT.getText();
					DNIT.setText("");
					DNIT.requestFocus();
				}
				else if(componente.getName() != null && componente.getName().equals("NOMBRE")){
					JTextField nombreT = (JTextField) componente;
					nombre = nombreT.getText();
					nombreT.setText("");
					nombreT.setEnabled(false);
				}
				else if(componente.getName() != null && componente.getName().startsWith("TELEFONO")){
					JTextField telefonoT = (JTextField) componente;
					telefono = telefonoT.getText();
					telefonoT.setText("");
					telefonoT.setEnabled(false);
				}
			}
			MySQL.darDeAltaEmpleado(dni, nombre, telefono);
			new VentanaInfo(this.padre,nombre+ " se ha dado de alta correctamente",VentanaInfo.INFO);
		}
		else if(accion.getName().equals("asignación-de-empleados")){
			JPanel panel = (JPanel) accion.getParent();
			for(int i = 0; i < panel.getComponentCount(); i++){
				if(panel.getComponent(i).getClass().toString().contains("JScrollPane")){
					JScrollPane pane = (JScrollPane) panel.getComponent(i);
					JViewport puerto = (JViewport) pane.getComponent(3);
					JTableHeader cabecera = (JTableHeader) puerto.getComponent(0);
					JTable tabla = cabecera.getTable();
					ModeloAsignacion modelo = (ModeloAsignacion) tabla.getModel();
					ArrayList<ArrayList<Object>> data = modelo.getData();
					Iterator<ArrayList<Object>> it = data.iterator();
					boolean continuar = true;
					while(it.hasNext() && continuar){
						ArrayList<Object> row = it.next();
						String nombre = String.valueOf(row.get(0));
						float salario = 0;
						int encargado = 0;
						String barra = "";
						String observaciones = "";
						if(row.get(1) == null || row.get(1).equals("")){
							new VentanaInfo(this.padre,"Debe asignar un salario a "+nombre,VentanaInfo.ERROR);
							continuar = false;
						}
						else if(continuar && row.get(2) == null || row.get(2).equals("")){
							new VentanaInfo(this.padre,"Debe asignar Barra de Trabajo a "+nombre,VentanaInfo.ERROR);
							continuar = false;
						}
						if(continuar){
							salario = Float.parseFloat(String.valueOf(row.get(1)));
							barra = String.valueOf(row.get(2));
							if(row.get(3).toString().equals("true")){
								encargado = 1;
							}
							if(row.get(3) == null || row.get(4) == ""){
								observaciones = "";
							}
							else{
								observaciones = String.valueOf(row.get(4));
							}
							MySQL.insertarAsignacion(nombre, encargado, salario, observaciones, barra);
						}
					}
					new VentanaInfo(this.padre,"Asignación Almacenada Correctamente",VentanaInfo.INFO);
				}
			}
		}
		else if(accion.getName().equals("editar-datos-de-empleados")){
			JPanel panel = (JPanel) accion.getParent();
			for(int i = 0; i < panel.getComponentCount(); i++){
				if(panel.getComponent(i).getClass().toString().contains("JScrollPane")){
					JScrollPane pane = (JScrollPane) panel.getComponent(i);
					JViewport puerto = (JViewport) pane.getComponent(3);
					JTableHeader cabecera = (JTableHeader) puerto.getComponent(0);
					JTable tabla = cabecera.getTable();
					DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
					for(int j = 0 ; j < modelo.getRowCount() ; j++){
						String id = "";
						String dni = "";
						String nombre = "";
						String telefono = "";
						for( int k = 0 ; k < modelo.getColumnCount() ; k++){
							if(modelo.getColumnName(k).equals("ID")){
								id = modelo.getValueAt(j, k).toString();								
							}
							else if(modelo.getColumnName(k).equals("DNI")){
								dni = modelo.getValueAt(j, k).toString();
							}
							else if(modelo.getColumnName(k).equals("NOMBRE")){
								nombre = modelo.getValueAt(j, k).toString();
							}
							else if(modelo.getColumnName(k).equals("TELEFONO")){
								telefono = modelo.getValueAt(j, k).toString();
							}	
						}
						MySQL.actualizarEmpleado(id, dni, nombre, telefono);
						new VentanaInfo(this.padre,"Información Actualizada",VentanaInfo.INFO);
					}
				}
			}
		}
		else if(accion.getName().equals("informe-de-empleados")){
			JPanel panel = (JPanel) accion.getParent();
			for(int i = 0; i < panel.getComponentCount(); i++){
				if(panel.getComponent(i).getClass().toString().contains("JScrollPane")){
					JScrollPane pane = (JScrollPane) panel.getComponent(i);
					JViewport puerto = (JViewport) pane.getComponent(3);
					JTableHeader cabecera = (JTableHeader) puerto.getComponent(0);
					JTable tabla = cabecera.getTable();
					ModeloAsignacion modelo = (ModeloAsignacion) tabla.getModel();
					PDF.generarInforme(modelo.getData());
				}
			}
		}
		else if(accion.getName().equals("gestión-de-precios-entrada")){
			JPanel panel = (JPanel) accion.getParent();
			boolean fin = false;
			for(int i = 0 ; i < panel.getComponentCount() && !fin; i++){
				if(panel.getComponent(i).getClass().toString().contains("JScrollPane")){
					JScrollPane pane = (JScrollPane) panel.getComponent(i);
					JViewport puerto = (JViewport) pane.getComponent(3);
					JTableHeader cabecera = (JTableHeader) puerto.getComponent(0);
					JTable tabla = cabecera.getTable();
					ModeloEntradas modelo = (ModeloEntradas) tabla.getModel();
					ArrayList<ArrayList<Object>> data = modelo.getData();
					for(int j = 0 ; j < data.size()-1 ; j++){
						String nombre = String.valueOf(data.get(j).get(0));
						String bebida = String.valueOf(data.get(j).get(1));
						float precio =  Float.parseFloat(String.valueOf(data.get(j).get(2)).replace(",", "."));
						MySQL.insertarEntrada(nombre,bebida,precio,idturno);
					}
					fin = true;
					new VentanaInfo(this.padre,"Precios Actualizados", VentanaInfo.INFO);
				}
			}	
		}
		else if(accion.getName().equals("gestor-de-configuración")){
			JPanel panel = (JPanel) accion.getParent();
			boolean fin = false;
			for(int i = 0 ; i < panel.getComponentCount() && !fin; i++){
				if(panel.getComponent(i).getClass().toString().contains("JScrollPane")){
					JScrollPane pane = (JScrollPane) panel.getComponent(i);
					JViewport puerto = (JViewport) pane.getComponent(3);
					JTableHeader cabecera = (JTableHeader) puerto.getComponent(0);
					JTable tabla = cabecera.getTable();
					ModeloConfiguracion modelo = (ModeloConfiguracion) tabla.getModel();
					ArrayList<ArrayList<Object>> data = modelo.getData();
					for(int j = 0 ; j < data.size(); j++){
						String parametro = String.valueOf(data.get(j).get(0));
						String valor = String.valueOf(data.get(j).get(1));
						MySQL.actualizarConfiguracion(parametro,valor);
					}
					fin = true;
					new VentanaInfo(this.padre,"Configuración Actualizada", VentanaInfo.INFO);
				}
			}			
		}
		else if(accion.getName().equals("activar-turno")){
				MySQL.activarTurno(MySQL.getIdTurno(accion.getText()));
				new VentanaInfo(this.padre,"Turno Activado", VentanaInfo.INFO);
		}
	}
	
	private void initContenido(){
		initAltaProductos();
		initGestionStock();
		initCorreccionStock();
		initAltaEmpleados();
		initDatosEmpleados();
		initAsignaEmpleados();
		initGestionPreciosEntrada();
		initActivarTurno();
		initGestorConfiguracion();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void itemStateChanged(ItemEvent event) {
		String opcion = String.valueOf(event.getItem());
		if(!opcion.equals(" Seleccione un turno ")){
			JComboBox<String> lista = (JComboBox<String>) event.getSource();
			JPanel panel = (JPanel) lista.getParent();
			boolean fin = false;
			for(int i = 0 ; i < panel.getComponentCount() && !fin; i++){
				if(panel.getComponent(i).getClass().toString().contains("JScrollPane")){
					JScrollPane pane = (JScrollPane) panel.getComponent(i);
					JViewport puerto = (JViewport) pane.getComponent(3);
					JTableHeader cabecera = (JTableHeader) puerto.getComponent(0);
					JTable tabla = cabecera.getTable();
					ModeloEntradas modelo = (ModeloEntradas) tabla.getModel();
					modelo.clearModel();
					ArrayList<Map<String,String>> datos = MySQL.getEntradas(opcion);
					idturno = MySQL.getIdTurno(opcion);
					Iterator<Map<String,String>> it = datos.iterator();
					while(it.hasNext()){
						Map<String,String> mapa = it.next();
						ArrayList<Object> array = new ArrayList<Object>();
						array.add("*"+mapa.get("nombre"));
						array.add(mapa.get("nombre_bebida"));
						array.add(mapa.get("precio"));
						modelo.addRow(array.toArray());
					}
					modelo.addEmptyRow();
					tabla.setModel(modelo);
					fin = true;
				}
			}
			panel.validate();
		}
	}

}
