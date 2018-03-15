package numberBase;

/*
 * Mar 2, 2018
 * COMP 585 Project 2
 * SwingWorker
 * Yixin Chen
 * 
 * BinaryDialog class
 * Convert a binary number to decimal, octal, and hexadecimal number
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

public class BinaryDialog extends JInternalFrame{
private static  BinaryDialog instance = null;
	
	private JTextField textField;
	private JButton button;
	private JLabel label, label1, label2, label3;
	private JPanel upperPanel, lowerPanel;
	private KeyListener keyListener;
	
	public static  BinaryDialog getInstance(){
		if(instance == null){
			instance = new  BinaryDialog();
		}
		return instance;
	}
	
	//build frame
	private BinaryDialog(){
		//args: title, resisability, closability, maximzablity and iconifiability
		super("Binary", false, true, false, false);
		textField = new JTextField(10);
		button = new JButton("Convert");
		label = new JLabel("Binary: ");
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
				Binary();				
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
						Binary();
						break;
					case 27:
						//Escape, clear the input field
						dispose();
						instance = new BinaryDialog();
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
	private void Binary(){

		try{
			String bin = textField.getText();
			ConvertBinary conBin = new ConvertBinary(bin);
			label1.setText("Decimal: " + String.valueOf(conBin.getDecimal()));
			label2.setText("Octal: " + conBin.getOctal());
			label3.setText("Hexadecimal: " + conBin.getHexa());
		}
		catch(NumberFormatException nfe){
			JOptionPane.showMessageDialog(this,"Enter a binary number!");
		}
	}
}
