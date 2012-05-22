package net.vistiyos.interfaz;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UnsupportedLookAndFeelException;

public class Ventana extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Sidebar menu;
	private Contenido contenido;
	private JPanel todo;
	private GridBagConstraints estilo;
	
	public Ventana() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, SQLException{
		super("Administrador Status");
		init();
		initLayout();
		initEstilo();
		todo.add(menu,estilo);
		estilo.gridx++;
		estilo.gridwidth = 5;
        estilo.weightx = 1.0;
        estilo.weighty = 1.0;
		todo.add(contenido,estilo);
		this.setContentPane(todo);
        this.setVisible(true);
        new VentanaPassword(this);
	}
		
	private void init() throws SQLException{
		todo = new JPanel();
		estilo = new GridBagConstraints();
		menu = new Sidebar(this);
		contenido = new Contenido(this);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        this.toFront();
        this.setAlwaysOnTop(true);
	}
	
	private void initLayout(){
		todo.setLayout(new GridBagLayout());
	}
	
	private void initEstilo(){
		estilo.gridx = 0;
        estilo.gridy = 0;
        estilo.fill = GridBagConstraints.VERTICAL;
        estilo.gridwidth = 1;
	}
	
	void setContenido(String id){
		contenido.setContenido(id);
	}

}