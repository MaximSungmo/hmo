package kr.co.sunnyvale.sunny.domain;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "LOGIN_INFO")
public class LoginInfo {
	
	public static final int SUCCESS = 1;
	public static final int FAILURE = 2;
	public static final int LOGOUT = 3;

	public LoginInfo(){
		this.setCreateDate(new Date());
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "ip_address")
	private String ipAddress;
	
	@Column(name = "session_id")
	private String sessionId; 
	
	@Column(name = "country")
	private String country;
	
	@Column(name = "browser")
	private String browser;

	@Column(name = "is_miss_password")
	private boolean isMissPassword;
	
	@Column(name = "type")
	private int type;
	
	@Column(name = "create_date")
	private Date createDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "site_id")
	private Site site;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public boolean isMissPassword() {
		return isMissPassword;
	}

	public void setMissPassword(boolean isMissPassword) {
		this.isMissPassword = isMissPassword;
	}

	
	
}
