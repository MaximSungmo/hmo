package kr.co.sunnyvale.sunny.controller.web;


import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.sunnyvale.sunny.annotation.AuthUser;
import kr.co.sunnyvale.sunny.annotation.ParseSunny;
import kr.co.sunnyvale.sunny.annotation.SmallGroupPrivacy;
import kr.co.sunnyvale.sunny.domain.Content;
import kr.co.sunnyvale.sunny.domain.Draft;
import kr.co.sunnyvale.sunny.domain.Media;
import kr.co.sunnyvale.sunny.domain.Note;
import kr.co.sunnyvale.sunny.domain.Site;
import kr.co.sunnyvale.sunny.domain.SmallGroup;
import kr.co.sunnyvale.sunny.domain.Tag;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.repository.hibernate.ContentService;
import kr.co.sunnyvale.sunny.repository.hibernate.UserSmallGroupAccessRepository;
import kr.co.sunnyvale.sunny.service.DraftService;
import kr.co.sunnyvale.sunny.service.LastReadService;
import kr.co.sunnyvale.sunny.service.MediaService;
import kr.co.sunnyvale.sunny.service.NoteService;
import kr.co.sunnyvale.sunny.service.SiteService;
import kr.co.sunnyvale.sunny.service.SmallGroupService;
import kr.co.sunnyvale.sunny.service.TagService;
import kr.co.sunnyvale.sunny.service.UserService;
import kr.co.sunnyvale.sunny.service.impl.OperationService;
import kr.co.sunnyvale.sunny.util.LoginUtils;
import kr.co.sunnyvale.sunny.util.SearchUtils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
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
public class NoteController {
	
	@Autowired
	private NoteService noteService;

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
	private UserSmallGroupAccessRepository userSmallGroupAccessRepository;
	
	@Autowired
	private MediaService mediaService; 
	
	private Logger logger = Logger.getLogger(this.getClass());

	/*
	 * ********************************************************
	 * MultiSite
	 * *********************************************************
	 */
	
	@RequestMapping( value = "/{path}/note", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
	public ModelAndView index( 
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny, 
			@AuthUser SecurityUser securityUser, 
			@RequestParam(value="q", required=false) String queries,
			@RequestParam(value ="tab", required=false) String tabTitle,
			@RequestParam(value ="tagTitle", required=false) String tagTitle,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) Boolean isPagelet ) {
		
		ModelAndView modelAndView = new ModelAndView();

		User authUser = null;
		
		if( securityUser != null ){
			authUser = new User( securityUser.getUserId() );
		}
		Page<Note> pagedResult = noteService.getPagedPostingNotes( sunny, authUser, queries, null, null, tabTitle, tagTitle, page, Page.DEFAULT_PAGE_SIZE );

//		if( securityUser != null ){
//			int tmpNoteCount = securityUser.getNoteLastReadCount();
//					
//			int retNoteCount = lastReadService.updateNoteCount( new User(securityUser.getUserId()) );
//			if( tmpNoteCount != retNoteCount ){
//				securityUser.setNoteLastReadCount( retNoteCount );
//			}
//			
//		}
		
		modelAndView.addObject("pagedResult", pagedResult);
		if( tabTitle != null ){
			modelAndView.addObject("tab", tabTitle);
		}
		
		if( isPagelet != null && isPagelet ){
			modelAndView.setViewName("/pagelet/note/list");
		}else{
			modelAndView.setViewName("/note/index");	
		}
		
		return modelAndView;
	}

