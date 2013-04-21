/**
 * 
 */
package pattern.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ancuta
 *
 */
public class PropertiesFile {
	
	//de generalizat sau externalizat.
	private static String propertiesDir = "C:\\Users\\ancuta\\Documents\\GitHub\\tdd\\ancuta-gheorghe\\CommandParser\\properties";

	public void saveToFile(String namespaceName){
		
	}
	
	public static ArrayList<Map<String, String>> loadFile(String namespaceName){
		BufferedReader br = null;
		ArrayList<Map<String, String>> paramsTemp = new ArrayList<Map<String, String>>();
		try {
			br = new BufferedReader(new FileReader(PropertiesFile.propertiesDir + "\\" + namespaceName + ".properties"));
		    String line = br.readLine();
	        Map<String, String> paramTemp = null;
	        while (line != null) {
	        	paramTemp = new HashMap<String, String>();
	        	paramTemp.put(line.split("=")[0], line.split("=")[1]);
	        	paramsTemp.add(paramTemp);
	            line = br.readLine();
	        }
	        br.close();
	    } catch (Exception e) {
		}
		return paramsTemp;
	}
	
	public static ArrayList<String> getFiles(){
		File fTemp = new File(PropertiesFile.propertiesDir);
		ArrayList<String> files = new ArrayList<String>();
		for (final File fileEntry : fTemp.listFiles()) {
	            files.add(fileEntry.getName().split("\\.")[0]);
	    }
		return files;
	}
	
	public static void main(String[] args){
		//PropertiesFile.getFiles();
	}
}
