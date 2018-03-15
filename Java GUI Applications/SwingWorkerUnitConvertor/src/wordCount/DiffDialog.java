package wordCount;
/*
 * Feb 27, 2018
 * COMP 585 Project 2
 * Yixin Chen
 * 
 * SwingWorker
 * doInBackground Class
 * 
 * DiffDialog Class
 * Count and output the number of different words in a selected files
 * output the total number of different words for each file
 * output all the different words and their appearance count
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

public class DiffDialog extends JInternalFrame {
    private static DiffDialog instance = null;

    private JLabel lbl, lbl2;
    private JTextField tf;
    private JTextArea ta, ta2;
    private JFileChooser fc;
    private String fileName;   
    private Task task;
    private JProgressBar progressBar;
    private JFrame frame; // to properly center JDialogFrame   
    private JButton fileBtn, startButton, cancelButton;
    private List<String> textFiles = new ArrayList<String>();
    private File file;
    
    public static DiffDialog getInstance(JFrame frame) {
        if(instance == null) {
            instance = new DiffDialog(frame);
        }
        return instance;   
    }  
    
    //select a file or a directory
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
    private DiffDialog(JFrame frame) {

        super("Different Words", false, true, false, false);
        this.frame = frame;
        // init
        tf = new JTextField(30);
        tf.setEditable(false);
        //text area for listing result
        ta = new JTextArea();
		ta.setEditable(false);
		ta.setBackground(new JButton().getBackground());
		ta.setFont(new JButton().getFont());
        ta.setRows(5);
        
        ta2 = new JTextArea();
		ta2.setEditable(false);
		ta2.setBackground(new JButton().getBackground());
		ta2.setFont(new JButton().getFont());
        ta2.setRows(5);
        
        fileBtn = new JButton("...");
        startButton = new JButton("Start");
        cancelButton = new JButton("Cancel");
        lbl = new JLabel("Number of total different words in the current file: ");
        lbl2 = new JLabel();
        fc = new JFileChooser();
        progressBar = new JProgressBar(0, 100);
        //Call setStringPainted now so that the progress bar height
        //stays the same whether or not the string is shown.
        progressBar.setStringPainted(false);      
        fileName = "";    
        
        fileBtn.setPreferredSize(new Dimension(20, 20));
        startButton.setPreferredSize(new Dimension(100, 20));
        cancelButton.setPreferredSize(new Dimension(100, 20));
        
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
        upperPanel.add(cancelButton);
        
        midPanel.add(progressBar);
        
        lowerPanel.add(lbl);
        lowerPanel.add(lbl2);
        
        JScrollPane myScrollPane = new JScrollPane(ta);
        JScrollPane myScrollPane2 = new JScrollPane(ta2);
        
        mainPanel.add(upperPanel, BorderLayout.NORTH);
        mainPanel.add(midPanel, BorderLayout.CENTER);
        mainPanel.add(lowerPanel, BorderLayout.SOUTH);  
        
        add(mainPanel, BorderLayout.NORTH);
        add(myScrollPane, BorderLayout.CENTER);
        add(myScrollPane2, BorderLayout.SOUTH);

        // add filebutton listener
        fileBtn.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent ae) { 
                chooseFile();
            } 
        }); 
        
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
            	task = new Task();
            	ta.setText("File Name\tNumber of different words\n");
                task.execute();
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                task.cancel(true);
            }
        });
     
        pack();
        setLocation(50, 50);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    }
    
    //count the appearance time of words
	public static Map<String, Integer> wordCount(String[] strings) {
		  Map<String, Integer> map = new HashMap<String, Integer> ();
		  for (String s:strings) {		    
		    if (!map.containsKey(s)) {  // first time we've seen this string
		      map.put(s, 1);
		    }
		    else {
		      int count = map.get(s);
		      map.put(s, count + 1);
		    }
		  }
		  return map;	
	}
	
	//SwingWorker
	class Task extends SwingWorker<Void, String> {
		int words = 0;
        /*
        * Main task. Executed in background thread.
        */
        @Override
        protected Void doInBackground() throws Exception{
            if(fileName.equals("")) {
                JOptionPane.showMessageDialog(frame, "Choose a file!");
                return null;
            }
            if(textFiles.size() == 0){
            	JOptionPane.showMessageDialog(frame, "No text file found");
                return null;
            }
            progressBar.setIndeterminate(true);
            //read files and count words
            try {          	
            	for(int i = 0; i < textFiles.size(); i++){
            		int count = 0;
	                String line = "";
	                FileReader data = new FileReader(textFiles.get(i));
	                BufferedReader br = new BufferedReader(data);
	                ArrayList<String> temps = new ArrayList<String>();
	                while((line = br.readLine()) != null) {
	    				line = line.toString().replaceAll("[^a-zA-Z ]","");
	    		    	line = line.toLowerCase();
	    		    	StringTokenizer tokenizer = new StringTokenizer(line);
	    				
	    		    	while (tokenizer.hasMoreTokens()){
	    					String tempString = String.valueOf(tokenizer.nextToken());
	    					if(!temps.contains(tempString)){
	    						count++;
	    	                    publish(String.valueOf(count));
	    	                    Thread.sleep(1);	
	    					}
	    		    		temps.add(tempString);
	    		    	  }
	                }
	                // close the file
	                br.close();
	    			String[] input = temps.toArray(new String[0]);
	    			data.close();
	    			words = words + count;
	    			//output result in the text area
	    			if(file.isDirectory()){
	    				ta.append(textFiles.get(i).substring(fileName.length()+1, textFiles.get(i).length()) + "\t" + count + "\n");
	    				ta2.append(textFiles.get(i).substring(fileName.length()+1, textFiles.get(i).length()) + "\n");
	    			}
	    			else if(file.isFile()){
	    				ta.append(file.getName() + "\t" + count +"\n");
	    				ta2.append(file.getName() + "\n");
	    			}

	    			Map<String, Integer> result = new HashMap<String, Integer> ();
	    			result = wordCount(input);
	    			Map<String, Integer> treeMap = new TreeMap<String, Integer>(result);
	    			
	    			for (Map.Entry<String, Integer> entry : treeMap.entrySet()) {    				
	    				ta2.append(entry.getKey() + ": " + "\t" + entry.getValue() + "\n"); 
	    			}
            	}
//            	ta.append("Total words:\t" + words);
            }
            catch(FileNotFoundException ex) {
                JOptionPane.showMessageDialog(frame, "File not found!");
            }
            catch(IOException ex) {
                JOptionPane.showMessageDialog(frame, "An error occured");
            }
            return null;
        }

        @Override
        protected void process(List<String> chunks) {
        	lbl2.setText(chunks.get(chunks.size()-1));
        }

        /*
        * Executed in event dispatch thread
        */
        @Override
       protected void done() {
            progressBar.setIndeterminate(false);
            startButton.setEnabled(true);
            if (isCancelled()){
                JOptionPane.showMessageDialog(frame, "Task canceled");
                lbl2.setText("");
            }
        }
    }
}
