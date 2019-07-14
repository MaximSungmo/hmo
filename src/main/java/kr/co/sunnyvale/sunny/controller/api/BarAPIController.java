package kr.co.sunnyvale.sunny.controller.api;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import kr.co.sunnyvale.sunny.annotation.AuthUser;
import kr.co.sunnyvale.sunny.annotation.ParseSunny;
import kr.co.sunnyvale.sunny.domain.Channel;
import kr.co.sunnyvale.sunny.domain.Notification;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.dto.JsonResult;
import kr.co.sunnyvale.sunny.domain.dto.NoticeDTO;
import kr.co.sunnyvale.sunny.domain.dto.NotifyInfoDTO;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;

/**
 * {SpringSecurity}
 * 
 * 
 * @author kickscar
 *
 */
@Controller
public class BarAPIController {
	
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
	
	
	@RequestMapping( value = "/bar/counts", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult getBarCounts( 
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@AuthUser SecurityUser securityUser, HttpServletResponse response
			) throws IOException{
		
		LoginUtils.checkLogin(securityUser);
		
		
		User user = userService.findById( securityUser.getUserId() );

		HashMap<String, Integer> countMap = new HashMap<String, Integer>();

		Number notificationUnreadCount = notificationService.getNotificationUnreadCount(user);
		countMap.put( "noti", notificationUnreadCount.intValue());
		
//		Number friendRequestUnreadCount = friendService.getFriendRequestUnreadCount(user);
//		countMap.put( "friendRequest", friendRequestUnreadCount.intValue());
		
		Number noticeUnreadCount = storyService.getNoticeUnreadCount(sunny, user);
		countMap.put( "notice", noticeUnreadCount.intValue());
		
		Number messageUnreadCount = channelService.getChannelUnreadCount(user);
		countMap.put( "message", messageUnreadCount.intValue());
		
		return new JsonResult(true, messageSource.getMessage("bar.successGetCounts", null, LocaleUtils.getLocale()), countMap);
	}
	

	@RequestMapping( value = "/notification/infos", method = RequestMethod.GET)
	@ResponseBody
	public JsonResult getBarNotiInfo( 
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@AuthUser SecurityUser securityUser
			) {
		
		LoginUtils.checkLogin(securityUser);

		
		Map<String, NotifyInfoDTO> infosMap = Maps.newHashMap();
		
		NotifyInfoDTO notificationNotifyInfoDto = notificationService.getNotifyInfo(securityUser.getUserId());
		NotifyInfoDTO noticeNotifyInfoDto = storyService.getNoticeNotifyInfo(sunny, securityUser.getUserId());
		NotifyInfoDTO channelNotifyInfoDto = channelService.getNotifyInfo(sunny, securityUser.getUserId());

		infosMap.put("notification", notificationNotifyInfoDto);
		infosMap.put("notice", noticeNotifyInfoDto);
		infosMap.put("message", channelNotifyInfoDto);
		return new JsonResult(true, messageSource.getMessage("bar.successGetCounts", null, LocaleUtils.getLocale()), infosMap);
	}
	
	
	@RequestMapping( value = "/bar/noti_count", method = RequestMethod.GET)
//	@ResponseBody
	public synchronized void getBarNotiCounts( 
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@AuthUser SecurityUser securityUser, HttpServletResponse response
			) throws IOException{
		
		LoginUtils.checkLogin(securityUser);
		User user = new User(securityUser.getUserId() );

		Number notificationUnreadCount = notificationService.getNotificationUnreadCount(user);
		response.setStatus(HttpServletResponse.SC_OK);
		
		OutputStream out = response.getOutputStream();
		out.write(notificationUnreadCount.toString().getBytes());
		out.flush();
		out.close();

	}
	
	@RequestMapping( value = "/bar/notice_count", method = RequestMethod.GET)
//	@ResponseBody
	public synchronized void getBarNoticeCounts( 
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@AuthUser SecurityUser securityUser, HttpServletResponse response
			) throws IOException{
		
		LoginUtils.checkLogin(securityUser);
		User user = new User(securityUser.getUserId() );

		Number noticeUnreadCount = storyService.getNoticeUnreadCount(sunny, user);
		
		response.setStatus(HttpServletResponse.SC_OK);
		OutputStream out = response.getOutputStream();
		out.write(noticeUnreadCount.toString().getBytes());
		out.flush();
		out.close();
		
//		return new JsonResult(true, messageSource.getMessage("bar.successGetCounts", null, LocaleUtils.getLocale()), noticeUnreadCount.intValue());
	}
	
	@RequestMapping( value = "/bar/message_count", method = RequestMethod.GET)
//	@ResponseBody
	public synchronized void getBarMessageCounts( 
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@AuthUser SecurityUser securityUser, HttpServletResponse response
			) throws IOException{
		
		System.out.println("혹시 두번오나요?" + securityUser.getUserId());
		LoginUtils.checkLogin(securityUser);
		User user = new User(securityUser.getUserId() );
		
		Number messageUnreadCount = channelService.getChannelUnreadCount(user);
		response.setStatus(HttpServletResponse.SC_OK);
		OutputStream out = response.getOutputStream();
		out.write(messageUnreadCount.toString().getBytes());
		out.flush();
		out.close();
		
//		out.write(new JsonResult(true, "success", messageUnreadCount.intValue()).getBytes());
////		
//		out = 
//		Number messageUnreadCount = 2;
		
		
//		return null; //new JsonResult(true, "success", messageUnreadCount.intValue());
	}
	
	
	@RequestMapping( value = "/{path}/bar/noties", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult getBarNoti( 
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
										@RequestParam(value="cid", required=false) Long notiId,
										@RequestParam(value="top", required=false) Boolean top,
										@RequestParam(value="size", required=false) Integer size){
		
		LoginUtils.checkLogin(securityUser);
		
		User user = userService.findById( securityUser.getUserId() );

		List<Notification> notifications = null;

		Stream stream;
		
		if(top == null)
			top = true;
		
		if( notiId == null ){
			notifications = notificationService.getNotificationsAndUpdate(user, new Stream(10));
		}else{
			stream = new Stream(top == true ? true : false, "updateDate", notificationService.getUpdateDate(notiId), size);
			notifications = notificationService.getNotifications(user, stream);
		}
		return new JsonResult(true, messageSource.getMessage("bar.successGetNotifications", null, LocaleUtils.getLocale()), notifications);
	}
	
	@RequestMapping( value = "/{path}/bar/notice", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult getBarNotice( 
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
										@RequestParam(value="cid", required=false) Long noticeId,
										@RequestParam(value="top", required=false) Boolean top,
										@RequestParam(value="size", required=false) Integer size){
		
		LoginUtils.checkLogin(securityUser);
		
		User user = userService.findById( securityUser.getUserId() );

		List<NoticeDTO> notices = null;

		Stream stream = null;
		
		if(top == null)
			top = true;
		
		if( noticeId == null || noticeId < 1){
			notices = storyService.getNoticesAndUpdate(sunny, user, null, stream);
		}else{
			stream = new Stream(top == true ? true : false, "this.id", noticeId, size);
			notices = storyService.getNotices(sunny, user, null, stream);
		}
		return new JsonResult(true, messageSource.getMessage("bar.successGetNotifications", null, LocaleUtils.getLocale()), notices);
	}
	
	@RequestMapping( value = "/bar/notice", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult getBarNotice( 
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value = "cid", required = false) Long noticeId,
			@RequestParam(value = "top", required = false) Boolean top,
			@RequestParam(value = "size", required = false) Integer size) {

		return getBarNotice( null, sunny, securityUser, noticeId,top,size );
	}
	
//	@RequestMapping( value = "/{path}/bar/friend_requests", method = RequestMethod.GET, headers = { "Accept=application/json" })
//	@ResponseBody
//	public JsonResult getBarFollows( 
//			@PathVariable("path") String path,
//			@ParseSunny(shouldExistsSite = false) Sunny sunny,
//			@AuthUser SecurityUser securityUser,
//			@RequestParam(value="uid", required=false) Long userId, 
//			@RequestParam(value="top", required=false) Boolean top,
//			@RequestParam(value="size", required=false) Integer size ){
//		
//		
//		LoginUtils.checkLogin(securityUser);
//		
//		User user = userService.findById( securityUser.getUserId() );
//
//		List<Friend> followRelation = friendService.getNonAcceptedFriendRequests(user, new Stream(null, null, null, 100));
//
//		return new JsonResult(true,  messageSource.getMessage("bar.successGetFriendRequests", null, LocaleUtils.getLocale()), followRelation);
//	}
	
	@RequestMapping( value = "/{path}/bar/messages", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult getBarChannels( 
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
										@AuthUser SecurityUser securityUser,
										@RequestParam(value="cid", required=false) Long channelId,
										@RequestParam(value="top", required=false) Boolean top,
										@RequestParam(value="size", required=false) Integer size){
		LoginUtils.checkLogin(securityUser);
		User user = userService.findById( securityUser.getUserId() );

		if(top == null)
			top = true;

		Stream stream = null;

		List<Channel> channels = null;
		
		if( channelId == null || channelId < 1){
			channels = channelService.getChannelsAndUpdate(user, new Stream(7));
		}else{
			stream = new Stream(top == true ? true : false, "updateDate", channelService.getUpdateDate(channelId), size);
			channels = channelService.getChannels(user, stream);
		}

		return new JsonResult(true, messageSource.getMessage("bar.successGetMessages", null, LocaleUtils.getLocale()), channels);
	}

	/*
	 * MultiSiteProxy
	 */
	
	@RequestMapping( value = "/bar/noties", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult getBarNoti( 
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value = "cid", required = false) Long notiId,
			@RequestParam(value = "top", required = false) Boolean top,
			@RequestParam(value = "size", required = false) Integer size) {

		return getBarNoti( null, sunny, securityUser, notiId,top,size );

	}
	
//	@RequestMapping( value = "/bar/friend_requests", method = RequestMethod.GET, headers = { "Accept=application/json" })
//	@ResponseBody
//	public JsonResult getBarFollows( 
//			@ParseSunny(shouldExistsSite = false) Sunny sunny,
//			@AuthUser SecurityUser securityUser,
//			@RequestParam(value="uid", required=false) Long userId, 
//			@RequestParam(value="top", required=false) Boolean top,
//			@RequestParam(value="size", required=false) Integer size ){
//		return getBarFollows( null, sunny, securityUser, userId, top, size );
//	}
	
	@RequestMapping(value = "/bar/messages", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult getBarChannels(
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value = "cid", required = false) Long channelId,
			@RequestParam(value = "top", required = false) Boolean top,
			@RequestParam(value = "size", required = false) Integer size) {

		return getBarChannels(null, sunny,
				securityUser, channelId, top, size);

	}
	
}
