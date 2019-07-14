package kr.co.sunnyvale.sunny.controller.api;

import javax.servlet.http.HttpServletRequest;

import kr.co.sunnyvale.sunny.annotation.AuthUser;
import kr.co.sunnyvale.sunny.annotation.ParseSunny;
import kr.co.sunnyvale.sunny.domain.Content;
import kr.co.sunnyvale.sunny.domain.Draft;
import kr.co.sunnyvale.sunny.domain.Site;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.dto.JsonResult;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.service.DraftService;
import kr.co.sunnyvale.sunny.service.SiteService;
import kr.co.sunnyvale.sunny.service.UserService;
import kr.co.sunnyvale.sunny.util.LocaleUtils;
import kr.co.sunnyvale.sunny.util.LoginUtils;
import kr.co.sunnyvale.sunny.util.RequestUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
public class DraftAPIController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DraftService draftService;
	
	@Autowired
	private SiteService siteService;

	@Autowired
	private MessageSource messageSource;
	
	
	@RequestMapping( value = "/draft/delete", method = RequestMethod.GET, headers = { "Accept=application/json" }  )
	@ResponseBody
	public JsonResult delete( 
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, @RequestParam( value="id", required=true )Long id){


		LoginUtils.checkLogin(securityUser);
		
		boolean isSameUser = draftService.isSameUser( id, securityUser.getUserId() );
		
		if( isSameUser == false ){
			return new JsonResult(false,  "fail", null);
		}
		draftService.delete(id);
		return new JsonResult(true,  "success", null);
	}

	@ResponseBody
	@RequestMapping( value="/{path}/draft/{id}", method = RequestMethod.GET, headers = { "Accept=application/json" } )
	public JsonResult getNoteDraft( 
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@PathVariable Long id
			){

		LoginUtils.checkLogin(securityUser);
		
		Draft draft = draftService.select( id );

		return new JsonResult(true,  messageSource.getMessage("draft.successGetDraft", null, LocaleUtils.getLocale()), draft);
	}
	
	@ResponseBody
	@RequestMapping( value="/{path}/draft/target/{id}", method = RequestMethod.GET, headers = { "Accept=application/json" } )
	public JsonResult getTargetDraft( 
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@PathVariable( "id" ) Long targetId
			){


		LoginUtils.checkLogin(securityUser);
		Draft draft = draftService.selectFromTarget( targetId, new User( securityUser.getUserId() ) );

		return new JsonResult(true,  messageSource.getMessage("draft.successGetDraft", null, LocaleUtils.getLocale()), draft);
	}

	@ResponseBody
	@RequestMapping( value = "/{path}/draft/create/note", method = RequestMethod.GET, headers = { "Accept=application/json", "Content-Type=text/javascript" }  )
	public JsonResult tmpCreate( 
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			Long pushContentId, 
			Long targetContentId, 
			@RequestParam(value="gid", required=false) Long groupId ){

		LoginUtils.checkLogin(securityUser);
		Site site = sunny.getSite();
		
		draftService.createDefault( site, Content.TYPE_NOTE, pushContentId, targetContentId, securityUser.getUserId(), groupId );

		return new JsonResult(true,  messageSource.getMessage("draft.successCreateNote", null, LocaleUtils.getLocale()), null);
	}
	
	
	@ResponseBody
	@RequestMapping( value="/{path}/draft/update", method = RequestMethod.POST, headers = { "Accept=application/json" } )
	public JsonResult noteDraft( 
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestBody Draft draft,
			HttpServletRequest request
			
			){

		LoginUtils.checkLogin(securityUser);
		draft.setUser( new User( securityUser.getUserId() ) );
		
		draftService.update(sunny, draft);
		
		return new JsonResult(true,  messageSource.getMessage("draft.successUpdate", null, LocaleUtils.getLocale()), null);
	}
	
	@ResponseBody
	@RequestMapping( value="/{path}/draft/publish", method = RequestMethod.POST, headers = { "Accept=application/json" } )
	public JsonResult notePublish( 
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestBody Draft draft,
			HttpServletRequest request
			) throws Exception{


		LoginUtils.checkLogin(securityUser);
		draft.setUser( new User( securityUser.getUserId() ) );
		
		String ipAddress = request.getRemoteAddr();

		Site site = sunny.getSite();
		Content content = draftService.publish(sunny, draft,ipAddress);
		//TODO 컨텐츠가 Answer 였을 때의 처리
		return new JsonResult(true,  messageSource.getMessage("draft.successPublish", null, LocaleUtils.getLocale()), content.getId());
	}

	@ResponseBody
	@RequestMapping( value="/draft/publish", method = RequestMethod.POST, headers = { "Accept=application/json" } )
	public JsonResult notePublish( 
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestBody Draft draft,
			HttpServletRequest request
			) throws Exception{

		return notePublish( null, sunny, securityUser, draft, request);

	}
	@ResponseBody
	@RequestMapping( value="/{path}/draft/push", method = RequestMethod.POST, headers = { "Accept=application/json" } )
	public JsonResult notePush( 
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestBody Draft draft,
			HttpServletRequest request
			){

		LoginUtils.checkLogin(securityUser);
		draft.setUser( new User( securityUser.getUserId() ));
		
		String ipAddress = request.getRemoteAddr();
		
		draftService.push(sunny, draft, ipAddress);

		return new JsonResult(true,  messageSource.getMessage("draft.successPush", null, LocaleUtils.getLocale()), draft.getPushContent().getId());
	}
//	@RequestMapping( value = "/draft/delete", method = RequestMethod.GET, headers = { "Accept=application/json" }  )
//	@ResponseBody
//	public JsonResult delete(
//			@ParseSunny(shouldExistsSite=true) Sunny sunny, 
//			@AuthUser SecurityUser securityUser, 
//			@RequestParam( value="id", required=true )Long id){
//
//		return delete( null, sunny, securityUser, id );
//	}

	/*
	 * MultiSiteProxy
	 */
	
	@ResponseBody
	@RequestMapping( value="/draft/{id}", method = RequestMethod.GET, headers = { "Accept=application/json" } )
	public JsonResult getNoteDraft( 
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@PathVariable Long id
			){
		return getNoteDraft( null, sunny, securityUser, id );
	}
	
	@ResponseBody
	@RequestMapping( value="/draft/target/{id}", method = RequestMethod.GET, headers = { "Accept=application/json" } )
	public JsonResult getTargetDraft(
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@PathVariable( "id" ) Long targetId
			){


		return getTargetDraft( null, sunny, securityUser, targetId );
	}

	@ResponseBody
	@RequestMapping( value = "/draft/create/note", method = RequestMethod.GET, headers = { "Accept=application/json", "Content-Type=text/javascript" }  )
	public JsonResult tmpCreate(
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			Long pushContentId , 
			Long targetContentId, 
			@RequestParam(value="gid", required=false) Long groupId ){
		return tmpCreate( null, sunny, securityUser, pushContentId,targetContentId, groupId  );
	}
	
	
	@ResponseBody
	@RequestMapping( value="/draft/update", method = RequestMethod.POST, headers = { "Accept=application/json" } )
	public JsonResult noteDraft( 
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestBody Draft draft,
			HttpServletRequest request
			
			){

		return noteDraft( null, sunny, securityUser, draft, request);

	}
	
	
	@ResponseBody
	@RequestMapping( value="/draft/push", method = RequestMethod.POST, headers = { "Accept=application/json" } )
	public JsonResult notePush( 
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestBody Draft draft,
			HttpServletRequest request
			){


		return notePush( null, sunny, securityUser, draft, request);
	}

}
