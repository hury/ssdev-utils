package ctd.test.beans;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.mvel2.MVEL;

import ctd.util.JSONUtils;


import junit.framework.TestCase;

public class PerfTester extends TestCase {
	private Map<String,Object> map;
	private DataBean bean;
	private final static int N = 300000;
	
	public void setUp(){
		map = new HashMap<String,Object>();
		
		map.put("name", "sean yu");
		map.put("age", 35);
		map.put("sex", "MALE");
		map.put("payment", "100000.25");
		
		bean = new DataBean();
		
	}
	
	public void testMvel(){
		for(int i = 0; i < N; i ++){
			Set<String> keys = map.keySet();
			for(String k : keys){
				MVEL.setProperty(bean, k, map.get(k));
			}
		}
		
		
		
	}
	
	public void testBeanUtils() throws IllegalAccessException, InvocationTargetException{
		for(int i = 0; i < N; i ++){
			Set<String> keys = map.keySet();
			for(String k : keys){
				BeanUtils.setProperty(bean, k, map.get(k));
				//MVEL.setProperty(bean, k, map.get(k));
			}
		}
		
		
	}
	
	public void testDirect(){
		for(int i = 0; i < N; i ++){
			bean.setName((String)map.get("name"));
			bean.setAge((int)map.get("age"));
			bean.setPayment(Double.parseDouble((String)map.get("payment")));
			bean.setSex((String)map.get("sex"));
		}
		
		
	}

	
}

