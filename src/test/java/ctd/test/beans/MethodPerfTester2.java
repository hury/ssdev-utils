package ctd.test.beans;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.mvel2.MVEL;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;

import ctd.util.JSONUtils;


import junit.framework.TestCase;

public class MethodPerfTester2 extends TestCase {
	private Map<String,Object> map;
	private DataBean bean;
	private final static int N = 5000000;
	
	public void setUp(){
		map = new HashMap<String,Object>();
		
		map.put("name", "sean yu");
		map.put("age", 35);
		map.put("sex", "MALE");
		map.put("payment", "100000.25");
		
		bean = new DataBean();
		
	}
	
	public void testDirect(){
		for(int i=0;i < N;i ++){
			bean.setName("sean2");
		}
		
	}
	
	public void testReflect1() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Method m = DataBean.class.getDeclaredMethod("setName", new Class<?>[]{String.class});
		
		for(int i=0;i < N;i ++){
			m.invoke(bean, "sean2");
		}
		
	}
	
	public void testReflect1b() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		
		for(int i=0;i < N;i ++){
			Method m = DataBean.class.getDeclaredMethod("setName", new Class<?>[]{String.class});
			m.invoke(bean, "sean2");
		}
		
	}
	
	public void testReflect2() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Method m = DataBean.class.getDeclaredMethod("setName", new Class<?>[]{String.class});
		m.setAccessible(true);
		for(int i=0;i < N;i ++){
			m.invoke(bean, "sean2");
		}
		
	}
	
	public void testReflect2b() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		
		for(int i=0;i < N;i ++){
			Method m = DataBean.class.getDeclaredMethod("setName", new Class<?>[]{String.class});
			m.setAccessible(true);
			m.invoke(bean, "sean2");
		}
		
	}
	
	public void testReflect2c() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Map<String,Method> methods = new HashMap<String,Method>();
		final String mn = "setName";
		
		for(int i=0;i < N;i ++){
			Method m = methods.get(mn);
			if(m == null){
				m = DataBean.class.getDeclaredMethod(mn, new Class<?>[]{String.class});
				methods.put(mn, m);
			}
			m.setAccessible(true);
			m.invoke(bean, mn);
		}
		
	}
	
	public void testReflect2d() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Map<String,Method> methods = new HashMap<String,Method>();
		final String mn = "setName";
		final String mid = "pchis.personnel.module1.setName(String,)";
		
		for(int i=0;i < N;i ++){
			Method m = methods.get(mid);
			if(m == null){
				m = DataBean.class.getDeclaredMethod(mn, new Class<?>[]{String.class});
				methods.put(mid, m);
			}
			m.setAccessible(true);
			m.invoke(bean, mn);
		}
		
	}
	
	
}

