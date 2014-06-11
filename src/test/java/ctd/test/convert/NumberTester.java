package ctd.test.convert;

import ctd.util.converter.ConversionUtils;

public class NumberTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Number n = ConversionUtils.convert("12", Number.class);
		System.out.println(n.getClass().getName());

	}

}
