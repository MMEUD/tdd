package string.calculator.kata.one;

/**
 * @author ancuta
 *
 */

public class NegativeNumberException extends Throwable{

	private static final long serialVersionUID = 6559266230547082691L;
	
	//TODO: clarify what this function should return
	public static Throwable showMessage(String negativeNumbers){
		System.out.println("Negatives not allowed " + negativeNumbers);
		Throwable throwable = new Throwable(); 
		return throwable;
	}

}
