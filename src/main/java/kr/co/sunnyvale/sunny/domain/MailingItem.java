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
import javax.persistence.Transient;

@Entity
@Table(name = "MAILING_ITEM")
public class MailingItem {

	
	public MailingItem(Long userId, Long mailingQueueId) {
		super();
		this.userId = userId;
		this.mailingQueue = new MailingQueue(mailingQueueId);
	}

	public MailingItem(Long userId, MailingQueue mailingQueue) {
		super();
		this.userId = userId;
		this.mailingQueue = mailingQueue;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "create_date")
	private Date createDate;
	
	@Column(name = "receive_email")
	private String receiveEmail;
	
	@Column(name = "is_complete", columnDefinition = "boolean default false")
	private boolean complete;
	
	
	/**
	 * 메일을 받을 사람들 아이디
	 */
	@Transient
	private Long userId;
	
	/**
	 * 메일에 대한 정보가 있는 mailQueue 아이디
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mailing_queue_id")
	private MailingQueue mailingQueue;


	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

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

	public String getReceiveEmail() {
		return receiveEmail;
	}

	public void setReceiveEmail(String receiveEmail) {
		this.receiveEmail = receiveEmail;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public MailingQueue getMailingQueue() {
		return mailingQueue;
	}

	public void setMailingQueue(MailingQueue mailingQueue) {
		this.mailingQueue = mailingQueue;
	}

	
	
	
}
