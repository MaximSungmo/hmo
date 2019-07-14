package kr.co.sunnyvale.sunny.controller.web;

import java.text.ParseException;

import kr.co.sunnyvale.sunny.annotation.AuthUser;
import kr.co.sunnyvale.sunny.annotation.ParseSunny;
import kr.co.sunnyvale.sunny.domain.Media;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.extend.MediaType;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.security.SecurityUserService;
import kr.co.sunnyvale.sunny.service.FriendService;
import kr.co.sunnyvale.sunny.service.MailService;
import kr.co.sunnyvale.sunny.service.MediaService;
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

@Controller
public class MediaController {

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
	private MediaService mediaService;
	
	@Autowired
	private MailService mailService;
	
	@RequestMapping( value = "/{path}/media" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView media(  
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="q", required=false) String query,
			@RequestParam(value="my", required=false) Integer isMy,
			@RequestParam(value="types[]", required=false) MediaType[] types,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) Boolean isPagelet ) throws ParseException {

		LoginUtils.checkLogin(securityUser);
		
		ModelAndView modelAndView = new ModelAndView();
		Page<Media> pagedResult = null;
		User user = new User( securityUser.getUserId() );
		pagedResult = mediaService.getPagedResult(sunny, null, user, query, isMy, types, page, Page.DEFAULT_PAGE_SIZE);
		modelAndView.addObject("pagedResult", pagedResult);
		
		if( types != null ){
			for( MediaType typeEach : types ){
				modelAndView.addObject("TYPE_" + typeEach.name(), true);
//				switch( typeEach ){
//				case Media.TYPE_IMAGE:
//					modelAndView.addObject("TYPE_IMAGE", true);
//					break;
//					
//				case Media.TYPE_OTHER_FILE:
//					modelAndView.addObject("TYPE_OTHER", true);
//					break;
//				}
			}
		}
		
		if( isPagelet != null && isPagelet ){
			modelAndView.setViewName("/pagelet/media/list");
		}else{
			modelAndView.setViewName("/media/list");	
		}
		
		
		return modelAndView;
	}
	


	@RequestMapping( value = "/media" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView media(  
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="q", required=false) String query,
			@RequestParam(value="my", required=false) Integer isMy,
			@RequestParam(value="types[]", required=false) MediaType[] types,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) Boolean isPagelet  ) throws ParseException {
		return media(null, sunny, securityUser, query, isMy, types, page, isPagelet);
	}
	
	
}	
