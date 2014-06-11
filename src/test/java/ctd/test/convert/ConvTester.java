package ctd.test.convert;

import java.util.HashMap;
import java.util.Map;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import ctd.util.converter.ConversionUtils;

public class ConvTester {

	private String id;
	private String name;
	private int age;
	private String clz;
	private HashMap<String,Object> properties = new HashMap<String,Object>();


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	
	public static void main(String[] args) {
		Element el = DocumentHelper.createElement("root");
		el.addAttribute("id", "001");
		el.addAttribute("name", "Sean");
		el.addAttribute("age", "32");
		el.addAttribute("age1", "32");
		
		ConvTester o = ConversionUtils.convert(el, ConvTester.class);
		System.out.println(o.getAge() + "," + o.getId());
		
		
		HashMap<String,Object> data = new HashMap<String,Object>();
		data.put("name", "Sophie");
		ConvTester o2 = ConversionUtils.convert(data, ConvTester.class);
		System.out.println(o2.getName());
		
		Element el2 = ConversionUtils.convert(o, Element.class);
		System.out.println(el2.asXML());
	}

	public String getClz() {
		return clz;
	}
	
	public HashMap<String,Object> getProperties(){
		return null;
	}

}
