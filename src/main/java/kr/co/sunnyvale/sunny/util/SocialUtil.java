package kr.co.sunnyvale.sunny.util;

import javax.servlet.http.HttpServletRequest;


public class SocialUtil {
	public static String getWrapProviderId(String providerId, HttpServletRequest request) {
		String wrapProviderId = request.getServerName() + "." + providerId;
		System.out.println("변환된 providerId : " + wrapProviderId);
		return wrapProviderId; 
	}
	
	public static String getStrippedProviderId(String providerId) {
		int lastIndexOf = providerId.lastIndexOf(".");
		
		if( lastIndexOf == -1 ){
			return providerId; 
		}
		
		return providerId.substring(lastIndexOf + 1 , providerId.length() );
		
	}
}
