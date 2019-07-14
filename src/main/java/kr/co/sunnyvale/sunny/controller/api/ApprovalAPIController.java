package kr.co.sunnyvale.sunny.controller.api;

import java.util.List;

import kr.co.sunnyvale.sunny.annotation.AuthUser;
import kr.co.sunnyvale.sunny.annotation.ParseSunny;
import kr.co.sunnyvale.sunny.domain.Approval;
import kr.co.sunnyvale.sunny.domain.FeelAndContentAndUser;
import kr.co.sunnyvale.sunny.domain.SmallGroupApproval;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.dto.ContentDTO;
import kr.co.sunnyvale.sunny.domain.dto.JsonResult;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.repository.hibernate.ContentService;
import kr.co.sunnyvale.sunny.service.ApprovalService;
import kr.co.sunnyvale.sunny.service.SiteService;
import kr.co.sunnyvale.sunny.service.SmallGroupApprovalService;
import kr.co.sunnyvale.sunny.service.SmallGroupService;
import kr.co.sunnyvale.sunny.service.StoryService;
import kr.co.sunnyvale.sunny.service.UserService;
import kr.co.sunnyvale.sunny.util.LocaleUtils;
import kr.co.sunnyvale.sunny.util.LoginUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
public class ApprovalAPIController {
	
	@Autowired
	private StoryService storyService;
	
	@Autowired
	private SmallGroupService smallGroupService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private SiteService siteService;

	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private ApprovalService approvalService; 
	
	@Autowired
	private SmallGroupApprovalService smallGroupApprovalService; 
	

