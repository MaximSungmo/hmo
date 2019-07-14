package kr.co.sunnyvale.sunny.domain.dto;


public class ChannelUserDTO {
	
	private long id;

	private String email;
	
	private String name;
	
	private String profilePic;
	
	private Long lastReadMessageInfoId;

	
	public long getId() {
		return id;
	}

	public void setId(long id) {
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

	public Long getLastReadMessageInfoId() {
		return lastReadMessageInfoId;
	}

	public void setLastReadMessageInfoId(Long lastReadMessageInfoId) {
		this.lastReadMessageInfoId = lastReadMessageInfoId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	
}
