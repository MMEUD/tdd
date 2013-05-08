/**
 * 
 */
package tests.pattern.interpreter;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import pattern.interpreter.GetExpression;
import pattern.interpreter.SetExpression;
import pattern.singleton.Console;

/**
 * @author Ancuta Gheorghe
 *
 */
public class GetExpressionTest {

	@Before
	public void setUp() throws Exception {
		Console console = Console.getInstance();
		console.setCurrentNamespace("general");
		String[] expression = {"testParam1", "testValue1"};
		new SetExpression(expression);
	}

	@Test
	public final void testInterpretCommand() {
		String[] expression = {"testParam1", "testParam2"};
		GetExpression gExpression = new GetExpression(expression);
		assertTrue(gExpression.interpretCommand().equals("Get command is not formed properly. Correct format: get {parameter_name}"));
		
		String[] expression2 = {"testParam1"};
		GetExpression gExpression2 = new GetExpression(expression2);
		assertTrue(gExpression2.interpretCommand().equals("general: testParam1 = testValue1"));
		
		String[] expression3 = {"testParam2"};
		GetExpression gExpression3 = new GetExpression(expression3);
		assertTrue(gExpression3.interpretCommand().equals("general: this parameter does not exist."));
	}

	@Test
	public final void testValidateCommandParameters() {
		String[] expression = {"testParam1", "testParam2"};
		GetExpression gExpression = new GetExpression(expression);
		assertFalse(gExpression.validateCommandParameters());
		
		String[] expression2 = {"testParam1"};
		GetExpression gExpression2 = new GetExpression(expression2);
		assertTrue(gExpression2.validateCommandParameters());
		
		String[] expression3 = {};
		GetExpression gExpression3 = new GetExpression(expression3);
		assertFalse(gExpression3.validateCommandParameters());
	}

}
