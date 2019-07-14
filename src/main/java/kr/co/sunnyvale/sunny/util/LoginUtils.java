package kr.co.sunnyvale.sunny.util;

import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;

import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 현재 로그인한 사용자 정보를 가져옴.
 * @author Mook
 *
 */
public class LoginUtils {
	public static void checkLogin(SecurityUser securityUser){
		if( securityUser == null ){
			throw new RuntimeException();
		}
	}

	public static SecurityUser getSecurityUser() {
		Object principal = null;
		if( SecurityContextHolder.getContext().getAuthentication() != null ){
			principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}
		
		if (principal == null || principal.getClass() == String.class) {
			return null;
		} else {
			return (SecurityUser) principal;
		}
	}
}
