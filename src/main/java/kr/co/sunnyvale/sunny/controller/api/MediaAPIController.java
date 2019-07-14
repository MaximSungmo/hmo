package kr.co.sunnyvale.sunny.controller.api;

import javax.servlet.http.HttpServletRequest;

import kr.co.sunnyvale.sunny.annotation.AuthUser;
import kr.co.sunnyvale.sunny.domain.Media;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.dto.JsonResult;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.repository.hibernate.UserRepository;
import kr.co.sunnyvale.sunny.service.MediaService;
import kr.co.sunnyvale.sunny.service.SmallGroupService;
import kr.co.sunnyvale.sunny.service.StoryService;
import kr.co.sunnyvale.sunny.service.UserInfoService;
import kr.co.sunnyvale.sunny.service.UserService;
import kr.co.sunnyvale.sunny.util.LocaleUtils;
import kr.co.sunnyvale.sunny.util.LoginUtils;
import kr.co.sunnyvale.sunny.util.RequestUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class MediaAPIController {
	
	@Autowired
	private StoryService storyService;

	@Autowired
	private UserService userService;

	private SmallGroupService smallGroupService;
	
	@Autowired
	private UserInfoService userInfoService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private MediaService mediaService;
	
	@Autowired
	private MessageSource messageSource;
	
	/*
	 * *************************************************
	 * MultiSite
	 * *************************************************
	 */
	
	@RequestMapping( value = "/{path}/media/addtocontent", method = RequestMethod.POST, headers = { "Accept=application/json" } )
	@ResponseBody
	public JsonResult addToContent(
			@PathVariable("path") String path, 
			@AuthUser SecurityUser securityUser,
			@RequestBody Media media, 
			HttpServletRequest request) {

		LoginUtils.checkLogin(securityUser);
		media.setUser( new User( securityUser.getUserId() ));
		mediaService.save(media);

		return new JsonResult(true, "success", media.getId());
	}
	
	@RequestMapping( value = "/media/addtocontent", method = RequestMethod.POST, headers = { "Accept=application/json" } )
	@ResponseBody
	public JsonResult addToContent(
			@AuthUser SecurityUser securityUser,
			@RequestBody Media media, 
			HttpServletRequest request) {

		return addToContent(null, securityUser, media, request);
	}
	
	@RequestMapping( value = "/{path}/media/add", method = RequestMethod.POST, headers = { "Accept=application/json" } )
	@ResponseBody
	public JsonResult add(
			@PathVariable("path") String path, 
			@AuthUser SecurityUser securityUser, 
			@RequestBody Media media, 
			HttpServletRequest request) {


		LoginUtils.checkLogin(securityUser);
		media.setUser( new User( securityUser.getUserId() ));
		mediaService.save(media);
		
		return new JsonResult(true, "success", media.getId());
	}
	
	@RequestMapping( value = "/media/add", method = RequestMethod.POST, headers = { "Accept=application/json" } )
	@ResponseBody
	public JsonResult add(
			@AuthUser SecurityUser securityUser, 
			@RequestBody Media media, 
			HttpServletRequest request) {


		return add(null, securityUser, media, request);
	}
	
	@RequestMapping( value = "/{path}/media/addToTargetDraft", method = RequestMethod.POST, headers = { "Accept=application/json" } )
	@ResponseBody
	public JsonResult addToTargetDraft(
			@PathVariable("path") String path,
			@AuthUser SecurityUser securityUser, 
			@RequestBody Media media, 
			HttpServletRequest request) {

		LoginUtils.checkLogin(securityUser);
		
		media.setUser( new User( securityUser.getUserId() ));
		mediaService.saveToTargetDraft(media);

		return new JsonResult(true, "success", media.getId());
	}
	
	@RequestMapping( value = "/media/addToTargetDraft", method = RequestMethod.POST, headers = { "Accept=application/json" } )
	@ResponseBody
	public JsonResult addToTargetDraft(
			@AuthUser SecurityUser securityUser, 
			@RequestBody Media media, 
			HttpServletRequest request) {

		return addToTargetDraft(null, securityUser, media, request);
	}
	@RequestMapping( value = "/{path}/media/delfromcontent", method = RequestMethod.GET, headers = { "Accept=application/json" } )
	@ResponseBody
	public JsonResult delete(
			@PathVariable("path") String path, 
			@AuthUser SecurityUser securityUser, 
			@RequestParam( value="mid", required=true) Long mediaId,
			@RequestParam( value="cid", required=false) Long contentId,	
			HttpServletRequest request) {


		LoginUtils.checkLogin(securityUser);
		mediaService.delFromContent(mediaId);

		return new JsonResult(true, "success", null);
	}
	
	@RequestMapping( value = "/media/delfromcontent", method = RequestMethod.GET, headers = { "Accept=application/json" } )
	@ResponseBody
	public JsonResult delete(
			@AuthUser SecurityUser securityUser, 
			@RequestParam( value="mid", required=true) Long mediaId,
			@RequestParam( value="cid", required=false) Long contentId,	
			HttpServletRequest request) {

		return delete(null, securityUser, mediaId, contentId, request);
	}
	
	@RequestMapping( value = "/{path}/media/delfromdraft", method = RequestMethod.GET, headers = { "Accept=application/json" } )
	@ResponseBody
	public JsonResult deleteFromDraft(
			@PathVariable("path") String path, 
			@AuthUser SecurityUser securityUser, 
			@RequestParam( value="mid", required=true) Long mediaId,
			@RequestParam( value="did", required=false) Long draftId,	
			HttpServletRequest request) {


		LoginUtils.checkLogin(securityUser);
//		mediaService.delFromContent(mediaId);

		mediaService.delFromDraft( mediaId );
		return new JsonResult(true, "success", null);
	}
	
	@RequestMapping( value = "/media/delfromdraft", method = RequestMethod.GET, headers = { "Accept=application/json" } )
	@ResponseBody
	public JsonResult deleteFromDraft(
			@AuthUser SecurityUser securityUser, 
			@RequestParam( value="mid", required=true) Long mediaId,
			@RequestParam( value="did", required=false) Long draftId,	
			HttpServletRequest request) {

		return deleteFromDraft(null, securityUser, mediaId, draftId, request);
	}
	
	@RequestMapping( value = "/{path}/media/del", method = RequestMethod.GET, headers = { "Accept=application/json" } )
	@ResponseBody
	public JsonResult del(
			@PathVariable("path") String path,
			@AuthUser SecurityUser securityUser,
			@RequestParam( value="mid", required=true) Long mediaId,
			@RequestParam( value="cid", required=false) Long contentId,
			HttpServletRequest request) {


		LoginUtils.checkLogin(securityUser);
		
		mediaService.del(mediaId);

		return new JsonResult(true, "success", null);
	}
	
	@RequestMapping( value = "/media/del", method = RequestMethod.GET, headers = { "Accept=application/json" } )
	@ResponseBody
	public JsonResult del(
			@AuthUser SecurityUser securityUser,
			@RequestParam( value="mid", required=true) Long mediaId,
			@RequestParam( value="cid", required=false) Long contentId,
			HttpServletRequest request) {

		return del(null, securityUser, mediaId, contentId, request);
	}
	
//	@RequestMapping( value = "/{path}/media/removeprofile", method = RequestMethod.GET, headers = { "Accept=application/json" } )
//	@ResponseBody
//	public JsonResult removeProfile(@PathVariable("path") String path, @AuthUser SecurityUser securityUser, HttpServletRequest request) {
//
//
//		LoginUtils.checkLogin(securityUser);
//		
//		userService.removeProfile( securityUser.getUserId() );
//
//		return new JsonResult(true, messageSource.getMessage("media.successDeleteProfile", null, LocaleUtils.getLocale()), null);
//	}
//	
	
//	@RequestMapping( value = "/{path}/media/addprofile", method = RequestMethod.POST, headers = { "Accept=application/json" } )
//	@ResponseBody
//	public JsonResult addProfile(@PathVariable("path") String path, @AuthUser SecurityUser securityUser, 
//								@RequestBody Media media,
//			HttpServletRequest request) {
//
//		LoginUtils.checkLogin(securityUser);
//		
//		media.setUser( new User( securityUser.getUserId() ));
//		mediaService.save(media);
//		userService.updateProfilePic( securityUser.getUserId(), media);
//		
//		
//		SecurityContext securityContext = SecurityContextHolder.getContext();
//		Authentication authentication = securityContext.getAuthentication();
//		securityUser.setProfilePic( media.getSmallSrc() );
//		SecurityContextHolder.getContext().setAuthentication( new UsernamePasswordAuthenticationToken( securityUser, authentication.getCredentials(), securityUser.getAuthorities() ) );	
//		
//		return new JsonResult(true, messageSource.getMessage("media.successSaveProfile", null, LocaleUtils.getLocale()), null);
//	}

	
	/*
	 * MultiSiteProxy
	 */
	
	
	
}
