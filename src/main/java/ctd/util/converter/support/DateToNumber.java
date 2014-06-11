package ctd.util.converter.support;

import java.util.Date;

import org.springframework.core.convert.converter.Converter;

public class DateToNumber implements Converter<Date,Number> {

	@Override
	public Number convert(Date source) {
		return source.getTime();
	}

}
