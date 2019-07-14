package kr.co.sunnyvale.sunny.domain;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "MESSAGE")
public class Message {
	
	public static final int TYPE_NORMAL = 0;
	public static final int TYPE_INVITE = 1;
	public static final int TYPE_OUT= 2;
	public static final int TYPE_CREATE = 3;
	
	
	public Message(){
		this.setIsHidden( false );
//		this.setIsRead( false );
	}
	
	@Id
	@Column(name = "id", columnDefinition="bigint(20)" )
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "is_hidden", columnDefinition="boolean default false")
	private Boolean isHidden;
	
//	@Column(name = "isRead", columnDefinition="boolean default false")
//	private Boolean isRead;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "channel_id")
	private Channel channel;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "info_id")
	private MessageInfo info;
	
//	@Transient
//	private int readCount = 0;


	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@JsonIgnore
	public Channel getChannel() {
		return channel;
	}

	@JsonIgnore
	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	@JsonIgnore
	public User getUser() {
		return user;
	}
	@JsonIgnore
	public void setUser(User user) {
		this.user = user;
	}


	public Boolean getIsHidden() {
		return isHidden;
	}

	public void setIsHidden(Boolean isHidden) {
		this.isHidden = isHidden;
	}

//	public Boolean getIsRead() {
//		return isRead;
//	}
//
//	public void setIsRead(Boolean isRead) {
//		this.isRead = isRead;
//	}

	public MessageInfo getInfo() {
		return info;
	}

	public void setInfo(MessageInfo info) {
		this.info = info;
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
		Message other = (Message) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

//	public int getReadCount() {
//		return readCount;
//	}
//
//	public void setReadCount(int readCount) {
//		this.readCount = readCount;
//	}

	
}
