package kr.co.sunnyvale.sunny.controller.api;

import javax.servlet.http.HttpServletRequest;

import kr.co.sunnyvale.sunny.annotation.AuthUser;
import kr.co.sunnyvale.sunny.annotation.ParseSunny;
import kr.co.sunnyvale.sunny.domain.Story;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.dto.JsonResult;
import kr.co.sunnyvale.sunny.domain.dto.NotifyInfoDTO;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.service.ChannelService;
import kr.co.sunnyvale.sunny.service.FriendService;
import kr.co.sunnyvale.sunny.service.MessageService;
import kr.co.sunnyvale.sunny.service.NotificationService;
import kr.co.sunnyvale.sunny.service.StoryService;
import kr.co.sunnyvale.sunny.service.UserService;
import kr.co.sunnyvale.sunny.util.LocaleUtils;
import kr.co.sunnyvale.sunny.util.LoginUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * {SpringSecurity}
 * 
 * 
 * @author kickscar
 *
 */
@Controller
public class NoticeAPIController {
	
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
	
	
	
	@RequestMapping( value = "/notice/get_new_one", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult getNewOne( 
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@AuthUser SecurityUser securityUser, HttpServletRequest request){
		
		
		LoginUtils.checkLogin(securityUser);
		
		User user = userService.findById( securityUser.getUserId() );

		Story story = null;
		
		story = storyService.geNoticeNewOne( sunny, user );
		
		return new JsonResult(true, messageSource.getMessage("bar.successGetNotifications", null, LocaleUtils.getLocale()), story.toNotifyDto());
	}
	
	@RequestMapping( value = "/notice/info", method = RequestMethod.GET)
	@ResponseBody
	public JsonResult getBarNotiInfo( 
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@AuthUser SecurityUser securityUser
			) {
		
		LoginUtils.checkLogin(securityUser);

		NotifyInfoDTO notifyInfoDto = storyService.getNoticeNotifyInfo(sunny, securityUser.getUserId());

		return new JsonResult(true, messageSource.getMessage("bar.successGetCounts", null, LocaleUtils.getLocale()), notifyInfoDto);
	}
	
}
