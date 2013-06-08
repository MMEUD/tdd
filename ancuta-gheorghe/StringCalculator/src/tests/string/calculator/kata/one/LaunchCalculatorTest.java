package tests.string.calculator.kata.one;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import string.calculator.kata.one.Calculator;
import string.calculator.kata.one.NegativeNumberException;

public class LaunchCalculatorTest {

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
}
