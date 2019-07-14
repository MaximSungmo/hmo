package kr.co.sunnyvale.sunny.controller.api;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import kr.co.sunnyvale.sunny.annotation.AuthUser;
import kr.co.sunnyvale.sunny.annotation.ParseSunny;
import kr.co.sunnyvale.sunny.domain.Friend;
import kr.co.sunnyvale.sunny.domain.Site;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.UserAppleToken;
import kr.co.sunnyvale.sunny.domain.dto.JsonResult;
import kr.co.sunnyvale.sunny.domain.dto.UserDTO;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.domain.post.RelationDTO;
import kr.co.sunnyvale.sunny.domain.post.RequestFriendDTO;
import kr.co.sunnyvale.sunny.domain.post.UpdateProfileUser;
import kr.co.sunnyvale.sunny.exception.NotFoundMessageSourceException;
import kr.co.sunnyvale.sunny.service.FriendService;
import kr.co.sunnyvale.sunny.service.SiteService;
import kr.co.sunnyvale.sunny.service.UserService;
import kr.co.sunnyvale.sunny.service.impl.DeviceRegisterService;
import kr.co.sunnyvale.sunny.util.LocaleUtils;
import kr.co.sunnyvale.sunny.util.LoginUtils;

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

@Controller
public class UserAPIController {

	@Autowired
	private UserService userService;

	@Autowired
	private SiteService siteService;
	
	@Autowired
	private FriendService friendService;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private DeviceRegisterService deviceRegisterService;
	
