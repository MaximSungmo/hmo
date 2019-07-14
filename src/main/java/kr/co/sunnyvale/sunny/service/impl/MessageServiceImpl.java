package kr.co.sunnyvale.sunny.service.impl;

import java.util.Date;
import java.util.List;

import kr.co.sunnyvale.sunny.domain.Channel;
import kr.co.sunnyvale.sunny.domain.ChannelUser;
import kr.co.sunnyvale.sunny.domain.Message;
import kr.co.sunnyvale.sunny.domain.MessageInfo;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.dto.MessageDTO;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.redis.RedisPublisher;
import kr.co.sunnyvale.sunny.redis.notify.MessageNotify;
import kr.co.sunnyvale.sunny.redis.notify.dto.NotifyDataDTO;
import kr.co.sunnyvale.sunny.repository.hibernate.LastReadRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.MessageInfoRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.MessageRepository;
import kr.co.sunnyvale.sunny.service.ChannelService;
import kr.co.sunnyvale.sunny.service.MessageInfoService;
import kr.co.sunnyvale.sunny.service.MessageService;
import kr.co.sunnyvale.sunny.service.SiteService;
import kr.co.sunnyvale.sunny.service.UserService;
import kr.co.sunnyvale.sunny.util.HtmlUtil;
import kr.co.sunnyvale.sunny.util.StringEscapeUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

@Service( value="channelMessageService" )
@Transactional
public class MessageServiceImpl implements MessageService{

	@Autowired
	private MessageInfoService messageInfoService;

	@Autowired
	private ChannelUserService channelUserService;
	
	@Autowired
	private MessageRepository messageRepository;
	
	@Autowired
	private UserService userService; 
	
	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ChannelService channelService;
	
	@Autowired
	private SiteService siteService;
	
	@Autowired
	private LastReadRepository lastReadRepository;

	@Autowired
	private MessageInfoRepository messageInfoRepository;
	
