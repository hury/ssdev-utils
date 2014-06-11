package ctd.util;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import ctd.util.store.ActiveStore;
import ctd.util.store.StoreFactory;

public class AppContextHolder implements ApplicationContextAware{
	public static final String SESSION_FACTORY = "mySessionFactory";
	
	protected static String domain;
	protected static String host;
	protected static ApplicationContext appContext;
	protected static ActiveStore store;
	
	private static CountDownLatch storeInitCountDownLatch = new CountDownLatch(1);
	
	public static void setName(String name){
		domain = name;
	}
	
	public static String getName(){
		return domain;
	}
	
	public static String getHost(){
		return host;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext ctx)throws BeansException {
		appContext = ctx;
	}
	
	public static Object getBean(String beanName){
		return appContext.getBean(beanName);
	}
	
	public static void removeBean(String beanName){
		DefaultListableBeanFactory acf = (DefaultListableBeanFactory) appContext.getAutowireCapableBeanFactory();
		if(acf.containsBean(beanName)){
			acf.removeBeanDefinition(beanName);
		}
	}
	
	public static void addBean(String beanName,Class<?> clz,HashMap<String,Object> properties){
		DefaultListableBeanFactory acf = (DefaultListableBeanFactory) appContext.getAutowireCapableBeanFactory();
		if(acf.containsBean(beanName)){
			acf.removeBeanDefinition(beanName);
		}
		BeanDefinitionBuilder  bd = BeanDefinitionBuilder.rootBeanDefinition(clz);
		Set<String> names = properties.keySet();
		for(String nm : names){
			bd.addPropertyValue(nm, properties.get(nm));
		}
		acf.registerBeanDefinition(beanName, bd.getBeanDefinition());
	}
	
	public static boolean containBean(String beanName){
		return appContext.containsBean(beanName);
	}
	
	public static <T> T getBean(String beanName,Class<T> type){
		return appContext.getBean(beanName,type);
	}
	
	public static void setActiveStoreAddress(String address){
		if(!StringUtils.isEmpty(address)){
			int i = address.lastIndexOf("/");
			String serverAddress = address.substring(i + 1);
			
			store = StoreFactory.createStore(serverAddress);
		}
		storeInitCountDownLatch.countDown();
	}
	
	public static ActiveStore getActiveStore(){
		try {
			storeInitCountDownLatch.await();
		} 
		catch (InterruptedException e) {
			
		}
		return store;
	}
}