	@RequestMapping( value = "/user/stream", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult userStreamOrder(
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestParam(value="q", required=false) String queryName,
			@RequestParam(value="top", required=false) Boolean top,
			@RequestParam(value="uid", required=false) Long userId, 
			@RequestParam(value="size", required=false) Integer size){
		
//		Stream stream;
		
		LoginUtils.checkLogin(securityUser);
		
	
		if(top == null)
			top = true;
		
		if( size == null ){
			size = 10;
		}
//		if( userId == null ){
//			stream = new Stream(size);
//		}else{
//			stream = new Stream(top == true ? false : true, "id", userId, size);	
//		}
		
		List<User> users = userService.getSimpleUserList(sunny.getSite().getId(), securityUser.getUserId(), userId, queryName, top, size);
		
		return new JsonResult(true,  messageSource.getMessage("follow.successGetFriends", null, LocaleUtils.getLocale()), users);
		
	}
	
	
	
	@RequestMapping( value = "/{path}/user/stream/order", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult userStreamOrder(
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestParam(value="status[]", required=false) Integer[] status,
			@RequestParam(value="buid", required=false) Long basecampUserId, 
			@RequestParam(value="q", required=false) String queryName,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="size", required=false) Integer pageSize){
		
		/*
		 * 4가지 경우의 수 중에 2가지만 검사한다.
		 * 1. 가져올 사람 아이디도 지정하지 않고 로그인도 안한 경우 
		 *  -> 에러
		 *  2. 가져올 사람 아이디는 지정하지 않았는데 로그인은 된 경우
		 *   -> 본인꺼 가져오는것이므로 자기 아이디를 가져올 사람 아이디에 넣으면 됨
		 * 나머지
		 * 3. 가져올 사람 아이디 지정되고 로그인 안한 경우
		 *  -> 친구는 갖고 오되 보는 사람과의 관계는 체크하지 않는다.
		 * 4. 가져올 사람 아이디 지정되고 로그인 한 경우
		 *  -> 친구도 갖고 오고 보는 사람과의 관계도 연산해서 리턴한다.  
		 */
		LoginUtils.checkLogin(securityUser);
		
		if( basecampUserId == null ){
			basecampUserId = securityUser.getUserId();
		}
//		if(top == null)
//			top = true;
		
//		if( name == null || name.equals("")){
//			stream = new Stream(null, "name", null, 10);
//		}else
//			stream = new Stream(top == true ? false : true, "name", name, size);
		
		
		Page<User> relations = userService.getUserList(sunny.getSite().getId(), basecampUserId, queryName, status, page, pageSize == null ? 10 : pageSize);
		
		if( relations == null ){
			return new JsonResult(false,  messageSource.getMessage("follow.noFriend", null, LocaleUtils.getLocale()), null);
		}

		return new JsonResult(true,  messageSource.getMessage("follow.successGetFriends", null, LocaleUtils.getLocale()), relations);
	}
	
	@RequestMapping( value = "/user/stream/order", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult userStreamOrder(
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestParam(value="status[]", required=false) Integer[] status,
			@RequestParam(value="buid", required=false) Long basecampUserId, 
			@RequestParam(value="q", required=false) String queryName,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="size", required=false) Integer pageSize){
		
		return userStreamOrder( null, sunny, securityUser, status, basecampUserId, queryName, page, pageSize );

	}
	
	@ResponseBody
	@RequestMapping(value = "/user/valid/email", method = RequestMethod.GET, headers = { "Accept=application/json" })
	public JsonResult validEmail(@RequestParam("email") String email) {

		User user = new User();
		user.setEmail(email);

		Validator validator = Validation.buildDefaultValidatorFactory()
				.getValidator();
		Set<ConstraintViolation<User>> validatorResults = validator
				.validateProperty(user, "email");

		if (validatorResults.isEmpty() == false) {
			return userValidMessage(validatorResults);
		}

		if (userService.existsEmail(email) == false) {
			return new JsonResult(true, messageSource.getMessage(
					"user.emailAvailable", null, LocaleUtils.getLocale()), null);
		}
		return new JsonResult(false, messageSource.getMessage(
				"user.emailExists", null, LocaleUtils.getLocale()), null);
	}
	
	
	@RequestMapping(value = "/{path}/user/match", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult matchList(
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			String key) {
		LoginUtils.checkLogin(securityUser);
		Stream stream = null;
		Site site = sunny.getSite();
		
		List<UserDTO> matchUsers = userService.getMatchUsers(site, securityUser.getUserId(), key, stream);

		return new JsonResult(true, messageSource.getMessage(
				"user.getMatchList", null, LocaleUtils.getLocale()), matchUsers);
	}
	
	@RequestMapping(value = "/user/match", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult matchList(
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			String key) {
		return matchList(null, sunny, securityUser, key);
	}
	
//	@ResponseBody
//	@RequestMapping(value = "/{domainName}/user/regist", method = RequestMethod.POST, headers = { "Accept=application/json" })
//	public JsonResult registUser( 
//			@PathVariable("domainName") String domainName,
//			@ParseSunny(shouldExistsSite=false) Sunny sunny,
//			@AuthUser SecurityUser securityUser,
//			@RequestBody User user){
//
//		if( securityUser == null ) {
//			throw new NotLoggedInUserException();
//		}
//		// 이메일 중복 
//		if( userService.existsEmail( user.getEmail() ) == true ) {
//			throw new SimpleSunnyException( "user.EmailExists" );
//		}
//		
//		Validator validator = Validation.buildDefaultValidatorFactory()
//				.getValidator();
//		
//		Set<ConstraintViolation<User>> validatorResults = validator
//				.validate(user);
//
//		if (validatorResults.isEmpty() == false) {
//			throw new NotValidSignupFormException();
//		}
//		Site site = siteService.getSiteFromDomain(domainName);
//		userService.registUser(site, user);
//		return new JsonResult(true, messageSource.getMessage("user.successJoined", null, LocaleUtils.getLocale()), null);
//	}

//	@ResponseBody
//	@RequestMapping(value = "/user/regist", method = RequestMethod.POST, headers = { "Accept=application/json" })
//	public JsonResult registUser( 
//			@AuthUser SecurityUser securityUser,
//			@RequestBody User user){
//		return registUser(RequestUtils.getCurrentServerName(), securityUser, user);
//	}
//	
	
	
	@ResponseBody
	@RequestMapping( value = "/{path}/user/{id}/alter_settings", method = RequestMethod.POST, headers = { "Accept=application/json" } )
	public JsonResult alterSettings(
			@PathVariable("path") String domainName,
			@PathVariable("id") Long userId,
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestBody UpdateProfileUser updateUser) {

		LoginUtils.checkLogin(securityUser);

		userService.updateUser( userId, updateUser, securityUser );
		
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		securityContext.setAuthentication(
				new UsernamePasswordAuthenticationToken(securityUser,
						authentication.getCredentials(), securityUser
								.getAuthorities()));
		
		return new JsonResult(true, messageSource.getMessage(
				"user.successAlterSetting", null, LocaleUtils.getLocale()),
				null);
	}
	
	
	@ResponseBody
	@RequestMapping( value = "/user/{id}/alter_settings", method = RequestMethod.POST, headers = { "Accept=application/json" } )
	public JsonResult alterSettings(
			@PathVariable("id") Long id,
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestBody UpdateProfileUser updateUser) {

		return alterSettings(null, id, sunny, securityUser, updateUser);
	}
	
	@ResponseBody
	@RequestMapping( value = "/{path}/user/request_friend", method = RequestMethod.POST, headers = { "Accept=application/json" } )
	public JsonResult requestFriend(
			@PathVariable("path") String domainName,
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestBody RequestFriendDTO requestFriendDTO) {

		LoginUtils.checkLogin(securityUser);

		Long userId = requestFriendDTO.getUserId();
		int requestType = requestFriendDTO.getRequestType();
		
		if( requestType == RequestFriendDTO.REQUEST_TYPE_FRIEND || requestType == RequestFriendDTO.REQUEST_TYPE_ACCEPT ){
			friendService.requestFriend(sunny, userId, securityUser.getUserId());
		}else if( requestType == RequestFriendDTO.REQUEST_TYPE_DENY ){
			friendService.denyRequestFriend( userId, securityUser.getUserId() );
		}else{
			friendService.cancelRequestFriend(userId, securityUser.getUserId());
		}
		
		return new JsonResult(true, messageSource.getMessage(
				"user.successAlterSetting", null, LocaleUtils.getLocale()),
				null);
	}
	
	
	@ResponseBody
	@RequestMapping( value = "/user/request_friend", method = RequestMethod.POST, headers = { "Accept=application/json" } )
	public JsonResult requestFriend(
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestBody RequestFriendDTO requestFriendDTO ) {

		return requestFriend(null, sunny, securityUser, requestFriendDTO);
	}

	@ResponseBody
	@RequestMapping( value = "/{path}/user/friend_requests", method = RequestMethod.GET, headers = { "Accept=application/json" } )
	public JsonResult getFriendRequests(
			@PathVariable("path") String domainName,
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="uid", required=false) Long userId, 
			@RequestParam(value="top", required=false) Boolean top,
			@RequestParam(value="size", required=false) Integer size) {

		LoginUtils.checkLogin(securityUser);

		User user = userService.findById( securityUser.getUserId() );
		
		Stream stream = null;
		
//		if(top == null) 
//			top = true;
//		
//		if( userId == null ){
//			stream = new Stream(null, null, null, 30);
//		}else{
//			stream = new Stream( top == true ? false : true, "updateDate",  messageInfoId , size);
//		}
		
		List<Friend> friends = friendService.getNonAcceptedFriendRequests(user, stream);
		
		return new JsonResult(true, messageSource.getMessage(
				"user.successAlterSetting", null, LocaleUtils.getLocale()),
				friends);
	}
	
	
	@ResponseBody
	@RequestMapping( value = "/user/friend_requests", method = RequestMethod.GET, headers = { "Accept=application/json" } )
	public JsonResult getFriendRequests(
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="uid", required=false) Long userId, 
			@RequestParam(value="top", required=false) Boolean top,
			@RequestParam(value="size", required=false) Integer size ) {

		return getFriendRequests(null, sunny, securityUser, userId, top, size);
	}
	
	@ResponseBody
	@RequestMapping( value = "/user/{userId}/apple/register", method = RequestMethod.POST, headers = { "Accept=application/json" } )
	public JsonResult pushAppleDevice(
			@PathVariable("userId") Long userId,
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestBody UserAppleToken userAppleToken) {

		LoginUtils.checkLogin(securityUser);

		System.out.println("애플 등록");
		
		deviceRegisterService.registerAppleDevice( userId, userAppleToken );
		
		return new JsonResult(true, "토큰이 등록되었습니다",
				userAppleToken);
	}
	

	@ResponseBody
	@RequestMapping( value = "/user/{userId}/apple/unregister", method = RequestMethod.GET, headers = { "Accept=application/json" } )
	public JsonResult pushAppleDevice(
			@PathVariable("userId") Long userId,
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value = "deviceToken", required=true) String deviceToken ) {

		LoginUtils.checkLogin(securityUser);

		System.out.println("애플 등록");
		
		deviceRegisterService.unregisterAppleDevice( userId, deviceToken );
		
		return new JsonResult(true, "토큰 삭제 완료",	null);
	}
	

	@RequestMapping(value = "/user/ping", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult pingAuth(
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser ) {
		return new JsonResult( true, null, securityUser.getUserId() );
	}	
	
	private <T> JsonResult userValidMessage(Set<ConstraintViolation<T>> validatorResults) {
			for (ConstraintViolation<T> validatorResult : validatorResults) {
				return new JsonResult(false, validatorResult.getMessage(), null);
		}

		throw new NotFoundMessageSourceException();
	}
}
