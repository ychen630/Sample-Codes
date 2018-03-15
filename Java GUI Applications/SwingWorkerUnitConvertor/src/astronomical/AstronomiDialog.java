package astronomical;
/*
 * Feb 27, 2018
 * COMP 585 Project 2
 * Swingworker
 * Yixin Chen
 * 
 * AstronomiDialog Class
 * Convert astronomical distances
 * From astronomical unit to km, lightyear and parsec
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

public class AstronomiDialog extends JInternalFrame{
	private static AstronomiDialog instance = null;
	
	private JTextField textField;
	private JButton button;
	private JLabel label, label1, label2, label3;
	private JPanel upperPanel, lowerPanel;
	private KeyListener keyListener;
	
	public static AstronomiDialog getInstance(){
		if(instance == null){
			instance = new AstronomiDialog();
		}
		return instance;
	}
	
	//build frame
	private AstronomiDialog(){
		//args: title, resisability, closability, maximzablity and iconifiability
		super("Astronomical Unit", false, true, false, false);
		textField = new JTextField(20);
		button = new JButton("Convert");
		label = new JLabel("Astronomical Unit: ");
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
		setLocation(100, 100);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}
	
	//button listener
	private void addButtonListener() {
		button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				au();			
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
						au();
						break;
					case 27:
						//Escape, clear the input field
						dispose();
						instance = new AstronomiDialog();
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
	
	//covert unit
	private void au(){
		try{
			double au = Double.parseDouble(textField.getText());
			String prefix = String.valueOf(au);
			if(prefix.charAt(prefix.length()-1) == '0')
			{
				prefix = prefix.substring(0, prefix.length()-2);
			}
			if(au == 1)
				prefix = prefix + " astronomical unit ≈ ";
			else
				prefix = prefix + " astronomical units ≈ ";
			
			double ans_km = au*149597870.7;
			ScientificNotation ans_km_SN = new ScientificNotation(ans_km);
			label1.setText(prefix + ans_km_SN.getResult() + " kilometers");
			
			double ans_Ly = ans_km / 9460730472580.8; 
			ScientificNotation ans_Ly_SN = new ScientificNotation(ans_Ly);
			label2.setText(prefix + ans_Ly_SN.getResult() + " lightyears");
			
			double ans_Ps = au / 206264.806247096;
			ScientificNotation ans_Ps_SN = new ScientificNotation(ans_Ps);
			label3.setText(prefix + ans_Ps_SN.getResult() + " parsecs");
		}
		catch(NumberFormatException nfe){
			JOptionPane.showMessageDialog(this,"Enter a number!");
		}
	}
}
