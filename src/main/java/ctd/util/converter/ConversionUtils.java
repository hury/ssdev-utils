package ctd.util.converter;


import java.util.Set;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

import ctd.util.converter.support.DateToLong;
import ctd.util.converter.support.DateToNumber;
import ctd.util.converter.support.DateToString;
import ctd.util.converter.support.DocumentToString;
import ctd.util.converter.support.ElementToObject;
import ctd.util.converter.support.ElementToString;
import ctd.util.converter.support.LongToDate;
import ctd.util.converter.support.MapToObject;
import ctd.util.converter.support.ObjectToElement;
import ctd.util.converter.support.ObjectToMap;
import ctd.util.converter.support.StringToDate;
import ctd.util.converter.support.StringToDocument;
import ctd.util.converter.support.StringToElement;
import ctd.util.converter.support.StringToInetSocketAddress;
import ctd.util.converter.support.StringToList;
import ctd.util.converter.support.StringToMap;

public class ConversionUtils {
	private static ConfigurableConversionService conversion = new DefaultConversionService();
	
	static {
		conversion.addConverter(new LongToDate());
		conversion.addConverter(new DateToLong());
		conversion.addConverter(new DateToNumber());
		conversion.addConverter(new DateToString());
		conversion.addConverter(new StringToDate());
		conversion.addConverter(new StringToMap());
		conversion.addConverter(new StringToList());
		conversion.addConverter(new StringToDocument());
		conversion.addConverter(new StringToElement());
		conversion.addConverter(new StringToInetSocketAddress());
		conversion.addConverter(new DocumentToString());
		conversion.addConverter(new ElementToString());
		conversion.addConverter(new ElementToObject());
		conversion.addConverter(new ObjectToElement());
		conversion.addConverter(new MapToObject());
		conversion.addConverter(new ObjectToMap());
	}
	
	@SuppressWarnings("rawtypes")
	public void setConverters(Set<Converter> converters) {
		for(Converter c : converters){
			conversion.addConverter(c);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T convert(Object source, Class<T> targetType){
		if(targetType.isInstance(source)){
			return (T)source;
		}
		return conversion.convert(source, targetType);
	}
	
	public static boolean canConvert(Class<?> sourceType, Class<?> targetType){
		return conversion.canConvert(sourceType, targetType);
	}
	
}

