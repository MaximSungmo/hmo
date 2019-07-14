package kr.co.sunnyvale.sunny.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import kr.co.sunnyvale.sunny.domain.Channel;
import kr.co.sunnyvale.sunny.domain.ChannelUser;
import kr.co.sunnyvale.sunny.domain.LastRead;
import kr.co.sunnyvale.sunny.domain.Message;
import kr.co.sunnyvale.sunny.domain.MessageInfo;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.dto.ChannelInsideDTO;
import kr.co.sunnyvale.sunny.domain.dto.ChannelListDTO;
import kr.co.sunnyvale.sunny.domain.dto.ChannelUserDTO;
import kr.co.sunnyvale.sunny.domain.dto.NotifyInfoDTO;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.exception.CannotInviteFromOneToOneException;
import kr.co.sunnyvale.sunny.exception.ChannelUserAlreadyExistsException;
import kr.co.sunnyvale.sunny.exception.ChatUserCountException;
import kr.co.sunnyvale.sunny.repository.hibernate.ChannelRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.ChannelUserRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.LastReadRepository;
import kr.co.sunnyvale.sunny.service.ChannelService;
import kr.co.sunnyvale.sunny.service.MessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

@Service( value="channelService" )
@Transactional
public class ChannelServiceImpl implements ChannelService{

	@Autowired
	private ChannelUserService channelUserService;
	
	@Autowired
	private ChannelRepository channelRepository;

	@Autowired
	private ChannelUserRepository channelUserRepository;
	
	@Autowired
	private LastReadRepository lastReadRepository;
	
	@Autowired
	private MessageSource messageSource;

	@Autowired
	private MessageService messageService;
	
	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.ChannelService#createChannel(kr.co.sunnyvale.sunny.domain.User, java.util.Set)
	 */
	@Override
	@Transactional
	public Channel createChannel(Sunny sunny, User user, Set<Long> userIds){
		
		Channel channel = new Channel();
		channel.setUserCount( userIds.size() );
		channel.setCreator( user );
		channel.setHidden(true);
		
		
		if( channel.getUserCount() > 2 )
			channel.setType( Channel.GROUP );
		else if( channel.getUserCount() == 2 ){
			Iterator<Long> iter = userIds.iterator();
			Long user1 = (Long) iter.next();
			Long user2 = (Long) iter.next();
			Channel oneToOneChannel = getOneToOneRoom( user1, user2);
			if( oneToOneChannel != null ){
				return oneToOneChannel; 
			}
			channel.setUser1( new User(user1) );
			channel.setUser2( new User(user2) );
			
			channel.setType( Channel.ONETOONE );
		}else{
			throw new ChatUserCountException();
		}
		channelRepository.save(channel);
		

		
		MessageInfo messageInfo = messageService.systemSave( sunny, channel, Message.TYPE_CREATE, user, userIds.toArray(new Long[userIds.size()]));
		
		/*
		 * createDate 때문에 넣음
		 */
		for( Long relationUser : userIds ){
				ChannelUser channelUser = new ChannelUser();
				channelUser.setUser(new User(relationUser));
				channelUser.setIsHidden(false);
				channelUser.setChannel(channel);
				channelUser.setLastReadMessageInfoId(messageInfo.getId());
				channelUserService.save(channelUser);
		}
		
		channel.setTmpJoinedUsers( channelUserService.getJoinUsersExceptMe(user, channel.getId(), 4 ));
		
		return channel;
	}
	
