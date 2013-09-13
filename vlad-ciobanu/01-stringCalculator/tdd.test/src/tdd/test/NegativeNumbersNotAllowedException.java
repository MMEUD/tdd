package tdd.test;

public class NegativeNumbersNotAllowedException extends Exception {

	private String message;
	
	public NegativeNumbersNotAllowedException(String[] individualNumbers) {
		message = "negatives not allowed: ";
		
		for(String stringNumber : individualNumbers) {
			int number = Integer.parseInt(stringNumber);
			if(number < 0)
				message += stringNumber + ", ";
		}
	}
	
	@Override
	public String getMessage() {
		return message;
	}

	private static final long serialVersionUID = 1L;

}