	@RequestMapping( value = "/note", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
	public ModelAndView index(
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestParam(value="q", required=false) String queries,
			@RequestParam(value ="tab", required=false) String tabTitle,
			@RequestParam(value ="tagTitle", required=false) String tagTitle,
			@RequestParam(value="page", required=false) Integer page ,
			@RequestParam(value="pl", required=false) Boolean isPagelet) {

		return index( null, sunny, securityUser,
				queries,
				tabTitle,
				tagTitle,
				page,
				isPagelet);
	}
	
	
	
	@SmallGroupPrivacy( onlyAdmin = false )
	@RequestMapping( value = "/{path}/group/{id}/note", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
	public ModelAndView smallGroupNotes( 
			@PathVariable("path") String path, 
			@PathVariable( "id" ) Long groupId, 
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="q", required=false) String queryName,
			@RequestParam(value ="tab", required=false) String tabTitle, 
			@RequestParam(value ="tagTitle", required=false) String tagTitle,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) Boolean isPagelet ) {

		ModelAndView modelAndView = new ModelAndView();
		

		User authUser = null;
		
		if( securityUser != null ){
			authUser = new User( securityUser.getUserId() );
		}
		
		Page<Note> pagedResult = noteService.getPagedPostingNotes( sunny, authUser, queryName, groupId, null, tabTitle, tagTitle, page, Page.DEFAULT_PAGE_SIZE );
		
		modelAndView.addObject("pagedResult", pagedResult);
		if( tabTitle != null ){
			modelAndView.addObject("tab", tabTitle);
		}
		if( isPagelet != null && isPagelet ){
			modelAndView.setViewName("/pagelet/note/list");
		}else{
			modelAndView.setViewName("/note/index");	
		}
		
		return modelAndView;
	}
	
	@SmallGroupPrivacy( onlyAdmin = false )
	@RequestMapping( value = "/group/{id}/note", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
	public ModelAndView smallGroupNotes( 
			@PathVariable( "id" ) Long id,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestParam(value="q", required=false) String queryName,
			@RequestParam(value ="tab", required=false) String tabTitle, 
			@RequestParam(value ="tagTitle", required=false) String tagTitle,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) Boolean isPagelet ) {

		return smallGroupNotes(
				null, 
				id,
				sunny,
				securityUser,
				queryName,
				tabTitle,
				tagTitle,
				page,
				isPagelet);
	}

