package kr.co.sunnyvale.sunny.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import kr.co.sunnyvale.sunny.domain.validator.constraints.ValidEmail;
import kr.co.sunnyvale.sunny.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "SITE_INACTIVE_USER")
public class SiteInactiveUser  {

	public static final int STATUS_REQUEST=0;
	public static final int STATUS_SEND_EMAIL=1;
	public static final int STATUS_DENY=2;
	public static final int STATUS_COMPLETE=3;
	
	public static final int TYPE_REQUEST=0;
	public static final int TYPE_INVITE=1;
	
	public SiteInactiveUser(){
		this.setCreateDate(new Date());
		this.setUpdateDate(new Date());
		this.setSalt(StringUtils.genRandomString( 10 ));
	}
	
	public SiteInactiveUser(Long id) {
		this.setId(id);
	}

	@Id
	@Column(name = "id", columnDefinition="bigint(20)" )
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "site_id")
	private Site site;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "site_inactive_id")
	private SiteInactive siteInactive;

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "user_id")
//	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "invite_user_id")
	private User inviteUser;

	
	@Column( name="invite_message")
	private String inviteMessage; 
	
	@Column( name="job_title_1")
	private String jobTitle1;
	
	@Column( name="job_title_2")
	private String jobTitle2;
	
	@Column( name="job_title_3")
	private String jobTitle3;

	
	@Column(name = "create_date")
	private Date createDate;

	@Column(name = "update_date")
	private Date updateDate;
	
	@Column(name = "request_message")
	private String requestMessage;
	
	@Column(name = "status", columnDefinition="integer default 0")
	private int status;
	
	@Column(name = "type", columnDefinition="integer default 0")
	private int type;
	
	
	@Column(name = "agreeEmail", columnDefinition="boolean default false")
	private boolean agreeEmail;
	
	@Column(name = "is_admin", columnDefinition="boolean default false")
	private boolean isAdmin;
	
	@ValidEmail
	@Column( name="email")
	protected String email;

//	@ValidName
	@Column( name="name", length=40 )
	protected String name;
	
//	@ValidPassword
	@Column( name="password", length=64 )
	protected String password;
	
	@Transient
	private String passwordConfirm;

	@Column( name="salt", length=64 )
	@JsonIgnore
	protected String salt;
	
	@OneToMany(mappedBy = "siteInactiveUser", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	@OrderBy("createDate DESC")
	protected List<AuthToken> authTokens;
	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Site getSite() {
		return site;
	}


	public void setSite(Site site) {
		this.site = site;
	}


	public User getInviteUser() {
		return inviteUser;
	}

	public void setInviteUser(User inviteUser) {
		this.inviteUser = inviteUser;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getRequestMessage() {
		return requestMessage;
	}

	public void setRequestMessage(String requestMessage) {
		this.requestMessage = requestMessage;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}


	public boolean isAgreeEmail() {
		return agreeEmail;
	}

	public void setAgreeEmail(boolean agreeEmail) {
		this.agreeEmail = agreeEmail;
	}


	public String getSalt() {
		return salt;
	}


	public void setSalt(String salt) {
		this.salt = salt;
	}

	
	
	public String getInviteMessage() {
		return inviteMessage;
	}

	public void setInviteMessage(String inviteMessage) {
		this.inviteMessage = inviteMessage;
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
	
	

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public User parseToUser() {
		User user = new User();
		user.setEmail(this.getEmail());
		user.setJobTitle1(this.getJobTitle1());
		user.setJobTitle2(this.getJobTitle2());
		user.setJobTitle3(this.getJobTitle3());
		user.setName(this.getName());
		user.setPassword(this.getPassword());
		user.setSalt(this.getSalt());
		user.setAdmin(this.isAdmin);
		user.setSite(this.getSite());
		return user;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public SiteInactive getSiteInactive() {
		return siteInactive;
	}

	public void setSiteInactive(SiteInactive siteInactive) {
		this.siteInactive = siteInactive;
	}

	
	
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Override
	public String toString() {
		return "SiteInactiveUser [id=" + id + ", site=" + site
				+ ", siteInactive=" + siteInactive + ", inviteUser="
				+ inviteUser + ", inviteMessage=" + inviteMessage
				+ ", jobTitle1=" + jobTitle1 + ", jobTitle2=" + jobTitle2
				+ ", jobTitle3=" + jobTitle3 + ", createDate=" + createDate
				+ ", requestMessage=" + requestMessage + ", status=" + status
				+ ", type=" + type + ", agreeEmail=" + agreeEmail
				+ ", isAdmin=" + isAdmin + ", email=" + email + ", name="
				+ name + ", passwordConfirm="
				+ ", salt=" + salt + ", authTokens="
				+ authTokens + "]";
	}


}
