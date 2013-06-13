/**
 * 
 */
package com.zimbra;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * @author Ancuta Gheorghe
 *
 */
public class XmlParser {

	public static ArrayList<HashMap<String, String>> getFormatedData(String xml){
		ArrayList<HashMap<String, String>> mails = new ArrayList<HashMap<String, String>>();
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder parser = factory.newDocumentBuilder();
			Document document = parser.parse("test.xml");
			Element root = document.getDocumentElement();
			NodeList  items = root.getElementsByTagName("item");
			for (int i=0 ; i<items.getLength() ; i++) {
				HashMap<String, String> mail = new HashMap<String, String>();
				Element item = (Element)items.item(i);
				NodeList children = item.getChildNodes();
				for (int j=0 ; j<children.getLength() ; j++) {
					Node child = (Node)children.item(j);
					if (child.getNodeName() != null && !"#text".equals(child.getNodeName())){
						mail.put(child.getNodeName(), child.getTextContent());
					}
				}
				mails.add(mail);
			}
		} catch(Exception ex){
			System.out.println("Mails : DOMParser.parse() Error:" + ex.getMessage());
			ex.printStackTrace();
		}
		return mails;
	}
}
