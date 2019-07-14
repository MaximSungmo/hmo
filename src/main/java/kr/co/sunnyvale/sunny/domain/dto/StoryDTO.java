package kr.co.sunnyvale.sunny.domain.dto;

import java.util.Date;
import java.util.List;

import kr.co.sunnyvale.sunny.domain.Content;
import kr.co.sunnyvale.sunny.domain.Media;
import kr.co.sunnyvale.sunny.domain.Reply;
import kr.co.sunnyvale.sunny.domain.SmallGroup;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.util.HtmlUtil;

public class StoryDTO {
	
	
	
	/*
	 * Content 필드
	 */
	private Long id;
	
	private String taggedTextPrev;
	
	private String taggedTextNext;
	
	private int returnCount; 

	private String contentPic;
	
	private Date createDate;
	
	private Date updateDate;
	
	private Date arriveDate;
	
	private Integer type;

	private Integer replyCount;
	
	private int mediaCount;

	private Integer feelScore;
	
	private Integer feelCount;
	
	private String ipAddress;
	
	private boolean isGroupStory = false;
	
	private Long smallGroupId;
	
	private String smallGroupName;
	
	private int smallGroupType;
	
	private int permissionType;
	
	private boolean feedback;
	/////// End of Content 필드 끝
	
	

	
	/*
	 * StoryLine 정보들
	 * 
	 */
//	private Long storyLineId;
//	
//	private String storyLineTitle;
	
	
	//// End of StoryLine
	
	private List<Reply> replys;
	
	/*
	 * User 정보 
	 */
	private Long userId;
	
	private String userName;
	
	
	private String userProfilePic;
	
	/*
	 * PostUser 정보 
	 */
	private Long postUserId;
	
	private String postUserName;
	
	private String postUserProfilePic;
	
	///// End of User 정보들
	
//	/*
//	 * Category 
//	 */
//	
//	private Integer category1Id;
//	
//	private String category1Name;
//	
//	private Integer category2Id;
//	
//	private String category2Name;
	
	
	////// End of Category
	
	/*
	 * 임시 변수	
	 */
	
	private Integer feeledId;

	private List<Media> mediaes;
	
//	private String searchedText;
	

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}

	public String getTaggedTextPrev() {
		return taggedTextPrev;
	}


	public void setTaggedTextPrev(String taggedTextPrev) {
		this.taggedTextPrev = taggedTextPrev;
	}


	public String getTaggedTextNext() {
		return taggedTextNext;
	}


	public void setTaggedTextNext(String taggedTextNext) {
		this.taggedTextNext = taggedTextNext;
	}


	public int getReturnCount() {
		return returnCount;
	}


	public void setReturnCount(int returnCount) {
		this.returnCount = returnCount;
	}


	public String getContentPic() {
		return contentPic;
	}


	public void setContentPic(String contentPic) {
		this.contentPic = contentPic;
	}


	public Date getCreateDate() {
		return createDate;
	}


	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}


	public Date getUpdateDate() {
		return updateDate;
	}


	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}



//
//	public Long getStoryLineId() {
//		return storyLineId;
//	}
//
//
//	public void setStoryLineId(Long storyLineId) {
//		this.storyLineId = storyLineId;
//	}
//
//
//	public String getStoryLineTitle() {
//		return storyLineTitle;
//	}
//
//
//	public void setStoryLineTitle(String storyLineTitle) {
//		this.storyLineTitle = storyLineTitle;
//	}
//



	public List<Reply> getReplys() {
		return replys;
	}


	public void setReplys(List<Reply> replys) {
		this.replys = replys;
	}


	public List<Media> getMediaes() {
		return mediaes;
	}


	public void setMediaes(List<Media> mediaes) {
		this.mediaes = mediaes;
	}




	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}




	public Long getUserId() {
		return userId;
	}


	public void setUserId(Long userId) {
		this.userId = userId;
	}


	public String getUserProfilePic() {
		if( this.userProfilePic == null ){
			return User.DEFAULT_PROFILE_PIC;
		}
		return userProfilePic;
	}


	public void setUserProfilePic(String userProfilePic) {
		this.userProfilePic = userProfilePic;
	}


	public Long getPostUserId() {
		return postUserId;
	}


	public void setPostUserId(Long postUserId) {
		this.postUserId = postUserId;
	}


	public String getPostUserName() {
		return postUserName;
	}


	public void setPostUserName(String postUserName) {
		this.postUserName = postUserName;
	}




	public String getPostUserProfilePic() {
		if( this.postUserProfilePic == null ){
			return User.DEFAULT_PROFILE_PIC;
		}

		return postUserProfilePic;
	}


	public void setPostUserProfilePic(String postUserProfilePic) {
		this.postUserProfilePic = postUserProfilePic;
	}


	public Integer getFeeledId() {
		return feeledId;
	}


	public void setFeeledId(Integer feeledId) {
		this.feeledId = feeledId;
	}


	public Integer getType() {
		return type;
	}


	public void setType(Integer type) {
		this.type = type;
	}