	@Transactional( readOnly = true)
	private Channel getOneToOneRoom(Long user1Id, Long user2Id) {
		return channelRepository.getOneToOneRoom( user1Id, user2Id );
	}

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.ChannelService#save(kr.co.sunnyvale.sunny.domain.Channel)
	 */
	@Override
	@Transactional
	public void save(Channel channel){
		channelRepository.save(channel);
	}
	
	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.ChannelService#update(kr.co.sunnyvale.sunny.domain.Channel)
	 */
	@Override
	@Transactional
	public void update(Channel channel){
		channelRepository.update(channel);
	}
	
	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.ChannelService#find(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public Channel find(Long channelId){
		return channelRepository.select(channelId);
	}

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.ChannelService#getChannels(kr.co.sunnyvale.sunny.domain.User, kr.co.sunnyvale.sunny.domain.extend.Stream)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Channel> getChannels(User user, Stream stream) {
		List<Channel> channels = Lists.newArrayList();//channelRepository.getChannel(user, stream);
		channels = channelRepository.getUsersShownChannels(user.getId(), stream);
		for( Channel channel : channels ){
			channel.setTmpJoinedUsers( channelUserService.getJoinUsersExceptMe(user, channel.getId(), 4 ));
		}
		return channels;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Page<Channel> getChannels(User user, Integer pageNum, int pageSize) {
		
		Page<Channel> pagedChannels = channelRepository.getUsersShownChannels(user.getId(), pageNum, pageSize);
		for( Channel channel : pagedChannels.getContents() ){
			channel.setTmpJoinedUsers( channelUserService.getJoinUsersExceptMe(user, channel.getId(), 4 ));
		}
		return pagedChannels;
	}
	
	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.ChannelService#getChannel(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public Channel getChannel(Long id) {
		return channelRepository.select(id);
	}
	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.ChannelService#getJoinUsers(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<User> getJoinUsers(Long channelId) {
		
		return channelRepository.getJoinUsers(channelId);
	}
	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.ChannelService#getChannelUnreadCount(kr.co.sunnyvale.sunny.domain.User)
	 */
	@Override
	@Transactional(readOnly = true)
	public Number getChannelUnreadCount(User user) {
		Date lastRead = getChannelLastRead(user);
		return channelRepository.getChannelUnreadCount(user, lastRead);
	}

	private Date getChannelLastRead(User user) {
		return (Date) lastReadRepository.findColumnByObject("messageLastRead", "user", user);
	}

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.ChannelService#getUpdateDate(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public Serializable getUpdateDate(Long channelId) {
	
		return channelRepository.getUpdateDate( channelId );
	}

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.ChannelService#getChannelsAndUpdate(kr.co.sunnyvale.sunny.domain.User, kr.co.sunnyvale.sunny.domain.extend.Stream)
	 */
	@Override
	public List<Channel> getChannelsAndUpdate(User user, Stream stream) {
		lastReadRepository.updateMessageDate(user);
		
		return getChannels( user, stream );

	}

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.ChannelService#inviteUsers(kr.co.sunnyvale.sunny.domain.extend.Sunny, java.lang.Long, kr.co.sunnyvale.sunny.domain.User, java.util.List)
	 */
	@Override
	@Transactional
	public void inviteUsers(Sunny sunny, Long channelId, User user, Set<Long> userIds) {
		Channel channel = channelRepository.select(channelId);
		
		if( channel.getType() == Channel.ONETOONE ){
			throw new CannotInviteFromOneToOneException();
		}
		
		if( userIds == null || userIds.size() == 0 )
			throw new RuntimeException();
		
		MessageInfo messageInfo = messageService.systemSave( sunny, channel, Message.TYPE_INVITE, user, userIds.toArray(new Long[userIds.size()]));
		
		for( Long userId : userIds ){
			ChannelUser channelUser = channelUserService.findByUserChannel(userId, channelId);
		
			if( channelUser != null ){
				throw new ChannelUserAlreadyExistsException();
			}
			
			channelUser = new ChannelUser();
			channelUser.setChannel( channel );
			channelUser.setIsHidden(true);
			channelUser.setLastReadMessageInfoId(messageInfo.getId());
			User objectUser = new User( userId ) ;
			channelUser.setUser( objectUser );
			
			channelUserService.save(channelUser);
			
		}
		channel.setExistInvitedUser(true);
		channel.setUserCount( channel.getUserCount() + userIds.size() );
		channelRepository.update(channel);
			
		
	}

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.ChannelService#minusUserCount(java.lang.Long)
	 */
	@Override
	@Transactional
	public void minusUserCount(Long channelId) {
		Channel channel = channelRepository.select(channelId);
		channel.setUserCount( channel.getUserCount() - 1 );
	}

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.ChannelService#plusUserCount(java.lang.Long)
	 */
	@Override
	@Transactional
	public void plusUserCount(Long channelId) {
		Channel channel = channelRepository.select(channelId);
		channel.setUserCount( channel.getUserCount() + 1 );
	}

	
	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.ChannelService#info(java.lang.Long, kr.co.sunnyvale.sunny.domain.User)
	 */
	@Override
	public ChannelInsideDTO info(Long channelId, User user) {
			Channel channel = find(channelId);
			ChannelInsideDTO channelDto = new ChannelInsideDTO();
			
			channelDto.setId(channel.getId());
			channelDto.setType(channel.getType());
			channelDto.setLastUserName( channel.getLastUserName() );
			channelDto.setLastUserProfilePic( channel.getLastUserProfilePic() );
			channelDto.setLink( "/message/" + channel.getId() );
			channelDto.setUpdateDate( channel.getUpdateDate() );
			channelDto.setLastTextSnippet( channel.getLastTextSnippet() );
			
			List<ChannelUserDTO> channelUserDTOs = channelUserService.getJoinChannelUserDTOs(channelId, null);
			
			channelDto.setUsers(channelUserDTOs);
//			ChannelUser channelUser = channelUserRepository.findByUserChannel(user.getId(), channelId);
//			
//			Channel channel = channelUser.getChannel();
//			channel.setTmpJoinedUsers( channelUserService.getJoinUsersExceptMe(user, channel.getId(), 4 ));
			return channelDto;
			//if( channelUser.get)
	}

	@Override
	public ChannelListDTO listInfo(Long channelId, User user) {
		Channel channel = find(channelId);
		ChannelListDTO channelDto = new ChannelListDTO();
		
		channelDto.setId(channel.getId());
		channelDto.setType(channel.getType());
		channelDto.setLastUserName( channel.getLastUserName() );
		channelDto.setLastUserProfilePic( channel.getLastUserProfilePic() );
		channelDto.setLink( "/message/" + channel.getId() );
		channelDto.setUpdateDate( channel.getUpdateDate() );
		channelDto.setLastTextSnippet( channel.getLastTextSnippet() );
		channelDto.setUserCount( channel.getUserCount() );
		channelDto.setTmpJoinedUsers( channelUserService.getJoinUsersExceptMe(user, channel.getId(), 4 ));
		
//		ChannelUser channelUser = channelUserRepository.findByUserChannel(user.getId(), channelId);
//		
//		Channel channel = channelUser.getChannel();
//		channel.setTmpJoinedUsers( channelUserService.getJoinUsersExceptMe(user, channel.getId(), 4 ));
		return channelDto;
	}

	@Override
	public Channel getNotifyNewOne(Sunny sunny, User user) {
		
		LastRead lastRead = lastReadRepository.findUniqByObject("user", user);
		return channelRepository.getNotifynewOne( sunny, user, lastRead );
	}

	@Override
	public NotifyInfoDTO getNotifyInfo(Sunny sunny, Long userId) {
		LastRead lastRead = lastReadRepository.findUniqByObject("user.id", userId);
		return channelRepository.getNotifyInfo( sunny, userId, lastRead );
	}

	
}

