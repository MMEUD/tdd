/**
 * 
 */
package tests.pattern.utils;


import org.junit.Before;
import org.junit.Test;

import pattern.utils.Parameter;

import static org.junit.Assert.assertEquals;

/**
 * @author Ancuta Gheorghe
 *
 */
public class ParameterTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void testParameterBuild(){
		Parameter p = new Parameter("age", "27y");
		assertEquals("The elements are equal.", p.getName(), "age");
		
	}

}
