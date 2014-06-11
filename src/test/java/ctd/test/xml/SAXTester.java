package ctd.test.xml;

import java.io.InputStream;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.DocumentException;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.dom4j.io.SAXReader;

import ctd.util.JSONUtils;

public class SAXTester implements ElementHandler{

	/**
	 * @param args
	 * @throws DocumentException 
	 */
	public static void main(String[] args) throws DocumentException {
		InputStream is = SAXTester.class.getResourceAsStream("areaCode.xml"); 
		
		SAXReader reader = new SAXReader(); 
		reader.setDefaultHandler(new SAXTester()); 
		Document doc = reader.read(is); 
		
		String path = "//items";
		List<Element> items = doc.getRootElement().selectNodes(path);
		System.out.println(items.size());
		for(Element el : items){
			System.out.println(el.asXML());
		}
	}

	@Override
	public void onStart(ElementPath elementPath) {
		//System.out.println(elementPath.getCurrent().asXML());
	}

	@Override
	public void onEnd(ElementPath elementPath) {
		
	}

}
