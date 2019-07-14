package kr.co.sunnyvale.sunny.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

/**
 * {SpringSecurity}
 * 
 * <p>
 * 로그인 사용자의 웹 디테일 정보를 담는  클래스이다.
 * 
 * <p>
 * 로그인 사용자의 웹 디테일 정보는 remoteIP, 세션 아이디, 브라우저 버젼등 클라이언트 정보가 됤 수 있다.
 * 기본 제공 클래스 {@link org.springframework.security.web.authentication.WebAuthenticationDetails}에는 
 * 세션아이디, remoteIP 두 개만 제공하기 때문에 상속을 통해 확장할 필요가 있다. 
 * 
 * <p>
 * {@link org.springframework.security.web.authentication.WebAuthenticationDetailsSource}가
 * {@link org.springframework.security.web.authentication.WebAuthenticationDetails} 를 제공하듯
 *
 * {@link kr.co.sunnyvale.SunnyWebAuthenticationDetailsSource.HydeWebAuthenticationDetailsSource.RedwoodWebAuthenticationDetailsSource}를 통해 제공될 수 있다
 * 
 * @author kickscar
 *
 *
 */
public class WebAuthenticationDetailsEx extends WebAuthenticationDetails {
	
	private static final long serialVersionUID = 3681004865282273006L;

	public WebAuthenticationDetailsEx( HttpServletRequest request ) {
		super( request );
	}
	
}
