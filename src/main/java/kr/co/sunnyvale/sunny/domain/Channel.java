package kr.co.sunnyvale.sunny.domain;

import java.io.Serializable;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import kr.co.sunnyvale.sunny.domain.dto.NotifyDTO;
import kr.co.sunnyvale.sunny.redis.MessageType;
import kr.co.sunnyvale.sunny.util.StringEscapeUtils;

import org.hibernate.annotations.BatchSize;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "CHANNEL")
@org.hibernate.annotations.Entity(
		dynamicUpdate = true
)
public class Channel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3352250777861150997L;
	public static int ONETOONE = 0;
	public static int GROUP = 1;
	
	public Channel(){
		this.setCreateDate(new Date());
		this.setUpdateDate(new Date());
	}
	
	public Channel(Long channelId) {
		setId(channelId);
	}

	@Id
	@Column(name = "id", columnDefinition="bigint(20)" )
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToMany(targetEntity = User.class, cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
	@JoinTable(name = "CHANNEL_USER", joinColumns = { 
			@JoinColumn(name = "channel_id", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "user_id", nullable = false, updatable = false) })
	@BatchSize(size=10)
	private List<User> users;

	@OneToOne( fetch = FetchType.LAZY )
	private User creator;
	
	@OneToOne( fetch=FetchType.LAZY)
	@JoinColumn( name="last_message_info" )
	private MessageInfo lastMessageInfo;
	
	@JsonIgnore
	@OneToMany(mappedBy = "channel", fetch = FetchType.LAZY)
	protected List<ChannelUser> userRelation;

	@Column(name = "is_hidden", columnDefinition="boolean default true")
	private boolean isHidden;
	
	@Column(name = "exist_invited_user", columnDefinition="boolean default false")
	private boolean existInvitedUser;
	
	@Column(name = "last_text_snippet")	
	private String lastTextSnippet;
	
	@Column(name = "last_escape_html_snippet")	
	private String lastEscapeHTMLSnippet;
	
	@Column(name = "last_user_name")
	private String lastUserName;
	
	@Column(name = "last_user_profile_pic")
	private String lastUserProfilePic;
	
//	@OneToOne( fetch=FetchType.LAZY)
//	@JoinColumn( name="last_message" )
//	private MessageInfo lastMessage;

	@OneToMany(mappedBy = "channel", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
	private List<Message> messages;
	
	@OneToMany(mappedBy = "channel", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
	private List<MessageInfo> messageInfos;
	
	@OneToOne( fetch = FetchType.LAZY )
	private User user1;
	
	@OneToOne( fetch = FetchType.LAZY )
	private User user2;
	
	@Column(name = "title")
	private String title;
	
	/*
	 * 0 : 1:1
	 * 1 : group
	 */
	@Column(name = "type", columnDefinition="integer default 0")
	private Integer type;
	
	@Column(name = "user_count", columnDefinition="integer default 0")
	private Integer userCount;

	@Column(name = "max_user_count", columnDefinition="integer default 0")
	private Integer maxUserCount;
	
	@Column(name = "create_date")
	private Date createDate;
	
	@Column(name = "update_date")
	protected Date updateDate;
	
	@Transient
	List<User> tmpJoinedUsers;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}



//	public MessageInfo getLastMessage() {
//		return lastMessage;
//	}
//
//	public void setLastMessage(MessageInfo lastMessage) {
//		this.lastMessage = lastMessage;
//	}

	@JsonIgnore
	public List<Message> getMessages() {
		return messages;
	}

	@JsonIgnore
	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public Integer getUserCount() {
		return userCount;
	}

	public void setUserCount(Integer userCount) {
		this.userCount = userCount;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getMaxUserCount() {
		return maxUserCount;
	}

	public void setMaxUserCount(Integer maxUserCount) {
		this.maxUserCount = maxUserCount;
	}

	public User getUser1() {
		return user1;
	}

	public void setUser1(User user1) {
		this.user1 = user1;
	}

	public User getUser2() {
		return user2;
	}

	public void setUser2(User user2) {
		this.user2 = user2;
	}

	public String getLastEscapeHTMLSnippet() {
		
		
		
		
		if( lastEscapeHTMLSnippet == null ){
			String escapeString = StringEscapeUtils.escapeHtml( lastTextSnippet );
			return escapeString;
		}
		return lastEscapeHTMLSnippet;
	}
	
	public void setLastEscapeHTMLSnippet(String lastEscapeHTMLSnippet) {
		this.lastEscapeHTMLSnippet = lastEscapeHTMLSnippet;
	}

	public String getLastTextSnippet() {
		return lastTextSnippet;
	}

	public void setLastTextSnippet(String lastTextSnippet) {
		this.lastTextSnippet = lastTextSnippet;
	}

	public String getLastUserName() {
		return lastUserName;
	}

	public void setLastUserName(String lastUserName) {
		this.lastUserName = lastUserName;
	}

	public String getLastUserProfilePic() {
		return lastUserProfilePic;
	}

	public void setLastUserProfilePic(String lastUserProfilePic) {
		this.lastUserProfilePic = lastUserProfilePic;
	}

	public List<ChannelUser> getUserRelation() {
		return userRelation;
	}

	public void setUserRelation(List<ChannelUser> userRelation) {
		this.userRelation = userRelation;
	}

	public List<User> getTmpJoinedUsers() {
		return tmpJoinedUsers;
	}

	public void setTmpJoinedUsers(List<User> tmpJoinedUsers) {
		this.tmpJoinedUsers = tmpJoinedUsers;
	}

	public MessageInfo getLastMessageInfo() {
		return lastMessageInfo;
	}

	public void setLastMessageInfo(MessageInfo lastMessageInfo) {
		this.lastMessageInfo = lastMessageInfo;
	}

	public boolean isHidden() {
		return isHidden;
	}

	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	public NotifyDTO toNotifyDto() {
		NotifyDTO notificationDto = new NotifyDTO();
		
		notificationDto.setTypeName( MessageType.CHAT.getTypeName() );
		
		notificationDto.setId( this.id );
		notificationDto.setLink( "/message/"  + this.id );
		notificationDto.setName( this.getLastUserName() );
		notificationDto.setStrippedMessage( this.getLastTextSnippet() );
		notificationDto.setThumbnail( this.getLastUserProfilePic() );
		notificationDto.setUpdateDate( this.getUpdateDate() );
		return notificationDto;
	}

	public boolean isExistInvitedUser() {
		return existInvitedUser;
	}

	public void setExistInvitedUser(boolean existInvitedUser) {
		this.existInvitedUser = existInvitedUser;
	}



}
