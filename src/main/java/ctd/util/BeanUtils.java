package ctd.util;

import org.dozer.DozerBeanMapper;
import org.mvel2.MVEL;


public class BeanUtils {
	private final static DozerBeanMapper dozer = new DozerBeanMapper();
	
	
	public static <T> T map(Object source, Class<T> destinationClass) {
		
		return dozer.map(source, destinationClass);
		
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T map(Object source, Object dest) {
		dozer.map(source, dest);
		return (T)dest;
	}
	
	public static void copy(Object source, Object dest) {
		dozer.map(source, dest);
	}
	
	public static Object getProperty(Object bean,String nm) {
		Object val = null;
		val = MVEL.getProperty(nm, bean);
		return val;
	}
	
	public static void setProperty(Object bean,String nm,Object v) {
		MVEL.setProperty(bean, nm, v);
	}
	
	
}
