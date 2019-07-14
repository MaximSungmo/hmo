package kr.co.sunnyvale.sunny.domain.post;

public class MovePutDTO {

	private Long siteId;
	
	private Long id;
	
	private Long beforePrevId;
	
	private Long targetPrevId;

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

	public Long getBeforePrevId() {
		return beforePrevId;
	}

	public void setBeforePrevId(Long beforePrevId) {
		this.beforePrevId = beforePrevId;
	}

	public Long getTargetPrevId() {
		return targetPrevId;
	}

	public void setTargetPrevId(Long targetPrevId) {
		this.targetPrevId = targetPrevId;
	}
	
	
	
}
