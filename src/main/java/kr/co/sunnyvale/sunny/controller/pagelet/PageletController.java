package kr.co.sunnyvale.sunny.controller.pagelet;

import java.text.ParseException;
import java.util.List;

import kr.co.sunnyvale.sunny.annotation.AuthUser;
import kr.co.sunnyvale.sunny.annotation.ParseSunny;
import kr.co.sunnyvale.sunny.domain.Pds;
import kr.co.sunnyvale.sunny.domain.SiteInactiveUser;
import kr.co.sunnyvale.sunny.domain.SmallGroup;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.exception.SimpleSunnyException;
import kr.co.sunnyvale.sunny.service.BookMarkService;
import kr.co.sunnyvale.sunny.service.ChannelService;
import kr.co.sunnyvale.sunny.service.FavoriteUserService;
import kr.co.sunnyvale.sunny.service.FriendService;
import kr.co.sunnyvale.sunny.service.MessageService;
import kr.co.sunnyvale.sunny.service.NotificationService;
import kr.co.sunnyvale.sunny.service.PdsService;
import kr.co.sunnyvale.sunny.service.SmallGroupService;
import kr.co.sunnyvale.sunny.service.StoryService;
import kr.co.sunnyvale.sunny.service.UserService;
import kr.co.sunnyvale.sunny.service.impl.SiteInactiveUserService;
import kr.co.sunnyvale.sunny.util.LoginUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Lists;

@Controller
public class PageletController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private MessageSource messageSource;

	@Autowired
	private MessageService messageService;
	
	@Autowired
	private ChannelService channelService;
	
	@Autowired
	private FriendService friendService;
	
	@Autowired
	private StoryService storyService; 
	
	@Autowired
	private SmallGroupService smallGroupService; 
	
	@Autowired
	private FavoriteUserService favoriteUserService;
	
	@Autowired
	private BookMarkService bookMarkService;  
	
	@Autowired
	private PdsService pdsService; 
	
	
	@Autowired
	private SiteInactiveUserService siteInactiveUserService; 
	
	@RequestMapping( value = "/{path}/pagelet/contact_group_users/{smallGroupId}" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView contactGroupUsers(  
			@PathVariable("path") String path,
			@PathVariable("smallGroupId") Long smallGroupId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="page", required=false) Integer page) throws ParseException {
		
		
		ModelAndView modelAndView = new ModelAndView();
		
		if( sunny.isGoRedirect() == true ){
			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
			return modelAndView;
		}
		LoginUtils.checkLogin(securityUser);
		
		List<Long> smallGroupIds = Lists.newArrayList();
		smallGroupIds.add( smallGroupId );
		
		Page<User> pagedResult = userService.getUserList(sunny, smallGroupIds, null, null, null, null,null, null, null, page, Page.DEFAULT_PAGE_SIZE);

		modelAndView.addObject("pagedResult", pagedResult);
		modelAndView.addObject("sunny", sunny);
		
		modelAndView.setViewName("/contact/pagelet/list");

		return modelAndView;
	}
	
	
	@RequestMapping( value = "/pagelet/contact_group_users/{smallGroupId}" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView contactGroupUsers(  
			@PathVariable("smallGroupId") Long smallGroupId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="page", required=false) Integer page) throws ParseException {
		
		return contactGroupUsers(null, smallGroupId, sunny, securityUser, page);
	}
	
