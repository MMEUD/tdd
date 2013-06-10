package tests.string.calculator.kata.one;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import string.calculator.kata.one.Calculator;

public class LaunchCalculatorTest {

	/*
	@Test public void 
	test_add_result_for_empty_string() throws Throwable {
		Calculator calculator = new Calculator();
		assertEquals(0, calculator.add(""));
	}
	
	@Test public void 
	test_add_result_for_one_number_string() throws Throwable {
		Calculator calculator = new Calculator();
		assertEquals(1, calculator.add("1"));
	}
	
	@Test public void 
	test_add_result_for_two_numbers_string() throws Throwable {
		Calculator calculator = new Calculator();
		assertEquals(2, calculator.add("-\n1-1"));
	}
	
	@Test public void 
	test_add_result_for_seven_numbers_string() throws Throwable {
		Calculator calculator = new Calculator();
		assertEquals(7, calculator.add(",\n1,1,1,1,1,1,1"));
	}
	
	@Test public void 
	test_add_result_for_seven_numbers_string_different_delimiter() throws Throwable {
		Calculator calculator = new Calculator();
		assertEquals(7, calculator.add(":\n1:1:1:1:1:1:1"));
	}
	
	//TODO: clarify how this test should be done!
	//@Test public void 
	//test_add_result_for_negative_numbers_string() throws Throwable {
	//	Calculator calculator = new Calculator();
	//	assertEquals(NegativeNumberException.showMessage(" -1"), calculator.add(";\n1;-1"));
	//}
	
	@Test public void 
	test_add_result_for_numbers_greater_than_1000_numbers() throws Throwable {
		Calculator calculator = new Calculator();
		assertEquals(3, calculator.add(":\n1:1:1:1001"));
		assertEquals(1003, calculator.add(":\n1:1:1:1000"));
	}
	*/
	
	@Test public void 
	test_add_result_for_numbers_greater_than_1000_numbers() throws Throwable {
		Calculator calculator = new Calculator();
		assertEquals(3, calculator.add("//[***]\n1***2***1001"));
		assertEquals(1003, calculator.add("//[***]\n1***2***1000"));
	}
	
}
