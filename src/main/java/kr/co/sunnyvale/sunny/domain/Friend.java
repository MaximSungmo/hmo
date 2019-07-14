package kr.co.sunnyvale.sunny.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "FRIEND")
public class Friend implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6529325160764213736L;
	public static final int TYPE_NONE = 0;
	public static final int TYPE_FRIEND = 1;

	
	@Id
	@Column(name = "id", columnDefinition="bigint(20)" )
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;

	/*
	 * 친구요청 받는 사람( 대상자 )
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "followed_id")
	private User followed;

	/*
	 * 친구요청 하는 사람 ( 나 )
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "follower_id")
	private User follower;
	
	@Column(name = "send_friend_request", columnDefinition="bit default 0")
	private boolean sendFriendRequest;
	
	@Column(name = "friend", columnDefinition="bit default 0")
	private boolean friend;

	@Column(name = "friend_request_date")
	private Date friendRequestDate;
	
	@Transient
	private Friend myRelation;
	
	public Friend(){
		setFriendRequestDate(new Date());
		setFriend(false);
	}
	
	public Friend( Long followed, Long follower ) {
		this();
		this.followed = new User(followed);
		this.follower = new User(follower);
	}

	@JsonIgnore
	public User getFollowed() {
		return followed;
	}
	@JsonIgnore
	public void setFollowed(User followed) {
		this.followed = followed;
	}

	public User getFollower() {
		return follower;
	}

	public void setFollower(User follower) {
		this.follower = follower;
	}


	public boolean isFriend() {
		return friend;
	}

	public void setFriend(boolean friend) {
		this.friend = friend;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isSendFriendRequest() {
		return sendFriendRequest;
	}

	public void setSendFriendRequest(boolean sendFriendRequest) {
		this.sendFriendRequest = sendFriendRequest;
	}

	public Date getFriendRequestDate() {
		return friendRequestDate;
	}

	public void setFriendRequestDate(Date friendRequestDate) {
		this.friendRequestDate = friendRequestDate;
	}

	public Friend getMyRelation() {
		return myRelation;
	}

	public void setMyRelation(Friend myRelation) {
		this.myRelation = myRelation;
	}

	
}
//
//class FollowPK implements Serializable {
//	
//	private static final long serialVersionUID = -1L;
//	
//    protected User followed;
//	protected User follower;
//
//    public FollowPK() {}
//
//    public FollowPK( User follower, User followed ) {
//        this.follower = follower;
//        this.followed = followed;
//    }
//
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result
//				+ ((followed == null) ? 0 : followed.hashCode());
//		result = prime * result
//				+ ((follower == null) ? 0 : follower.hashCode());
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		FollowPK other = (FollowPK) obj;
//		if (followed == null) {
//			if (other.followed != null)
//				return false;
//		} else if (!followed.equals(other.followed))
//			return false;
//		if (follower == null) {
//			if (other.follower != null)
//				return false;
//		} else if (!follower.equals(other.follower))
//			return false;
//		return true;
//	}
//
//
//    
//}
