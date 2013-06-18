/**
 * 
 */
package com.moodmedia.storeportal.zimbra.parser;

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

import com.moodmedia.storeportal.zimbra.connection.CustomRequest;

/**
 * @author Ancuta Gheorghe
 *
 */
public class InboxParser extends AParser {

	public InboxParser(CustomRequest customRequest) {
		super(customRequest);
	}

	public ArrayList<HashMap<String, Object>> processData(String xml){
		ArrayList<HashMap<String, Object>> mails = new ArrayList<HashMap<String, Object>>();
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder parser = factory.newDocumentBuilder();
			InputSource is = new InputSource(); 
			is.setCharacterStream(new StringReader(xml)); 
			Document document = parser.parse(is); 
			Element root = document.getDocumentElement();
			NodeList  items = root.getElementsByTagName("m");
			for (int i=0 ; i<items.getLength() ; i++) {
				Element item = (Element)items.item(i);
				HashMap<String, Object> mail = new HashMap<String, Object>();
				mail.put("read", (item.hasAttribute("f")&&"u".equals(item.getAttribute("f")))?"no":"yes");
				mail.put("hasAttachment", (item.hasAttribute("f")&&"a".equals(item.getAttribute("f")))?"yes":"no");
				mail.put("d", item.getAttribute("d"));
				mail.put("id", item.getAttribute("id"));
				mail.put("cid", item.getAttribute("cid"));
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
