package numberBase;

/*
 * Mar 2, 2018
 * COMP 585 Project 2
 * SwingWorker
 * Yixin Chen
 * 
 * Convert Binary class
 * Convert a binary string to decimal, octal, and hexadecimal number
 *
 */

public class ConvertBinary {
	private int decimal;
	private String binary;
	private String octal;
	private String hexa;
	
	//constructor
	public ConvertBinary(String input){
		binary = input;
		decimal = ConvertToDecimal(input);
		octal = ConvertToOctal(decimal);
		hexa = ConvertToHexa(decimal);
	}
	
	private String ConvertToHexa(int input) {
		// TODO Auto-generated method stub
		return Integer.toString(input,16);
	}

	private String ConvertToOctal(int input) {
		// TODO Auto-generated method stub
		return Integer.toString(input,8);
	}

	private int ConvertToDecimal(String input) {
		// TODO Auto-generated method stub
		return Integer.parseInt(input, 2);
	}
	
	public int getDecimal(){
		return decimal;
	}
	
	public String getOctal(){
		return octal;
	}
	public String getHexa(){
		return hexa;
	}
}
