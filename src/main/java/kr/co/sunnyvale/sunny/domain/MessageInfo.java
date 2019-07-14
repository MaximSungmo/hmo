package kr.co.sunnyvale.sunny.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "MESSAGE_INFO")
public class MessageInfo {
	
	public MessageInfo(){
		this.setType(Message.TYPE_NORMAL);
		this.setCreateDate(new Date());
	}
	
	@Id
	@Column(name = "id", columnDefinition="bigint(20)" )
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "raw_text")
	@Type(type="text")
	private String rawText;
	
	@Column(name = "tagged_text")
	@Type(type="text")
	protected String taggedText;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "sender_id")
	private User sender;
	
	/**
	 * 행동의 주체. 예를 들면 친구 초대를 했다거나 강퇴했을 때 액션을 취한 사람이 된다.
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "subject_user_id")
	private User subjectUser;
	
	
	
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "USER_MESSAGE_INFO_OBJECTED", joinColumns = { 
			@JoinColumn(name = "message_info_id", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "user_id", nullable = false, updatable = false) })
	@BatchSize(size=10)
	private List<User> objectUsers;
	
	
	/**
	 * 일방적으로 당한 사람. 예를 들면 초대된 사람
	 */
//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "object_user_id")
//	private User objectUser;
	

	@Column(name = "type", columnDefinition="integer default 0")
	private Integer type;
	
	@Column(name = "create_date")
	private Date createDate;
	
	// ChatRoom 용도 
//	@JsonIgnore
//	@JsonBackReference
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "channel_id")
	private Channel channel;

	@Column(name = "unread_count", columnDefinition="int default 0")
	private int unreadCount = 0;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRawText() {
		return rawText;	
		
	}

	public void setRawText(String rawText) {
		this.rawText = rawText;
	}

	public String getText() {
		return rawText;
	}
	public void setText(String text ){
		this.rawText = text; 
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}


	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

//	public User getLastUser() {
//		return lastUser;
//	}
//
//	public void setLastUser(User lastUser) {
//		this.lastUser = lastUser;
//	}
//
//	public String getLastUserName() {
//		return lastUserName;
//	}
//
//	public void setLastUserName(String lastUserName) {
//		this.lastUserName = lastUserName;
//	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public User getSubjectUser() {
		return subjectUser;
	}

	public void setSubjectUser(User subjectUser) {
		this.subjectUser = subjectUser;
	}

//	public User getObjectUser() {
//		return objectUser;
//	}
//
//	public void setObjectUser(User objectUser) {
//		this.objectUser = objectUser;
//	}

	public int getUnreadCount() {
		return unreadCount;
	}

	public void setUnreadCount(int unreadCount) {
		this.unreadCount = unreadCount;
	}

	public void addUnreadCount() {
		this.unreadCount++;
	}

	public void delUnreadCount() {
		this.unreadCount--;
	}

	public List<User> getObjectUsers() {
		return objectUsers;
	}

	public void setObjectUsers(List<User> objectUsers) {
		this.objectUsers = objectUsers;
	}

	public String getTaggedText() {
		return taggedText;
	}

	public void setTaggedText(String taggedText) {
		this.taggedText = taggedText;
	}

}
