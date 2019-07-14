package kr.co.sunnyvale.sunny.domain.dto;

public class UserSmallGroupDTO {
	private Long userId;
	private Long smallGroupId;
	private boolean join;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getSmallGroupId() {
		return smallGroupId;
	}
	public void setSmallGroupId(Long smallGroupId) {
		this.smallGroupId = smallGroupId;
	}
	public boolean isJoin() {
		return join;
	}
	public void setJoin(boolean join) {
		this.join = join;
	}
	
	
	
	
	
}
