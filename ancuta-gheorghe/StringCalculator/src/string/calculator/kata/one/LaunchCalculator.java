package string.calculator.kata.one;

import tests.string.calculator.kata.one.Calculator;

public class LaunchCalculator {

	
	public static void main(String[] args){
		Calculator calculator = new Calculator();
		System.out.println(calculator.add(""));
		System.out.println(calculator.add("1"));
		System.out.println(calculator.add("1,1\n1"));
	}
}
