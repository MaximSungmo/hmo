package kr.co.sunnyvale.sunny.controller.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import kr.co.sunnyvale.sunny.annotation.AuthUser;
import kr.co.sunnyvale.sunny.annotation.ParseSunny;
import kr.co.sunnyvale.sunny.annotation.SmallGroupPrivacy;
import kr.co.sunnyvale.sunny.domain.SmallGroup;
import kr.co.sunnyvale.sunny.domain.Tag;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.UserSmallGroupAccess;
import kr.co.sunnyvale.sunny.domain.dto.JsonResult;
import kr.co.sunnyvale.sunny.domain.dto.StoryDTO;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.domain.extend.SmallGroupTree;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.domain.post.ContentPermissionDTO;
import kr.co.sunnyvale.sunny.domain.post.SmallGroupRegistPostDTO;
import kr.co.sunnyvale.sunny.service.ChannelService;
import kr.co.sunnyvale.sunny.service.FavoriteUserService;
import kr.co.sunnyvale.sunny.service.FriendService;
import kr.co.sunnyvale.sunny.service.MessageService;
import kr.co.sunnyvale.sunny.service.NotificationService;
import kr.co.sunnyvale.sunny.service.SmallGroupService;
import kr.co.sunnyvale.sunny.service.SmallGroupSmallGroupAccessService;
import kr.co.sunnyvale.sunny.service.StoryService;
import kr.co.sunnyvale.sunny.service.TagService;
import kr.co.sunnyvale.sunny.service.UserService;
import kr.co.sunnyvale.sunny.service.UserSmallGroupAccessService;
import kr.co.sunnyvale.sunny.util.LocaleUtils;
import kr.co.sunnyvale.sunny.util.LoginUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Lists;

/**
 * {SpringSecurity}
 * 
 * 
 * @author kickscar
 *
 */
