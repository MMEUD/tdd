/**
 * 
 */
package tests.pattern.interpreter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pattern.interpreter.*;

/**
 * @author Ancuta Gheorghe
 *
 */
public class EvaluatorTest {

	@Test
	public void testInstanceOfGet(){
		String expression = "get parameterTest";
		Evaluator sentence = new Evaluator(expression);
		boolean check = sentence.getExpression() instanceof GetExpression;
		assertEquals(check, true);
	}
	
	@Test
	public void testInstanceOfListWithoutParameters(){
		String expression = "list";
		Evaluator sentence = new Evaluator(expression);
		boolean check = sentence.getExpression() instanceof ListExpression;
		assertEquals(check, true);
	}
	
	@Test
	public void testInstanceOfListWithParameters(){
		String expression = "list namespaceTest";
		Evaluator sentence = new Evaluator(expression);
		boolean check = sentence.getExpression() instanceof ListExpression;
		assertEquals(check, true);
	}
	
	@Test
	public void testInstanceOfLoadWithoutParameters(){
		String expression = "load";
		Evaluator sentence = new Evaluator(expression);
		boolean check = sentence.getExpression() instanceof LoadExpression;
		assertEquals(check, true);
	}
	
	@Test
	public void testInstanceOfLoadWithParameters(){
		String expression = "load namespaceTest";
		Evaluator sentence = new Evaluator(expression);
		boolean check = sentence.getExpression() instanceof LoadExpression;
		assertEquals(check, true);
	}
	
	@Test
	public void testInstanceOfNs(){
		String expression = "ns namespaceTest";
		Evaluator sentence = new Evaluator(expression);
		boolean check = sentence.getExpression() instanceof NsExpression;
		assertEquals(check, true);
	}
	
	@Test
	public void testInstanceOfSaveWithoutParameters(){
		String expression = "save";
		Evaluator sentence = new Evaluator(expression);
		boolean check = sentence.getExpression() instanceof SaveExpression;
		assertEquals(check, true);
	}
	
	@Test
	public void testInstanceOfSaveWithParameters(){
		String expression = "save namespaceTest";
		Evaluator sentence = new Evaluator(expression);
		boolean check = sentence.getExpression() instanceof SaveExpression;
		assertEquals(check, true);
	}
	
	@Test
	public void testInstanceOfSet(){
		String expression = "set parameterNameTest parameterValueTest";
		Evaluator sentence = new Evaluator(expression);
		boolean check = sentence.getExpression() instanceof SetExpression;
		assertEquals(check, true);
	}
	
}
