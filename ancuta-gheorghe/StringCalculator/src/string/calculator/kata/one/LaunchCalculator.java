package string.calculator.kata.one;


public class LaunchCalculator {

	
	public static void main(String[] args) throws Throwable{
		Calculator calculator = new Calculator();
		OutputWriter.printSum(calculator.add(""));
		OutputWriter.printSum(calculator.add(";\n1;2"));
		OutputWriter.printSum(calculator.add(";\n-1;-7;5"));
	}
}
