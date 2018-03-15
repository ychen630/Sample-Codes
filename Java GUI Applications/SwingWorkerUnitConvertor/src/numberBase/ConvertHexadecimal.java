package numberBase;

/*
 * Mar 2, 2018
 * COMP 585 Project 2
 * SwingWorker
 * Yixin Chen
 * 
 * Convert hexadecimal class
 * Convert a hexadecimal string to decimal, octal, and binary number
 *
 */

public class ConvertHexadecimal {
	private int decimal;
	private String binary;
	private String octal;
	private String hexa;
	
	//constructor
	public ConvertHexadecimal(String input){
		hexa = input;
		decimal = ConvertToDecimal(input);
		octal = ConvertToOctal(decimal);
		binary = ConvertToBinary(decimal);
	}

	private String ConvertToBinary(int input) {
		// TODO Auto-generated method stub
		return Integer.toBinaryString(input);
	}

	private String ConvertToOctal(int input) {
		// TODO Auto-generated method stub
		return Integer.toString(input,8);
	}

	private int ConvertToDecimal(String input) {
		// TODO Auto-generated method stub
		return Integer.parseInt(input, 16);
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
	public String getBinary(){
		return binary;
	}
}
