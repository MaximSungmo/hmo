package kr.co.sunnyvale.sunny.controller.web;


import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.sunnyvale.sunny.annotation.AuthUser;
import kr.co.sunnyvale.sunny.annotation.ParseSunny;
import kr.co.sunnyvale.sunny.domain.Approval;
import kr.co.sunnyvale.sunny.domain.Content;
import kr.co.sunnyvale.sunny.domain.Draft;
import kr.co.sunnyvale.sunny.domain.Note;
import kr.co.sunnyvale.sunny.domain.Site;
import kr.co.sunnyvale.sunny.domain.SmallGroup;
import kr.co.sunnyvale.sunny.domain.SmallGroupApproval;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.dto.ContentDTO;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.repository.hibernate.ContentService;
import kr.co.sunnyvale.sunny.repository.hibernate.UserSmallGroupAccessRepository;
import kr.co.sunnyvale.sunny.service.ApprovalService;
import kr.co.sunnyvale.sunny.service.DraftService;
import kr.co.sunnyvale.sunny.service.LastReadService;
import kr.co.sunnyvale.sunny.service.MediaService;
import kr.co.sunnyvale.sunny.service.NoteService;
import kr.co.sunnyvale.sunny.service.SiteService;
import kr.co.sunnyvale.sunny.service.SmallGroupApprovalService;
import kr.co.sunnyvale.sunny.service.SmallGroupService;
import kr.co.sunnyvale.sunny.service.TagService;
import kr.co.sunnyvale.sunny.service.TemplateService;
import kr.co.sunnyvale.sunny.service.UserService;
import kr.co.sunnyvale.sunny.service.impl.OperationService;
import kr.co.sunnyvale.sunny.util.LoginUtils;
import kr.co.sunnyvale.sunny.util.SearchUtils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

/**
 * {SpringSecurity}
 * 
 * @author kickscar
 *
 */
@Controller
public class ApprovalController {
	
	@Autowired
	private NoteService noteService;
	
	@Autowired
	private SmallGroupApprovalService smallGroupApprovalService;

	@Autowired
	private SiteService siteService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DraftService draftService;

	@Autowired
	private ContentService contentService;
	
	@Autowired
	private OperationService operationService;
	
	@Autowired
	private TagService tagService;

	@Autowired
	private SmallGroupService smallGroupService;
	
	@Autowired
	private LastReadService lastReadService;
	
	@Autowired
	private ApprovalService approvalService;
	
	@Autowired
	private TemplateService templateService;
	
	@Autowired
	private UserSmallGroupAccessRepository userSmallGroupAccessRepository;
	
	@Autowired
	private MediaService mediaService; 
	
	private Logger logger = Logger.getLogger(this.getClass());

	/*
	 * ********************************************************
	 * MultiSite
	 * *********************************************************
	 */
	
