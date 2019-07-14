package kr.co.sunnyvale.sunny.security;

import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.util.StringUtils;

public class SunnyTokenBasedRememberMeServices extends TokenBasedRememberMeServices {
	
	@Deprecated
	public SunnyTokenBasedRememberMeServices() {
	}
	
    public SunnyTokenBasedRememberMeServices(String key, UserDetailsService userDetailsService) {
        super(key, userDetailsService);
    }
	
//	@Override
//	protected UserDetails processAutoLoginCookie(String[] cookieTokens,
//			HttpServletRequest request, HttpServletResponse response) {
//		return super.processAutoLoginCookie(cookieTokens, request, response);
//	}

    @Override
    protected UserDetails processAutoLoginCookie(String[] cookieTokens, HttpServletRequest request,
            HttpServletResponse response) {

    	// 리멤버미를 토큰으로 사용하는 써니 서비스에서는 InvalidCookieException 을 제대로 처리해줘야된다. 
    	// 하지만 Filter Chain 을 Stop 하는걸 못 찾아서 우선 AccessDeniedHandler에 넘겨버림. 
//    	try{
    		return super.processAutoLoginCookie(cookieTokens, request, response);
//    	}catch(InvalidCookieException ex){
//    		return null;
//    	}
    	
//        if (cookieTokens.length != 3) {
//            throw new InvalidCookieException("Cookie token did not contain 3" +
//                    " tokens, but contained '" + Arrays.asList(cookieTokens) + "'");
//        }
//
//        long tokenExpiryTime;
//
//        try {
//            tokenExpiryTime = new Long(cookieTokens[1]).longValue();
//        }
//        catch (NumberFormatException nfe) {
//            throw new InvalidCookieException("Cookie token[1] did not contain a valid number (contained '" +
//                    cookieTokens[1] + "')");
//        }
//
//        if (isTokenExpired(tokenExpiryTime)) {
//            throw new InvalidCookieException("Cookie token[1] has expired (expired on '"
//                    + new Date(tokenExpiryTime) + "'; current time is '" + new Date() + "')");
//        }
//
//        // Check the user exists.
//        // Defer lookup until after expiry time checked, to possibly avoid expensive database call.
//
//        UserDetails userDetails = getUserDetailsService().loadUserByUsername(cookieTokens[0]);
//
//        // Check signature of token matches remaining details.
//        // Must do this after user lookup, as we need the DAO-derived password.
//        // If efficiency was a major issue, just add in a UserCache implementation,
//        // but recall that this method is usually only called once per HttpSession - if the token is valid,
//        // it will cause SecurityContextHolder population, whilst if invalid, will cause the cookie to be cancelled.
//        String expectedTokenSignature = makeTokenSignature(tokenExpiryTime, userDetails.getUsername(),
//                userDetails.getPassword());
//
//        
//        System.out.println("쿠키 로그인에 들어왔습니다.");
//        System.out.println("익셉션 날려봅니다.");
//        throw new RuntimeException("그냥 날려보는 쿠키 익셉션");
        
//        if (!equals(expectedTokenSignature,cookieTokens[2])) {
//            throw new InvalidCookieException("Cookie token[2] contained signature '" + cookieTokens[2]
//                                                                                                    + "' but expected '" + expectedTokenSignature + "'");
//        }

//        return userDetails;
    }
//    private static boolean equals(String expected, String actual) {
//        byte[] expectedBytes = bytesUtf8(expected);
//        byte[] actualBytes = bytesUtf8(actual);
//        if (expectedBytes.length != actualBytes.length) {
//            return false;
//        }
//
//        int result = 0;
//        for (int i = 0; i < expectedBytes.length; i++) {
//            result |= expectedBytes[i] ^ actualBytes[i];
//        }
//        return result == 0;
//    }
//
//    private static byte[] bytesUtf8(String s) {
//        if (s == null) {
//            return null;
//        }
//        return Utf8.encode(s);
//    }
	
