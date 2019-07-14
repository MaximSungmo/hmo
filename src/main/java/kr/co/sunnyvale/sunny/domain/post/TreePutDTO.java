package kr.co.sunnyvale.sunny.domain.post;

public class TreePutDTO {

	

	private Long siteId;
	
	private Long id;
	
	private Long targetId;
	
	
	/*
	 * before - 맨 처음으로 간 경우. 부모가 없음.
	 * after - 어떠한 형제의 다음으로 옮김. 만약 다른 부모 밑에서 현재 부모로 옮겨도 만약 중간으로 옮길 경우 after 가 나온다. 주의할 것!
	 * inside - 어떠한 부모의 첫번째 위치로 옮겼을 때
	 */
	private String position;


	public Long getSiteId() {
		return siteId;
	}


	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Long getTargetId() {
		return targetId;
	}


	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}


	public String getPosition() {
		return position;
	}


	public void setPosition(String position) {
		this.position = position;
	}

	
	
}
