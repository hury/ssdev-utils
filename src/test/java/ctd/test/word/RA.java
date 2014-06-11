package ctd.test.word;

import java.net.URLDecoder;

import org.apache.commons.codec.digest.DigestUtils;

public class RA {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		System.out.println(DigestUtils.md5Hex("CMHMC1"));
		System.out.println(URLDecoder.decode("%D3%C3%BB%A7%C3%FB%BB%F2%C3%DC%C2%EB%B4%ED%CE%F3"));
		
	}

}
