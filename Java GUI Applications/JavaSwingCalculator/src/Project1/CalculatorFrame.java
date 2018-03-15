/*
 * Feb 8, 2018
 * COMP 585 Project 1
 * Yixin Chen
 * 
 * CalculatorFrame class
 * 
 * Construct the frame of the calculator:
 * build input panel: input and history
 * build button panel: numbers: 0-9, operators: ., %, =, /, *, -, +, √, x², mod, x⁻¹, ←, C, and CE 
 * build main panel: input panel and button panel
 * add click listeners
 * add key listeners
 * build menu
 * add menu listeners
 *
 */

package Project1;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class CalculatorFrame extends JFrame
{	
	private static final long serialVersionUID = 1L;
	
	// panels
    private JPanel mainPanel;
    private JPanel buttonPanel;
    private JPanel inputPanel;

    // text field
    private JTextField input;
	
	//text area
	private JTextArea history;

    // buttons
    private JButton buttons[];

    // menu
    private JMenuBar menuBar;
    private JMenu appMenu;
    private JMenu helpMenu;
    private JMenuItem exit;
    private JMenuItem about;
    
    //key listener
	private KeyListener keyListener;
	
	private boolean evaluate = false;
	
	public CalculatorFrame()
	{		
		//build input panel, button panel and main panel
		buildInputPanel();
		buildButtonPanel();		
		buildMainPanel();
		add(mainPanel);
		
		//build menu
		buildMenu();
        setJMenuBar(menuBar);

        //set icon
        setIconImage(Toolkit.getDefaultToolkit().getImage("images/calculator.png"));
        
        pack();
	}
	
	private void buildInputPanel(){
		//create text field for input
        input = new JTextField();
        input.setFont(new Font("Dialog", Font.BOLD, 14));
        input.setHorizontalAlignment(SwingConstants.RIGHT);
        
        //create text area listing calculation history
		history = new JTextArea();
		history.setRows(5);
		history.setEditable(false);
		history.setBackground(new JButton().getBackground());
		history.setFont(new JButton().getFont());
		
		//build input panel
		inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
		//add textfield input to input panel
        inputPanel.add(input, BorderLayout.CENTER);
        //add textarea history to input panel
		JScrollPane myScrollPane = new JScrollPane(history);
		inputPanel.add(myScrollPane, BorderLayout.NORTH);
		
		//create and add KeyListener, Enter and Esc
		createKeyListener();
		input.addKeyListener(keyListener);
		history.addKeyListener(keyListener);

	}
	
	//build button panel
	private void buildButtonPanel()
	{
        //Create button JPanel, and set GridLayout
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 6, 6, 6));
		
		//create and label buttons
		buttons = new JButton[24];		
		
		// Create numeric buttons
		for (int i = 0; i < 10; i++)
		{
			// set each Jbutton label to the value of index
			buttons[i] = new JButton(String.valueOf(i));
		}
				
		// Create operator Jbuttons
		buttons[10] = new JButton(".");
		buttons[11] = new JButton("%");
		buttons[12] = new JButton("=");
		buttons[13] = new JButton("/");
		buttons[14] = new JButton("*");
		buttons[15] = new JButton("-");
		buttons[16] = new JButton("+");
		buttons[17] = new JButton("√");
		buttons[18] = new JButton("x²");
		buttons[19] = new JButton("mod");
		buttons[20] = new JButton("x⁻¹");
		buttons[21] = new JButton("←");
		buttons[22] = new JButton("C");
		buttons[23] = new JButton("CE");
		
        //add buttons to button JPanel
		//first row
		for (int i = 7; i < 10; i++)
		{
			buttonPanel.add(buttons[i]);
		}
		buttonPanel.add(buttons[13]);
		buttonPanel.add(buttons[22]);
		buttonPanel.add(buttons[23]);

		//second row
		for (int i = 4; i < 7; i++)
		{
			buttonPanel.add(buttons[i]);
		}
		buttonPanel.add(buttons[14]);
		buttonPanel.add(buttons[17]);
		buttonPanel.add(buttons[21]);
		
		//third row
		for (int i = 1; i < 4; i++)
		{
			buttonPanel.add(buttons[i]);
		}
		buttonPanel.add(buttons[15]);
		buttonPanel.add(buttons[18]);
		buttonPanel.add(buttons[20]);

		//forth row
		buttonPanel.add(buttons[0]);
		buttonPanel.add(buttons[10]);
		buttonPanel.add(buttons[11]);
		buttonPanel.add(buttons[16]);
		buttonPanel.add(buttons[19]);
		buttonPanel.add(buttons[12]);
		      
		//set empty border
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));

		//add action listener to buttons
		addClickListeners();
	}
	
	//build main panel
	private void buildMainPanel()
	{
		//Create main JPanel for the calculator, and set BorderLayout
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
		//add text field input to main panel
        mainPanel.add(inputPanel, BorderLayout.CENTER);
        //add button panel to main panel
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		mainPanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
	}
	
	//click listeners
	public class ClickListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent ae){
			for(int i = 0; i < buttons.length; i++)
			{
				if(ae.getSource() == buttons[i]){
					String temp = String.valueOf(i);
					//add action listeners to numeric buttons
					if(i < 10){
						if (evaluate) 
						{
							clearExist();
						}
						addToDisplay(temp);
								
					}
					//add action listeners to operator buttons and other functional buttons
					else
					{
						switch(i)
						{
							//operator .
							case 10:
								addToDisplay(".");
								break;
							
							//operator %
							case 11:
								addToDisplay("%");
								break;
							
							//operator =
							case 12:
								evaluateExpression();
								break;
							
							//operator /	
							case 13:
								addToDisplay("/");
								break;
							
							//operator *
							case 14:
								addToDisplay("*");
								break;
								
							//operator -
							case 15:
								addToDisplay("-");
								break;
							
							//operator +
							case 16:
								addToDisplay("+");
								break;
														
							//operator √
							case 17:
								if(evaluate) 
								{
									clearExist();
								}
								addToDisplay("√");
								break;
							
							//operator ²
							case 18:
								addToDisplay("²");	
								break;
							
							//operator mod
							case 19:
								addToDisplay("mod");
								break;
							
							//operator	⁻¹
							case 20:
								addToDisplay("⁻¹");
								break;
							
							//function button backspace
							case 21:
								if(evaluate)
								{
									clearExist();
								}
								else if(input.getText().length() > 0)
								{
									input.setText(input.getText().substring(0, input.getText().length() - 1));
								}
								break;
							
							//function button C, clear all (input and history)
							case 22:	
								clearAll();
								break;
							
							//function button CE, clear existing (input)
							case 23:
								clearExist();
								break;						
						}
					}
				}
			}			
		}
	}
	
	private void addClickListeners(){
		ActionListener listener = new ClickListener();
		for (int i=0; i<buttons.length; i++){
			buttons[i].addActionListener(listener);
		}
	}
	
	private void addToDisplay(String str){
		input.setText(input.getText() + str);
		evaluate = false;
	}
	
	private void clearExist(){
		input.setText("");
		evaluate = false;
	}
	
	private void clearAll(){
		input.setText(""); 
		history.setText(""); 
		evaluate = false;
	}
	
	private void buildMenu()
	{
	    // setup the menu
        menuBar = new JMenuBar();
        
        // menu and short cuts
        appMenu = new JMenu("App");
		appMenu.setMnemonic(KeyEvent.VK_A);
        helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(KeyEvent.VK_H);
        
		// menu items
        exit = new JMenuItem("Exit");
        about = new JMenuItem("About");
        
        // add menu items to menu
        appMenu.add(exit);
        helpMenu.add(about);
        
        // add menus to menu bar
        menuBar.add(appMenu);
        menuBar.add(helpMenu);
        
        //add menu listeners
        addMenuListeners();
	}
	
	//menu listeners
	private void addMenuListeners(){
		exit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent ae) {
				exitActionPerformed();				
			}
		});

		about.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent ae) {
				aboutActionPerformed();
			}
		});
	}
	
	private void exitActionPerformed(){
		dispose();
	}

	private void aboutActionPerformed(){
		JOptionPane.showMessageDialog(this, "Calculator information.\n\nDeveloper: Yixin Chen\nVersion: 1.0.");
	}
	
	//create key listener
	private void createKeyListener() 
	{
		keyListener = new KeyListener()
		{

			@Override
			public void keyPressed(KeyEvent ke) 
			{
				handleKeyPressed(ke.getKeyCode());
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
	
	//key listener for Enter and Esc
	private void handleKeyPressed(int keyCode)
	{
		switch(keyCode)
		{
			case 10:
				//Enter, evaluate the expression input
				evaluateExpression();
				break;
			case 27:
				//Escape, clear the input field
				clearExist();
				break;
		}
	}

	//evaluate expression input
	public void evaluateExpression()
	{
		Calculation res = new Calculation(input.getText());
		//if the expression is valid, perform the evaluation
		if(res.getValidation()){
			String result = String.valueOf(Math.round(res.getResult()*1000000.0)/1000000.0);
			//if the result is an integer, delete ".0"
			if(result.charAt(result.length()-1) == '0')
			{
				result = result.substring(0, result.length()-2);
			}		
			//display result to text area history
			if(history.getText() == "")
			{
				history.setText(input.getText() + " = " + result + "\n");
			}
			else
			{
				history.append(input.getText() + " = " + result + "\n");
			}
			//display result to text field input
			input.setText(result);
			evaluate = true;
		}
	}		
}
