package kr.co.sunnyvale.sunny.controller.web;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kr.co.sunnyvale.sunny.annotation.AuthUser;
import kr.co.sunnyvale.sunny.annotation.NoPathRedirect;
import kr.co.sunnyvale.sunny.annotation.ParseSunny;
import kr.co.sunnyvale.sunny.domain.Channel;
import kr.co.sunnyvale.sunny.domain.ChannelUser;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.redis.ConnectMember;
import kr.co.sunnyvale.sunny.redis.RedisPublisher;
import kr.co.sunnyvale.sunny.service.ChannelService;
import kr.co.sunnyvale.sunny.service.MessageService;
import kr.co.sunnyvale.sunny.service.SiteService;
import kr.co.sunnyvale.sunny.service.UserService;
import kr.co.sunnyvale.sunny.service.impl.ChannelUserService;
import kr.co.sunnyvale.sunny.util.LoginUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;



@Controller
public class MessageController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private ChannelService channelService;
	
	@Autowired
	private RedisPublisher redisPublisher;
	
	@Autowired
	private SiteService siteService;	
	
	@Autowired
	private ChannelUserService channelUserService;
	
	
	
	/*
	 * ********************************************************
	 * MultiSite
	 * *********************************************************
	 */
	
	@RequestMapping( value="/{path}/message/channels", method=RequestMethod.GET, headers={ "Accept=text/html" } )
	public ModelAndView chatIndex( 
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="ordering", required=false) String ordering,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) Boolean isPagelet ){

		LoginUtils.checkLogin(securityUser);
		
		ModelAndView modelAndView = new ModelAndView();
		if( sunny.isGoRedirect() == true ){
			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
			return modelAndView;
		}
		
		// 좌측 프로필 정보
//		User user = userService.findById(securityUser.getUserId());
		
		Page<Channel> pagedResult = channelService.getChannels(new User(securityUser.getUserId()), page, Page.DEFAULT_CHANNEL_SIZE);
		modelAndView.addObject( "pagedResult", pagedResult );
		
		if( isPagelet != null && isPagelet == true ){
			modelAndView.setViewName("/pagelet/message/channels");
		}else{
			modelAndView.setViewName("/message/channels");	
		}
				
		return modelAndView;
	}

	/*
	 * MultiSiteProxy
	 */
//	@NoPathRedirect
	@RequestMapping( value="/message/channels", method=RequestMethod.GET, headers={ "Accept=text/html" } )
	public ModelAndView chatIndex( 
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="ordering", required=false) String ordering,
			@RequestParam(value="page", required=false) Integer page,
			@RequestParam(value="pl", required=false) Boolean isPagelet){

		return chatIndex( null, sunny, securityUser, ordering, page, isPagelet);
		
	}
	@RequestMapping( value="/{path}/message/connectUsers", method=RequestMethod.GET, headers={ "Accept=text/html" } )
	public ModelAndView connectUsers( 
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser ){


		LoginUtils.checkLogin(securityUser);
		ModelAndView modelAndView = new ModelAndView();
		if( sunny.isGoRedirect() == true ){
			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
			return modelAndView;
		}
		// 좌측 프로필 정보
		User user = userService.findById(securityUser.getUserId());
		
		final String domain = sunny.getServerName();
		
		Set<ConnectMember> connectUsers = redisPublisher.getConnectUserList(domain);
		
		modelAndView.addObject( "connectUsers", connectUsers );
		modelAndView.addObject( "user", user );
		modelAndView.setViewName("/message/connectUsers");		
		return modelAndView;
	}
	
	@RequestMapping( value="/{path}/message/{channelId}", method=RequestMethod.GET, headers={ "Accept=text/html" } )
	public ModelAndView chatEntryChannel( 
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@PathVariable( "channelId" ) String strChannelId ) throws NoSuchRequestHandlingMethodException{

		LoginUtils.checkLogin(securityUser);
		Long channelId = null;
		try{
			channelId = Long.parseLong(strChannelId);
		}catch( Exception ex){
			throw new NoSuchRequestHandlingMethodException("chatEntryChannel", MessageController.class );
		}
		ModelAndView modelAndView = new ModelAndView();
		if( sunny.isGoRedirect() == true ){
			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
			return modelAndView;
		}

		User user = userService.findById(securityUser.getUserId());

		ChannelUser channelUser = channelUserService.findByUserChannel(user.getId(), channelId);
		if( channelUser == null ){
			modelAndView.setViewName("redirect:/message/channels");		
			return modelAndView;	
		}
		
		
		Channel channel = channelService.getChannel(channelId);
		
		modelAndView.addObject( "channel", channel);
		//modelAndView.addObject( "messages", messages );
		modelAndView.addObject( "user", user );
		modelAndView.setViewName("/message/inside");		
		return modelAndView;
	}
	

//	@RequestMapping( value="/{path}/message/create", method=RequestMethod.POST, headers={ "Accept=text/html" } )
//	public ModelAndView chatCreateChannel(
//			@PathVariable("path") String path, 
//			@ParseSunny(shouldExistsSite=true) Sunny sunny,
//			@AuthUser SecurityUser securityUser, 
//			@RequestParam(value="userIds", required=true) List<Long> userIds){
//		
//		LoginUtils.checkLogin(securityUser);
//		
//		ModelAndView modelAndView = new ModelAndView();
//		if( sunny.isGoRedirect() == true ){
//			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
//			return modelAndView;
//		}
//		User user = userService.findById(securityUser.getUserId());
//		userIds.add(securityUser.getUserId());
//		
//		Channel channel = channelService.createChannel(sunny, user, new HashSet<Long>(userIds));
//		
//		modelAndView.addObject( "user", user );
//		modelAndView.setViewName("redirect:/message/" + channel.getId());
//		return modelAndView;
//	}

	
	@NoPathRedirect
	@RequestMapping( value="/message/connectUsers", method=RequestMethod.GET, headers={ "Accept=text/html" } )
	public ModelAndView connectUsers(
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser ){

		return connectUsers( null, sunny, securityUser );
	}
	
	@NoPathRedirect
	@RequestMapping( value="/message/{channelId}", method=RequestMethod.GET, headers={ "Accept=text/html" } )
	public ModelAndView chatEntryChannel( 
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@PathVariable( "channelId" ) String strChannelId ) throws NoSuchRequestHandlingMethodException{
		
		return chatEntryChannel( null, sunny, securityUser, strChannelId );
	}
	
//
//	@NoPathRedirect
//	@RequestMapping( value="/message/create", method=RequestMethod.POST, headers={ "Accept=text/html" } )
//	public ModelAndView chatCreateChannel( 
//			@ParseSunny(shouldExistsSite=true) Sunny sunny,
//			@AuthUser SecurityUser securityUser, 
//			@RequestParam(value="userIds", required=true) List<Long> userIds){
//
//
//		return chatCreateChannel( null, sunny, securityUser, userIds );
//	}
	
}
