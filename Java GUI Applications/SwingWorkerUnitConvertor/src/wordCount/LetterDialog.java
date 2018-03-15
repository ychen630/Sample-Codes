package wordCount;

/*
 * Mar 3, 2018
 * COMP 585 Project 2
 * SwingWorker
 * Yixin Chen
 * 
 * LetterDialog Class
 * Count and output the number of words start with each a-z letters
 * within a specific file 
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

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
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;

public class LetterDialog extends JInternalFrame{
    private static LetterDialog instance = null;

    private JLabel lbl, lbl2;
    private JTextField tf;
    private JTextArea ta;
    private JFileChooser fc;
    private String fileName;   
    private JProgressBar progressBar;
    private JFrame frame; // to properly center JDialogFrame
    private JButton fileBtn, startButton;
    private List<String> textFiles = new ArrayList<String>();
    private File file;
    
    public static LetterDialog getInstance(JFrame frame) {
        if(instance == null) {
            instance = new LetterDialog(frame);
        }
        return instance;   
    }  

    //choose file method, allow user to choose one file at a time
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
    }*/
    
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
    private LetterDialog(JFrame frame) {
        super("Different Start Letters", false, true, false, false);
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
        ta.setText("Contents of file:\n");
        
        fileBtn = new JButton("...");
        startButton = new JButton("Start");
        lbl = new JLabel("Number of different start letters: ");
        lbl2 = new JLabel();
        fc = new JFileChooser();
        progressBar = new JProgressBar(0, 100);
        //Call setStringPainted now so that the progress bar height
        //stays the same whether or not the string is shown.
        progressBar.setStringPainted(false);      
        fileName = "";    
        
        fileBtn.setPreferredSize(new Dimension(20, 20));
        startButton.setPreferredSize(new Dimension(100, 20));
        
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

        // add file button listener
        fileBtn.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent ae) { 
                chooseFile();
            } 
        }); 
        
        //build button action  listener
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
            	WordCount();
            }
        });
     
        pack();
        setLocation(50, 50);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    }
    
    //use hash map to count the appearance time of each letter
	public static Map<String, Integer> wordCount(String[] strings) {
		  Map<String, Integer> map = new HashMap<String, Integer> ();
		  for (String s:strings) {		    
		    if (!map.containsKey(s)) {  // first time we've seen this string
		      map.put(s, 1);
		    }
		    else {						// from second time we see the string (key), increase count (value) by one
		      int count = map.get(s);
		      map.put(s, count + 1);
		    }
		  }
		  return map;	
	}
	
	//word count method, traverse a file and store all words in terms of its first letter 
	//in a string array list, and then count the appearance of each first letter using
	//the wordCount method
	private void WordCount() {
		int words = 0;
		int count = 0;
		ArrayList<String> temps = new ArrayList<String>();
        if(fileName.equals("")) {
        	JOptionPane.showMessageDialog(frame, "Choose a file!");
        }
        else{
            progressBar.setIndeterminate(true);
            lbl2.setText("");
            ta.setText("Contents of file:\n");
            try {
            	for(int i = 0; i < textFiles.size(); i++){
	                String line = "";
	                FileReader data = new FileReader(textFiles.get(i));
	                BufferedReader br = new BufferedReader(data);
	                
	                while((line = br.readLine()) != null) {
	    				line = line.toString().replaceAll("[^a-zA-Z ]","");
	    		    	line = line.toLowerCase();
	    		    	StringTokenizer tokenizer = new StringTokenizer(line);
	    				
	    		    	while (tokenizer.hasMoreTokens()){
	    					String tempString = String.valueOf(tokenizer.nextToken().charAt(0));
	    					if(!temps.contains(tempString)){
	    						words++;
	    					}
	    		    		temps.add(tempString);
	    		    	  }
	                }
                
	            
	            
	            // close the file
                br.close();
    			data.close();
            	}
            	lbl2.setText(String.valueOf(words));
            	String[] input = temps.toArray(new String[0]);
    			Map<String, Integer> result = new HashMap<String, Integer> ();
    			result = wordCount(input);
    			Map<String, Integer> treeMap = new TreeMap<String, Integer>(result);
    			ta.append("Start letter\tCount\n");
    			for (Map.Entry<String, Integer> entry : treeMap.entrySet()) {
    				ta.append(entry.getKey() + ": \t"+ entry.getValue() + "\n");
    				count = count + entry.getValue();
    			}
    			ta.append("Total: \t" + count + "\n");
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
}

