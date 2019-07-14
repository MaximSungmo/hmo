package kr.co.sunnyvale.sunny.web.servlet.resolver;

import kr.co.sunnyvale.sunny.annotation.AuthUser;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthUserHandlerMethodArgumentResolver implements
		HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		
		return parameter.getParameterAnnotation(AuthUser.class) != null 
				&& parameter.getParameterType().equals(SecurityUser.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {
		
		if( this.supportsParameter(parameter)){
			Object principal = null;
			if( SecurityContextHolder.getContext().getAuthentication() != null ){
				principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			}
			
			if (principal == null || principal.getClass() == String.class) {
				return null;
			} else {
				return principal;
			}
		}else{
			return WebArgumentResolver.UNRESOLVED;
		}
		
	}

}
