package numberBase;

/*
 * Mar 2, 2018
 * COMP 585 Project 2
 * SwingWorker
 * Yixin Chen
 * 
 * Convert decimal class
 * Convert a decimal number to binary, octal, and hexadecimal string
 *
 */

public class ConvertDecimal {
	private int decimal;
	private String binary;
	private String octal;
	private String hexa;
	
	//constructor
	public ConvertDecimal(int input){
		decimal = input;
		binary = ConvertToBinary(input);
		octal = ConvertToOctal(input);
		hexa = ConvertToHexa(input);
	}

	private String ConvertToHexa(int input) {
		return Integer.toString(input,16);
	}

	private String ConvertToOctal(int input) {
		return Integer.toString(input,8);
	}

	private String ConvertToBinary(int input) {
		return Integer.toBinaryString(input);
	}
	
	public String getBinary(){
		return binary;
	}
	
	public String getOctal(){
		return octal;
	}
	public String getHexa(){
		return hexa;
	}
	
}
