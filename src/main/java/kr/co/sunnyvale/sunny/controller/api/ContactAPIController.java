package kr.co.sunnyvale.sunny.controller.api;

import java.text.ParseException;

import kr.co.sunnyvale.sunny.annotation.AuthUser;
import kr.co.sunnyvale.sunny.annotation.ParseSunny;
import kr.co.sunnyvale.sunny.domain.FavoriteUser;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.dto.JsonResult;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.exception.SimpleSunnyException;
import kr.co.sunnyvale.sunny.service.ChannelService;
import kr.co.sunnyvale.sunny.service.FavoriteUserService;
import kr.co.sunnyvale.sunny.service.FriendService;
import kr.co.sunnyvale.sunny.service.MessageService;
import kr.co.sunnyvale.sunny.service.NotificationService;
import kr.co.sunnyvale.sunny.service.StoryService;
import kr.co.sunnyvale.sunny.service.UserService;
import kr.co.sunnyvale.sunny.util.LoginUtils;

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
public class ContactAPIController {
	
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
	private FavoriteUserService favoriteUserService;
	
	/*
	 * *************************************************
	 * MultiSite
	 * *************************************************
	 */
	@RequestMapping( value = "/{path}/contact/check_favorite", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult checkFavorite( 
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam("uid") Long userId
			){
		
		LoginUtils.checkLogin(securityUser);
		
		boolean alreadyFavorited = favoriteUserService.isAlreadyFavorited(sunny, userId, securityUser.getUserId());
		
		return new JsonResult(true, "success", alreadyFavorited);
	}
	
	
	@RequestMapping( value = "/{path}/contact/add_favorite", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult addFavorite( 
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam("uid") Long userId
			){
		
		LoginUtils.checkLogin(securityUser);
		
		favoriteUserService.favorite(sunny, new User(userId), new User(securityUser.getUserId()));
		
		return new JsonResult(true, "success", true);
	}
	
	@RequestMapping( value = "/{path}/contact/remove_favorite", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult removeFavorite( 
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam("uid") Long userId
			){
		
		LoginUtils.checkLogin(securityUser);
		
		favoriteUserService.removeFavorite(sunny, new User(userId), new User(securityUser.getUserId()));
		
		return new JsonResult(true, "success", false);
	}
	
	@RequestMapping( value = "/contact/add_favorite", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult addFavorite(
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam("uid") Long userId
			){

		return addFavorite( null, sunny, securityUser, userId );
	}
	@RequestMapping( value = "/contact/remove_favorite", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult removeFavorite(
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam("uid") Long userId
			){

		return removeFavorite( null, sunny, securityUser, userId );
	}
	
	/*
	 * MultiSiteProxy
	 */
	
	@RequestMapping( value = "/contact/check_favorite", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult checkFavorite(
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam("uid") Long userId
			){

		return checkFavorite( null, sunny, securityUser, userId );
	}
	
	
	
	
}
