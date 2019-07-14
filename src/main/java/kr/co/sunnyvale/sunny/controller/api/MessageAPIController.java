package kr.co.sunnyvale.sunny.controller.api;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import kr.co.sunnyvale.sunny.annotation.AuthUser;
import kr.co.sunnyvale.sunny.annotation.ParseSunny;
import kr.co.sunnyvale.sunny.domain.Channel;
import kr.co.sunnyvale.sunny.domain.ChannelUser;
import kr.co.sunnyvale.sunny.domain.Message;
import kr.co.sunnyvale.sunny.domain.MessageInfo;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.dto.JsonResult;
import kr.co.sunnyvale.sunny.domain.dto.MessageDTO;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.service.ChannelService;
import kr.co.sunnyvale.sunny.service.FriendService;
import kr.co.sunnyvale.sunny.service.MessageService;
import kr.co.sunnyvale.sunny.service.UserService;
import kr.co.sunnyvale.sunny.service.impl.ChannelUserService;
import kr.co.sunnyvale.sunny.util.LocaleUtils;
import kr.co.sunnyvale.sunny.util.LoginUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

/**
 * {SpringSecurity}
 * 
 * 
 * @author kickscar
 *
 */
@Controller
public class MessageAPIController {
	
	@Autowired
	private UserService userService;

	@Autowired
	private FriendService friendRequestService;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private ChannelUserService channelUserService;

	@Autowired
	private ChannelService channelService;
	
	@Autowired
	private MessageSource messageSource;
	
