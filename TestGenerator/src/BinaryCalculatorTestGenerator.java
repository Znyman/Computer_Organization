import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Random;

public class BinaryCalculatorTestGenerator
{
	private static final int NUM_INPUTS = 500;
	private static final int MAX_BITS = 56;
	private static final String[] OPERATORS = {"+", "-", "*", "/"};
	private static Random RAND = new Random(0);

	public static void main(String[] args) throws Exception
	{
		PrintWriter commandFile = new PrintWriter(new FileWriter("commands.txt"));
		PrintWriter expectedFile = new PrintWriter(new FileWriter("results.txt"));
		expectedFile.println("Welcome to the BinaryCalculator");

		for(int i=0; i<NUM_INPUTS; i++){
			// pick the number of bits to use.
			int n_bits = RAND.nextInt(MAX_BITS)+1; // add 1 because zero bits is not valid

			String arg1 = randomBitString(n_bits);
			String arg2 = randomBitString(n_bits);

			for(String op : OPERATORS){
				String cmd = arg1 + " " + op + " " + arg2;
				System.out.println("IN: " + cmd);
				commandFile.println(cmd);
				String result = s2l(arg1) + " " + op + " " + s2l(arg2) + " = " + computeResult(arg1, op, arg2);
				System.out.println("OUT: " + result);
				expectedFile.println(result);
			}
		}
		commandFile.close();
		expectedFile.close();
	}

	private static String computeResult(String arg1, String op, String arg2)
	{
		long l1 = s2l(arg1);
		long l2 = s2l(arg2);
		long l3, r = 0;
		switch(op){
		case "+":
			l3 = l1 + l2;
			break;
		case "-":
			l3 = l1 - l2;
			break;
		case "*":
			l3 = l1 * l2;
			break;
		default:
			if(l2 == 0){
				return "ERROR";
			}
			l3 = l1 / l2;
			r = l1 % l2;
		}
		l3 = trimBits(l3, arg1.length());
		if(op.equals("/")){
			return l3 + "R" + r;
		}
		else {
			return "" + l3;
		}
	}

	private static long trimBits(long l, int length)
	{
		long out = 0;
		long value = 1;
		for(int i=0; i<length-1; i++){
			if((l & 0x1) == 0x1){
				out += value;
			}
			l = l >>> 1;
		    value += value;
		}
		// now check the sign bit
		if((l & 0x1) == 0x1){
			out -= value;
		}
		return out;
	}

	private static long s2l(String binary)
	{
		long out = 0;
		long value = 1;
		char[] chars = binary.toCharArray();
		for(int i=chars.length-1; i>=0; i--){
			if(chars[i] == '1'){
				if(i == 0){
					out -= value;
				}
				else {
					out += value;
				}
			}
			value += value; // double it
		}
		return out;
	}

	private static String randomBitString(int n_bits)
	{
		StringBuilder sb = new StringBuilder(n_bits);
		for(int i=0; i<n_bits; i++){
			if(RAND.nextBoolean()){
				sb.append('1');
			}
			else {
				sb.append('0');
			}
		}
		return sb.toString();
	}
}