	@Autowired
	private ContentService contentService;
	
	
	@RequestMapping( value = "/{path}/approval", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult approvalStream(
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestParam(value="menu", required=false) String menu,
			@RequestParam("contentId") Long contentId,  
			@RequestParam(value="q[]", required=false) String[] queries,
			@RequestParam(value="top", required=false) Boolean top, 
			@RequestParam(value="size", required=false) Integer size){
	
		LoginUtils.checkLogin(securityUser);
		
		
		Stream stream;
		
		if(top == null)
			top = true;
		
		if( contentId == null )
			stream = new Stream();
		else
			stream = new Stream(top == true ? true : false, "this.id", contentId, size);
		
		User user = userService.findById( securityUser.getUserId() );
		List<ContentDTO> stories = contentService.fetchApprovals(sunny, menu, null, false,user, queries, stream);
		
		if( stories == null ){
			return new JsonResult(false, messageSource.getMessage("story.noStories", null, LocaleUtils.getLocale()), null);
		}

		return new JsonResult(true, messageSource.getMessage("story.successFetchStories", null, LocaleUtils.getLocale()), stories);
	}
	
	@RequestMapping( value = "/approval", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult approvalStream(
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestParam(value="menu", required=false) String menu,
			@RequestParam("contentId") Long contentId,  
			@RequestParam(value="q[]", required=false) String[] queries,
			@RequestParam(value="top", required=false) Boolean top, 
			@RequestParam(value="size", required=false) Integer size){
	
		return approvalStream( null, sunny, securityUser, menu, contentId, queries, top, size);
	}

//	@RequestMapping( value = "/{path}/approval/sent_process", method = RequestMethod.GET, headers = { "Accept=application/json" })
//	@ResponseBody
//	public JsonResult sentProcess(
//			@PathVariable("path") String path,
//			@ParseSunny(shouldExistsSite=false) Sunny sunny,
//			@AuthUser SecurityUser securityUser, 
//			@RequestParam("contentId") Long contentId,  
//			@RequestParam(value="q[]", required=false) String[] queries,
//			@RequestParam(value="top", required=false) Boolean top, 
//			@RequestParam(value="size", required=false) Integer size){
//	
//		LoginUtils.checkLogin(securityUser);
//		
//		
//		Stream stream;
//		
//		if(top == null)
//			top = true;
//		
//		if( contentId == null )
//			stream = new Stream();
//		else
//			stream = new Stream(top == true ? true : false, "this.id", contentId, size);
//		
//		User user = userService.findById( securityUser.getUserId() );
//		List<ContentDTO> stories = contentService.fetchApprovals(sunny, null, false, Approval.STATUS_SENT, user, null, user, null, queries, stream);
//		
//		if( stories == null ){
//			return new JsonResult(false, messageSource.getMessage("story.noStories", null, LocaleUtils.getLocale()), null);
//		}
//
//		return new JsonResult(true, messageSource.getMessage("story.successFetchStories", null, LocaleUtils.getLocale()), stories);
//	}
//	
//	@RequestMapping( value = "/approval/sent_process", method = RequestMethod.GET, headers = { "Accept=application/json" })
//	@ResponseBody
//	public JsonResult sentProcess(
//			@ParseSunny(shouldExistsSite=false) Sunny sunny,
//			@AuthUser SecurityUser securityUser, 
//			@RequestParam("contentId") Long contentId,  
//			@RequestParam(value="q[]", required=false) String[] queries,
//			@RequestParam(value="top", required=false) Boolean top, 
//			@RequestParam(value="size", required=false) Integer size){
//	
//		return sentProcess( null, sunny, securityUser, contentId, queries, top, size);
//	}
//	
//	@RequestMapping( value = "/{path}/approval/receive_process", method = RequestMethod.GET, headers = { "Accept=application/json" })
//	@ResponseBody
//	public JsonResult receiveProcess(
//			@PathVariable("path") String path,
//			@ParseSunny(shouldExistsSite=false) Sunny sunny,
//			@AuthUser SecurityUser securityUser, 
//			@RequestParam("contentId") Long contentId,  
//			@RequestParam(value="q[]", required=false) String[] queries,
//			@RequestParam(value="top", required=false) Boolean top, 
//			@RequestParam(value="size", required=false) Integer size){
//	
//		LoginUtils.checkLogin(securityUser);
//		
//		
//		Stream stream;
//		
//		if(top == null)
//			top = true;
//		
//		if( contentId == null )
//			stream = new Stream();
//		else
//			stream = new Stream(top == true ? true : false, "this.id", contentId, size);
//		
//		User user = userService.findById( securityUser.getUserId() );
//		List<ContentDTO> stories = contentService.fetchApprovals(sunny, null, false, Approval.STATUS_SENT, null, user, user, null, queries, stream);
//			
//		if( stories == null ){
//			return new JsonResult(false, messageSource.getMessage("story.noStories", null, LocaleUtils.getLocale()), null);
//		}
//
//		return new JsonResult(true, messageSource.getMessage("story.successFetchStories", null, LocaleUtils.getLocale()), stories);
//	}
//	
//	@RequestMapping( value = "/approval/receive_process", method = RequestMethod.GET, headers = { "Accept=application/json" })
//	@ResponseBody
//	public JsonResult receiveProcess(
//			@ParseSunny(shouldExistsSite=false) Sunny sunny,
//			@AuthUser SecurityUser securityUser, 
//			@RequestParam("contentId") Long contentId,  
//			@RequestParam(value="q[]", required=false) String[] queries,
//			@RequestParam(value="top", required=false) Boolean top, 
//			@RequestParam(value="size", required=false) Integer size){
//	
//		return receiveProcess( null, sunny, securityUser, contentId, queries, top, size);
//	}
//	
//	@RequestMapping( value = "/{path}/approval/approved", method = RequestMethod.GET, headers = { "Accept=application/json" })
//	@ResponseBody
//	public JsonResult approved(
//			@PathVariable("path") String path,
//			@ParseSunny(shouldExistsSite=false) Sunny sunny,
//			@AuthUser SecurityUser securityUser, 
//			@RequestParam("contentId") Long contentId,  
//			@RequestParam(value="q[]", required=false) String[] queries,
//			@RequestParam(value="top", required=false) Boolean top, 
//			@RequestParam(value="size", required=false) Integer size){
//	
//		LoginUtils.checkLogin(securityUser);
//		
//		
//		Stream stream;
//		
//		if(top == null)
//			top = true;
//		
//		if( contentId == null )
//			stream = new Stream();
//		else
//			stream = new Stream(top == true ? true : false, "this.id", contentId, size);
//		
//		User user = userService.findById( securityUser.getUserId() );
//		List<ContentDTO> stories = contentService.fetchApprovals(sunny, null, false, Approval.STATUS_APPROVED, null, null, user, null, queries, stream);
//		
//		if( stories == null ){
//			return new JsonResult(false, messageSource.getMessage("story.noStories", null, LocaleUtils.getLocale()), null);
//		}
//
//		return new JsonResult(true, messageSource.getMessage("story.successFetchStories", null, LocaleUtils.getLocale()), stories);
//	}
//	
//	@RequestMapping( value = "/approval/approved", method = RequestMethod.GET, headers = { "Accept=application/json" })
//	@ResponseBody
//	public JsonResult approved(
//			@ParseSunny(shouldExistsSite=false) Sunny sunny,
//			@AuthUser SecurityUser securityUser, 
//			@RequestParam("contentId") Long contentId,  
//			@RequestParam(value="q[]", required=false) String[] queries,
//			@RequestParam(value="top", required=false) Boolean top, 
//			@RequestParam(value="size", required=false) Integer size){
//	
//		return approved( null, sunny, securityUser, contentId, queries, top, size);
//	}
//	
//	@RequestMapping( value = "/{path}/approval/sent_approved", method = RequestMethod.GET, headers = { "Accept=application/json" })
//	@ResponseBody
//	public JsonResult sentApproved(
//			@PathVariable("path") String path,
//			@ParseSunny(shouldExistsSite=false) Sunny sunny,
//			@AuthUser SecurityUser securityUser, 
//			@RequestParam("contentId") Long contentId,  
//			@RequestParam(value="q[]", required=false) String[] queries,
//			@RequestParam(value="top", required=false) Boolean top, 
//			@RequestParam(value="size", required=false) Integer size){
//	
//		LoginUtils.checkLogin(securityUser);
//		
//		
//		Stream stream;
//		
//		if(top == null)
//			top = true;
//		
//		if( contentId == null )
//			stream = new Stream();
//		else
//			stream = new Stream(top == true ? true : false, "this.id", contentId, size);
//		
//		User user = userService.findById( securityUser.getUserId() );
//		List<ContentDTO> stories = contentService.fetchApprovals(sunny, null, false, Approval.STATUS_APPROVED, user, null, user, null, queries, stream);
//		
//		if( stories == null ){
//			return new JsonResult(false, messageSource.getMessage("story.noStories", null, LocaleUtils.getLocale()), null);
//		}
//
//		return new JsonResult(true, messageSource.getMessage("story.successFetchStories", null, LocaleUtils.getLocale()), stories);
//	}
//	
//	@RequestMapping( value = "/approval/sent_approved", method = RequestMethod.GET, headers = { "Accept=application/json" })
//	@ResponseBody
//	public JsonResult sentApproved(
//			@ParseSunny(shouldExistsSite=false) Sunny sunny,
//			@AuthUser SecurityUser securityUser, 
//			@RequestParam("contentId") Long contentId,  
//			@RequestParam(value="q[]", required=false) String[] queries,
//			@RequestParam(value="top", required=false) Boolean top, 
//			@RequestParam(value="size", required=false) Integer size){
//	
//		return sentApproved( null, sunny, securityUser, contentId, queries, top, size);
//	}
//	
//	@RequestMapping( value = "/{path}/approval/receive_approved", method = RequestMethod.GET, headers = { "Accept=application/json" })
//	@ResponseBody
//	public JsonResult receiveApproved(
//			@PathVariable("path") String path,
//			@ParseSunny(shouldExistsSite=false) Sunny sunny,
//			@AuthUser SecurityUser securityUser, 
//			@RequestParam("contentId") Long contentId,  
//			@RequestParam(value="q[]", required=false) String[] queries,
//			@RequestParam(value="top", required=false) Boolean top, 
//			@RequestParam(value="size", required=false) Integer size){
//	
//		LoginUtils.checkLogin(securityUser);
//		
//		
//		Stream stream;
//		
//		if(top == null)
//			top = true;
//		
//		if( contentId == null )
//			stream = new Stream();
//		else
//			stream = new Stream(top == true ? true : false, "this.id", contentId, size);
//		
//		User user = userService.findById( securityUser.getUserId() );
//		List<ContentDTO> stories = contentService.fetchApprovals(sunny, null, false, Approval.STATUS_APPROVED, null, user, user, null, queries, stream);
//		
//		if( stories == null ){
//			return new JsonResult(false, messageSource.getMessage("story.noStories", null, LocaleUtils.getLocale()), null);
//		}
//
//		return new JsonResult(true, messageSource.getMessage("story.successFetchStories", null, LocaleUtils.getLocale()), stories);
//	}
//	
//	@RequestMapping( value = "/approval/receive_approved", method = RequestMethod.GET, headers = { "Accept=application/json" })
//	@ResponseBody
//	public JsonResult receiveApproved(
//			@ParseSunny(shouldExistsSite=false) Sunny sunny,
//			@AuthUser SecurityUser securityUser, 
//			@RequestParam("contentId") Long contentId,  
//			@RequestParam(value="q[]", required=false) String[] queries,
//			@RequestParam(value="top", required=false) Boolean top, 
//			@RequestParam(value="size", required=false) Integer size){
//	
//		return receiveApproved( null, sunny, securityUser, contentId, queries, top, size);
//	}
//	
//	@RequestMapping( value = "/{path}/approval/sent_reject", method = RequestMethod.GET, headers = { "Accept=application/json" })
//	@ResponseBody
//	public JsonResult sentReject(
//			@PathVariable("path") String path,
//			@ParseSunny(shouldExistsSite=false) Sunny sunny,
//			@AuthUser SecurityUser securityUser, 
//			@RequestParam("contentId") Long contentId,  
//			@RequestParam(value="q[]", required=false) String[] queries,
//			@RequestParam(value="top", required=false) Boolean top, 
//			@RequestParam(value="size", required=false) Integer size){
//	
//		LoginUtils.checkLogin(securityUser);
//		
//		
//		Stream stream;
//		
//		if(top == null)
//			top = true;
//		
//		if( contentId == null )
//			stream = new Stream();
//		else
//			stream = new Stream(top == true ? true : false, "this.id", contentId, size);
//		
//		User user = userService.findById( securityUser.getUserId() );
//		List<ContentDTO> stories = contentService.fetchApprovals(sunny, null, false, null, null, null, user, null, queries, stream);
//		
//		if( stories == null ){
//			return new JsonResult(false, messageSource.getMessage("story.noStories", null, LocaleUtils.getLocale()), null);
//		}
//
//		return new JsonResult(true, messageSource.getMessage("story.successFetchStories", null, LocaleUtils.getLocale()), stories);
//	}
//	
//	@RequestMapping( value = "/approval/sent_reject", method = RequestMethod.GET, headers = { "Accept=application/json" })
//	@ResponseBody
//	public JsonResult sentReject(
//			@ParseSunny(shouldExistsSite=false) Sunny sunny,
//			@AuthUser SecurityUser securityUser, 
//			@RequestParam("contentId") Long contentId,  
//			@RequestParam(value="q[]", required=false) String[] queries,
//			@RequestParam(value="top", required=false) Boolean top, 
//			@RequestParam(value="size", required=false) Integer size){
//	
//		return sentReject( null, sunny, securityUser, contentId, queries, top, size);
//	}
//	
//	@RequestMapping( value = "/{path}/approval/receive_reject", method = RequestMethod.GET, headers = { "Accept=application/json" })
//	@ResponseBody
//	public JsonResult receiveReject(
//			@PathVariable("path") String path,
//			@ParseSunny(shouldExistsSite=false) Sunny sunny,
//			@AuthUser SecurityUser securityUser, 
//			@RequestParam("contentId") Long contentId,  
//			@RequestParam(value="q[]", required=false) String[] queries,
//			@RequestParam(value="top", required=false) Boolean top, 
//			@RequestParam(value="size", required=false) Integer size){
//	
//		LoginUtils.checkLogin(securityUser);
//		
//		
//		Stream stream;
//		
//		if(top == null)
//			top = true;
//		
//		if( contentId == null )
//			stream = new Stream();
//		else
//			stream = new Stream(top == true ? true : false, "this.id", contentId, size);
//		
//		User user = userService.findById( securityUser.getUserId() );
//		List<ContentDTO> stories = contentService.fetchApprovals(sunny, null, false, Approval.STATUS_REJECT, user, null, user, null, queries, stream);
//		
//		if( stories == null ){
//			return new JsonResult(false, messageSource.getMessage("story.noStories", null, LocaleUtils.getLocale()), null);
//		}
//
//		return new JsonResult(true, messageSource.getMessage("story.successFetchStories", null, LocaleUtils.getLocale()), stories);
//	}
//	
//	@RequestMapping( value = "/approval/receive_reject", method = RequestMethod.GET, headers = { "Accept=application/json" })
//	@ResponseBody
//	public JsonResult receiveReject(
//			@ParseSunny(shouldExistsSite=false) Sunny sunny,
//			@AuthUser SecurityUser securityUser, 
//			@RequestParam("contentId") Long contentId,  
//			@RequestParam(value="q[]", required=false) String[] queries,
//			@RequestParam(value="top", required=false) Boolean top, 
//			@RequestParam(value="size", required=false) Integer size){
//	
//		return receiveReject( null, sunny, securityUser, contentId, queries, top, size);
//	}
//	
//	@RequestMapping( value = "/{path}/approval/receive", method = RequestMethod.GET, headers = { "Accept=application/json" })
//	@ResponseBody
//	public JsonResult receive(
//			@PathVariable("path") String path,
//			@ParseSunny(shouldExistsSite=false) Sunny sunny,
//			@AuthUser SecurityUser securityUser, 
//			@RequestParam("contentId") Long contentId,  
//			@RequestParam(value="q[]", required=false) String[] queries,
//			@RequestParam(value="top", required=false) Boolean top, 
//			@RequestParam(value="size", required=false) Integer size){
//	
//		LoginUtils.checkLogin(securityUser);
//		
//		
//		Stream stream;
//		
//		if(top == null)
//			top = true;
//		
//		if( contentId == null )
//			stream = new Stream();
//		else
//			stream = new Stream(top == true ? true : false, "this.id", contentId, size);
//		
//		User user = userService.findById( securityUser.getUserId() );
//		List<ContentDTO> stories = contentService.fetchApprovals(sunny, null, false, null, null, user, user, SmallGroupApproval.TYPE_RECEIVER, queries, stream);
//			
//		
//		if( stories == null ){
//			return new JsonResult(false, messageSource.getMessage("story.noStories", null, LocaleUtils.getLocale()), null);
//		}
//
//		return new JsonResult(true, messageSource.getMessage("story.successFetchStories", null, LocaleUtils.getLocale()), stories);
//	}
//	
//	@RequestMapping( value = "/approval/receive", method = RequestMethod.GET, headers = { "Accept=application/json" })
//	@ResponseBody
//	public JsonResult receive(
//			@ParseSunny(shouldExistsSite=false) Sunny sunny,
//			@AuthUser SecurityUser securityUser, 
//			@RequestParam("contentId") Long contentId,  
//			@RequestParam(value="q[]", required=false) String[] queries,
//			@RequestParam(value="top", required=false) Boolean top, 
//			@RequestParam(value="size", required=false) Integer size){
//	
//		return receive( null, sunny, securityUser, contentId, queries, top, size);
//	}
//	
//	
//	
//	
//	@RequestMapping( value = "/{path}/approval/circulation", method = RequestMethod.GET, headers = { "Accept=application/json" })
//	@ResponseBody
//	public JsonResult circulation(
//			@PathVariable("path") String path,
//			@ParseSunny(shouldExistsSite=false) Sunny sunny,
//			@AuthUser SecurityUser securityUser, 
//			@RequestParam("contentId") Long contentId,  
//			@RequestParam(value="q[]", required=false) String[] queries,
//			@RequestParam(value="top", required=false) Boolean top, 
//			@RequestParam(value="size", required=false) Integer size){
//	
//		LoginUtils.checkLogin(securityUser);
//		
//		
//		Stream stream;
//		
//		if(top == null)
//			top = true;
//		
//		if( contentId == null )
//			stream = new Stream();
//		else
//			stream = new Stream(top == true ? true : false, "this.id", contentId, size);
//		
//		User user = userService.findById( securityUser.getUserId() );
//		List<ContentDTO> stories = contentService.fetchApprovals(sunny, null, false, null, null, user, user, SmallGroupApproval.TYPE_CIRCULATION, queries, stream);
//			
//		if( stories == null ){
//			return new JsonResult(false, messageSource.getMessage("story.noStories", null, LocaleUtils.getLocale()), null);
//		}
//
//		return new JsonResult(true, messageSource.getMessage("story.successFetchStories", null, LocaleUtils.getLocale()), stories);
//	}
//	
//	@RequestMapping( value = "/approval/circulation", method = RequestMethod.GET, headers = { "Accept=application/json" })
//	@ResponseBody
//	public JsonResult circulation(
//			@ParseSunny(shouldExistsSite=false) Sunny sunny,
//			@AuthUser SecurityUser securityUser, 
//			@RequestParam("contentId") Long contentId,  
//			@RequestParam(value="q[]", required=false) String[] queries,
//			@RequestParam(value="top", required=false) Boolean top, 
//			@RequestParam(value="size", required=false) Integer size){
//	
//		return circulation( null, sunny, securityUser, contentId, queries, top, size);
//	}
//	
	
	@RequestMapping(value = "/{path}/approval/users", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult approvalUsers(
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@RequestParam(value = "id", required = true) Long approvalId,
			@RequestParam(value = "sgaid", required = false) Long smallGroupApprovalId,
			@RequestParam(value = "types[]", required = false) Integer[] types,
			@RequestParam(value = "top", required = false) Boolean top,
			@RequestParam(value = "size", required = false) Integer size) {

		Stream stream;

		if (top == null)
			top = true;

		if (smallGroupApprovalId == null)
			stream = new Stream();
		else
			stream = new Stream(top == true ? true : false, "id", smallGroupApprovalId,
					size);
		
		List<SmallGroupApproval> smallGroupApprovals = smallGroupApprovalService.getUsers(approvalId, stream, types);

		// if( feelUsers.size() == 0 ){
		// return new JsonResult(true, "평가한 사용자가 없습니다.", null);
		// }

		return new JsonResult(true, "유저 리스트를 가져왔습니다.", smallGroupApprovals);

	}
	@RequestMapping(value = "/approval/users", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult approvalUsers(
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@RequestParam(value = "id", required = true) Long approvalId,
			@RequestParam(value = "sgaid", required = false) Long smallGroupApprovalId,
			@RequestParam(value = "types[]", required = false) Integer[] types,
			@RequestParam(value = "top", required = false) Boolean top,
			@RequestParam(value = "size", required = false) Integer size) {

		return approvalUsers(null, sunny, approvalId, smallGroupApprovalId, types, top, size);
	}

	@RequestMapping(value = "/approval/ok", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult approve( 
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@RequestParam(value = "aid", required = false) Long approvalId,
			@RequestParam(value = "sgaid", required = false) Long smallGroupApprovalId,
			@AuthUser SecurityUser securityUser ){

		LoginUtils.checkLogin(securityUser);
		
		approvalService.ok( sunny, approvalId, smallGroupApprovalId, securityUser.getUserId() );
		
		return new JsonResult(true, "success", null);
	}
	@RequestMapping(value = "/approval/reject", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult reject( 
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@RequestParam(value = "aid", required = false) Long approvalId,
			@RequestParam(value = "sgaid", required = false) Long smallGroupApprovalId,
			@AuthUser SecurityUser securityUser ){

		LoginUtils.checkLogin(securityUser);
		
		approvalService.reject( sunny, approvalId, smallGroupApprovalId, securityUser.getUserId() );
		
		return new JsonResult(true, "success", null);
	}
	
	@RequestMapping(value = "/approval/request_reject", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult requestReject( 
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@RequestParam(value = "aid", required = false) Long approvalId,
			@AuthUser SecurityUser securityUser ){

		LoginUtils.checkLogin(securityUser);
		
		approvalService.requestReject( approvalId, securityUser.getUserId() );
		
		return new JsonResult(true, "success", null);
	}
	@RequestMapping(value = "/approval/cancel_request_reject", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult cancelRequestReject( 
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@RequestParam(value = "aid", required = false) Long approvalId,
			@AuthUser SecurityUser securityUser ){

		LoginUtils.checkLogin(securityUser);
		
		approvalService.cancelRequestReject( approvalId, securityUser.getUserId() );
		
		return new JsonResult(true, "success", null);
	}
	
}
