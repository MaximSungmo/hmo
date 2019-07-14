package kr.co.sunnyvale.sunny.web.servlet.resolver;

import kr.co.sunnyvale.sunny.annotation.ParseSunny;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.service.SunnyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class ParseSunnyHandlerMethodArgumentResolver implements
		HandlerMethodArgumentResolver {

	@Autowired
	private SunnyService sunnyService;
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		
		return parameter.getParameterAnnotation(ParseSunny.class) != null 
				&& parameter.getParameterType().equals(Sunny.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {
		
		if( this.supportsParameter(parameter)){
			ParseSunny parseSunny = parameter.getParameterAnnotation(ParseSunny.class);
			return sunnyService.parseCurrent(parseSunny.shouldExistsSite());
		}else{
			return WebArgumentResolver.UNRESOLVED;
		}
		
	}

}
