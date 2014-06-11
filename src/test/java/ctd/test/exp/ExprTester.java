package ctd.test.exp;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Stack;

import javassist.CannotCompileException;
import javassist.NotFoundException;

import ognl.Ognl;
import ognl.OgnlException;

import org.joda.time.LocalDate;
import org.mvel2.MVEL;
import org.mvel2.compiler.CompiledExpression;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;

import ctd.util.JSONUtils;
import ctd.util.context.Context;
import ctd.util.context.ContextUtils;
import ctd.util.context.beans.DateBean;
import ctd.util.context.beans.JVMStatBean;
import ctd.util.converter.ConversionUtils;
import ctd.util.exp.ExpressionContextBean;
import ctd.util.exp.ExpressionProcessor;
import ctd.util.exp.exception.ExprException;

public class ExprTester {

	/**
	 * @param args
	 * @throws ExprException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws OgnlException 
	 * @throws CannotCompileException 
	 * @throws NotFoundException 
	 */
	public static void main(String[] args) throws ExprException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, OgnlException, NotFoundException, CannotCompileException {
		final ApplicationContext appContext = new ClassPathXmlApplicationContext("classpath:/ctd/util/exp/spring-expr-base.xml");
		ExpressionProcessor processor = (ExpressionProcessor) appContext.getBean("exprProcessor");
		
		ContextUtils.put("name", "sean");
		ContextUtils.put("date", new DateBean());
		ContextUtils.put("jvm", new JVMStatBean());
		ContextUtils.put("a", 5.5);
		ContextUtils.put("b", 8);
		
		ExpressionContextBean bean = new ExpressionContextBean();
		bean.setForPreparedStatement(true);
		
		ContextUtils.put("$exp", bean);
		
		
		String exp1 = "['eq',['$','name'],['$','%name']] ";
		
		
		System.out.println(processor.toString(exp1));
		System.out.println(ContextUtils.get("$exp.statementParameters"));
		bean.clearPatameters();
		
		exp1 = "['and'," +
					"['like',['$','name'],['$','%name']]," +
					"['eq',['$','age'],['$','%b']]" +
				"]";
		
		
		System.out.println(processor.toString(exp1));
		System.out.println(ContextUtils.get("$exp.statementParameters"));
		
	}

}
