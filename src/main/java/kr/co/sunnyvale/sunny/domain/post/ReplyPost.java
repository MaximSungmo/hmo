package kr.co.sunnyvale.sunny.domain.post;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.User;



public class ReplyPost {
	
	public ReplyPost(){
		
	}
	
	public ReplyPost(String text, Long contentId, User user, String ipAddress) {
		super();
		this.text = text;
		this.contentId = contentId;
		this.user = user;
		this.ipAddress = ipAddress;
	}

	private String text;
	
	private Long contentId;
	
	private Long	mediaId;

	private User user;
	
	private String ipAddress;
	
	private String requestBody;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Long getContentId() {
		return contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	public Long getMediaId() {
		return mediaId;
	}

	public void setMediaId(Long mediaId) {
		this.mediaId = mediaId;
	}

	
	
}