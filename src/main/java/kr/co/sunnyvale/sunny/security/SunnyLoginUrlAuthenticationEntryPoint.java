package kr.co.sunnyvale.sunny.security;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.util.LoginUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

/**
 * {SpringSecurity}
 * 
 * <p>
 * 엔트리포인트는 일종의 리스너다. 필터에 등록되어 이벤트 발생시 특정 메서드가 실행되게 되어 있다.
 * 
 * <p>
 * AuthenticationException or AccessDeniedException 익셉션 발생시 commence 가 호출된다.
 * 이 시점에서 오류 응답(403)을 하면 된다.
 * 오버라이드된 메서드내에서 JSON응답에 대한 처리가 다양한데, 이는 브라우저마다 크로스 도메인 처리방식이 다르기 때문이다.
 * 
 * @see org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint#commence(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.AuthenticationException)
 * @author kickscar
 *
 */
public class SunnyLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {
	private static final Log logger = LogFactory.getLog(LoginUrlAuthenticationEntryPoint.class);

    public SunnyLoginUrlAuthenticationEntryPoint( String loginFormUrl ) {
    	super( loginFormUrl );
    }
    
	/**
	 * 
	 * 
	 *  
	 */
	@Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
		logger.debug("=============== CustomLoginUrlAuthenticationEntryPoint:commence ================");
    	Enumeration e = request.getHeaderNames();
    	while( e.hasMoreElements() ) {
    		String headerName = (String) e.nextElement();
    		logger.debug( headerName + ":" + request.getHeader( headerName ) );
    	}		
    	logger.debug("=================================================================================");

//		같은 도메인에서 가능함		
//		String xrequestedWith = ((HttpServletRequest) request).getHeader("x-requested-with");
//		if( xrequestedWith != null && xrequestedWith.equals("XMLHttpRequest") && authException != null ) {
//			response.sendError( HttpServletResponse.SC_UNAUTHORIZED );
//		} else {
//			super.commence(request, response, authException);
//		}
//
		SecurityUser securityUser = LoginUtils.getSecurityUser();
		
		if( securityUser == null ){
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}else{
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		}
		
		// 크로스 도메인 지원
		String accept = request.getHeader( "accept" );
		
		if( accept == null || accept.isEmpty() ){
	    	super.commence(request, response, authException);
	    	return;
		}
		
    	if( accept.matches( ".*application/json.*" ) == true ) {
    		logger.debug("JSON Access Denied");
    		response.getOutputStream().write( "{ 'result':'fail', 'message':'', 'data':null }".getBytes() );
//    		response.sendError( HttpServletResponse.SC_UNAUTHORIZED );
    		
    		return;
    	}

    	if( accept.matches( ".*application/javascript.*" ) == true ) {
    		logger.debug("Javascript Access Denied");
    		//response.sendError( HttpServletResponse.SC_UNAUTHORIZED );
    		response.getOutputStream().write( "{ 'result':'fail', 'message':'', 'data':null }".getBytes() );
    		return;
    	}
    	
    	if( accept.equals("*/*")  == true ) {  // mozilla
    		//response.sendError( HttpServletResponse.SC_UNAUTHORIZED );
    		response.getOutputStream().write( "{ 'result':'fail', 'message':'', 'data':null }".getBytes() );
    		return;
    	}
    	
    	super.commence(request, response, authException);
	}
}