	/**
	 * Remember me에 의한 로그인이 성공했을 때가 아닌 일반 로그인 성공했을 시 쿠키를 굽는 부분임.
	 */
	@Override
	public void onLoginSuccess(HttpServletRequest request,
			HttpServletResponse response,
			Authentication successfulAuthentication) {
		   
		
	       String username = retrieveUserName(successfulAuthentication);
	       String password = retrievePassword(successfulAuthentication);

	       // If unable to find a username and password, just abort as TokenBasedRememberMeServices is
	       // unable to construct a valid token in this case.
	       if (!StringUtils.hasLength(username)) {
	           logger.debug("Unable to retrieve username");
	           return;
	       }

	       // 제대로 된 사용자인데 비밀번호가 없을 리가 없을 듯 하고, 이미 유저 안에 들어있는 password 는 암호화된 비밀번호이기 때문에 소용 없을 것 같다. 
	       if (!StringUtils.hasLength(password)) {
	           UserDetails user = (SecurityUser) getUserDetailsService().loadUserByUsername(username);
	           password = user.getPassword();
	       }

	       int tokenLifetime = calculateLoginLifetime(request, successfulAuthentication);
	       long expiryTime = System.currentTimeMillis();
	       
	       // SEC-949
	       expiryTime += 1000L* (tokenLifetime < 0 ? TWO_WEEKS_S : tokenLifetime);

	       String signatureValue = makeTokenSignature(expiryTime, username, password);
	       
	       setCookie(new String[] {username, Long.toString(expiryTime), signatureValue}, tokenLifetime, request, response);
	       
	       
	       
	       
	       // setCookie 로는 secure 는 webview 와 Android 의 cookieSyncManager 와 sync 가 되지 않는 문제가 발생했다.
	       // 그러므로 살짝 난독화 해서 하나 더 보낸다. 
	       
	       String jsessionId = request.getSession().getId();
	       String rememberMe = encodeCookie(new String[] {username, Long.toString(expiryTime), signatureValue});
	       
	       SecurityUser securityUser = null;
			if (SecurityContextHolder.getContext().getAuthentication() != null) {
				Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				if (principal != null && principal instanceof UserDetails) {
					securityUser = (SecurityUser) principal;
					securityUser.setToken(rememberMe);
				}
			}
	       
	       
	       String sunnyCookieValue = encodeCookie(new String[] {rememberMe, jsessionId});
	       
	       // 안되나 해서 시험해봄. 별거 없으면 빼세요
	       Cookie cookie = new Cookie("sunny", sunnyCookieValue);
	       cookie.setMaxAge(1000000000);
	       cookie.setPath("/");
	       cookie.setSecure(false);
//	       cookie.setHttpOnly(false);
	       response.addCookie(cookie);
	       
	       if (logger.isDebugEnabled()) {
	           logger.debug("Added remember-me cookie for user '" + username + "', expiry: '"
	                   + new Date(expiryTime) + "'");
	       }
	}

//	@Override
//	protected void setCookie(String[] tokens, int maxAge, HttpServletRequest request, HttpServletResponse response) {
//        String cookieValue = encodeCookie(tokens);
//        Cookie cookie = new Cookie(super.getCookieName(), cookieValue);
//        cookie.setMaxAge(maxAge);
//        cookie.setPath(getCookiePath(request));
//
//        // 이상하게 secureCookie 를 하면 모바일에서 쿠키를 가져오지 못한다. 이슈 해결되면 빼기
////        if (useSecureCookie == null) {
////            cookie.setSecure(request.isSecure());
////        } else {
////            cookie.setSecure(useSecureCookie);
////        }
////
////        if(setHttpOnlyMethod != null) {
////            ReflectionUtils.invokeMethod(setHttpOnlyMethod, cookie, Boolean.TRUE);
////        } else if (logger.isDebugEnabled()) {
////            logger.debug("Note: Cookie will not be marked as HttpOnly because you are not using Servlet 3.0 (Cookie#setHttpOnly(boolean) was not found).");
////        }
//
//        response.addCookie(cookie);
//    }

}