//	@RequestMapping( value = "/{path}/pagelet/{smallGroupId}/right_notice" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
//	public ModelAndView rightNotice(  
//			@PathVariable("path") String path,
//			@PathVariable("smallGroupId") Long smallGroupId,
//			@ParseSunny(shouldExistsSite=true) Sunny sunny,
//			@AuthUser SecurityUser securityUser) throws ParseException {
//		
//		
//		ModelAndView modelAndView = new ModelAndView();
//		
//		LoginUtils.checkLogin(securityUser);
//		
//		modelAndView.addObject("rightNotices", storyService.getNotices( sunny, smallGroupId, new Stream(3) ));
//
//		modelAndView.addObject("sunny", sunny);
//		
//		modelAndView.setViewName("/pagelet/right_notice");
//
//		return modelAndView;
//	}
	
	
	@RequestMapping( value = "/pagelet/{smallGroupId}/right_requests" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView rightRequests(  
			@PathVariable("smallGroupId") Long smallGroupId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser) throws ParseException {
		
		
		ModelAndView modelAndView = new ModelAndView();
		
		LoginUtils.checkLogin(securityUser);
		
		Page<SiteInactiveUser> pagedResult = siteInactiveUserService.getSiteInactiveUsers( sunny, SiteInactiveUser.STATUS_REQUEST, SiteInactiveUser.TYPE_REQUEST, 1, 1000) ;
		modelAndView.addObject("pagedResult", pagedResult);
		modelAndView.addObject("sunny", sunny);
		modelAndView.setViewName("/pagelet/right_requests");

		return modelAndView;
	}
	
	
	
//	@RequestMapping( value = "/pagelet/{smallGroupId}/right_notice" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
//	public ModelAndView rightNotice(  
//			@PathVariable("smallGroupId") Long smallGroupId,
//			@ParseSunny(shouldExistsSite=true) Sunny sunny,
//			@AuthUser SecurityUser securityUser) throws ParseException {
//		
//		return rightNotice(null, smallGroupId, sunny, securityUser);
//	}
	
	
	
	@RequestMapping( value = "/{path}/pagelet/pds/{pdsId}" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView pdsView(  
			@PathVariable("path") String path,
			@PathVariable("pdsId") Long pdsId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser) throws ParseException {
		
		
		ModelAndView modelAndView = new ModelAndView();
		
		if( sunny.isGoRedirect() == true ){
			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
			return modelAndView;
		}
		LoginUtils.checkLogin(securityUser);
		
		Pds content = pdsService.getPds(sunny, new User(securityUser.getUserId()), pdsId);

		modelAndView.addObject("content", content);
		
		modelAndView.addObject("sunny", sunny);
		
		modelAndView.addObject("alreadyBookmarked", bookMarkService.isAlreadyBookMark(sunny, pdsId, securityUser.getUserId()));
		modelAndView.setViewName("/pagelet/pds/view");

		return modelAndView;
	}
	
	
	@RequestMapping( value = "/pagelet/pds/{pdsId}" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView pdsView(  
			@PathVariable("pdsId") Long pdsId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser) throws ParseException {
		
		return pdsView(null, pdsId, sunny, securityUser);
	}
	
	// pagelet
		@RequestMapping( value = "/{path}/pagelet/pds" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
		public ModelAndView pdsPageletList(  
				@PathVariable("path") String path,
				@ParseSunny(shouldExistsSite=true) Sunny sunny,
				@AuthUser SecurityUser securityUser,
				@RequestParam(value="tab", required=false) String tab,
				@RequestParam(value="q", required=false) String query,
				@RequestParam(value="qt", required=false) Integer queryType,
				@RequestParam(value="status[]", required=false) Integer[] status,
				@RequestParam(value="range", required=false) String range,
				@RequestParam(value="ordering", required=false) String ordering,
				@RequestParam(value="page", required=false) Integer page) throws ParseException {

			
			ModelAndView modelAndView = new ModelAndView();
			
			if( sunny.isGoRedirect() == true ){
				throw new SimpleSunnyException();
			}
			LoginUtils.checkLogin(securityUser);
			
			
			User user = userService.findById(securityUser.getUserId());

			Page<Pds> pagedResult = null;
			if( tab == null || tab.equals("all")){
				pagedResult = pdsService.getPagedSmallGroupsPds( sunny, null, null, user, query, ordering, page, Page.DEFAULT_PAGE_SIZE);	
			}else if( tab.equals("my") ){
				pagedResult = pdsService.getPagedSmallGroupsPds( sunny, null, user, user, query, ordering, page, Page.DEFAULT_PAGE_SIZE);
			}else if( tab.equals("favorite")){
				
			}
					
			
			
			modelAndView.addObject("pagedResult", pagedResult);
			

			modelAndView.addObject("sunny", sunny);
			modelAndView.setViewName("/pagelet/pds/list");

			return modelAndView;
		}
		
		
		

		@RequestMapping( value = "/pagelet/pds" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
		public ModelAndView pdsPageletList(  
				@ParseSunny(shouldExistsSite=true) Sunny sunny,
				@AuthUser SecurityUser securityUser,
				@RequestParam(value="tab", required=false) String tab,
				@RequestParam(value="q", required=false) String query,
				@RequestParam(value="qt", required=false) Integer queryType,
				@RequestParam(value="status[]", required=false) Integer[] status,
				@RequestParam(value="range", required=false) String range,
				@RequestParam(value="ordering", required=false) String ordering,
				@RequestParam(value="page", required=false) Integer page ) throws ParseException {
			
			return pdsPageletList(null, sunny, securityUser, tab, query, queryType, status, range, ordering, page);
		}
	
	
	// pagelet
	@RequestMapping( value = "/{path}/contact/pagelet/list" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView contactPageletList(  
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="q", required=false) String queryName,
			@RequestParam(value="qt", required=false) Integer queryType,
			@RequestParam(value="status[]", required=false) Integer[] status,
			@RequestParam(value="range", required=false) String range,
			@RequestParam(value="ordering", required=false) String ordering,
			@RequestParam(value="page", required=false) Integer page) throws ParseException {

		
		ModelAndView modelAndView = new ModelAndView();
		
		if( sunny.isGoRedirect() == true ){
			throw new SimpleSunnyException();
		}
		LoginUtils.checkLogin(securityUser);
		
		Page<User> pagedResult = userService.getUserList(sunny, null, queryType, queryName, status, range, ordering, null, null, page, Page.DEFAULT_PAGE_SIZE);

		modelAndView.addObject("pagedResult", pagedResult);
		modelAndView.addObject("sunny", sunny);
		
		
		if( status != null ){
			for( Integer statusEach : status ){
				switch( statusEach ){
				case User.STATUS_WORKING:
					modelAndView.addObject("STATUS_WORKING", true);
					break;
					
				case User.STATUS_VACATION:
					modelAndView.addObject("STATUS_VACATION", true);
					break;
				case User.STATUS_LEAVE:
					modelAndView.addObject("STATUS_LEAVE", true);
					break;
				}
			}
		}
		
		modelAndView.setViewName("/contact/pagelet/list");

		return modelAndView;
	}
	
	
	

	@RequestMapping( value = "/contact/pagelet/list" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView contactPageletList(  
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="q", required=false) String queryName,
			@RequestParam(value="qt", required=false) Integer queryType,
			@RequestParam(value="status[]", required=false) Integer[] status,
			@RequestParam(value="range", required=false) String range,
			@RequestParam(value="ordering", required=false) String ordering,
			@RequestParam(value="page", required=false) Integer page ) throws ParseException {
		
		return contactPageletList(null, sunny, securityUser, queryName, queryType, status, range, ordering, page);
	}
	
	
	@RequestMapping( value = "/{path}/pagelet/permission" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView pageletSmallGroup(  
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="type", required=false) Integer smallGroupType,
			@RequestParam(value="q", required=false) String queryName,
			@RequestParam(value="qt", required=false) Integer queryType,
			@RequestParam(value="ordering", required=false) String ordering,
			@RequestParam(value="page", required=false) Integer page) throws ParseException {

		ModelAndView modelAndView = new ModelAndView();
		
		if( sunny.isGoRedirect() == true ){
			throw new SimpleSunnyException();
		}
		LoginUtils.checkLogin(securityUser);
		
		
		if( smallGroupType == null ){
			Page<User> pagedResult = userService.getUserList(sunny, null, queryType, queryName, null, null, ordering, securityUser.getUserId(),null,  page, Page.DEFAULT_PAGE_SIZE);
			modelAndView.addObject("pagedResult", pagedResult);
			modelAndView.setViewName("/pagelet/permission/user");
		}else{
			Page<SmallGroup> pagedResult = smallGroupService.pagedList(sunny, securityUser.getUserId(), queryType, queryName, smallGroupType, ordering, page, Page.DEFAULT_PAGE_SIZE);
			modelAndView.addObject("pagedResult", pagedResult);
			modelAndView.setViewName("/pagelet/permission/department");
		}

		modelAndView.addObject("sunny", sunny);
		

		return modelAndView;
	}
	
	@RequestMapping( value = "/pagelet/permission" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView pageletSmallGroup(  
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="type", required=false) Integer smallGroupType,
			@RequestParam(value="q", required=false) String queryName,
			@RequestParam(value="qt", required=false) Integer queryType,
			@RequestParam(value="ordering", required=false) String ordering,
			@RequestParam(value="page", required=false) Integer page ) throws ParseException {
		
		return pageletSmallGroup(null, sunny, securityUser, smallGroupType, queryName, queryType, ordering, page);
	}
	
	
	
	@RequestMapping( value = "/{path}/pagelet/user_and_group" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView pageletUserAndGroup(  
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="type", required=false) Integer smallGroupType,
			@RequestParam(value="q", required=false) String queryName,
			@RequestParam(value="qt", required=false) Integer queryType,
			@RequestParam(value="ordering", required=false) String ordering,
			@RequestParam(value="page", required=false) Integer page) throws ParseException {

		ModelAndView modelAndView = new ModelAndView();
		
		if( sunny.isGoRedirect() == true ){
			throw new SimpleSunnyException();
		}
		LoginUtils.checkLogin(securityUser);
		
		
		if( smallGroupType == null || smallGroupType == SmallGroup.TYPE_ME ){
			Page<User> pagedResult = userService.getUserList(sunny, null, queryType, queryName, null, null, ordering, securityUser.getUserId(), null, page, Page.DEFAULT_PAGE_SIZE);
			modelAndView.addObject("isUser", true);
			modelAndView.addObject("pagedResult", pagedResult);
			modelAndView.setViewName("/pagelet/user_and_group");
		}else{
			Page<SmallGroup> pagedResult = smallGroupService.pagedList(sunny, securityUser.getUserId(), queryType, queryName, smallGroupType, ordering, page, Page.DEFAULT_PAGE_SIZE);
			modelAndView.addObject("pagedResult", pagedResult);
			modelAndView.setViewName("/pagelet/user_and_group");
		}

		modelAndView.addObject("sunny", sunny);
		

		return modelAndView;
	}
	
	@RequestMapping( value = "/pagelet/user_and_group" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView pageletUserAndGroup(  
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="type", required=false) Integer smallGroupType,
			@RequestParam(value="q", required=false) String queryName,
			@RequestParam(value="qt", required=false) Integer queryType,
			@RequestParam(value="ordering", required=false) String ordering,
			@RequestParam(value="page", required=false) Integer page ) throws ParseException {
		
		return pageletUserAndGroup(null, sunny, securityUser, smallGroupType, queryName, queryType, ordering, page);
	}
	
	
	@RequestMapping( value = "/{path}/pagelet/invite_users" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView pageletInviteUsers(  
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="sgid", required=false) Long smallGroupId,
			@RequestParam(value="q", required=false) String queryName,
			@RequestParam(value="page", required=false) Integer page) throws ParseException {

		ModelAndView modelAndView = new ModelAndView();
		
		if( sunny.isGoRedirect() == true ){
			throw new SimpleSunnyException();
		}
		LoginUtils.checkLogin(securityUser);
		
		
		//Page<User> pagedResult = userService.getUserList(sunny, smallGroupIds, null, queryName, null, null, null, page, Page.DEFAULT_PAGE_SIZE);
		Page<User> pagedResult = userService.getPagedNotInSmallGroupUserList(sunny, smallGroupId, securityUser.getUserId(), queryName, page, Page.DEFAULT_PAGE_SIZE);
		modelAndView.addObject("pagedResult", pagedResult);
		modelAndView.setViewName("/pagelet/invite_users");

		modelAndView.addObject("sunny", sunny);
		

		return modelAndView;
	}
	
	@RequestMapping( value = "/pagelet/invite_users" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView pageletSmallGroup(  
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="sgid", required=false) Long smallGroupId,
			@RequestParam(value="q", required=false) String queryName,
			@RequestParam(value="page", required=false) Integer page ) throws ParseException {
		
		return pageletInviteUsers(null, sunny, securityUser, smallGroupId, queryName, page);
	}
}
