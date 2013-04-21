package pattern.interpreter;

/**
 * 
 */

/**
 * @author Ancuta Gheorghe
 *
 */
public abstract class Expression {

	public abstract void interpretCommand();
	
	public abstract boolean validateCommandParameters();
	
}
