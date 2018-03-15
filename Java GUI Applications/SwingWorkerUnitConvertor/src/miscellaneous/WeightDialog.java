package miscellaneous;

/*
 * Mar 2, 2018
 * COMP 585 Project 2
 * SwingWorker
 * Yixin Chen
 * 
 * WeightDialog class
 * Convert the weight on earth to weight on other planets in the Solar System and the Moon and Pluto
 *
 */

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
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

public class WeightDialog extends JInternalFrame{
	private static WeightDialog instance = null;
	
	private JTextField textField;
	private JButton button;
	private JLabel label, label1;
	private JLabel[] labels = new JLabel[9];
	private JPanel upperPanel, lowerPanel;
	private KeyListener keyListener;
	
	public static WeightDialog getInstance(){
		if(instance == null){
			instance = new WeightDialog();
		}
		return instance;
	}
	
	//build frame
	private WeightDialog(){
		//args: title, resisability, closability, maximzablity and iconifiability
		super("Weight On Other Planet", false, true, false, false);
		textField = new JTextField(15);
		button = new JButton("Convert");
		label = new JLabel("Weight on earth: ");
		label1 = new JLabel(" lbs  ");
		upperPanel = new JPanel();
		lowerPanel = new JPanel();
		upperPanel.setLayout(new FlowLayout());
		lowerPanel.setLayout(new GridLayout(3, 3, 4, 4));
		
		upperPanel.add(label);
		upperPanel.add(textField);
		upperPanel.add(label1);
		upperPanel.add(button);
		
		labels[0] = new JLabel("Moon: ");		
		labels[1] = new JLabel("Mercury: ");
		labels[2] = new JLabel("Venus: ");
		labels[3] = new JLabel("Mars: ");
		labels[4] = new JLabel("Jupiter: ");
		labels[5] = new JLabel("Saturn: ");
		labels[6] = new JLabel("Uranus: ");
		labels[7] = new JLabel("Neptune: ");
		labels[8] = new JLabel("Pluto: ");
	
		for(int i = 0; i < labels.length; i++){
			lowerPanel.add(labels[i]);
		}
		
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
				Weight();				
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
						Weight();
						break;
					case 27:
						//Escape, clear the input field
						dispose();
						instance = new WeightDialog();
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

	//convert weight
	private void Weight(){
		try{
			double wt = Double.parseDouble(textField.getText());
			
			double moo_wt = wt * 0.166;
			double mer_wt = wt * 0.378;
			double ven_wt = wt * 0.907;
			double mar_wt = wt * 0.377;
			double jup_wt = wt * 2.364;
			double sat_wt = wt * 0.916;
			double ura_wt = wt * 0.889;
			double nep_wt = wt * 1.125;
			double plu_wt = wt * 0.067;			
			double sun_wt = wt * 27.072;
			
			labels[0].setText("Moon: " + String.valueOf(Math.round(moo_wt*1000.0)/1000.0) + " lbs");		
			labels[1].setText("Mercury: " + String.valueOf(Math.round(mer_wt*1000.0)/1000.0) + " lbs");
			labels[2].setText("Venus: " + String.valueOf(Math.round(ven_wt*1000.0)/1000.0) + " lbs");
			labels[3].setText("Mars: " + String.valueOf(Math.round(mar_wt*1000.0)/1000.0) + " lbs");
			labels[4].setText("Jupiter: "+ String.valueOf(Math.round(jup_wt*1000.0)/1000.0) + " lbs");
			labels[5].setText("Saturn: " + String.valueOf(Math.round(sat_wt*1000.0)/1000.0) + " lbs");
			labels[6].setText("Uranus: " + String.valueOf(Math.round(ura_wt*1000.0)/1000.0) + " lbs");
			labels[7].setText("Neptune: " + String.valueOf(Math.round(nep_wt*1000.0)/1000.0) + " lbs");
			labels[8].setText("Pluto: " + String.valueOf(Math.round(plu_wt*1000.0)/1000.0) + " lbs");		
			
		}
		catch(NumberFormatException nfe){
			JOptionPane.showMessageDialog(this,"Enter a number!");
		}
	}
}