	/*
	 * *************************************************
	 * MultiSite
	 * *************************************************
	 */
	@RequestMapping( value = "/{path}/message/{cid}", method = RequestMethod.GET, headers = { "content-type=application/json,application/xml"})
	@ResponseBody
	public JsonResult chatEntryChannel(
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@PathVariable("cid") String strChannelId,
			@RequestParam(value = "top", required = false) Boolean top,
			@RequestParam(value = "ur", required = false) Boolean unread,
			@RequestParam(value = "msgId", required = false) Long messageInfoId,
			@RequestParam(value = "size", required = false) Integer size)
			throws NoSuchRequestHandlingMethodException {

		LoginUtils.checkLogin(securityUser);
		Long channelId = null;
		try{
			channelId = Long.parseLong(strChannelId);
		}catch( Exception ex){
			throw new NoSuchRequestHandlingMethodException("chatEntryChannel", MessageAPIController.class );
		}
		Stream stream;
		
		if(top == null) 
			top = true;
		
		if( messageInfoId == null ){
			stream = new Stream(null, null, null, 30);
		}else{
			stream = new Stream( top == true ? false : true, "infoAlias.id",  messageInfoId , size);
		}
		User user = null; 
		if( securityUser != null ){
			user = new User( securityUser.getUserId() );
		}
		
		Boolean updateLastDate = false;
		if( top == null || top == false ){
			updateLastDate = true; 
		}
		/*
		 * 기준점이 없으면 현재 기준의 메시지 가져오기
		 */
		if( messageInfoId == null )
			updateLastDate = true; 
		
		/**
		 * 기존 코드가 트랜잭션이 너무 길게 잡혀있어서 짧게 변경함
		 * 1. 마지막 메시지를 가져온다.
		 * 2. 해당 유저가 이전에 현재 채널에서 이번엔 어디서부터 어디까지 읽었는지를 가져온다. 
		 *    만약 이전에 읽은 곳 보다 최신 메시지가 있다면 update 한다. 
		 */
		
		Long lastMessageInfoId = messageService.getLastMessageInfoId( channelId );
		List<Message> messages = null;
		//ChannelUser channelUser = channelUserService.findAndUpdateMessageInfo(sunny, user.getId(), channelId, lastMessageInfoId, updateLastDate);

		
		messages = messageService.getMessages(sunny, user, unread, lastMessageInfoId, channelId, updateLastDate, stream);
		
		
		
		return new JsonResult(true, messageSource.getMessage("message.successGetMessages", null, LocaleUtils.getLocale()), messages);

	}
	
	
	@RequestMapping( value = "/{path}/message/channels", method = RequestMethod.GET, headers = { "Accept=application/json", "content-type=application/json,application/xml"})
	@ResponseBody
	public JsonResult chatChannels(
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="ordering", required=false) String ordering,
			@RequestParam(value="page", required=false) Integer page){

		LoginUtils.checkLogin(securityUser);
//		User user = userService.findById(securityUser.getUserId());
		Page<Channel> pagedResult = null;
		if( page != null && page == -1 ){
			// -1 로 들어오면 다 줌.
			pagedResult = channelService.getChannels(new User(securityUser.getUserId()), 0, 10000);
		}else{
			pagedResult = channelService.getChannels(new User(securityUser.getUserId()), page, Page.DEFAULT_CHANNEL_SIZE);
		}


		return new JsonResult(true, messageSource.getMessage("message.successGetChannels", null, LocaleUtils.getLocale()), pagedResult);
	}
	
	
	@RequestMapping( value = "/{path}/message/followers", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult chatFollowerList(
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser){
		

		LoginUtils.checkLogin(securityUser);
		// TODO : 메시지 안에서 팔로워 리스트 구현?? 머지 이건?
		return new JsonResult(true, messageSource.getMessage("message.successGetFollowers", null, LocaleUtils.getLocale()), null);
	}
	

	@RequestMapping( value = "/message/readers", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult getMessageReaders(
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@RequestParam(value = "channelId", required = false) Long channelId,
			@AuthUser SecurityUser securityUser){
		

		LoginUtils.checkLogin(securityUser);

		List<ChannelUser> channelUsers = channelUserService.getLastReadUsers(sunny, channelId, securityUser.getUserId());
		
		
		return new JsonResult(true, messageSource.getMessage("message.successGetFollowers", null, LocaleUtils.getLocale()), channelUsers);
	}
	
	@RequestMapping( value= "/{path}/message/create", method=RequestMethod.POST, headers = { "content-type=application/json,application/xml"} )
	@ResponseBody
	public JsonResult chatCreateChannel(
			@PathVariable("path") String path, 
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestBody HashMap<String, List<Long>> body ){

		LoginUtils.checkLogin(securityUser);

		List<Long> userIds = body.get("userIds");
		
		User user = userService.findById(securityUser.getUserId());

		userIds.add(securityUser.getUserId());
		
		if( userIds == null || userIds.size() < 1 ){
			return new JsonResult(false, messageSource.getMessage("message.failCreateMessageNoUsers", null, LocaleUtils.getLocale()), null);
		}
		
		Channel channel = channelService.createChannel(sunny, user, new HashSet<Long>(userIds));

		return new JsonResult(true, messageSource.getMessage("message.successCreate", null, LocaleUtils.getLocale()), channel);
	}
	
	@RequestMapping( value= "/{path}/message/send", method=RequestMethod.POST, headers = { "content-type=application/json,application/xml"} )
	@ResponseBody
	public JsonResult chatSaveMessage( 
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestBody MessageDTO messageDto ){

		User user = userService.findById(securityUser.getUserId());
		
		Message message = messageService.save( sunny, user, messageDto );

		return new JsonResult(true, messageSource.getMessage("message.successSend", null, LocaleUtils.getLocale()), message);
	}

	
	/*
	 * MultiSiteProxy
	 * 
	 */
	
	@RequestMapping( value = "/message/{cid}", method = RequestMethod.GET, headers = { "content-type=application/json,application/xml"})
	@ResponseBody
	public JsonResult chatEntryChannel(

			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@PathVariable("cid") String strChannelId,
			@RequestParam(value = "top", required = false) Boolean top,
			@RequestParam(value = "ur", required = false) Boolean unread,
			@RequestParam(value = "msgId", required = false) Long messageId,
			@RequestParam(value = "size", required = false) Integer size										
			) throws NoSuchRequestHandlingMethodException{

		return chatEntryChannel( null, sunny, securityUser, strChannelId, top, unread, messageId, size);
	}
	
	
	@RequestMapping( value = "/message/channels", method = RequestMethod.GET, headers = { "Accept=application/json", "content-type=application/json,application/xml"})
	@ResponseBody
	public JsonResult chatChannels(
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="ordering", required=false) String ordering,
			@RequestParam(value="page", required=false) Integer page){
		return chatChannels( null, sunny, securityUser, ordering, page);
	}
	
	
	@RequestMapping( value = "/message/followers", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult chatFollowerList(
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser){
		return chatFollowerList( null, sunny, securityUser);
	}
	
	@RequestMapping( value= "/message/create", method=RequestMethod.POST, headers = { "content-type=application/json,application/xml"} )
	@ResponseBody
	public JsonResult chatCreateChannel(
			@ParseSunny(shouldExistsSite=false) Sunny sunny, 
			@AuthUser SecurityUser securityUser, 
			@RequestBody HashMap<String, List<Long>> body ){
		return chatCreateChannel( null, sunny, securityUser, body);
	}
	
	@RequestMapping( value= "/message/send", method=RequestMethod.POST, headers = { "content-type=application/json,application/xml"} )
	@ResponseBody
	public JsonResult chatSaveMessage(
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestBody MessageDTO messageDto ){
		return chatSaveMessage( null, sunny, securityUser, messageDto);
	}

	
}
