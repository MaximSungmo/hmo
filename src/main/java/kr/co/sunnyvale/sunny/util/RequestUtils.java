package kr.co.sunnyvale.sunny.util;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class RequestUtils {
	
//	public static String MAIN_DOMAIN = "sunnysns.com";
//	
//	public static String getCurrentServerName( ){
//		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();       
//		HttpServletRequest hsr = sra.getRequest();
//		String serverName = hsr.getServerName();
//		
//		// 메인 도메인인 meet42.com/yacamp 와 같은 Path 식으로 들어왔을 경우엔 path 만 리턴하고 
//		// www.yacamp.com 와 같은 식으로 따로 도메인 들어왔을 때가 다름
//		if( serverName.indexOf( RequestUtils.MAIN_DOMAIN ) == -1 ){
//			return serverName;
//		}
//		// www.yacamp.com 같이 root 로 들어오는 경우엔 그냥 리턴
//		String requestUri = hsr.getRequestURI().substring(1);
//		if( requestUri.length() == 0 ){
//			return serverName;
//		}
//		
//		// www.yacamp.com/hello/asd?dkfjkd 와 같이 뒤에 덕지덕지 있으면 잘라내고 www.yacamp.com 만 리턴
//		if( requestUri.indexOf('/') > -1 ){
//			requestUri =  requestUri.substring(0, requestUri.indexOf('/'));
//		}
//		return requestUri;
//	}
//	
//	public static String getCurrentDocumentDomain( ){
//		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();       
//		HttpServletRequest hsr = sra.getRequest();
//		String serverName = hsr.getServerName();
//		return StringUtils.getBaseDomain(  hsr.getServerName() );
//		
//	}
//	
//	public static String getSitePath() {
//		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder
//				.currentRequestAttributes();
//		HttpServletRequest hsr = sra.getRequest();
//		String serverName = hsr.getServerName();
//
//		if (serverName.indexOf(RequestUtils.MAIN_DOMAIN) == -1) {
//			return "";
//		}
//		String requestUri = hsr.getRequestURI().substring(1);
//		if (requestUri.length() == 0) {
//			return "";
//		}
//
//		if (requestUri.indexOf('/') > -1) {
//			requestUri = requestUri.substring(0, requestUri.indexOf('/'));
//		}
//		return "/" + requestUri;
//	}
	public static String getRequestDump(HttpServletRequest request)
			throws IOException {
//		BufferedRequestWrapper bufferedReqest = new BufferedRequestWrapper(
//				request);
//
		StringBuilder dump = new StringBuilder("");
		dump.append("[header : " + getHeadersRequestMap(request) + "]\n");
		dump.append("[requestUri : " + request.getRequestURI() + "]\n");
		dump.append("[ipaddress : " + request.getRemoteAddr() + "]\n");
		dump.append("[parameters : " + getTypesafeRequestMap(request) + "]\n");
//		dump.append("[body : " + bufferedReqest.getRequestBody() + "]\n");

		return dump.toString();
	}

	public static Map<String, String> getHeadersRequestMap(
			HttpServletRequest request) {
		Map<String, String> typesafeRequestMap = new HashMap<String, String>();

		Enumeration<?> requestHeaderNames = request.getHeaderNames();

		while (requestHeaderNames.hasMoreElements()) {

			String name = (String) requestHeaderNames.nextElement();

			String requestHeaderValue = request.getHeader(name);

			typesafeRequestMap.put(name, requestHeaderValue);

		}
		return typesafeRequestMap;
	}

	/**
	 * 리퀘스트를 Map 으로 변경
	 * 
	 * @param request
	 * @return map<String, String>
	 */
	public static Map<String, String> getTypesafeRequestMap(
			HttpServletRequest request) {
		Map<String, String> typesafeRequestMap = new HashMap<String, String>();

		Enumeration<?> requestParamNames = request.getParameterNames();

		while (requestParamNames.hasMoreElements()) {

			String requestParamName = (String) requestParamNames.nextElement();

			String requestParamValue = request.getParameter(requestParamName);

			typesafeRequestMap.put(requestParamName, requestParamValue);

		}
		return typesafeRequestMap;
	}

}