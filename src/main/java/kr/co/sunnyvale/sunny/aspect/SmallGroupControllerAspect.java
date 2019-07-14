package kr.co.sunnyvale.sunny.aspect;

import java.util.Map;

import kr.co.sunnyvale.sunny.annotation.SmallGroupPrivacy;
import kr.co.sunnyvale.sunny.domain.SmallGroup;
import kr.co.sunnyvale.sunny.domain.SmallGroupInactiveUser;
import kr.co.sunnyvale.sunny.domain.UserSmallGroupAccess;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.exception.SimpleSunnyException;
import kr.co.sunnyvale.sunny.repository.hibernate.SmallGroupInactiveUserRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.UserSmallGroupAccessRepository;
import kr.co.sunnyvale.sunny.service.SmallGroupService;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

@Aspect
@Component
public class SmallGroupControllerAspect{
	

	@Autowired
	private SmallGroupService smallGroupService;
	
	@Autowired
	private UserSmallGroupAccessRepository userSmallGroupAccessRepository;
	
	@Autowired
	private SmallGroupInactiveUserRepository smallGroupInactiveUserRepository;
	
	@Around(value="@annotation(smallGroupPrivacy)")
	public Object methodArround(ProceedingJoinPoint joinPoint, SmallGroupPrivacy smallGroupPrivacy) throws Throwable {
		SecurityUser currentUser = null ;
		Long smallGroupId = null;
		String path = "";
		for (Object argument : joinPoint.getArgs()) {
			if( argument.getClass() == SecurityUser.class ){
				currentUser = (SecurityUser) argument;
				break;
			}else if( argument.getClass() == Long.class && smallGroupId == null ){
				smallGroupId = (Long) argument;
			}else if( argument.getClass() == String.class ){
				path = "/" + (String) argument ;
			}
	    }
		
		if( currentUser == null || smallGroupId == null ){
			ModelAndView modelAndView = new ModelAndView("/error/exception");
			modelAndView.addObject("message", "그룹에 대한 접근이 잘못되었습니다.");
			return modelAndView;
		}
		UserSmallGroupAccess smallGroupUser = userSmallGroupAccessRepository.getUserSmallGroupAccessUser(smallGroupId, currentUser.getUserId());
		if( currentUser.isAdmin() == false && ( smallGroupPrivacy.onlyAdmin() == true && smallGroupUser != null && smallGroupUser.isAdmin() == false ) ){
			throw new SimpleSunnyException();
		}
		
		SmallGroup smallGroup = smallGroupService.getSmallGroup( smallGroupId );
		// 가입되지 않은 유저의 경우 무조건 redirect 
		if( currentUser.isAdmin() == false && ( smallGroupUser == null && !joinPoint.getSignature().getName().equals("about") )){
			ModelAndView modelAndView = new ModelAndView("redirect:" + path + "/group/" + smallGroupId + "/about");
			return modelAndView;
		}
		Object result = joinPoint.proceed();
		
		ModelAndView modelAndView = (ModelAndView) result;
		Map<String, Object> map = modelAndView.getModel();
		
		SmallGroup checkSmallGroup = (SmallGroup) map.get("smallGroup") ;
		if( checkSmallGroup == null ){
			modelAndView.addObject("smallGroup", smallGroup);
		}
		
		if( map.get("smallGroupUser") == null ){
			modelAndView.addObject("smallGroupUser", smallGroupUser);	
		}
		
		if( smallGroupUser == null ){
			SmallGroupInactiveUser inactiveUser = smallGroupInactiveUserRepository.findRelation(smallGroupId, currentUser.getUserId());
			modelAndView.addObject("smallGroupInactiveUser", inactiveUser);
		}
		
		return result; 
	}
}
