package kr.co.sunnyvale.sunny.controller.web;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import kr.co.sunnyvale.sunny.annotation.AuthUser;
import kr.co.sunnyvale.sunny.annotation.NoPathRedirect;
import kr.co.sunnyvale.sunny.annotation.ParseSunny;
import kr.co.sunnyvale.sunny.annotation.SmallGroupPrivacy;
import kr.co.sunnyvale.sunny.domain.Pds;
import kr.co.sunnyvale.sunny.domain.SmallGroup;
import kr.co.sunnyvale.sunny.domain.SmallGroupInactiveUser;
import kr.co.sunnyvale.sunny.domain.SmallGroupSmallGroupAccess;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.UserSmallGroupAccess;
import kr.co.sunnyvale.sunny.domain.dto.StoryDTO;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.repository.hibernate.SmallGroupInactiveUserRepository;
import kr.co.sunnyvale.sunny.security.SecurityUserService;
import kr.co.sunnyvale.sunny.service.BookMarkService;
import kr.co.sunnyvale.sunny.service.FriendService;
import kr.co.sunnyvale.sunny.service.MailService;
import kr.co.sunnyvale.sunny.service.PdsService;
import kr.co.sunnyvale.sunny.service.SiteService;
import kr.co.sunnyvale.sunny.service.SmallGroupService;
import kr.co.sunnyvale.sunny.service.SmallGroupSmallGroupAccessService;
import kr.co.sunnyvale.sunny.service.StoryService;
import kr.co.sunnyvale.sunny.service.TagService;
import kr.co.sunnyvale.sunny.service.UserService;
import kr.co.sunnyvale.sunny.service.UserSmallGroupAccessService;
import kr.co.sunnyvale.sunny.util.LoginUtils;
import kr.co.sunnyvale.sunny.util.SearchUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SmallGroupController {

	@Autowired
	private UserService userService;

	@Autowired
	private SecurityUserService securityUserService;
	
	@Autowired
	private SiteService siteService;
	
	@Autowired
	private StoryService storyService;
	
	@Autowired
	private FriendService friendRequestService;
	
	@Autowired
	private SmallGroupService smallGroupService;
	
	@Autowired
	private UserSmallGroupAccessService userSmallGroupAccessService;
	
	@Autowired
	private SmallGroupInactiveUserRepository smallGroupInactiveUserRepository;
	
	@Autowired
	private SmallGroupSmallGroupAccessService smallGroupSmallGroupAccessService;
	
	@Autowired
	private BookMarkService bookMarkService;
	
	@Autowired
	private PdsService pdsService; 
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private TagService tagService;

	@SmallGroupPrivacy(onlyAdmin = false)
	@RequestMapping( value = "/{path}/group/{id}/joined" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView groupJoinedUsers(  
			@PathVariable("path") String path,
			@PathVariable("id") Long smallGroupId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="q", required=false) String queryName,
			@RequestParam(value="ordering", required=false) String ordering,
			@RequestParam(value="desc", required=false) Boolean desc,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) Boolean isPagelet ) throws ParseException {

		ModelAndView modelAndView = new ModelAndView();
		
		if( sunny.isGoRedirect() == true ){
			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
			return modelAndView;
		}
		LoginUtils.checkLogin(securityUser);

		
		Page<UserSmallGroupAccess> pagedResult = userSmallGroupAccessService.getJoined(sunny, smallGroupId, null, queryName, ordering, desc, page, Page.DEFAULT_PAGE_SIZE);
		
		modelAndView.addObject("pagedResult", pagedResult);
		
		if( isPagelet != null && isPagelet == true){
			modelAndView.setViewName("/pagelet/group/user");
		}else{
			modelAndView.setViewName("/group/group/user");			
		}


		return modelAndView;
	}
	

	@SmallGroupPrivacy(onlyAdmin = false)
	@NoPathRedirect
	@RequestMapping( value = "/group/{id}/joined" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView groupJoinedUsers(  
			@PathVariable("id") Long smallGroupId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="q", required=false) String queryName,
			@RequestParam(value="ordering", required=false) String ordering,
			@RequestParam(value="desc", required=false) Boolean desc,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) Boolean isPagelet  ) throws ParseException {
		
		return groupJoinedUsers(null, smallGroupId, sunny, securityUser, queryName, ordering, desc, page, isPagelet);
	}

	
	@SmallGroupPrivacy(onlyAdmin = false)
	@RequestMapping( value = "/{path}/group/{id}/inactive" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView groupInactiveUsers(  
			@PathVariable("path") String path,
			@PathVariable("id") Long smallGroupId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="q", required=false) String queryName,
			@RequestParam(value="ordering", required=false) String ordering,
			@RequestParam(value="desc", required=false) Boolean desc,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) Boolean isPagelet ) throws ParseException {

		ModelAndView modelAndView = new ModelAndView();
		
		if( sunny.isGoRedirect() == true ){
			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
			return modelAndView;
		}
		LoginUtils.checkLogin(securityUser);

		
		Page<SmallGroupInactiveUser> pagedResult = smallGroupInactiveUserRepository.getJoinUsers(sunny, smallGroupId, queryName, ordering, desc, page, Page.DEFAULT_PAGE_SIZE);
		
		modelAndView.addObject("pagedResult", pagedResult);
		
		modelAndView.addObject("inactive", true);

		if( isPagelet != null && isPagelet == true){
			modelAndView.setViewName("/pagelet/group/user");
		}else{
			modelAndView.setViewName("/group/group/user");			
		}


		return modelAndView;
	}
	

	@SmallGroupPrivacy(onlyAdmin = false)
	@NoPathRedirect
	@RequestMapping( value = "/group/{id}/inactive" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView groupInactiveUsers(  
			@PathVariable("id") Long smallGroupId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="q", required=false) String queryName,
			@RequestParam(value="ordering", required=false) String ordering,
			@RequestParam(value="desc", required=false) Boolean desc,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) Boolean isPagelet  ) throws ParseException {
		
		return groupInactiveUsers(null, smallGroupId, sunny, securityUser, queryName, ordering, desc, page, isPagelet);
	}
	
	
	@SmallGroupPrivacy(onlyAdmin = false)
	@RequestMapping( value = "/{path}/group/{id}/access" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView groupAccess(  
			@PathVariable("path") String path,
			@PathVariable("id") Long smallGroupId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="q", required=false) String queryName,
			@RequestParam(value="ordering", required=false) String ordering,
			@RequestParam(value="desc", required=false) Boolean desc,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) Boolean isPagelet ) throws ParseException {

		ModelAndView modelAndView = new ModelAndView();
		
		if( sunny.isGoRedirect() == true ){
			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
			return modelAndView;
		}
		LoginUtils.checkLogin(securityUser);

		
		Page<SmallGroupSmallGroupAccess> pagedResult = smallGroupSmallGroupAccessService.getPagedAccessibleList(sunny, smallGroupId, queryName, ordering, desc, page, Page.DEFAULT_PAGE_SIZE);
		
		modelAndView.addObject("pagedResult", pagedResult);

		if( isPagelet != null && isPagelet == true){
			modelAndView.setViewName("/pagelet/group/access");
		}else{
			modelAndView.setViewName("/group/group/access");
		}


		return modelAndView;
	}
	

	@SmallGroupPrivacy(onlyAdmin = false)
	@NoPathRedirect
	@RequestMapping( value = "/group/{id}/access" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView groupAccess(  
			@PathVariable("id") Long smallGroupId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="q", required=false) String queryName,
			@RequestParam(value="ordering", required=false) String ordering,
			@RequestParam(value="desc", required=false) Boolean desc,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) Boolean isPagelet  ) throws ParseException {
		
		return groupAccess(null, smallGroupId, sunny, securityUser, queryName, ordering, desc, page, isPagelet);
	}
	
	@SmallGroupPrivacy(onlyAdmin = true)
	@RequestMapping( value = "/{path}/group/{id}/setting" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView setting(  
			@PathVariable("path") String path,
			@PathVariable("id") Long smallGroupId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser) {

		ModelAndView modelAndView = new ModelAndView();
		
		if( sunny.isGoRedirect() == true ){
			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
			return modelAndView;
		}
		LoginUtils.checkLogin(securityUser);

		modelAndView.setViewName("/group/group/setting");

		return modelAndView;
	}
	

	@SmallGroupPrivacy(onlyAdmin = true)
	@NoPathRedirect
	@RequestMapping( value = "/group/{id}/setting" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView setting(  
			@PathVariable("id") Long smallGroupId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser) {
		
		return setting(null, smallGroupId, sunny, securityUser);
				
	}


	

	@SmallGroupPrivacy( onlyAdmin = true )
	@RequestMapping( value = "/{path}/group/{id}/setting", method = RequestMethod.POST, headers = { "Accept=text/html" })
	public ModelAndView update( 
			@PathVariable("path") String path,
			@PathVariable("id") Long smallGroupId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@ModelAttribute SmallGroup smallGroup, 
			HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView();
		smallGroupService.update(smallGroup);
		
		modelAndView.setViewName("redirect:" + request.getRequestURI() );
		return modelAndView; 
	}
	
	@SmallGroupPrivacy( onlyAdmin = true )
	@NoPathRedirect
	@RequestMapping( value = "/group/{id}/setting", method = RequestMethod.POST, headers = { "Accept=text/html" })
	public ModelAndView update( 
			@PathVariable("id") Long smallGroupId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@ModelAttribute SmallGroup smallGroup, 
			HttpServletRequest request) {

		return update(null, smallGroupId, sunny, securityUser, smallGroup, request); 
	}
//	@SmallGroupPrivacy(onlyAdmin = false)
//	@RequestMapping( value = "/{path}/department/{id}" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
//	public ModelAndView departmentView(  
//			@PathVariable("path") String path,
//			@PathVariable("id") Long smallGroupId,
//			@ParseSunny(shouldExistsSite=true) Sunny sunny,
//			@AuthUser SecurityUser securityUser,
//			@RequestParam(value="q", required=false) String queryName,
//			@RequestParam(value="ordering", required=false) String ordering,
//			@RequestParam(value="page", required=false) Integer page) throws ParseException {
//
//		
//		ModelAndView modelAndView = new ModelAndView();
//		
//		if( sunny.isGoRedirect() == true ){
//			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
//			return modelAndView;
//		}
//		LoginUtils.checkLogin(securityUser);
//		
//		SmallGroup smallGroup = smallGroupService.getSmallGroup(smallGroupId);
//		modelAndView.addObject("smallGroup", smallGroup);
//				
//		modelAndView.setViewName("/group/department/story");
//
//		return modelAndView;
//	}
//	
//
//	@NoPathRedirect
//	@RequestMapping( value = "/department/{id}" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
//	public ModelAndView departmentView(  
//			@PathVariable("id") Long smallGroupId,
//			@ParseSunny(shouldExistsSite=true) Sunny sunny,
//			@AuthUser SecurityUser securityUser,
//			@RequestParam(value="q", required=false) String queryName,
//			@RequestParam(value="ordering", required=false) String ordering,
//			@RequestParam(value="page", required=false) Integer page ) throws ParseException {
//		
//		return departmentView(null, smallGroupId, sunny, securityUser, queryName, ordering, page);
//				
//	}

	

	@SmallGroupPrivacy(onlyAdmin = false)
	@RequestMapping( value = "/{path}/group/{id}" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView groupView(  
			@PathVariable("path") String path,
			@PathVariable("id") Long smallGroupId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="q[]", required=false) String[] queries,
			@RequestParam(value="nq", required=false) String newQuery,
			@RequestParam(value="recursive", required=false) Boolean isRecursive,
			@RequestParam(value="ordering", required=false) String ordering) throws ParseException {

		
		ModelAndView modelAndView = new ModelAndView();
		
		if( sunny.isGoRedirect() == true ){
			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
			return modelAndView;
		}
		LoginUtils.checkLogin(securityUser);
		
//		SmallGroup smallGroup = smallGroupService.getSmallGroup(smallGroupId);
//		modelAndView.addObject("group", smallGroup);
//		
		
		Stream stream = new Stream(null, null, null, 10);
		
		if( queries != null ){
			queries = SearchUtils.checkAndFixQuery( queries, newQuery, isRecursive );
		}

		User user = userService.findById(securityUser.getUserId());
		
		boolean isWantChildren = false;
		List<StoryDTO> plazaStories = storyService.fetchSmallGroupStories(sunny, smallGroupId, user, isWantChildren, queries, stream);
		
		modelAndView.addObject("stories",plazaStories);
	
		modelAndView.addObject("queries",  queries);
		
		modelAndView.addObject("composerTags", tagService.getComposerTags( smallGroupId ));

		modelAndView.setViewName("/group/group/story");

		return modelAndView;
	}

	@SmallGroupPrivacy(onlyAdmin = false)
	@NoPathRedirect
	@RequestMapping( value = "/group/{id}" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView groupView(  
			@PathVariable("id") Long smallGroupId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="q[]", required=false) String[] queries,
			@RequestParam(value="nq", required=false) String newQuery,
			@RequestParam(value="recursive", required=false) Boolean isRecursive,
			@RequestParam(value="ordering", required=false) String ordering) throws ParseException {
		
		return groupView(null, smallGroupId, sunny, securityUser, queries, newQuery, isRecursive, ordering);
	}

	@SmallGroupPrivacy(onlyAdmin = false)
	@RequestMapping( value = "/{path}/group/{id}/about" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView about(  
			@PathVariable("path") String path,
			@PathVariable("id") Long smallGroupId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser) throws ParseException {

		
		ModelAndView modelAndView = new ModelAndView();
		
		if( sunny.isGoRedirect() == true ){
			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
			return modelAndView;
		}
		LoginUtils.checkLogin(securityUser);
		
		modelAndView.setViewName("/group/group/about");

		return modelAndView;
	}

	@SmallGroupPrivacy(onlyAdmin = false)
	@NoPathRedirect
	@RequestMapping( value = "/group/{id}/about" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView about(  
			@PathVariable("id") Long smallGroupId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser) throws ParseException {
		
		return about(null, smallGroupId, sunny, securityUser);
	}


	@SmallGroupPrivacy(onlyAdmin = false)
	@RequestMapping( value = "/{path}/group/{id}/pds" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView pds(  
			@PathVariable("path") String path,
			@PathVariable("id") Long smallGroupId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="q", required=false) String query,
			@RequestParam(value="ordering", required=false) String ordering,
			@RequestParam(value="desc", required=false) Boolean desc,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) Boolean isPagelet ) throws ParseException {

		
		ModelAndView modelAndView = new ModelAndView();
		
		if( sunny.isGoRedirect() == true ){
			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
			return modelAndView;
		}
		LoginUtils.checkLogin(securityUser);
		
		User user = userService.findById(securityUser.getUserId());
		
		Page<Pds> pagedResult = pdsService.getPagedSmallGroupsPds( sunny, smallGroupId, null, user, query, ordering, page, Page.DEFAULT_PAGE_SIZE);

		modelAndView.addObject("pagedResult", pagedResult);
		modelAndView.addObject("current", "list");
		
		if( isPagelet != null && isPagelet ){
			modelAndView.setViewName("/pagelet/pds/list");
		}else{
			modelAndView.setViewName("/group/group/pds");
		}
		return modelAndView;
	}
	

	@SmallGroupPrivacy(onlyAdmin = false)
	@NoPathRedirect
	@RequestMapping( value = "/group/{id}/pds" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView pds(  
			@PathVariable("id") Long smallGroupId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="q", required=false) String queryName,
			@RequestParam(value="ordering", required=false) String ordering,
			@RequestParam(value="desc", required=false) Boolean desc,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) Boolean isPagelet ) throws ParseException {
		return pds(null, smallGroupId, sunny, securityUser, queryName, ordering, desc, page, isPagelet);
	}
	
	
	
	@SmallGroupPrivacy(onlyAdmin = false)
	@RequestMapping( value = "/{path}/group/{id}/pds/{pid}" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView pdsView(  
			@PathVariable("path") String path,
			@PathVariable("id") Long smallGroupId,
			@PathVariable("pid") Long pdsId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="pl", required=false) Boolean isPagelet ) throws ParseException {

		
		ModelAndView modelAndView = new ModelAndView();
		
		if( sunny.isGoRedirect() == true ){
			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
			return modelAndView;
		}
		LoginUtils.checkLogin(securityUser);

		modelAndView.addObject("content", pdsService.getPds(sunny, new User(securityUser.getUserId()), pdsId));
		
		modelAndView.addObject("alreadyBookmarked", bookMarkService.isAlreadyBookMark(sunny, pdsId, securityUser.getUserId()));
		
		modelAndView.addObject("current", "view");

		if( isPagelet != null && isPagelet ){
			modelAndView.setViewName("/pagelet/pds/view");
		}else{
			modelAndView.setViewName("/group/group/pds");
		}

		return modelAndView;
	}
	

	@SmallGroupPrivacy(onlyAdmin = false)
	@NoPathRedirect
	@RequestMapping( value = "/group/{id}/pds/{pid}" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView pdsView(  
			@PathVariable("id") Long smallGroupId,
			@PathVariable("pid") Long pdsId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="pl", required=false) Boolean isPagelet  ) throws ParseException {
		return pdsView(null, smallGroupId, pdsId, sunny, securityUser, isPagelet);
	}
	
	@SmallGroupPrivacy(onlyAdmin = false)
	@RequestMapping( value = "/{path}/group/{id}/pds/write" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView writePds(  
			@PathVariable("path") String path,
			@PathVariable("id") Long smallGroupId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser) throws ParseException {

		
		ModelAndView modelAndView = new ModelAndView();
		
		if( sunny.isGoRedirect() == true ){
			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
			return modelAndView;
		}
		LoginUtils.checkLogin(securityUser);
		
		modelAndView.addObject("current", "write");
		
		modelAndView.setViewName("/group/group/pds");

		return modelAndView;
	}
	

	@SmallGroupPrivacy(onlyAdmin = false)
	@NoPathRedirect
	@RequestMapping( value = "/group/{id}/pds/write" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView writePds(  
			@PathVariable("id") Long smallGroupId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser) throws ParseException {
		return writePds(null, smallGroupId, sunny, securityUser);
	}
	
	
	@RequestMapping( value = "/{path}/group" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView groups(  
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="tab", required=false) String tab,
			@RequestParam(value="q", required=false) String queryName,
			@RequestParam(value="ordering", required=false) String ordering,
			@RequestParam(value="desc", required=false) Boolean desc,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) Boolean isPagelet) throws ParseException {

		
		ModelAndView modelAndView = new ModelAndView();
		
		if( sunny.isGoRedirect() == true ){
			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
			return modelAndView;
		}
		LoginUtils.checkLogin(securityUser);

		
		
		User user = null;
		
		if( "my".equals(tab) ){
			user = new User(securityUser.getUserId());
		}
		
		Page<SmallGroup> pagedResult = smallGroupService.getSmallGroupPage(sunny.getSite(), null, user, SmallGroup.TYPE_GROUP, queryName, ordering, desc, page, Page.DEFAULT_PAGE_SIZE);

		modelAndView.addObject("pagedResult", pagedResult);
		
		if( isPagelet == null || isPagelet == false ){
			modelAndView.setViewName("/group/group/list");	
		}else{
			modelAndView.setViewName("/pagelet/group/list");
		}

		return modelAndView;
	}
	

	@NoPathRedirect
	@RequestMapping( value = "/group" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView groups(  
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="tab", required=false) String tab,
			@RequestParam(value="q", required=false) String queryName,
			@RequestParam(value="ordering", required=false) String ordering,
			@RequestParam(value="desc", required=false) Boolean desc,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) Boolean isPagelet ) throws ParseException {
		
		return groups(null, sunny, securityUser, tab, queryName, ordering, desc, page, isPagelet);
				
	}


	
	
	@RequestMapping( value = "/{path}/project" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView projects(  
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="tab", required=false) String tab,
			@RequestParam(value="q", required=false) String queryName,
			@RequestParam(value="ordering", required=false) String ordering,
			@RequestParam(value="desc", required=false) Boolean desc,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) Boolean isPagelet) throws ParseException {

		
		ModelAndView modelAndView = new ModelAndView();
		
		if( sunny.isGoRedirect() == true ){
			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
			return modelAndView;
		}
		LoginUtils.checkLogin(securityUser);
		
		User user = null;
		
		if( "my".equals(tab) ){
			user = new User(securityUser.getUserId());
		}
		
		Page<SmallGroup> pagedResult = smallGroupService.getSmallGroupPage(sunny.getSite(), null, user, SmallGroup.TYPE_PROJECT, queryName, ordering, desc, page, Page.DEFAULT_PAGE_SIZE);

		modelAndView.addObject("pagedResult", pagedResult);
		
		if( isPagelet == null || isPagelet == false ){
			modelAndView.setViewName("/group/project/list");	
		}else{
			modelAndView.setViewName("/pagelet/group/list");
		}

		return modelAndView;
	}
	

	@NoPathRedirect
	@RequestMapping( value = "/project" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView projects(  
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="tab", required=false) String tab,
			@RequestParam(value="q", required=false) String queryName,
			@RequestParam(value="ordering", required=false) String ordering,
			@RequestParam(value="desc", required=false) Boolean desc,
			@RequestParam(value="page", required=false) Integer page ,
			@RequestParam(value="pl", required=false) Boolean isPagelet) throws ParseException {
		
		return projects(null, sunny, securityUser, tab, queryName, ordering, desc, page, isPagelet);
				
	}

	
	
	
	@RequestMapping( value = "/{path}/department" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView departments(  
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="tab", required=false) String tab,
			@RequestParam(value="q", required=false) String queryName,
			@RequestParam(value="ordering", required=false) String ordering,
			@RequestParam(value="desc", required=false) Boolean desc,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) Boolean isPagelet) throws ParseException {

		
		ModelAndView modelAndView = new ModelAndView();
		
		if( sunny.isGoRedirect() == true ){
			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
			return modelAndView;
		}
		LoginUtils.checkLogin(securityUser);
		User user = null;
		
		
		Page<SmallGroup> pagedResult = null; 
				
		if( "my".equals(tab) ){
			user = new User(securityUser.getUserId());
			pagedResult = smallGroupService.getSmallGroupPage(sunny.getSite(), null, user, SmallGroup.TYPE_DEPARTMENT, queryName, false, page, Page.DEFAULT_PAGE_SIZE);
		}else{
			pagedResult = smallGroupService.getSmallGroupPage(sunny.getSite(), null, null, SmallGroup.TYPE_DEPARTMENT, queryName, false, null, 10000);
		}
		

		modelAndView.addObject("pagedResult", pagedResult);
		if( isPagelet == null || isPagelet == false ){
			modelAndView.setViewName("/group/department/list");	
		}else{
			modelAndView.setViewName("/pagelet/group/list");
		}
		

		return modelAndView;
	}
	

	@NoPathRedirect
	@RequestMapping( value = "/department" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView departments(  
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="tab", required=false) String tab,
			@RequestParam(value="q", required=false) String queryName,
			@RequestParam(value="ordering", required=false) String ordering,
			@RequestParam(value="desc", required=false) Boolean desc,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) Boolean isPagelet) throws ParseException {
		
		return departments(null, sunny, securityUser, tab, queryName, ordering, desc, page, isPagelet);
				
	}
	
}