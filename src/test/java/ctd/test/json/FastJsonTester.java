package ctd.test.json;


import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class FastJsonTester {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Document doc = DocumentHelper.parseText("<root id='1'><name>sean</name></root>");
		
		String s = JSON.toJSONString(doc,SerializerFeature.WriteClassName);
		System.out.println(s);
		
		
		
		doc = (Document)JSON.parseObject(s);
		System.out.println(doc.asXML());
		
		
		
//		Exception e = new IllegalStateException("error");
		
//		String s = JSON.toJSONString(e,SerializerFeature.WriteClassName);
//		System.out.println(s);
//		
//		HashMap<String,Object> map = new HashMap<String,Object>();
//		map.put("exp", e);
//		String s1 = JSON.toJSONString(map,SerializerFeature.WriteClassName);
//		System.out.println(s1);
//		
//		map = JSON.parseObject(s1,HashMap.class);
//		
//		
//		//e = JSON.parseObject(s, Exception.class);
//		throw (Exception)map.get("exp");
	}

}
