package kr.co.sunnyvale.sunny.controller.api;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import kr.co.sunnyvale.sunny.annotation.AuthUser;
import kr.co.sunnyvale.sunny.annotation.ParseSunny;
import kr.co.sunnyvale.sunny.domain.Site;
import kr.co.sunnyvale.sunny.domain.Story;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.dto.JsonResult;
import kr.co.sunnyvale.sunny.domain.dto.StoryDTO;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.domain.post.StoryPostDTO;
import kr.co.sunnyvale.sunny.exception.SimpleSunnyException;
import kr.co.sunnyvale.sunny.service.PdsService;
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

@Controller
public class SuperAdminAPIController {
	
	@Autowired
	private PdsService pdsService;
	
	@Autowired
	private SmallGroupService smallGroupService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private SiteService siteService;
	
	@Autowired
	private StoryService storyService;

	@Autowired
	private MessageSource messageSource;
	
	@RequestMapping( value = "/super/story_stream", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult lobbyStream(
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
		List<StoryDTO> plazaStories = storyService.fetchSuperStories(sunny, user, stream);
		
		if( plazaStories == null ){
			return new JsonResult(false, messageSource.getMessage("story.noStories", null, LocaleUtils.getLocale()), null);
		}

		return new JsonResult(true, messageSource.getMessage("story.successFetchStories", null, LocaleUtils.getLocale()), plazaStories);
	}
	
	@RequestMapping( value = "/super/post_story", method = RequestMethod.POST, headers = { "Accept=application/json;charset=utf-8"})
	@ResponseBody
	public JsonResult post( 
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
		if( storyPost.getUserId() == null ){
			storyPost.setUserId(securityUser.getUserId());
		}
		User currentUser = userService.findById( User.SUPER_USER );
		storyPost.setUser(currentUser);			// 담벼락 유저
		storyPost.setPostUser(currentUser);			// 담벼락 유저
		storyPost.setIpAddress(request.getRemoteAddr());
		
		Story story = storyService.postSuperStory(sunny, storyPost);
		
		return new JsonResult(true, "true", story);
	}
}
