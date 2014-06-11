package ctd.test.path;

import org.springframework.util.PatternMatchUtils;

public class PathMatcher {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String p = "com.*bsoft.*";
		boolean result = PatternMatchUtils.simpleMatch(p, "com.absoft.ss.xx");
		System.out.println(result);
	}

}
