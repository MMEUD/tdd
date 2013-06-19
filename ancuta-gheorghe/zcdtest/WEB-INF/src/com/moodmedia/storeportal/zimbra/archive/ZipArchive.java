/**
 * 
 */
package com.moodmedia.storeportal.zimbra.archive;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.moodmedia.storeportal.zimbra.connection.CustomRequest;

/**
 * @author Ancuta Gheorghe
 *
 */
public class ZipArchive {
	
	public static String saveZipToDisk(String saveTo, InputStream inputStream, CustomRequest customRequest) 
			throws FileNotFoundException, IOException {
		String pathToZip = saveTo + customRequest.getUsername() + "_" + customRequest.getIdMail() + ".zip";
		FileOutputStream fileOutputStream = new FileOutputStream(pathToZip);
		byte[] b = new byte[1024];
		int count;
		while ((count = inputStream.read(b)) >= 0) {
		    fileOutputStream.write(b, 0, count);
		}
		fileOutputStream.flush(); 
		fileOutputStream.close();
		return pathToZip;
	}
	
	public static void unZipToDisk(File archive, CustomRequest customRequest) {
		File baseFolder= new File("D:\\zcs\\" + customRequest.getUsername() + "_" + customRequest.getIdMail());
	    FileInputStream fileInputStream;
	    try {
	    	fileInputStream = new FileInputStream(archive);
	    	ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
	    	ZipEntry zipEntry = null;
	    	while ((zipEntry = zipInputStream.getNextEntry()) != null) {
	    		File destinationFile = new File(baseFolder, zipEntry.getName());
	    		unpackEntry(destinationFile, zipInputStream);
	    	}
	    	zipInputStream.close();
	    	deleteZip(customRequest);
	    } catch (FileNotFoundException e) {
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	  }

	  private static void unpackEntry(File destinationFile, ZipInputStream zipInputStream) {
		  createParentFolder(destinationFile);
		  FileOutputStream fileOutputStream = null;
		  try {
			  fileOutputStream = new FileOutputStream(destinationFile);
			  for (int i = zipInputStream.read(); i != -1; i = zipInputStream.read()) {
				  fileOutputStream.write(i);
			  }
    	  zipInputStream.closeEntry();
    	  fileOutputStream.close();
		  } catch (IOException e) {
			  e.printStackTrace();
		  }
	  }

	  private static void createParentFolder(File destinationFile) {
		  File parent = new File(destinationFile.getParent());
		  parent.mkdirs();
	  }
	  
	  private static void deleteZip(CustomRequest customRequest){
		  File file= new File("D:\\zcs\\" + customRequest.getUsername() + "_" + customRequest.getIdMail() + ".zip");
		  file.delete();
			
	  }

}
