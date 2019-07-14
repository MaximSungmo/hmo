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
public class PdsController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private PdsService pdsService;

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
	
	@RequestMapping( value = "/{path}/pds" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView pds(  
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="tab", required=false) String tab,
			@RequestParam(value="q", required=false) String query,
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
		Page<Pds> pagedResult = null;
		pagedResult = pdsService.getPagedSmallGroupsPds( sunny, null, null, user, query, ordering, page, Page.DEFAULT_PAGE_SIZE);	
		modelAndView.addObject("pagedResult", pagedResult);
		modelAndView.addObject("current", "list");
		modelAndView.addObject("tab", "all");
		
		if( isPagelet != null && isPagelet ){
			modelAndView.setViewName("/pagelet/pds/list");
		}else{
			modelAndView.setViewName("/pds/index");
		}
		
		return modelAndView;
	}
	

	@NoPathRedirect
	@RequestMapping( value = "/pds" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView pds(  
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="tab", required=false) String tab,
			@RequestParam(value="q", required=false) String query,
			@RequestParam(value="ordering", required=false) String ordering,
			@RequestParam(value="desc", required=false) Boolean desc,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) Boolean isPagelet  ) throws ParseException {
		return pds(null, sunny, securityUser, tab, query, ordering, desc, page, isPagelet);
	}
	
	
	@RequestMapping( value = "/{path}/pds/my" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView myPds(  
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="tab", required=false) String tab,
			@RequestParam(value="q", required=false) String query,
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
		Page<Pds> pagedResult = pdsService.getPagedSmallGroupsPds( sunny, null, user, user, query, ordering, page, Page.DEFAULT_PAGE_SIZE);

		modelAndView.addObject("pagedResult", pagedResult);

		modelAndView.addObject("current", "list");
		modelAndView.addObject("tab", "my");
		
		if( isPagelet != null && isPagelet ){
			modelAndView.setViewName("/pagelet/pds/list");
		}else{
			modelAndView.setViewName("/pds/index");
		}
		

		return modelAndView;
	}
	

	@NoPathRedirect
	@RequestMapping( value = "/pds/my" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView myPds(  
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="tab", required=false) String tab,
			@RequestParam(value="q", required=false) String query,
			@RequestParam(value="ordering", required=false) String ordering,
			@RequestParam(value="desc", required=false) Boolean desc,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) Boolean isPagelet ) throws ParseException {
		return myPds(null, sunny, securityUser, tab, query, ordering, desc, page, isPagelet);
	}

	
	@RequestMapping( value = "/{path}/pds/favorite" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView favoritePds(  
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="tab", required=false) String tab,
			@RequestParam(value="q", required=false) String query,
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
		
		Integer[] types = new Integer[1];
		types[0] = Content.TYPE_PDS;
		pagedResult = bookMarkService.getPagedBookMarks(sunny, types, null, user, query, ordering, page, Page.DEFAULT_PAGE_SIZE);
		//Page<Pds> pagedResult = pdsService.getPagedSmallGroupsPds( sunny, null, null, user, query, ordering, page, Page.DEFAULT_PAGE_SIZE);

		modelAndView.addObject("pagedResult", pagedResult);
		modelAndView.addObject("current", "list");
		modelAndView.addObject("tab", "favorite");

		if( isPagelet != null && isPagelet ){
			modelAndView.setViewName("/pagelet/pds/list");
		}else{
			modelAndView.setViewName("/pds/index");
		}
		return modelAndView;
	}
	

	@NoPathRedirect
	@RequestMapping( value = "/pds/favorite" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView favoritePds(  
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="tab", required=false) String tab,
			@RequestParam(value="q", required=false) String query,
			@RequestParam(value="ordering", required=false) String ordering,
			@RequestParam(value="desc", required=false) Boolean desc,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) Boolean isPagelet  ) throws ParseException {
		return favoritePds(null, sunny, securityUser, tab, query, ordering, desc, page, isPagelet);
	}


	@RequestMapping( value = "/{path}/pds/write" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView pdsWrite(  
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser) throws ParseException {

		
		ModelAndView modelAndView = new ModelAndView();
		
		if( sunny.isGoRedirect() == true ){
			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
			return modelAndView;
		}
		LoginUtils.checkLogin(securityUser);
		
		modelAndView.addObject("current", "write");
		modelAndView.setViewName("/pds/index");

		return modelAndView;
	}
	

	@NoPathRedirect
	@RequestMapping( value = "/pds/write" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView pdsWrite(  
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser) throws ParseException {
		return pdsWrite(null, sunny, securityUser);
	}

	
	@RequestMapping( value = "/{path}/pds/{pdsId}" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView pdsView(  
			@PathVariable("path") String path,
			@PathVariable("pdsId") Long pdsId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="pl", required=false) Boolean isPagelet  ) {

		
		ModelAndView modelAndView = new ModelAndView();
		
		if( sunny.isGoRedirect() == true ){
			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
			return modelAndView;
		}
		LoginUtils.checkLogin(securityUser);
		
		modelAndView.addObject("content", pdsService.getPds(sunny, new User(securityUser.getUserId()), pdsId));
		
		modelAndView.addObject("alreadyBookmarked", bookMarkService.isAlreadyBookMark(sunny, pdsId, securityUser.getUserId()));
		
		modelAndView.addObject("current", "view");

		if( isPagelet != null && isPagelet ){
			modelAndView.setViewName("/pagelet/pds/view");
		}else{
			modelAndView.setViewName("/pds/index");
		}

		return modelAndView;
	}
	

	@NoPathRedirect
	@RequestMapping( value = "/pds/{pdsId}" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView pdsView(  
			@PathVariable("pdsId") Long pdsId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="pl", required=false) Boolean isPagelet  ){
		return pdsView(null,  pdsId, sunny, securityUser, isPagelet);
	}
	
}	
