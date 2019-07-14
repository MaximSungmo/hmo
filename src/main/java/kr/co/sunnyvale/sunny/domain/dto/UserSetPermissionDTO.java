package kr.co.sunnyvale.sunny.domain.dto;

public class UserSetPermissionDTO {
	private Long userId;
	private Long smallGroupId;
	private String permissionType;
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
	public String getPermissionType() {
		return permissionType;
	}
	public void setPermissionType(String permissionType) {
		this.permissionType = permissionType;
	}
	
	
}
