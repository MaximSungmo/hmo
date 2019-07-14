package kr.co.sunnyvale.sunny.domain.post;

import java.util.List;


public class StoryModifyPostDTO {

	private String text;
	
	private List<Long>	mediaIds;
	
	private String requestBody;
	
	
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

	public String getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}


	

}