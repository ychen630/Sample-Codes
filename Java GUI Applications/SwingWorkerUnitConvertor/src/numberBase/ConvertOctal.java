package numberBase;

/*
 * Mar 2, 2018
 * COMP 585 Project 2
 * SwingWorker
 * Yixin Chen
 * 
 * Convert Octal class
 * Convert a Octal string to decimal, binary, and hexadecimal number
 *
 */

public class ConvertOctal {
	private int decimal;
	private String binary;
	private String octal;
	private String hexa;
	
	//constructor
	public ConvertOctal(String input){
		octal = input;
		decimal = ConvertToDecimal(input);
		hexa = ConvertToHexa(decimal);
		binary = ConvertToBinary(decimal);
	}

	private String ConvertToBinary(int input) {
		// TODO Auto-generated method stub
		return Integer.toBinaryString(input);
	}

	private String ConvertToHexa(int input) {
		// TODO Auto-generated method stub
		return Integer.toString(input,16);
	}

	private int ConvertToDecimal(String input) {
		// TODO Auto-generated method stub
		return Integer.parseInt(input, 8);
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
