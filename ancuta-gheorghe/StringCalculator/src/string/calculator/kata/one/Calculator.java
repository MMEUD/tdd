/**
 * 
 */
package string.calculator.kata.one;

/**
 * @author ancuta
 *
 */
public class Calculator {

	private static final int SECOND_POSITION_IN_INPUT = 1;
	private static final int FIRST_POSITION_IN_INPUT = 0;
	private static final String DEFAULT_VALUE_FOR_EMTPY_INPUT = "0";
	private static final String NEW_LINE = "\n";
	private static final String DEFAULT_DELIMITER = ";";

	public int add(String stringOfDelimiterAndNumbers) {
		String[] numbers = null;
		if (stringOfDelimiterAndNumbers.split(NEW_LINE).length > 1){
			String delimiter = stringOfDelimiterAndNumbers.split(NEW_LINE)[FIRST_POSITION_IN_INPUT];
			numbers = stringOfDelimiterAndNumbers.split(NEW_LINE)[SECOND_POSITION_IN_INPUT].split(delimiter);
		} else {
			numbers = stringOfDelimiterAndNumbers.split(NEW_LINE)[FIRST_POSITION_IN_INPUT].split(DEFAULT_DELIMITER);
		}
		int sum = 0;
		for (String number: numbers){
			sum += Integer.parseInt((number!=null&&!"".equals(number))?number:DEFAULT_VALUE_FOR_EMTPY_INPUT);
		}
		return sum;
	}

}
