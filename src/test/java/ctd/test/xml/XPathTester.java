package ctd.test.xml;

import java.io.IOException;
import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import ctd.util.xml.XMLHelper;

public class XPathTester {
	public static void main(String[] args) throws DocumentException, IOException {
		InputStream is = SAXTester.class.getResourceAsStream("areaCode.xml"); 
		Document doc = XMLHelper.getDocument(is);
		Element el = (Element)doc.getRootElement().selectSingleNode("//item[@key='330201']");
		System.out.println(el.getPath());
	}
}
