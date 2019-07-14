package kr.co.sunnyvale.sunny.controller.web;

import kr.co.sunnyvale.sunny.annotation.AuthUser;
import kr.co.sunnyvale.sunny.annotation.NoPathRedirect;
import kr.co.sunnyvale.sunny.annotation.ParseSunny;
import kr.co.sunnyvale.sunny.domain.SiteInactiveUser;
import kr.co.sunnyvale.sunny.domain.SmallGroup;
import kr.co.sunnyvale.sunny.domain.SmallGroupSmallGroupAccess;
import kr.co.sunnyvale.sunny.domain.Template;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.UserSmallGroupAccess;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.exception.AlreadyJoinedException;
import kr.co.sunnyvale.sunny.exception.SunnyException;
import kr.co.sunnyvale.sunny.security.SecurityUserService;
import kr.co.sunnyvale.sunny.service.AdminService;
import kr.co.sunnyvale.sunny.service.AuthTokenService;
import kr.co.sunnyvale.sunny.service.MailService;
import kr.co.sunnyvale.sunny.service.SiteService;
import kr.co.sunnyvale.sunny.service.SmallGroupService;
import kr.co.sunnyvale.sunny.service.SmallGroupSmallGroupAccessService;
import kr.co.sunnyvale.sunny.service.SunnyService;
import kr.co.sunnyvale.sunny.service.TemplateService;
import kr.co.sunnyvale.sunny.service.UserService;
import kr.co.sunnyvale.sunny.service.UserSmallGroupAccessService;
import kr.co.sunnyvale.sunny.service.impl.MenuService;
import kr.co.sunnyvale.sunny.service.impl.SiteInactiveUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AdminController {

	@Autowired
	private UserService userService;

	@Autowired
	private TemplateService templateService;
	
	@Autowired
	private SecurityUserService securityUserService;
	
	@Autowired
	private SunnyService sunnyService; 
	
	@Autowired
	private SiteService siteService;
	
	@Autowired
	private AuthTokenService authTokenService;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private SmallGroupService smallGroupService;
	
	@Autowired
	private SiteInactiveUserService siteInactiveUserService; 
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private UserSmallGroupAccessService userSmallGroupAccessService;
	
	@Autowired
	private SmallGroupSmallGroupAccessService smallGroupSmallGroupAccessService;
	
	@Autowired
	private MenuService menuService;
	
	/*
	 * ********************************************************
	 * MultiSite
	 * *********************************************************
	 */
	
	@RequestMapping( value = "/a/signup", method = RequestMethod.GET , headers = { "Accept=text/html" })
	public ModelAndView getSignup(
		@ParseSunny(shouldExistsSite=false) Sunny sunny,	
		@AuthUser SecurityUser securityUser ) {
		
		ModelAndView modelAndView = new ModelAndView();
		if( sunny.getSite() == null && securityUser == null ) {
			modelAndView.setViewName( "/a/signup" );
		}  
		
		modelAndView.setViewName( "redirect:/" );
		return modelAndView;
	}
	
	@RequestMapping( value = "/a/welcome", method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView welcome( @PathVariable("path") String path,@RequestHeader( value = "referer", required = false ) final String referer, @AuthUser SecurityUser securityUser ) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName( ( securityUser != null || referer == null )  ?  "redirect:/" : "/a/welcome" );
		return modelAndView;
	}
	
	@RequestMapping( value = "/a/reactivate" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView reActivate( @AuthUser SecurityUser securityUser) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName( "/a/reactivate" );
		return modelAndView;
	}
	
	@RequestMapping( value = "/a/already_joined" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView alreadyJoined( @AuthUser SecurityUser securityUser) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName( "/error/already_joined" );
		return modelAndView;
	}
	
	@RequestMapping( value = "/a/finish_activate" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView finishActivate( @AuthUser SecurityUser securityUser) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName( "/a/finish_activate" );
		return modelAndView;
	}
	@RequestMapping( value = "/a/error_activate" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView errorActivate( @AuthUser SecurityUser securityUser) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName( "/error/confirm_fail" );
		return modelAndView;
	}
	@RequestMapping( value = "/a/activate" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView activate( @AuthUser SecurityUser securityUser, Long id, String key ) {
		ModelAndView modelAndView = new ModelAndView();
		
		try{
			userService.confirmAdmin(key, id);
		}catch(SunnyException ex){
			ex.printStackTrace();
			
			if( ex instanceof AlreadyJoinedException ){
				modelAndView.setViewName( "redirect:/a/already_joined" );
				return modelAndView;	
			}
			
			modelAndView.setViewName( "redirect:/a/error_activate" );
			return modelAndView;
		}
		
		modelAndView.setViewName( "redirect:/a/finish_activate" );
		return modelAndView;
	}
	
//	@RequestMapping( value = "/a/reactivate", method = RequestMethod.GET, headers = { "Accept=text/html" } )
//	public ModelAndView reactivate(@RequestHeader( value = "referer", required = false ) final String referer, @AuthUser SecurityUser securityUser ) throws IOException, MessagingException {
//		
//		
//	}
	
	@RequestMapping( value = "/a/sent_activate", method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView sentActivate( @AuthUser SecurityUser securityUser ) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName( "/a/sent_activate" );
		return modelAndView;
	}	

	
	
	@RequestMapping( value = "/a/reactivate" , method = RequestMethod.POST, headers = { "Accept=text/html" } )
	public ModelAndView alterPassword(
		@RequestParam(value="username", required=true) String email,
		@ParseSunny(shouldExistsSite=true) Sunny sunny ) {
		
		ModelAndView modelAndView = new ModelAndView();
	
		SiteInactiveUser siteInactiveUser = siteInactiveUserService.findByEmail( null, email );
		mailService.sendConfirmAdminMail( sunny, siteInactiveUser.getSiteInactive(),  siteInactiveUser );
		
		modelAndView.setViewName( "redirect:/a/sent_activate" );
		return modelAndView;
	}
	
	
	@RequestMapping( value = "/{path}/a" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView getAdmin(  @PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser ) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("redirect:" + ( path != null ? "/" + path : "" ) + "/a/site"); 
		return modelAndView;
//		
//		if( sunny.isGoRedirect() == true ){
//			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
//			return modelAndView;
//		}
//		modelAndView.addObject("sunny", sunny);
//		modelAndView.setViewName( "/a/index" );
		//return modelAndView;
	}
	
	
	@NoPathRedirect
	@RequestMapping( value = "/a" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView getAdmin(
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser){
		return getAdmin(null, sunny, securityUser);
	}

	
	@RequestMapping( value = "/{path}/a/template" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView getTemplates(  
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser
			) {
		
		ModelAndView modelAndView = new ModelAndView();

		modelAndView.addObject("templates", templateService.getTemplates(sunny, null));
		
		modelAndView.setViewName( "/a/template/index" );
		return modelAndView;
	}
	
	@NoPathRedirect
	@RequestMapping( value = "/a/template" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView getTemplates(
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser) {
		
		return getTemplates(null, sunny, securityUser);
	}
	
	
	@RequestMapping( value = "/{path}/a/template/{id}" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView getTemplateView(  
			@PathVariable("path") String path,
			@PathVariable("id") Long templateId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser
			) {
		
		ModelAndView modelAndView = new ModelAndView();

		modelAndView.addObject("template", templateService.find(templateId));
		
		modelAndView.setViewName( "/a/template/view" );
		return modelAndView;
	}
	
	@NoPathRedirect
	@RequestMapping( value = "/a/template/{id}" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView getTemplateView(
			@PathVariable("id") Long templateId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser) {
		
		return getTemplateView(null, templateId, sunny, securityUser);
	}
	
	@RequestMapping( value = "/{path}/a/template/{id}/delete" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView deleteTemplate(  
			@PathVariable("path") String path,
			@PathVariable("id") Long templateId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser
			) {
		
		ModelAndView modelAndView = new ModelAndView();

		templateService.delete( templateId );
		
		modelAndView.setViewName( "redirect:/a/template" );
		return modelAndView;
	}
	
	@NoPathRedirect
	@RequestMapping( value = "/a/template/{id}/delete" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView deleteTemplate(
			@PathVariable("id") Long templateId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser) {
		
		return deleteTemplate(null, templateId, sunny, securityUser);
	}
	
	@RequestMapping( value = "/{path}/a/template/write" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView templateWrite(  
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser
			) {
		
		ModelAndView modelAndView = new ModelAndView();

		modelAndView.setViewName( "/a/template/write" );
		return modelAndView;
	}
	
	@NoPathRedirect
	@RequestMapping( value = "/a/template/write" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView templateWrite(
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser) {
		
		return templateWrite(null, sunny, securityUser);
	}
	
	
	
	@RequestMapping( value = "/{path}/a/template" , method = RequestMethod.POST, headers = { "Accept=text/html" } )
	public ModelAndView postTemplates(  
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@ModelAttribute Template template
			) {

		template.setSite(sunny.getSite());
		template.setUser(new User(securityUser.getUserId()));
		ModelAndView modelAndView = new ModelAndView();

		templateService.save(template);
		
		modelAndView.setViewName( "redirect:/a/template" );
		return modelAndView;
	}
	
	@NoPathRedirect
	@RequestMapping( value = "/a/template" , method = RequestMethod.POST, headers = { "Accept=text/html" } )
	public ModelAndView postTemplates(
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@ModelAttribute Template template) {
		
		return postTemplates(null, sunny, securityUser, template);
	}
	
	
	@RequestMapping( value = "/{path}/a/user" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView getUsers(  
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="q", required=false) String queryName,
			@RequestParam(value="qt", required=false) Integer queryType,
			@RequestParam(value="status[]", required=false) Integer[] status,
			@RequestParam(value="range", required=false) String range,
			@RequestParam(value="ordering", required=false) String ordering,
			@RequestParam(value="onlyAdmin", required=false) Boolean onlyAdmin,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) Boolean isPagelet ) {
		
		ModelAndView modelAndView = new ModelAndView();

		if( sunny.isGoRedirect() == true ){
			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
			return modelAndView;
		}
			
		Page<User> pagedResult = userService.getUserList(sunny, null, queryType, queryName, status, range, ordering, null, onlyAdmin, page, Page.DEFAULT_PAGE_SIZE);
	
		//pagedResult.setUniqueList();
		modelAndView.addObject("pagedResult", pagedResult);

		
		
		if( isPagelet != null && isPagelet ){
			modelAndView.setViewName( "/pagelet/a/user/users" );
			return modelAndView;
		}
		
		if( status != null ){
			for( Integer statusEach : status ){
				switch( statusEach ){
				case User.STATUS_WORKING:
					modelAndView.addObject("STATUS_WORKING", true);
					break;
					
				case User.STATUS_VACATION:
					modelAndView.addObject("STATUS_VACATION", true);
					break;
				case User.STATUS_LEAVE:
					modelAndView.addObject("STATUS_LEAVE", true);
					break;
				}
			}
		}

		modelAndView.setViewName( "/a/user/index" );
		return modelAndView;
	}
	
	@NoPathRedirect
	@RequestMapping( value = "/a/user" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView getUsers(
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="q", required=false) String queryName,
			@RequestParam(value="qt", required=false) Integer queryType,
			@RequestParam(value="status[]", required=false) Integer[] status,
			@RequestParam(value="range", required=false) String range,
			@RequestParam(value="ordering", required=false) String ordering,
			@RequestParam(value="onlyAdmin", required=false) Boolean onlyAdmin,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) Boolean isPagelet) {
		
		
		
		return getUsers(null, sunny, securityUser, queryName, queryType, status, range, ordering, onlyAdmin, page, isPagelet);
	}
	
	@RequestMapping( value = "/{path}/a/user/{id}/info" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView getUserInfo(  
			@PathVariable("path") String path,
			@PathVariable("id") Long userId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser) {
		ModelAndView modelAndView = new ModelAndView();
		if( sunny.isGoRedirect() == true ){
			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
			return modelAndView;
		}
		User user = userService.findById(userId);
		
		modelAndView.addObject("user", user );
		
		modelAndView.addObject("sunny", sunny);
		modelAndView.setViewName( "/a/user/info" );
		return modelAndView;
	}

	@NoPathRedirect
	@RequestMapping( value = "/a/user/{id}/info" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView getUserInfo(
			@PathVariable("id") Long id,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser) {
		return getUserInfo(null, id, sunny, securityUser);
	}
	
	
	@RequestMapping( value = "/{path}/a/user/{id}/department" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView getUserDepartment(  
			@PathVariable("path") String path,
			@PathVariable("id") Long siteUserId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="q", required=false) String queryName,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) boolean isPagelet) {
		
		ModelAndView modelAndView = new ModelAndView();
		if( sunny.isGoRedirect() == true ){
			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
			return modelAndView;
		}
		User user = userService.findById(siteUserId);
		boolean includePath = true;
		modelAndView.addObject("pagedResult", smallGroupService.getSmallGroupPage(sunny.getSite(), null, user, SmallGroup.TYPE_DEPARTMENT, queryName, includePath, page, Page.DEFAULT_PAGE_SIZE));
		modelAndView.addObject("user", user);
		
		modelAndView.addObject("sunny", sunny);
		
		modelAndView.addObject("firstSmallGroups", smallGroupService.getChildrenSmallGroupIdNames(sunny.getSite(), null, SmallGroup.TYPE_DEPARTMENT));
		if( isPagelet == true ){
			modelAndView.setViewName( "/pagelet/a/user/departments" );
		}else{
			modelAndView.setViewName( "/a/user/department" );
		}
		
		return modelAndView;
	}

	@NoPathRedirect
	@RequestMapping( value = "/a/user/{id}/department" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView getUserDepartment(
			@PathVariable("id") Long id,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="q", required=false) String queryName,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) boolean isPagelet) {
		return getUserDepartment(null, id, sunny, securityUser, queryName, page, isPagelet);
	}
	
	
	
//	@RequestMapping( value = "/{path}/a/user/{id}/project" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
//	public ModelAndView getUserProject(  
//			@PathVariable("path") String path,
//			@PathVariable("id") Long id,
//			@ParseSunny(shouldExistsSite=true) Sunny sunny,
//			@AuthUser SecurityUser securityUser,
//			@RequestParam(value="page", required=false) Integer page) {
//		
//		ModelAndView modelAndView = new ModelAndView();
//		if( sunny.isGoRedirect() == true ){
//			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
//			return modelAndView;
//		}
//		User user = userService.findById(id);
//		modelAndView.addObject("pagedResult", smallGroupService.getSmallGroupPage(sunny.getSite(), null, user, SmallGroup.TYPE_PROJECT, false, page, Page.DEFAULT_PAGE_SIZE));
//		modelAndView.addObject("user", user);
//		
//		modelAndView.addObject("sunny", sunny);
//		modelAndView.setViewName( "/a/user/project" );
//		return modelAndView;
//	}
//
//	@NoPathRedirect
//	@RequestMapping( value = "/a/user/{id}/project" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
//	public ModelAndView getUserProject(
//			@PathVariable("id") Long id,
//			@ParseSunny(shouldExistsSite=true) Sunny sunny,
//			@AuthUser SecurityUser securityUser,
//			@RequestParam(value="page", required=false) Integer page) {
//		return getUserProject(null, id, sunny, securityUser, page);
//	}
	
	@RequestMapping( value = "/{path}/a/user/{id}/group" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView getUserGroup(  
			@PathVariable("path") String path,
			@PathVariable("id") Long id,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="sgtype", required=false) Integer smallGroupType,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) boolean isPagelet) {
		
		ModelAndView modelAndView = new ModelAndView();
		if( sunny.isGoRedirect() == true ){
			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
			return modelAndView;
		}
		User user = userService.findById(id);
		
		if( smallGroupType == null ){
			smallGroupType = SmallGroup.TYPE_GROUP;
		}
		
		
		modelAndView.addObject("user", user);
		modelAndView.addObject("pagedResult", smallGroupService.getSmallGroupPage(sunny.getSite(), null, user, smallGroupType, null, false, page, Page.DEFAULT_PAGE_SIZE));
		//
		//modelAndView.addObject("pagedResult", smallGroupService.getSmallGroupPage(sunny.getSite(), null, user, SmallGroup.TYPE_GROUP, page, Page.DEFAULT_PAGE_SIZE));
		modelAndView.addObject("sunny", sunny);

		
		modelAndView.addObject("firstSmallGroups", smallGroupService.getChildrenSmallGroupIdNames(sunny.getSite(), null, smallGroupType));
		if( isPagelet == true ){
			modelAndView.setViewName( "/pagelet/a/user/group" );
		}else{
			modelAndView.setViewName( "/a/user/group" );
		}
		return modelAndView;
	}

	@NoPathRedirect
	@RequestMapping( value = "/a/user/{id}/group" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView getUserGroup(
			@PathVariable("id") Long id,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="sgtype", required=false) Integer smallGroupType,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) boolean isPagelet) {
		return getUserGroup(null, id, sunny, securityUser, smallGroupType, page, isPagelet);
	}
	
	
	
	
	@RequestMapping( value = "/{path}/a/department" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView getDepartment(  
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="q", required=false) String queryName,
			@RequestParam(value="page", required=false) Integer page) {
		
		ModelAndView modelAndView = new ModelAndView();
		if( sunny.isGoRedirect() == true ){
			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
			return modelAndView;
		}
		modelAndView.addObject("sunny", sunny);

		modelAndView.setViewName( "/a/department/index" );
		return modelAndView;
	}

	@NoPathRedirect
	@RequestMapping( value = "/a/department" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView getDepartment(
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="q", required=false) String queryName,
			@RequestParam(value="page", required=false) Integer page) {
		
		return getDepartment(null, sunny, securityUser, queryName, page);
	}
	
	/** 
	 * 
	 * smallGroup을 tree로 보여주는 예 
	 * ajax로 변경해야함.
	 * */
	@RequestMapping( value = "/a/department/tree" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView getDepartmentTree(  
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="page", required=false) Integer page) {
		return getDepartmentTree(null, SmallGroup.TYPE_DEPARTMENT, sunny, securityUser, page);
	}	

	@RequestMapping( value = "/{path}/a/department/tree" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView getDepartmentTree(  
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="page", required=false) Integer page) {
		return getDepartmentTree(path, SmallGroup.TYPE_DEPARTMENT, sunny, securityUser, page);
	}
	
	@RequestMapping( value = "/a/department/tree/{smallGroupType}" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView getDepartmentTree(  
			@PathVariable("smallGroupType") int smallGroupType,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="page", required=false) Integer page) {
		return getDepartmentTree(null, SmallGroup.TYPE_DEPARTMENT, sunny, securityUser, page);
	}
	
	@RequestMapping( value = "/{path}/a/department/tree/{smallGroupType}" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView getDepartmentTree(  
			@PathVariable("path") String path,
			@PathVariable("smallGroupType") int smallGroupType,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="page", required=false) Integer page) {
		
		ModelAndView modelAndView = new ModelAndView();
		if( sunny.isGoRedirect() == true ){
			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
			return modelAndView;
		}
		//Page<SmallGroup> smallGroupPage = smallGroupService.getSmallGroupPage(sunny.getSite(), null, null, smallGroupType, true, page, 5000);

		
		
		
		//modelAndView.addObject("pagedResult", smallGroupPage);
		modelAndView.addObject("sunny", sunny);
		modelAndView.setViewName( "/a/department/tree" );
		return modelAndView;
	}

	
	
	@RequestMapping( value = "/{path}/a/accessGroup" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView getAccessGroup(  
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="page", required=false) Integer page) {
		
		ModelAndView modelAndView = new ModelAndView();
		if( sunny.isGoRedirect() == true ){
			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
			return modelAndView;
		}
		
		modelAndView.addObject("sunny", sunny);
		modelAndView.setViewName( "/a/access_group/index" );
		return modelAndView;
	}

	@NoPathRedirect
	@RequestMapping( value = "/a/accessGroup" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView getAccessGroup(
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="page", required=false) Integer page) {
		return getAccessGroup(null, sunny, securityUser, page);
	}
	
	
	@RequestMapping( value = "/{path}/a/site" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView getSite(  
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="page", required=false) Integer page) {
		
		ModelAndView modelAndView = new ModelAndView();
		if( sunny.isGoRedirect() == true ){
			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
			return modelAndView;
		}
		
		modelAndView.addObject("sunny", sunny);
		modelAndView.addObject("site", sunny.getSite());
		modelAndView.setViewName( "/a/site/index" );
		return modelAndView;
	}

	@NoPathRedirect
	@RequestMapping( value = "/a/site" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView getSite(
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="page", required=false) Integer page) {
		return getSite(null, sunny, securityUser, page);
	}

	@RequestMapping( value = "/{path}/a/menu" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView getMenu(  
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser) {
		
		ModelAndView modelAndView = new ModelAndView();
		if( sunny.isGoRedirect() == true ){
			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
			return modelAndView;
		}
		
		modelAndView.addObject("sunny", sunny);
		modelAndView.addObject("site", sunny.getSite());
		modelAndView.addObject("siteMenus", menuService.getSiteMenus(sunny));
		modelAndView.setViewName( "/a/menu/index" );
		return modelAndView;
	}

	@NoPathRedirect
	@RequestMapping( value = "/a/menu" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView getMenu(
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser) {
		return getMenu(null, sunny, securityUser);
	}

	

	@RequestMapping( value = "/{path}/a/site/inactive_users" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView getSiteInactiveUsers(  
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="page", required=false) Integer page) {
		
		ModelAndView modelAndView = new ModelAndView();
		if( sunny.isGoRedirect() == true ){
			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
			return modelAndView;
		}
		
		modelAndView.addObject("sunny", sunny);
		modelAndView.addObject("site", sunny.getSite());
		
		modelAndView.addObject("pagedResult", siteInactiveUserService.getSiteInactiveUsers( sunny, SiteInactiveUser.STATUS_REQUEST, SiteInactiveUser.TYPE_REQUEST, page, 1000) );
		
		modelAndView.setViewName( "/a/site/inactive_users" );
		return modelAndView;
	}
	@NoPathRedirect
	@RequestMapping( value = "/a/site/inactive_users" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView getSiteInactiveUsers(
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="page", required=false) Integer page) {
		return getSiteInactiveUsers(null, sunny, securityUser, page);
	}
	@RequestMapping( value = "/{path}/a/site/invite_users" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView getSiteInviteUsers(  
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="page", required=false) Integer page) {
		
		ModelAndView modelAndView = new ModelAndView();
		if( sunny.isGoRedirect() == true ){
			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
			return modelAndView;
		}
		
		modelAndView.addObject("sunny", sunny);
		modelAndView.addObject("site", sunny.getSite());
		
		modelAndView.addObject("pagedResult", siteInactiveUserService.getSiteInactiveUsers( sunny, SiteInactiveUser.STATUS_REQUEST, SiteInactiveUser.TYPE_INVITE, page, 1000) );
		
		modelAndView.setViewName( "/a/site/invite_users" );
		return modelAndView;
	}

	@NoPathRedirect
	@RequestMapping( value = "/a/site/invite_users" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView getSiteInviteUsers(
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="page", required=false) Integer page) {
		return getSiteInviteUsers(null, sunny, securityUser, page);
	}
	
	@RequestMapping( value = "/{path}/a/site/sent_users" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView getSiteInactiveSentUsers(  
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="page", required=false) Integer page) {
		
		ModelAndView modelAndView = new ModelAndView();
		if( sunny.isGoRedirect() == true ){
			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
			return modelAndView;
		}
		
		modelAndView.addObject("sunny", sunny);
		modelAndView.addObject("site", sunny.getSite());
		
		modelAndView.addObject("pagedResult", siteInactiveUserService.getSiteInactiveUsers( sunny, SiteInactiveUser.STATUS_SEND_EMAIL, SiteInactiveUser.TYPE_REQUEST, page, 1000) );
		
		modelAndView.setViewName( "/a/site/sent_users" );
		return modelAndView;
	}

	@NoPathRedirect
	@RequestMapping( value = "/a/site/sent_users" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView getSiteInactiveSentUsers(
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="page", required=false) Integer page) {
		return getSiteInactiveSentUsers(null, sunny, securityUser, page);
	}
	
	

	@RequestMapping( value = "/a/site/remove_invite", method = RequestMethod.GET )
	@ResponseBody
	public ModelAndView removeInvite(
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="id", required=true) Long siteInactiveUserId ){  
		
		ModelAndView modelAndView = new ModelAndView();


		siteInactiveUserService.delete( siteInactiveUserId );
		
		String redirect = "redirect:/" + securityUser.getSiteId() + "/a/site/invite_users" ;

		modelAndView.setViewName( redirect );
		return modelAndView;
	}

	

	
	@RequestMapping( value = "/a/site/resend_invite" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView siteResendInviteEmail(  
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="id", required=false) Long siteInactiveUserId) {
		
		ModelAndView modelAndView = new ModelAndView();
		
		SiteInactiveUser siteInactiveUser = siteInactiveUserService.findById(siteInactiveUserId);
		
		mailService.sendInviteSiteInactiveUserMail( sunny, siteInactiveUser );
		
		String redirect = "redirect:/a/site/invite_users" ;
		modelAndView.setViewName( redirect );
		return modelAndView;
	}

//	@RequestMapping( value = "/{path}/a/site/deny" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
//	public ModelAndView siteDenyInactiveUser(  
//			@PathVariable("path") String path,
//			@ParseSunny(shouldExistsSite=true) Sunny sunny,
//			@AuthUser SecurityUser securityUser,
//			@RequestParam(value="id", required=false) Long siteInactiveUserId) {
//		
//		ModelAndView modelAndView = new ModelAndView();
//		
//		siteInactiveUserService.delete( siteInactiveUserId );
//		modelAndView.setViewName( referer );
//		return modelAndView;
//	}
//
//	@NoPathRedirect
//	@RequestMapping( value = "/a/site/deny" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
//	public ModelAndView siteDenyInactiveUser(
//			@ParseSunny(shouldExistsSite=true) Sunny sunny,
//			@AuthUser SecurityUser securityUser,
//			@RequestParam(value="id", required=false) Long siteInactiveUserId, 
//			@RequestHeader(value = "referer", required = false) final String referer) {
//		return siteDenyInactiveUser(null, sunny, securityUser, siteInactiveUserId, referer);
//	}

	
	
	@RequestMapping( value = "/{path}/a/site/accept_after_confirm" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView siteAcceptAfterConfirm(  
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="id", required=false) Long siteInactiveUserId) {
		
		ModelAndView modelAndView = new ModelAndView();
		
		siteService.acceptInactiveUserAfterConfirm( sunny, siteInactiveUserId );
		String redirect = "redirect:/" + securityUser.getSiteId() + "/a/site/inactive_users" ;
		modelAndView.setViewName( redirect );
		return modelAndView;
	}

	@NoPathRedirect
	@RequestMapping( value = "/a/site/accept_after_confirm" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView siteAcceptAfterConfirm(
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="id", required=false) Long siteInactiveUserId) {
		return siteAcceptAfterConfirm(null, sunny, securityUser, siteInactiveUserId);
	}
	
	
	@RequestMapping( value = "/{path}/a/department/{id}/info" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView getDepartmentInfo(  
			@PathVariable("path") String path,
			@PathVariable("id") Long id,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser) {
		ModelAndView modelAndView = new ModelAndView();
		if( sunny.isGoRedirect() == true ){
			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
			return modelAndView;
		}
		
		SmallGroup smallGroup = smallGroupService.getSmallGroup(id);
		
		modelAndView.addObject("smallGroup", smallGroup);
		modelAndView.addObject("sunny", sunny);
		modelAndView.setViewName( "/a/department/info" );
		return modelAndView;
	}

	@NoPathRedirect
	@RequestMapping( value = "/a/department/{id}/info" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView getDepartmentInfo(
			@PathVariable("id") Long id,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser) {
		return getDepartmentInfo(null, id, sunny, securityUser);
	}	
	
	@RequestMapping( value = "/{path}/a/department/{id}/users" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView getDepartmentUsers(  
			@PathVariable("path") String path,
			@PathVariable("id") Long id,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="q", required=false) String queryName,
			@RequestParam(value="ordering", required=false) String ordering,
			@RequestParam(value="desc", required=false) Boolean desc,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) Boolean isPagelet) {
		
		ModelAndView modelAndView = new ModelAndView();
		if( sunny.isGoRedirect() == true ){
			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
			return modelAndView;
		}
		SmallGroup smallGroup = smallGroupService.getSmallGroup(id);
		
		Page<UserSmallGroupAccess> pagedResult = userSmallGroupAccessService.getJoined(sunny, id, null, queryName, ordering, desc, page, Page.DEFAULT_PAGE_SIZE);
		
		modelAndView.addObject("site", sunny.getSite());
		modelAndView.addObject("smallGroup", smallGroup);
		modelAndView.addObject("pagedResult",pagedResult);
		
		
		modelAndView.addObject("sunny", sunny);
		if( isPagelet != null && isPagelet == true ){
			modelAndView.setViewName( "/pagelet/a/department/user" );
		}else{
			modelAndView.setViewName( "/a/department/users" );
		}
		return modelAndView;
	}

	@NoPathRedirect
	@RequestMapping( value = "/a/department/{id}/users" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView getDepartmentUsers(
			@PathVariable("id") Long id,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="q", required=false) String queryName,
			@RequestParam(value="ordering", required=false) String ordering,
			@RequestParam(value="desc", required=false) Boolean desc,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) Boolean isPagelet) {
		return getDepartmentUsers(null, id, sunny, securityUser, queryName, ordering, desc, page, isPagelet);
	}
	
	@RequestMapping( value = "/{path}/a/department/{id}/access" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView getDepartmentAccessUsers(  
			@PathVariable("path") String path,
			@PathVariable("id") Long id,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="q", required=false) String queryName,
			@RequestParam(value="ordering", required=false) String ordering,
			@RequestParam(value="desc", required=false) Boolean desc,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) Boolean isPagelet) {
		
		ModelAndView modelAndView = new ModelAndView();
		if( sunny.isGoRedirect() == true ){
			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
			return modelAndView;
		}
		SmallGroup smallGroup = smallGroupService.getSmallGroup(id);
		
		Page<SmallGroupSmallGroupAccess> pagedResult = smallGroupSmallGroupAccessService.getPagedAccessibleList(sunny, id, queryName, ordering, desc, page, Page.DEFAULT_PAGE_SIZE);
		//Page<UserSmallGroupAccess> pagedResult = userSmallGroupAccessService.getJoined(sunny, id, null, queryName, ordering, desc, page, Page.DEFAULT_PAGE_SIZE);
		
		modelAndView.addObject("site", sunny.getSite());
		modelAndView.addObject("smallGroup", smallGroup);
		modelAndView.addObject("pagedResult",pagedResult);
		
		
		modelAndView.addObject("sunny", sunny);
		if( isPagelet != null && isPagelet == true ){
			modelAndView.setViewName( "/pagelet/a/department/access" );
		}else{
			modelAndView.setViewName( "/a/department/access" );
		}
		return modelAndView;
	}

	@NoPathRedirect
	@RequestMapping( value = "/a/department/{id}/access" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView getDepartmentAccessUsers(
			@PathVariable("id") Long id,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="q", required=false) String queryName,
			@RequestParam(value="ordering", required=false) String ordering,
			@RequestParam(value="desc", required=false) Boolean desc,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) Boolean isPagelet) {
		return getDepartmentAccessUsers(null, id, sunny, securityUser, queryName, ordering, desc, page, isPagelet);
	}
}
