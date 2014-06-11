package ctd.util.converter.support;

import java.util.Date;

import org.springframework.core.convert.converter.Converter;

public class LongToDate implements Converter<Long,Date> {

	@Override
	public Date convert(Long source) {
		return new Date(source);
	}

}
