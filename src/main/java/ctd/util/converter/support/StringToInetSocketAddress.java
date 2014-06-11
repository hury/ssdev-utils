package ctd.util.converter.support;

import java.net.InetSocketAddress;

import org.springframework.core.convert.converter.Converter;

import ctd.util.NetUtils;

public class StringToInetSocketAddress implements Converter<String,InetSocketAddress> {
	
	@Override
	public InetSocketAddress convert(String source) {
		return NetUtils.toAddress(source);
	}

}
