/**
 * 
 */
package tests.string.calculator.kata.one;

/**
 * @author ancuta
 *
 */
public class Calculator {

	public int add(String string) {
		String[] numbers = string.split(",|\n");
		int result = 0;
		for (String number: numbers){
			result += Integer.parseInt((number!=null&&!"".equals(number))?number:"0");
		}
		return result;
	}

}
