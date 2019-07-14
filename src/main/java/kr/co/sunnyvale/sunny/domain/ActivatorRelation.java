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
@Table(name = "ACTIVATOR_RELATION")
public class ActivatorRelation {

	public ActivatorRelation(){
		this.setCreateDate(new Date());
	}
	
	public ActivatorRelation(User user) {
		this();
		this.setUser( user );
		this.setName( user.getName() );
	}

	@Id
	@Column(name = "id", columnDefinition="bigint(20)" )
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne( fetch = FetchType.LAZY )
	@JoinColumn(name = "user_id")
	private User user;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "basecamp_name")
	private String basecampName;
	
	@ManyToOne( fetch = FetchType.LAZY )
	@JoinColumn(name = "notification_id")
	private Notification notification;

	@Column(name = "create_date")
	private Date createDate;
	
	
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBasecampName() {
		return basecampName;
	}

	public void setBasecampName(String basecampName) {
		this.basecampName = basecampName;
	}

	public Notification getNotification() {
		return notification;
	}

	public void setNotification(Notification notification) {
		this.notification = notification;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	
}
