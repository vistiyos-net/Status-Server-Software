package net.vistiyos.interfaz;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class VentanaInfo extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JPanel panel;
	private JTextArea texto;
	private JButton aceptar;
	private ImageIcon icon;
	private JLabel image;
	private GridBagConstraints estilo;
	
	public static final int INFO = 1;
	public static final int ERROR = 2;
	public static final int WARN = 3;
	
	VentanaInfo(Ventana padre,String texto,int tipo){
		super(padre);
		panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		estilo = new GridBagConstraints();
		this.texto = new JTextArea(texto);
		this.texto.setEditable(false);
		this.texto.setWrapStyleWord(true);
		this.texto.setLineWrap(true);
		this.texto.setOpaque(false);
		aceptar = new JButton("Aceptar");
		aceptar.requestFocus();
		aceptar.addActionListener(this);
		estilo.gridheight = 1;
		estilo.gridwidth = 1;
		estilo.gridx = 0;
		estilo.gridy = 0;
		switch(tipo){
			case INFO:
				icon = new ImageIcon(System.getProperty("user.dir")+"/icons/info.png");
				this.setTitle("Información");
			break;
			case ERROR:
				icon = new ImageIcon(System.getProperty("user.dir")+"/icons/error.png");
				this.setTitle("Error");
			break;
			case WARN:
				icon = new ImageIcon(System.getProperty("user.dir")+"/icons/warn.png");
				this.setTitle("Aviso");
			break;
		}
		image = new JLabel();
		image.setIcon(icon);
		estilo.insets = new Insets(15,15,15,15);
		panel.add(image,estilo);
		estilo.gridx = 1;
		estilo.gridwidth = 2;
		estilo.weightx = 1.0;
		estilo.weighty = 1.0;
		panel.add(this.texto, estilo);
		estilo.gridx = 2;
		estilo.gridy = 1;
		estilo.gridwidth = 1;
		estilo.insets = new Insets(0,0,15,0);
		panel.add(aceptar, estilo);
		this.setContentPane(panel);
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setSize(250, 150);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		this.setVisible(false);
	}

}
