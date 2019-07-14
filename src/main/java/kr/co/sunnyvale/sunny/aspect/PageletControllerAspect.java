package kr.co.sunnyvale.sunny.aspect;

import java.util.Map;

import kr.co.sunnyvale.sunny.domain.SmallGroup;
import kr.co.sunnyvale.sunny.domain.UserInfo;
import kr.co.sunnyvale.sunny.domain.dto.CurrentInfo;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.repository.hibernate.UserInfoRepository;
import kr.co.sunnyvale.sunny.service.ChannelService;
import kr.co.sunnyvale.sunny.service.FriendService;
import kr.co.sunnyvale.sunny.service.NotificationService;
import kr.co.sunnyvale.sunny.service.SiteService;
import kr.co.sunnyvale.sunny.service.SmallGroupService;
import kr.co.sunnyvale.sunny.service.SunnyService;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
/**
 * 
 * @author urstory
 *
 * JoinPoint : 어떤 위치에 Advice를 적용할 것인가?
 * Advice : 횡적관심사 - 메소드에 적용할 내용
 * PointCut : 어떤 Advice가 어떤 JoinPoint에 적용될 것인가?
 * Aspect는 위의 3가지를 하나의 클래스가 구현.
 * 
 * Aspect는 JoinPoint: 
 * 	@Before (메소드가 시작할때) 
 * 	@After (메소드가 종료될때) 
 * 	@AfterThrowing (Exception이 발생할때) 
 * 	@Around (Before + After)
 * 
 * execution(public * kr.co.sunnyvale.yacamp.service.UserService.*(..))
 * Advice 는 위의  Annotation이 적용될 메소드에 구현된 내용.
 * 
 * cf) Controller에서는   Aspect가 사용되기 어려울 수 있음 ( 참고: http://whiteship.tistory.com/1124 )
 * 
 */
@Component
@Aspect
public class PageletControllerAspect{
	
	
	private Logger logger = LoggerFactory.getLogger( this.getClass() ); 

	@Autowired
	private SiteService siteService;
	
	@Autowired
	private UserInfoRepository userInfoRepository;

	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private SmallGroupService smallGroupService; 

	@Autowired
	private FriendService friendService;

	@Autowired
	private ChannelService channelService;

	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private SunnyService sunnyService; 

	@Around("execution(public * kr.co.sunnyvale.sunny.controller.pagelet..*(..))")
	public Object methodArround(ProceedingJoinPoint joinPoint) throws Throwable {
		Object result = null;
		
		result = joinPoint.proceed();
		
		
		ModelAndView modelAndView = (ModelAndView) result;
		
		/*
		 * 잡다한 데이터들을 넣어준다.
		 * 일차적으로 sunny 를 넣어준다.
		 * 
		 */
		Map<String, Object> map = modelAndView.getModel();
		Sunny sunny = (Sunny) map.get("sunny") ;
		if( sunny == null ){
			sunny = sunnyService.parseCurrent(false);
			modelAndView.addObject("sunny", sunny);	
		}
		
		return modelAndView;
		
	}
}
