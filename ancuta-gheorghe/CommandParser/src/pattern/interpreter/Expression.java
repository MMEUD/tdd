package pattern.interpreter;

/**
 * 
 */

/**
 * @author Ancuta Gheorghe
 *
 */
public abstract class Expression {

	public abstract String interpretCommand();
	
	public abstract boolean validateCommandParameters();
	
}
