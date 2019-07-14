package kr.co.sunnyvale.sunny.domain.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import kr.co.sunnyvale.sunny.domain.Content;
import kr.co.sunnyvale.sunny.domain.Media;
import kr.co.sunnyvale.sunny.domain.Notification;
import kr.co.sunnyvale.sunny.domain.Reply;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.util.HtmlUtil;

public class NoticeDTO {
	
	
	public final static int DEFAULT_SNIPPET_SIZE = 80;
	
	/*
	 * Content 필드
	 */
	private Long id;
	
	private String snippetText;
	
	@JsonIgnore
	private String rawText;

	private String contentPic;
	
	private Date createDate;
	
	private Date updateDate;
	
	private Date arriveDate;
	
	private Integer type;

	private Integer replyCount;
	
	private int mediaCount;

	private Integer feelScore;
	
	private Integer feelCount;
	
	private boolean isGroupStory = false;
	
	private Long smallGroupId;
	
	private String smallGroupName;
	
	private int smallGroupType;
	
	private int permissionType;
	/////// End of Content 필드 끝
	

	/*
	 * User 정보 
	 */
	private Long userId;
	
	private String userName;
	
	
	private String userProfilePic;

	
	/*
	 * 그 외
	 */
	
	private boolean read = false;

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getSnippetText() {
		return getStrippedSnippetText();
		//return snippetText;
	}


	public void setSnippetText(String snippetText) {
		this.snippetText = snippetText;
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


	public Date getArriveDate() {
		return arriveDate;
	}


	public void setArriveDate(Date arriveDate) {
		this.arriveDate = arriveDate;
	}


	public Integer getType() {
		return type;
	}


	public void setType(Integer type) {
		this.type = type;
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


	public boolean isGroupStory() {
		return isGroupStory;
	}


	public void setGroupStory(boolean isGroupStory) {
		this.isGroupStory = isGroupStory;
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


	public int getSmallGroupType() {
		return smallGroupType;
	}


	public void setSmallGroupType(int smallGroupType) {
		this.smallGroupType = smallGroupType;
	}


	public int getPermissionType() {
		return permissionType;
	}


	public void setPermissionType(int permissionType) {
		this.permissionType = permissionType;
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
		return userProfilePic;
	}


	public void setUserProfilePic(String userProfilePic) {
		this.userProfilePic = userProfilePic;
	}
	
	@JsonIgnore
	public String getRawText() {
		return rawText;
	}
	@JsonIgnore
	public void setRawText(String rawText) {
		this.rawText = rawText;
	}


	public String getStrippedSnippetText() {
		if( this.getRawText() == null || this.getRawText().isEmpty()){
			return "";
		}
		
		this.snippetText = HtmlUtil.stripMention(this.getRawText());
		
		if( this.snippetText.length() > NoticeDTO.DEFAULT_SNIPPET_SIZE ){
			this.snippetText = this.snippetText.substring(0, NoticeDTO.DEFAULT_SNIPPET_SIZE + 1)  ;
		}
		this.snippetText = HtmlUtil.removeHtmlTag(this.snippetText, null);
		this.snippetText = HtmlUtil.stripBold(this.snippetText);
		return snippetText;
	}


	public boolean isRead() {
		return read;
	}


	public void setRead(boolean read) {
		this.read = read;
	}
	
}
