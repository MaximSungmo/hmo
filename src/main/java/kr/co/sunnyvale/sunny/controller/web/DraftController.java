package kr.co.sunnyvale.sunny.controller.web;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.sunnyvale.sunny.annotation.AuthUser;
import kr.co.sunnyvale.sunny.annotation.ParseSunny;
import kr.co.sunnyvale.sunny.domain.Content;
import kr.co.sunnyvale.sunny.domain.Draft;
import kr.co.sunnyvale.sunny.domain.Site;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.service.DraftService;
import kr.co.sunnyvale.sunny.service.SiteService;
import kr.co.sunnyvale.sunny.util.LoginUtils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * {SpringSecurity}
 * 
 * @author kickscar
 *
 */
@Controller
public class DraftController {
	
	@Autowired
	private DraftService draftService;
	
	@Autowired
	private SiteService siteService;
	
	private Logger logger = Logger.getLogger(this.getClass());



	
	/*
	 * ********************************************************
	 * MultiSite
	 * *********************************************************
	 */
	@RequestMapping( value = "/{path}/draft/preview/{id}", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
	public ModelAndView preview( 
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@PathVariable( "id" ) Long id,							
			HttpServletResponse response,
			HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView();
		return modelAndView;
	}
	
	@RequestMapping( value = "/{path}/draft/delete", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
	public ModelAndView delete(  
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestParam( value="id", required=true )Long id,
			@RequestHeader(value = "referer", required = false) final String referer){

		LoginUtils.checkLogin(securityUser);
		
		ModelAndView modelAndView = new ModelAndView();
		
		boolean isSameUser = draftService.isSameUser( id, securityUser.getUserId() );
		
		if( isSameUser == false ){
			logger.error("다른 질문 유저로부터 삭제 시도");
			modelAndView.setViewName("redirect:/error/exception");
			return modelAndView;
		}
		draftService.delete(id);
		modelAndView.setViewName("redirect:/");

		return modelAndView;
		
	}
	

	@RequestMapping( value = "/{path}/draft/create/note", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
	public ModelAndView tmpCreate( 
			@PathVariable("path") String path, 
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			Long pushContentId , 
			Long targetContentId, 
			@RequestParam(value="gid", required=false) Long groupId ){

		LoginUtils.checkLogin(securityUser);
		ModelAndView modelAndView = new ModelAndView();

		Site site = sunny.getSite();
		Draft draft = draftService.createDefault( site, Content.TYPE_NOTE, pushContentId, targetContentId, securityUser.getUserId(), groupId );
		
		String redirectUrl = "/note/write?id=" + draft.getId();
		if( groupId != null ){
			redirectUrl = "/group/" + groupId + redirectUrl;
		}
		
		if( path != null ){
			redirectUrl = "/" + path + redirectUrl;
		}
		System.out.println("리턴 URL = " + redirectUrl);
		modelAndView.setViewName("redirect:" + redirectUrl);
		return modelAndView;
	}

	
	@RequestMapping( value = "/draft/create/note", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
	public ModelAndView tmpCreate( 
			@AuthUser SecurityUser securityUser, 
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			Long pushContentId , 
			Long targetContentId, 
			@RequestParam(value="gid", required=false) Long groupId ){

		return tmpCreate( 
				null,
				sunny,
				securityUser, 
				pushContentId,							
				targetContentId,
				groupId );
	}
	
	
	@RequestMapping( value = "/{path}/draft/create/approval", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
	public ModelAndView createApprovalDraft( 
			@PathVariable("path") String path, 
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser ){

		LoginUtils.checkLogin(securityUser);
		ModelAndView modelAndView = new ModelAndView();

		Site site = sunny.getSite();
		Draft draft = draftService.createDefault( site, Content.TYPE_APPROVAL, null, null, securityUser.getUserId(), null );
		
		String redirectUrl = "/approval/write?id=" + draft.getId();
		
		if( path != null ){
			redirectUrl = "/" + path + redirectUrl;
		}
		modelAndView.setViewName("redirect:" + redirectUrl);
		return modelAndView;
	}

	
	@RequestMapping( value = "/draft/create/approval", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
	public ModelAndView createApprovalDraft( 
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser ){

		return createApprovalDraft(null, sunny, securityUser);
	}
	
	/*
	 * MultiSiteProxy
	 */
	@RequestMapping( value = "/draft/preview/{id}", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
	public ModelAndView preview( 
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
							@PathVariable( "id" ) Long id,							
							HttpServletResponse response,
							HttpServletRequest request) {


		return preview( null, sunny, securityUser, 
				id,							
				response,
				request );
	}
	
	@RequestMapping( value = "/draft/delete", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
	public ModelAndView delete( 
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestParam( value="id", required=true )Long id,
			@RequestHeader(value = "referer", required = false) final String referer){

		return delete( 
				null, 
				sunny, 
				securityUser, 
				id,							
				referer );
		
	}
	

}
