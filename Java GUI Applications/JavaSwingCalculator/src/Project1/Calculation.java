/*
 * Feb 6, 2018
 * COMP 585 Project 1
 * Yixin Chen
 * 
 * Calculation class
 * 
 * First check the validation of a given expression using regular expressions
 * If the expression input is valid, then the program calculates the result of the expression input
 * 
 * Can process the following operators:
 * +, -, *, /, %, mod, √, ², and ⁻¹
 * 
 * Precedence of operators:
 * √ > (², ⁻¹, %) > mod > (*, /) > (+, -)
 *
 */

package Project1;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

public class Calculation {

	private double result;
	private String expression;
	private boolean validExpression;
	
	public Calculation(String input)
	{
		expression = input;
		if(validExpression(expression))
		{
			validExpression = true;
			result = Calculate(input);		
		}
		else
		{
			result = 0;
			validExpression = false;
		}
	}
	
	public double getResult()
	{
		return result;
	}
	
	public boolean getValidation()
	{
		return validExpression;
	}
	
	public void setExpression(String input)
	{
		expression = input;
	}
	
	public String getExpression()
	{
		return expression;
	}
	
	public void setValidation(boolean boo)
	{
		validExpression = boo;
	}
	
	//validate the expression input
	private boolean validExpression(String text) 
	{
		//regular expression for a valid expression
		Pattern pattern1 = Pattern.compile("^((((\\-)?√+)|(\\-))?\\d+(\\.\\d+)?(²|%|⁻¹)*([\\+\\-\\*\\/]|(mod)){1})*(((\\-)?√+)|(\\-))?\\d+(\\.\\d+)?(²|%|⁻¹)*$");
		Matcher matcher1 = pattern1.matcher(text);
		if(!matcher1.matches())
		{
			JOptionPane.showMessageDialog(null, "Malformed expression! Input again.");
			return false;
		}
		
		//regular expression for checking whether 0 is a divisor
		Pattern pattern2 = Pattern.compile("((\\/|(mod))0+(\\.0*)?(\\D|$))|((\\D|^)(0\\.0*)?0+⁻¹)");
        Matcher matcher2 = pattern2.matcher(text);
        if(matcher2.find())
        {
			JOptionPane.showMessageDialog(null, "Zero cannot be the divisor! Input again.");
			return false;
		}		
		return true;
	}
	
