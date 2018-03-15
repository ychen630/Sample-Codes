package wordCount;

/*
 * Mar 3, 2018
 * COMP 585 Project 2
 * SwingWorker
 * Yixin Chen

 * 
 * TotalDialog Class
 * Count and output the total number of words of all txt files in a selected folder
 * output the total number of words for each file
 *
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;


public class TotalDialog extends JInternalFrame{
	private static TotalDialog instance = null;

    private JLabel lbl, lbl2;
    private JTextField tf;
    private JButton fileBtn, startButton;
    private JFileChooser fc;
    private String fileName;  
    private JProgressBar progressBar;
    private JFrame frame; // to properly center JDialogFrame
    private JTextArea ta;
    private List<String> textFiles = new ArrayList<String>();
    private File file;
     
    public static TotalDialog getInstance(JFrame frame) {
        if(instance == null) {
            instance = new TotalDialog(frame);
        }
        return instance;   
    }  

    //select single file
    /*
    private void chooseFile() {
        lbl2.setText("");
        fileName = "";
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            tf.setText(file.getAbsolutePath());
            fileName = file.getAbsolutePath();
        } else {
            JOptionPane.showMessageDialog(this, "Open command cancelled by user.");
        }   
    }
*/
    //recursively traverse a selected folder, and return all the txt files in this folder and its sub-folders
    private void chooseFile() {
        lbl2.setText("");
        ta.setText("");
        fileName = "";
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fc.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = fc.getSelectedFile();
            tf.setText(file.getAbsolutePath());
            fileName = file.getAbsolutePath();
            RecursiveFileDisplay rf = new RecursiveFileDisplay(file);
            textFiles = rf.getTextFiles();
            if(file.isDirectory()){
	            ta.append("Text files in the current directory:\n");
	            if(textFiles.size() == 0){
	            	ta.append("No text file found!");
	            	JOptionPane.showMessageDialog(this, "No text file found!");
	            }
	            for(String str: textFiles){
	            	ta.append(str.substring(fileName.length() + 1, str.length())+"\n");
	            }
            }
            else{
            	ta.append("Text file:\n" + file.getName());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Open command cancelled by user.");
        }   
    }
    
    //build frame
    private TotalDialog(JFrame frame) {
        super("Total Words", false, true, false, false);
        this.frame = frame;
        // init
        tf = new JTextField(30);
        tf.setEditable(false);
        ta = new JTextArea();
		ta.setEditable(false);
		ta.setBackground(new JButton().getBackground());
		ta.setFont(new JButton().getFont());
        ta.setRows(5);
        ta.setLineWrap(true);
        fileBtn = new JButton("...");
        startButton = new JButton("Start");
        lbl = new JLabel("Total words: ");
        lbl2 = new JLabel();
        fc = new JFileChooser();
        progressBar = new JProgressBar(0, 100);
        //Call setStringPainted now so that the progress bar height
        //stays the same whether or not the string is shown.
        progressBar.setStringPainted(false);      
        fileName = "";    
        
        fileBtn.setPreferredSize(new Dimension(20, 20));
        startButton.setPreferredSize(new Dimension(80, 20));
        
        JPanel upperPanel = new JPanel();
        JPanel midPanel = new JPanel();
        JPanel lowerPanel = new JPanel();
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        
        upperPanel.setLayout(new FlowLayout());
        midPanel.setLayout(new FlowLayout());
        lowerPanel.setLayout(new FlowLayout());
        
        upperPanel.add(tf);
        upperPanel.add(fileBtn);
        upperPanel.add(startButton);
        
        midPanel.add(progressBar);
        
        lowerPanel.add(lbl);
        lowerPanel.add(lbl2);
        
        JScrollPane myScrollPane = new JScrollPane(ta);
        
        mainPanel.add(upperPanel, BorderLayout.NORTH);
        mainPanel.add(midPanel, BorderLayout.CENTER);
        mainPanel.add(lowerPanel, BorderLayout.SOUTH);  
        
        add(mainPanel, BorderLayout.NORTH);
        add(myScrollPane, BorderLayout.CENTER);

        // add filebutton listener
        fileBtn.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent ae) { 
                chooseFile();
            } 
        }); 
        
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
            	WordCount();
            }
        });
     
        pack();
        setLocation(50, 50);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    }
	
    //count all the words
	private void WordCount() {
		int words = 0;
        if(fileName.equals("")) {
            JOptionPane.showMessageDialog(frame, "Choose a file!");
            return;
        }
        if(textFiles.size() == 0){
        	JOptionPane.showMessageDialog(frame, "No text file found");
            return;
        }
        
	    progressBar.setIndeterminate(true);
	    ta.setText("");
	    try {
	    	ta.setText("File Name\tWords\n");
	    	for(int i = 0; i < textFiles.size(); i++){
	    		int count = 0;
	            String line = "";
	            FileReader data = new FileReader(textFiles.get(i));
	            BufferedReader br = new BufferedReader(data);
	            while((line = br.readLine()) != null) {
					line = line.toString().replaceAll("[^a-zA-Z ]",""); 
			    	line = line.toLowerCase();
			    	StringTokenizer tokenizer = new StringTokenizer(line);					
			    	while (tokenizer.hasMoreTokens()){
			    		tokenizer.nextToken();
						count++;
			    	  }
	            }         
	            words = words + count;
	            // close the file   
	            br.close();
				data.close();
				if(textFiles.size() > 1){
					ta.append(textFiles.get(i).substring(fileName.length() + 1, textFiles.get(i).length()) + "\t" + count + "\n");
				}
				else
					ta.append(file.getName() + "\t" + count + "\n");
	        }
            lbl2.setText(String.valueOf(words));
            ta.append("Total words: " + words);
	    	progressBar.setIndeterminate(false);
	    }
	    catch(FileNotFoundException ex) {
	    	JOptionPane.showMessageDialog(frame, "File not found!");
	    }
	    catch(IOException ex) {
	    	JOptionPane.showMessageDialog(frame, "An error occured");
	    }
	}    
}