	@RequestMapping( value = "/{path}/note/{id}", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
	public ModelAndView view(
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@PathVariable( "id" ) Long id,			
			@RequestParam(value="q", required=false) String queries,
			@RequestParam(value="uid", required=false) String userId,
			@RequestParam(value ="tab", required=false) String tabTitle,
			@RequestParam(value ="tagTitle", required=false) String tagTitle,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) Boolean isPagelet,				
			HttpServletResponse response,
			HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView();

		modelAndView.addObject("queries", queries);
		
		
		User user = null;
		
		if( securityUser != null ){
			user = userService.findById( securityUser.getUserId() );
		}
		
		Note note = noteService.select(id, user);
		SmallGroup smallGroup = null;
		Long smallGroupId = null;

		Site site = sunny.getSite();
		if( note.getSmallGroup() != null && !note.getSmallGroup().getId().equals(site.getLobbySmallGroup().getId()) ){
			smallGroup = smallGroupService.getSmallGroup( note.getSmallGroup().getId());
			smallGroupId = smallGroup.getId();
		}else{
			smallGroupId = site.getLobbySmallGroup().getId();
		}
		Note prevNote = noteService.getSequenceNote( sunny, user, -1, note, queries, smallGroupId, userId, tabTitle, tagTitle, id );

		Note nextNote = noteService.getSequenceNote( sunny, user, 1, note, queries, smallGroupId, userId, tabTitle, tagTitle, id );

		modelAndView.addObject("prevNote", prevNote);
		modelAndView.addObject("nextNote", nextNote);
		
		
		if( securityUser == null ){
			
			modelAndView.addObject("content", note);

			Cookie cookie = WebUtils.getCookie(request, "n-" + note.getId());
			if( cookie == null || !cookie.getValue().equals("1") ){
				operationService.addViewCount( note, null );
				cookie = new Cookie("n-" + note.getId(), "1");
				cookie.setMaxAge(216000);
				response.addCookie(cookie);
			}
		}else{
			operationService.addViewCount( note, user);
		}
		

		modelAndView.addObject("content", note);
		modelAndView.addObject("mediaes", mediaService.getMedies(note.getId(), null));
		
		if( securityUser != null && userId != null && userId.equals(securityUser.getUserId()) ){
			modelAndView.addObject("m", "my");
		}else{
			modelAndView.addObject("m", "all");
		}

//		if( smallGroupId != site.getLobbySmallGroup().getId()){
//			modelAndView.addObject( "smallGroup", smallGroupService.getSmallGroup( note.getSmallGroup().getId()) );
//			modelAndView.setViewName( ( note.getSmallGroup().getId() != site.getLobbySmallGroup().getId() ? "/group/" : "" ) + "/note/view");
//		}else{
			modelAndView.setViewName( "/note/view" );
//		}
		
		return modelAndView;
	}
	@RequestMapping( value = "/note/{id}", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
	public ModelAndView view( 
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@PathVariable( "id" ) Long id,			
			@RequestParam(value="q", required=false) String queries,
			@RequestParam(value="uid", required=false) String userId,
			@RequestParam(value ="tab", required=false) String tabTitle,
			@RequestParam(value ="tagTitle", required=false) String tagTitle,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) Boolean isPagelet,				
			HttpServletResponse response,
			HttpServletRequest request) {

		return view( 
				null,
				sunny,
				securityUser,
				id,
				queries,
				userId,
				tabTitle,
				tagTitle,
				page,
				isPagelet,
				response,
				request);
	}
	@SmallGroupPrivacy(onlyAdmin=false)
	@RequestMapping( value = "/{path}/group/{sgid}/note/{id}", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
	public ModelAndView smallGroupView(
			@PathVariable("path") String path, 
			@PathVariable( "sgid" ) Long smallGroupId,
			@PathVariable( "id" ) Long id,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestParam(value="q", required=false) String queries,
			@RequestParam(value="uid", required=false) String userId,
			@RequestParam(value ="tab", required=false) String tabTitle,
			@RequestParam(value ="tagTitle", required=false) String tagTitle,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) Boolean isPagelet,				
			HttpServletResponse response,
			HttpServletRequest request) {

		
		ModelAndView modelAndView = new ModelAndView();

		modelAndView.addObject("queries", queries);
		
		
		User user = null;
		
		if( securityUser != null ){
			user = userService.findById( securityUser.getUserId() );
		}
		
		Note note = noteService.select(id, user);
		SmallGroup smallGroup = null;

		Site site = sunny.getSite();
		if( note.getSmallGroup() != null && !note.getSmallGroup().getId().equals(site.getLobbySmallGroup().getId()) ){
			smallGroup = smallGroupService.getSmallGroup( note.getSmallGroup().getId());
			smallGroupId = smallGroup.getId();
		}else{
			smallGroupId = site.getLobbySmallGroup().getId();
		}
		Note prevNote = noteService.getSequenceNote( sunny, user, -1, note, queries, smallGroupId, userId, tabTitle, tagTitle, id );

		Note nextNote = noteService.getSequenceNote( sunny, user, 1, note, queries, smallGroupId, userId, tabTitle, tagTitle, id );

		
		
		if( securityUser == null ){
			
			modelAndView.addObject("content", note);

			Cookie cookie = WebUtils.getCookie(request, "n-" + note.getId());
			if( cookie == null || !cookie.getValue().equals("1") ){
				operationService.addViewCount( note, null );
				cookie = new Cookie("n-" + note.getId(), "1");
				cookie.setMaxAge(216000);
				response.addCookie(cookie);
			}
		}else{
			operationService.addViewCount( note, user);
		}
		

		modelAndView.addObject("content", note);
		modelAndView.addObject("mediaes", mediaService.getMedies(note.getId(), null));
		modelAndView.addObject("prevNote", prevNote);
		modelAndView.addObject("nextNote", nextNote);
		
		if( securityUser != null && userId != null && userId.equals(securityUser.getUserId()) ){
			modelAndView.addObject("m", "my");
		}else{
			modelAndView.addObject("m", "all");
		}

//		if( smallGroupId != site.getLobbySmallGroup().getId()){
//			modelAndView.addObject( "smallGroup", smallGroupService.getSmallGroup( note.getSmallGroup().getId()) );
//			modelAndView.setViewName( ( note.getSmallGroup().getId() != site.getLobbySmallGroup().getId() ? "/group/" : "" ) + "/note/view");
//		}else{
			modelAndView.setViewName( "/note/view" );
//		}
		
		return modelAndView;
	}
	

