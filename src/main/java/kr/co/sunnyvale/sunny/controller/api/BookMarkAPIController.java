package kr.co.sunnyvale.sunny.controller.api;

import kr.co.sunnyvale.sunny.annotation.AuthUser;
import kr.co.sunnyvale.sunny.annotation.ParseSunny;
import kr.co.sunnyvale.sunny.domain.BookMark;
import kr.co.sunnyvale.sunny.domain.Content;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.dto.JsonResult;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.service.BookMarkService;
import kr.co.sunnyvale.sunny.service.ChannelService;
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
import org.springframework.web.bind.annotation.RequestBody;
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
public class BookMarkAPIController {
	
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
	private BookMarkService bookMarkService;
	
	/*
	 * *************************************************
	 * MultiSite
	 * *************************************************
	 */
	@RequestMapping( value = "/{path}/bookmark/check_already", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult checkBookMark( 
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam("cid") Long contentId
			){
		
		LoginUtils.checkLogin(securityUser);
		
		boolean alreadyBookMarked = bookMarkService.isAlreadyBookMark(sunny, contentId, securityUser.getUserId());
		
		return new JsonResult(true, "success", alreadyBookMarked );
	}
	
	
	
	
	@RequestMapping( value = "/{path}/contact/remove", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult removeBookMark( 
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam("cid") Long contentId
			){
		
		LoginUtils.checkLogin(securityUser);
		
		bookMarkService.removeBookMark(sunny, new Content(contentId), new User(securityUser.getUserId()));
		
		return new JsonResult(true, "success", false);
	}
	@RequestMapping( value = "/{path}/bookmark/add", method = RequestMethod.POST, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult addBookMark( 
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestBody BookMark bookmark
			){
		
		LoginUtils.checkLogin(securityUser);
		
		bookmark.setUser(new User(securityUser.getUserId()));
		
		bookMarkService.bookMark(sunny, bookmark);
		
		return new JsonResult(true, "success", true);
	}
	@RequestMapping( value = "/bookmark/add", method = RequestMethod.POST, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult addBookMark(
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestBody BookMark bookmark
			){

		return addBookMark( null, sunny, securityUser, bookmark );
	}
	
	@RequestMapping( value = "/{path}/bookmark/add", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult addBookMark( 
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam("cid") Long contentId,
			@RequestParam(value="ct", required=false) Integer contentType
			){
		
		LoginUtils.checkLogin(securityUser);
		
		bookMarkService.bookMark(sunny, new Content(contentId), contentType, new User(securityUser.getUserId()));
		
		return new JsonResult(true, "success", true);
	}
	@RequestMapping( value = "/bookmark/add", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult addBookMark(
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam("cid") Long contentId,
			@RequestParam(value="ct", required=false) Integer contentType
			){

		return addBookMark( null, sunny, securityUser,  contentId, contentType );
	}
	@RequestMapping( value = "/bookmark/remove", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult removeBookMark(
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam("cid") Long contentId
			){

		return removeBookMark( null, sunny, securityUser, contentId );
	}
	
	/*
	 * MultiSiteProxy
	 */
	
	@RequestMapping( value = "/contact/check_already", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult checkBookMark(
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam("cid") Long contentId
			){

		return checkBookMark( null, sunny, securityUser, contentId );
	}
	
	
	
	
}
