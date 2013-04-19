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
			    
				/*
			    if (s.length() > 0){
			    	if (s.substring(0, 2).equals("ns")){
				    	System.out.println("Current namespace: " + s.substring(2));
				    }
			    	if (s.substring(0, 3).equals("set")){
				    	System.out.println("Current namespace: " + s.split(" ")[1] + " = " + s.split(" ")[2]);
				    }
			    }
			    */
		 	}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}

}
