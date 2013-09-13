package tdd.test.facts;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import tdd.test.Calculator;
import tdd.test.NegativeNumbersNotAllowedException;

public class CalctulatorFacts {

	private Calculator calculator;

	@Before
	public void createCalculator() {
		calculator = new Calculator();
	}
	
	@Test
	public void AddEmptyStringReturnsZero() throws NegativeNumbersNotAllowedException
	{	
		// When
		int actual = calculator.Add("");
		
		// Then
		int expected = 0;
		assertEquals(expected, actual);
	}
	
	@Test
	public void AddSingleNumberReturnsNumber() throws NegativeNumbersNotAllowedException
	{
		// When
		int actual = calculator.Add("1");
		
		// Then
		int expected = 1;
		assertEquals(expected, actual);
	}
	
	@Test
	public void AddTwoNumbersReturnsSum() throws NegativeNumbersNotAllowedException
	{
		// When
		int actual = calculator.Add("1,2");
		
		// Then
		int expected = 3;
		assertEquals(expected, actual);
	}
	
	@Test
	public void AddFiveNumbersReturnsSum() throws NegativeNumbersNotAllowedException
	{
		// When
		int actual = calculator.Add("1,2,3,4,5");
		
		// Then
		int expected = 15;
		assertEquals(expected, actual);
	}
	
	@Test
	public void NewlineDelimiterShouldBeHandled() throws NegativeNumbersNotAllowedException
	{
		// When
		int actual = calculator.Add("1,2\n3,4");
		
		// Then
		int expected = 10;
		assertEquals(expected, actual);
	}
	
	@Test
	public void CustomDelimitersShouldBeHandled() throws NegativeNumbersNotAllowedException
	{
		// When
		int actual = calculator.Add("//[']\n1'2");
		
		// Then
		int expected = 3;
		assertEquals(expected, actual);
	}
	
	@Test(expected=NegativeNumbersNotAllowedException.class)
	public void AddingNegativeNumbersResultsInException() throws NegativeNumbersNotAllowedException
	{
		// When
		calculator.Add("-1,-2");
	}
	
	@Test
	public void AddingNumbersBiggerThan1000ShouldBeIgnored() throws NegativeNumbersNotAllowedException
	{
		// When
		int actual = calculator.Add("1,2,1000");
		
		// Then
		int expected = 3;
		assertEquals(expected, actual);
	}
	
	@Test
	public void CustomDelimitersCanBeAnySize() throws NegativeNumbersNotAllowedException
	{
		// When
		int actual = calculator.Add("//[BigDelimiterHere]\n1BigDelimiterHere2BigDelimiterHere3");
		
		// Then
		int expected = 6;
		assertEquals(expected, actual);
	}
	
	@Test
	public void MultipleCustomDelimiters() throws NegativeNumbersNotAllowedException
	{
		// When
		int actual = calculator.Add("//[aa][bb]\n1aa2bb3aa4");
		
		// Then
		int expected = 10;
		assertEquals(expected, actual);
	}
	
}
