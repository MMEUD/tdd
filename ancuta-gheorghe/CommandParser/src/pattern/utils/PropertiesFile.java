/**
 * 
 */
package pattern.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pattern.strategy.Context;
import pattern.strategy.OrderMap;

/**
 * @author ancuta
 *
 */
public class PropertiesFile {
	
	//de generalizat sau externalizat.
	//private static String propertiesDir = "C:\\Users\\ancuta\\Documents\\GitHub\\tdd\\ancuta-gheorghe\\CommandParser\\properties";
	private static String propertiesDir = "C:\\Users\\Ancuta Gheorghe\\Documents\\GitHub\\tdd\\ancuta-gheorghe\\CommandParser\\properties";

	public static void saveToFile(String namespaceName, ArrayList<Parameter> parameters){
		Writer output;
		try {
			output = new BufferedWriter(new FileWriter(propertiesDir + "\\" + namespaceName + ".properties", true));
			for (Parameter paramTemp: parameters){
				output.append(paramTemp.getName() + "=" + paramTemp.getValue() + "\n");
			}
			output.close();
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
	}
	
	public static Map<String, String> loadFile(String namespaceName){
		BufferedReader br = null;
		Map<String, String> paramsTemp = new HashMap<String, String>();
		try {
			br = new BufferedReader(new FileReader(PropertiesFile.propertiesDir + "\\" + namespaceName + ".properties"));
		    String line = br.readLine();
	        while (line != null) {
	        	paramsTemp.put(line.split("=")[0], line.split("=")[1]);
	        	line = br.readLine();
	        }
	        br.close();
	    } catch (Exception e) {
		}
	    Context context = new Context(new OrderMap());
	    return context.executeStrategy(paramsTemp);
	}
	
	public static ArrayList<String> getFiles(){
		File fTemp = new File(PropertiesFile.propertiesDir);
		ArrayList<String> files = new ArrayList<String>();
		for (final File fileEntry : fTemp.listFiles()) {
	            files.add(fileEntry.getName().split("\\.")[0]);
	    }
		return files;
	}

}
