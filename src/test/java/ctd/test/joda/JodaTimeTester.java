package ctd.test.joda;

import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.PeriodFormat;

public class JodaTimeTester {

	public static void main(String[] args) {
		DateTime dt1 = new DateTime();
		String s =DateTimeFormat.forPattern("EEEE, dd MMMM yyyy HH:mm:ss 'GMT'").withLocale(Locale.ENGLISH).print(dt1);
		System.out.println(s);
		DateTime dt = DateTimeFormat.forPattern("EEEE, dd MMMM yyyy HH:mm:ss 'GMT'").withLocale(Locale.ENGLISH).parseDateTime("Fri, 26 Jul 2013 11:16:23 GMT");
		System.out.println(dt);
		
		Period p = new Period(dt1,dt);
		System.out.println(p.getYears());
		
		System.out.println(DateTimeFormat.forPattern("yyyyMMddHHmmss").print(dt));
	}

}