	@SmallGroupPrivacy(onlyAdmin=false)
	@RequestMapping( value = "/group/{sgid}/note/{id}", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
	public ModelAndView smallGroupView( 
			@PathVariable( "sgid" ) Long smallGroupId,
			@PathVariable( "id" ) Long id,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestParam(value="q", required=false) String queries,
			@RequestParam(value="uid", required=false) String userId,
			@RequestParam(value ="tab", required=false) String tabTitle,
			@RequestParam(value ="tagTitle", required=false) String tagTitle,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) Boolean isPagelet,				
			HttpServletResponse response,
			HttpServletRequest request) {

		return smallGroupView( 
				null,
				smallGroupId,
				id,
				sunny,
				securityUser,
				queries,
				userId,
				tabTitle,
				tagTitle,
				page,
				isPagelet,
				response,
				request);
	}
	
	
	
	@RequestMapping( value = "/{path}/note/edit/{id}", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
	public ModelAndView editPushDraft(
			@PathVariable("path") String path,  
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@PathVariable( "id" ) Long pushContentId,
			HttpServletResponse response,
			HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView();
		

		LoginUtils.checkLogin(securityUser);
		
		Draft draft = draftService.selectFromPush( sunny, pushContentId, new User(securityUser.getUserId()) );
		
		List<Media> mediaes = mediaService.getMedies(pushContentId, null);
		
		modelAndView.addObject("mediaes", mediaes);
		modelAndView.addObject("draft", draft);
		modelAndView.setViewName("/note/edit");
		return modelAndView;
	}
	@RequestMapping( value = "/note/edit/{id}", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
	public ModelAndView editPushDraft( 
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@PathVariable( "id" ) Long pushContentId,							
			HttpServletResponse response,
			HttpServletRequest request) {

		return editPushDraft( 
				null, 
				sunny,
				securityUser,
				pushContentId,response,request);
	}

	@SmallGroupPrivacy(onlyAdmin=false)
	@RequestMapping( value = "/{path}/group/{sgid}/note/edit/{id}", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
	public ModelAndView editGroupNote(
			@PathVariable("path") String path,  
			@PathVariable( "sgid" ) Long smallGroupId,
			@PathVariable( "id" ) Long pushContentId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			HttpServletResponse response,
			HttpServletRequest request) {

		ModelAndView modelAndView = editPushDraft(path, sunny, securityUser, pushContentId, response, request);
		
		return modelAndView;
	}
	@SmallGroupPrivacy(onlyAdmin=false)
	@RequestMapping( value = "/group/{sgid}/note/edit/{id}", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
	public ModelAndView editGroupNote( 
			@PathVariable( "sgid" ) Long smallGroupId,
			@PathVariable( "id" ) Long pushContentId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 							
			HttpServletResponse response,
			HttpServletRequest request) {

		return editGroupNote( 
				null, 
				smallGroupId,
				pushContentId,
				sunny,
				securityUser,response,request);
	}

	
//	@RequestMapping( value = "/{path}/note/preview/{id}", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
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
//	@RequestMapping( value = "/note/preview/{id}", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
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
	
	@RequestMapping( value = "/{path}/note/delete", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
	public ModelAndView delete( 
			@PathVariable("path") String path, 
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestParam( value="id", required=true )Long id,
			@RequestHeader(value = "referer", required = false) final String referer){

		ModelAndView modelAndView = new ModelAndView();
		LoginUtils.checkLogin(securityUser);
		
		boolean isSameUser = contentService.isSameUser( id, securityUser.getUserId() );
		
		if( isSameUser == false ){
			logger.error("다른 질문 유저로부터 삭제 시도");
			modelAndView.setViewName("redirect:/error/exception");
			return modelAndView;
		}
		noteService.delete(id);
		modelAndView.setViewName("redirect:/note");

		return modelAndView;
		
	}

	@RequestMapping( value = "/note/delete", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
	public ModelAndView delete( 
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestParam( value="id", required=true )Long id,
			@RequestHeader(value = "referer", required = false) final String referer){

		return delete( 
				null,
				sunny,
				securityUser,
				id,referer);
	}
	
	@RequestMapping( value = "/{path}/group/{id}/note/drafts", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
	public ModelAndView noteDrafts( 
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@PathVariable("id") Long id,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) Boolean isPagelet ){

		ModelAndView modelAndView = new ModelAndView();		
		Page<Draft> pagedResult = draftService.getPagedList( sunny, id, new User(securityUser.getUserId()), Content.TYPE_NOTE,  page, Page.DEFAULT_PAGE_SIZE);
		
		modelAndView.addObject("pagedResult", pagedResult);
		modelAndView.addObject("smallGroup", smallGroupService.getSmallGroup( id ));
		if( isPagelet != null && isPagelet == true ){
			modelAndView.setViewName("/pagelet/drafts");	
		}else{
			modelAndView.setViewName("/note/drafts");
		}
		
		return modelAndView;
	}


	@RequestMapping( value = "/group/{id}/note/drafts", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
	public ModelAndView noteDrafts( 
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@PathVariable("id") Long id,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) Boolean isPagelet ){

		return noteDrafts(
				null,
				sunny,
				securityUser,
				id,
				page,
				isPagelet);
	}
	
	@RequestMapping( value = "/{path}/note/drafts", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
	public ModelAndView write( 
			@PathVariable("path") String path, 
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) Boolean isPagelet ){

		ModelAndView modelAndView = new ModelAndView();		
		Page<Draft> pagedResult = draftService.getPagedList( sunny, null, new User(securityUser.getUserId()), Content.TYPE_NOTE,  page, Page.DEFAULT_PAGE_SIZE);
		
		modelAndView.addObject("pagedResult", pagedResult);
		modelAndView.setViewName("/note/drafts");
		return modelAndView;
	}
	

	@RequestMapping( value = "/note/drafts", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
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
	

	
	@RequestMapping( value = "/{path}/note/write", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
	public ModelAndView writeNext( 
			@PathVariable("path") String path, 
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestParam( value = "id", required=false) Long id){

		ModelAndView modelAndView = new ModelAndView();
		
		Draft draft = draftService.select(id);
		draft.setMediaes( mediaService.getFromDraft(draft));
		modelAndView.addObject("draft", draft); 
		Site site = sunny.getSite();
//		if( !draft.getSmallGroup().getId().equals( site.getLobbySmallGroup().getId() )){
//			modelAndView.addObject("smallGroup", draft.getSmallGroup() );
//			modelAndView.setViewName("/group/note/write");
//		}else{
			modelAndView.setViewName("/note/write");
//		}
		return modelAndView;
	}
	

	@RequestMapping( value = "/note/write", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
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
	

	@SmallGroupPrivacy(onlyAdmin=false)
	@RequestMapping( value = "/{path}/group/{sgid}/note/write", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
	public ModelAndView groupWriteNext( 
			@PathVariable("path") String path, 
			@PathVariable("sgid") Long smallGroupId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestParam( value = "id", required=false) Long id){

		ModelAndView modelAndView = new ModelAndView();
		
		Draft draft = draftService.select(id);
		modelAndView.addObject("draft", draft); 
		Site site = sunny.getSite();
//		if( !draft.getSmallGroup().getId().equals( site.getLobbySmallGroup().getId() )){
//			modelAndView.addObject("smallGroup", draft.getSmallGroup() );
//			modelAndView.setViewName("/group/note/write");
//		}else{
			modelAndView.setViewName("/note/write");
//		}
		return modelAndView;
	}
	
	@SmallGroupPrivacy(onlyAdmin=false)
	@RequestMapping( value = "/group/{sgid}/note/write", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
	public ModelAndView groupWriteNext( 
			@PathVariable("sgid") Long smallGroupId,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestParam( value = "id", required=false) Long id){

		return writeNext(
				null,
				sunny,
				securityUser,
				id);
	}
	
//	@RequestMapping( value = "/{path}/note/edit", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
//	public ModelAndView edit( 
//			@PathVariable("path") String path, 
//			@ParseSunny(shouldExistsSite=true) Sunny sunny,
//			@AuthUser SecurityUser securityUser,
//			@RequestParam(value = "id", required = false) final Long id ){
//
//		ModelAndView modelAndView = new ModelAndView();
//		Draft draft = draftService.selectFromPush( sunny, id, new User(securityUser.getUserId()) );
//
//		modelAndView.addObject("draft",draft);
//		Site site = sunny.getSite();
//		if( !draft.getSmallGroup().getId().equals( site.getLobbySmallGroup().getId() )){
//			modelAndView.addObject("smallGroup", draft.getSmallGroup() );
//			modelAndView.setViewName("/group/note/edit");
//		}else{
//			modelAndView.setViewName("/note/edit");
//		}
//		
//		return modelAndView;
//	}
//
//	@RequestMapping( value = "/note/edit", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
//	public ModelAndView edit( 
//			@ParseSunny(shouldExistsSite=true) Sunny sunny,
//			@AuthUser SecurityUser securityUser,
//			@RequestParam(value = "id", required = false) final Long id ){
//
//		return edit(
//				null,
//				sunny,
//				securityUser,
//				id);
//	}
	

//	@RequestMapping( value = "/{path}/note/tagged/{tagTitle}", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
//	public ModelAndView noteTag( 
//			@PathVariable("path") String path, 
//			@ParseSunny(shouldExistsSite=true) Sunny sunny,
//			@AuthUser SecurityUser securityUser,
//			@PathVariable( "tagTitle" ) String tagTitle )  {
//		
//		ModelAndView modelAndView = new ModelAndView();
//
//		Stream stream = null;
//
//		Site site = sunny.getSite();
//		List<Note> notes = noteService.getTaggedList(site.getLobbySmallGroup(), tagTitle, stream);
//		
//		modelAndView.addObject("contents", notes);
//		modelAndView.setViewName("/note/index");
//		return modelAndView;
//	}

	/*
	 * Multi Site Proxy
	 */
	

//	@RequestMapping( value = "/note/user/{userId}", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
//	public ModelAndView usersNote(
//			@ParseSunny(shouldExistsSite=true) Sunny sunny,
//			@AuthUser SecurityUser securityUser,
//					@PathVariable(value="userId") String userId,
//					@RequestParam(value="q[]", required=false) String[] queries,
//					@RequestParam(value="nq", required=false) String newQuery,
//					@RequestParam(value="recursive", required=false) Boolean isRecursive,
//					@RequestParam(value ="tab", required=false) String tabTitle, 
//					@RequestParam(value ="tagTitle", required=false) String tagTitle,
//					@RequestParam(value="page", required=false) Integer page ) {
//		return usersNote( 
//				null, 
//				sunny, 
//				securityUser,
//				userId,
//				queries,
//				newQuery,
//				isRecursive,
//				tabTitle,
//				tagTitle,
//				page);
//	}
	
//	@RequestMapping( value = "/note/tagged/{tagTitle}", method = RequestMethod.GET, headers = { "Accept=text/html" }  )
//	public ModelAndView noteTag( 
//			@ParseSunny(shouldExistsSite=true) Sunny sunny,
//			@AuthUser SecurityUser securityUser,
//			@PathVariable( "tagTitle" ) String tagTitle )  {
//		
//		return noteTag(
//				null,
//				sunny,
//				securityUser,
//				tagTitle);
//	}
}
