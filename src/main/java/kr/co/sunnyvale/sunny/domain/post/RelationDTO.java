package kr.co.sunnyvale.sunny.domain.post;

import kr.co.sunnyvale.sunny.domain.User;


public class RelationDTO {
	
	
	
	/*
	 * Relation 필드
	 */
	private Long id;
	
//	private boolean friend;

	private Integer type;
	
	/*
	 * User 정보 
	 */
	private Long userId;
	
	private String userName;
	
	private String userProfilePic;
	
	private String userStatusMessage;
	
	private int userFriendCount; 
	
	///// End of User 정보들

//	private FollowRelation myrelation;

	// 함께 아는 친구들
	private int crossFriendCount;
	
	/*
	 * 임시 변수	
	 */
	
//	private boolean alreadyRelationed;

	
	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Long getUserId() {
		return userId;
	}


	public void setUserId(Long userId) {
		this.userId = userId;
	}


	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserProfilePic() {
		if( this.userProfilePic == null )
			return User.DEFAULT_PROFILE_PIC;
		
		return userProfilePic;
	}

	public void setUserProfilePic(String userProfilePic) {
		this.userProfilePic = userProfilePic;
	}


	public int getUserFriendCount() {
		return userFriendCount;
	}




	public void setUserFriendCount(int userFriendCount) {
		this.userFriendCount = userFriendCount;
	}


	public Integer getType() {
		return type;
	}


	public void setType(Integer type) {
		this.type = type;
	}


	public int getCrossFriendCount() {
		return crossFriendCount;
	}


	public void setCrossFriendCount(int crossFriendCount) {
		this.crossFriendCount = crossFriendCount;
	}


	public String getUserStatusMessage() {
		return userStatusMessage;
	}


	public void setUserStatusMessage(String userStatusMessage) {
		this.userStatusMessage = userStatusMessage;
	}
	
	
	
}
