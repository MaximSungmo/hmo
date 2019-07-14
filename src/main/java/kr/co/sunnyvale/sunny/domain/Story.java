package kr.co.sunnyvale.sunny.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import kr.co.sunnyvale.sunny.domain.dto.NotifyDTO;
import kr.co.sunnyvale.sunny.redis.MessageType;

@Entity
@Table(name = "STORY")
@PrimaryKeyJoinColumn(name="id")
public class Story extends Content {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7354365112377128609L;
	
	@Column(name="is_feedback", columnDefinition="boolean default false")
	private boolean feedback;

	public Story(){
		super(null, Content.TYPE_STORY);
	}
	
	public Story( Site site ){
		super(site, Content.TYPE_STORY);
	}
	public Story(Long storyId) {
		this.setId(storyId);
	}

	public NotifyDTO toNotifyDto() {
		NotifyDTO notificationDto = new NotifyDTO();
		
		if( super.isNotice() ){
			notificationDto.setTypeName(MessageType.NOTIFICATION.getTypeName());
		}else{
			notificationDto.setTypeName(MessageType.NOTICE.getTypeName());
		}
		
		notificationDto.setId( this.id );
		notificationDto.setLink( "/story/" + this.id );
		notificationDto.setName(this.getUser().getName());
		notificationDto.setStrippedMessage( this.getStrippedSnippetText() );
		notificationDto.setThumbnail( this.getUser().getProfilePic() );
		notificationDto.setUpdateDate( this.getCreateDate() );
		return notificationDto;
	}
	

	public boolean isFeedback() {
		return feedback;
	}

	public void setFeedback(boolean feedback) {
		this.feedback = feedback;
	}
	
}
