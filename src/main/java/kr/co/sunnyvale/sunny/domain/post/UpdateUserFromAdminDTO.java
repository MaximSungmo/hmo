package kr.co.sunnyvale.sunny.domain.post;

public class UpdateUserFromAdminDTO {
	
	private String jobTitle1;
	private String jobTitle2;
	private String jobTitle3;
	
	private String adminComment;
	
	private Integer status;
	
	private Boolean admin;

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

	public String getAdminComment() {
		return adminComment;
	}

	public void setAdminComment(String adminComment) {
		this.adminComment = adminComment;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}
	
	
	
	
	
}

