package kr.co.sunnyvale.sunny.controller.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Qualifier;
import javax.servlet.http.HttpServletRequest;

import kr.co.sunnyvale.sunny.annotation.AuthUser;
import kr.co.sunnyvale.sunny.annotation.NoPathRedirect;
import kr.co.sunnyvale.sunny.annotation.ParseSunny;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.dto.StoryDTO;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.service.StoryService;
import kr.co.sunnyvale.sunny.service.UserService;
import kr.co.sunnyvale.sunny.util.SearchUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CsController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private StoryService storyService; 

	
	
//	@RequestMapping( value="/test/loginUsers")
//	public ModelAndView loginUsers( HttpServletRequest request ){
//		
//		ModelAndView modelAndView = new ModelAndView();
//		
//		List<Object> principals = sessionRegistry.getAllPrincipals();
//		
//		System.out.println(principals);
//		
//		modelAndView.setViewName( "/test/loginUsers");
//		return modelAndView;
//		
//	}
	
	@RequestMapping( value = "/cs/privacy" )
	public ModelAndView privacy() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/cs/privacy");
		return modelAndView;
	}
	
	@RequestMapping( value = "/cs/policies" )
	public ModelAndView policies() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/cs/policies");
		return modelAndView;
	}
	
	@RequestMapping( value = "/cs/location" )
	public ModelAndView location() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/cs/location");
		return modelAndView;
	}
}
