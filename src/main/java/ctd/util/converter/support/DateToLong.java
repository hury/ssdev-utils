package ctd.util.converter.support;

import java.util.Date;

import org.springframework.core.convert.converter.Converter;

public class DateToLong implements Converter<Date,Long> {

	@Override
	public Long convert(Date source) {
		return source.getTime();
	}

}
