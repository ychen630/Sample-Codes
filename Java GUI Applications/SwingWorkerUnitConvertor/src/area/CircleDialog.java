/*
 * Mar 2, 2018
 * COMP 585 Project 2
 * SwingWorker
 * Yixin Chen
 * 
 * CircleDialog class
 * Build internal frame for function area of circle
 * and calculate the area of a circle of radius input by user
 *
 */


package area;

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

public class CircleDialog extends JInternalFrame{
	private static CircleDialog instance = null;
	
	private JTextField textField;
	private JButton button;
	private JLabel label, label1, label2;
	private JPanel upperPanel, lowerPanel;
	private KeyListener keyListener;
	
	public static CircleDialog getInstance(){
		if(instance == null){
			instance = new CircleDialog();
		}
		return instance;
	}
	
	//build frame
	private CircleDialog(){
		//args: title, resisability, closability, maximzablity and iconifiability
		super("Area Of Circle", false, true, false, false);
		textField = new JTextField(10);
		textField.setFocusable(true);
		button = new JButton("Calculate");
		label = new JLabel("Radius: ");
		label1 = new JLabel("Area: ");
		label2 = new JLabel();
		upperPanel = new JPanel();
		lowerPanel = new JPanel();
		upperPanel.setLayout(new FlowLayout());
		lowerPanel.setLayout(new FlowLayout());
		
		upperPanel.add(label);
		upperPanel.add(textField);
		upperPanel.add(button);
		lowerPanel.add(label1);
		lowerPanel.add(label2);
		
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

	private void createKeyListener() 
	{
		keyListener = new KeyListener()
		{

			@Override
			public void keyPressed(KeyEvent ke) 
			{
				switch(ke.getKeyCode())
				{
					case 10:
						//Enter, evaluate the expression input
						areaOfCircle();
						break;
					case 27:
						//Escape, clear the input field
						dispose();
						instance = new CircleDialog();
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

	private void addButtonListener() {
		// TODO Auto-generated method stub
		button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				areaOfCircle();
				
			}
			
		});
		
	}

	//calculate area
	private void areaOfCircle(){
		label2.setText("");
		try{
			double radius = Double.parseDouble(textField.getText());
			double answer = Math.PI*(radius*radius);
			label2.setText(String.valueOf(answer));
		}
		catch(NumberFormatException nfe){
			JOptionPane.showMessageDialog(this,"Enter a number!");
		}
	}
}
