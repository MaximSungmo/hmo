package kr.co.sunnyvale.sunny.domain.post;


public class UpdateProfileUser {

	private String name;
	
	private String email;
	
	private Integer status;
	
	private String statusMessage;
	
	private String phone;						// 전화번호  010-0000-0000
	
	private String jobTitle1;
	
	private String jobTitle2;
	
	private String jobTitle3;
	
	private String innercall;
	
	private Long profilePicId;
	
	private String coverPic; 
	
	
	public UpdateProfileUser(){
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}




	public Long getProfilePicId() {
		return profilePicId;
	}


	public void setProfilePicId(Long profilePicId) {
		this.profilePicId = profilePicId;
	}


	public String getCoverPic() {
		return coverPic;
	}


	public void setCoverPic(String coverPic) {
		this.coverPic = coverPic;
	}


	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getStatusMessage() {
		return statusMessage;
	}


	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}


	public String getInnercall() {
		return innercall;
	}


	public void setInnercall(String innercall) {
		this.innercall = innercall;
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



}