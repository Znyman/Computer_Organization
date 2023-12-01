import java.util.Scanner;

public class BinaryCalculator
{
	private static String arg1, operator, arg2;
	private static boolean[] result, remainder, product, quotient, firstNumberBinary, secondNumberBinary;
	
	public static void main(String[] args)
	{
		System.out.println("Welcome to the BinaryCalculator");
		Scanner in = new Scanner(System.in);
		while(true)
		{
			if(in.hasNext())
			{
				arg1 = in.next();
				if(arg1.equalsIgnoreCase("QUIT"))
				{
					break;
				}
			}
			else
			{
				break;
			}
			if(in.hasNext())
			{
				operator = in.next();
			}
			if(in.hasNext())
			{
				arg2 = in.next();
			}
			if(arg1.length() != arg2.length())
			{
				System.err.println("ERROR: '" + arg1 + "' and '" + arg2 + "' are not the same length.");
				continue;
			}
			if(arg1.length() > 64 || arg2.length() > 64)
			{
				System.err.println("ERROR: '" + arg1 + "' and/or '" + arg2 + "' are greater than 64 bits.");
				continue;
			}
			
			firstNumberBinary = new boolean[arg1.length()];
			secondNumberBinary = new boolean[arg2.length()];
			
			for(int i = firstNumberBinary.length-1; i >= 0 ; i--)
			{
				if(arg1.charAt(i) == '0')
				{
					firstNumberBinary[i] = false;
				}
				if(arg1.charAt(i) == '1')
				{
					firstNumberBinary[i] = true;
				}
				if(arg2.charAt(i) == '0')
				{
					secondNumberBinary[i] = false;
				}
				if(arg2.charAt(i) == '1')
				{
					secondNumberBinary[i] = true;
				}
			}
			System.out.print(convert(firstNumberBinary) +  " " + operator + " " + convert(secondNumberBinary) + " = ");
			
			if(operator.equals("+"))
			{
				addition(firstNumberBinary, secondNumberBinary);
				System.out.print(convert(result));
				System.out.println();
			}
			if(operator.equals("-"))
			{
				subtraction(firstNumberBinary, secondNumberBinary);
				System.out.print(convert(result));
				System.out.println();
			}
			if(operator.equals("*"))
			{
				multiplication(firstNumberBinary, secondNumberBinary);
				System.out.print(convert(product));
				System.out.println();
			}
			if(operator.equals("/"))
			{
				division(firstNumberBinary, secondNumberBinary);
				if(convert(secondNumberBinary) == 0)
				{
					System.out.print("ERROR");
					System.out.println();
				}
				else
				{
					System.out.print(convert(quotient) + "R" + convertRemainder(remainder));
					System.out.println();
				}
			}
		}
		in.close();
	}
	
