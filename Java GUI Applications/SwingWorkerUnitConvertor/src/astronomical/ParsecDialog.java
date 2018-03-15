package astronomical;
/*
 * Feb 27, 2018
 * COMP 585 Project 2
 * SwingWorker
 * Yixin Chen
 * 
 * ParsecDialog Class
 * Convert Astronomical distances
 * from parsec to km, astronomical unit and lightyear
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

public class ParsecDialog extends JInternalFrame{
	private static ParsecDialog instance = null;
	
	private JTextField textField;
	private JButton button;
	private JLabel label, label1, label2, label3;
	private JPanel upperPanel, lowerPanel;
	private KeyListener keyListener;
	
	public static ParsecDialog getInstance(){
		if(instance == null){
			instance = new ParsecDialog();
		}
		return instance;
	}
	
	//build frame
	private ParsecDialog(){
		//args: title, resisability, closability, maximzablity and iconifiability
		super("Parsec", false, true, false, false);
		textField = new JTextField(20);
		button = new JButton("Convert");
		label = new JLabel("Parsec: ");
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
				Parsec();			
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
						Parsec();
						break;
					case 27:
						//Escape, clear the input field
						dispose();
						instance = new ParsecDialog();
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

	//convert unit
	private void Parsec(){
		try{
			double ps = Double.parseDouble(textField.getText());
			String prefix = String.valueOf(ps);
			if(prefix.charAt(prefix.length()-1) == '0')
			{
				prefix = prefix.substring(0, prefix.length()-2);
			}
			if(ps == 1)
				prefix = prefix + " parsec ≈ ";
			else
				prefix = prefix + " parsecs ≈ ";
			
			double ans_km = ps*206264.806247096*149597870.7;
			ScientificNotation ans_km_SN = new ScientificNotation(ans_km);
			label1.setText(prefix + ans_km_SN.getResult() + " kilometers");
			
			double ans_AU = ans_km / 149597870.7;
			ScientificNotation ans_AU_SN = new ScientificNotation(ans_AU);
			label2.setText(prefix + ans_AU_SN.getResult() + " astronomical units");
			
			double ans_Ly = ans_km / 9460730472580.8; 
			ScientificNotation ans_Ly_SN = new ScientificNotation(ans_Ly);
			label3.setText(prefix + ans_Ly_SN.getResult() + " lightyears");
		}
		catch(NumberFormatException nfe){
			JOptionPane.showMessageDialog(this,"Enter a number!");
		}
	}
}
