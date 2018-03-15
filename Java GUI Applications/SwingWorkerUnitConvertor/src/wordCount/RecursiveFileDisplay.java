package wordCount;

/*
 * Mar 3, 2018
 * COMP 585 Project 2
 * SwingWorker
 * Yixin Chen
 * 
 * RecursiveFlieDisplay Class
 * Traverse all the sub folders of a specific folder recursively
 * and find all the txt files in this folder
 *
 */

import java.util.List;

//import javax.swing.JOptionPane;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class RecursiveFileDisplay {
	private List<String> textFiles = new ArrayList<String>();
	
	//constructor
	public RecursiveFileDisplay(File file){
		displayDirectoryContents(file);
	}
	
	//recursively traverse the directory dir
	private void displayDirectoryContents(File dir) {
		if(dir.isFile()){//if dir is a file, check if it is a txt file and return 
			try{
				if(dir.getName().endsWith(".txt")){
					textFiles.add(dir.getCanonicalPath());
				}
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		else{
			try {
				File[] files = dir.listFiles();//list all files
				for (File file : files) {
					if (file.isDirectory()) {
						displayDirectoryContents(file);//directory, recursively visit
					} 
					else {
						if(file.getName().endsWith(".txt")){
							textFiles.add(file.getCanonicalPath());//file, add to result
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public List<String> getTextFiles(){
		return textFiles;
	}
}
