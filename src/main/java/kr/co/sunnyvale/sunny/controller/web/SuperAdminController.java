package kr.co.sunnyvale.sunny.controller.web;

import java.text.ParseException;
import java.util.List;

import kr.co.sunnyvale.sunny.annotation.AuthUser;
import kr.co.sunnyvale.sunny.annotation.ParseSunny;
import kr.co.sunnyvale.sunny.domain.DefaultTag;
import kr.co.sunnyvale.sunny.domain.MailingQueue;
import kr.co.sunnyvale.sunny.domain.Menu;
import kr.co.sunnyvale.sunny.domain.Site;
import kr.co.sunnyvale.sunny.domain.SiteInactiveUser;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.dto.StoryDTO;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.exception.SimpleSunnyException;
import kr.co.sunnyvale.sunny.repository.hibernate.DefaultTagRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.MailingQueueRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.MenuRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.SiteInactiveUserRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.SiteRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.UserRepository;
import kr.co.sunnyvale.sunny.service.AdminService;
import kr.co.sunnyvale.sunny.service.SiteService;
import kr.co.sunnyvale.sunny.service.StoryService;
import kr.co.sunnyvale.sunny.service.UserService;
import kr.co.sunnyvale.sunny.service.impl.BatchService;
import kr.co.sunnyvale.sunny.service.impl.MenuService;
import kr.co.sunnyvale.sunny.service.impl.SiteInactiveUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SuperAdminController {
	
	@Autowired
	private SiteService	siteService;
	
	@Autowired
	private SiteRepository siteRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BatchService batchService;
	
	@Autowired
	private MenuRepository menuRepository; 

	@Autowired
	private MenuService menuService; 

	@Autowired
	private SiteInactiveUserRepository siteInactiveUserRepository;
	
	@Autowired
	private SiteInactiveUserService siteInactiveUserService; 
	
	@Autowired
	private MailingQueueRepository mailingQueueRepository;
	
	@Autowired
	private StoryService storyService; 
	
	@Autowired
	private DefaultTagRepository defaultTagRepository;
	
	
//	@RequestMapping( value = "/super/batch/media", method = RequestMethod.GET )
	public ModelAndView batchMedia( ){
		ModelAndView modelAndView = new ModelAndView();
		
		batchService.retypeMedia();
		
		modelAndView.setViewName("/super/index");
		return modelAndView;
	}
	
	@RequestMapping( value = "/super", method = RequestMethod.GET )
	public ModelAndView admin( ){
		ModelAndView modelAndView = new ModelAndView();
		
		modelAndView.setViewName("/super/index");
		return modelAndView;
	}
	
//	@RequestMapping( value = "/super/generateDefaults", method = RequestMethod.GET )
//	public ModelAndView generateDefaultRoleAndEvaluate( ){
//		ModelAndView modelAndView = new ModelAndView();
//		adminService.generateDefaults();
//		modelAndView.setViewName("redirect:/super/a/site");
//		return modelAndView;
//	}
	
	@RequestMapping( value = "/super/sync_menu", method = RequestMethod.GET )
	public ModelAndView generateDefaultMenus( ){
		ModelAndView modelAndView = new ModelAndView();
		adminService.syncMenus();
		modelAndView.setViewName("redirect:/super/menu");
		return modelAndView;
	}
	


	@RequestMapping( value = "/super/update_menu", method = RequestMethod.POST )
	public ModelAndView updateMenu( @ModelAttribute Menu menu,
			@AuthUser SecurityUser securityUser, @RequestHeader(value = "referer", required = false) final String referer){
		ModelAndView modelAndView = new ModelAndView();
		menuService.updateChanged( menu );
		modelAndView.setViewName("redirect:" + referer);
		return modelAndView;
	}

	@RequestMapping( value = "/super/create_menu", method = RequestMethod.POST )
	public ModelAndView saveMenu( @ModelAttribute Menu menu,
			@AuthUser SecurityUser securityUser, @RequestHeader(value = "referer", required = false) final String referer){
		ModelAndView modelAndView = new ModelAndView();
		menuService.save( menu );
		modelAndView.setViewName("redirect:" + referer);
		return modelAndView;
	}
	
//	@RequestMapping( value = "/super/a/migrationMultiSite", method = RequestMethod.GET )
//	public ModelAndView migrationMultiSite( 
//			@RequestParam(value="key", required=true) String key ){
//		ModelAndView modelAndView = new ModelAndView();
//		if( !key.equals("kang1004") ){
//			modelAndView.setViewName("redirect:/");
//			return modelAndView;
//		}
//		batchService.migrationMultiSite();
//		modelAndView.setViewName("redirect:/");
//		return modelAndView;
//	}

	
	
	@RequestMapping( value = "/super/menu", method = RequestMethod.GET )
	public ModelAndView allMenu( ){
		ModelAndView modelAndView = new ModelAndView();
		
		List<Menu> menus = menuRepository.getAll(null);
		
		modelAndView.addObject("menus", menus);

		modelAndView.setViewName("/super/menu");
		return modelAndView;
	}
	
	@RequestMapping( value = "/super/users", method = RequestMethod.GET )
	public ModelAndView allUserInfo(
			@RequestParam(value="q", required=false) String queryName,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) Boolean isPagelet ){
		
		ModelAndView modelAndView = new ModelAndView();
		
		Page<User> pagedResult = userService.getAllFromSuper(page, 20);

		modelAndView.addObject("pagedResult", pagedResult);
		
		if( isPagelet != null && isPagelet == true ){
			modelAndView.setViewName("/super/pagelet/users");
		}else{
			modelAndView.setViewName("/super/users");	
		}
		
		return modelAndView;
	}
	
	@RequestMapping( value = "/super/sites", method = RequestMethod.GET )
	public ModelAndView allSite(
			@RequestParam(value="q", required=false) String queryName,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) Boolean isPagelet
			){
		ModelAndView modelAndView = new ModelAndView();
		
		Page<Site> pagedResult= siteService.getAllFromSuper(page, 20);
		
		modelAndView.addObject("pagedResult", pagedResult);

		if( isPagelet != null && isPagelet == true ){
			modelAndView.setViewName("/super/pagelet/sites");
		}else{
			modelAndView.setViewName("/super/sites");	
		}
		
		return modelAndView;
	}
	
	@RequestMapping( value = "/super/pagelet/site_permission" , method = RequestMethod.GET, headers = { "Accept=text/html" } )
	public ModelAndView pageletSmallGroup(  
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="q", required=false) String queryName,
			@RequestParam(value="qt", required=false) Integer queryType,
			@RequestParam(value="ordering", required=false) String ordering,
			@RequestParam(value="page", required=false) Integer page) throws ParseException {

		ModelAndView modelAndView = new ModelAndView();

		
		Page<Site> pagedResult = siteService.getAllFromSuper(page, Page.DEFAULT_PAGE_SIZE);
		modelAndView.addObject("pagedResult", pagedResult);
		modelAndView.setViewName("/super/pagelet/site_permission");
		modelAndView.addObject("sunny", sunny);
		

		return modelAndView;
	}
	
	
	
	@RequestMapping( value = "/super/site_inactive_users", method = RequestMethod.GET )
	public ModelAndView siteInactiveUsers( ){
		ModelAndView modelAndView = new ModelAndView();
		List<SiteInactiveUser> siteInactiveUsers = siteInactiveUserRepository.getAll(null);
		
		modelAndView.addObject("siteInactiveUsers", siteInactiveUsers);

		modelAndView.setViewName("/super/site_inactive_users");
		return modelAndView;
	}
	

	@RequestMapping( value = "/super/send_email", method = RequestMethod.GET )
	public ModelAndView sendEmail( 
			@RequestParam(value="q", required=false) String queryName,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) Boolean isPagelet){
		ModelAndView modelAndView = new ModelAndView();
		
		Page<MailingQueue> pagedResult = mailingQueueRepository.getPagedList(queryName, page, 15);

		modelAndView.addObject("pagedResult", pagedResult);

		List<Site> sites = siteRepository.getAll(null);
		modelAndView.addObject("sites", sites);
		
		if( isPagelet != null && isPagelet == true ){
			modelAndView.setViewName("/super/pagelet/emails");
		}else{
			modelAndView.setViewName("/super/send_email");	
		}
		
		return modelAndView;
	}


	@RequestMapping( value = "/super/send_email", method = RequestMethod.POST )
	public ModelAndView removeSite( @ModelAttribute MailingQueue mailingQueue,
			@AuthUser SecurityUser securityUser, @RequestHeader(value = "referer", required = false) final String referer){
		ModelAndView modelAndView = new ModelAndView();
		mailingQueue.setUser(new User(securityUser.getUserId()));
		mailingQueueRepository.save(mailingQueue);
		modelAndView.setViewName("redirect:" + referer);
		return modelAndView;
	}
	

	@RequestMapping( value = "/super/delete_email", method = RequestMethod.POST )
	public ModelAndView removeSite( @RequestParam(value="id") Long id,
			@AuthUser SecurityUser securityUser, @RequestHeader(value = "referer", required = false) final String referer){
		ModelAndView modelAndView = new ModelAndView();
		if( id != null )
			mailingQueueRepository.delete(id);
		modelAndView.setViewName("redirect:" + referer);
		return modelAndView;
	}
	
	
