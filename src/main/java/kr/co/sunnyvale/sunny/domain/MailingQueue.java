package kr.co.sunnyvale.sunny.domain;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "MAILING_QUEUE")
public class MailingQueue {
	
	public final static int STATUS_WAITING = 0; 
	public final static int STATUS_PROGRESS = 1;
	public final static int STATUS_COMPLETE = 2;
	
	
	public MailingQueue(){
		this.setCreateDate(new Date());
	}
	
	public MailingQueue(Long mailingQueueId) {
		this();
		this.id = mailingQueueId;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "create_date")
	private Date createDate;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;
	

	@Column(name = "sendEmail")
	private String sendEmail;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "raw_text")
	@Type(type="text")
	protected String text;

	@Column(name = "is_only_admin", columnDefinition="boolean default false")
	private boolean onlyAdmin;
	
	@Column(name = "status", columnDefinition="integer default 0")
	private int status;
	
	@Column( name="site_id" )
	protected Long siteId;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isOnlyAdmin() {
		return onlyAdmin;
	}

	public void setOnlyAdmin(boolean onlyAdmin) {
		this.onlyAdmin = onlyAdmin;
	}


	public String getSendEmail() {
		return sendEmail;
	}

	public void setSendEmail(String sendEmail) {
		this.sendEmail = sendEmail;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Long getSiteId() {
		return siteId;
	}

	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}

	
}