	public static long convert(boolean[] numberBinary)
	{
		long numberDecimal = 0;
		long powerOfTwo = 1;
		
		if(!numberBinary[0])
		{
			for(int i = arg1.length()-1; i >= 0; i--)
			{
				if(numberBinary[i])
				{
					numberDecimal += powerOfTwo;
				}
				powerOfTwo *= 2;
			}
		}
		if(numberBinary[0])
		{
			for(int i = arg1.length()-1; i >= 0; i--)
			{
				if(numberBinary[i])
				{
					numberDecimal += powerOfTwo;
				}
				powerOfTwo *= 2;
			}
			numberDecimal += (~powerOfTwo)+1;
		}
		return numberDecimal;
	}
	public static long convertRemainder(boolean[] numberBinary)
	{
		long numberDecimal = 0;
		long powerOfTwo = 1;
		
		if(!numberBinary[0])
		{
			for(int i = numberBinary.length-1; i >= 0; i--)
			{
				if(numberBinary[i])
				{
					numberDecimal += powerOfTwo;
				}
				powerOfTwo *= 2;
			}
		}
		if(numberBinary[0])
		{
			for(int i = numberBinary.length-1; i >= 0; i--)
			{
				if(numberBinary[i])
				{
					numberDecimal += powerOfTwo;
				}
				powerOfTwo *= 2;
			}
			numberDecimal += (~powerOfTwo)+1;
		}
		return numberDecimal;
	}
	public static boolean[] chop(boolean[] numberBinary)
	{
		boolean[] choppedBinary = new boolean[numberBinary.length];
		for(int i = numberBinary.length/2; i < numberBinary.length; i++)
		{
			choppedBinary[i-numberBinary.length/2] = numberBinary[i];
		}
		return choppedBinary;
	}
	public static boolean[] negate(boolean[] numberBinary)
	{
		boolean[] negatedBinary = new boolean[numberBinary.length];
		boolean carry = false;
		//flip the bits
		for(int i = numberBinary.length-1; i >= 0; i--)
		{
			if(numberBinary[i])
			{
				negatedBinary[i] = false;
			}
			else
			{
				negatedBinary[i] = true;
			}
		}
		//add 1
		if(!negatedBinary[numberBinary.length-1])
		{
			negatedBinary[numberBinary.length-1] = true;
		}
		else
		{
			negatedBinary[numberBinary.length-1] = false;
			carry = true;
			for(int i = numberBinary.length-2; i >= 0; i--)
			{
				if(carry)
				{
					if(negatedBinary[i])
					{
						negatedBinary[i] = false;
						carry = true;
					}
					else
					{
						negatedBinary[i] = true;
						carry = false;
					}
				}
			}
		}
		return negatedBinary;
	}
	public static boolean[] shiftLeft(boolean[] numberBinary)
	{
		boolean[] shiftedNumberBinary = new boolean[numberBinary.length];
		for(int i = numberBinary.length-1; i >= 0; i--)
		{
			shiftedNumberBinary[i] = numberBinary[i];
		}
		//shift left and set rightmost bit to 0
		for(int j = 0; j < shiftedNumberBinary.length-1; j++)
		{
			shiftedNumberBinary[j] = shiftedNumberBinary[j+1];
		}
		shiftedNumberBinary[shiftedNumberBinary.length-1] = false;
		return shiftedNumberBinary;
	}
	public static boolean[] shiftRight(boolean[] numberBinary)
	{
		boolean[] shiftedNumberBinary = new boolean[numberBinary.length];
		for(int i = numberBinary.length-1; i >= 0; i--)
		{
			shiftedNumberBinary[i] = numberBinary[i];
		}
		//shift right and set leftmost bit to 0
		for(int j = shiftedNumberBinary.length-1; j > 0; j--)
		{
			shiftedNumberBinary[j] = shiftedNumberBinary[j-1];
		}
		shiftedNumberBinary[0] = false;
		return shiftedNumberBinary;
	}
	public static boolean[] addition(boolean[] numberOneBinary, boolean[] numberTwoBinary)
	{
		boolean carry = false;
		result = new boolean[numberOneBinary.length];
		
		for(int i = numberOneBinary.length-1; i >= 0; i--)
		{
			if(!carry)
			{
				if(numberOneBinary[i] && numberTwoBinary[i])
				{
					result[i] = false;
					carry = true;
				}
				if(numberOneBinary[i] && !numberTwoBinary[i])
				{
					result[i] = true;
					carry = false;
				}
				if(!numberOneBinary[i] && numberTwoBinary[i])
				{
					result[i] = true;
					carry = false;
				}
				if(!numberOneBinary[i] && !numberTwoBinary[i])
				{
					result[i] = false;
					carry = false;
				}
			}
			
			else
			{
				if(numberOneBinary[i] && numberTwoBinary[i])
				{
					result[i] = true;
					carry = true;
				}
				if(numberOneBinary[i] && !numberTwoBinary[i])
				{
					result[i] = false;
					carry = true;
				}
				if(!numberOneBinary[i] && numberTwoBinary[i])
				{
					result[i] = false;
					carry = true;
				}
				if(!numberOneBinary[i] && !numberTwoBinary[i])
				{
					result[i] = true;
					carry = false;
				}
			}
		}
		return result;
	}
	public static boolean[] subtraction(boolean[] numberOneBinary, boolean[] numberTwoBinary)
	{
		boolean[] negatedBinary = new boolean[numberOneBinary.length];
		result = new boolean[numberOneBinary.length];
		negatedBinary = negate(numberTwoBinary);
		result = addition(numberOneBinary, negatedBinary);
		return result;
	}
	public static boolean[] multiplication(boolean[] numberOneBinary, boolean[] numberTwoBinary)
	{
		boolean[] multiplicand = new boolean[numberOneBinary.length*2];//he said make *2 (fix)
		boolean[] multiplier = new boolean[numberTwoBinary.length];
		boolean[] tempProduct = new boolean[numberOneBinary.length*2];
		product = new boolean[numberOneBinary.length*2];//Product
		
		for(int i = tempProduct.length-1; i >= 0; i--)
		{
			tempProduct[i] = false;
			multiplicand[i] = false;
		}
		for(int i = multiplier.length-1; i >= 0; i--)
		{
			multiplicand[i+(numberOneBinary.length)] = numberOneBinary[i];
			multiplier[i] = numberTwoBinary[i];
		}
		
		for(int i = numberOneBinary.length-1; i >= 0; i--)
		{
			if(multiplier[numberOneBinary.length-1])
			{
				tempProduct = addition(tempProduct, multiplicand);
				multiplicand = shiftLeft(multiplicand);
				multiplier = shiftRight(multiplier);
			}
			else
			{
				multiplicand = shiftLeft(multiplicand);
				multiplier = shiftRight(multiplier);
			}
		}
		product = chop(tempProduct);
		return product;
	}
	public static boolean[] division(boolean[] numberOneBinary, boolean[] numberTwoBinary)
	{
		boolean[] divisor = new boolean[numberOneBinary.length*2];
		boolean[] tempRemainder = new boolean[numberTwoBinary.length*2];//Initialized with dividend
		boolean[] tempQuotient = new boolean[numberOneBinary.length];
		remainder = new boolean[numberTwoBinary.length*2];
		quotient = new boolean[numberOneBinary.length];
		
		for(int i = (numberOneBinary.length*2)-1; i >= 0; i--)
		{
			divisor[i] = false;
			tempRemainder[i] = false;
		}
		for(int i = 0; i < numberOneBinary.length; i++)
		{
			tempRemainder[i+numberOneBinary.length] = numberOneBinary[i];
			tempQuotient[i] = false;
			divisor[i] = numberTwoBinary[i];
		}
		
		for(int j = 0; j <= numberOneBinary.length; j++)
		{
			tempRemainder = subtraction(tempRemainder, divisor);
			if(!tempRemainder[0])
			{
				//shift quotient left and set rightmost bit to 1
				for(int i = 0; i < numberOneBinary.length-1; i++)
				{
					tempQuotient[i] = tempQuotient[i+1];
				}
				tempQuotient[numberOneBinary.length-1] = true;
			}
			else
			{
				tempRemainder = addition(tempRemainder, divisor);
				tempQuotient = shiftLeft(tempQuotient);
			}
			divisor = shiftRight(divisor);
		}
		for(int i = tempRemainder.length-1; i >= 0; i--)
		{
			remainder[i] = tempRemainder[i];
		}
		for(int i = tempQuotient.length-1; i >= 0; i--)
		{
			quotient[i] = tempQuotient[i];
		}
		return quotient;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

