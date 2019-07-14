package kr.co.sunnyvale.sunny.controller.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import kr.co.sunnyvale.sunny.annotation.AuthUser;
import kr.co.sunnyvale.sunny.annotation.NoPathRedirect;
import kr.co.sunnyvale.sunny.annotation.ParseSunny;
import kr.co.sunnyvale.sunny.domain.AuthToken;
import kr.co.sunnyvale.sunny.domain.Friend;
import kr.co.sunnyvale.sunny.domain.Site;
import kr.co.sunnyvale.sunny.domain.SiteInactiveUser;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.dto.StoryDTO;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.domain.post.PasswordPost;
import kr.co.sunnyvale.sunny.exception.NotCorrectPasswordException;
import kr.co.sunnyvale.sunny.exception.SimpleSunnyException;
import kr.co.sunnyvale.sunny.exception.SunnyException;
import kr.co.sunnyvale.sunny.security.SecurityUserService;
import kr.co.sunnyvale.sunny.service.AuthTokenService;
import kr.co.sunnyvale.sunny.service.FriendService;
import kr.co.sunnyvale.sunny.service.MailService;
import kr.co.sunnyvale.sunny.service.SiteService;
import kr.co.sunnyvale.sunny.service.StoryService;
import kr.co.sunnyvale.sunny.service.UserService;
import kr.co.sunnyvale.sunny.service.impl.SiteInactiveUserService;
import kr.co.sunnyvale.sunny.util.LoginUtils;
import kr.co.sunnyvale.sunny.util.SearchUtils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private FriendService friendService;

	@Autowired
	private SecurityUserService securityUserService;
	
	@Autowired
	private SiteService siteService;
	
	@Autowired
	private StoryService storyService;
	
	@Autowired
	private FriendService friendRequestService;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private AuthTokenService authTokenService; 
	
	@Autowired
	private SiteInactiveUserService siteInactiveUserService; 
	
	
	private Logger logger = Logger.getLogger(UserController.class);
	/*
	 * ********************************************************
	 * MultiSite
	 * *********************************************************
	 */
	
	@RequestMapping( value = "/{path}/user/login" )
	public ModelAndView userLogin(
		@PathVariable("path") String path,
		@AuthUser SecurityUser securityUser ) {
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName( ( securityUser != null ) ? "redirect:/" : "/user/login");
		
		return modelAndView;
	}
	@RequestMapping( value = "/user/login" )
	public ModelAndView userLogin( @AuthUser SecurityUser securityUser ) {

		return userLogin( null, securityUser );
	}

	@RequestMapping( value = "/{path}/user/{userId}" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView basecamp(  
			@PathVariable("path") String path,
			@PathVariable("userId") Long userId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="q[]", required=false) String[] queries,
			@RequestParam(value="nq", required=false) String newQuery,
			@RequestParam(value="recursive", required=false) Boolean isRecursive,
			@RequestParam(value="last", required=false) String lastDateString ) throws ParseException {
		
		
		ModelAndView modelAndView = new ModelAndView();
		if( sunny.isGoRedirect() == true ){
			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
			return modelAndView;
		}
		User user = userService.findById( securityUser.getUserId() );

		Stream stream = new Stream(null, null, null, 10);
		if( lastDateString != null ){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date lastDate = format.parse(lastDateString);
			stream.setBaseColumn("createDate");
			stream.setGreaterThan(true);
			stream.setBaseData(lastDate);
			
		}
		
		if( queries != null ){
			queries = SearchUtils.checkAndFixQuery( queries, newQuery, isRecursive );
		}

		User basecampUser = userService.findById( userId );

		if( basecampUser == null ){
			throw new SimpleSunnyException();
		}
		
		
		if( !user.getSite().getId().equals( basecampUser.getSite().getId()) ){
			throw new SimpleSunnyException();
		}
		
		friendRequestService.calcRelation( basecampUser, user);
		
		List<StoryDTO> stories = storyService.fetchBasecampStories(sunny, basecampUser, user, queries, stream);

		if( basecampUser.getId().equals(user.getId())){
			modelAndView.addObject("myBasecamp", true);
		}
		
		modelAndView.addObject("basecampUser", basecampUser);
		modelAndView.addObject("stories",stories);
		
		modelAndView.setViewName( "/user/basecamp" );
		return modelAndView;
	}
	
	
	

	@NoPathRedirect
	@RequestMapping( value = "/user/{userId}" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView basecamp(  
			@PathVariable("userId") Long userId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="q[]", required=false) String[] queries,
			@RequestParam(value="nq", required=false) String newQuery,
			@RequestParam(value="recursive", required=false) Boolean isRecursive,
			@RequestParam(value="last", required=false) String lastDateString ) throws ParseException {
		
		return basecamp(null, userId, sunny, securityUser, queries, newQuery, isRecursive, lastDateString);
	}
	
	
	@RequestMapping( value = "/{path}/user/{userId}/friends" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView friends(  
			@PathVariable("path") String path,
			@PathVariable("userId") Long userId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="q", required=false) String query,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) Boolean isPagelet ) throws ParseException {
		
		
		ModelAndView modelAndView = new ModelAndView();
		
		if( sunny.isGoRedirect() == true ){
			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
			return modelAndView;
		}
		
		LoginUtils.checkLogin(securityUser);

		
		User basecampUser = userService.findById( userId );
		
		if( basecampUser == null ){
			throw new SimpleSunnyException();
		}
		User user = userService.findById( securityUser.getUserId() );
		
		
		if( !user.getSite().getId().equals( basecampUser.getSite().getId()) ){
			throw new SimpleSunnyException();
		}
		
		friendRequestService.calcRelation( basecampUser, user);
		

		if( basecampUser.getId().equals(user.getId())){
			modelAndView.addObject("myBasecamp", true);
		}
		
		modelAndView.addObject("basecampUser", basecampUser);
		Page<Friend> pagedResult = friendService.getFriends( sunny, securityUser.getUserId(), userId, query, page, Page.DEFAULT_PAGE_SIZE );
		modelAndView.addObject("pagedResult", pagedResult);
		modelAndView.setViewName( "/user/friends" );
		return modelAndView;
	}
	
	
	

	@NoPathRedirect
	@RequestMapping( value = "/user/{userId}/friends" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView friends(  
			@PathVariable("userId") Long userId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="q", required=false) String query,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) Boolean isPagelet ) throws ParseException {
		
		return friends(null, userId, sunny, securityUser, query, page, isPagelet);
	}
	
	
	@RequestMapping( value = "/{path}/user/{userId}/setting" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView setting(  
			@PathVariable("path") String path,
			@PathVariable("userId") Long userId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser ) throws ParseException {
		
		
		ModelAndView modelAndView = new ModelAndView();
		
		
		
		if( sunny.isGoRedirect() == true ){
			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
			return modelAndView;
		}
		
		LoginUtils.checkLogin(securityUser);
		
		if( !userId.equals(securityUser.getUserId())){
			throw new SimpleSunnyException();
		}
		
		User user = userService.findById( securityUser.getUserId() );
		modelAndView.addObject("user", user);
		modelAndView.setViewName( "/user/setting" );
		return modelAndView;
	}
	
	
	

	@NoPathRedirect
	@RequestMapping( value = "/user/{userId}/setting" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView setting(  
			@PathVariable("userId") Long userId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser ) throws ParseException {
		
		return setting(null, userId, sunny, securityUser);
	}
	
	
	@RequestMapping( value = "/user/alter_setting_password", method = RequestMethod.POST, headers = { "Accept=text/html" })
	public ModelAndView alterPassword( 
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@ModelAttribute @Valid PasswordPost passwordPost, 
			BindingResult results ){
		
		ModelAndView modelAndView = new ModelAndView();

		
		
		if ( results.hasErrors() ) {
			User user = userService.findById( securityUser.getUserId() );
			modelAndView.addObject("user", user);
			modelAndView.addAllObjects(results.getModel());
	        modelAndView.setViewName("/user/setting");
			return modelAndView;
	    }
		
		if( !passwordPost.getNewPassword().equals(passwordPost.getConfirmPassword()) ){

			User user = userService.findById( securityUser.getUserId() );
			modelAndView.addObject("user", user);
			results.rejectValue("newPassword", "{user.password.NotMatch}", "새로 입력하신 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
			results.rejectValue("confirmPassword", "user.password.NotMatch", "새로 입력하신 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
			modelAndView.addAllObjects(results.getModel());
			modelAndView.setViewName("/user/setting");
			return modelAndView;
		}
		
		try{
			userService.changePassword(securityUser.getUserId(), passwordPost);	
		}catch(NotCorrectPasswordException ex){
			
			User user = userService.findById( securityUser.getUserId() );
			modelAndView.addObject("user", user);
			results.rejectValue("currentPassword", ex.getMessageSourceString(), ex.getLocalizedMessage());
			modelAndView.addAllObjects(results.getModel());
			modelAndView.setViewName("/user/setting");
			return modelAndView;
		}
		
		
		modelAndView.setViewName("redirect:/" + sunny.getSite().getId() + "/user/" + securityUser.getUserId() + "/setting?scp=true");
		return modelAndView;
	}
	
	
	@RequestMapping( value = "/user/find_password", method = RequestMethod.GET, headers = { "Accept=text/html" })
	public ModelAndView findPassword( @AuthUser SecurityUser securityUser ){
		ModelAndView modelAndView = new ModelAndView();
		
		modelAndView.setViewName( ( securityUser != null ) ? ( "redirect:/" ) : ( "/user/find_password" ) );
		
		return modelAndView;
	}
	
	@RequestMapping( value = "/user/find_password", method = RequestMethod.POST, headers = { "Accept=text/html" })
	public ModelAndView sendPassword(
		@AuthUser SecurityUser securityUser, String email,
		@ParseSunny(shouldExistsSite=true) Sunny sunny ) {
			
		ModelAndView modelAndView = new ModelAndView();

		if( securityUser != null ) {
			modelAndView.setViewName( "redirect:/" );
			return modelAndView;
		}
		
		if( email.trim().equals("guest1@sunny.com") ){
			modelAndView.setViewName( "redirect:/" );
			return modelAndView;
		}
		
		User user = userService.findByEmail( email ) ;
		if( user == null ){
			modelAndView.addObject("email", email);
			modelAndView.addObject("noExists", true);
			modelAndView.setViewName( "/user/find_password" );
			return modelAndView;
		}

		mailService.sendResetPasswordMail( sunny, user, email );
		modelAndView.setViewName( "redirect:/user/sent_password" );
		return modelAndView;
		
	}

	@RequestMapping( value = "/user/sent_password", method = RequestMethod.GET, headers = { "Accept=text/html" })
	public ModelAndView sentPassword(
		@AuthUser SecurityUser securityUser,
		@RequestHeader( value = "referer", required = false ) final String referer ) {
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName( (  securityUser != null || referer == null  )  ?  ( "redirect:/" ) : ( "/user/sent_password" ) );
		return modelAndView;
	}
	
	@RequestMapping( value = "/user/reset_password" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView resetPassword( Long id, String key ) {
		ModelAndView modelAndView = new ModelAndView();
		AuthToken correctKey =  authTokenService.getCorrectAuthToken( id, key, AuthToken.TYPE_PASSWORD );
		if( correctKey != null ) {
			modelAndView.addObject( "user", correctKey.getUser() );
			modelAndView.addObject( "key", correctKey );
			modelAndView.addObject( "isValidKey", true );
			modelAndView.setViewName( "/user/reset_password" );
		} else {
			modelAndView.setViewName( "/user/error_reset_password" );
		}
		return modelAndView;
	}
	@RequestMapping( value = "/user/alter_password" , method = RequestMethod.POST, headers = { "Accept=text/html" } )
	public ModelAndView alterPassword( Long user_id, String key_value, String password ) {
		ModelAndView modelAndView = new ModelAndView();
		AuthToken correctKey = authTokenService.getCorrectAuthToken( user_id, key_value, AuthToken.TYPE_PASSWORD );
		if( correctKey == null ) {
			modelAndView.addObject( "isValidKey", false );
			modelAndView.setViewName( "/user/reset_password" );
			return modelAndView;
		}	
		User user = correctKey.getUser();
		PasswordPost passwordPost = new PasswordPost();
		passwordPost.setNewPassword(password);
		userService.changePassword( user.getId(), passwordPost );
		authTokenService.removeValue( correctKey );
		modelAndView.setViewName( "redirect:/user/finish_reset_password" );
		return modelAndView;
	}

	@RequestMapping( value = "/user/finish_reset_password" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView finishPassword( @RequestHeader( value = "referer", required = false ) final String referer ) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName( "/user/finish_reset_password" );
		return modelAndView;
	}
	
	
	
	@RequestMapping( value = "/user/reactivate" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView reActivate( @AuthUser SecurityUser securityUser) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName( "/user/reactivate" );
		return modelAndView;
	}
	
	@RequestMapping( value = "/user/finish_activate" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView finishActivate( @AuthUser SecurityUser securityUser) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName( "/user/finish_activate" );
		return modelAndView;
	}
	@RequestMapping( value = "/user/error_activate" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView errorActivate( @AuthUser SecurityUser securityUser) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName( "/error/confirm_fail" );
		return modelAndView;
	}
	@RequestMapping( value = "/user/activate" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView activate( @AuthUser SecurityUser securityUser, Long id, String key ) {
//		ModelAndView modelAndView = new ModelAndView();
//		
//		try{
//			userService.activate(key, id);
//		}catch(SunnyException ex){
//			ex.printStackTrace();
//			modelAndView.setViewName( "redirect:/user/error_activate" );
//			return modelAndView;
//		}
//		
//		modelAndView.setViewName( "redirect:/user/finish_activate" );
//		return modelAndView;
		

		ModelAndView modelAndView = new ModelAndView();
		
		
		SiteInactiveUser siteInactiveUser = siteInactiveUserService.findById(id);
		try{
			AuthToken authToken = authTokenService.getCorrectAuthToken(siteInactiveUser, key, AuthToken.TYPE_SITE_INACTIVE_USER_CONFIRM);
			if( authToken == null ){
				throw new SimpleSunnyException();
			}
		}catch(SunnyException ex){
			ex.printStackTrace();
			modelAndView.setViewName( "redirect:/user/error_activate" );
			return modelAndView;
		}
		
		
		//siteService.accept(sunny, id);
		modelAndView.addObject(siteInactiveUser);
		modelAndView.setViewName("/site/inactive_user_extra_form");
		return modelAndView;	
		
	}
	
//	@RequestMapping( value = "/a/reactivate", method = RequestMethod.GET, headers = { "Accept=text/html" } )
//	public ModelAndView reactivate(@RequestHeader( value = "referer", required = false ) final String referer, @AuthUser SecurityUser securityUser ) throws IOException, MessagingException {
//		
//		
//	}
	
	@RequestMapping( value = "/user/sent_activate", method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView sentActivate( @AuthUser SecurityUser securityUser ) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName( "/user/sent_activate" );
		return modelAndView;
	}	

	
	
	@RequestMapping( value = "/user/reactivate" , method = RequestMethod.POST, headers = { "Accept=text/html" } )
	public ModelAndView alterPassword(
		@RequestParam(value="username", required=true) String email,
		@ParseSunny(shouldExistsSite=true) Sunny sunny ) {
		
		ModelAndView modelAndView = new ModelAndView();
	
		SiteInactiveUser siteInactiveUser = siteInactiveUserService.findByEmail( null, email );
		mailService.sendConfirmSiteInactiveUserMail( sunny, siteInactiveUser );
		
		modelAndView.setViewName( "redirect:/user/sent_activate" );
		return modelAndView;
	}
	
	
	
}
