package kr.co.sunnyvale.sunny.domain.dto;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.Media;


public class StoryModifyDTO {
	
	private List<MediaDTO>	medias;
	
	private String requestBody;

	public String getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	public List<MediaDTO> getMedias() {
		return medias;
	}

	public void setMedias(List<MediaDTO> medias) {
		this.medias = medias;
	}


}