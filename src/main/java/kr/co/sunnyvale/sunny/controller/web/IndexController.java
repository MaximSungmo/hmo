package kr.co.sunnyvale.sunny.controller.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import kr.co.sunnyvale.sunny.annotation.AuthUser;
import kr.co.sunnyvale.sunny.annotation.NoPathRedirect;
import kr.co.sunnyvale.sunny.annotation.ParseSunny;
import kr.co.sunnyvale.sunny.domain.Notification;
import kr.co.sunnyvale.sunny.domain.Site;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.dto.StoryDTO;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.service.NotificationService;
import kr.co.sunnyvale.sunny.service.SiteService;
import kr.co.sunnyvale.sunny.service.StoryService;
import kr.co.sunnyvale.sunny.service.SunnyService;
import kr.co.sunnyvale.sunny.service.TagService;
import kr.co.sunnyvale.sunny.service.UserService;
import kr.co.sunnyvale.sunny.util.SearchUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {
	
	@Autowired
	private SunnyService sunnyService; 

	@Autowired
	private SiteService siteService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private StoryService storyService; 
	
	@Autowired
	private TagService tagService;
	
	@Autowired
	private NotificationService notificationService;
	
	@RequestMapping( value = "/{path}/newsfeed" )
	public ModelAndView lobby(
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite = true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestParam(value="q[]", required=false) String[] queries,
			@RequestParam(value="nq", required=false) String newQuery,
			@RequestParam(value="recursive", required=false) Boolean isRecursive,
			@RequestParam(value="last", required=false) String lastDateString ) throws ParseException {
		
		
		if( sunny.isNoExistsSite() == true ){
			throw new AccessDeniedException("Access Denied");
		}
		
		ModelAndView modelAndView = new ModelAndView();
//		if( sunny.isGoRedirect() == true ){
//			System.out.println("redirect path : " + sunny.getRedirectPath());
//			modelAndView.setViewName("redirect:" + sunny.getPath());
//			return modelAndView;
//		}
		User user = null;
		
		if( securityUser != null ){

			// 계정 비활성 사용자 ( Access Denied )
			if( securityUser.hasRole("ROLE_USER") != true ){
				modelAndView.setViewName("/error/403");
				return modelAndView;
			}

			user = userService.findById( securityUser.getUserId() );
		}

		Stream stream = new Stream(null, null, null, 10);
		if( lastDateString != null ){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date lastDate = format.parse(lastDateString);
			stream.setBaseColumn("createDate");
			stream.setGreaterThan(true);
			stream.setBaseData(lastDate);
			
		}
		
		if( queries != null ){
			queries = SearchUtils.checkAndFixQuery( queries, newQuery, isRecursive );
		}

		List<StoryDTO> plazaStories = storyService.fetchLobbyStories(sunny, user, queries, stream);
		
		modelAndView.addObject("stories",plazaStories);
	
		modelAndView.addObject("queries",  queries);
		
		modelAndView.addObject("composerTags", tagService.getComposerTags( sunny.getSite().getLobbySmallGroup().getId() ));

		modelAndView.setViewName("/story/lobby");
		
		System.out.println("로비 가져오기 완료");
		return modelAndView;
	}
	
	
	@RequestMapping( value = "/lobby" )
	public ModelAndView redirectToNewsfeed(
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestParam(value="q[]", required=false) String[] queries,
			@RequestParam(value="nq", required=false) String newQuery,
			@RequestParam(value="recursive", required=false) Boolean isRecursive,
			@RequestParam(value="last", required=false) String lastDateString ) throws ParseException {
		

		if( securityUser == null ) {
			return frontPage( securityUser );
		}

		ModelAndView modelAndView = new ModelAndView();
		if( sunny.getSite() != null ){
			modelAndView.setViewName( "redirect:/" + sunny.getSite().getId() + "/newsfeed" ) ;	
		}else{
			modelAndView.setViewName( "redirect:/newsfeed" ) ;	
		}
		
		return modelAndView;
		
	}
	@RequestMapping( value = "/{path}/lobby" )
	public ModelAndView redirectToNewsfeed(
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestParam(value="q[]", required=false) String[] queries,
			@RequestParam(value="nq", required=false) String newQuery,
			@RequestParam(value="recursive", required=false) Boolean isRecursive,
			@RequestParam(value="last", required=false) String lastDateString ) throws ParseException {
		

		if( securityUser == null ) {
			return frontPage( securityUser );
		}

		ModelAndView modelAndView = new ModelAndView();
		if( sunny.getSite() != null ){
			modelAndView.setViewName( "redirect:/" + sunny.getSite().getId() + "/newsfeed" ) ;	
		}else{
			modelAndView.setViewName( "redirect:/newsfeed" ) ;	
		}
		
		return modelAndView;
		
	}

	
	
	@NoPathRedirect
	@RequestMapping( value = "/newsfeed" )
	public ModelAndView plaza(
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestParam(value="q[]", required=false) String[] queries,
			@RequestParam(value="nq", required=false) String newQuery,
			@RequestParam(value="recursive", required=false) Boolean isRecursive,
			@RequestParam(value="last", required=false) String lastDateString ) throws ParseException {
		return lobby( null, sunny, securityUser, queries, newQuery, isRecursive, lastDateString );
	}

	@RequestMapping( value = "/{path}/notice" )
	public ModelAndView notice( 
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite = true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestParam(value="q[]", required=false) String[] queries,
			@RequestParam(value="nq", required=false) String newQuery,
			@RequestParam(value="recursive", required=false) Boolean isRecursive,
			@RequestParam(value="last", required=false) String lastDateString ) throws ParseException {
		
		
		if( sunny.isNoExistsSite() == true ){
			throw new AccessDeniedException("Access Denied");
		}
		
		ModelAndView modelAndView = new ModelAndView();
		if( sunny.isGoRedirect() == true ){
			System.out.println("redirect path : " + sunny.getRedirectPath());
			modelAndView.setViewName("redirect:" + sunny.getPath());
			return modelAndView;
		}
		User user = null;
		
		if( securityUser != null ){

			// 계정 비활성 사용자 ( Access Denied )
			if( securityUser.hasRole("ROLE_USER") != true ){
				modelAndView.setViewName("/error/403");
				return modelAndView;
			}

			user = userService.findById( securityUser.getUserId() );
		}

		Stream stream = new Stream(null, null, null, 10);
		if( lastDateString != null ){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date lastDate = format.parse(lastDateString);
			stream.setBaseColumn("createDate");
			stream.setGreaterThan(true);
			stream.setBaseData(lastDate);
			
		}
		
		if( queries != null ){
			queries = SearchUtils.checkAndFixQuery( queries, newQuery, isRecursive );
		}

		List<StoryDTO> stories = storyService.fetchNoticeStories(sunny, user, queries, stream);
		
		modelAndView.addObject("stories", stories);
	
		modelAndView.addObject("queries",  queries);

		modelAndView.setViewName("/story/notice");
		return modelAndView;
	}
	
	
	@NoPathRedirect
	@RequestMapping( value = "/notice" )
	public ModelAndView notice( 
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
							@RequestParam(value="q[]", required=false) String[] queries,
							@RequestParam(value="nq", required=false) String newQuery,
							@RequestParam(value="recursive", required=false) Boolean isRecursive,
							@RequestParam(value="last", required=false) String lastDateString ) throws ParseException {
		return notice( null, sunny, securityUser, queries, newQuery, isRecursive, lastDateString );
	}

	
//	@RequestMapping( value = "/{path}/newsfeed" )
//	public ModelAndView newsfeed( 
//			@PathVariable("path") String path,
//			@ParseSunny(shouldExistsSite = true) Sunny sunny,
//			@AuthUser SecurityUser securityUser, 
//			@RequestParam(value="q[]", required=false) String[] queries,
//			@RequestParam(value="nq", required=false) String newQuery,
//			@RequestParam(value="recursive", required=false) Boolean isRecursive,
//			@RequestParam(value="last", required=false) String lastDateString ) throws ParseException {
//		
//		ModelAndView modelAndView = new ModelAndView();
//		if( sunny.isGoRedirect() == true ){
//			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
//			return modelAndView;
//		}
//		User user = null;
//		
//		if( securityUser != null ){
//
//			// 계정 비활성 사용자 ( Access Denied )
//			if( securityUser.hasRole("ROLE_USER") != true ){
//				modelAndView.setViewName("/error/403");
//				return modelAndView;
//			}
//
//			user = userService.findById( securityUser.getUserId() );
//		}
//
//		Stream stream = new Stream(null, null, null, 10);
//		if( lastDateString != null ){
//			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//			Date lastDate = format.parse(lastDateString);
//			stream.setBaseColumn("createDate");
//			stream.setGreaterThan(true);
//			stream.setBaseData(lastDate);
//			
//		}
//		
//		if( queries != null ){
//			queries = SearchUtils.checkAndFixQuery( queries, newQuery, isRecursive );
//		}
//
////		Sunny sunny = sunnyService.parseCurrent();
////		List<StoryDTO> plazaStories = storyService.getPlazaStoryList( site, user, queries, stream);
//		
////		modelAndView.addObject("stories",plazaStories);
//		List<StoryDTO> plazaStories = storyService.fetchNewsfeedStories(sunny, user, queries, stream);
//		
//		modelAndView.addObject("stories",plazaStories);
//	
//		modelAndView.addObject("queries",  queries);
//
//		modelAndView.addObject("sunny", sunny);
//		modelAndView.setViewName("/story/newsfeed");
//		return modelAndView;
//	}
//	
//	
//	@NoPathRedirect
//	@RequestMapping( value = "/newsfeed" )
//	public ModelAndView newsfeed( 
//			@ParseSunny(shouldExistsSite=true) Sunny sunny,
//			@AuthUser SecurityUser securityUser, 
//							@RequestParam(value="q[]", required=false) String[] queries,
//							@RequestParam(value="nq", required=false) String newQuery,
//							@RequestParam(value="recursive", required=false) Boolean isRecursive,
//							@RequestParam(value="last", required=false) String lastDateString ) throws ParseException {
//		return newsfeed( null, sunny, securityUser, queries, newQuery, isRecursive, lastDateString );
//	}
//	
	@RequestMapping( value = "/{path}" )
	public ModelAndView index( 
			@PathVariable("path") String path, 
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="q[]", required=false) String[] queries,
			@RequestParam(value="nq", required=false) String newQuery,
			@RequestParam(value="recursive", required=false) Boolean isRecursive,
			@RequestParam(value="last", required=false) String lastDateString) throws ParseException {
		

		sunny.setGoRedirect(false);
		
		if( securityUser == null ) {
			return frontPage( securityUser );
		}
		
		//return index(null, sunny, securityUser);
		ModelAndView modelAndView = new ModelAndView();
//		System.out.println("사이트 아이디 : " + sunny.getSite().getId());
//		System.out.println("패스는? " + path );
		
		String redirectPath = sunny.getPath();
		if( redirectPath == null ){
			redirectPath = "";
		}
		//modelAndView.setViewName( "redirect:" +  redirectPath + "/lobby" ) ;
		return lobby(path, sunny, securityUser, queries, newQuery, isRecursive, lastDateString);
//		return modelAndView;
		
		// 검색엔진 때문에 잠시 막아놓음
		//return ( securityUser == null ) ? frontPage( securityUser ) : lobby( null, sunny,securityUser, null, null, null, null);

		// naver 에 등록할 때만 잠시 열어놨던 부분
//		return plaza(  path, securityUser, null, null, null, null );
	}
	
	@RequestMapping( value = "/unsupport" )
	public ModelAndView unsupportedBrowser( ) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName( "/index/unsupport");
		return modelAndView;
	}
	
	@RequestMapping( value = "/" )
	public ModelAndView index( 
		@ParseSunny(shouldExistsSite=false) Sunny sunny, 
		@AuthUser SecurityUser securityUser,
		@RequestParam(value="q[]", required=false) String[] queries,
		@RequestParam(value="nq", required=false) String newQuery,
		@RequestParam(value="recursive", required=false) Boolean isRecursive,
		@RequestParam(value="last", required=false) String lastDateString) throws ParseException {
		
		Site site = sunny.getSite();
		// System.out.println( "=========================>" + site );
		
		if( site == null ) {
			return frontPage( securityUser );
		}

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName( "redirect:/" + sunny.getSite().getId() + "/newsfeed" ) ;	

		return modelAndView;

//		if( sunny.getSite() != null ){
//		}else{
//			modelAndView.setViewName( "redirect:/newsfeed" ) ;	
//		}
		
	}
	
	
