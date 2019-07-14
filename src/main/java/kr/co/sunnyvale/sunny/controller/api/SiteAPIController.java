package kr.co.sunnyvale.sunny.controller.api;

import kr.co.sunnyvale.sunny.annotation.AuthUser;
import kr.co.sunnyvale.sunny.annotation.ParseSunny;
import kr.co.sunnyvale.sunny.domain.SiteInactiveUser;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.dto.JsonResult;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.service.AuthTokenService;
import kr.co.sunnyvale.sunny.service.SiteService;
import kr.co.sunnyvale.sunny.service.UserService;
import kr.co.sunnyvale.sunny.service.impl.SiteInactiveUserService;
import kr.co.sunnyvale.sunny.util.LocaleUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * {SpringSecurity}
 * 
 * 
 * @author kickscar
 *
 */
@Controller
public class SiteAPIController {
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

	@ResponseBody
	@RequestMapping(value = "/site/{id}/signup", method = RequestMethod.POST, headers = { "Accept=application/json" })
	public JsonResult signup(
			@PathVariable( "id" ) Long id,
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
							@RequestBody SiteInactiveUser siteInactiveUser){
		

		ModelAndView modelAndView = new ModelAndView();


		siteService.requestSignup( sunny, siteInactiveUser );
		
		
		modelAndView.setViewName("/site/success_request_signup");
		return new JsonResult(true, messageSource.getMessage("user.successJoined", null, LocaleUtils.getLocale()), null);
	}
	

}