	//check whether 0 is the divisor during the calculation process 
	private boolean dividedByZero(double x){
		if(x == 0){
			this.setValidation(false);
			JOptionPane.showMessageDialog(null, "Zero cannot be the divisor! Input again.");
			return true;
		}
		return false;
	}
	
	
	double Calculate(String input)
	{
        // Create stacks for operators and operands
        Stack<Integer> op  = new Stack<Integer>();
        Stack<Double> val = new Stack<Double>();
        
        // Create temporary stacks for operators and operands
        Stack<Integer> optmp  = new Stack<Integer>();
        Stack<Double> valtmp = new Stack<Double>();
 	
		//if the first number is negative, add 0 in front
		if(input.charAt(0) == '-')
		{
			input = "0" + input;
		}
		
		//treat all operator - as negative sign
		input = input.replaceAll("-","+-");

        //variables
        String temp = "";    //temporary variable
		int sqrtCount = 0;   //number of consecutive √ operators
		int divisionCount = 0;  //number of consecutive / operators
		String modTemp = "";    //temp variable for mod operation
		int modCount = 0;		//number of consecutive mod operators
		
		// Store operands and operators in respective stacks
        for (int i = 0;i < input.length();i++)
        {
            char ch = input.charAt(i);
            if (ch == '-')
            {
                temp = '-' + temp; //negtive sign
            }
            else if(ch == 'o' || ch == 'd'|| ch == '¹')
			{
            	//skip these three chars
			}
            else if (ch != '+' && ch != '*' && ch != '/' && ch != '⁻' && ch != '%' &&  ch != '√' && ch != '²' && ch != 'm')
            {
               temp = temp + ch; //digits
            }
            else
            {
            	//process %
				if(ch == '%')
				{
					temp = String.valueOf(Double.parseDouble(temp)/100);
				}
				//process ²
				else if(ch == '²')
				{
					if(temp.charAt(0) != '-'){
						temp = String.valueOf(Double.parseDouble(temp)*Double.parseDouble(temp));
					}
					else{
						temp = String.valueOf(-Double.parseDouble(temp)*Double.parseDouble(temp));
					}
				}
				//process ⁻¹
				else if(ch == '⁻')
				{
					temp = String.valueOf(1/Double.parseDouble(temp));
				}
				//process mod
				else if(ch == 'm')
				{
					if(modCount == 0){
						modTemp = temp;
						temp = "";
						modCount++;
					}
					else if(modCount > 0){
						modTemp = String.valueOf(Double.parseDouble(modTemp) % Double.parseDouble(temp));
						temp = "";
					}
				}
				//process /
				else if(ch == '/'){
					if(modCount > 0){
						temp = String.valueOf(Double.parseDouble(modTemp) % Double.parseDouble(temp));
						if(dividedByZero(Double.parseDouble(temp))){
							return 0;
						}
						modCount = 0;
					}
					ch = '*';
					op.push((int)ch);
					if(divisionCount == 0){
						val.push(Double.parseDouble(temp));			
						temp ="";
					}
					else{
						if(dividedByZero(Double.parseDouble(temp))){
							return 0;
						}
						val.push(1/Double.parseDouble(temp));			
						temp ="";						
					}
					divisionCount++;
				}
				//process √
				else if(ch == '√')
				{
					sqrtCount++;
				}
				else
				{
					//process √
					if((sqrtCount > 0) && (temp != ""))
					{
						while(sqrtCount > 0){
							temp = String.valueOf(Math.sqrt(Double.parseDouble(temp)));
							sqrtCount--;
						}
					}
					//process /
					if((sqrtCount == 0) && (divisionCount > 0) && (modCount == 0)){
						if(dividedByZero(Double.parseDouble(temp))){
							return 0;
						}
						temp = String.valueOf(1/Double.parseDouble(temp));
						divisionCount = 0;
					}
					//process mod
					if(sqrtCount == 0 && modCount > 0){
						temp = String.valueOf(Double.parseDouble(modTemp) % Double.parseDouble(temp));
						modCount = 0;
					}
					
					if(sqrtCount == 0  && divisionCount == 0 && modCount == 0)
					{		
						val.push(Double.parseDouble(temp));
						op.push((int)ch);
						temp = "";
					}
				}
            }
        }
		
        //evaluate consecutive √ operators
		if((sqrtCount > 0) && (temp != ""))
		{
				while(sqrtCount > 0)
				{
						temp = String.valueOf(Math.sqrt(Double.parseDouble(temp)));
						sqrtCount--;
				}
		}
		
		if(modCount > 0 && temp != ""){
			temp = String.valueOf(Double.parseDouble(modTemp) % Double.parseDouble(temp));
			modCount = 0;
		}
		
		if(divisionCount > 0 && temp != ""){
			if(dividedByZero(Double.parseDouble(temp))){
				return 0;
			}
			temp = String.valueOf(1/Double.parseDouble(temp));
			divisionCount = 0;
		}

		
        val.push(Double.parseDouble(temp));
        // Create char array of operators as per precedence
        char operators[] = {'*', '+'};
        
        // Evaluation of expression
        for (int i = 0; i < 2; i++)
        {
            boolean it = false;
            while (!op.isEmpty())
            {
                int optr = op.pop();
                double v1 = val.pop();
                double v2 = val.pop();
                if (optr == operators[i])
                {
                    // if operator matches evaluate and store in temporary stack               
                	if (i == 0)
                    {
                        valtmp.push(v2 * v1);
                        it = true;
                        break;
                    }
                    else if (i == 1)
                    {
                        valtmp.push(v2 + v1);
                        it = true;
                        break;
                    }
                }
                else
                {
                    valtmp.push(v1);
                    val.push(v2);
                    optmp.push(optr);
                }                
            }    
            // Push back all elements from temporary stacks to main stacks            
            while (!valtmp.isEmpty())
                val.push(valtmp.pop());
            while (!optmp.isEmpty())
                op.push(optmp.pop());
            // Iterate again for same operator
            if (it)
                i--;                            
        }
        
		double result = val.pop();
		return result;				
	}
}
