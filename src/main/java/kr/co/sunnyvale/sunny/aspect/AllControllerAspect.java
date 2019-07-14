package kr.co.sunnyvale.sunny.aspect;

import java.util.List;
import java.util.Map;

import kr.co.sunnyvale.sunny.domain.Menu;
import kr.co.sunnyvale.sunny.domain.SmallGroup;
import kr.co.sunnyvale.sunny.domain.User;
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
import kr.co.sunnyvale.sunny.service.StoryService;
import kr.co.sunnyvale.sunny.service.SunnyService;
import kr.co.sunnyvale.sunny.service.TagService;
import kr.co.sunnyvale.sunny.service.UserService;
import kr.co.sunnyvale.sunny.service.impl.MenuService;

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
public class AllControllerAspect{
	
	
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
	private MenuService menuService;
	
	@Autowired
	private TagService tagService;
	
	@Autowired
	private UserService userService; 

	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private SunnyService sunnyService; 
	
	@Autowired
	private StoryService storyService; 

	@Around("execution(public * kr.co.sunnyvale.sunny.controller.web..*(..))")
	public Object methodArround(ProceedingJoinPoint joinPoint) throws Throwable {
		Object result = null;
		boolean isUserLoggedIn = false;
		boolean isPagelet = false;
		Object principal = null;
		if (SecurityContextHolder.getContext().getAuthentication() != null) {

			principal = SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			if (principal.getClass() != String.class) {
				isUserLoggedIn = true;
			}
		}
		
		
		result = joinPoint.proceed();
		
		
		ModelAndView modelAndView = (ModelAndView) result;
		
		/*
		 * redirect 는 다음의 데이터들이 필요 없으므로 그대로 리턴. 
		 */
		if( modelAndView.getViewName().indexOf("redirect:") > -1 ){
			return modelAndView;
		}
		
		if( modelAndView.getViewName().indexOf("/pagelet/") > -1 ){
			isPagelet = true;
		}
		
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
		if (isPagelet == false && sunny.isGoRedirect() == false && isUserLoggedIn && sunny.getSite() != null ) {

//			modelAndView.addObject("rightNotices", storyService.getNotices( sunny, sunny.getSite().getLobbySmallGroup().getId(), new Stream(3) ));
			
			SecurityUser securityUser = (SecurityUser) principal;
			
			User user = userService.findById( securityUser.getUserId() );
			UserInfo info = user.getInfo();
			CurrentInfo currentInfo = new CurrentInfo();
			
			currentInfo.setDepartmentCount( info.getDepartmentCount() );
			currentInfo.setLeftDepartments(smallGroupService.getSmallGroupList(sunny.getSite(), null, info.getUser(), SmallGroup.TYPE_DEPARTMENT, new Stream(3)));
			currentInfo.setLeftGroups(smallGroupService.getSmallGroupList(sunny.getSite(), null, info.getUser(), SmallGroup.TYPE_GROUP, new Stream(3)));
//			currentInfo.setLeftProjects(smallGroupService.getSmallGroupList(sunny.getSite(),  null, info.getUser(), SmallGroup.TYPE_PROJECT, new Stream(3)));
			currentInfo.setNotificationCount(notificationService.getNotificationUnreadCount(info.getUser()).intValue());
//			currentInfo.setFriendRequestCount(friendService.getFriendRequestUnreadCount(info.getUser()).intValue());
			currentInfo.setNoticeCount( storyService.getNoticeUnreadCount(sunny, user).intValue());
			currentInfo.setMessageCount(channelService.getChannelUnreadCount(info.getUser()).intValue());
			currentInfo.setTopTags( tagService.getTopTags( sunny.getSite().getLobbySmallGroup().getId() ));
			List<Menu> menus = menuService.findBySite( sunny.getSite().getId() );
			currentInfo.setLeftMenus( menus );
			modelAndView.addObject("currentInfo", currentInfo);
		}
		
		return modelAndView;
		
	}
}
