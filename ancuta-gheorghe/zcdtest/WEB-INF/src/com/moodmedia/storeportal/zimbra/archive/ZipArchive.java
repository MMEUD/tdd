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
	
	public static void unzip() {
		String archive = "D:\\zcs\\ancuta_263.zip";
		File baseFolder= new File("D:\\zcs\\ancuta_263");
	    FileInputStream fin;
	    try {
	      fin = new FileInputStream(archive);
	      ZipInputStream zin = new ZipInputStream(fin);
	      ZipEntry ze = null;
	      while ((ze = zin.getNextEntry()) != null) {
	          File destinationFile = new File(baseFolder, ze.getName());
	          unpackEntry(destinationFile, zin);
	      }
	      zin.close();
	    } catch (FileNotFoundException e) {
	      e.printStackTrace();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	  }

	  private static void unpackEntry(File destinationFile, ZipInputStream zin) {
	    createParentFolder(destinationFile);
	    FileOutputStream fout = null;
	    try {
	      fout = new FileOutputStream(destinationFile);
	      for (int c = zin.read(); c != -1; c = zin.read()) {
    	    fout.write(c);
    	  }
    	  zin.closeEntry();
    	  fout.close();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }

	  }

	  private static void createParentFolder(File destinationFile) {
	    File parent = new File(destinationFile.getParent());
	    parent.mkdirs();
	  }
	
	//---------------------------------
	
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
	
	public static void unzipToDisk(File fileZip, CustomRequest customRequest)
	{
		//File f= new File("D:\\zcs\\ancuta_263.zip");
		String fName = "D:\\zcs\\ancuta_263.zip";
	    byte[] buf = new byte[1024];
	    ZipInputStream zinstream = null;
		try {
			zinstream = new ZipInputStream(
			    new FileInputStream(fName));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    ZipEntry zentry = null;
		try {
			zentry = zinstream.getNextEntry();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    System.out.println("Name of current Zip Entry : " + zentry + "\n");
	    while (zentry != null) {
	      String entryName = zentry.getName();
	      System.out.println("Name of  Zip Entry : " + entryName);
	      FileOutputStream outstream = null;
		try {
			outstream = new FileOutputStream("D:\\zcs\\");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      int n;

	      try {
			while ((n = zinstream.read(buf, 0, 1024)) > -1) {
			    outstream.write(buf, 0, n);

			  }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      System.out.println("Successfully Extracted File Name : "
	          + entryName);
	      try {
			outstream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	      try {
			zinstream.closeEntry();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      try {
			zentry = zinstream.getNextEntry();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    }
	    try {
			zinstream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

}
	    /*ZipInputStream zipInputStream = null;
	    try {
	        zipInputStream = new ZipInputStream(new FileInputStream(f));
	        ZipEntry zipEntry = zipInputStream.getNextEntry();
	        byte[] buffer = new byte[(int) zipEntry.getSize()];
	        FileOutputStream fileOutputStream = new FileOutputStream(customRequest.getUsername() + "_" + customRequest.getIdMail());
	        while ((zipInputStream.read(buffer))>0){
	            fileOutputStream.write(buffer);
	        }
	        fileOutputStream.flush();
	        fileOutputStream.close();
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally 
	    {
	        if (zipInputStream != null) 
	        {
	            try { zipInputStream.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }*/
	
}
