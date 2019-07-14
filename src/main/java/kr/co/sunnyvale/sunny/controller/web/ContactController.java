package kr.co.sunnyvale.sunny.controller.web;

import java.text.ParseException;
import java.util.List;

import kr.co.sunnyvale.sunny.annotation.AuthUser;
import kr.co.sunnyvale.sunny.annotation.NoPathRedirect;
import kr.co.sunnyvale.sunny.annotation.ParseSunny;
import kr.co.sunnyvale.sunny.domain.SmallGroup;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.security.SecurityUserService;
import kr.co.sunnyvale.sunny.service.FriendService;
import kr.co.sunnyvale.sunny.service.MailService;
import kr.co.sunnyvale.sunny.service.SiteService;
import kr.co.sunnyvale.sunny.service.SmallGroupService;
import kr.co.sunnyvale.sunny.service.StoryService;
import kr.co.sunnyvale.sunny.service.UserService;
import kr.co.sunnyvale.sunny.util.LoginUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Lists;

@Controller
public class ContactController {

	@Autowired
	private UserService userService;

	@Autowired
	private SecurityUserService securityUserService;
	
	@Autowired
	private SiteService siteService;
	
	@Autowired
	private StoryService storyService;
	
	@Autowired
	private FriendService friendRequestService;
	
	@Autowired
	private SmallGroupService smallGroupService;
	
	@Autowired
	private MailService mailService;
	
	
	
	
	@RequestMapping( value = "/{path}/contact" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView contact(  
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
			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
			return modelAndView;
		}
		LoginUtils.checkLogin(securityUser);
		
		Page<User> pagedResult = userService.getUserList(sunny, null, queryType, queryName, status, range, ordering, null, null, page, Page.DEFAULT_PAGE_SIZE);

		modelAndView.addObject("pagedResult", pagedResult);
		
		
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
		
		modelAndView.setViewName("/contact/list");

		return modelAndView;
	}
	

	@NoPathRedirect
	@RequestMapping( value = "/contact" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView contact(  
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="q", required=false) String queryName,
			@RequestParam(value="qt", required=false) Integer queryType,
			@RequestParam(value="status[]", required=false) Integer[] status,
			@RequestParam(value="range", required=false) String range,
			@RequestParam(value="ordering", required=false) String ordering,
			@RequestParam(value="page", required=false) Integer page ) throws ParseException {
		
		return contact(null, sunny, securityUser, queryName, queryType, status, range, ordering, page);
				
	}
	
	@RequestMapping( value = "/{path}/contact/groups" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView contactGroups(  
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="page", required=false) Integer page) throws ParseException {

		ModelAndView modelAndView = new ModelAndView();
		if( sunny.isGoRedirect() == true ){
			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
			return modelAndView;
		}
		LoginUtils.checkLogin(securityUser);
		
		User user = new User(securityUser.getUserId());
		
		Page<SmallGroup> pagedResult  = smallGroupService.getContactSmallGroupPage(sunny.getSite(), null, page, 1000);

		modelAndView.addObject("pagedResult", pagedResult);
		
		modelAndView.setViewName("/contact/groups");

		return modelAndView;
	}
	
	
	

	@NoPathRedirect
	@RequestMapping( value = "/contact/groups" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView contactGroups(  
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="page", required=false) Integer page ) throws ParseException {
		
		return contactGroups(null, sunny, securityUser, page);
				
	}
	
	@RequestMapping( value = "/{path}/contact/groups/{smallGroupId}" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView contactGroupsUsers(  
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

		SmallGroup contactSmallGroup = smallGroupService.getSmallGroup(smallGroupId);
		
		
		modelAndView.addObject("contactSmallGroup", contactSmallGroup);
		
		modelAndView.setViewName("/contact/group_users");

		return modelAndView;
	}
	
	
	

	@NoPathRedirect
	@RequestMapping( value = "/contact/groups/{smallGroupId}" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView contactGroupsUsers(  
			@PathVariable("smallGroupId") Long smallGroupId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="page", required=false) Integer page ) throws ParseException {
		
		return contactGroupsUsers(null, smallGroupId, sunny, securityUser, page);
				
	}
	
	
	@RequestMapping( value = "/{path}/contact/favorites" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView favorites(  
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="page", required=false) Integer page) throws ParseException {

		
		ModelAndView modelAndView = new ModelAndView();
		
		if( sunny.isGoRedirect() == true ){
			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
			return modelAndView;
		}
		LoginUtils.checkLogin(securityUser);
		
		User user = userService.findById(securityUser.getUserId());
		
		Page<User> pagedResult = userService.getFavoriteUsers(sunny, user, page, 10000);

		modelAndView.addObject("pagedResult", pagedResult);
		
		modelAndView.setViewName("/contact/favorites");

		return modelAndView;
	}
	
	
	

	@NoPathRedirect
	@RequestMapping( value = "/contact/favorites" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView favorites(  
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="page", required=false) Integer page ) throws ParseException {
		
		return favorites(null, sunny, securityUser, page);
				
	}
}	
