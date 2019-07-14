package kr.co.sunnyvale.sunny.controller.api;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import kr.co.sunnyvale.sunny.annotation.AuthUser;
import kr.co.sunnyvale.sunny.annotation.ParseSunny;
import kr.co.sunnyvale.sunny.domain.Site;
import kr.co.sunnyvale.sunny.domain.SmallGroup;
import kr.co.sunnyvale.sunny.domain.Story;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.dto.JsonResult;
import kr.co.sunnyvale.sunny.domain.dto.StoryDTO;
import kr.co.sunnyvale.sunny.domain.dto.StoryModifyDTO;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.domain.post.StoryModifyPostDTO;
import kr.co.sunnyvale.sunny.domain.post.StoryPostDTO;
import kr.co.sunnyvale.sunny.exception.SimpleSunnyException;
import kr.co.sunnyvale.sunny.service.SiteService;
import kr.co.sunnyvale.sunny.service.SmallGroupService;
import kr.co.sunnyvale.sunny.service.StoryService;
import kr.co.sunnyvale.sunny.service.UserService;
import kr.co.sunnyvale.sunny.util.LocaleUtils;
import kr.co.sunnyvale.sunny.util.LoginUtils;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * {SpringSecurity}
 * 
 * 
 * @author kickscar
 *
 */
@Controller
public class StoryAPIController {
	
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
	
	
	@RequestMapping( value = "/{path}/story/{id}", method = RequestMethod.GET, headers = { "Accept=application/json" } )
	@ResponseBody
	public JsonResult storyView(
			@PathVariable("path") String path,
			@PathVariable( "id" ) Long id,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser){  
		
		User user = null;
		
		if( securityUser != null ){
			user = userService.findById(securityUser.getUserId());
		}
		
//		if( storyService.checkPermission( id, user ) == false ){
//			return new JsonResult(false, messageSource.getMessage("story.noPermission", null, LocaleUtils.getLocale()), null);
//		}
//		
		StoryDTO story = storyService.getStoryDTO(sunny, id, user);
		if( story == null ){
			return new JsonResult(false, messageSource.getMessage("story.noExist", null, LocaleUtils.getLocale()), null);
		}
		
		if(story.getId() == null ){
			return new JsonResult(false, messageSource.getMessage("story.noExist", null, LocaleUtils.getLocale()), story);
		}
		return new JsonResult(true, "success", story);
	}
	@RequestMapping( value = "/story/{id}", method = RequestMethod.GET, headers = { "Accept=application/json" } )
	@ResponseBody
	public JsonResult storyView(
			@PathVariable( "id" ) Long id,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser){  
		return storyView(null, id, sunny, securityUser);
	}
	
