package kr.co.sunnyvale.sunny.controller.web;

import java.util.Date;

import kr.co.sunnyvale.sunny.annotation.AuthUser;
import kr.co.sunnyvale.sunny.annotation.ParseSunny;
import kr.co.sunnyvale.sunny.domain.AuthToken;
import kr.co.sunnyvale.sunny.domain.Site;
import kr.co.sunnyvale.sunny.domain.SiteInactiveUser;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.exception.SimpleSunnyException;
import kr.co.sunnyvale.sunny.exception.SunnyException;
import kr.co.sunnyvale.sunny.service.AuthTokenService;
import kr.co.sunnyvale.sunny.service.SiteService;
import kr.co.sunnyvale.sunny.service.UserService;
import kr.co.sunnyvale.sunny.service.impl.SiteInactiveUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SiteController {
	
	@Autowired
	private SiteService siteService;
	
	@Autowired
	private MessageSource messageSource;

	@Autowired
	private UserService userService; 
	
	@Autowired
	private AuthTokenService authTokenService;
	
	@Autowired
	private SiteInactiveUserService siteInactiveUserService;
	
	@RequestMapping( value = "/site", method = RequestMethod.GET )
	@ResponseBody
	public ModelAndView site(
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="q", required=false) String query,
			@RequestParam(value="ordering", required=false) String ordering,
			@RequestParam(value="desc", required=false) Boolean desc,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) Boolean isPagelet){  
		
		User user = null;
		
//		if( securityUser != null ){
//			user = userService.findById(securityUser.getUserId());
//		}
//		

		ModelAndView modelAndView = new ModelAndView();


		Page<Site> pagedResult = siteService.getOpenSites( securityUser != null ? securityUser.getUserId() : null, query, ordering, desc, page, Page.DEFAULT_PAGE_SIZE);
		modelAndView.addObject("pagedResult", pagedResult);
		if( isPagelet != null && isPagelet ){
			modelAndView.setViewName("/pagelet/site/list");	
		}else{
			modelAndView.setViewName("/site/list");
		}
		
		
		return modelAndView;
	}
	
	@RequestMapping( value = "/site/{id}/signup", method = RequestMethod.GET )
	@ResponseBody
	public ModelAndView siteSignup(
			@PathVariable( "id" ) Long id,
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser){  
		
		User user = null;
		
		if( securityUser != null ){
			user = userService.findById(securityUser.getUserId());
		}
		

		ModelAndView modelAndView = new ModelAndView();
		
		modelAndView.addObject("site", siteService.findById(id));
		
		modelAndView.setViewName("/site/signup");	
		
		return modelAndView;
	}

	@RequestMapping( value = "/site/confirm_invite", method = RequestMethod.GET )
	@ResponseBody
	public ModelAndView siteSignup(
			@AuthUser SecurityUser securityUser, Long id, String key){  
		
		ModelAndView modelAndView = new ModelAndView();
		
		
		SiteInactiveUser siteInactiveUser = siteInactiveUserService.findById(id);
		try{
			AuthToken authToken = authTokenService.getCorrectAuthToken(siteInactiveUser, key, AuthToken.TYPE_SITE_INACTIVE_USER_INVITE);
			if( authToken == null ){
				throw new SimpleSunnyException();
			}
		}catch(SunnyException ex){
			ex.printStackTrace();
			modelAndView.setViewName( "redirect:/user/error_activate" );
			return modelAndView;
		}
		
		
		//siteService.accept(sunny, id);
		modelAndView.addObject(siteInactiveUser);
		modelAndView.setViewName("/site/inactive_user_extra_form");	
		
		
		
		return modelAndView;
	}


//	@RequestMapping( value = "/site/not_yet_invite", method = RequestMethod.GET )
//	@ResponseBody
//	public ModelAndView siteSignup(
//			@AuthUser SecurityUser securityUser,
//			@RequestParam(value="userId", required=true) String email ){  
//		
//		ModelAndView modelAndView = new ModelAndView();
//		
//		SiteInactiveUser siteInactiveUser = siteInactiveUserService.findByEmail(null, email);
//		
//		modelAndView.addObject("site", siteInactiveUser.getSite());
//		
//		modelAndView.setViewName("/site/not_yet_invite");	
//		
//		return modelAndView;
//	}
//
//	@RequestMapping( value = "/site/request_invite_email", method = RequestMethod.POST )
//	@ResponseBody
//	public ModelAndView requestInviteEmail(
//			@ParseSunny(shouldExistsSite=false) Sunny sunny,
//			@AuthUser SecurityUser securityUser, 
//			@RequestParam(value="siteId", required=true) Long siteId,
//			@RequestParam(value="userId", required=true) String email ){  
//		
//
//		ModelAndView modelAndView = new ModelAndView();
//
//		SiteInactiveUser siteInactiveUser = siteInactiveUserService.findByEmail(new Site( siteId ), email);
//		siteInactiveUser.setUpdateDate(new Date());
//		siteInactiveUserService.update(siteInactiveUser);
//		
//		modelAndView.setViewName("redirect:/site/request_invite_email");
//		return modelAndView;
//	}
//	@RequestMapping( value = "/site/request_invite_email", method = RequestMethod.GET )
//	@ResponseBody
//	public ModelAndView requestInviteEmail(
//			@AuthUser SecurityUser securityUser ){  
//		
//
//		ModelAndView modelAndView = new ModelAndView();
//
//		modelAndView.setViewName("/site/request_invite_email");
//		return modelAndView;
//	}
	
	@RequestMapping( value = "/site/{id}/accept_invite", method = RequestMethod.POST )
	@ResponseBody
	public ModelAndView acceptInvite(
			@PathVariable( "id" ) Long id,
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@ModelAttribute SiteInactiveUser extraSiteInactiveUser ){  
		

		ModelAndView modelAndView = new ModelAndView();

		siteService.signupComplete( extraSiteInactiveUser );

		modelAndView.setViewName("/site/success_request_signup");
		//modelAndView.setViewName("redirect:/" + id);
		return modelAndView;
	}
	
//	@RequestMapping( value = "/site/{id}/accept_invite", method = RequestMethod.GET )
//	@ResponseBody
//	public ModelAndView acceptInvite(
//			@PathVariable( "id" ) Long id,
//			@ParseSunny(shouldExistsSite=false) Sunny sunny,
//			@AuthUser SecurityUser securityUser, 
//			@RequestParam(value="siteInactiveUserId", required=true) Long siteInactiveUserId ){
//		
//		
//		if( securityUser == null ){
//			throw new SimpleSunnyException();
//		}
//		
//
//		ModelAndView modelAndView = new ModelAndView();
//
//		User siteUser = siteService.acceptInvite( new SiteInactiveUser( siteInactiveUserId ) );
//		
//		modelAndView.setViewName("redirect:/" + siteUser.getSite().getId());
//		return modelAndView;
//	}
	@RequestMapping( value = "/site/{id}/signup", method = RequestMethod.POST )
	@ResponseBody
	public ModelAndView postSiteSignup(
			@PathVariable( "id" ) Long id,
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@ModelAttribute SiteInactiveUser siteInactiveUser ){  
		
		ModelAndView modelAndView = new ModelAndView();

		siteService.requestSignup( sunny, siteInactiveUser );
		
		
		modelAndView.setViewName("/site/success_request_signup");
		return modelAndView;
	}
}
