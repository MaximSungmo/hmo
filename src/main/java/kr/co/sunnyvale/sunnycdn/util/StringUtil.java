package kr.co.sunnyvale.sunnycdn.util;

public class StringUtil {

	public static String getFirstTwoFromLast(String prefix) {
		int length = prefix.length();
		return prefix.substring(length -2, length);
	}

	public static String getSecondTwoFromLast(String prefix) {
		int length = prefix.length();
		return prefix.substring(length - 4, length - 2);
	}
	public static String getBaseDomain( String host ) {
	    int startIndex = 0;
	    int nextIndex = host.indexOf( '.' );
	    int lastIndex = host.lastIndexOf( '.' );
	    while (nextIndex < lastIndex) {
	        startIndex = nextIndex + 1;
	        nextIndex = host.indexOf( '.', startIndex );
	    }
	    if (startIndex > 0) {
	        return host.substring(startIndex);
	    } else {
	        return host;
	    }
	}
}
