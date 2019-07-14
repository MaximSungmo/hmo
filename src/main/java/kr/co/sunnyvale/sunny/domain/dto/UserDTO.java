package kr.co.sunnyvale.sunny.domain.dto;

import java.util.List;

public class UserDTO {
	
	private Long id;
	
	private String name;
	
	private String profilePic;
	
	private List<String> departmentStrings;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public List<String> getDepartmentStrings() {
		return departmentStrings;
	}

	public void setDepartmentStrings(List<String> departmentStrings) {
		this.departmentStrings = departmentStrings;
	}
	
	
	
}
