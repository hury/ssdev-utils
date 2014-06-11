package ctd.util.converter.support;


import java.util.List;

import org.springframework.core.convert.converter.Converter;

import ctd.util.JSONUtils;

@SuppressWarnings("rawtypes")
public class StringToList implements Converter<String,List> {
	
	@Override
	public List convert(String source) {
		return JSONUtils.parse(source, List.class);
	}

}
