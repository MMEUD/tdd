package console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import pattern.interpreter.Evaluator;
import pattern.singleton.Console;


/**
 * 
 */

/**
 * @author Ancuta Gheorghe
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Console console = Console.getInstance();
		console.setNamespace("general");
		System.out.println("Current namespace: general\n");
		
		
		while (true){
			try{
			    BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			    String expression = bufferRead.readLine();
			    
			    Evaluator sentence = new Evaluator(expression);
				String result = sentence.interpret();
				System.out.println(result);
			  
		 	}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}

}
