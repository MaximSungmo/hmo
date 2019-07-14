package kr.co.sunnyvale.sunny.controller.api;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import kr.co.sunnyvale.sunny.annotation.AuthUser;
import kr.co.sunnyvale.sunny.annotation.ParseSunny;
import kr.co.sunnyvale.sunny.domain.Channel;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.dto.ChannelInsideDTO;
import kr.co.sunnyvale.sunny.domain.dto.ChannelListDTO;
import kr.co.sunnyvale.sunny.domain.dto.JsonResult;
import kr.co.sunnyvale.sunny.domain.dto.NotifyInfoDTO;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.service.ChannelService;
import kr.co.sunnyvale.sunny.service.MessageService;
import kr.co.sunnyvale.sunny.service.UserService;
import kr.co.sunnyvale.sunny.service.impl.ChannelUserService;
import kr.co.sunnyvale.sunny.util.LocaleUtils;
import kr.co.sunnyvale.sunny.util.LoginUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Sets;

/**
 * {SpringSecurity}
 * 
 * 
 * @author kickscar
 * 
 */
@Controller
public class ChannelAPIController {

	@Autowired
	private UserService userService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private ChannelService channelService;

	@Autowired
	private ChannelUserService channelUserService;

	@Autowired
	private MessageSource messageSource;


	/*
	 * *************************************************
	 * MultiSite *************************************************
	 */

	@RequestMapping(value = "/{path}/channel/info", method = RequestMethod.GET, headers = { "content-type=application/json,application/xml" })
	@ResponseBody
	public JsonResult insideInfo(
			@PathVariable("path") String domainName,
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam("id") Long channelId) {
		
		LoginUtils.checkLogin(securityUser);

		User user = new User(securityUser.getUserId());

//		Channel channel = channelService.info(channelId, user);
		ChannelInsideDTO channelDto = channelService.info(channelId, user);

		return new JsonResult(true, "channel Info", channelDto);
	}

	
	@RequestMapping(value = "/channel/info", method = RequestMethod.GET, headers = { "content-type=application/json,application/xml" })
	@ResponseBody
	public JsonResult insideInfo(
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam("id") Long channelId) {

		return insideInfo(null, sunny,
				securityUser, channelId);

	}
	@RequestMapping(value = "/{path}/channel/list_info", method = RequestMethod.GET, headers = { "content-type=application/json,application/xml" })
	@ResponseBody
	public JsonResult listInfo(
			@PathVariable("path") String domainName,
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam("id") Long channelId) {
		
		LoginUtils.checkLogin(securityUser);

		User user = new User(securityUser.getUserId());

//		Channel channel = channelService.info(channelId, user);
		ChannelListDTO channelDto = channelService.listInfo(channelId, user);

		return new JsonResult(true, "channel Info", channelDto);
	}

	
	@RequestMapping(value = "/channel/list_info", method = RequestMethod.GET, headers = { "content-type=application/json,application/xml" })
	@ResponseBody
	public JsonResult listInfo(
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam("id") Long channelId) {

		return listInfo(null, sunny,
				securityUser, channelId);

	}
	@RequestMapping(value = "/{path}/channel/leave", method = RequestMethod.GET, headers = { "content-type=application/json,application/xml" })
	@ResponseBody
	public JsonResult chatLeaveChannel(
			@PathVariable("path") String domainName,
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam("id") Long channelId) {
		
		LoginUtils.checkLogin(securityUser);

		User user = new User(securityUser.getUserId());

		channelUserService.leave(sunny, channelId, user);

		return new JsonResult(true, messageSource.getMessage("chat.leave", new String[]{securityUser.getName()}, LocaleUtils.getLocale()), null);
	}

	
	@RequestMapping(value = "/{path}/channel/invite", method = RequestMethod.POST, headers = { "content-type=application/json,application/xml" })
	@ResponseBody
	public JsonResult invite(@PathVariable("path") String domainName,
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value = "id", required = false) Long channelId,
			HttpServletRequest request,
			@RequestBody Long ... userIds
		) {

		LoginUtils.checkLogin(securityUser);

		
		Set<Long> userIdList = Sets.newHashSet();
		
		for( Long userId : userIds ){
			userIdList.add(userId);
		}
		
		User user = userService.findById(securityUser.getUserId());
		channelService.inviteUsers(sunny, channelId, user, userIdList);

		return new JsonResult(true, "success", null);
	}

	
	@RequestMapping(value = "/channel/leave", method = RequestMethod.GET, headers = { "content-type=application/json,application/xml" })
	@ResponseBody
	public JsonResult chatLeaveChannel(
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam("id") Long channelId) {

		return chatLeaveChannel(null, sunny,
				securityUser, channelId);

	}

	@RequestMapping(value = "/channel/invite", method = RequestMethod.POST, headers = { "content-type=application/json,application/xml" })
	@ResponseBody
	public JsonResult invite(
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value = "id", required = false) Long channelId,
			HttpServletRequest request,
			@RequestBody Long ... userIds
			) {

		return invite(null, sunny, securityUser,
				channelId, request, userIds);

	}
	

	@RequestMapping( value = "/channel/notify/get_new_one", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult getNewOne( 
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@AuthUser SecurityUser securityUser, HttpServletRequest request){
		
		
		LoginUtils.checkLogin(securityUser);
		
		User user = userService.findById( securityUser.getUserId() );

		Channel channel = null;
		
		channel = channelService.getNotifyNewOne( sunny, user );
		
		return new JsonResult(true, messageSource.getMessage("bar.successGetNotifications", null, LocaleUtils.getLocale()), channel.toNotifyDto());
	}
	
	@RequestMapping( value = "/channel/notify/info", method = RequestMethod.GET)
	@ResponseBody
	public JsonResult getBarNotiInfo( 
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@AuthUser SecurityUser securityUser
			) {
		
		LoginUtils.checkLogin(securityUser);

		NotifyInfoDTO notifyInfoDto = channelService.getNotifyInfo(sunny, securityUser.getUserId());

		return new JsonResult(true, messageSource.getMessage("bar.successGetCounts", null, LocaleUtils.getLocale()), notifyInfoDto);
	}
	

	
	
	
	
	
	
	
	
	
	
	
}
