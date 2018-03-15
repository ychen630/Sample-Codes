package numberBase;

/*
 * Mar 2, 2018
 * COMP 585 Project 2
 * SwingWorker
 * Yixin Chen
 * 
 * OctalDialog class
 * Convert a binary number to decimal, binary, and hexadecimal number
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

public class OctalDialog extends JInternalFrame{
private static  OctalDialog instance = null;
	
	private JTextField textField;
	private JButton button;
	private JLabel label, label1, label2, label3;
	private JPanel upperPanel, lowerPanel;
	private KeyListener keyListener;
	
	public static  OctalDialog getInstance(){
		if(instance == null){
			instance = new OctalDialog();
		}
		return instance;
	}
	
	//build frame
	private OctalDialog(){
		//args: title, resisability, closability, maximzablity and iconifiability
		super("Octal", false, true, false, false);
		textField = new JTextField(10);
		button = new JButton("Convert");
		label = new JLabel("Octal: ");
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
				Octal();				
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
						Octal();
						break;
					case 27:
						//Escape, clear the input field
						dispose();
						instance = new OctalDialog();
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
	private void Octal(){

		try{
			String oct = textField.getText();
			ConvertOctal conOct = new ConvertOctal(oct);
			label1.setText("Decimal: " + String.valueOf(conOct.getDecimal()));
			label2.setText("Binary: " + conOct.getBinary());
			label3.setText("Hexadecimal: " + conOct.getHexa());
		}
		catch(NumberFormatException nfe){
			JOptionPane.showMessageDialog(this,"Enter an octal number!");
		}
	}
}