	@RequestMapping( value = "/{path}/story/{id}/modify", method = RequestMethod.GET, headers = { "Accept=application/json" } )
	@ResponseBody
	public JsonResult storyModify(
			@PathVariable("path") String path,
			@PathVariable( "id" ) Long id,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser){  
		
		User user = null;
		
		if( securityUser != null ){
			user = userService.findById(securityUser.getUserId());
		}
		
//		if( storyService.checkPermission( id, user ) == false ){
//			return new JsonResult(false, messageSource.getMessage("story.noPermission", null, LocaleUtils.getLocale()), null);
//		}
		
		StoryModifyDTO storyDto = storyService.getModifyStory(sunny, user, id);
		if( storyDto == null ){
			return new JsonResult(false, messageSource.getMessage("story.noExist", null, LocaleUtils.getLocale()), null);
		}
		
		return new JsonResult(true, "success", storyDto);
	}
	@RequestMapping( value = "/story/{id}/modify", method = RequestMethod.GET, headers = { "Accept=application/json" } )
	@ResponseBody
	public JsonResult storyModify(
			@PathVariable( "id" ) Long id,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser){  
		return storyModify(null, id, sunny, securityUser);
	}
	
	
	@RequestMapping( value = "/{path}/story/{id}/modify", method = RequestMethod.POST, headers = { "Accept=application/json;charset=utf-8"})
	@ResponseBody
	public JsonResult storyModifyUpdate( 
			@PathVariable("path") String path,
			@PathVariable( "id" ) Long id,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			HttpServletRequest request) {

		LoginUtils.checkLogin(securityUser);

		String requestBody = null;
		StoryModifyPostDTO storyModify = null;
		try{
			requestBody = IOUtils.toString(request.getInputStream());
			ObjectMapper mapper = new ObjectMapper();
			storyModify = mapper.readValue(requestBody, StoryModifyPostDTO.class);
			storyModify.setRequestBody(requestBody);
		}catch(IOException ex){
			ex.printStackTrace();
			throw new SimpleSunnyException();
		}

		/*
		 * ReceiverId 가 존재하는 경우 : 상대방 베이스캠프에 글을 올릴 때
		 * ReceiverId 가 없는 경우       : 광장, 새소식 등에 글을 올릴 때. 즉, 내 거에 글을 올릴 때
		 */
		Story story = storyService.modifyStory(sunny, id, storyModify);
		
//		userService.changeLastSelectedPermission( securityUser.getUserId(), securityUser, storyPost.getPermission() );
		
		return new JsonResult(true, messageSource.getMessage("story.successPostStory", null, LocaleUtils.getLocale()), story);
	}
	
	
	@RequestMapping( value = "/story/{id}/modify", method = RequestMethod.POST, headers = { "Accept=application/json;charset=UTF-8" })
	@ResponseBody
	public JsonResult storyModifyUpdate(
			@PathVariable( "id" ) Long id,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			HttpServletRequest request) {
		return storyModifyUpdate( null, id, sunny, securityUser, request );
	}
	
