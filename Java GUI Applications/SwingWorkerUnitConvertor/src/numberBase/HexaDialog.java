package numberBase;

/*
 * Mar 2, 2018
 * COMP 585 Project 2
 * SwingWorker
 * Yixin Chen
 * 
 * HexaDialog class
 * Convert a hexadecimal number to decimal, octal, and binary number
 *
 */

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class HexaDialog extends JInternalFrame{
private static HexaDialog instance = null;
	
	private JTextField textField;
	private JButton button;
	private JLabel label, label1, label2, label3;
	private JPanel upperPanel, lowerPanel;
	private KeyListener keyListener;
	
	public static HexaDialog getInstance(){
		if(instance == null){
			instance = new HexaDialog();
		}
		return instance;
	}
	
	//build frame
	private  HexaDialog(){
		//args: title, resisability, closability, maximzablity and iconifiability
		super("Hexadecimal", false, true, false, false);
		textField = new JTextField(10);
		button = new JButton("Convert");
		label = new JLabel("Hexadecimal: ");
		label1 = new JLabel(" ");
		label2 = new JLabel(" ");
		label3 = new JLabel(" ");
		upperPanel = new JPanel();
		lowerPanel = new JPanel();
		upperPanel.setLayout(new FlowLayout());
		lowerPanel.setLayout(new BorderLayout());
		
		upperPanel.add(label);
		upperPanel.add(textField);
		upperPanel.add(button);
		lowerPanel.add(label1, BorderLayout.NORTH);
		lowerPanel.add(label2, BorderLayout.CENTER);
		lowerPanel.add(label3, BorderLayout.SOUTH);
		
		add(upperPanel, BorderLayout.NORTH);
		add(lowerPanel, BorderLayout.SOUTH);
		
		addButtonListener();
		createKeyListener();
		textField.addKeyListener(keyListener);
		pack();
		//setBounds(25, 25, 250, 120);
		setLocation(100, 100);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}

	private void addButtonListener() {
		// TODO Auto-generated method stub
		button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Hexadecimal();				
			}
		});
	}
	
	private void createKeyListener(){
		keyListener = new KeyListener(){

			@Override
			public void keyPressed(KeyEvent ke) 
			{
				switch(ke.getKeyCode())
				{
					case 10:
						//Enter, evaluate the expression input
						Hexadecimal();
						break;
					case 27:
						//Escape, clear the input field
						dispose();
						instance = new HexaDialog();
						break;
				}
			}

			@Override
			public void keyReleased(KeyEvent ke) 
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyTyped(KeyEvent ke)
			{
				// TODO Auto-generated method stub
				
			}			
		};		
	}

	//convert and display numbers
	private void Hexadecimal(){

		try{
			String hex = textField.getText();
			ConvertHexadecimal conHex = new ConvertHexadecimal(hex);
			label1.setText("Decimal: " + String.valueOf(conHex.getDecimal()));
			label2.setText("Binary: " + conHex.getBinary());
			label3.setText("Octal: " + conHex.getOctal());
		}
		catch(NumberFormatException nfe){
			JOptionPane.showMessageDialog(this,"Enter a hexadecimal number!");
		}
	}
}
