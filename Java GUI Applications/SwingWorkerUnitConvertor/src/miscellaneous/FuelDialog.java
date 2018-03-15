package miscellaneous;

/*
 * Mar 2, 2018
 * COMP 585 Project 2
 * SwingWorker
 * Yixin Chen
 * 
 * FuelDialog class
 * Convert fuel economy unit between miles per gallon and liters per 100 km
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

public class FuelDialog extends JInternalFrame{
	private static FuelDialog instance = null;
	
	private JTextField textField1, textField2;
	private JButton button1, button2;
	private JLabel label3, label4;
	private JPanel upperPanel;
	private KeyListener keyListener1, keyListener2;
	
	public static FuelDialog getInstance(){
		if(instance == null){
			instance = new FuelDialog();
		}
		return instance;
	}
	
	//build frae
	private FuelDialog(){
		//args: title, resisability, closability, maximzablity and iconifiability
		super("Fuel Economy", false, true, false, false);
		textField1 = new JTextField(10);
		textField2 = new JTextField(10);
		button1 = new JButton("Convert");
		button2 = new JButton("Convert");

		label3 = new JLabel("Miles per gallon ");
		label4 = new JLabel("Liters per 100 km ");
		upperPanel = new JPanel();
		upperPanel.setLayout(new FlowLayout());
		upperPanel.add(label3);
		upperPanel.add(textField1);
		upperPanel.add(button1);
		upperPanel.add(label4);
		upperPanel.add(textField2);
		upperPanel.add(button2);

		
		add(upperPanel, BorderLayout.NORTH);
		
		addButtonListener();
		createKeyListener1();
		textField1.addKeyListener(keyListener1);
		createKeyListener2();
		textField2.addKeyListener(keyListener2);
		
		pack();
		//setBounds(25, 25, 250, 200);
		setLocation(100, 100);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}

	private void addButtonListener() {
		// TODO Auto-generated method stub
		button1.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				mpgToLkm();				
			}			
		});
		button2.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				lkmtoMpg();				
			}			
		});		
	}
	
	private void createKeyListener1(){
		keyListener1 = new KeyListener(){

			@Override
			public void keyPressed(KeyEvent ke) 
			{
				switch(ke.getKeyCode())
				{
					case 10:
						//Enter, evaluate the expression input
						mpgToLkm();	
						break;
					case 27:
						//Escape, clear the input field
						dispose();
						instance = new FuelDialog();
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
	
	private void createKeyListener2(){
		keyListener2 = new KeyListener(){

			@Override
			public void keyPressed(KeyEvent ke) 
			{
				switch(ke.getKeyCode())
				{
					case 10:
						//Enter, evaluate the expression input
						lkmtoMpg();	
						break;
					case 27:
						//Escape, clear the input field
						dispose();
						instance = new FuelDialog();
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

	//convert mpg to l/100km
	private void mpgToLkm(){
		try{
			double a = Double.parseDouble(textField1.getText());
			double answer = 235.215/a;
			textField2.setText(String.valueOf(Math.round(answer*100.0)/100.0));
		}
		catch(NumberFormatException nfe){
			JOptionPane.showMessageDialog(this,"Enter a number!");
		}
	}
	
	//convert l/100km to mpg
	private void lkmtoMpg(){
		try{
			double a = Double.parseDouble(textField2.getText());
			double answer = 235.215/a;
			textField1.setText(String.valueOf(Math.round(answer*100.0)/100.0));
		}
		catch(NumberFormatException nfe){
			JOptionPane.showMessageDialog(this,"Enter a number!");
		}
	}
}