	@RequestMapping( value = "/{path}/approval", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
	public ModelAndView index( 
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny, 
			@AuthUser SecurityUser securityUser, 
			@RequestParam(value="menu", required=false) String menu,
			@RequestParam(value="q[]", required=false) String[] queries,
			@RequestParam(value="nq", required=false) String newQuery,
			@RequestParam(value="recursive", required=false) Boolean isRecursive,
			@RequestParam(value="last", required=false) String lastDateString  ) {
		
		
		if( sunny.isNoExistsSite() == true ){
			throw new AccessDeniedException("Access Denied");
		}
		ModelAndView modelAndView = new ModelAndView();
		
		if( sunny.isGoRedirect() == true ){
			System.out.println("redirect path : " + sunny.getRedirectPath());
			modelAndView.setViewName("redirect:" + sunny.getPath());
			return modelAndView;
		}
		User user = null;
		
		if( securityUser != null ){

			// 계정 비활성 사용자 ( Access Denied )
			if( securityUser.hasRole("ROLE_USER") != true ){
				modelAndView.setViewName("/error/403");
				return modelAndView;
			}

			user = userService.findById( securityUser.getUserId() );
		}

		Stream stream = new Stream(null, null, null, 10);
		if( queries != null ){
			queries = SearchUtils.checkAndFixQuery( queries, newQuery, isRecursive );
		}
		
		List<ContentDTO> stories = contentService.fetchApprovals(sunny, menu, null, false, user, queries, stream);
		
		modelAndView.addObject("stories",stories);
	
		modelAndView.addObject("queries",  queries);

		modelAndView.setViewName("/approval/index");
		
		return modelAndView;
	}

	@RequestMapping( value = "/approval", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
	public ModelAndView index(
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestParam(value="menu", required=false) String menu,
			@RequestParam(value="q[]", required=false) String[] queries,
			@RequestParam(value="nq", required=false) String newQuery,
			@RequestParam(value="recursive", required=false) Boolean isRecursive,
			@RequestParam(value="last", required=false) String lastDateString ) {

		return index(null, sunny, securityUser, menu, queries, newQuery, isRecursive, lastDateString);
	}
	
//	
//	@RequestMapping( value = "/{path}/approval/sent_process", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
//	public ModelAndView sentProcess( 
//			@PathVariable("path") String path,
//			@ParseSunny(shouldExistsSite=true) Sunny sunny, 
//			@AuthUser SecurityUser securityUser, 
//			@RequestParam(value="q[]", required=false) String[] queries,
//			@RequestParam(value="nq", required=false) String newQuery,
//			@RequestParam(value="recursive", required=false) Boolean isRecursive,
//			@RequestParam(value="last", required=false) String lastDateString  ) {
//		
//		
//		if( sunny.isNoExistsSite() == true ){
//			throw new AccessDeniedException("Access Denied");
//		}
//		ModelAndView modelAndView = new ModelAndView();
//		
//		if( sunny.isGoRedirect() == true ){
//			System.out.println("redirect path : " + sunny.getRedirectPath());
//			modelAndView.setViewName("redirect:" + sunny.getPath());
//			return modelAndView;
//		}
//		User user = null;
//		
//		if( securityUser != null ){
//
//			// 계정 비활성 사용자 ( Access Denied )
//			if( securityUser.hasRole("ROLE_USER") != true ){
//				modelAndView.setViewName("/error/403");
//				return modelAndView;
//			}
//
//			user = userService.findById( securityUser.getUserId() );
//		}
//
//		Stream stream = new Stream(null, null, null, 10);
//		if( queries != null ){
//			queries = SearchUtils.checkAndFixQuery( queries, newQuery, isRecursive );
//		}
//		
//		List<ContentDTO> stories = contentService.fetchApprovals(sunny, null, false, Approval.STATUS_SENT, user, null, user, null, queries, stream);
//		
//		modelAndView.addObject("stories",stories);
//	
//		modelAndView.addObject("queries",  queries);
//
//		modelAndView.setViewName("/approval/sent_process");
//		
//		return modelAndView;
//	}
//
//	@RequestMapping( value = "/approval/sent_process", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
//	public ModelAndView sentProcess(
//			@ParseSunny(shouldExistsSite=true) Sunny sunny,
//			@AuthUser SecurityUser securityUser, 
//			@RequestParam(value="q[]", required=false) String[] queries,
//			@RequestParam(value="nq", required=false) String newQuery,
//			@RequestParam(value="recursive", required=false) Boolean isRecursive,
//			@RequestParam(value="last", required=false) String lastDateString ) {
//
//		return sentProcess(null, sunny, securityUser, queries, newQuery, isRecursive, lastDateString);
//	}
//	
//	
//	@RequestMapping( value = "/{path}/approval/receive_process", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
//	public ModelAndView receiveProcess( 
//			@PathVariable("path") String path,
//			@ParseSunny(shouldExistsSite=true) Sunny sunny, 
//			@AuthUser SecurityUser securityUser, 
//			@RequestParam(value="q[]", required=false) String[] queries,
//			@RequestParam(value="nq", required=false) String newQuery,
//			@RequestParam(value="recursive", required=false) Boolean isRecursive,
//			@RequestParam(value="last", required=false) String lastDateString  ) {
//		
//		
//		if( sunny.isNoExistsSite() == true ){
//			throw new AccessDeniedException("Access Denied");
//		}
//		ModelAndView modelAndView = new ModelAndView();
//		
//		if( sunny.isGoRedirect() == true ){
//			System.out.println("redirect path : " + sunny.getRedirectPath());
//			modelAndView.setViewName("redirect:" + sunny.getPath());
//			return modelAndView;
//		}
//		User user = null;
//		
//		if( securityUser != null ){
//
//			// 계정 비활성 사용자 ( Access Denied )
//			if( securityUser.hasRole("ROLE_USER") != true ){
//				modelAndView.setViewName("/error/403");
//				return modelAndView;
//			}
//
//			user = userService.findById( securityUser.getUserId() );
//		}
//
//		Stream stream = new Stream(null, null, null, 10);
//		if( queries != null ){
//			queries = SearchUtils.checkAndFixQuery( queries, newQuery, isRecursive );
//		}
//
//		List<ContentDTO> stories = contentService.fetchApprovals(sunny, null, false, Approval.STATUS_SENT, null, user, user, null, queries, stream);
//		
//		modelAndView.addObject("stories",stories);
//	
//		modelAndView.addObject("queries",  queries);
//
//		modelAndView.setViewName("/approval/receive_process");
//		
//		return modelAndView;
//	}
//
//	@RequestMapping( value = "/approval/receive_process", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
//	public ModelAndView receiveProcess(
//			@ParseSunny(shouldExistsSite=true) Sunny sunny,
//			@AuthUser SecurityUser securityUser, 
//			@RequestParam(value="q[]", required=false) String[] queries,
//			@RequestParam(value="nq", required=false) String newQuery,
//			@RequestParam(value="recursive", required=false) Boolean isRecursive,
//			@RequestParam(value="last", required=false) String lastDateString ) {
//
//		return receiveProcess(null, sunny, securityUser, queries, newQuery, isRecursive, lastDateString);
//	}
//	
//	@RequestMapping( value = "/{path}/approval/approved", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
//	public ModelAndView approved( 
//			@PathVariable("path") String path,
//			@ParseSunny(shouldExistsSite=true) Sunny sunny, 
//			@AuthUser SecurityUser securityUser, 
//			@RequestParam(value="q[]", required=false) String[] queries,
//			@RequestParam(value="nq", required=false) String newQuery,
//			@RequestParam(value="recursive", required=false) Boolean isRecursive,
//			@RequestParam(value="last", required=false) String lastDateString  ) {
//		
//		
//		if( sunny.isNoExistsSite() == true ){
//			throw new AccessDeniedException("Access Denied");
//		}
//		ModelAndView modelAndView = new ModelAndView();
//		
//		if( sunny.isGoRedirect() == true ){
//			System.out.println("redirect path : " + sunny.getRedirectPath());
//			modelAndView.setViewName("redirect:" + sunny.getPath());
//			return modelAndView;
//		}
//		User user = null;
//		
//		if( securityUser != null ){
//
//			// 계정 비활성 사용자 ( Access Denied )
//			if( securityUser.hasRole("ROLE_USER") != true ){
//				modelAndView.setViewName("/error/403");
//				return modelAndView;
//			}
//
//			user = userService.findById( securityUser.getUserId() );
//		}
//
//		Stream stream = new Stream(null, null, null, 10);
//		if( queries != null ){
//			queries = SearchUtils.checkAndFixQuery( queries, newQuery, isRecursive );
//		}
//
//		List<ContentDTO> stories = contentService.fetchApprovals(sunny, null, false, Approval.STATUS_APPROVED, null, null, user, null, queries, stream);
//		
//		modelAndView.addObject("stories",stories);
//	
//		modelAndView.addObject("queries",  queries);
//
//		modelAndView.setViewName("/approval/approved");
//		
//		return modelAndView;
//	}
//
//	@RequestMapping( value = "/approval/approved", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
//	public ModelAndView approved(
//			@ParseSunny(shouldExistsSite=true) Sunny sunny,
//			@AuthUser SecurityUser securityUser, 
//			@RequestParam(value="q[]", required=false) String[] queries,
//			@RequestParam(value="nq", required=false) String newQuery,
//			@RequestParam(value="recursive", required=false) Boolean isRecursive,
//			@RequestParam(value="last", required=false) String lastDateString ) {
//
//		return approved(null, sunny, securityUser, queries, newQuery, isRecursive, lastDateString);
//	}
//	
//	@RequestMapping( value = "/{path}/approval/sent_approved", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
//	public ModelAndView sentApproved( 
//			@PathVariable("path") String path,
//			@ParseSunny(shouldExistsSite=true) Sunny sunny, 
//			@AuthUser SecurityUser securityUser, 
//			@RequestParam(value="q[]", required=false) String[] queries,
//			@RequestParam(value="nq", required=false) String newQuery,
//			@RequestParam(value="recursive", required=false) Boolean isRecursive,
//			@RequestParam(value="last", required=false) String lastDateString  ) {
//		
//		
//		if( sunny.isNoExistsSite() == true ){
//			throw new AccessDeniedException("Access Denied");
//		}
//		ModelAndView modelAndView = new ModelAndView();
//		
//		if( sunny.isGoRedirect() == true ){
//			System.out.println("redirect path : " + sunny.getRedirectPath());
//			modelAndView.setViewName("redirect:" + sunny.getPath());
//			return modelAndView;
//		}
//		User user = null;
//		
//		if( securityUser != null ){
//
//			// 계정 비활성 사용자 ( Access Denied )
//			if( securityUser.hasRole("ROLE_USER") != true ){
//				modelAndView.setViewName("/error/403");
//				return modelAndView;
//			}
//
//			user = userService.findById( securityUser.getUserId() );
//		}
//
//		Stream stream = new Stream(null, null, null, 10);
//		if( queries != null ){
//			queries = SearchUtils.checkAndFixQuery( queries, newQuery, isRecursive );
//		}
//
//		List<ContentDTO> stories = contentService.fetchApprovals(sunny, null, false, Approval.STATUS_APPROVED, user, null, user, null, queries, stream);
//		
//		modelAndView.addObject("stories",stories);
//	
//		modelAndView.addObject("queries",  queries);
//
//		modelAndView.setViewName("/approval/sent_approved");
//		
//		return modelAndView;
//	}
//
//	@RequestMapping( value = "/approval/sent_approved", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
//	public ModelAndView sentApproved(
//			@ParseSunny(shouldExistsSite=true) Sunny sunny,
//			@AuthUser SecurityUser securityUser, 
//			@RequestParam(value="q[]", required=false) String[] queries,
//			@RequestParam(value="nq", required=false) String newQuery,
//			@RequestParam(value="recursive", required=false) Boolean isRecursive,
//			@RequestParam(value="last", required=false) String lastDateString ) {
//
//		return sentApproved(null, sunny, securityUser, queries, newQuery, isRecursive, lastDateString);
//	}
//	
//	@RequestMapping( value = "/{path}/approval/receive_approved", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
//	public ModelAndView receiveApproved( 
//			@PathVariable("path") String path,
//			@ParseSunny(shouldExistsSite=true) Sunny sunny, 
//			@AuthUser SecurityUser securityUser, 
//			@RequestParam(value="q[]", required=false) String[] queries,
//			@RequestParam(value="nq", required=false) String newQuery,
//			@RequestParam(value="recursive", required=false) Boolean isRecursive,
//			@RequestParam(value="last", required=false) String lastDateString  ) {
//		
//		
//		if( sunny.isNoExistsSite() == true ){
//			throw new AccessDeniedException("Access Denied");
//		}
//		ModelAndView modelAndView = new ModelAndView();
//		
//		if( sunny.isGoRedirect() == true ){
//			System.out.println("redirect path : " + sunny.getRedirectPath());
//			modelAndView.setViewName("redirect:" + sunny.getPath());
//			return modelAndView;
//		}
//		User user = null;
//		
//		if( securityUser != null ){
//
//			// 계정 비활성 사용자 ( Access Denied )
//			if( securityUser.hasRole("ROLE_USER") != true ){
//				modelAndView.setViewName("/error/403");
//				return modelAndView;
//			}
//
//			user = userService.findById( securityUser.getUserId() );
//		}
//
//		Stream stream = new Stream(null, null, null, 10);
//		if( queries != null ){
//			queries = SearchUtils.checkAndFixQuery( queries, newQuery, isRecursive );
//		}
//
//		List<ContentDTO> stories = contentService.fetchApprovals(sunny, null, false, Approval.STATUS_APPROVED, null, user, user, null, queries, stream);
//		
//		modelAndView.addObject("stories",stories);
//	
//		modelAndView.addObject("queries",  queries);
//
//		modelAndView.setViewName("/approval/receive_approved");
//		
//		return modelAndView;
//	}
//
//	@RequestMapping( value = "/approval/receive_approved", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
//	public ModelAndView receiveApproved(
//			@ParseSunny(shouldExistsSite=true) Sunny sunny,
//			@AuthUser SecurityUser securityUser, 
//			@RequestParam(value="q[]", required=false) String[] queries,
//			@RequestParam(value="nq", required=false) String newQuery,
//			@RequestParam(value="recursive", required=false) Boolean isRecursive,
//			@RequestParam(value="last", required=false) String lastDateString ) {
//
//		return receiveApproved(null, sunny, securityUser, queries, newQuery, isRecursive, lastDateString);
//	}
//	
//	
//	@RequestMapping( value = "/{path}/approval/sent_reject", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
//	public ModelAndView sentReject( 
//			@PathVariable("path") String path,
//			@ParseSunny(shouldExistsSite=true) Sunny sunny, 
//			@AuthUser SecurityUser securityUser, 
//			@RequestParam(value="q[]", required=false) String[] queries,
//			@RequestParam(value="nq", required=false) String newQuery,
//			@RequestParam(value="recursive", required=false) Boolean isRecursive,
//			@RequestParam(value="last", required=false) String lastDateString  ) {
//		
//		
//		if( sunny.isNoExistsSite() == true ){
//			throw new AccessDeniedException("Access Denied");
//		}
//		ModelAndView modelAndView = new ModelAndView();
//		
//		if( sunny.isGoRedirect() == true ){
//			System.out.println("redirect path : " + sunny.getRedirectPath());
//			modelAndView.setViewName("redirect:" + sunny.getPath());
//			return modelAndView;
//		}
//		User user = null;
//		
//		if( securityUser != null ){
//
//			// 계정 비활성 사용자 ( Access Denied )
//			if( securityUser.hasRole("ROLE_USER") != true ){
//				modelAndView.setViewName("/error/403");
//				return modelAndView;
//			}
//
//			user = userService.findById( securityUser.getUserId() );
//		}
//
//		Stream stream = new Stream(null, null, null, 10);
//		if( queries != null ){
//			queries = SearchUtils.checkAndFixQuery( queries, newQuery, isRecursive );
//		}
//
//		List<ContentDTO> stories = contentService.fetchApprovals(sunny, null, false, Approval.STATUS_REJECT, user, null, user, null, queries, stream);
//		
//		modelAndView.addObject("stories",stories);
//	
//		modelAndView.addObject("queries",  queries);
//
//		modelAndView.setViewName("/approval/sent_reject");
//		
//		return modelAndView;
//	}
//
//	@RequestMapping( value = "/approval/sent_reject", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
//	public ModelAndView sentReject(
//			@ParseSunny(shouldExistsSite=true) Sunny sunny,
//			@AuthUser SecurityUser securityUser, 
//			@RequestParam(value="q[]", required=false) String[] queries,
//			@RequestParam(value="nq", required=false) String newQuery,
//			@RequestParam(value="recursive", required=false) Boolean isRecursive,
//			@RequestParam(value="last", required=false) String lastDateString ) {
//
//		return sentReject(null, sunny, securityUser, queries, newQuery, isRecursive, lastDateString);
//	}
//	
//	@RequestMapping( value = "/{path}/approval/receive_reject", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
//	public ModelAndView receiveReject( 
//			@PathVariable("path") String path,
//			@ParseSunny(shouldExistsSite=true) Sunny sunny, 
//			@AuthUser SecurityUser securityUser, 
//			@RequestParam(value="q[]", required=false) String[] queries,
//			@RequestParam(value="nq", required=false) String newQuery,
//			@RequestParam(value="recursive", required=false) Boolean isRecursive,
//			@RequestParam(value="last", required=false) String lastDateString  ) {
//		
//		
//		if( sunny.isNoExistsSite() == true ){
//			throw new AccessDeniedException("Access Denied");
//		}
//		ModelAndView modelAndView = new ModelAndView();
//		
//		if( sunny.isGoRedirect() == true ){
//			System.out.println("redirect path : " + sunny.getRedirectPath());
//			modelAndView.setViewName("redirect:" + sunny.getPath());
//			return modelAndView;
//		}
//		User user = null;
//		
//		if( securityUser != null ){
//
//			// 계정 비활성 사용자 ( Access Denied )
//			if( securityUser.hasRole("ROLE_USER") != true ){
//				modelAndView.setViewName("/error/403");
//				return modelAndView;
//			}
//
//			user = userService.findById( securityUser.getUserId() );
//		}
//
//		Stream stream = new Stream(null, null, null, 10);
//		if( queries != null ){
//			queries = SearchUtils.checkAndFixQuery( queries, newQuery, isRecursive );
//		}
//
//		List<ContentDTO> stories = contentService.fetchApprovals(sunny, null, false, Approval.STATUS_REJECT, null, user, user, null, queries, stream);
//		
//		modelAndView.addObject("stories",stories);
//	
//		modelAndView.addObject("queries",  queries);
//
//		modelAndView.setViewName("/approval/receive_reject");
//		
//		return modelAndView;
//	}
//
//	@RequestMapping( value = "/approval/receive_reject", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
//	public ModelAndView receiveReject(
//			@ParseSunny(shouldExistsSite=true) Sunny sunny,
//			@AuthUser SecurityUser securityUser, 
//			@RequestParam(value="q[]", required=false) String[] queries,
//			@RequestParam(value="nq", required=false) String newQuery,
//			@RequestParam(value="recursive", required=false) Boolean isRecursive,
//			@RequestParam(value="last", required=false) String lastDateString ) {
//
//		return receiveReject(null, sunny, securityUser, queries, newQuery, isRecursive, lastDateString);
//	}
//	
//	
//	@RequestMapping( value = "/{path}/approval/receive", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
//	public ModelAndView receive( 
//			@PathVariable("path") String path,
//			@ParseSunny(shouldExistsSite=true) Sunny sunny, 
//			@AuthUser SecurityUser securityUser, 
//			@RequestParam(value="q[]", required=false) String[] queries,
//			@RequestParam(value="nq", required=false) String newQuery,
//			@RequestParam(value="recursive", required=false) Boolean isRecursive,
//			@RequestParam(value="last", required=false) String lastDateString  ) {
//		
//		
//		if( sunny.isNoExistsSite() == true ){
//			throw new AccessDeniedException("Access Denied");
//		}
//		ModelAndView modelAndView = new ModelAndView();
//		
//		if( sunny.isGoRedirect() == true ){
//			System.out.println("redirect path : " + sunny.getRedirectPath());
//			modelAndView.setViewName("redirect:" + sunny.getPath());
//			return modelAndView;
//		}
//		User user = null;
//		
//		if( securityUser != null ){
//
//			// 계정 비활성 사용자 ( Access Denied )
//			if( securityUser.hasRole("ROLE_USER") != true ){
//				modelAndView.setViewName("/error/403");
//				return modelAndView;
//			}
//
//			user = userService.findById( securityUser.getUserId() );
//		}
//
//		Stream stream = new Stream(null, null, null, 10);
//		if( queries != null ){
//			queries = SearchUtils.checkAndFixQuery( queries, newQuery, isRecursive );
//		}
//
//		List<ContentDTO> stories = contentService.fetchApprovals(sunny, null, false, null, null, user, user, SmallGroupApproval.TYPE_RECEIVER, queries, stream);
//		
//		modelAndView.addObject("stories",stories);
//	
//		modelAndView.addObject("queries",  queries);
//
//		modelAndView.setViewName("/approval/receive");
//		
//		return modelAndView;
//	}
//
//	@RequestMapping( value = "/approval/receive", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
//	public ModelAndView receive(
//			@ParseSunny(shouldExistsSite=true) Sunny sunny,
//			@AuthUser SecurityUser securityUser, 
//			@RequestParam(value="q[]", required=false) String[] queries,
//			@RequestParam(value="nq", required=false) String newQuery,
//			@RequestParam(value="recursive", required=false) Boolean isRecursive,
//			@RequestParam(value="last", required=false) String lastDateString ) {
//
//		return receive(null, sunny, securityUser, queries, newQuery, isRecursive, lastDateString);
//	}
//	
//	@RequestMapping( value = "/{path}/approval/circulation", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
//	public ModelAndView circulation( 
//			@PathVariable("path") String path,
//			@ParseSunny(shouldExistsSite=true) Sunny sunny, 
//			@AuthUser SecurityUser securityUser, 
//			@RequestParam(value="q[]", required=false) String[] queries,
//			@RequestParam(value="nq", required=false) String newQuery,
//			@RequestParam(value="recursive", required=false) Boolean isRecursive,
//			@RequestParam(value="last", required=false) String lastDateString  ) {
//		
//		
//		if( sunny.isNoExistsSite() == true ){
//			throw new AccessDeniedException("Access Denied");
//		}
//		ModelAndView modelAndView = new ModelAndView();
//		
//		if( sunny.isGoRedirect() == true ){
//			System.out.println("redirect path : " + sunny.getRedirectPath());
//			modelAndView.setViewName("redirect:" + sunny.getPath());
//			return modelAndView;
//		}
//		User user = null;
//		
//		if( securityUser != null ){
//
//			// 계정 비활성 사용자 ( Access Denied )
//			if( securityUser.hasRole("ROLE_USER") != true ){
//				modelAndView.setViewName("/error/403");
//				return modelAndView;
//			}
//
//			user = userService.findById( securityUser.getUserId() );
//		}
//
//		Stream stream = new Stream(null, null, null, 10);
//		if( queries != null ){
//			queries = SearchUtils.checkAndFixQuery( queries, newQuery, isRecursive );
//		}
//
//		
//
//		
//		
//		
//		List<ContentDTO> stories = contentService.fetchApprovals(sunny, null, false, null, null, user, user, SmallGroupApproval.TYPE_CIRCULATION, queries, stream);
//		
//		modelAndView.addObject("stories",stories);
//	
//		modelAndView.addObject("queries",  queries);
//
//		modelAndView.setViewName("/approval/circulation");
//		
//		return modelAndView;
//	}
//
//	@RequestMapping( value = "/approval/circulation", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
//	public ModelAndView circulation(
//			@ParseSunny(shouldExistsSite=true) Sunny sunny,
//			@AuthUser SecurityUser securityUser, 
//			@RequestParam(value="q[]", required=false) String[] queries,
//			@RequestParam(value="nq", required=false) String newQuery,
//			@RequestParam(value="recursive", required=false) Boolean isRecursive,
//			@RequestParam(value="last", required=false) String lastDateString ) {
//
//		return circulation(null, sunny, securityUser, queries, newQuery, isRecursive, lastDateString);
//	}
	@RequestMapping( value = "/{path}/approval/{id}", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
	public ModelAndView view(
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@PathVariable( "id" ) Long id,			
			@RequestParam(value="q[]", required=false) String[] queries,	
			@RequestParam(value="sgid", required=false) Long smallGroupId,
			@RequestParam(value="uid", required=false) Long userId,
			@RequestParam(value="menu", required=false) String menu,
			@RequestParam(value="tagTitle", required=false) String tagTitle,
			HttpServletResponse response,
			HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView();
		
		LoginUtils.checkLogin(securityUser);
		
		User user = userService.findById( securityUser.getUserId() );

		Approval approval = approvalService.findForView(id, user);
		operationService.addViewCount( approval, user);
		modelAndView.addObject("content", approval);
		
		Approval prevApproval = approvalService.getSequence( sunny, user, -1, approval, queries, smallGroupId, userId, menu, tagTitle);

		Approval nextApproval = approvalService.getSequence( sunny, user, 1, approval, queries, smallGroupId, userId, menu, tagTitle);

		modelAndView.addObject("prevApproval", prevApproval);
		modelAndView.addObject("nextApproval", nextApproval);
		

	
		
		SmallGroupApproval smallGroupApproval = smallGroupApprovalService.find(user.getId(), id, null);
		modelAndView.addObject("smallGroupApproval", smallGroupApproval);
		modelAndView.addObject("mediaes", mediaService.getMedies(id, null));
		
		modelAndView.setViewName("/approval/view");

		return modelAndView;
	}
	@RequestMapping( value = "/approval/{id}", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
	public ModelAndView view( 
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@PathVariable( "id" ) Long id,			
			@RequestParam(value="q[]", required=false) String[] queries,	
			@RequestParam(value="sgid", required=false) Long smallGroupId,
			@RequestParam(value="uid", required=false) Long userId,
			@RequestParam(value="menu", required=false) String menu,
			@RequestParam(value="tagTitle", required=false) String tagTitle,
			HttpServletResponse response,
			HttpServletRequest request) {

		return view(null, sunny, securityUser, id, queries, smallGroupId, userId, menu, tagTitle, response, request); 
				
	}
	

	
//	@RequestMapping( value = "/{path}/approval/preview/{id}", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
//	public ModelAndView preview( 
//			@PathVariable("path") String path, 
//			@ParseSunny(shouldExistsSite=true) Sunny sunny,
//			@AuthUser SecurityUser securityUser, 
//			@PathVariable( "id" ) Long id,							
//			HttpServletResponse response,
//			HttpServletRequest request) {
//
//		ModelAndView modelAndView = new ModelAndView();
//		
//	
//		modelAndView.addObject("draft", draftService.select(id) );
//		modelAndView.setViewName("/note/preview");
//		return modelAndView;
//	}
//
//	@RequestMapping( value = "/approval/preview/{id}", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
//	public ModelAndView preview( 
//			@ParseSunny(shouldExistsSite=true) Sunny sunny,
//			@AuthUser SecurityUser securityUser, 
//			@PathVariable( "id" ) Long id,							
//			HttpServletResponse response,
//			HttpServletRequest request) {
//
//		return preview(
//				null, 
//				sunny,
//				securityUser,
//				id,response,request);
//	}
	
//	@RequestMapping( value = "/{path}/approval/delete", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
//	public ModelAndView delete( 
//			@PathVariable("path") String path, 
//			@ParseSunny(shouldExistsSite=true) Sunny sunny,
//			@AuthUser SecurityUser securityUser, 
//			@RequestParam( value="id", required=true )Long id,
//			@RequestHeader(value = "referer", required = false) final String referer){
//
//		ModelAndView modelAndView = new ModelAndView();
//		LoginUtils.checkLogin(securityUser);
//		
//		boolean isSameUser = contentService.isSameUser( id, securityUser.getUserId() );
//		
//		if( isSameUser == false ){
//			logger.error("다른 질문 유저로부터 삭제 시도");
//			modelAndView.setViewName("redirect:/error/exception");
//			return modelAndView;
//		}
//		approvalService.delete(id);
//		modelAndView.setViewName("redirect:/note");
//
//		return modelAndView;
//		
//	}

//	@RequestMapping( value = "/note/delete", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
//	public ModelAndView delete( 
//			@ParseSunny(shouldExistsSite=true) Sunny sunny,
//			@AuthUser SecurityUser securityUser, 
//			@RequestParam( value="id", required=true )Long id,
//			@RequestHeader(value = "referer", required = false) final String referer){
//
//		return delete( 
//				null,
//				sunny,
//				securityUser,
//				id,referer);
//	}
	
	
	@RequestMapping( value = "/{path}/approval/drafts", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
	public ModelAndView write( 
			@PathVariable("path") String path, 
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) Boolean isPagelet ){

		ModelAndView modelAndView = new ModelAndView();		
		Page<Draft> pagedResult = draftService.getPagedList( sunny, null, new User(securityUser.getUserId()), Content.TYPE_APPROVAL,  page, Page.DEFAULT_PAGE_SIZE);
		
		modelAndView.addObject("pagedResult", pagedResult);
		modelAndView.setViewName("/approval/drafts");
		return modelAndView;
	}
	

	@RequestMapping( value = "/approval/drafts", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
	public ModelAndView write( 
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) Boolean isPagelet ){

		return write(
				null,
				sunny,
				securityUser,
				page,
				isPagelet);
	}
	

	
	@RequestMapping( value = "/{path}/approval/write", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
	public ModelAndView writeNext( 
			@PathVariable("path") String path, 
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestParam( value = "id", required=false) Long id){

		ModelAndView modelAndView = new ModelAndView();
		
		Draft draft = draftService.select(id);
		modelAndView.addObject("draft", draft); 
		Site site = sunny.getSite();
		
		modelAndView.addObject("templates", templateService.getTemplates(sunny, null));
		
//		if( !draft.getSmallGroup().getId().equals( site.getLobbySmallGroup().getId() )){
//			modelAndView.addObject("smallGroup", draft.getSmallGroup() );
//			modelAndView.setViewName("/group/note/write");
//		}else{
			modelAndView.setViewName("/approval/write");
//		}
		return modelAndView;
	}
	

	@RequestMapping( value = "/approval/write", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
	public ModelAndView writeNext( 
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestParam( value = "id", required=false) Long id){

		return writeNext(
				null,
				sunny,
				securityUser,
				id);
	}
}
