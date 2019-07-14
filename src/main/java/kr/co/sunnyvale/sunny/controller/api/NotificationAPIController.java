package kr.co.sunnyvale.sunny.controller.api;

import javax.servlet.http.HttpServletRequest;

import kr.co.sunnyvale.sunny.annotation.AuthUser;
import kr.co.sunnyvale.sunny.annotation.ParseSunny;
import kr.co.sunnyvale.sunny.domain.Notification;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * {SpringSecurity}
 * 
 * 
 * @author kickscar
 *
 */
@Controller
public class NotificationAPIController {
	
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
	
	
	
	/*
	 * *************************************************
	 * MultiSite
	 * *************************************************
	 */
	
	
//	@RequestMapping( value = "/notifications/make_read", method = RequestMethod.GET, headers = { "Accept=application/json" })
//	@ResponseBody
//	public JsonResult notificationMakeRead(
//			@ParseSunny(shouldExistsSite = false) Sunny sunny,
//			@AuthUser SecurityUser securityUser,
//			@RequestParam(value="nid", required=false) Long notificationId
//			){
//		
//		LoginUtils.checkLogin(securityUser);
//
//		
//		notificationService.makeRead( sunny, securityUser.getUserId(), notificationId );
//		
//		return new JsonResult(true, "success", null);
//	}
	
//	@RequestMapping( value = "/{path}/bar/count", method = RequestMethod.GET, headers = { "Accept=application/json" })
//	@ResponseBody
//	public JsonResult getBarCounts( 
//			@PathVariable("path") String path,
//			@ParseSunny(shouldExistsSite = false) Sunny sunny,
//			@AuthUser SecurityUser securityUser,
//			@RequestParam(value="cpc", required=false) Integer checkPlazaCount
//			){
//		
//		LoginUtils.checkLogin(securityUser);
//		
//		
//		User user = userService.findById( securityUser.getUserId() );
//
//		HashMap<String, Integer> countMap = new HashMap<String, Integer>();
//
//		Number notificationUnreadCount = notificationService.getNotificationUnreadCount(user);
//		countMap.put( "noti", notificationUnreadCount.intValue());
//		
////		Number friendRequestUnreadCount = friendService.getFriendRequestUnreadCount(user);
////		countMap.put( "friendRequest", friendRequestUnreadCount.intValue());
//		
//		Number noticeUnreadCount = storyService.getNoticeUnreadCount(sunny, user);
//		countMap.put( "notice", noticeUnreadCount.intValue());
//		
//		Number messageUnreadCount = channelService.getChannelUnreadCount(user);
//		countMap.put( "message", messageUnreadCount.intValue());
//		
//		return new JsonResult(true, messageSource.getMessage("bar.successGetCounts", null, LocaleUtils.getLocale()), countMap);
//	}

	@RequestMapping( value = "/notification/make_read", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult notificationMakeRead(
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="nid", required=false) Long notificationId
			){
		
		LoginUtils.checkLogin(securityUser);

		
//		notificationService.makeReadStoryNoties( story.getId(), user );

		notificationService.makeRead( sunny, securityUser.getUserId(), notificationId );
		
		return new JsonResult(true, "success", null);
	}
	
	@RequestMapping( value = "/notification/get_new_one", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult getNewOne( 
			@AuthUser SecurityUser securityUser, HttpServletRequest request){
		
		
		LoginUtils.checkLogin(securityUser);
		
		User user = userService.findById( securityUser.getUserId() );

		Notification notification = null;

		
		notification = notificationService.geNewOne( user );
		
		return new JsonResult(true, messageSource.getMessage("bar.successGetNotifications", null, LocaleUtils.getLocale()), notification.toNotificationDto());
	}
	
	@RequestMapping( value = "/notification/info", method = RequestMethod.GET)
	@ResponseBody
	public JsonResult getBarNotiInfo( 
			@AuthUser SecurityUser securityUser
			) {
		
		LoginUtils.checkLogin(securityUser);

		NotifyInfoDTO notifyInfoDto = notificationService.getNotifyInfo(securityUser.getUserId());

		return new JsonResult(true, messageSource.getMessage("bar.successGetCounts", null, LocaleUtils.getLocale()), notifyInfoDto);
	}
	
}
