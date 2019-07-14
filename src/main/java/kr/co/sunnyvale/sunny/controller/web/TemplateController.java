package kr.co.sunnyvale.sunny.controller.web;

import java.text.ParseException;

import kr.co.sunnyvale.sunny.annotation.AuthUser;
import kr.co.sunnyvale.sunny.annotation.NoPathRedirect;
import kr.co.sunnyvale.sunny.annotation.ParseSunny;
import kr.co.sunnyvale.sunny.domain.Template;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.security.SecurityUserService;
import kr.co.sunnyvale.sunny.service.FriendService;
import kr.co.sunnyvale.sunny.service.MailService;
import kr.co.sunnyvale.sunny.service.SiteService;
import kr.co.sunnyvale.sunny.service.SmallGroupService;
import kr.co.sunnyvale.sunny.service.StoryService;
import kr.co.sunnyvale.sunny.service.TemplateService;
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
public class TemplateController {

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
	
	@Autowired
	private TemplateService templateService;
	

	@RequestMapping( value = "/template/pay" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView payTemplate(  
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="pl", required=false) Boolean isPagelet ) throws ParseException {

		ModelAndView modelAndView = new ModelAndView();
		
		LoginUtils.checkLogin(securityUser);
		
		modelAndView.setViewName("/template/pay");
		
		return modelAndView;
	}
	
	@RequestMapping( value = "/{path}/template/{id}" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView template(  
			@PathVariable("path") String path,
			@PathVariable("id") Long templateId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="pl", required=false) Boolean isPagelet ) throws ParseException {

		ModelAndView modelAndView = new ModelAndView();
		
		LoginUtils.checkLogin(securityUser);

		Template template = templateService.find(templateId);
		
		modelAndView.addObject("template", template);
		
		modelAndView.setViewName("/template/view");
		
		return modelAndView;
	}
	

	@NoPathRedirect
	@RequestMapping( value = "/template/{id}" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView template(  
			@PathVariable("id") Long templateId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="pl", required=false) Boolean isPagelet  ) throws ParseException {
		return template(null, templateId, sunny, securityUser, isPagelet);
	}
	
}	
