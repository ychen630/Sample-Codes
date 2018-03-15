/*
 * Feb 8, 2018
 * COMP 585 Project 1
 * Yixin Chen
 * 
 * Main class
 *
 */

package Project1;

import javax.swing.JFrame;

public class Projet1 {

	public static void main(String[] args) {
		JFrame frame = new CalculatorFrame();
		frame.setTitle("Java Swing Calculator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
	}

}
