package kr.co.sunnyvale.sunny.controller.api;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import kr.co.sunnyvale.sunny.annotation.AuthUser;
import kr.co.sunnyvale.sunny.annotation.ParseSunny;
import kr.co.sunnyvale.sunny.domain.Pds;
import kr.co.sunnyvale.sunny.domain.Site;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.dto.JsonResult;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.domain.post.ContentPostDTO;
import kr.co.sunnyvale.sunny.exception.SimpleSunnyException;
import kr.co.sunnyvale.sunny.service.PdsService;
import kr.co.sunnyvale.sunny.service.SiteService;
import kr.co.sunnyvale.sunny.service.SmallGroupService;
import kr.co.sunnyvale.sunny.service.UserService;
import kr.co.sunnyvale.sunny.util.LoginUtils;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class PdsAPIController {
	
	@Autowired
	private PdsService pdsService;
	
	@Autowired
	private SmallGroupService smallGroupService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private SiteService siteService;

	@Autowired
	private MessageSource messageSource;
	
	@RequestMapping( value = "/{path}/pds/post", method = RequestMethod.POST, headers = { "Accept=application/json;charset=utf-8"})
	@ResponseBody
	public JsonResult post( 
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			HttpServletRequest request) {

		LoginUtils.checkLogin(securityUser);

		String requestBody = null;
		ContentPostDTO contentPost  = null;
		try{
			requestBody = IOUtils.toString(request.getInputStream());
			ObjectMapper mapper = new ObjectMapper();
			contentPost = mapper.readValue(requestBody, ContentPostDTO.class);
			contentPost.setRequestBody(requestBody);
		}catch(IOException ex){
			ex.printStackTrace();
			throw new SimpleSunnyException();
		}

		/*
		 * ReceiverId 가 존재하는 경우 : 상대방 베이스캠프에 글을 올릴 때
		 * ReceiverId 가 없는 경우       : 광장, 새소식 등에 글을 올릴 때. 즉, 내 거에 글을 올릴 때
		 */
		if( contentPost.getUserId() == null ){
			contentPost.setUserId(securityUser.getUserId());
		}
		User currentUser = userService.findById( securityUser.getUserId() );
		contentPost.setUser(currentUser);			// 담벼락 유저
		contentPost.setPostUser(currentUser);			// 담벼락 유저
		contentPost.setIpAddress(request.getRemoteAddr());
		
		Pds pds = pdsService.postPds(sunny, contentPost);
		
//		userService.changeLastSelectedPermission( securityUser.getUserId(), securityUser, storyPost.getPermission() );
		
		return new JsonResult(true, "success", pds);
	}
	
	
	@RequestMapping( value = "/pds/post", method = RequestMethod.POST, headers = { "Accept=application/json;charset=UTF-8" })
	@ResponseBody
	public JsonResult post(
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			HttpServletRequest request) {
		return post( null, sunny, securityUser, request );
	}
}
