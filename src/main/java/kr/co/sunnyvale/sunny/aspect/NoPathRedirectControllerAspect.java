package kr.co.sunnyvale.sunny.aspect;

import kr.co.sunnyvale.sunny.annotation.NoPathRedirect;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.service.SunnyService;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

@Aspect
@Component
public class NoPathRedirectControllerAspect{
	
	@Autowired
	private SunnyService sunnyService; 

	@Around(value="@annotation(noPathRedirect)")
	public Object methodArround(ProceedingJoinPoint joinPoint, NoPathRedirect noPathRedirect) throws Throwable {
		
		System.out.println("이거부터 한번 보고 ");
		Sunny sunny = sunnyService.parseCurrent(true);
		
		System.out.println(sunny.toString());
		if( sunny.isGoRedirect() == true ){
			System.out.println("여기는 언제 걸리나요");
			ModelAndView mav = new ModelAndView();
			mav.setViewName("redirect:" + sunny.getPath() + sunny.getRequestURI());
			return mav; 
		}
		return joinPoint.proceed();
	}
}
