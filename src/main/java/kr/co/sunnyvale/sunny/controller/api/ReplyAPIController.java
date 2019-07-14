package kr.co.sunnyvale.sunny.controller.api;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import kr.co.sunnyvale.sunny.annotation.AuthUser;
import kr.co.sunnyvale.sunny.annotation.ParseSunny;
import kr.co.sunnyvale.sunny.domain.ActivityType;
import kr.co.sunnyvale.sunny.domain.Reply;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.dto.JsonResult;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.domain.post.ReplyPost;
import kr.co.sunnyvale.sunny.domain.post.StoryPostDTO;
import kr.co.sunnyvale.sunny.exception.SimpleSunnyException;
import kr.co.sunnyvale.sunny.redis.RedisPublisher;
import kr.co.sunnyvale.sunny.redis.notify.NotiNotify;
import kr.co.sunnyvale.sunny.repository.hibernate.ContentService;
import kr.co.sunnyvale.sunny.service.FeelService;
import kr.co.sunnyvale.sunny.service.NotificationService;
import kr.co.sunnyvale.sunny.service.ReceiverRelationService;
import kr.co.sunnyvale.sunny.service.ReplyService;
import kr.co.sunnyvale.sunny.service.SiteService;
import kr.co.sunnyvale.sunny.service.StoryService;
import kr.co.sunnyvale.sunny.service.UserService;
import kr.co.sunnyvale.sunny.util.LocaleUtils;
import kr.co.sunnyvale.sunny.util.LoginUtils;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * {SpringSecurity}
 * 
 * 
 * @author kickscar
 * 
 */
@Controller
public class ReplyAPIController {

	@Autowired
	private StoryService storyService;

	@Autowired
	private UserService userService;

	@Autowired
	private SiteService siteService;

	@Autowired
	private FeelService feelService;

	@Autowired
	private ReplyService replyService;

	@Autowired
	private ContentService contentService;

	@Autowired
	private ReceiverRelationService receiverRelationService;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private RedisPublisher redisPublisher;

	/*
	 * *************************************************
	 * MultiSite *************************************************
	 */
	@RequestMapping(value = "/{path}/reply/delete", method = RequestMethod.GET, headers = { "Accept=application/json;charset=utf-8"})
	@ResponseBody
	public JsonResult delete(
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value = "id", required = false) Long replyId,
			HttpServletRequest request) {

		LoginUtils.checkLogin(securityUser);

		Reply reply = replyService.select(replyId);

		if (reply == null) {
			return new JsonResult(false, messageSource.getMessage("reply.notFound", null, LocaleUtils.getLocale()), null);
		}

		replyService.delete(reply);

		return new JsonResult(true, "success", null);
	}

	@RequestMapping(value = "/{path}/reply/post", method = RequestMethod.POST, headers={"Accept=application/json;charset=utf-8"})
	@ResponseBody
	public JsonResult replyPost(
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			HttpServletRequest request) {
		
		LoginUtils.checkLogin(securityUser);
		
		User user = userService.findById(securityUser.getUserId());

		String requestBody = null;
		ReplyPost replyPost = null;
		try{
			requestBody = IOUtils.toString(request.getInputStream());
			ObjectMapper mapper = new ObjectMapper();
			replyPost = mapper.readValue(requestBody, ReplyPost.class);
			replyPost.setRequestBody(requestBody);
		}catch(IOException ex){
			ex.printStackTrace();
			throw new SimpleSunnyException();
		}
		
		replyPost.setIpAddress(request.getRemoteAddr());
		replyPost.setUser(user);
		Reply reply = replyService.save(sunny, replyPost);
		Set<String> userIds = receiverRelationService
				.getUserIdsFromReplyNoti(reply);
		
//		final String receiveUserString = StringUtils.getRedisUserIds(userIds);
		Long[] strTmp =(Long[]) userIds.toArray(new Long[userIds.size()]);
		if( strTmp != null && strTmp.length > 0 )
			redisPublisher.publish(new NotiNotify(user.getId(), replyPost.getContentId(), ActivityType.COMMENT, strTmp));

		if (reply.getId() != 0) {
			return new JsonResult(true, "success", reply);
		} else {
			return new JsonResult(true, messageSource.getMessage("reply.failPost", null, LocaleUtils.getLocale()), reply);
		}
	}

	@RequestMapping(value = "/{path}/reply/stream", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult replyStream(
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam("contentId") Long contentId,
			@RequestParam(value = "top", required = false) Boolean top,
			@RequestParam(value = "replyId", required = false) Long replyId,
			@RequestParam(value = "size", required = false) Integer size) {

		Stream stream;

		if (top == null)
			top = true;

		if (replyId == null)
			stream = new Stream();
		else
			stream = new Stream(top == true ? false : true, "createDate",
					replyService.getCreateDate(replyId), size);

		User user = null;
		if( securityUser != null)
			user = new User(securityUser.getUserId());
		
		List<Reply> replys = replyService.getReplyList(user, contentId,
				stream);

		return new JsonResult(true, "success", replys);
	}

	/*
	 * MultiSiteProxy
	 */

	@RequestMapping(value = "/reply/delete", method = RequestMethod.GET, headers = { "Accept=application/json;charset=utf-8"})
	@ResponseBody
	public JsonResult delete(
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value = "id", required = false) Long replyId,
			HttpServletRequest request) {
		return delete(null, sunny, securityUser, replyId, request);
	}

	@RequestMapping(value = "/reply/post", method = RequestMethod.POST, headers = { "Accept=application/json"})
	@ResponseBody
	public JsonResult replyPost(
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			HttpServletRequest request) {
		return replyPost(null, sunny, securityUser, request);
	}

	@RequestMapping(value = "/reply/stream", method = RequestMethod.GET, headers = { "Accept=application/json;charset=utf-8"})
	@ResponseBody
	public JsonResult replyStream(
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam("contentId") Long contentId,
			@RequestParam(value = "top", required = false) Boolean top,
			@RequestParam(value = "replyId", required = false) Long replyId,
			@RequestParam(value = "size", required = false) Integer size) {
		return replyStream(null, sunny, securityUser, contentId, top, replyId, size);
	}
}
