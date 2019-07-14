package kr.co.sunnyvale.sunny.service.impl;

import java.util.Date;
import java.util.List;

import kr.co.sunnyvale.sunny.domain.Friend;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.exception.FriendException;
import kr.co.sunnyvale.sunny.repository.hibernate.FriendRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.LastReadRepository;
import kr.co.sunnyvale.sunny.service.AfterService;
import kr.co.sunnyvale.sunny.service.FriendService;
import kr.co.sunnyvale.sunny.service.SmallGroupService;
import kr.co.sunnyvale.sunny.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( value="friendService" )
public class FriendServiceImpl implements FriendService {

	@Autowired
	private FriendRepository friendRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AfterService afterService; 
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private LastReadRepository lastReadRepository;
	
	@Autowired
	private SmallGroupService smallGroupService;
	
	@Override
	@Transactional
	public void requestFriend(Sunny sunny, Long followedId, Long followerId) {
		if( followedId.equals(followerId) == true ){
			throw new FriendException("error.friendRequest.recursive");
		}
		
		Friend friend = friendRepository.find(followedId, followerId);
		
		if( friend == null ){
			friend = new Friend( followedId, followerId );
			friendRepository.save(friend);
		}
		
		// 이미 친구인 경우는 에러!
		if( friend.isFriend() ){
			throw new FriendException("error.alreadyFriend");
		}
		
		// 이미 친구요청 된 경우엔 시간만 업데이트
		if( friend.isSendFriendRequest() ){
			friend.setFriendRequestDate(new Date());
			friendRepository.update(friend);
		}
		
		Friend aspectYou = friendRepository.find(followerId, followedId);
		
		/*
		 * 이미 상대방이 나에게 친구요청을 한 경우 친구맺고 끝
		 */
		if( aspectYou != null && aspectYou.isSendFriendRequest() == true ){
			aspectYou.setSendFriendRequest(false);
			aspectYou.setFriend(true);
			friend.setFriend(true);
			friendRepository.update(aspectYou);
			friendRepository.update(friend);
			
			userService.plusFriendCount(followerId);
			userService.plusFriendCount(followedId);
			
			smallGroupService.addBothFriendAccessGroup( followerId, followedId );
			
			return;
		}
		
		friend.setSendFriendRequest(true);
		friendRepository.update(friend);
		
		afterService.friendRequest( sunny, userService.findById(followedId), userService.findById(followerId));
	}
	

	@Override
	@Transactional
	public void cancelRequestFriend(Long followedId, Long followerId) {
		if( followerId.equals( followedId ) ){
			throw new FriendException("error.friendRequest.recursive"); 
		}
		
		Friend friend = friendRepository.find(followedId, followerId);
		if( friend == null ){
			throw new FriendException();
		}
		
		friend.setSendFriendRequest(false);
		
		if( friend.isFriend() == true ){
			friend.setFriend(false);
			
			Friend aspectYou = friendRepository.find(followerId, followedId);
			if( aspectYou != null ){
				aspectYou.setSendFriendRequest(false);
				aspectYou.setFriend(false);
			}
			
			userService.minusFriendCount(followerId);
			userService.minusFriendCount(followedId);
			smallGroupService.removeBothFriendAccessGroup( followerId, followedId );
			
		}
	}

	@Override
	@Transactional
	public void denyRequestFriend(Long followerId, Long followedId) {
		if( followerId.equals( followedId ) ){
			throw new FriendException("error.friendRequest.recursive"); 
		}
		Friend friend = friendRepository.find(followedId, followerId);
		if( friend == null ){
			throw new FriendException();
		}
		
		if(friend.isFriend() == true){
			friendRepository.delete(friend);
			Friend aspectYou = friendRepository.find(followerId, followedId);
			if( aspectYou != null ){
				friendRepository.delete(aspectYou);
			}
			smallGroupService.removeBothFriendAccessGroup( followerId, followedId );
			return;
		}

		friendRepository.delete(friend);
		
	}
	@Override
	public void calcRelation(User basecampUser, User user) {
		
		Friend friend = friendRepository.find(basecampUser.getId(), user.getId());
		
		if( friend == null ){
			
			Friend aspectYou = friendRepository.find(user.getId(), basecampUser.getId());
			
			if( aspectYou == null ){
				basecampUser.setRelationFriendRequest(User.RELATION_FRIEND_REQUEST_NONE);
				return;	
			}
			
			if( aspectYou.isSendFriendRequest() ){
				basecampUser.setRelationFriendRequest(User.RELATION_FRIEND_REQUEST_TO_ME);
				return;
			}
			
			basecampUser.setRelationFriendRequest(User.RELATION_FRIEND_REQUEST_NONE);
			return;
		}

		
		
		if( friend.isFriend() == true){
			basecampUser.setRelationFriendRequest(User.RELATION_FRIEND_REQUEST_FRIEND);
			return;
		}
		
		if( friend.isSendFriendRequest() == true ){
			basecampUser.setRelationFriendRequest(User.RELATION_FRIEND_REQUEST_FROM_ME);
			return;
		}

		
		Friend aspectYou = friendRepository.find(user.getId(), basecampUser.getId());
		
		if( aspectYou == null ){
			basecampUser.setRelationFriendRequest(User.RELATION_FRIEND_REQUEST_NONE);
			return;	
		}
	
		if( aspectYou.isSendFriendRequest() ){
			basecampUser.setRelationFriendRequest(User.RELATION_FRIEND_REQUEST_TO_ME);
			return;	
		}
		
		basecampUser.setRelationFriendRequest(User.RELATION_FRIEND_REQUEST_NONE);
		return;	
	}


	@Override
	public Number getFriendRequestUnreadCount(User user) {
		Date lastRead = (Date) lastReadRepository.findColumnByObject("friendRequestLastRead", "user", user);
		return friendRepository.getFriendRequestUnreadCount(user, lastRead);
	}


	@Override
	public List<Friend> getNonAcceptedFriendRequests(User user, Stream stream) {
		return friendRepository.getNonAcceptedFriendRequests(user.getId(), stream);
	}
	@Override
	@Transactional(readOnly = true)
	public Page<Friend> getFriends(Sunny sunny, Long authUserId, Long userId,
			String query, Integer page, int pageSize) {
		
		Page<Friend> pagedResult = friendRepository.getFriends(sunny, authUserId, userId, query, page, pageSize );
		
		for( Friend friend : pagedResult.getContents() ){
			if( authUserId.equals(userId) ){
				friend.setMyRelation(friend);
			}else{
				friend.setMyRelation( friendRepository.find(friend.getFollower().getId(), authUserId) );
			}
		}
		
		return pagedResult;
	}


}