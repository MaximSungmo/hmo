package kr.co.sunnyvale.sunny.util;

import java.util.Date;

public class DateUtils {
	
	public static Date prevDateByDay( int dayAgo ){
		Date today = new Date();
		long DAY_IN_MS = 1000 * 60 * 60 * 24;
		return new Date( today.getTime() - ( dayAgo * DAY_IN_MS));
	}
	
}