@Controller
public class SmallGroupAPIController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private UserSmallGroupAccessService userSmallGroupAccessService;
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
	private SmallGroupService smallGroupService;
	
	@Autowired
	private FavoriteUserService favoriteUserService;
	
	@Autowired
	private SmallGroupSmallGroupAccessService smallGroupSmallGroupAccessService;
	
	@Autowired
	private TagService tagService;

	@ResponseBody
	@RequestMapping(value = "/{path}/group", method = RequestMethod.POST, headers = { "Accept=application/json" })
	public JsonResult createGroup( 
							@PathVariable("path") String path,
							@ParseSunny(shouldExistsSite=true) Sunny sunny,
							@AuthUser SecurityUser securityUser,
							@RequestBody SmallGroupRegistPostDTO registDto ){

		LoginUtils.checkLogin(securityUser);

		User user = new User(securityUser.getUserId());
		SmallGroup smallGroup = smallGroupService.save(sunny, user, registDto);
		

		return new JsonResult(true, "success", smallGroup);
	}
	
	@ResponseBody
	@RequestMapping(value = "/group", method = RequestMethod.POST, headers = { "Accept=application/json" })
	public JsonResult createGroup( 
							@ParseSunny(shouldExistsSite=true) Sunny sunny,
							@AuthUser SecurityUser securityUser,
							@RequestBody SmallGroupRegistPostDTO userRegistDto ){

		return createGroup(null, sunny, securityUser, userRegistDto);
	}
	

	@SmallGroupPrivacy( onlyAdmin = true )
	@ResponseBody
	@RequestMapping( value = "/{path}/group/{id}/setting", method = RequestMethod.POST, headers = { "Accept=application/json" })
	public JsonResult update( 
			@PathVariable("path") String path,
			@PathVariable("id") Long smallGroupId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestBody SmallGroup smallGroup, 
			HttpServletRequest request) {

		smallGroupService.update(smallGroup);
		return new JsonResult(true, "success", smallGroup);
		
	}
	
	@SmallGroupPrivacy( onlyAdmin = true )
	@ResponseBody
	@RequestMapping( value = "/group/{id}/setting", method = RequestMethod.POST, headers = { "Accept=application/json" })
	public JsonResult update( 
			@PathVariable("id") Long smallGroupId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestBody SmallGroup smallGroup, 
			HttpServletRequest request) {

		return update(null, smallGroupId, sunny, securityUser, smallGroup, request);
		
	}
	
	@ResponseBody
	@RequestMapping(value = "/{path}/group/invite_users", method = RequestMethod.POST, headers = { "Accept=application/json" })
	public JsonResult inviteUsers( 
							@PathVariable("path") String path,
							@ParseSunny(shouldExistsSite=true) Sunny sunny,
							@AuthUser SecurityUser securityUser,
							@RequestParam(value="sgid", required=true) Long smallGroupId,
							@RequestBody List<Long> userIds ){

		LoginUtils.checkLogin(securityUser);

		User user = userService.findById(securityUser.getUserId());

		smallGroupService.inviteUsers(sunny, smallGroupId, user, userIds, true);

		return new JsonResult(true, "success", null);
	}
	
	@ResponseBody
	@RequestMapping(value = "/group/invite_users", method = RequestMethod.POST, headers = { "Accept=application/json" })
	public JsonResult inviteUsers( 
							@ParseSunny(shouldExistsSite=true) Sunny sunny,
							@AuthUser SecurityUser securityUser,
							@RequestParam(value="sgid", required=true) Long smallGroupId,
							@RequestBody List<Long> userIds ){

		return inviteUsers(null, sunny, securityUser, smallGroupId, userIds);
	}

	
	@ResponseBody
	@RequestMapping(value = "/{path}/group/access", method = RequestMethod.POST, headers = { "Accept=application/json" })
	public JsonResult addAccess( 
							@PathVariable("path") String path,
							@ParseSunny(shouldExistsSite=true) Sunny sunny,
							@AuthUser SecurityUser securityUser,
							@RequestParam(value="sgid", required=true) Long smallGroupId,
							@RequestBody List<ContentPermissionDTO> permissionDtos ){

		LoginUtils.checkLogin(securityUser);

		User user = new User(securityUser.getUserId());

		smallGroupService.addAccesses(sunny, smallGroupId, user, permissionDtos);

		return new JsonResult(true, "success", null);
	}
	
	@ResponseBody
	@RequestMapping(value = "/group/access", method = RequestMethod.POST, headers = { "Accept=application/json" })
	public JsonResult addAccess( 
							@ParseSunny(shouldExistsSite=true) Sunny sunny,
							@AuthUser SecurityUser securityUser,
							@RequestParam(value="sgid", required=true) Long smallGroupId,
							@RequestBody List<ContentPermissionDTO> permissionDtos ){

		return addAccess(null, sunny, securityUser, smallGroupId, permissionDtos);
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/{path}/group/{id}/add_user", method = RequestMethod.GET, headers = { "Accept=application/json" })
	public JsonResult addUser( 
							@PathVariable("path") String path,
							@PathVariable("id") Long smallGroupId,
							@ParseSunny(shouldExistsSite=true) Sunny sunny,
							@AuthUser SecurityUser securityUser,
							@RequestParam(value="uid", required=true) Long userId){

		LoginUtils.checkLogin(securityUser);

		User user = new User(securityUser.getUserId());
		List<Long> userIds = Lists.newArrayList();
		userIds.add(userId);
		smallGroupService.inviteUsers(sunny, smallGroupId, user, userIds, true);

		return new JsonResult(true, "success", null);
	}


	@ResponseBody
	@RequestMapping(value = "/group/{id}/add_user", method = RequestMethod.GET, headers = { "Accept=application/json" })
	public JsonResult addUser( 
							@ParseSunny(shouldExistsSite=true) Sunny sunny,
							@PathVariable("id") Long smallGroupId,
							@AuthUser SecurityUser securityUser,
							@RequestParam(value="uid", required=true) Long userId){

		return addUser(null, smallGroupId, sunny, securityUser, userId);
	}

	@ResponseBody
	@RequestMapping(value = "/{path}/group/{id}/accept_user", method = RequestMethod.GET, headers = { "Accept=application/json" })
	public JsonResult acceptUser( 
							@PathVariable("path") String path,
							@PathVariable("id") Long smallGroupId,
							@ParseSunny(shouldExistsSite=true) Sunny sunny,
							@AuthUser SecurityUser securityUser,
							@RequestParam(value="uid", required=true) Long userId){

		LoginUtils.checkLogin(securityUser);

		User user = new User(securityUser.getUserId());
		smallGroupService.acceptUser(sunny, smallGroupId, user, userId);

		return new JsonResult(true, "success", null);
	}


	@ResponseBody
	@RequestMapping(value = "/group/{id}/accept_user", method = RequestMethod.GET, headers = { "Accept=application/json" })
	public JsonResult acceptUser( 
							@ParseSunny(shouldExistsSite=true) Sunny sunny,
							@PathVariable("id") Long smallGroupId,
							@AuthUser SecurityUser securityUser,
							@RequestParam(value="uid", required=true) Long userId){

		return acceptUser(null, smallGroupId, sunny, securityUser, userId);
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/{path}/group/{id}/remove_user", method = RequestMethod.GET, headers = { "Accept=application/json" })
	public JsonResult removeUser( 
							@PathVariable("path") String path,
							@PathVariable("id") Long smallGroupId,
							@ParseSunny(shouldExistsSite=true) Sunny sunny,
							@AuthUser SecurityUser securityUser,
							@RequestParam(value="uid", required=true) Long userId){

		LoginUtils.checkLogin(securityUser);

		smallGroupService.removeUserFromSmallGroup(userId, smallGroupId);
		
		smallGroupService.removeInactiveUserFromSmallGroup( userId, smallGroupId );
		
		return new JsonResult(true, "success", null);
	}


	@ResponseBody
	@RequestMapping(value = "/group/{id}/remove_user", method = RequestMethod.GET, headers = { "Accept=application/json" })
	public JsonResult removeUser( 
							@ParseSunny(shouldExistsSite=true) Sunny sunny,
							@PathVariable("id") Long smallGroupId,
							@AuthUser SecurityUser securityUser,
							@RequestParam(value="uid", required=true) Long userId){

		return removeUser(null, smallGroupId, sunny, securityUser, userId);
	}

	@ResponseBody
	@RequestMapping(value = "/{path}/group/{id}/joinadmin", method = RequestMethod.GET, headers = { "Accept=application/json" })
	public JsonResult joinAdmin( 
							@PathVariable("path") String path,
							@PathVariable("id") Long smallGroupId,
							@ParseSunny(shouldExistsSite=true) Sunny sunny,
							@AuthUser SecurityUser securityUser,
							@RequestParam(value="uid", required=true) Long userId){

		LoginUtils.checkLogin(securityUser);

		User user = new User(securityUser.getUserId());
		smallGroupService.joinAdmin(sunny, smallGroupId, user, userId);

		return new JsonResult(true, "success", null);
	}


	@ResponseBody
	@RequestMapping(value = "/group/{id}/joinadmin", method = RequestMethod.GET, headers = { "Accept=application/json" })
	public JsonResult joinAdmin( 
							@ParseSunny(shouldExistsSite=true) Sunny sunny,
							@PathVariable("id") Long smallGroupId,
							@AuthUser SecurityUser securityUser,
							@RequestParam(value="uid", required=true) Long userId){

		return joinAdmin(null, smallGroupId, sunny, securityUser, userId);
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/{path}/group/{id}/removeadmin", method = RequestMethod.GET, headers = { "Accept=application/json" })
	public JsonResult removeAdmin( 
							@PathVariable("path") String path,
							@PathVariable("id") Long smallGroupId,
							@ParseSunny(shouldExistsSite=true) Sunny sunny,
							@AuthUser SecurityUser securityUser,
							@RequestParam(value="uid", required=true) Long userId){

		LoginUtils.checkLogin(securityUser);

		smallGroupService.removeAdmin(userId, smallGroupId);
		
//		smallGroupService.removeInactiveUserFromSmallGroup( userId, smallGroupId );
		
		return new JsonResult(true, "success", null);
	}


	@ResponseBody
	@RequestMapping(value = "/group/{id}/removeadmin", method = RequestMethod.GET, headers = { "Accept=application/json" })
	public JsonResult removeAdmin( 
							@ParseSunny(shouldExistsSite=true) Sunny sunny,
							@PathVariable("id") Long smallGroupId,
							@AuthUser SecurityUser securityUser,
							@RequestParam(value="uid", required=true) Long userId){

		return removeAdmin(null, smallGroupId, sunny, securityUser, userId);
	}

	
	@ResponseBody
	@RequestMapping(value = "/{path}/group/{id}/tag_names", method = RequestMethod.GET, headers = { "Accept=application/json" })
	public JsonResult getTagNames( 
							@PathVariable("path") String path,
							@PathVariable("id") Long smallGroupId,
							@ParseSunny(shouldExistsSite=true) Sunny sunny,
							@AuthUser SecurityUser securityUser){

		LoginUtils.checkLogin(securityUser);

		User user = new User(securityUser.getUserId());

		List<String> tagNames = tagService.getSmallGroupTagNames( smallGroupId );
		
		return new JsonResult(true, "success", tagNames);
	}


	@ResponseBody
	@RequestMapping(value = "/group/{id}/tag_names", method = RequestMethod.GET, headers = { "Accept=application/json" })
	public JsonResult getTagNames( 
							@ParseSunny(shouldExistsSite=true) Sunny sunny,
							@PathVariable("id") Long smallGroupId,
							@AuthUser SecurityUser securityUser){

		return getTagNames(null, smallGroupId, sunny, securityUser);
	}
	@ResponseBody
	@RequestMapping(value = "/{path}/group/{id}/tags", method = RequestMethod.GET, headers = { "Accept=application/json" })
	public JsonResult getTags( 
							@PathVariable("path") String path,
							@PathVariable("id") Long smallGroupId,
							@ParseSunny(shouldExistsSite=true) Sunny sunny,
							@AuthUser SecurityUser securityUser){

		LoginUtils.checkLogin(securityUser);

		User user = new User(securityUser.getUserId());

		
		List<Tag> tags = tagService.getSmallGroupTags( smallGroupId );
		return new JsonResult(true, "success", tags);
	}


	@ResponseBody
	@RequestMapping(value = "/group/{id}/tags", method = RequestMethod.GET, headers = { "Accept=application/json" })
	public JsonResult getTags( 
							@ParseSunny(shouldExistsSite=true) Sunny sunny,
							@PathVariable("id") Long smallGroupId,
							@AuthUser SecurityUser securityUser){

		return getTags(null, smallGroupId, sunny, securityUser);
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/{path}/group/{id}/composer_tags", method = RequestMethod.POST, headers = { "Accept=application/json" })
	public JsonResult postComposerTags( 
							@PathVariable("path") String path,
							@PathVariable("id") Long smallGroupId,
							@ParseSunny(shouldExistsSite=true) Sunny sunny,
							@AuthUser SecurityUser securityUser,
							@RequestBody List<Tag> tags ){

		LoginUtils.checkLogin(securityUser);

		tagService.setSmallGroupComposerTags( smallGroupId, tags );

		return new JsonResult(true, "success", null);
	}
	
	@ResponseBody
	@RequestMapping(value = "/group/{id}/composer_tags", method = RequestMethod.POST, headers = { "Accept=application/json" })
	public JsonResult postComposerTags( 
							@PathVariable("id") Long smallGroupId,
							@ParseSunny(shouldExistsSite=true) Sunny sunny,
							@AuthUser SecurityUser securityUser,
							@RequestBody List<Tag> tags ){

		return postComposerTags(null, smallGroupId, sunny, securityUser, tags);
	}
	
//	@ResponseBody
//	@RequestMapping(value = "/{path}/group/{id}/update_permission", method = RequestMethod.GET, headers = { "Accept=application/json" })
//	public JsonResult addPermission( 
//							@PathVariable("path") String path,
//							@PathVariable("id") Long smallGroupId,
//							@ParseSunny(shouldExistsSite=true) Sunny sunny,
//							@AuthUser SecurityUser securityUser,
//							@RequestParam(value="sgsgid", required=true) Long smallGroupSmallGroupId,
//							@RequestParam(value="r", required=false) Boolean readPermission,
//							@RequestParam(value="w", required=false) Boolean writePermission,
//							@RequestParam(value="d", required=false) Boolean deletePermission){
//
//		LoginUtils.checkLogin(securityUser);
//
//		smallGroupSmallGroupAccessService.updatePermission(smallGroupSmallGroupId, readPermission, writePermission, deletePermission);
//		
////		smallGroupService.removeInactiveUserFromSmallGroup( userId, smallGroupId );
//		
//		return new JsonResult(true, "success", null);
//	}


//	@ResponseBody
//	@RequestMapping(value = "/group/{id}/update_permission", method = RequestMethod.GET, headers = { "Accept=application/json" })
//	public JsonResult removeAdmin( 
//							@ParseSunny(shouldExistsSite=true) Sunny sunny,
//							@PathVariable("id") Long smallGroupId,
//							@AuthUser SecurityUser securityUser,
//							@RequestParam(value="sgsgid", required=true) Long smallGroupSmallGroupId,
//							@RequestParam(value="r", required=false) Boolean readPermission,
//							@RequestParam(value="w", required=false) Boolean writePermission,
//							@RequestParam(value="d", required=false) Boolean deletePermission){
//
//		return addPermission(null, smallGroupId, sunny, securityUser, smallGroupSmallGroupId, readPermission, writePermission, deletePermission);
//	}
	
	
	@ResponseBody
	@RequestMapping(value = "/{path}/group/{id}/remove_access", method = RequestMethod.GET, headers = { "Accept=application/json" })
	public JsonResult removeAccess( 
							@PathVariable("path") String path,
							@PathVariable("id") Long smallGroupId,
							@ParseSunny(shouldExistsSite=true) Sunny sunny,
							@AuthUser SecurityUser securityUser,
							@RequestParam(value="sgsgid", required=true) Long smallGroupSmallGroupId){

		LoginUtils.checkLogin(securityUser);

		smallGroupSmallGroupAccessService.remove(smallGroupSmallGroupId);
		
		return new JsonResult(true, "success", null);
	}


	@ResponseBody
	@RequestMapping(value = "/group/{id}/remove_access", method = RequestMethod.GET, headers = { "Accept=application/json" })
	public JsonResult removeAccess( 
							@ParseSunny(shouldExistsSite=true) Sunny sunny,
							@PathVariable("id") Long smallGroupId,
							@AuthUser SecurityUser securityUser,
							@RequestParam(value="sgsgid", required=true) Long smallGroupSmallGroupId){

		return removeAccess(null, smallGroupId, sunny, securityUser, smallGroupSmallGroupId);
	}

	
	@ResponseBody
	@RequestMapping(value = "/{path}/group/{id}/remove", method = RequestMethod.GET, headers = { "Accept=application/json" })
	public JsonResult removeSmallGroup( 
							@PathVariable("path") String path,
							@PathVariable("id") Long smallGroupId,
							@ParseSunny(shouldExistsSite=true) Sunny sunny,
							@AuthUser SecurityUser securityUser){

		LoginUtils.checkLogin(securityUser);

		UserSmallGroupAccess smallGroupUser = userSmallGroupAccessService.findBySmallGroupAndUser( smallGroupId, securityUser.getUserId() );
		
		if( securityUser.isAdmin() == false && ( smallGroupUser == null || smallGroupUser.isAdmin() == false )){
			return new JsonResult(false, "권한이 없습니다.", null);
		}
		
		smallGroupService.remove( sunny, smallGroupId );
		
		
		return new JsonResult(true, "success", null);
	}


	@ResponseBody
	@RequestMapping(value = "/group/{id}/remove", method = RequestMethod.GET, headers = { "Accept=application/json" })
	public JsonResult removeSmallGroup( 
							@ParseSunny(shouldExistsSite=true) Sunny sunny,
							@PathVariable("id") Long smallGroupId,
							@AuthUser SecurityUser securityUser){

		return removeSmallGroup(null, smallGroupId, sunny, securityUser);
	}

	
	@RequestMapping( value = "/{path}/group/{id}", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult groupStream(
			@PathVariable("path") String path,
			@PathVariable("id") Long smallGroupId,
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestParam("contentId") Long contentId,  
			@RequestParam(value="children", required=false) Boolean isWantChildren,
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
		//List<StoryDTO> plazaStories = storyService.fetchLobbyStories( sunny, user, queries, stream);
		List<StoryDTO> stories = storyService.fetchSmallGroupStories(sunny, smallGroupId, user, isWantChildren, queries, stream);
		
		if( stories == null ){
			return new JsonResult(false, messageSource.getMessage("story.noStories", null, LocaleUtils.getLocale()), null);
		}

		return new JsonResult(true, messageSource.getMessage("story.successFetchStories", null, LocaleUtils.getLocale()), stories);
	}
	
	@RequestMapping( value = "/group/{id}", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult groupStream(
			@PathVariable("id") Long groupId,
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestParam("contentId") Long contentId,  
			@RequestParam(value="children", required=false) Boolean isWantChildren,
			@RequestParam(value="q[]", required=false) String[] queries,
			@RequestParam(value="top", required=false) Boolean top, 
			@RequestParam(value="size", required=false) Integer size){
	
		return groupStream( null, groupId, sunny, securityUser, contentId, isWantChildren, queries, top, size);
	}

	
	@ResponseBody
	@RequestMapping( value = "/{path}/group/tree" , method = RequestMethod.GET, headers = { "Accept=application/json" } )
	public JsonResult getSmallGroupTree(
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="type", required=false) Integer type) {

		
		if( type == null )
			type = SmallGroup.TYPE_DEPARTMENT;
		
		List<SmallGroupTree> treeFirst = smallGroupService.getTreeFirst( sunny.getSite(), type);
		
		for( SmallGroupTree smallGroup : treeFirst ){
			smallGroupService.fillChildren(sunny, smallGroup);
		}
		
		return new JsonResult(true, "success", treeFirst);
	}
	
	@ResponseBody
	@RequestMapping( value = "/group/tree" , method = RequestMethod.GET, headers = { "Accept=application/json" } )
	public JsonResult getSmallGroupTree(
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="type", required=false) Integer type) {

		return getSmallGroupTree(null, sunny, securityUser, type);
	}

	
}
