package tests.string.calculator.kata.one;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LaunchCalculatorTest {

	@Test public void 
	test_add_result() {
		Calculator calculator = new Calculator();
		assertEquals(3, calculator.add("1,2"));
	}

}
