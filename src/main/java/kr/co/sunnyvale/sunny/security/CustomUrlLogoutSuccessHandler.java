package kr.co.sunnyvale.sunny.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

public class CustomUrlLogoutSuccessHandler extends AbstractAuthenticationTargetUrlRequestHandler
implements LogoutSuccessHandler{

	@Override
	public void onLogoutSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		
		String accept = request.getHeader( "accept" );
		
    	if( accept.matches( ".*application/json.*" ) == true ) {
    		logger.debug("success");
//    		response.sendError( HttpServletResponse.SC_ACCEPTED);
    		response.getOutputStream().write( "{ 'result':'success', 'message':'logout success', 'data':'logout success' }".getBytes() );
    		
    		return;
    	}

    	if( accept.matches( ".*application/javascript.*" ) == true ) {
    		logger.debug("incorrect");
    		//response.sendError( HttpServletResponse.SC_UNAUTHORIZED );
    		response.getOutputStream().write( "{ 'result':'success', 'message':'logout success', 'data':'logout success' }".getBytes() );
    		return;
    	}
    	
    	if( accept.equals("*/*")  == true ) {  // mozilla
    		//response.sendError( HttpServletResponse.SC_UNAUTHORIZED );
    		response.getOutputStream().write( "{ 'result':'success', 'message':'logout success', 'data':'logout success' }".getBytes() );
    		return;
    	}
		super.handle(request, response, authentication);		
		
	}

}
