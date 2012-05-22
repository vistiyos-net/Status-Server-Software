package net.vistiyos.interfaz;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;

public class Sidebar extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	
	private Ventana padre;
	private GridBagConstraints estilo;
	private String[] elementos = {"Alta de Productos","Gestionar Stock","Correción de Stock","Gestión de Precios Barras","Gestión de Precios Entrada","Alta de Empleados","Editar Datos de Empleados","Asignación de Empleados","Gestor de Configuración","Activar Turno"};
	private JButton[] botones;
	private Map<String,String> botonesID;
	
	
	Sidebar(Ventana padre){
		super();
		this.padre = padre; 
		init();
		initLayout();
		initEstilo();
		setElementos();
	}
	
	private void setElementos() {
		int i = 0;
		for(String elemento : elementos){
			botones[i] = new JButton(elemento);
			botones[i].addActionListener(this);
			estilo.gridy++;
			this.add(botones[i], estilo);
			i++;
		}
		JButton bloquear = new JButton("Bloquear Software");
		bloquear.addActionListener(this);
		bloquear.setName("BLOQUEAR");
		estilo.gridy++;
		this.add(bloquear, estilo);
	}

	private void init(){
		estilo = new GridBagConstraints();
		botones = new JButton[elementos.length];
		botonesID = new HashMap<String,String>();
		for(String elemento : elementos){
			String clave = elemento.toLowerCase().replaceAll(" ", "-");
			System.out.println(clave);
			botonesID.put(clave, elemento);
		}
		
	}
	
	private void initLayout(){
		this.setLayout(new GridBagLayout());
	}
	
	private void initEstilo(){
		estilo.gridx = 0;
        estilo.gridy = -1;
        estilo.fill = GridBagConstraints.BOTH;
        estilo.gridwidth = 1;
        estilo.gridheight = 1;
        estilo.weightx = 1.0;
        estilo.weighty = 1.0;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		JButton boton = (JButton) event.getSource();
		if(boton.getName() != null && boton.getName().equals("BLOQUEAR")){
			new VentanaPassword(this.padre);
		}
		else{
			padre.setContenido(boton.getText().toLowerCase().replaceAll(" ", "-"));
		}
	}

}