	@RequestMapping( value = "/{path}/story/{id}/permissions", method = RequestMethod.GET, headers = { "Accept=application/json" } )
	@ResponseBody
	public JsonResult storyPermissions(
			@PathVariable("path") String path,
			@PathVariable( "id" ) Long id,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="sgId", required=false) Long smallGroupId,
			@RequestParam(value="top", required=false) Boolean top, 
			@RequestParam(value="size", required=false) Integer size){  
		
		User user = null;
		
		if( securityUser != null ){
			user = userService.findById(securityUser.getUserId());
		}
		
//		if( storyService.checkPermission( id, user ) == false ){
//			return new JsonResult(false, messageSource.getMessage("story.noPermission", null, LocaleUtils.getLocale()), null);
//		}
		
		//StoryDTO story = storyService.getStoryDTO(sunny, id, user);
		
		Stream stream;
		
		if(top == null)
			top = true;
		
		if( smallGroupId == null )
			stream = new Stream();
		else
			stream = new Stream(top , "this.id", smallGroupId, size);
		
		List<SmallGroup> result = smallGroupService.getContentAssignedSmallGroups(sunny, id, stream);
		
		return new JsonResult(true, "success", result);
	}
	
	
	@RequestMapping( value = "/{path}/story/delete" , method = RequestMethod.GET, headers = { "Accept=application/json" } )
	@ResponseBody
	public JsonResult delete(
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestParam(value="id", required=false) Long storyId) {
		LoginUtils.checkLogin(securityUser);
		
		storyService.delete(storyId);

		return new JsonResult(true, messageSource.getMessage("story.successDeleteStory", null, LocaleUtils.getLocale()), null);
	}

	@RequestMapping( value = "/story/delete" , method = RequestMethod.GET, headers = { "Accept=application/json" } )
	@ResponseBody
	public JsonResult delete(
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestParam(value="id", required=false) Long storyId) {
		LoginUtils.checkLogin(securityUser);
		
		storyService.delete(storyId);

		return new JsonResult(true, messageSource.getMessage("story.successDeleteStory", null, LocaleUtils.getLocale()), null);
	}

	
	@RequestMapping( value = "/{path}/story/post", method = RequestMethod.POST, headers = { "Accept=application/json;charset=utf-8"})
	@ResponseBody
	public JsonResult post( 
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			HttpServletRequest request) {

		LoginUtils.checkLogin(securityUser);
		Site site = sunny.getSite();

		String requestBody = null;
		StoryPostDTO storyPost  = null;
		try{
			requestBody = IOUtils.toString(request.getInputStream());
			ObjectMapper mapper = new ObjectMapper();
			storyPost = mapper.readValue(requestBody, StoryPostDTO.class);
			storyPost.setRequestBody(requestBody);
		}catch(IOException ex){
			ex.printStackTrace();
			throw new SimpleSunnyException();
		}

		/*
		 * ReceiverId 가 존재하는 경우 : 상대방 베이스캠프에 글을 올릴 때
		 * ReceiverId 가 없는 경우       : 광장, 새소식 등에 글을 올릴 때. 즉, 내 거에 글을 올릴 때
		 */
		Long receiverId = storyPost.getReceiverId();
		if( storyPost.getUserId() == null ){
			storyPost.setUserId(securityUser.getUserId());
		}
		User currentUser = userService.findById( storyPost.getUserId() );
		currentUser.setMySmallGroup( new SmallGroup( securityUser.getMySmallGroupId() ));
		if( receiverId != null  && !receiverId.equals(securityUser.getUserId())){
			storyPost.setUser(new User( storyPost.getReceiverId() ) );			// 담벼락 유저
			storyPost.setPostUser(currentUser );		// 보내는 사람
		}else{
			storyPost.setUser(currentUser);			// 담벼락 유저
			storyPost.setPostUser(currentUser);			// 담벼락 유저
		}
		storyPost.setIpAddress(request.getRemoteAddr());
		
		Story story = storyService.postStory(sunny, storyPost);
		
//		userService.changeLastSelectedPermission( securityUser.getUserId(), securityUser, storyPost.getPermission() );
		
		return new JsonResult(true, messageSource.getMessage("story.successPostStory", null, LocaleUtils.getLocale()), story);
	}
	
	
	@RequestMapping( value = "/story/post", method = RequestMethod.POST, headers = { "Accept=application/json;charset=UTF-8" })
	@ResponseBody
	public JsonResult post(
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			HttpServletRequest request) {
		System.out.println("hello2");
		return post( null, sunny, securityUser, request );
	}
	
	
	@RequestMapping( value = "/{path}/newsfeed", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult newsfeedStream(
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestParam("contentId") Long contentId,  
			@RequestParam(value="q[]", required=false) String[] queries,
			@RequestParam(value="top", required=false) Boolean top, 
			@RequestParam(value="size", required=false) Integer size){
	
		LoginUtils.checkLogin(securityUser);
		
		
		Stream stream;
		
		if(top == null)
			top = true;
		
		if( contentId == null )
			stream = new Stream();
		else
			stream = new Stream(top == true ? true : false, "this.id", contentId, size);
		
		User user = userService.findById( securityUser.getUserId() );
		List<StoryDTO> plazaStories = storyService.fetchNewsfeedStories( sunny, user, queries, stream);
		
		if( plazaStories == null ){
			return new JsonResult(false, messageSource.getMessage("story.noStories", null, LocaleUtils.getLocale()), null);
		}

		return new JsonResult(true, messageSource.getMessage("story.successFetchStories", null, LocaleUtils.getLocale()), plazaStories);
	}
	
	/*
	 * MultiSiteProxy
	 */
	@RequestMapping( value = "/newsfeed", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult newsfeedStream(
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestParam("contentId") Long contentId,  
			@RequestParam(value="q[]", required=false) String[] queries,
			@RequestParam(value="top", required=false) Boolean top, 
			@RequestParam(value="size", required=false) Integer size){
	
		return newsfeedStream( null, sunny, securityUser, contentId, queries, top, size);
	}

	
	@RequestMapping( value = "/{path}/lobby", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult lobbyStream(
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestParam("contentId") Long contentId,  
			@RequestParam(value="q[]", required=false) String[] queries,
			@RequestParam(value="top", required=false) Boolean top, 
			@RequestParam(value="size", required=false) Integer size){
	
		Stream stream;
		
		if(top == null)
			top = true;
		
		if( contentId == null )
			stream = new Stream();
		else
			stream = new Stream(top == true ? true : false, "this.id", contentId, size);
		
		User user = null; 
		
		if( securityUser != null ){
			user = userService.findById( securityUser.getUserId() );
		}
		List<StoryDTO> plazaStories = storyService.fetchLobbyStories( sunny, user, queries, stream);
		
		if( plazaStories == null ){
			return new JsonResult(false, messageSource.getMessage("story.noStories", null, LocaleUtils.getLocale()), null);
		}

		return new JsonResult(true, messageSource.getMessage("story.successFetchStories", null, LocaleUtils.getLocale()), plazaStories);
	}
	
	
	@RequestMapping( value = "/lobby", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult lobbyStream(
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestParam("contentId") Long contentId,  
			@RequestParam(value="q[]", required=false) String[] queries,
			@RequestParam(value="top", required=false) Boolean top, 
			@RequestParam(value="size", required=false) Integer size){
	
		return lobbyStream( null, sunny, securityUser, contentId, queries, top, size);
	}
	
	@RequestMapping( value = "/{path}", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult indexStream(
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestParam("contentId") Long contentId,  
			@RequestParam(value="q[]", required=false) String[] queries,
			@RequestParam(value="top", required=false) Boolean top, 
			@RequestParam(value="size", required=false) Integer size){
	
		return lobbyStream( null, sunny, securityUser, contentId, queries, top, size);
	}
	
	@RequestMapping( value = "/{path}/user/{userId}", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult basecampStream(
			@PathVariable("path") String path,
			@PathVariable("userId") Long userId,
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestParam("contentId") Long contentId,  
			@RequestParam(value="q[]", required=false) String[] queries,
			@RequestParam(value="top", required=false) Boolean top, 
			@RequestParam(value="size", required=false) Integer size){
	
		Stream stream;
		
		if(top == null)
			top = true;
		
		if( contentId == null )
			stream = new Stream();
		else
			stream = new Stream(top == true ? true : false, "this.id", contentId, size);
		
		User user = null; 
		
		if( securityUser != null ){
			user = userService.findById( securityUser.getUserId() );
		}
		User basecampUser = userService.findById(userId);
		List<StoryDTO> stories = storyService.fetchBasecampStories(sunny, basecampUser, user, queries, stream);
		
		if( stories == null ){
			return new JsonResult(false, messageSource.getMessage("story.noStories", null, LocaleUtils.getLocale()), null);
		}

		return new JsonResult(true, messageSource.getMessage("story.successFetchStories", null, LocaleUtils.getLocale()), stories);
	}
	
	@RequestMapping( value = "/user/{userId}", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult basecampStream(
			@PathVariable("userId") Long userId,
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestParam("contentId") Long contentId,  
			@RequestParam(value="q[]", required=false) String[] queries,
			@RequestParam(value="top", required=false) Boolean top, 
			@RequestParam(value="size", required=false) Integer size){
	
		return basecampStream( null, userId, sunny, securityUser, contentId, queries, top, size);
	}

	@RequestMapping( value = "/{path}/feedback", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult feedbackStream(
			@PathVariable("path") String path,
			@AuthUser SecurityUser securityUser, 
			@RequestParam("contentId") Long contentId,  
			@RequestParam(value="q[]", required=false) String[] queries,
			@RequestParam(value="top", required=false) Boolean top, 
			@RequestParam(value="size", required=false) Integer size){
	
		Stream stream;
		
		if(top == null)
			top = true;
		
		if( contentId == null )
			stream = new Stream();
		else
			stream = new Stream(top == true ? true : false, "this.id", contentId, size);
		
		User user = null; 
		
		if( securityUser != null ){
			user = userService.findById( securityUser.getUserId() );
		}
		List<StoryDTO> stories = storyService.fetchFeedback(user, queries, stream);
		
		if( stories == null ){
			return new JsonResult(false, messageSource.getMessage("story.noStories", null, LocaleUtils.getLocale()), null);
		}

		return new JsonResult(true, messageSource.getMessage("story.successFetchStories", null, LocaleUtils.getLocale()), stories);
	}
	
	
	@RequestMapping( value = "/feedback", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult feedbackStream(
			@AuthUser SecurityUser securityUser, 
			@RequestParam("contentId") Long contentId,  
			@RequestParam(value="q[]", required=false) String[] queries,
			@RequestParam(value="top", required=false) Boolean top, 
			@RequestParam(value="size", required=false) Integer size){
	
		return feedbackStream( null, securityUser, contentId, queries, top, size);
	}
}