//	@RequestMapping( value = "/front" )
	public ModelAndView frontPage( @AuthUser SecurityUser securityUser ) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName( "/index/front" ) ;
		return modelAndView;
	}
	
	@RequestMapping( value = "/{path}/feedback" )
	public ModelAndView feedback(
			@PathVariable("path") String path,
			@AuthUser SecurityUser securityUser, 
			@RequestParam(value="q[]", required=false) String[] queries,
			@RequestParam(value="nq", required=false) String newQuery,
			@RequestParam(value="recursive", required=false) Boolean isRecursive,
			@RequestParam(value="last", required=false) String lastDateString ) throws ParseException {
		
		ModelAndView modelAndView = new ModelAndView();
		
		Stream stream = new Stream(null, null, null, 10);
		
		if( queries != null ){
			queries = SearchUtils.checkAndFixQuery( queries, newQuery, isRecursive );
		}

		User user = null;
		
		if( securityUser != null ){
			user = userService.findById(securityUser.getUserId());
		}
		
		List<StoryDTO> plazaStories = storyService.fetchFeedback(user, queries, stream);
		
		modelAndView.addObject("stories",plazaStories);
	
		modelAndView.addObject("queries",  queries);

		modelAndView.setViewName("/feedback");
		
		return modelAndView;
	}
	
	
	@RequestMapping( value = "/feedback" )
	public ModelAndView feedback(
			@AuthUser SecurityUser securityUser, 
			@RequestParam(value="q[]", required=false) String[] queries,
			@RequestParam(value="nq", required=false) String newQuery,
			@RequestParam(value="recursive", required=false) Boolean isRecursive,
			@RequestParam(value="last", required=false) String lastDateString ) throws ParseException {
		return feedback( null, securityUser, queries, newQuery, isRecursive, lastDateString );
	}
	
	
	@RequestMapping( value = "/issue" )
	public ModelAndView issue( ) {
		
		ModelAndView modelAndView = new ModelAndView();
		
		modelAndView.setViewName("/issue");
		
		return modelAndView;
	}
	@RequestMapping( value = "/notifications" )
	public ModelAndView notifications( 
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="page", required=false) Integer page ,
			@RequestParam(value="pl", required=false) Boolean isPagelet ) {
		
		ModelAndView modelAndView = new ModelAndView();
		
		User user = null;
		
		if( securityUser != null ){
			user = userService.findById(securityUser.getUserId());
		}
		
		
		Page<Notification> pagedResult  = null;
		if( isPagelet != null && isPagelet ){
			pagedResult = notificationService.getNotifications(user, page, Page.DEFAULT_PAGE_SIZE);
		}else{
			pagedResult = notificationService.getNotificationsAndUpdate(user, page, Page.DEFAULT_PAGE_SIZE);
		}
		
		
		modelAndView.addObject("pagedResult", pagedResult);
		
		if( isPagelet != null && isPagelet ){
			modelAndView.setViewName("/pagelet/notifications");
		}else{
			modelAndView.setViewName("/notifications");	
		}
		
		return modelAndView;
	}
	
	
}