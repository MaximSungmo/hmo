package kr.co.sunnyvale.sunny.controller.popup;

import java.text.ParseException;
import java.util.List;

import kr.co.sunnyvale.sunny.annotation.ParseSunny;
import kr.co.sunnyvale.sunny.domain.Site;
import kr.co.sunnyvale.sunny.domain.dto.StoryDTO;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.repository.hibernate.MailingQueueRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.SiteRepository;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PopupController {

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
	private MailingQueueRepository mailingQueueRepository;
	
	@Autowired
	private SiteInactiveUserService siteInactiveUserService; 
	
	@Autowired
	private SiteRepository siteRepository;
	
	
	@RequestMapping( value = "/super/email/{id}" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView popEmail(  
			@PathVariable("id") Long id) throws ParseException {
		
		
		ModelAndView modelAndView = new ModelAndView();
		
		modelAndView.addObject("item", mailingQueueRepository.select(id));
		
		modelAndView.setViewName("/super/popup/email");

		return modelAndView;
	}
	
	@RequestMapping( value = "/super/site/{id}", method = RequestMethod.GET )
	public ModelAndView sitesStory(
			@PathVariable("id") Long siteId,
			@ParseSunny(shouldExistsSite=false) Sunny sunny){
		ModelAndView modelAndView = new ModelAndView();
		Site site = siteRepository.findFromSiteId(siteId);
		
		modelAndView.addObject("site", site);
		List<StoryDTO> stories = storyService.getSuperAdminStories(sunny, siteId, null, null, null);
		
		modelAndView.addObject("userCount", userService.getUserCount(siteId, null));
		
		modelAndView.addObject("stories", stories);

		modelAndView.setViewName("/super/popup/site");
		return modelAndView;
	}
	
}
