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

public class MethodPerfTester extends TestCase {
	private Map<String,Object> map;
	private DataBean bean;
	private final static int N = 900000;
	
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
	
	public void testReflect2() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Method m = DataBean.class.getDeclaredMethod("setName", new Class<?>[]{String.class});
		
		m.setAccessible(true);
		for(int i=0;i < N;i ++){
			m.invoke(bean, "sean2");
		}
		
	}
	
	
	public void testMethodHandler() throws Throwable{
		MethodHandles.Lookup lookup = MethodHandles.lookup();
		MethodType type = MethodType.methodType(void.class, new Class<?>[]{String.class});
		MethodHandle methodHandle = lookup.findVirtual(DataBean.class, "setName", type);
		
		for(int i=0;i < N;i ++){
			methodHandle.invokeExact(bean,"sean2");
		}
		
	}
	
	public void testFastMethod() throws Throwable{
		FastClass fc = FastClass.create(DataBean.class);
		FastMethod fm = fc.getMethod("setName", new Class<?>[]{String.class});
		
		for(int i=0;i < N;i ++){
			fm.invoke(bean, new Object[]{"sean2"});
			
		}
	}
	
}

