package kr.co.sunnyvale.sunny.domain;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "EXCEPTION_LOG")
public class ExceptionLog {
	

	public ExceptionLog(){
		this.setCreateDate(new Date());
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "user_id")
	private Long userId;
	
	@Column(name = "site_id")
	private Long siteId;
	

	@Column(name = "requestDump")
	@Type(type="text")
	private String requestDump;

	@Column(name = "exceptionMessage")
	@Type(type="text")
	private String exceptionMessage;
	
	@Column(name = "stackTrace")
	@Type(type="text")
	private String stackTrace;
	
	@Column(name = "create_date")
	private Date createDate;

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


	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getSiteId() {
		return siteId;
	}

	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}

	public String getRequestDump() {
		return requestDump;
	}

	public void setRequestDump(String requestDump) {
		this.requestDump = requestDump;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

	public String getStackTrace() {
		return stackTrace;
	}

	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}

	@Override
	public String toString() {
		return "ExceptionLog [id=" + id + ", userId=" + userId + ", siteId="
				+ siteId + ", requestDump=" + requestDump
				+ ", exceptionMessage=" + exceptionMessage + ", stackTrace="
				+ stackTrace + ", createDate=" + createDate + "]";
	}
	
	
}
