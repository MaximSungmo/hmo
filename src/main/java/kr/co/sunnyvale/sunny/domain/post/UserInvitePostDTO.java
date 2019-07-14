package kr.co.sunnyvale.sunny.domain.post;


public class UserInvitePostDTO {

	private String email;
	
	private String inviteMessage;
	
	private String name; 
	
	private boolean admin;
	
	private String jobTitle1;
	
	private String jobTitle2;
	
	private String jobTitle3;

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	

	public String getJobTitle1() {
		return jobTitle1;
	}

	public void setJobTitle1(String jobTitle1) {
		this.jobTitle1 = jobTitle1;
	}

	public String getJobTitle2() {
		return jobTitle2;
	}

	public void setJobTitle2(String jobTitle2) {
		this.jobTitle2 = jobTitle2;
	}

	public String getJobTitle3() {
		return jobTitle3;
	}

	public void setJobTitle3(String jobTitle3) {
		this.jobTitle3 = jobTitle3;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getInviteMessage() {
		return inviteMessage;
	}

	public void setInviteMessage(String inviteMessage) {
		this.inviteMessage = inviteMessage;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
}
