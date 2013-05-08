/**
 * 
 */
package tests.pattern.interpreter;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import pattern.interpreter.NsExpression;
import pattern.interpreter.NsExpression;
import pattern.interpreter.SetExpression;
import pattern.singleton.Console;

/**
 * @author Ancuta Gheorghe
 *
 */
public class NsExpressionTest {

	@Before
	public void setUp() throws Exception {
		Console console = Console.getInstance();
		console.setCurrentNamespace("general");
	}

	@Test
	public final void testInterpretCommand() {
		
		String[] expression = {"testParam1", "testParam2"};
		NsExpression nsExpression = new NsExpression(expression);
		assertTrue(nsExpression.interpretCommand().equals("Ns command is not formed properly. Correct format: ns {namespace}"));
		
		String[] expression2 = {"testParam1"};
		NsExpression nsExpression2 = new NsExpression(expression2);
		assertTrue(nsExpression2.interpretCommand().equals("Current namespace: testParam1"));
	}

	@Test
	public final void testValidateCommandParameters() {
		String[] expression = {"testParam1", "testParam2"};
		NsExpression nsExpression = new NsExpression(expression);
		assertFalse(nsExpression.validateCommandParameters());
		
		String[] expression2 = {"testParam1"};
		NsExpression nsExpression2 = new NsExpression(expression2);
		assertTrue(nsExpression2.validateCommandParameters());
		
		String[] expression3 = {};
		NsExpression nsExpression3 = new NsExpression(expression3);
		assertFalse(nsExpression3.validateCommandParameters());
	}

}
