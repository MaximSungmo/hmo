package kr.co.sunnyvale.sunny.service.impl;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.Channel;
import kr.co.sunnyvale.sunny.domain.ChannelUser;
import kr.co.sunnyvale.sunny.domain.Message;
import kr.co.sunnyvale.sunny.domain.MessageInfo;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.dto.ChannelUserDTO;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.exception.SimpleSunnyException;
import kr.co.sunnyvale.sunny.repository.hibernate.ChannelUserRepository;
import kr.co.sunnyvale.sunny.service.ChannelService;
import kr.co.sunnyvale.sunny.service.MessageInfoService;
import kr.co.sunnyvale.sunny.service.MessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( value="channelUserService" )
@Transactional
public class ChannelUserService{
	
	@Autowired
	private ChannelUserRepository channelUserRepository;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private ChannelService channelService;
	
	@Transactional
	public void save(ChannelUser channelUser){
		channelUserRepository.save(channelUser);
	}
	
	@Transactional
	public void update(ChannelUser channelUser){
		channelUserRepository.update(channelUser);
	}
	
	@Transactional(readOnly = true)
	public ChannelUser find(Long channelUserId){
		return channelUserRepository.select(channelUserId);
	}
	@Transactional(readOnly = true)
	public List<ChannelUser> findUsersOrderByReadASC(Long channelId) {
		return channelUserRepository.findUsersOrderByReadASC(channelId);
	}

	@Transactional(readOnly = true)
	public List<Long> getJoinUserIds(Long id) {
		return channelUserRepository.getJoinUserIds( id );
	}

	@Transactional(readOnly = true)
	public List<User> getJoinUsers(Long id, Integer userMax) {
		return channelUserRepository.getJoinUsers( id, userMax );
	}

	@Transactional(readOnly = true)
	public List<User> getJoinUsersExceptMe(User user, Long id, Integer userMax ) {
		return channelUserRepository.getJoinUsersExceptMe( user, id, userMax );
	}

	@Transactional(readOnly = true)
	public ChannelUser findByUserChannel(Long userId, Long channelId) {
		return channelUserRepository.findByUserChannel( userId, channelId );
	}
	@Transactional
	public void updateLastReadToLastMessageInfo(Long userId, Long channelId, Long messageInfoId) {
		ChannelUser channelUser = channelUserRepository.findByUserChannel(userId, channelId );
		channelUser.setPrevReadMessageInfoId( channelUser.getLastReadMessageInfoId() );
		channelUser.setLastReadMessageInfoId(messageInfoId);
//		channelUser.setLastReadDate( new Date() );
		channelUserRepository.update(channelUser );
	}

	@Transactional
	public void leave(Sunny sunny, Long channelId, User user) {
		
		Channel channel = channelService.find(channelId);
		
		ChannelUser channelUser = channelUserRepository.findByUserChannel(user.getId(), channelId);
		
		if( channelUser == null ){
			throw new SimpleSunnyException();
		}
		
		/**
		 * OneToOne 방일때와 Group 일 때의 처리가 다르다.
		 */
		if( channel.getType() == Channel.ONETOONE ){
			channelUser.setIsHidden(true);
			channelUserRepository.update(channelUser);
		}else{
			channelUserRepository.delete(channelUser);	
			channelService.minusUserCount( channelId );
			messageService.systemSave( sunny, new Channel(channelId), Message.TYPE_OUT, user, null);
		}
		messageService.removeChannelMessages(channelId, user.getId());
	}

	@Transactional
	public void cancleHidden(Long id, Long userId) {
		ChannelUser channelUser =  channelUserRepository.findByUserChannel( userId, id );
		channelUser.setIsHidden(false);
	}

	@Transactional
	public List<ChannelUser> getUsersShownChannelUsers(Long id, Stream stream) {
		return channelUserRepository.getUsersShownChannelUsers(id, stream);
	}

	@Transactional
	public Channel info(Long channelId, User user) {
		Channel channel = channelService.find(channelId);
//		ChannelUser channelUser = channelUserRepository.findByUserChannel(user.getId(), channelId);
//		
//		Channel channel = channelUser.getChannel();
		
		return channel;
		//if( channelUser.get)
	}

	@Transactional//(isolation=Isolation.REPEATABLE_READ)
	public ChannelUser findAndUpdateMessageInfo(Sunny sunny, Long userId, Long channelId,
			Long lastMessageInfoId, Boolean updateLastDate) {
		ChannelUser channelUser = findByUserChannel(userId, channelId);
		
		if( channelUser == null ){
			return null;
		}
		
		if( lastMessageInfoId == null ){
			return channelUser;
		}
		
		if( updateLastDate == false )
			return channelUser;
		// 글을 읽는 사용자가 이전에 마지막으로 읽은 메시지가 현재 마지막 메시지와 다르면
					// 그 마지막 메시지 정보를 현재 마지막 메시지로 변경하고 그 이전은 읽은 것으로 처리한다. 
		if( !lastMessageInfoId.equals(channelUser.getLastReadMessageInfoId()) ){
			/*
			 * 처음 채팅방을 읽는 사람의 경우 lastReadMessageInfoId 가 존재하지 않는다.
			 * 그럴 경우 사용자가 입장한 시간 이후 처음 메시지를 첫 메시지로 표시한다. 
			 */
			if( channelUser.getLastReadMessageInfoId() == null ){
				channelUser.setPrevReadMessageInfoId(null);
				channelUser.setLastReadMessageInfoId(lastMessageInfoId);
			}else{
				channelUser.setPrevReadMessageInfoId( channelUser.getLastReadMessageInfoId() );
				channelUser.setLastReadMessageInfoId(lastMessageInfoId);
			}
			channelUser.setUpdateFlag(true);
			update(channelUser);
			messageService.readMessages( sunny, channelId, userId, channelUser.getPrevReadMessageInfoId(), channelUser.getLastReadMessageInfoId());	
			
			//messageService.readMessages( sunny, channelId, userId, channelUser.getPrevReadMessageInfoId(), channelUser.getLastReadMessageInfoId());
			
		}
		
		return channelUser;
	}

	public List<Long> getAlreadyReadUserIds(Long channelId,
			Long lastReadMessageInfoId) {
		return channelUserRepository.getAlreadyReadUserIds( channelId, lastReadMessageInfoId);
	}

	@Autowired
	private MessageInfoService messageInfoService;
	
	@Transactional(readOnly = true)
	public List<ChannelUser> getLastReadUsers(Sunny sunny, Long channelId, Long userId) {
		MessageInfo lastMessageInfo = messageInfoService.getLastFromChannel( channelId );
		return channelUserRepository.getLastReadUsers( channelId, lastMessageInfo.getId(), userId );
	}

	@Transactional(readOnly = true)
	public List<ChannelUserDTO> getJoinChannelUserDTOs(Long channelId, Integer userMax) {
		return channelUserRepository.getJoinChannelUserDTOs( channelId, userMax );
	}

	@Transactional
	public void cancleHiddenAll(Long channelId) {
		channelUserRepository.cancleHiddenAll( channelId );
	}

}

