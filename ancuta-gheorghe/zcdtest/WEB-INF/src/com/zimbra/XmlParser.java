/**
 * 
 */
package com.zimbra;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


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
			InputSource is = new InputSource(); 
			is.setCharacterStream(new StringReader(correctXml(xml))); 
			Document document = parser.parse(is); 
			Element root = document.getDocumentElement();
			NodeList  items = root.getElementsByTagName("m");
			for (int i=0 ; i<items.getLength() ; i++) {
				Element item = (Element)items.item(i);
				HashMap<String, String> mail = new HashMap<String, String>();
				mail.put("f", item.hasAttribute("f")?"no":"yes");
				mail.put("d", item.getAttribute("d"));
				NodeList children = item.getChildNodes();
				for (int j=0 ; j<children.getLength() ; j++) {
					Node child = (Node)children.item(j);
					if ("e".equals(child.getNodeName())){
						NamedNodeMap eChild = child.getAttributes();
						Node sender = eChild.getNamedItem("a");
						mail.put(sender.getNodeName(), sender.getNodeValue());
					} else {
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
