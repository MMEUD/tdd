package tests.string.calculator.kata.one;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import string.calculator.kata.one.Calculator;

public class LaunchCalculatorTest {

	@Test public void 
	test_add_result_for_empty_string() {
		Calculator calculator = new Calculator();
		assertEquals(0, calculator.add(""));
	}
	
	@Test public void 
	test_add_result_for_one_number_string() {
		Calculator calculator = new Calculator();
		assertEquals(1, calculator.add("1"));
	}
	
	@Test public void 
	test_add_result_for_two_numbers_string() {
		Calculator calculator = new Calculator();
		assertEquals(2, calculator.add("-\n1-1"));
	}
	
	@Test public void 
	test_add_result_for_seven_numbers_string() {
		Calculator calculator = new Calculator();
		assertEquals(7, calculator.add(",\n1,1,1,1,1,1,1"));
	}
	
	@Test public void 
	test_add_result_for_seven_numbers_string_different_delimiter() {
		Calculator calculator = new Calculator();
		assertEquals(7, calculator.add(":\n1:1:1:1:1:1:1"));
	}
}
