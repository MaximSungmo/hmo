package kr.co.sunnyvale.sunny.domain;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import kr.co.sunnyvale.sunny.util.StringUtils;

@Entity
@Table(name = "AUTH_TOKEN")
public class AuthToken {

	public static final String TYPE_PASSWORD = "password";            
	public static final String TYPE_ACTIVATE_ADMIN = "activate_admin";
	public static final String TYPE_ACTIVATE = "activate";
	public static final String TYPE_FELLOW = "invite";
	public static final String TYPE_SITE_INACTIVE_USER_CONFIRM= "site_inactive_user_confirm";
	public static final String TYPE_SITE_INACTIVE_USER_INVITE= "site_inactive_user_invite";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(name = "value")
	private String value;
	
	@Column(name = "create_date")
	@JsonIgnore
	private Date createDate;

	@Column(name = "expire_date")
	@JsonIgnore
	private Date expireDate;
	
	@Column(name = "type")
	private String type;

	@Column(name = "extra_data_1")
	@JsonIgnore
	private String extraData1;

	@OneToOne(fetch = FetchType.LAZY)
	private User user;
	
	@OneToOne(fetch = FetchType.LAZY)
	private Site site;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private SiteInactiveUser siteInactiveUser;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private SiteInactive siteInactive;
	
	public AuthToken(){
		Date  currentDate = new Date();
		Date afterDate = new Date();
		afterDate.setTime(currentDate.getTime() + 1800000);
		this.setcreateDate(currentDate);
		
		// 1800000 은 Millsecond 로 30분 뒤에 만료되는 것을 뜻한다.

		this.setExpireDate(afterDate);
	}
	public AuthToken(User user, String type) {
		this();
		this.user = user;
		this.type = type;
		this.value = makeConfirmCode();
	}

	public AuthToken(SiteInactiveUser siteInactiveUser, String type) {
		this();
		this.siteInactiveUser = siteInactiveUser;
		this.type = type;
		this.value = makeConfirmCode();
	}
	private String makeConfirmCode() {
		int valueLength = 32;
		return StringUtils.genRandomString(valueLength);
	}
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Date getcreateDate() {
		return createDate;
	}

	public void setcreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getExtraData1() {
		return extraData1;
	}
	public void setExtraData1(String extraData1) {
		this.extraData1 = extraData1;
	}
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public SiteInactiveUser getSiteInactiveUser() {
		return siteInactiveUser;
	}
	public void setSiteInactiveUser(SiteInactiveUser siteInactiveUser) {
		this.siteInactiveUser = siteInactiveUser;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AuthToken other = (AuthToken) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	public Site getSite() {
		return site;
	}
	public void setSite(Site site) {
		this.site = site;
	}
	public SiteInactive getSiteInactive() {
		return siteInactive;
	}
	public void setSiteInactive(SiteInactive siteInactive) {
		this.siteInactive = siteInactive;
	}

}
