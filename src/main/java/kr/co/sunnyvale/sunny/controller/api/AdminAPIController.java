package kr.co.sunnyvale.sunny.controller.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import kr.co.sunnyvale.sunny.annotation.AuthUser;
import kr.co.sunnyvale.sunny.annotation.ParseSunny;
import kr.co.sunnyvale.sunny.domain.AuthToken;
import kr.co.sunnyvale.sunny.domain.SiteInactive;
import kr.co.sunnyvale.sunny.domain.SiteInactiveUser;
import kr.co.sunnyvale.sunny.domain.SmallGroup;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.dto.JsonResult;
import kr.co.sunnyvale.sunny.domain.dto.UserSetPermissionDTO;
import kr.co.sunnyvale.sunny.domain.dto.UserSmallGroupDTO;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.domain.extend.SmallGroupIdName;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.domain.post.AdminJoinDTO;
import kr.co.sunnyvale.sunny.domain.post.DepartmentRegistPostDTO;
import kr.co.sunnyvale.sunny.domain.post.MovePutDTO;
import kr.co.sunnyvale.sunny.domain.post.SiteUpdateDTO;
import kr.co.sunnyvale.sunny.domain.post.TreePutDTO;
import kr.co.sunnyvale.sunny.domain.post.UpdateSmallGroupDTO;
import kr.co.sunnyvale.sunny.domain.post.UpdateUserFromAdminDTO;
import kr.co.sunnyvale.sunny.domain.post.UserInvitePostDTO;
import kr.co.sunnyvale.sunny.exception.AlreadyJoinedException;
import kr.co.sunnyvale.sunny.exception.LoggedInUserException;
import kr.co.sunnyvale.sunny.exception.NotValidSignupFormException;
import kr.co.sunnyvale.sunny.exception.SimpleSunnyException;
import kr.co.sunnyvale.sunny.repository.hibernate.SmallGroupSmallGroupAccessRepository;
import kr.co.sunnyvale.sunny.service.AdminService;
import kr.co.sunnyvale.sunny.service.MailService;
import kr.co.sunnyvale.sunny.service.SiteService;
import kr.co.sunnyvale.sunny.service.SmallGroupService;
import kr.co.sunnyvale.sunny.service.UserService;
import kr.co.sunnyvale.sunny.service.impl.SiteInactiveUserService;
import kr.co.sunnyvale.sunny.service.impl.SiteMenuService;
import kr.co.sunnyvale.sunny.util.LocaleUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AdminAPIController {
	

	@Autowired
	private UserService userService;

	@Autowired
	private SiteService siteService;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private SmallGroupService smallGroupService;
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private SiteMenuService siteMenuService; 
	
	@Autowired
	private SmallGroupSmallGroupAccessRepository smallGroupSmallGroupAccessRepository;
	
	@Autowired
	private SiteInactiveUserService siteInactiveUserService; 
	
	@Autowired
	private MailService mailService;
	
	@ResponseBody
	@RequestMapping( value = "/a/signup", method = RequestMethod.POST, headers = { "Accept=application/json" } )
	public JsonResult signup( @AuthUser SecurityUser securityUser, @RequestBody AdminJoinDTO adminJoinDTO ) {
		// 회원가입시 호출된다.
		// User정보와 Site정보가 올바른지 살펴본후 올바르지 않으면 삭제한다.
		
		
		if( securityUser != null ) {
			throw new LoggedInUserException();
		}
		
		// 이메일 중복 
		if( userService.existsEmail( adminJoinDTO.getUserEmail() ) == true ) {
			throw new SimpleSunnyException( "user.EmailExists" );
		}
		SiteInactiveUser siteInactiveUser = adminJoinDTO.parseToSiteInactiveUser();
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		
		Set<ConstraintViolation<SiteInactiveUser>> validatorResults = validator.validate(siteInactiveUser);

		if (validatorResults.isEmpty() == false) {
			throw new NotValidSignupFormException();
		}
		
		SiteInactive siteInactive = adminJoinDTO.parseToSiteInactive();
		Set<ConstraintViolation<SiteInactive>> siteValidatorResults = validator.validate(siteInactive);

		if (siteValidatorResults.isEmpty() == false) {
			throw new NotValidSignupFormException();
		}
		
		AuthToken authToken = userService.registAdmin( adminJoinDTO );
		return new JsonResult(true, messageSource.getMessage( "user.successJoined", null, LocaleUtils.getLocale()), authToken );
	}

	
	@ResponseBody
	@RequestMapping(value = "/a/user/{id}", method = RequestMethod.DELETE, headers = { "Accept=application/json" })
	public JsonResult removeUser( 
							@PathVariable("id") Long userId,
							@ParseSunny(shouldExistsSite=false) Sunny sunny,
							@AuthUser SecurityUser securityUser ){

		
		
		userService.deleteUser(userId, true);
		return new JsonResult(true, messageSource.getMessage("user.successJoined", null, LocaleUtils.getLocale()), null);
	}

	
	
	@ResponseBody
	@RequestMapping(value = "/{path}/a/user/invite", method = RequestMethod.POST, headers = { "Accept=application/json" })
	public JsonResult inviteUser( 
		@PathVariable("path") String path,
		@ParseSunny(shouldExistsSite=false) Sunny sunny,
		@AuthUser SecurityUser securityUser,
		@RequestBody UserInvitePostDTO userInviteDto ){
		
		if( userService.findByEmail( userInviteDto.getEmail()) != null){
			return new JsonResult(false, "이미 사이트에 가입되어있는 사용자입니다.", null);
		}
		
		if( siteInactiveUserService.findByEmailAndType(sunny.getSite(), userInviteDto.getEmail(), SiteInactiveUser.TYPE_INVITE ) != null ){
			return new JsonResult(false, "이미 초대 혹은 가입요청 상태인 이메일입니다.", null);
		}
		
		userService.inviteUser (sunny, new User( securityUser.getUserId() ), userInviteDto);
		return new JsonResult(true, messageSource.getMessage("user.successJoined", null, LocaleUtils.getLocale()), null);
	}
	
	@ResponseBody
	@RequestMapping(value = "/a/user/invite", method = RequestMethod.POST, headers = { "Accept=application/json" })
	public JsonResult inviteUser( 
							@ParseSunny(shouldExistsSite=false) Sunny sunny,
							@AuthUser SecurityUser securityUser,
							@RequestBody UserInvitePostDTO userInviteDto ){

		return inviteUser(null, sunny, securityUser, userInviteDto);
	}
	
//	
//	@ResponseBody
//	@RequestMapping(value = "/{path}/a/user", method = RequestMethod.POST, headers = { "Accept=application/json" })
//	public JsonResult registUser( 
//							@PathVariable("path") String path,
//							@ParseSunny(shouldExistsSite=false) Sunny sunny,
//							@AuthUser SecurityUser securityUser,
//							@RequestBody UserRegistPostDTO userRegistDto ){
//
//		
//		User user = userRegistDto.parseToUser();
//		Validator validator = Validation.buildDefaultValidatorFactory()
//				.getValidator();
//		
//		Set<ConstraintViolation<User>> validatorResults = validator
//				.validate(user);
//
//		if (validatorResults.isEmpty() == false) {
//			System.out.println(validatorResults.toString());
//			throw new NotValidSignupFormException();
//		}
//		userService.registUser(sunny.getSite(), userRegistDto);
//		return new JsonResult(true, messageSource.getMessage("user.successJoined", null, LocaleUtils.getLocale()), null);
//	}
//	
//	@ResponseBody
//	@RequestMapping(value = "/a/user", method = RequestMethod.POST, headers = { "Accept=application/json" })
//	public JsonResult registUser( 
//							@ParseSunny(shouldExistsSite=false) Sunny sunny,
//							@AuthUser SecurityUser securityUser,
//							@RequestBody UserRegistPostDTO userRegistDto ){
//
//		return registUser(null, sunny, securityUser, userRegistDto);
//	}
	
	@ResponseBody
	@RequestMapping(value = "/{path}/a/user/{id}/info", method = RequestMethod.POST, headers = { "Accept=application/json" })
	public JsonResult updateUser( 
							@PathVariable("path") String path,
							@PathVariable("id") Long userId,
							@ParseSunny(shouldExistsSite=false) Sunny sunny,
							@AuthUser SecurityUser securityUser,
							@RequestBody UpdateUserFromAdminDTO updateSiteUserFromAdminDto ){
	
			userService.update(userId, updateSiteUserFromAdminDto);
		
		return new JsonResult(true, messageSource.getMessage("user.successJoined", null, LocaleUtils.getLocale()), null);
	}
	
	@ResponseBody
	@RequestMapping(value = "/a/user/{id}/info", method = RequestMethod.POST, headers = { "Accept=application/json" })
	public JsonResult updateUser( 
							@PathVariable("id") Long id, 
							@ParseSunny(shouldExistsSite=false) Sunny sunny,
							@AuthUser SecurityUser securityUser,
							@RequestBody UpdateUserFromAdminDTO updateSiteUserFromAdminDto ){

		return updateUser(null, id, sunny, securityUser, updateSiteUserFromAdminDto);
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/{path}/a/department/{id}/info", method = RequestMethod.POST, headers = { "Accept=application/json" })
	public JsonResult updateDepartment( 
							@PathVariable("path") String path,
							@PathVariable("id") Long smallGroupId,
							@ParseSunny(shouldExistsSite=false) Sunny sunny,
							@AuthUser SecurityUser securityUser,
							@RequestBody UpdateSmallGroupDTO updateSmallGroupDto ){
	
		smallGroupService.update(smallGroupId, updateSmallGroupDto);
		
		return new JsonResult(true, messageSource.getMessage("user.successJoined", null, LocaleUtils.getLocale()), null);
	}
	
	@ResponseBody
	@RequestMapping(value = "/a/department/{id}/info", method = RequestMethod.POST, headers = { "Accept=application/json" })
	public JsonResult updateDepartment( 
							@PathVariable("id") Long id, 
							@ParseSunny(shouldExistsSite=false) Sunny sunny,
							@AuthUser SecurityUser securityUser,
							@RequestBody UpdateSmallGroupDTO updateSmallGroupDto ){

		return updateDepartment(null, id, sunny, securityUser, updateSmallGroupDto);
	}
	
	@ResponseBody
	@RequestMapping(value = "/{path}/a/department", method = RequestMethod.POST, headers = { "Accept=application/json" })
	public JsonResult registDepartment( 
							@PathVariable("path") String path,
							@ParseSunny(shouldExistsSite=false) Sunny sunny,
							@AuthUser SecurityUser securityUser,
							@RequestBody DepartmentRegistPostDTO departmentRegistDto ){
		
		SmallGroup parentSmallGroup = null;
		if(departmentRegistDto.getParentDepartmentRadio() == 2){ // 사용자가 상위부서를 지정하였을 경우
			parentSmallGroup = smallGroupService.getSmallGroup(departmentRegistDto.getDepartmentParentId());
		}
		
		SmallGroup prevSmallGroup = null;
		if( departmentRegistDto.getDepartmentPrevId() != null ){
			prevSmallGroup = smallGroupService.getSmallGroup(departmentRegistDto.getDepartmentPrevId());
			
			if( parentSmallGroup == null )
				parentSmallGroup = prevSmallGroup.getParent();
		}
		
		SmallGroup smallGroup = new SmallGroup();
		smallGroup.setTitle(departmentRegistDto.getDepartmentTitle());
		smallGroup.setDescription(departmentRegistDto.getDepartmentDescription());
		smallGroup.setType(SmallGroup.TYPE_DEPARTMENT);
//		smallGroup.setPrivacy(SmallGroup.PRIVACY_SECRET);
		
		smallGroupService.saveSmallGroupAndPermission(sunny.getSite(), parentSmallGroup, prevSmallGroup, smallGroup, SmallGroup.TYPE_DEPARTMENT);
		
		return new JsonResult(true, messageSource.getMessage("department.successRegist", null, LocaleUtils.getLocale()), null);
	}
	
	@ResponseBody
	@RequestMapping(value = "/a/menu", method = RequestMethod.PUT, headers = { "Accept=application/json" })
	public JsonResult moveMenu( 
							@AuthUser SecurityUser securityUser,
							@RequestBody MovePutDTO movePutDTO ){

		siteMenuService.move(movePutDTO);
		
		return new JsonResult(true, "success", null);

	}
	
	
	@ResponseBody
	@RequestMapping(value = "/a/department/tree", method = RequestMethod.PUT, headers = { "Accept=application/json" })
	public JsonResult moveMenu( 
							@AuthUser SecurityUser securityUser,
							@RequestBody TreePutDTO treePutDTO ){

		smallGroupService.move(treePutDTO);
		
		return new JsonResult(true, "success", null);

	}
	

	@ResponseBody
	@RequestMapping(value = "/a/menu/visible", method = RequestMethod.GET, headers = { "Accept=application/json" })
	public JsonResult changeVisible( 
							@AuthUser SecurityUser securityUser,
							@RequestParam(value="smid", required=true) Long siteMenuId,
							@RequestParam(value="visible", required=true) Boolean isVisible){
		
		siteMenuService.changeVisible( siteMenuId, isVisible );

		return new JsonResult(true, "success", null);
	}
	
	
	
	@ResponseBody
	@RequestMapping(value = "/a/department", method = RequestMethod.POST, headers = { "Accept=application/json" })
	public JsonResult registDepartment( 
							@ParseSunny(shouldExistsSite=false) Sunny sunny,
							@AuthUser SecurityUser securityUser,
							@RequestBody DepartmentRegistPostDTO departmentRegistDto ){

		return registDepartment(null, sunny, securityUser, departmentRegistDto);
	}

	
	@ResponseBody
	@RequestMapping(value = "/{path}/a/site", method = RequestMethod.POST, headers = { "Accept=application/json" })
	public JsonResult updateSite( 
							@PathVariable("path") String path,
							@ParseSunny(shouldExistsSite=false) Sunny sunny,
							@AuthUser SecurityUser securityUser,
							@RequestBody SiteUpdateDTO siteUpdateDto ){
		
		siteService.update(sunny.getSite().getId(), siteUpdateDto);

		return new JsonResult(true, "success", null);
	}
	
	@ResponseBody
	@RequestMapping(value = "/a/site", method = RequestMethod.POST, headers = { "Accept=application/json" })
	public JsonResult updateSite( 
							@ParseSunny(shouldExistsSite=false) Sunny sunny,
							@AuthUser SecurityUser securityUser,
							@RequestBody SiteUpdateDTO siteUpdateDto ){

		return updateSite(null, sunny, securityUser, siteUpdateDto );
	}

	
	
	// /${site.id}/a/user/permission
	@ResponseBody
	@RequestMapping( value = "/{path}/a/user/permission" , method = RequestMethod.POST, headers = { "Accept=application/json" } )
	public JsonResult setUserPermission(
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestBody UserSetPermissionDTO userSetPermissionDTO) {


		smallGroupService.setPermission( userSetPermissionDTO.getUserId(), userSetPermissionDTO.getSmallGroupId(), userSetPermissionDTO.getPermissionType(), true);
		
		return new JsonResult(true, messageSource.getMessage("department.setUserPermission", null, LocaleUtils.getLocale()), null);
	}	
	
	@ResponseBody
	@RequestMapping( value = "/{path}/a/users/smallgroup/{smallGroupId}" , method = RequestMethod.GET, headers = { "Accept=application/json" } )
	public JsonResult getUsers(
			@PathVariable("path") String path,
			@PathVariable("smallGroupId") Long smallGroupId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="userName", required=false) String userName,
			@RequestParam(value="page", required=false) Integer page) {
		
		SmallGroup smallGroup = smallGroupService.getSmallGroup(smallGroupId);
		Map<String, Object> map = new HashMap<String, Object>();
		//Page<User> userPage = userService.getUserList(smallGroupId, true, userName,  page, Page.DEFAULT_PAGE_SIZE);
		Page<User> userPage = userService.getUserList(sunny, null, 0, userName, null, null, null, null, null, page, Page.DEFAULT_PAGE_SIZE);
		List<User> list = userPage.getContents();
		for (User user : list) {
			List<SmallGroup> list2 = user.getSmallGroups();
			for (SmallGroup smallGroup2 : list2) {
				//System.out.println("sg2 : " + smallGroup2.getName());
			}
		}
		map.put("userPage", userPage);
		map.put("smallGroup", smallGroup);
		map.put("site", sunny.getSite());
		
		return new JsonResult(true, messageSource.getMessage("department.getUsers", null, LocaleUtils.getLocale()), map);
	}
		

	@ResponseBody
	@RequestMapping( value = "/{path}/a/get_group_children_idname" , method = RequestMethod.GET, headers = { "Accept=application/json" } )
	public JsonResult getChildrenGroups(
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="type", required=false) Integer type,
			@RequestParam(value="parentId", required=false) Long parentId) {

		
		List<SmallGroupIdName> smallGroupIdNames = smallGroupService.getChildrenSmallGroupIdNames(sunny.getSite(), parentId, type);
		
		return new JsonResult(true, messageSource.getMessage("department.getUsers", null, LocaleUtils.getLocale()), smallGroupIdNames);
	}

	
	/**
	 * id에 해당하는 회원에 smallGroupId에 해당하는 부서를 설정합니다.
	 * smallGroupId에 해당하는 부서에 id에 해당하는 회원을 추가합니다.
	 * @param path
	 * @param id
	 * @param smallGroupId
	 * @param sunny
	 * @param securityUser
	 * @return
	 */
	@ResponseBody
	@RequestMapping( value = "/a/user/setsmallgroup" , method = RequestMethod.POST, headers = { "Accept=application/json" } )
	public JsonResult setSmallGroupFromUser(
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestBody UserSmallGroupDTO userSmallGroupDTO ) {

		
		if( userSmallGroupDTO.isJoin() == true ){
			// repository 단에서 smallGroup.id >= id 가 이해가 가지 않아서 우선 빼놓음.
			//userService.setSmallGroup(sunny.getSite().getId(), userSmallGroupDTO.getUserId(), userSmallGroupDTO.getSmallGroupId());
			try{
				smallGroupService.addUserToSmallGroup(userSmallGroupDTO.getUserId(), userSmallGroupDTO.getSmallGroupId(), false);
			}catch(AlreadyJoinedException ex){
				return new JsonResult(false, "이미 해당 그룹에 가입되어있습니다.", null);
			}
		}else{
			smallGroupService.removeUserFromSmallGroup(userSmallGroupDTO.getUserId(), userSmallGroupDTO.getSmallGroupId());
			//userService.delSmallGroup(sunny.getSite().getId(), userSmallGroupDTO.getUserId(), userSmallGroupDTO.getSmallGroupId(), true);
		}
		
		return new JsonResult(true, messageSource.getMessage("department.addUser", null, LocaleUtils.getLocale()), null);
	}
	


	@ResponseBody
	@RequestMapping( value = "/a/site/resend_invite" , method = RequestMethod.GET, headers = { "Accept=application/json" } )
	public JsonResult siteResendInviteEmail(  
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="id", required=false) Long siteInactiveUserId) {
		
		
		SiteInactiveUser siteInactiveUser = siteInactiveUserService.findById(siteInactiveUserId);
		
		mailService.sendInviteSiteInactiveUserMail( sunny, siteInactiveUser);
		return new JsonResult( true, "초대메일이 재전송되었습니다", null );
	}
	
	@ResponseBody
	@RequestMapping( value = "/{path}/a/department/delete" , method = RequestMethod.GET, headers = { "Accept=application/json" } )
	public JsonResult removeSmallGroup(
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="id", required=true) Long id) {

		
		smallGroupService.deleteSmallGroup(sunny.getSite(), id, true);
		return new JsonResult(true, "success" , id);
	}

	@ResponseBody
	@RequestMapping( value = "/a/department/delete" , method = RequestMethod.GET, headers = { "Accept=application/json" } )
	public JsonResult removeSmallGroup(
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="id", required=true) Long id) {

		return removeSmallGroup(null, sunny, securityUser, id);
	}
	

	@ResponseBody
	@RequestMapping( value = "/{path}/a/site/deny" , method = RequestMethod.GET, headers = {  "Accept=application/json"  } )
	public JsonResult siteDenyInactiveUser(  
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="id", required=false) Long siteInactiveUserId) {
		
		siteInactiveUserService.delete( siteInactiveUserId );
		
		return new JsonResult(true, "success" , siteInactiveUserId);
	}

	@ResponseBody
	@RequestMapping( value = "/a/site/deny" , method = RequestMethod.GET, headers = {  "Accept=application/json"  } )
	public JsonResult siteDenyInactiveUser(
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="id", required=false) Long siteInactiveUserId) {
		return siteDenyInactiveUser(null, sunny, securityUser, siteInactiveUserId);
	}
	
	@ResponseBody
	@RequestMapping( value = "/a/site/accept_after_confirm" , method = RequestMethod.GET, headers = { "Accept=application/json" } )
	public JsonResult siteAcceptAfterConfirm(  
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="id", required=false) Long siteInactiveUserId) {
		
		
		siteService.acceptInactiveUserAfterConfirm( sunny, siteInactiveUserId );
		return new JsonResult(true, "success" , siteInactiveUserId);
	}

}
