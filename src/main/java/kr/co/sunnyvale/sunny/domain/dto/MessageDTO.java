package kr.co.sunnyvale.sunny.domain.dto;

public class MessageDTO {
	
	private Long channelId;
	
	private String text;

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	
}
