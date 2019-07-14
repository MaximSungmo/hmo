package kr.co.sunnyvale.sunny.domain.post;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.User;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class ContentPostDTO {

	private Long userId;

	private String title;
	
	private String text;
	
	private int permissionType;

	private List<Long>	mediaIds;
	
	private Long receiverId;
	
	private String ipAddress;

	private Long smallGroupId;

	private User user;
	
	private User postUser;
	
	private String requestBody; 
	
	private List<ContentPermissionDTO> permissions;
	
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<Long> getMediaIds() {
		return mediaIds;
	}

	public void setMediaIds(List<Long> mediaIds) {
		this.mediaIds = mediaIds;
	}

	public Long getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(Long receiverId) {
		this.receiverId = receiverId;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Long getSmallGroupId() {
		return smallGroupId;
	}

	public void setSmallGroupId(Long smallGroupId) {
		this.smallGroupId = smallGroupId;
	}

	@JsonIgnore
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@JsonIgnore
	public User getPostUser() {
		return postUser;
	}

	public void setPostUser(User postUser) {
		this.postUser = postUser;
	}

	public int getPermissionType() {
		return permissionType;
	}

	public void setPermissionType(int permissionType) {
		this.permissionType = permissionType;
	}

	public String getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	public List<ContentPermissionDTO> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<ContentPermissionDTO> permissions) {
		this.permissions = permissions;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "StoryPostDTO [userId=" + userId + ", text=" + text
				+ ", permissionType=" + permissionType + ", mediaIds="
				+ mediaIds + ", receiverId=" + receiverId + ", ipAddress="
				+ ipAddress + ", smallGroupId=" + smallGroupId + ", user="
				+ user + ", postUser=" + postUser + ", requestBody="
				+ requestBody + ", permissions=" + permissions + "]";
	}

	

}