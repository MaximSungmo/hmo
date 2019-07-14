package kr.co.sunnyvale.sunny.controller.web;

import java.text.ParseException;

import kr.co.sunnyvale.sunny.annotation.AuthUser;
import kr.co.sunnyvale.sunny.annotation.NoPathRedirect;
import kr.co.sunnyvale.sunny.annotation.ParseSunny;
import kr.co.sunnyvale.sunny.domain.BookMark;
import kr.co.sunnyvale.sunny.domain.Content;
import kr.co.sunnyvale.sunny.domain.Pds;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.security.SecurityUserService;
import kr.co.sunnyvale.sunny.service.BookMarkService;
import kr.co.sunnyvale.sunny.service.FriendService;
import kr.co.sunnyvale.sunny.service.MailService;
import kr.co.sunnyvale.sunny.service.PdsService;
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
public class BookMarkController {

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
	private BookMarkService bookMarkService;
	
	@Autowired
	private MailService mailService;
	
	@RequestMapping( value = "/{path}/bookmark" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView bookmark(  
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="tab", required=false) String tab,
			@RequestParam(value="q", required=false) String query,
			@RequestParam(value="types[]", required=false) Integer[] types,
			@RequestParam(value="ordering", required=false) String ordering,
			@RequestParam(value="desc", required=false) Boolean desc,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) Boolean isPagelet ) throws ParseException {

		ModelAndView modelAndView = new ModelAndView();
		
		if( sunny.isGoRedirect() == true ){
			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
			return modelAndView;
		}
		LoginUtils.checkLogin(securityUser);
		User user = userService.findById(securityUser.getUserId());
		Page<BookMark> pagedResult = null;
		pagedResult = bookMarkService.getPagedBookMarks(sunny, types, null, user, query, ordering, page, Page.DEFAULT_PAGE_SIZE);	
		modelAndView.addObject("pagedResult", pagedResult);
		
		if( isPagelet != null && isPagelet ){
			modelAndView.setViewName("/pagelet/bookmark/list");
		}else{
			if( types != null ){
				for( Integer typeEach : types ){
					switch( typeEach ){
					case Content.TYPE_PDS:
						modelAndView.addObject("TYPE_PDS", true);
						break;
						
					case Content.TYPE_NOTE:
						modelAndView.addObject("TYPE_NOTE", true);
						break;
					case Content.TYPE_STORY:
						modelAndView.addObject("TYPE_STORY", true);
						break;
					}
				}
			}

			
			modelAndView.setViewName("/bookmark/list");
		}
		
		return modelAndView;
	}
	

	@NoPathRedirect
	@RequestMapping( value = "/bookmark" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView bookmark(  
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="tab", required=false) String tab,
			@RequestParam(value="q", required=false) String query,
			@RequestParam(value="types[]", required=false) Integer[] types,
			@RequestParam(value="ordering", required=false) String ordering,
			@RequestParam(value="desc", required=false) Boolean desc,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) Boolean isPagelet  ) throws ParseException {
		return bookmark(null, sunny, securityUser, tab, query, types, ordering, desc, page, isPagelet);
	}
	
	
}	
