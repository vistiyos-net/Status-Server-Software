package net.vistiyos.interfaz;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import net.vistiyos.db.MySQL;

public class VentanaPassword extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel panel;
    private JPanel teclado;
    private JButton boton1;
    private JButton boton2;
    private JButton boton3;
    private JButton boton4;
    private JButton boton5;
    private JButton boton6;
    private JButton boton7;
    private JButton boton8;
    private JButton boton9;
    private JButton boton0;
    private JButton aceptar;
    private JButton cancelar;
    private JPasswordField password;
    private Ventana ventana;
    
    public VentanaPassword(Ventana vnt){
        super(vnt,"INTRODUZCA LA CONTRASEÑA");
        ventana = vnt;
        this.setBounds(vnt.getWidth()/2-150,vnt.getHeight()/2-150,this.getWidth(),this.getHeight());
        this.teclado=new JPanel();
        this.teclado.setLayout(new GridBagLayout());
        GridBagConstraints cn=new GridBagConstraints();
        this.boton0=new JButton("0");
        this.boton1=new JButton("1");
        this.boton2=new JButton("2");
        this.boton3=new JButton("3");
        this.boton4=new JButton("4");
        this.boton5=new JButton("5");
        this.boton6=new JButton("6");
        this.boton7=new JButton("7");
        this.boton8=new JButton("8");
        this.boton9=new JButton("9");
        this.aceptar=new JButton("Aceptar");
        this.aceptar.setName("ACEPTAR");
        this.cancelar=new JButton("Cancelar");
        this.cancelar.setName("CANCELAR");
        this.password=new JPasswordField(20);
        this.cancelar.addActionListener(this);
        this.aceptar.addActionListener(this);
        this.boton0.addActionListener(this);
        this.boton1.addActionListener(this);
        this.boton2.addActionListener(this);
        this.boton3.addActionListener(this);
        this.boton4.addActionListener(this);
        this.boton5.addActionListener(this);
        this.boton6.addActionListener(this);
        this.boton7.addActionListener(this);
        this.boton8.addActionListener(this);
        this.boton9.addActionListener(this);
        cn.gridx=0;
        cn.gridy=0;
        cn.fill=GridBagConstraints.BOTH;
        cn.gridwidth=1;
        cn.gridheight=1;
        cn.weightx=1.0;
        cn.weighty=1.0;
        cn.anchor=GridBagConstraints.NORTHWEST;
        this.teclado.add(this.boton1, cn);
        cn.gridx=1;
        cn.gridwidth=2;
        this.teclado.add(this.boton2, cn);
        cn.gridwidth=1;
        cn.gridx=3;
        this.teclado.add(this.boton3, cn);
        cn.gridx=0;
        cn.gridy++;
        this.teclado.add(this.boton4, cn);
        cn.gridx=1;
        cn.gridwidth=2;
        this.teclado.add(this.boton5, cn);
        cn.gridwidth=1;
        cn.gridx=3;
        this.teclado.add(this.boton6, cn);
        cn.gridx=0;
        cn.gridy++;
        this.teclado.add(this.boton7, cn);
        cn.gridx=1;
        cn.gridwidth=2;
        this.teclado.add(this.boton8, cn);
        cn.gridwidth=1;
        cn.gridx=3;
        this.teclado.add(this.boton9, cn);
        cn.gridy++;
        cn.gridx=0;
        this.teclado.add(this.aceptar,cn);
        cn.gridx=1;
        this.teclado.add(this.boton0, cn);
        cn.gridwidth=1;
        cn.gridx=3;
        this.teclado.add(this.cancelar, cn);
        this.panel=new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        this.panel.add(this.password);
        this.panel.add(teclado);
        this.setContentPane(this.panel);
        this.setSize(300, 300);
        this.setModalityType(ModalityType.APPLICATION_MODAL);
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.setVisible(true);
    }
    

	@Override
	public void actionPerformed(ActionEvent event) {
		JButton boton = (JButton) event.getSource();
		if(boton.getName() == null){
			StringBuilder sb = new StringBuilder();
			this.password.setText(sb.append(this.password.getPassword()).append(boton.getText()).toString());
		}
		else if(boton.getName().equals("ACEPTAR")){
			StringBuilder sb = new StringBuilder();
			sb.append(this.password.getPassword());
			if(MySQL.isClave(sb.toString())){
				this.setVisible(false);
			}
			else{
				new VentanaInfo(ventana,"¡CLAVE NO VÁLIDA!",VentanaInfo.ERROR);
			}
		}
		else if(boton.getName().equals("CANCELAR")){
			this.password.setText("");
		}
	}

}
