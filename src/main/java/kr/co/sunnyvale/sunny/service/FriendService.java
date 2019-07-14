package kr.co.sunnyvale.sunny.service;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.Friend;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;

public interface FriendService {
	
	public void requestFriend( Sunny sunny, Long followedId, Long followerId );
	
	public void cancelRequestFriend( Long followedId, Long followerId );

	public void calcRelation(User basecampUser, User user);

	public Number getFriendRequestUnreadCount(User user);

	public List<Friend> getNonAcceptedFriendRequests(User user, Stream stream);

	
	/**
	 * 나한테 친구요청한 정보를 삭제한다. 
	 * @param followerId
	 * @param followedId
	 */
	public void denyRequestFriend(Long followerId, Long followedId);

	Page<Friend> getFriends(Sunny sunny, Long authUserId, Long userId,
			String query, Integer page, int pageSize);
	
	
}
