package string.calculator.kata.one;


public class LaunchCalculator {

	
	public static void main(String[] args){
		Calculator calculator = new Calculator();
		OutputWriter.printSum(calculator.add(""));
		OutputWriter.printSum(calculator.add(";\n1;2"));
		OutputWriter.printSum(calculator.add("-\n1-1-5"));
	}
}