//	public Integer getCategory1Id() {
//		return category1Id;
//	}
//
//
//	public void setCategory1Id(Integer category1Id) {
//		this.category1Id = category1Id;
//	}
//
//
//	public String getCategory1Name() {
//		return category1Name;
//	}
//
//
//	public void setCategory1Name(String category1Name) {
//		this.category1Name = category1Name;
//	}
//
//
//	public Integer getCategory2Id() {
//		return category2Id;
//	}
//
//
//	public void setCategory2Id(Integer category2Id) {
//		this.category2Id = category2Id;
//	}
//
//
//	public String getCategory2Name() {
//		return category2Name;
//	}
//
//
//	public void setCategory2Name(String category2Name) {
//		this.category2Name = category2Name;
//	}
//




	public String getIpAddress() {
		return ipAddress;
	}


	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}




//	public String getSearchedText() {
//		return searchedText;
//	}
//
//
//	public void setSearchedText(String searchedText) {
//		this.searchedText = searchedText;
//	}
	
//	public Long getGroupId() {
//		return smallGroupId;
//	}
//
//
//	public void setGroupId(Long groupId) {
//		this.smallGroupId = groupId;
//	}
//
//
//	public String getGroupTitle() {
//		return smallGroupName;
//	}
//
//
//	public void setGroupTitle(String groupTitle) {
//		this.smallGroupName = groupTitle;
//	}

	
	
	public boolean isGroupStory() {
		return isGroupStory;
	}


	public Long getSmallGroupId() {
		return smallGroupId;
	}


	public void setSmallGroupId(Long smallGroupId) {
		this.smallGroupId = smallGroupId;
	}


	public String getSmallGroupName() {
		return smallGroupName;
	}


	public void setSmallGroupName(String smallGroupName) {
		this.smallGroupName = smallGroupName;
	}


	public void setGroupStory(boolean isGroupStory) {
		this.isGroupStory = isGroupStory;
	}


	public Integer getReplyCount() {
		return replyCount;
	}


	public void setReplyCount(Integer replyCount) {
		this.replyCount = replyCount;
	}


	public int getMediaCount() {
		return mediaCount;
	}


	public void setMediaCount(int mediaCount) {
		this.mediaCount = mediaCount;
	}


	public Integer getFeelScore() {
		return feelScore;
	}


	public void setFeelScore(Integer feelScore) {
		this.feelScore = feelScore;
	}


	public Integer getFeelCount() {
		return feelCount;
	}


	public void setFeelCount(Integer feelCount) {
		this.feelCount = feelCount;
	}


	public void fixSearchedText(String[] queries) {
		for( String query : queries ){
			if( this.getTaggedTextPrev() != null ){
				this.setTaggedTextPrev( HtmlUtil.highlighting(this.getTaggedTextPrev(), query, "<strong>", "</strong>") );
			}	
			if( this.getTaggedTextNext() != null ){
				this.setTaggedTextNext( HtmlUtil.highlighting(this.getTaggedTextNext(), query, "<strong>", "</strong>") );
			}
		}
	}


	public int getSmallGroupType() {
		return smallGroupType;
	}


	public void setSmallGroupType(int smallGroupType) {
		this.smallGroupType = smallGroupType;
	}


	public Date getArriveDate() {
		return arriveDate;
	}


	public void setArriveDate(Date arriveDate) {
		this.arriveDate = arriveDate;
	}


	public int getPermissionType() {
		return permissionType;
	}


	public void setPermissionType(int permissionType) {
		this.permissionType = permissionType;
	}

	public boolean isSystemMessage(){
		if( smallGroupId != null && smallGroupId == SmallGroup.SYSTEM_GROUP ){
			return true;
		}
		return false;
	}


	public boolean isFeedback() {
		return feedback;
	}


	public void setFeedback(boolean feedback) {
		this.feedback = feedback;
	}
	
	
}
