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
@Table(name = "USER_APPLE_TOKEN")
public class UserAppleToken  {

	
	public UserAppleToken(){
		this.createDate = new Date();
	}
	
	@Id
	@Column(name = "id", columnDefinition="bigint(20)" )
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Column(name = "device_token")
	private String deviceToken;

	@Column(name = "device_family")
	String deviceFamily;
	
	@Column(name = "device_name")
	String deviceName;
	
	@Column(name = "device_model")
	String deviceModel;
	
	@Column(name = "os_version")
	String osVersion;
	
	@Column(name = "app_version")
	String appVersion;
	
	@Column(name = "device_uuid")
	String deviceUUID;
	
	@Column(name = "createDate")
	private Date createDate;

	@Column(name = "is_production", columnDefinition="boolean default false")
	private Boolean production;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public String getDeviceFamily() {
		return deviceFamily;
	}

	public void setDeviceFamily(String deviceFamily) {
		this.deviceFamily = deviceFamily;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getDeviceUUID() {
		return deviceUUID;
	}

	public void setDeviceUUID(String deviceUUID) {
		this.deviceUUID = deviceUUID;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Boolean getProduction() {
		return production;
	}

	public void setProduction(Boolean production) {
		this.production = production;
	}


}