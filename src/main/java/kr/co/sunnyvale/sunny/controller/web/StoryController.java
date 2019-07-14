package kr.co.sunnyvale.sunny.controller.web;

import kr.co.sunnyvale.sunny.annotation.AuthUser;
import kr.co.sunnyvale.sunny.annotation.ParseSunny;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.dto.StoryDTO;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.exception.PageNotFoundException;
import kr.co.sunnyvale.sunny.service.NotificationService;
import kr.co.sunnyvale.sunny.service.SiteService;
import kr.co.sunnyvale.sunny.service.SmallGroupService;
import kr.co.sunnyvale.sunny.service.StoryService;
import kr.co.sunnyvale.sunny.service.UserService;
import kr.co.sunnyvale.sunny.service.impl.OperationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
public class StoryController {
	
	@Autowired
	private StoryService storyService;
	
	@Autowired
	private SmallGroupService smallGroupService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private SiteService siteService;

	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private OperationService operationService;
	
	@Autowired
	private NotificationService notificationService;
	
	@RequestMapping( value = "/story/{id}", method = RequestMethod.GET )
	@ResponseBody
	public ModelAndView storyView(
			@PathVariable( "id" ) Long id,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestParam(value="autoread", required=false) Integer autoread){  
		return storyView(null, id, sunny, securityUser, autoread);
	}
	
	@RequestMapping( value = "/{path}/story/{id}", method = RequestMethod.GET )
	@ResponseBody
	public ModelAndView storyView(
			@PathVariable("path") String path,
			@PathVariable( "id" ) Long id,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestParam(value="autoread", required=false) Integer autoread){  
		
		User user = null;
		
		if( securityUser != null ){
			user = userService.findById(securityUser.getUserId());
		}
		
//		if( storyService.checkPermission( id, user ) == false ){
//			throw new PageNotFoundException();
//		}
		
		StoryDTO story = storyService.getStoryDTO(sunny, id, user);
		if( story == null ){
			throw new PageNotFoundException();
		}
		
		if(story.getId() == null ){
			throw new PageNotFoundException();
		}
		if( autoread != null && autoread == 1 ){
			notificationService.makeReadStoryNoties(id, user);
		}
		
		operationService.addViewCount( story.getId(), user.getId());

		ModelAndView modelAndView = new ModelAndView();
		
		modelAndView.addObject("story", story);
		modelAndView.setViewName("/story/view");
		
		return modelAndView;
	}
}
