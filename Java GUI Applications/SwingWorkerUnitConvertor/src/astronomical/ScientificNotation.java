package astronomical;
/*
 * Feb 27, 2018
 * COMP 585 Project 2
 * SwingWorker
 * Yixin Chen
 * 
 * ScientificNotation Class
 * Use scientific notation to display a large or small given double number
 */

public class ScientificNotation {
	private static double input;
	private static String result;
	
	public ScientificNotation(double number){
		input = number;
		result = toScientificNotation(input);
	}
	
	private String toScientificNotation(double number) {
		String ret = new String();
		if(number > 1){
			double temp = number / 10;
			int count = 0;
			while(temp > 1){
				count++;
				temp = temp / 10;
			}
			if(count > 5){
				String power = superscript(count);
				number = Math.round(temp*10*100.0)/100.0;
				ret = String.valueOf(number) + " × 10" + power;
//				System.out.println(ret);
			}
			else{
				ret = String.valueOf(Math.round(number*100.0)/100.0);
			}
		}
		else{
//			System.out.println(number);
			double temp = number * 10;
			int count = 0;
			while(temp < 1){
				count++;
				temp = temp * 10;
			}
			count++;
			if(count > 3){
				String power = "⁻" + superscript(count);
				number = Math.round(temp * 100.0) / 100.0;
				ret = String.valueOf(number) + " × 10" + power;
			}
			else{
				ret = String.valueOf(Math.round(number*100000.0)/100000.0);
//				System.out.println("answer: " + ret);
			}
		}
		return ret;
	}

/*	
	private static String subscript(int number) {
		String str = String.valueOf(number);
	    str = str.replaceAll("0", "₀");
	    str = str.replaceAll("1", "₁");
	    str = str.replaceAll("2", "₂");
	    str = str.replaceAll("3", "₃");
	    str = str.replaceAll("4", "₄");
	    str = str.replaceAll("5", "₅");
	    str = str.replaceAll("6", "₆");
	    str = str.replaceAll("7", "₇");
	    str = str.replaceAll("8", "₈");
	    str = str.replaceAll("9", "₉");
	    return str;
	}
*/
	private static String superscript(int number) {
		String str = String.valueOf(number);
	    str = str.replaceAll("0", "⁰");
	    str = str.replaceAll("1", "¹");
	    str = str.replaceAll("2", "²");
	    str = str.replaceAll("3", "³");
	    str = str.replaceAll("4", "⁴");
	    str = str.replaceAll("5", "⁵");
	    str = str.replaceAll("6", "⁶");
	    str = str.replaceAll("7", "⁷");
	    str = str.replaceAll("8", "⁸");
	    str = str.replaceAll("9", "⁹");         
	    return str;
	}

	public String getResult(){
		return result;
	}
	
	
}
