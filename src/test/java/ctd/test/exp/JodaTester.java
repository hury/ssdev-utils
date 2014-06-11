package ctd.test.exp;

import java.util.Date;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.ISODateTimeFormat;

public class JodaTester {

	public JodaTester() {
		// TODO Auto-generated constructor stub
	}
	
	
	public static void main(String[] args){
		String DATETIME3_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
		
		String s = "1980-02-19T16:00:00.000Z";
		Date d = ISODateTimeFormat.dateTime().parseDateTime(s).toDate();
		System.out.println(d);
		
		d = DateTimeFormat.forPattern(DATETIME3_FORMAT).withZoneUTC().parseDateTime("1982-08-31T16:00:00Z").toDate();
		System.out.println(d);
	}
}
