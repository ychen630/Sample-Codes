package miscellaneous;
/*
 * Feb 27, 2018
 * COMP 585 Project 2
 * SwingWorker
 * Yixin Chen
 * 
 * ZodiacDialog Class
 * Calculate the Chinese Zodiac of a given year
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

public class ZodiacDialog extends JInternalFrame{
	private static ZodiacDialog instance = null;
	
	private JTextField textField;
	private JButton button;
	private JLabel label, label1, label2;
	private JPanel upperPanel, lowerPanel;
	private KeyListener keyListener;
	
	public static ZodiacDialog getInstance(){
		if(instance == null){
			instance = new ZodiacDialog();
		}
		return instance;
	}
	
	//build frame
	private ZodiacDialog(){
		//args: title, resisability, closability, maximzablity and iconifiability
		super("Chinese Zodiac", false, true, false, false);
		textField = new JTextField(10);
		button = new JButton("Show");
		label = new JLabel("Born year: ");
		label1 = new JLabel("Your zodiac: ");
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

	private void addButtonListener() {
		button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				ChineseZodiac();				
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
						ChineseZodiac();
						break;
					case 27:
						//Escape, clear the input field
						dispose();
						instance = new ZodiacDialog();
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

	//calculate the Chinese Zodiac
	private void ChineseZodiac(){
		label2.setText("");
		try{
			int year = Integer.parseInt(textField.getText());
			String answer = new String();
			switch(year%12){
		      case 0: 
		    	  answer = "Monkey"; 
		    	  break;
		      case 1: 
		    	  answer = "Rooster";
		    	  break;
		      case 2: 
		    	  answer = "Dog"; 
		    	  break;
		      case 3: 
		    	  answer = "Pig";
		    	  break;
		      case 4: 
		    	  answer = "Rat"; 
		    	  break;
		      case 5: 
		    	  answer = "Ox"; 
		    	  break;
		      case 6: 
		    	  answer = "Tiger"; 
		    	  break;
		      case 7: 
		    	  answer = "Rabbit"; 
		    	  break;
		      case 8: 
		    	  answer = "Dragon"; 
		    	  break;
		      case 9: 
		    	  answer = "Snake"; 
		    	  break;
		      case 10: 
		    	  answer = "Horse"; 
		    	  break;
		      case 11: 
		    	  answer = "Sheep"; 
		    	  break;
			}
			label2.setText(answer);
		}
		catch(NumberFormatException nfe){
			JOptionPane.showMessageDialog(this,"Enter an integer!");
		}
	}
}