    @Autowired
    private RedisPublisher redisPublisher;
	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.MessageService#save(kr.co.sunnyvale.sunny.domain.Message)
	 */
	@Override
	@Transactional
	public void save(Message message){
		messageRepository.save(message);
	}
	
	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.MessageService#update(kr.co.sunnyvale.sunny.domain.Message)
	 */
	@Override
	@Transactional
	public void update(Message message){
		messageRepository.update(message);
	}
	
	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.MessageService#find(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public Message find(Long messageId){
		return messageRepository.select(messageId);
	}

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.MessageService#removeChannelMessages(java.lang.Long, java.lang.Long)
	 */
	@Override
	@Transactional
	public void removeChannelMessages(Long channelId, Long userId) {
		messageRepository.removeChannelMessages( channelId, userId );
	}
	
	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.MessageService#getMessages(kr.co.sunnyvale.sunny.domain.extend.Sunny, kr.co.sunnyvale.sunny.domain.User, java.lang.Boolean, java.lang.Long, java.lang.Boolean, kr.co.sunnyvale.sunny.domain.extend.Stream)
	 */
	@Override
	@Transactional//(isolation=Isolation.SERIALIZABLE)
	public List<Message> getMessages(Sunny sunny, User user, Boolean unread, Long lastReadMessageInfoId, final Long channelId, Boolean updateLastDate, Stream stream) {
		
		ChannelUser channelUser = channelUserService.findAndUpdateMessageInfo(sunny, user.getId(), channelId, lastReadMessageInfoId, updateLastDate);

		List<Message> messages = null;

		if( unread == null || unread == false){
			messages = messageRepository.getMessages(user, channelId, null, stream);
		}else{
			messages = messageRepository.getMessages(user, channelId, lastReadMessageInfoId, stream);
		}
		
		if( channelUser.isUpdateFlag() && messages != null && messages.size() > 0 ){
			List<Long> joinUserIds = channelUserService.getJoinUserIds(channelId);
			int currentUserIndex =joinUserIds.indexOf( user.getId() );
			if(  currentUserIndex > -1 ){
				joinUserIds.remove(currentUserIndex);
			}
			Long[] strTmp =(Long[]) joinUserIds.toArray(new Long[joinUserIds.size()]);
			redisPublisher.publish(new MessageNotify(channelId, NotifyDataDTO.TYPE_MESSAGE_READ, user.getId(), messages.get(0).getInfo().getId(), strTmp));
		}
		
		return messages;
	}
	
	@Override
	@Transactional
	public void readMessages(Sunny sunny, Long channelId, Long userId, Long prevReadMessageInfoId, Long lastReadMessageInfoId) {
		
		
		messageInfoRepository.readMessages( channelId, prevReadMessageInfoId, lastReadMessageInfoId );
		//List<Long> joinUserIds = channelUserService.getJoinUserIds(  channelId );
//		int i = 10; 
//		 try {
//		      Thread.sleep(i * 1000);
//		    } catch (InterruptedException e) { }
//		
		
//		List<Long> joinUserIds = channelUserService.getJoinUserIds(channelId);
//		int currentUserIndex =joinUserIds.indexOf( userId );
//		if(  currentUserIndex > -1 ){
//			joinUserIds.remove(currentUserIndex);
//		}
//		Long[] strTmp =(Long[]) joinUserIds.toArray(new Long[joinUserIds.size()]);
//		redisPublisher.publish(new ChatMessage(sunny.getSite().getId(), channelId, MessageNotifyDTO.TYPE_READ, prevReadMessageInfoId, lastReadMessageInfoId, strTmp));

		lastReadRepository.updateMessageDate(new User(userId));
		
	}

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.MessageService#getCreateDate(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public Date getCreateDate(Long messageId) {
		return messageRepository.getCreateDate(messageId);
	}

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.MessageService#save(kr.co.sunnyvale.sunny.domain.extend.Sunny, kr.co.sunnyvale.sunny.domain.User, kr.co.sunnyvale.sunny.domain.MessageInfo)
	 */
	@Override
	@Transactional
	public Message save(Sunny sunny, User user, MessageDTO messageDto) {
		
		MessageInfo messageInfo = new MessageInfo();
		messageInfo.setChannel( new Channel( messageDto.getChannelId()));
		messageInfo.setText(messageDto.getText());
		
		if(messageDto.getText() != null){
			messageInfo.setRawText( messageDto.getText() );
			messageInfo.setTaggedText( HtmlUtil.parseMessageRawText(messageDto.getText()).getTaggedTextPrev() );
		}
		
		List<Long> joinUserIds = channelUserService.getJoinUserIds(  messageInfo.getChannel().getId() );

		Message retMessage = null;
		messageInfo.setUnreadCount(joinUserIds.size());
		messageInfo.setSender(user);
		messageInfoService.save( messageInfo );
		
		Channel channel = channelService.getChannel( messageInfo.getChannel().getId() );
		
		if( channel.isHidden() == true ){
			channel.setHidden( false );
		}
		
		
		int textLength = messageInfo.getRawText().length();
		
		channel.setLastTextSnippet(messageInfo.getRawText().substring(0, textLength < 50 ? textLength : 50));
		
		if( channel.isExistInvitedUser() ){
			channelUserService.cancleHiddenAll( channel.getId() );
			channel.setExistInvitedUser(false);
		}
		
		String escapeString = StringEscapeUtils.escapeHtml( channel.getLastTextSnippet() ) ;
		channel.setLastEscapeHTMLSnippet( escapeString );
		channel.setLastUserName( user.getName() );
		channel.setLastUserProfilePic( user.getProfilePic() );
		channel.setUpdateDate( new Date() );
		channelService.update(channel);
		
		
		/**
		 * 1:1 방의 경우 만약 상대방이 숨김으로 해놨으니 복구시킨다. 
		 */
		if( channel.getType() == Channel.ONETOONE ){
			channelUserService.cancleHiddenAll( channel.getId() );
//			for( Long userIdEach: joinUserIds ){
//				if( userIdEach.equals(user.getId()) ){
//					continue;
//				}
//				channelUserService.cancleHidden(channel.getId(), userIdEach);	
//			}
			
		}
		
		/*
		 * 채팅방에 있는 모든 사람들에게 메시지를 보냄
		 */
//		List<Long> joinUserIdsExceptMe = new ArrayList<Long>();
		for( Long userId : joinUserIds ){
			Message message = new Message();
			message.setUser( new User( userId ) );
			message.setInfo(messageInfo);
			message.setChannel( channel );
			messageRepository.save(message);
			if( userId.equals( user.getId() ) ){
				retMessage = message;
			}
//			else{
//				joinUserIdsExceptMe.add(userId);
//			}
		}

		// 채널 유저 테스트
//		channelUserService.updateLastReadToLastMessageInfo(user.getId(), channel.getId(), messageInfo.getId() );
//
//		// 글 쓴 사람은 당연히 최신글을 읽은 것이기 때문에 요렇게.
//		lastReadRepository.updateMessageDate(user);
		

		//String[] strTmp =(String[]) joinUserIdsExceptMe.toArray(new String[joinUserIdsExceptMe.size()]);
		Long[] strTmp =(Long[]) joinUserIds.toArray(new Long[joinUserIds.size()]);
		redisPublisher.publish(new MessageNotify(channel.getId(), NotifyDataDTO.TYPE_MESSAGE_SEND, user.getId(), strTmp));
        
        return retMessage;
	}
	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.MessageService#systemSave(kr.co.sunnyvale.sunny.domain.extend.Sunny, kr.co.sunnyvale.sunny.domain.Channel, int, kr.co.sunnyvale.sunny.domain.User, kr.co.sunnyvale.sunny.domain.User)
	 */
	@Override
	@Transactional
	public MessageInfo systemSave(Sunny sunny, Channel channel, int type, User subjectUser, Long ... objectUserIds) {
		
		MessageInfo messageInfo = new MessageInfo();
		messageInfo.setChannel( channel );
		messageInfo.setType(type);
		messageInfo.setSubjectUser(subjectUser);
		
		if( objectUserIds != null && objectUserIds.length > 0 ){
			
			List<User> objectUsers = Lists.newArrayList();
			
			StringBuilder userNames = new StringBuilder();
			
			boolean first = true;
			for( Long objectUserId : objectUserIds ){
				if( objectUserId.equals( subjectUser.getId() ) ){
					continue;
				}
				
				User user = userService.findById(objectUserId);
				objectUsers.add(user);
				
				if( first == true ){
					userNames.append(user.getName());
					first = false; 
				}else{
					userNames.append(", " + user.getName());
				}
			}
			messageInfo.setRawText(userNames.toString());
		
			messageInfo.setObjectUsers(objectUsers);
		}
		
		messageInfoService.save( messageInfo );
		
		
		
		/*
		 * 채팅방에 있는 모든 사람들에게 메시지를 보냄
		 */
		List<Long> joinUserIds = channelUserService.getJoinUserIds(  messageInfo.getChannel().getId() );
		
		for( Long userId : joinUserIds ){
			Message message = new Message();
			message.setUser( new User( userId ) );
			message.setInfo(messageInfo);
			message.setChannel( channel );
			messageRepository.save(message);
		}
		// 시스템 메시지는 가지 않는다. 
		if( type == Message.TYPE_CREATE ){
			return messageInfo;
		}
		Long[] strTmp =(Long[]) joinUserIds.toArray(new Long[joinUserIds.size()]);
		redisPublisher.publish(new MessageNotify(channel.getId(), NotifyDataDTO.TYPE_MESSAGE_SEND, subjectUser.getId(), strTmp));
		return messageInfo;
	}

	@Override
	public Long getLastMessageInfoId(Long channelId) {
		return messageRepository.getLastMessageInfoId( channelId );
	}
}

