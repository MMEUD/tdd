package tests.pattern.utils;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pattern.utils.Parameter;

/**
 * @author Ancuta Gheorghe
 *
 */
public class ParameterTest {

	@Test
	public void testParameterName(){
		Parameter p = new Parameter("age", "27y");
		assertEquals(p.getValue(), "27y");
	}

	@Test
	public void testParameterValue(){
		Parameter p = new Parameter("age", "27y");
		assertEquals(p.getValue(), "27y");
	}
}
