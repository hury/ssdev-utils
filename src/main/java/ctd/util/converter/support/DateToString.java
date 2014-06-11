package ctd.util.converter.support;

import java.util.Date;


import org.joda.time.format.DateTimeFormat;
import org.springframework.core.convert.converter.Converter;

public class DateToString implements Converter<Date,String> {
	
    private final static  String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
  
	@Override
	public String convert(Date source) {
		return DateTimeFormat.forPattern(DATETIME_FORMAT).print(source.getTime());
	}

}
