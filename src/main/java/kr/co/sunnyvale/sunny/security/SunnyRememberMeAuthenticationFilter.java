package kr.co.sunnyvale.sunny.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;

public class SunnyRememberMeAuthenticationFilter extends
		RememberMeAuthenticationFilter {
	
	@Deprecated
    public SunnyRememberMeAuthenticationFilter() {
		super();
    }
	
	
	public SunnyRememberMeAuthenticationFilter(
			AuthenticationManager authenticationManager,
			RememberMeServices rememberMeServices) {
		super(authenticationManager, rememberMeServices);
	}
	
	
	@Override
	protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            Authentication authResult) {
		request.getSession(true).setAttribute("loginNow", true);
		System.out.println("토큰 로그인 성공함");
		
    }
}
