package kr.co.sunnyvale.sunny.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class CacheInterceptor extends HandlerInterceptorAdapter {

	public final String startWithString;
	
	public CacheInterceptor(){
		startWithString =  "/assets";
	}
	
	public CacheInterceptor(String startWithString ){
		this.startWithString = startWithString;
	}
	
	
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		if( request.getRequestURI().startsWith(startWithString) ){
			
		}else{
			response.setHeader("Cache-Control",
					"no-cache, max-age=0, must-revalidate, no-store");
			response.setDateHeader("Expires", 0);
			response.setHeader("Pragma", "No-cache");	
		}
		
		super.postHandle(request, response, handler, modelAndView);
	}
	
}