//	@RequestMapping( value = "/super/remove_menu", method = RequestMethod.POST )
//	public ModelAndView removeSite( @RequestParam("id") Integer menuId, @RequestParam("sunny") String sunnyPassword,
//			@RequestHeader(value = "referer", required = false) final String referer){
//		ModelAndView modelAndView = new ModelAndView();
//		
//		if( sunnyPassword == null || !sunnyPassword.equals("sunnyvale") ){
//			throw new SimpleSunnyException();
//		}
//		//adminService.removeSiteFromAdminId(userId);
//		menuService.remove( menuId );
//		modelAndView.setViewName("redirect:" + referer);
//		return modelAndView;
//	}
//	

	
	@RequestMapping( value = "/super/remove_site", method = RequestMethod.POST )
	public ModelAndView removeSite( @RequestParam("id") Long siteId, @RequestParam("sunny") String sunnyPassword,
			@RequestHeader(value = "referer", required = false) final String referer){
		ModelAndView modelAndView = new ModelAndView();
		
		if( sunnyPassword == null || !sunnyPassword.equals("sunnyvale") ){
			throw new SimpleSunnyException();
		}
		
		//adminService.removeSiteFromAdminId(userId);
		adminService.removeSite(siteId);
		modelAndView.setViewName("redirect:" + referer);
		return modelAndView;
	}
	
	@RequestMapping( value = "/super/remove_user", method = RequestMethod.POST )
	public ModelAndView removeUser( @RequestParam("id") Long userId, @RequestParam("sunny") String sunnyPassword,
			@RequestHeader(value = "referer", required = false) final String referer){
		ModelAndView modelAndView = new ModelAndView();
		
		if( sunnyPassword == null || !sunnyPassword.equals("sunnyvale") ){
			throw new SimpleSunnyException();
		}
		
		//adminService.removeSiteFromAdminId(userId);
		userService.deleteUser(userId, true);
		modelAndView.setViewName("redirect:" + referer);
		return modelAndView;
	}
	
	
	@RequestMapping( value = "/super/remove_inactive_user", method = RequestMethod.POST )
	public ModelAndView removeInactiveUser( @RequestParam("id") Long siteInactiveUserId,
			@RequestHeader(value = "referer", required = false) final String referer){
		ModelAndView modelAndView = new ModelAndView();
		
		//adminService.removeSiteFromAdminId(userId);
		//adminService.removeSite(siteId);
		siteInactiveUserService.delete(siteInactiveUserId);
		modelAndView.setViewName("redirect:" + referer);
		return modelAndView;
	}
	
	
	@RequestMapping( value = "/super/confirm_inactive_user", method = RequestMethod.POST )
	public ModelAndView removeSite( @RequestParam("id") Long siteInactiveUserId,
			@RequestHeader(value = "referer", required = false) final String referer){

		ModelAndView modelAndView = new ModelAndView();
		
		SiteInactiveUser siteInactiveUser = siteInactiveUserService.findById(siteInactiveUserId);
		
		siteInactiveUser.setPassword("1234");
		
		System.out.println("고고고");
		siteService.signupComplete( siteInactiveUser );
		
		modelAndView.setViewName("redirect:" + referer);
		return modelAndView;
	}
	
	
	
	@RequestMapping( value = "/super/story", method = RequestMethod.GET )
	public ModelAndView superStory(
			@ParseSunny(shouldExistsSite = true) Sunny sunny,
			@AuthUser SecurityUser securityUser  ){

		ModelAndView modelAndView = new ModelAndView();
		Stream stream = new Stream(null, null, null, 10);

		User user =  userService.findById( securityUser.getUserId() );
		List<StoryDTO> superStories = storyService.fetchSuperStories(sunny, user, stream);

		modelAndView.addObject("stories",superStories);
		modelAndView.setViewName("/super/story");
		return modelAndView;
	}

	@RequestMapping( value = "/super/tags", method = RequestMethod.GET )
	public ModelAndView superTags(
			@ParseSunny(shouldExistsSite = true) Sunny sunny,
			@AuthUser SecurityUser securityUser  ){

		ModelAndView modelAndView = new ModelAndView();

		List<DefaultTag> tags = defaultTagRepository.getAll(null);

		modelAndView.addObject("tags",tags);
		modelAndView.setViewName("/super/tags");
		return modelAndView;
	}
	

	
	@RequestMapping( value = "/super/sync_tag", method = RequestMethod.GET )
	public ModelAndView generateDefaultTags( ){
		ModelAndView modelAndView = new ModelAndView();
		adminService.syncTags();
		modelAndView.setViewName("redirect:/super/tags");
		return modelAndView;
	}

	@RequestMapping( value = "/super/update_tag", method = RequestMethod.POST )
	public ModelAndView updateTag( @ModelAttribute DefaultTag tag,
			@AuthUser SecurityUser securityUser, @RequestHeader(value = "referer", required = false) final String referer){
		ModelAndView modelAndView = new ModelAndView();
		defaultTagRepository.update(tag);
		modelAndView.setViewName("redirect:" + referer);
		return modelAndView;
	}

	@RequestMapping( value = "/super/create_tag", method = RequestMethod.POST )
	public ModelAndView saveTag( @ModelAttribute DefaultTag tag,
			@AuthUser SecurityUser securityUser, @RequestHeader(value = "referer", required = false) final String referer){
		ModelAndView modelAndView = new ModelAndView();
		defaultTagRepository.save( tag );
		modelAndView.setViewName("redirect:" + referer);
		return modelAndView;
	}
	
}
