/**
 * 
 */
package com.moodmedia.storeportal.zimbra.archive;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;

/**
 * @author Ancuta Gheorghe
 *
 */
public class ZipArchive {

	public static void saveZipToDisk(String saveTo, InputStream inputStream) 
			throws FileNotFoundException, IOException {
		FileOutputStream fileOutputStream = new FileOutputStream(saveTo + "ancuta_263.zip");
		byte[] b = new byte[1024];
		int count;
		while ((count = inputStream.read(b)) >= 0) {
		    fileOutputStream.write(b, 0, count);
		}
		fileOutputStream.flush(); 
		fileOutputStream.close();
	}
			
}
