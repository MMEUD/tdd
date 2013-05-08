/**
 * 
 */
package tests.pattern.interpreter;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import pattern.interpreter.SetExpression;
import pattern.singleton.Console;

/**
 * @author Ancuta Gheorghe
 *
 */
public class SetExpressionTest {

	@Before
	public void setUp() throws Exception {
		Console console = Console.getInstance();
		console.setCurrentNamespace("general");
	}

	@Test
	public final void testInterpretCommand() {
		String[] expression = {"testParam1"};
		SetExpression sExpression = new SetExpression(expression);
		assertTrue(sExpression.interpretCommand().equals("Set command is not formed properly. Correct format: set {parameter_name} {parameter_value}"));
		
		String[] expression2 = {"testParam1", "testValue1"};
		SetExpression sExpression2 = new SetExpression(expression2);
		assertTrue(sExpression2.interpretCommand().equals("general: testParam1 = testValue1"));
	}

	@Test
	public final void testValidateCommandParameters() {
		String[] expression2 = {"testParam1", "testValue1"};
		SetExpression sExpression2 = new SetExpression(expression2);
		assertTrue(sExpression2.validateCommandParameters());
		
		String[] expression3 = {"testParam1"};
		SetExpression sExpression3 = new SetExpression(expression3);
		assertFalse(sExpression3.validateCommandParameters());
	}

}
