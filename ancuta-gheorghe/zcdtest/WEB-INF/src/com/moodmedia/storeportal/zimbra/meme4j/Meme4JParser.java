package com.moodmedia.storeportal.zimbra.meme4j;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.james.mime4j.message.BinaryBody;
import org.apache.james.mime4j.message.BodyPart;
import org.apache.james.mime4j.message.Entity;
import org.apache.james.mime4j.message.Message;
import org.apache.james.mime4j.message.Multipart;
import org.apache.james.mime4j.message.TextBody;
import org.apache.james.mime4j.parser.Field;

/**
 *
 * @author Denis Lunev <den@mozgoweb.com>
 */

public class Meme4JParser {

    private StringBuffer txtBody;
    private StringBuffer htmlBody;
    private ArrayList<BodyPart> attachments;

    /**
     *
     * @param fileName
     */
    public ArrayList<HashMap<String, Object>> parseMessage(String fileName) {
    	ArrayList<HashMap<String, Object>> mails = new ArrayList<HashMap<String, Object>>();
    	HashMap<String, Object> mail = new HashMap<String, Object>();
        FileInputStream fis = null;

        txtBody = new StringBuffer();
        htmlBody = new StringBuffer();
        attachments = new ArrayList<BodyPart>();

        try {
            //Get stream from file
            fis = new FileInputStream(fileName);
            //Create message with stream from file
            //If you want to parse String, you can use:
            //Message mimeMsg = new Message(new ByteArrayInputStream(mimeSource.getBytes()));
            Message mimeMsg = new Message(fis);

            //Get some standard headers
            mail.put("to", mimeMsg.getTo().toString());
            mail.put("from", mimeMsg.getFrom().toString());
            mail.put("subject", mimeMsg.getSubject());

            //Get custom header by name
            Field priorityFld = mimeMsg.getHeader().getField("X-Priority");
            //If header doesn't found it returns null
            if (priorityFld != null) {
                //Print header value
            	mail.put("priority", priorityFld.getBody());
            }

            //If message contains many parts - parse all parts
            if (mimeMsg.isMultipart()) {
            	Multipart multipart = (Multipart) mimeMsg.getBody();
                parseBodyParts(multipart);
            } else {
                //If it's single part message, just get text body
                String text = getTxtPart(mimeMsg);
                txtBody.append(text);
            }

            //Print text and HTML bodies
            mail.put("body", txtBody.toString());
            //System.out.println("Html body: " + htmlBody.toString());
            
            int i = 1;
            for (BodyPart attach : attachments) {
            	String attName = attach.getFilename();
            	System.out.println(attName);
            	mail.put("attach" + i, attName);
            	i++;
                //Create file with specified name
                FileOutputStream fos = new FileOutputStream(attName);
                try {
                    //Get attach stream, write it to file
                    BinaryBody bb = (BinaryBody) attach.getBody();
                    bb.writeTo(fos);
                } finally {
                    fos.close();
                }
            }

        } catch (IOException ex) {
            ex.fillInStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        mails.add(mail);
        return mails;
    }

    /**
     * This method classifies bodyPart as text, html or attached file
     *
     * @param multipart
     * @throws IOException
     */
    private void parseBodyParts(Multipart multipart) throws IOException {
        for (BodyPart part : multipart.getBodyParts()) {
            if (part.isMimeType("text/plain")) {
                String txt = getTxtPart(part);
                txtBody.append(txt);
            } else if (part.isMimeType("text/html")) {
                String html = getTxtPart(part);
                htmlBody.append(html);
            } else if (part.getDispositionType() != null && !part.getDispositionType().equals("")) {
                //If DispositionType is null or empty, it means that it's multipart, not attached file
                attachments.add(part);
            }

            //If current part contains other, parse it again by recursion
            if (part.isMultipart()) {
                parseBodyParts((Multipart) part.getBody());
            }
        }
    }

    /**
     *
     * @param part
     * @return
     * @throws IOException
     */
    private String getTxtPart(Entity part) throws IOException {
        //Get content from body
        TextBody tb = (TextBody) part.getBody();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        tb.writeTo(baos);
        return new String(baos.toByteArray());
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {

        String eml = "C:\\Users\\Ancuta Gheorghe\\Documents\\GitHub\\tdd\\ancuta-gheorghe\\EmlParser\\src\\267.eml";
        
        Meme4JParser parser = new Meme4JParser();
        ArrayList<HashMap<String, Object>> mails = parser.parseMessage(eml);
        for (HashMap<String, Object> mail: mails){
			System.out.println("To: " + mail.get("to") + "<br>");
			System.out.println("From: " + mail.get("from") + "<br>");
			System.out.println("Subject: " + mail.get("subject") + "<br>");
			System.out.println("Body: " + mail.get("body").toString().replace("\\\n", "<br>") + "<br>");
			int i = 1;
			while (mail.get("attach" + i) != null){
				System.out.println("Attachment" + i + ": " + mail.get("attach" + i) + "<br>");
				i++;
			}
			System.out.println("<a href='http://localhost:8080/zcd/mail'>Back</a><br>");
			System.out.println("<br>");
		}
        
    }
}