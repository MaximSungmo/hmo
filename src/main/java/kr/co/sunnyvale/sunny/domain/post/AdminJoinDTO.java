package kr.co.sunnyvale.sunny.domain.post;

import kr.co.sunnyvale.sunny.domain.SiteInactive;
import kr.co.sunnyvale.sunny.domain.SiteInactiveUser;
import kr.co.sunnyvale.sunny.domain.User;


public class AdminJoinDTO {
	
	private String siteCompanyName;
	
	private String siteCompanyPhone;
	
	private int siteEmployeeSize;
	
	private String siteCompanyRegion;
	
	private String userEmail;
	
	private String userName;
	
	private String userPassword;
	
	private String userPasswordConfirm;
	
	private String siteHomepage;
	
	private boolean userAgreeEmail;
	
	private boolean tosAgree;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSiteCompanyName() {
		return siteCompanyName;
	}

	public void setSiteCompanyName(String siteCompanyName) {
		this.siteCompanyName = siteCompanyName;
	}

	public String getSiteCompanyPhone() {
		return siteCompanyPhone;
	}

	public void setSiteCompanyPhone(String siteCompanyPhone) {
		this.siteCompanyPhone = siteCompanyPhone;
	}


	public int getSiteEmployeeSize() {
		return siteEmployeeSize;
	}

	public void setSiteEmployeeSize(int siteEmployeeSize) {
		this.siteEmployeeSize = siteEmployeeSize;
	}

	public String getSiteCompanyRegion() {
		return siteCompanyRegion;
	}

	public void setSiteCompanyRegion(String siteCompanyRegion) {
		this.siteCompanyRegion = siteCompanyRegion;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserPasswordConfirm() {
		return userPasswordConfirm;
	}

	public void setUserPasswordConfirm(String userPasswordConfirm) {
		this.userPasswordConfirm = userPasswordConfirm;
	}

	public boolean isUserAgreeEmail() {
		return userAgreeEmail;
	}

	public void setUserAgreeEmail(boolean userAgreeEmail) {
		this.userAgreeEmail = userAgreeEmail;
	}

	public boolean isTosAgree() {
		return tosAgree;
	}

	public void setTosAgree(boolean tosAgree) {
		this.tosAgree = tosAgree;
	}
	
	

	public String getSiteHomepage() {
		return siteHomepage;
	}

	public void setSiteHomepage(String siteHomepage) {
		this.siteHomepage = siteHomepage;
	}

	public SiteInactiveUser parseToSiteInactiveUser(){
		SiteInactiveUser user = new SiteInactiveUser();
		user.setName(getUserName());
		user.setPassword(getUserPassword());
		user.setEmail(getUserEmail());
		user.setAgreeEmail(isUserAgreeEmail());
		return user;
	}
	
	public SiteInactive parseToSiteInactive(){
		SiteInactive siteInactive = new SiteInactive();
		siteInactive.setCompanyName(getSiteCompanyName());
		siteInactive.setHomepage(getSiteHomepage());
		siteInactive.setCompanyPhone(getSiteCompanyPhone());
		siteInactive.setCompanyRegion(getSiteCompanyRegion());
		siteInactive.setEmployeeSize(getSiteEmployeeSize());
		return siteInactive;
	}
	
}
