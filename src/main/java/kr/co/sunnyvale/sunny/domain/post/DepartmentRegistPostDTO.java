package kr.co.sunnyvale.sunny.domain.post;

import com.fasterxml.jackson.annotation.JsonIgnore;

import kr.co.sunnyvale.sunny.domain.User;

public class DepartmentRegistPostDTO {

	private String departmentTitle;
	
	private String departmentDescription;

	private Long departmentParentId;
	
	private Long departmentPrevId;
	
	private int parentDepartmentRadio;

	public String getDepartmentTitle() {
		return departmentTitle;
	}

	public void setDepartmentTitle(String departmentTitle) {
		this.departmentTitle = departmentTitle;
	}

	public String getDepartmentDescription() {
		return departmentDescription;
	}

	public void setDepartmentDescription(String departmentDescription) {
		this.departmentDescription = departmentDescription;
	}

	public Long getDepartmentParentId() {
		return departmentParentId;
	}

	public void setDepartmentParentId(Long departmentParentId) {
		this.departmentParentId = departmentParentId;
	}

	public int getParentDepartmentRadio() {
		return parentDepartmentRadio;
	}

	public void setParentDepartmentRadio(int parentDepartmentRadio) {
		this.parentDepartmentRadio = parentDepartmentRadio;
	}

	
	
	public Long getDepartmentPrevId() {
		return departmentPrevId;
	}

	public void setDepartmentPrevId(Long departmentPrevId) {
		this.departmentPrevId = departmentPrevId;
	}

	@Override
	public String toString() {
		return "DepartmentRegistPostDTO [departmentName=" + departmentTitle
				+ ", departmentDescription=" + departmentDescription
				+ ", departmentParentId=" + departmentParentId
				+ ", parentDepartmentRadio=" + parentDepartmentRadio + "]";
	}

	
	
}
