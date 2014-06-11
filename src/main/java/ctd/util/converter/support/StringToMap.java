package ctd.util.converter.support;

import java.util.Map;
import org.springframework.core.convert.converter.Converter;
import ctd.util.JSONUtils;

@SuppressWarnings("rawtypes")
public class StringToMap implements Converter<String,Map> {
	
	@Override
	public Map convert(String source) {
		return JSONUtils.parse(source, Map.class);
	}

}
