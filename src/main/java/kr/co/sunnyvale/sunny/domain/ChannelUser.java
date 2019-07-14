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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "CHANNEL_USER")
public class ChannelUser  {

	@Id
	@Column(name = "id", columnDefinition="bigint(20)" )
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "channel_id")
	private Channel channel;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Column(name="is_owner", columnDefinition="boolean default false")
	private Boolean isOwner; 
	
//	@Column(name="unread_count", columnDefinition="integer default 0")
//	private Integer unreadCount = 0;
	
	/**
	 * 숨겨진 채팅방은 채팅방 리스트에서 보이지 않는다. 아카이브나 마찬가지임.
	 * 숨겨진 채팅방은 현재 1:1 방에서만 지원한다.
	 * 1:1 채팅방에서 '나가기'는 숨기기나 마찬가지다. 
	 * 1:1 채팅방에서 '나가기'를 하면 이전의 모든 메시지는 숨겨지거나 삭제된다.
	 * 
	 * 그룹 채팅방에서 '나가기'는 실제 삭제하기다.
	 * 그룹 채팅방에서 '나가기'를 하면 이전의 모든 메시지는 삭제된다. 
	 */
	@Column(name="is_hidden", columnDefinition="boolean default false")
	private Boolean isHidden;
	
	@Column(name="last_read_message_info_id")
	private Long lastReadMessageInfoId;
	
	@Column(name="prev_read_message_info_id")
	private Long prevReadMessageInfoId;
	
	@Column(name="last_read_date")
	private Date lastReadDate; 
	
	/* 
	 * 최초 채팅 참여시간
	 */
	@Column(name = "create_date")
	private Date createDate;

	/*
	 * 입장시간
	 */
	@Column(name = "start_date")
	private Date startDate;

	/*
	 * 대화방에서 나간 시간
	 */
	@Column(name = "out_date")
	private Date outDate;

	@Transient
	private boolean updateFlag = false;
	
	public ChannelUser(){
		this.setCreateDate(new Date());
		this.setStartDate(new Date());
	}
	
	public ChannelUser( Long userId, Long channelId ) {
		this();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getOutDate() {
		return outDate;
	}

	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Boolean getIsOwner() {
		return isOwner;
	}

	public void setIsOwner(Boolean isOwner) {
		this.isOwner = isOwner;
	}

//	public Integer getUnreadCount() {
//		return unreadCount;
//	}
//
//	public void setUnreadCount(Integer unreadCount) {
//		this.unreadCount = unreadCount;
//	}

	public Date getLastReadDate() {
		return lastReadDate;
	}

	public void setLastReadDate(Date lastReadDate) {
		this.lastReadDate = lastReadDate;
	}

	public Long getLastReadMessageInfoId() {
		return lastReadMessageInfoId;
	}

	public void setLastReadMessageInfoId(Long lastReadMessageInfoId) {
		this.lastReadMessageInfoId = lastReadMessageInfoId;
	}

	public Long getPrevReadMessageInfoId() {
		return prevReadMessageInfoId;
	}

	public void setPrevReadMessageInfoId(Long prevReadMessageInfoId) {
		this.prevReadMessageInfoId = prevReadMessageInfoId;
	}

	public Boolean getIsHidden() {
		return isHidden;
	}

	public void setIsHidden(Boolean isHidden) {
		this.isHidden = isHidden;
	}

	public boolean isUpdateFlag() {
		return updateFlag;
	}

	public void setUpdateFlag(boolean updateFlag) {
		this.updateFlag = updateFlag;
	}

	@Override
	public String toString() {
		return "ChannelUser [tmpUserId="+ user.getId() + " id=" + id + ", isOwner=" + isOwner + ", isHidden="
				+ isHidden + ", lastReadMessageInfoId=" + lastReadMessageInfoId
				+ ", prevReadMessageInfoId=" + prevReadMessageInfoId
				+ ", lastReadDate=" + lastReadDate + ", createDate="
				+ createDate + ", startDate=" + startDate + ", outDate="
				+ outDate + ", updateFlag=" + updateFlag + "]";
	}


}