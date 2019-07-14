package kr.co.sunnyvale.sunny.domain.post;

import kr.co.sunnyvale.sunny.domain.validator.constraints.ValidPassword;

public class PasswordPost {
	
	@ValidPassword
	private String currentPassword;

	@ValidPassword
	private String newPassword;
	
	@ValidPassword
	private String confirmPassword;

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	
}