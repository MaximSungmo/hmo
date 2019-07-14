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

@Entity
@Table(name = "LAST_READ")
public class LastRead {

	public LastRead(){

	}
	
	public LastRead(User user) {
		this();
		setUser(user);		
	}

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;

	@Column(name = "noti_last_read")
	private Date notificationLastRead;

	@Column(name = "friend_request_last_read")
	private Date friendRequestLastRead;

	@Column(name = "notice_last_read")
	private Date noticeLastRead;
	
	@Column(name = "message_last_read")
	private Date messageLastRead;
	
//	@Column(name = "plaza_last_read")
//	private Date plazaLastRead;
//	
//	@Column(name = "question_last_read_count", columnDefinition="integer default 0")
//	private int questionLastReadCount;
//	
//	@Column(name = "note_last_read_count", columnDefinition="integer default 0")
//	private int noteLastReadCount;
	
	
	public void updateNotificationDate(){
		this.setNotificationLastRead( new Date() );
	}
	
	public void updateNoticeDate(){
		this.setNoticeLastRead( new Date() );
	}
	
	public void updateFriendRequestDate(){
		this.setFriendRequestLastRead( new Date() );
	}
	public void updateMessageDate(){
		this.setMessageLastRead( new Date() );
	}
//	public void updatePlazaDate(){
//		this.setPlazaLastRead( new Date() );
//	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getNotificationLastRead() {
		return notificationLastRead;
	}

	public void setNotificationLastRead(Date notificationLastRead) {
		this.notificationLastRead = notificationLastRead;
	}

	public Date getFriendRequestLastRead() {
		return friendRequestLastRead;
	}

	public void setFriendRequestLastRead(Date friendRequestLastRead) {
		this.friendRequestLastRead = friendRequestLastRead;
	}

	public Date getMessageLastRead() {
		return messageLastRead;
	}

	public void setMessageLastRead(Date messageLastRead) {
		this.messageLastRead = messageLastRead;
	}

	public Date getNoticeLastRead() {
		return noticeLastRead;
	}

	public void setNoticeLastRead(Date noticeLastRead) {
		this.noticeLastRead = noticeLastRead;
	}

//	public Date getPlazaLastRead() {
//		return plazaLastRead;
//	}
//
//	public void setPlazaLastRead(Date plazaLastRead) {
//		this.plazaLastRead = plazaLastRead;
//	}
//
//	public int getQuestionLastReadCount() {
//		return questionLastReadCount;
//	}
//
//	public void setQuestionLastReadCount(int questionLastReadCount) {
//		this.questionLastReadCount = questionLastReadCount;
//	}
//
//	public int getNoteLastReadCount() {
//		return noteLastReadCount;
//	}
//
//	public void setNoteLastReadCount(int noteLastReadCount) {
//		this.noteLastReadCount = noteLastReadCount;
//	}
//	

}
