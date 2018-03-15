/*
 * Mar 2, 2018
 * COMP 585 Project 2
 * SwingWorker
 * Yixin Chen
 * 
 * EllipseDialog class
 * Build internal frame for function area of ellipse
 * and calculate the area of a given ellipse
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

public class EllipseDialog extends JInternalFrame{
	private static EllipseDialog instance = null;
	
	private JTextField textField1, textField2;
	private JButton button;
	private JLabel label1, label2, label3, label4;
	private JPanel upperPanel, lowerPanel;
	private KeyListener keyListener;
	
	public static EllipseDialog getInstance(){
		if(instance == null){
			instance = new EllipseDialog();
		}
		return instance;
	}
	
	//build frame
	private EllipseDialog(){
		//args: title, resisability, closability, maximzablity and iconifiability
		super("Area Of Ellipse", false, true, false, false);
		textField1 = new JTextField(10);
		textField2 = new JTextField(10);
		button = new JButton("Calculate");
		label1 = new JLabel("Area: ");
		label2 = new JLabel();
		label3 = new JLabel("Axis a: ");
		label4 = new JLabel("Axis b: ");
		upperPanel = new JPanel();
		lowerPanel = new JPanel();
		upperPanel.setLayout(new FlowLayout());
		lowerPanel.setLayout(new FlowLayout());
		
		upperPanel.add(label3);
		upperPanel.add(textField1);
		upperPanel.add(label4);
		upperPanel.add(textField2);
		upperPanel.add(button);
		lowerPanel.add(label1);
		lowerPanel.add(label2);
		
		add(upperPanel, BorderLayout.NORTH);
		add(lowerPanel, BorderLayout.SOUTH);
		
		addButtonListener();
		createKeyListener();
		textField1.addKeyListener(keyListener);
		textField2.addKeyListener(keyListener);
		pack();
		//setBounds(25, 25, 250, 200);
		setLocation(100, 100);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}

	private void addButtonListener() {
		// TODO Auto-generated method stub
		button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				areaOfEllipse();
			}
		});	
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
						areaOfEllipse();
						break;
					case 27:
						//Escape, clear the input field
						dispose();
						instance = new EllipseDialog();
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
	
	//calculate area
	private void areaOfEllipse(){
		label2.setText("");
		try{
			double radius1 = Double.parseDouble(textField1.getText());
			double radius2 = Double.parseDouble(textField2.getText());
			double answer = Math.PI*(radius1*radius2);
			label2.setText(String.valueOf(answer));
		}
		catch(NumberFormatException nfe){
			JOptionPane.showMessageDialog(this,"Enter a number!");
		}
	}
}
