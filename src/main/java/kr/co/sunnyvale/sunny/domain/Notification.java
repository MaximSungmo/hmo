package kr.co.sunnyvale.sunny.domain;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import kr.co.sunnyvale.sunny.domain.dto.NotifyDTO;
import kr.co.sunnyvale.sunny.redis.MessageType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "NOTIFICATION")
public class Notification {
	
	public final static int DEFAULT_SNIPPET_SIZE = 25;

	public Notification(){
		this.setCreateDate(new Date());
		this.setUpdateDate(new Date());
		this.setRead(false);
	}
	
	public Notification(long notificationId) {
		this.setId(notificationId);
	}

	@Id
	@Column(name = "id", columnDefinition="bigint(20)" )
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	/*
	 * 모든 노티의 근본이 되는 사람. 
	 * 예 : 
	 * 1. 스토리에 댓글을 단 경우 -> 스토리의 주인
	 * 2. Q&A 에서 답변에 댓글을 단 경우 -> Question 의 주인
	 * 3. Q&A 에서 Question 에 댓글을 단 경우 -> Question 의 주인
	 * 4. Q&A 에서 답변의 댓글에 평가를 한 경우 -> Answer 의 주인( 본래 Question 의 주인이 되는 것이 맞지만 현재 Question 과 Answer 를 같이 보고 있다. 좀 어렵죠? )
	 * 	
	 */
	@Column(name = "owner_id")
	private Long ownerId;
	
	@Column(name = "owner_name")
	private String ownerName;
	
	
	/*
	 * 노티를 받는 사람. 내 댓글 후의 댓글을 단 경우를 제외하면 owner 와 receiver 는 동일하다.
	 * 1. 스토리에 댓글을 단 경우 -> 스토리의 주인
	 * 2. Q&A 에서 답변에 댓글을 단 경우 -> Answer 의 주인
	 * 3. Q&A 에서 답변의 댓글에 평가를 한 경우 -> 댓글의 주인
	 */	
	
//	@OneToMany( mappedBy = "notification", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
//	protected List<ReceiverRelation> receiverRelations;

	@ManyToOne( fetch = FetchType.LAZY )
	@JoinColumn(name = "receiver_id")
	private User receiver;
	
//	@Column(name = "receiver_name")
//	private String receiverName;
//	
//	@Column(name ="receiver_basecamp_name")
//	private String receiverBasecampName;

	/*
	 *	행동을 한 사람 
	 */
	@Column(name = "activator_id")
	private Long activatorId;
	
	@Column(name = "activator_name")
	private String activatorName;
	
	@Column(name ="activator_profile_pic")
	private String activatorProfilePic;
	
	@Column(name ="activator_email")
	private String activatorEmail;
	
	/*
	 * 행동을 한 사람의 여유분들. 예 : 임성묵, 안대혁, 김성박님 외 4명 할 때 안대혁과 김성박
	 */
	
	@Column(name = "extra1_id")
	private Long extra1Id;
	
	@Column(name = "extra1_name")
	private String extra1Name;
	
	@Column(name ="extra1_profile_pic")
	private String extra1ProfilePic;

	
	@Column(name = "extra2_id")
	private Long extra2Id;
	
	@Column(name = "extra2_name")
	private String extra2Name;
	
	@Column(name ="extra2_profile_pic")
	private String extra2ProfilePic;
	
	@OneToMany(mappedBy = "notification", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	private List<ActivatorRelation> activators;
	
	@Column(name = "activator_count", columnDefinition="integer default 0")
	private int activatorCount;
	
//	@Transient
//	private int newActivatorCount;
	
	@Column(name = "activity_type")
	private Integer activityType;
	
	
	/*
	 * 행동한 컨텐츠 아이디
	 */
	@Column(name = "activate_content_id")
	private Long activateContentId;
	
	/*
	 * 노티를 받는 컨텐츠
	 * 예:
	 * 1. 스토리에 댓글을 단 경우 -> 스토리
	 * 2. 스토리의 댓글에 평가를 한 경우 -> 댓글
	 * 3. Q&A 의 답변의 댓글에 평가를 한 경우 -> 댓글
	 * 4. Q&A 의 답변의 댓글 다음에 댓글을 단 경우 -> 내 위의 댓글들 혹은 답변
	 * 5. Q&A 의 Question 에 답글을 단 경우 -> Question
	 */
	@Column(name = "target_content_id")
	private Long targetContentId;

	@Column(name = "target_content_type")
	private Integer targetContentType;
	
	@Column(name = "target_content_snippet")
	private String targetContentSnippet;
	
	/*
	 * 노티를 받는 컨텐츠의 부모
	 * 아무리 댓글이 노티를 받는다 해도 거기에 타고 들어가려면 스토리, qna 등의 주소로 먼저 들어가야한다.
	 * 
	 * 1. 스토리에 댓글을 단 경우 -> 스토리
	 * 2. Q&A의 답변의 댓글에 평가를 한 경우 -> 답변
	 * 3. 스토리의 댓글 다음에 댓글을 단 경우 -> 스토리
	 */
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "content_id")
	private Content content;
	
	@Column(name = "parent_content_id")
	private Long parentContentId;
	
	@Column(name = "parent_content_snippet")
	private String parentContentSnippet;
	
	@Column(name = "parent_content_type")
	private Integer parentContentType;
	
	@Column(name = "feel_id")
	private Integer feelId;
	
	@Column(name = "snippet_text")
	private String snippetText;
	
	@Column(name = "create_date")
	private Date createDate;
	
	@Column(name = "update_date")
	private Date updateDate;
	
	@Column(name = "is_read")
	private boolean read;
	
	@Column(name = "json_string", length=1024)
	private String jsonString;
	
//	@Column(name = "site_id")
//	private Long siteId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public void setOwner(User user) {
		this.setOwnerId( user.getId() );
		this.setOwnerName( user.getName() );
	}
	
	public String getOwnerName() {
		return ownerName;
	}

	private void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}



//	public String getReceiverName() {
//		return receiverName;
//	}
//
//	private void setReceiverName(String receiverName) {
//		this.receiverName = receiverName;
//	}
//
//	public String getReceiverBasecampName() {
//		return receiverBasecampName;
//	}
//
//	private void setReceiverBasecampName(String receiverBasecampName) {
//		this.receiverBasecampName = receiverBasecampName;
//	}

	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	public void setActivator( User user ){
		this.setActivatorId( user.getId() );
		this.setActivatorName( user.getName() );
		this.setActivatorProfilePic( user.getProfilePic() );
	}
	
	public void setActivator(SiteInactiveUser siteInactiveUser) {
		this.setActivatorId(siteInactiveUser.getId());
		this.setActivatorName( siteInactiveUser.getName() );
		this.setActivatorEmail( siteInactiveUser.getEmail() );
	}

	public String getActivatorName() {
		return activatorName;
	}

	private void setActivatorName(String activatorName) {
		this.activatorName = activatorName;
	}


	
	public String getActivatorProfilePic() {
		if( this.activatorProfilePic == null )
			return User.DEFAULT_PROFILE_PIC;

		return activatorProfilePic;
	}

	private void setActivatorProfilePic(String activatorProfilePic) {
		this.activatorProfilePic = activatorProfilePic;
	}

	public Integer getActivityType() {
		return activityType;
	}

	public void setActivityType(Integer activityType) {
		this.activityType = activityType;
	}

	private void setTargetContentSnippet(String text){
		if( text == null )
			return;
		
		if( text.length() < DEFAULT_SNIPPET_SIZE + 1 ){
			this.targetContentSnippet = text;
			return;
		}
		
		this.targetContentSnippet = text.substring(0, DEFAULT_SNIPPET_SIZE) + "...";
	}

	public void setParentContentSnippet(String text){
		if( text == null )
			return;
		
		if( text.length() < DEFAULT_SNIPPET_SIZE + 1 ){
			this.parentContentSnippet = text;
			return;
		}
		
		this.parentContentSnippet = text.substring(0, DEFAULT_SNIPPET_SIZE) + "...";
	}
	
	public List<ActivatorRelation> getActivators() {
		return activators;
	}

	public void setActivators(List<ActivatorRelation> activators) {
		this.activators = activators;
	}

	public int getActivatorCount() {
		
		return activatorCount;
	}

	public void setActivatorCount(int activatorCount) {
		this.activatorCount = activatorCount;
	}

	public Long getTargetContentId() {
		return targetContentId;
	}

	private void setTargetContentId(Long targetContentId) {
		this.targetContentId = targetContentId;
	}

	public void setParentContent(Content content) {
		this.setParentContentId( content.getId() );
		this.setParentContentSnippet( content.getStrippedSnippetText() );
		this.setParentContentType( content.getType() );
	}

	
	public Long getParentContentId() {
		return parentContentId;
	}

	public void setParentContentId(Long parentContentId) {
		this.parentContentId = parentContentId;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getTargetContentSnippet() {
		return targetContentSnippet;
	}

	public String getParentContentSnippet() {
		return parentContentSnippet;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}


	public String getExtra1Name() {
		return extra1Name;
	}

	public void setExtra1Name(String extra1Name) {
		this.extra1Name = extra1Name;
	}


	public String getExtra2Name() {
		return extra2Name;
	}

	public void setExtra2Name(String extra2Name) {
		this.extra2Name = extra2Name;
	}


	public String getExtra1ProfilePic() {
		return extra1ProfilePic;
	}

	public void setExtra1ProfilePic(String extra1ProfilePic) {
		this.extra1ProfilePic = extra1ProfilePic;
	}

	public String getExtra2ProfilePic() {
		return extra2ProfilePic;
	}

	public void setExtra2ProfilePic(String extra2ProfilePic) {
		this.extra2ProfilePic = extra2ProfilePic;
	}

	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}


	public void addActivatorCount() {
		 this.activatorCount = this.activatorCount + 1;
	}


	public void minusActivatorCount() {
		 this.activatorCount = this.activatorCount - 1;
	}

	
	public Integer getFeelId() {
		return feelId;
	}

	public void setFeelId(Integer feelId) {
		this.feelId = feelId;
	}

	public String getSnippetText() {
		return snippetText;
	}

	public void setSnippetText(String snippetText) {
		
		if( snippetText == null )
			return;
		
		
		if( snippetText.length() < DEFAULT_SNIPPET_SIZE + 1 ){
			this.snippetText = snippetText;
			return;
		}
		
		this.snippetText = snippetText.substring(0, DEFAULT_SNIPPET_SIZE) + "...";
	}


	public Integer getTargetContentType() {
		return targetContentType;
	}

	private void setTargetContentType(Integer targetContentType) {
		this.targetContentType = targetContentType;
	}

	public Integer getParentContentType() {
		return parentContentType;
	}

	public void setParentContentType(Integer parentContentType) {
		this.parentContentType = parentContentType;
	}

	
	public void regenerateActivators(List<ActivatorRelation> currentActivators) {
		User activator = new User();
		for( ActivatorRelation relation : currentActivators ){
			activator.setId( relation.getUser().getId() );
			activator.setName( relation.getName() );
			this.generateActivator( activator );
		}
		
	}

	
	public void generateActivator(User activator) {
		
		Long newActivatorId = activator.getId();
		
		if( newActivatorId.equals( this.activatorId) ){
			return;
		}
		
		/*
		 * 새로 들어온 것이 2번째것과 같을 경우엔 첫번째와 두번째를 바꿔준다.
		 */
		if( newActivatorId.equals( this.extra1Id ) ){
			swapActivatorAndExtra1();	
			return ;
		}
		
		/*
		 * 새로 들어온 것이 3번째것과 같을 경우엔 첫번째와 세번째를 바꿔준다.
		 */
		if( newActivatorId.equals( this.extra2Id ) ){
			swapActivatorAndExtra2();
			return; 
		}
		
		/*
		 * 첨보는게 들어왔을 경우
		 */
		this.extra2Id = this.extra1Id;
		this.extra2Name = this.extra1Name;
		this.extra2ProfilePic = this.extra1ProfilePic;
		
		
		this.extra1Id = this.activatorId;
		this.extra1Name = this.activatorName;
		this.extra1ProfilePic = this.activatorProfilePic;
		
		this.activatorId = activator.getId();
		this.activatorName = activator.getName();
		this.activatorProfilePic = activator.getProfilePic();
	}

	public void swapActivatorAndExtra1(){
		Long tmpId = this.extra1Id;
		String tmpName = this.extra1Name;
		String tmpProfilePic = this.extra1ProfilePic;
		
		this.extra1Id = this.activatorId;
		this.extra1Name = this.activatorName;
		this.extra1ProfilePic = this.activatorProfilePic;
		
		this.extra1Id = tmpId;
		this.extra1Name = tmpName;
		this.extra1ProfilePic = tmpProfilePic;
	}
	
	public void swapActivatorAndExtra2(){
		Long tmpId = this.extra2Id;
		String tmpName = this.extra2Name;
		String tmpProfilePic = this.extra2ProfilePic;
		
		this.extra2Id = this.activatorId;
		this.extra2Name = this.activatorName;
		this.extra2ProfilePic = this.activatorProfilePic;
		
		this.extra2Id = tmpId;
		this.extra2Name = tmpName;
		this.extra2ProfilePic = tmpProfilePic;
	}
	
	public int getEVALUATE(){
		return ActivityType.EVALUATE;
	}
	public int getCOMMENT(){
		return ActivityType.COMMENT;
	}
	public int getFEED(){
		return ActivityType.FEED;
	}
	public int getCOMMENT_EVALUATE(){
		return ActivityType.COMMENT_EVALUATE;
	}
//	public int getANSWER_EVALUATE(){
//		return ActivityType.ANSWER_EVALUATE;
//	}
	public int getCOMMENT_COMMENT(){
		return ActivityType.COMMENT_COMMENT;
	}
	public int getANSWER(){
		return ActivityType.ANSWER;
	}

	public String getLink(){
		
		String link = null;
		
		if( this.getActivityType() == ActivityType.REQUEST_SIGNUP ){
			link = "/a/site/inactive_users";
			return link;
		}
		
		if( getParentContentId() != null ) {
			switch( getParentContentType() ){
	
			case Content.TYPE_STORY:
				link = "/story/" + getParentContentId();
				break;
				
			case Content.TYPE_REPLY:
				link = "/story/" + getParentContentId();
				break;	
			case Content.TYPE_APPROVAL:
				link = "/approval/" + getParentContentId();
				break;	
			
			default:
				//throw new RuntimeException("잘못된 알림입니다");
			}
		}
		
		return link;
	}
	
	@JsonIgnore
	private String getWrappedLink(String message){
		
		String link = null;
		
		if( this.getActivityType() == ActivityType.REQUEST_SIGNUP ){
			link = "<a href='a/site/inactive_users'>" + message + "</a>";
			return link;
		}
		
		if( getParentContentId() != null ) {
			switch( getParentContentType() ){
	
			case Content.TYPE_STORY:
				link = "<a href='/story/" + getParentContentId() + "'>" + message + "</a>";
				break;
				
			case Content.TYPE_REPLY:
				link = "<a href='/story/" + getParentContentId() + "'>" + message + "</a>";
				break;	
			default:
				//throw new RuntimeException("잘못된 알림입니다");
			}
		}
		
		return link;
	}

	
	private String getWrappedUserLink( Long userId, String userName ){
		return "<a href='/user/" + userId + "'>" + userName + "</a>";
	}
	
	/*
	 * "/notifications" 에서만 사용되는 메시지. 안에 링크가 있다.
	 */
	@JsonIgnore
	public String getLinkedMessage(){
//		String retMessage = "<a href='/" + getActivatorId() + "'>" + this.getActivatorName() + "</a>님";

		String colorNamePrev = "<span class='color-name'>"; 
		String colorNameNext = "</span>";
		
		String retMessage =  null;
		
		if( this.getActivityType() == ActivityType.REQUEST_SIGNUP ){
			retMessage = colorNamePrev + this.getActivatorName() + "(" + this.getActivatorEmail() + ")" + colorNameNext + "님";
		}else{
			retMessage = getWrappedUserLink(this.getActivatorId(), this.getActivatorName()) + "님";
		}
		
		if( getActivatorCount() > 1 ){
			int extraCount = 1;
			retMessage += ", " + getWrappedUserLink(this.getExtra1Id(), this.getExtra1Name()) + "님";
			if( getActivatorCount() > 2 ){
				extraCount = 2;
				retMessage += ", " + getWrappedUserLink(this.getExtra2Id(), this.getExtra2Name())+ "님";
				retMessage += " 외 " + ( this.getActivatorCount() - extraCount ) + "명";
			}
		}
		
		
		
		String parentName = null;
		if( getTargetContentId() != null ){
			switch( getTargetContentType() ){
			
			case Content.TYPE_STORY:
				parentName = "스토리";
				break;
				
			case Content.TYPE_REPLY:
				parentName = "댓글";
				if( getActivityType() == ActivityType.COMMENT_COMMENT ){
					parentName = "스토리";
				}
				break;	
			
			case Content.TYPE_NONE:
				parentName = "컨텐츠";
				break;
			case Content.TYPE_NOTE:
				parentName = "노트";
				break;	
			case Content.TYPE_APPROVAL:
				parentName = "결재";
				break;
			default:
				//throw new RuntimeException("잘못된 알림입니다");
			}
			
		}
		
		/*
		 * 댓글과 같은 것들은 Owner 가 누구냐에 따라서 "회원님의 상태에 댓글을 등록" 혹은 "임성묵 님의 상태에 댓글을 등록" 과 같이
		 * 보여지기 때문에 myNoti 를 따로 둠
		 */
		boolean myNoti = false;
		String receiverName = null;
		if( getOwnerId() != null ){
		
			if( getOwnerId().equals( getReceiver().getId() )){
				myNoti = true; 
			}
			
			if( myNoti == true ){
				receiverName = "회원";
			}else{
				receiverName = getWrappedUserLink(getOwnerId(), getOwnerName());
			}
		}

		
		switch( this.getActivityType() ){
		case ActivityType.FRIEND_REQUEST :
			retMessage += "이 " + colorNamePrev + "친구요청" + colorNameNext + "을 했습니다";
			break;

		case ActivityType.EVALUATE:

			retMessage += "이 회원님의 " + getWrappedLink(parentName) + "에 " + getWrappedLink("공감") + "을 했습니다. " + ( getTargetContentSnippet() == null ? "" : getWrappedLink(( "\"" + getTargetContentSnippet() + "\"" )));
			break;

		case ActivityType.COMMENT:
			retMessage += (myNoti == true ? "이  " : "도 ") +  receiverName + "님의 " + getWrappedLink(parentName) + "에 " + getWrappedLink("댓글") + "을 등록했습니다. " + (this.getActivatorCount() < 2 && getSnippetText() != null ? getWrappedLink("\"" + getSnippetText() + "\"") : "");
			break;

		case ActivityType.COMMENT_EVALUATE:
			retMessage += "이  회원님의 " + getWrappedLink(parentName) + "을 " +  getWrappedLink("좋아요") + " 했습니다. " + ( getTargetContentSnippet() == null ? "" : getWrappedLink( "\"" + getTargetContentSnippet() + "\"") );
			break;	
		case ActivityType.MENTION_STORY:
			retMessage += "이 게시물에서 회원님을 " + getWrappedLink("언급") + "했습니다: \"" + getWrappedLink(getTargetContentSnippet()) + "\"";
			break;
		case ActivityType.MENTION_REPLY:
			retMessage += "이 댓글에서 회원님을 언급했습니다.: \"" + getWrappedLink(getSnippetText()) + "\"";
			break;
		case ActivityType.REQUEST_SIGNUP:
			retMessage += "이 가입요청을 했습니다.";
			break;
		default:
			retMessage += "이 뭔가를 했습니다. ( 케이스 : " + getActivityType() + ")";
			break;
		}
		return retMessage; 
	}
	
	
	public String getMessage(){
		String colorNamePrev = "<span class='color-name'>"; 
		String colorNameNext = "</span>";
		
		
		String retMessage =  null;
		
		if( this.getActivityType() == ActivityType.REQUEST_SIGNUP ){
			retMessage = colorNamePrev + this.getActivatorName() + "(" + this.getActivatorEmail() + ")" + colorNameNext + "님";
		}else{
			retMessage = colorNamePrev + this.getActivatorName() + colorNameNext + "님";
		}
		
		if( getActivatorCount() > 1 ){
			int extraCount = 1;
			retMessage += ", " + colorNamePrev + this.getExtra1Name() + colorNameNext + "님";
			if( getActivatorCount() > 2 ){
				extraCount = 2;
				retMessage += ", " + colorNamePrev + this.getExtra2Name() + colorNameNext + "님";
				retMessage += " 외 " + ( this.getActivatorCount() - extraCount ) + "명";
			}
		}
		
		String parentName = null;
		if( getTargetContentId() != null ){
			switch( getTargetContentType() ){
			
			case Content.TYPE_STORY:
				parentName = "스토리";
				break;
				
			case Content.TYPE_REPLY:
				parentName = "댓글";
				if( getActivityType() == ActivityType.COMMENT_COMMENT ){
					parentName = "스토리";
				}
				break;	
			
			case Content.TYPE_NONE:
				parentName = "컨텐츠";
				break;
			case Content.TYPE_NOTE:
				parentName = "노트";
				break;	
			case Content.TYPE_APPROVAL:
				parentName = "결재";
				break;
			default:
				//throw new RuntimeException("잘못된 알림입니다");
			}
			
		}
		
		/*
		 * 댓글과 같은 것들은 Owner 가 누구냐에 따라서 "회원님의 상태에 댓글을 등록" 혹은 "임성묵 님의 상태에 댓글을 등록" 과 같이
		 * 보여지기 때문에 myNoti 를 따로 둠
		 */
		boolean myNoti = false;
		String receiverName = null;
		if( getOwnerId() != null ){
		
			if( getOwnerId().equals( getReceiver().getId() )){
				myNoti = true; 
			}
			
			
			if( myNoti == true ){
				receiverName = "회원";
			}else{
				receiverName = colorNamePrev + getOwnerName() + colorNameNext;
			}
		}
		
		
		switch( this.getActivityType() ){
			case ActivityType.FRIEND_REQUEST :
				retMessage += "이 " + colorNamePrev + "친구요청" + colorNameNext + "을 했습니다";
			break;
			
			case ActivityType.EVALUATE:
				
				retMessage += "이 회원님의 " + parentName + "에 " + colorNamePrev + "공감" + colorNameNext + "을 했습니다. " + ( getTargetContentSnippet() == null ? "" : ( "\"" + getTargetContentSnippet() + "\"" ));
			break;
			
			case ActivityType.COMMENT:
				retMessage += (myNoti == true ? "이  " : "도 ") +  receiverName + "님의 " + parentName + "에 " + colorNamePrev + "댓글" + colorNameNext + "을 등록했습니다. " + (this.getActivatorCount() < 2 && getSnippetText() != null ? "\"" + getSnippetText() + "\"" : "");
			break;
			
			case ActivityType.FEED:
				retMessage += "이 회원님을  " +  colorNamePrev + "구독하기" +  colorNameNext + " 했습니다.";
			break;
			
			case ActivityType.COMMENT_EVALUATE:
				retMessage += "이  회원님의 " + parentName + "을 " +  colorNamePrev + "좋아요" +  colorNameNext + " 했습니다. " + ( getTargetContentSnippet() == null ? "" : ( "\"" + getTargetContentSnippet() + "\"") );
			break;	
			case ActivityType.MENTION_STORY:
				retMessage += "이 게시물에서 회원님을 언급했습니다: \"" + getTargetContentSnippet() + "\"";
			break;
			case ActivityType.MENTION_REPLY:
				retMessage += "이 댓글에서 회원님을 언급했습니다.: \"" + getSnippetText() + "\"";
			break;
			case ActivityType.REQUEST_APPROVAL:
				retMessage += "이 결재를 상신했습니다<br />제목 : <strong>\"" + getSnippetText() + "\"</strong>";
			break;	
			case ActivityType.APPROVAL_APPROBATOR_COMPLETE:
				retMessage += "이 상신한 결재가 완료되었습니다.<br />제목 : <strong>\"" + getSnippetText() + "\"</strong>";
			break;
			case ActivityType.APPROVAL_COOPERATION_COMPLETE:
				retMessage += "이 상신한 결재가 협조완료되었습니다.<br />제목 : <strong>\"" + getSnippetText() + "\"</strong>";
			break;
			case ActivityType.APPROVAL_REJECT:
				retMessage += "이 결재를 반려했습니다. <br />제목 : <strong>\"" + getSnippetText() + "\"</strong>";
			break;
			case ActivityType.REQUEST_SIGNUP:
				retMessage += "이 가입요청을 했습니다.";
			break;
			default:
				retMessage += "이 뭔가를 했습니다. ( 케이스 : " + getActivityType() + ")";
			break;
		}

		return retMessage; 
	}

	
	public String getStrippedMessage(){
		String colorNamePrev = ""; 
		String colorNameNext = "";
		
		String retMessage =  null;
		
		if( this.getActivityType() == ActivityType.REQUEST_SIGNUP ){
			retMessage = colorNamePrev + this.getActivatorName() + "(" + this.getActivatorEmail() + ")" + colorNameNext + "님";
		}else{
			retMessage = colorNamePrev + this.getActivatorName() + colorNameNext + "님";
		}
		
		if( getActivatorCount() > 1 ){
			int extraCount = 1;
			retMessage += ", " + colorNamePrev + this.getExtra1Name() + colorNameNext + "님";
			if( getActivatorCount() > 2 ){
				extraCount = 2;
				retMessage += ", " + colorNamePrev + this.getExtra2Name() + colorNameNext + "님";
				retMessage += " 외 " + ( this.getActivatorCount() - extraCount ) + "명";
			}
		}
		
		String parentName = null;
		if( getTargetContentId() != null ){
			switch( getTargetContentType() ){
			
			case Content.TYPE_STORY:
				parentName = "스토리";
				break;
				
			case Content.TYPE_REPLY:
				parentName = "댓글";
				if( getActivityType() == ActivityType.COMMENT_COMMENT ){
					parentName = "스토리";
				}
				break;	
			
			case Content.TYPE_NONE:
				parentName = "컨텐츠";
				break;
			case Content.TYPE_NOTE:
				parentName = "노트";
				break;	
			case Content.TYPE_APPROVAL:
				parentName = "결재";
				break;
			default:
				//throw new RuntimeException("잘못된 알림입니다");
			}
			
		}
		
		/*
		 * 댓글과 같은 것들은 Owner 가 누구냐에 따라서 "회원님의 상태에 댓글을 등록" 혹은 "임성묵 님의 상태에 댓글을 등록" 과 같이
		 * 보여지기 때문에 myNoti 를 따로 둠
		 */
		boolean myNoti = false;
		String receiverName = null;
		if( getOwnerId() != null ){
		
			if( getOwnerId().equals( getReceiver().getId() )){
				myNoti = true; 
			}
			
			
			if( myNoti == true ){
				receiverName = "회원";
			}else{
				receiverName = colorNamePrev + getOwnerName() + colorNameNext;
			}
		}
		
		
		switch( this.getActivityType() ){
			case ActivityType.FRIEND_REQUEST :
				retMessage += "이 " + colorNamePrev + "친구요청" + colorNameNext + "을 했습니다";
			break;
			
			case ActivityType.EVALUATE:
				
				retMessage += "이 회원님의 " + parentName + "에 " + colorNamePrev + "공감" + colorNameNext + "을 했습니다. \"" + getTargetContentSnippet() + "\"";
			break;
			
			case ActivityType.COMMENT:
				retMessage += (myNoti == true ? "이  " : "도 ") +  receiverName + "님의 " + parentName + "에 " + colorNamePrev + "댓글" + colorNameNext + "을 등록했습니다. " + (this.getActivatorCount() < 2 && getSnippetText() != null ? "\"" + getSnippetText() + "\"" : "");
			break;
			
			case ActivityType.FEED:
				retMessage += "이 회원님을  " +  colorNamePrev + "구독하기" +  colorNameNext + " 했습니다.";
			break;
			
			case ActivityType.COMMENT_EVALUATE:
				retMessage += "이  회원님의 " + parentName + "을 " +  colorNamePrev + "좋아요" +  colorNameNext + " 했습니다. \"" + getTargetContentSnippet() + "\"";
			break;	
			case ActivityType.MENTION_STORY:
				retMessage += "이 게시물에서 회원님을 언급했습니다: \"" + getTargetContentSnippet() + "\"";
			break;
			case ActivityType.MENTION_REPLY:
				retMessage += "이 댓글에서 회원님을 언급했습니다.: \"" + getSnippetText() + "\"";
			break;
			case ActivityType.REQUEST_APPROVAL:
				retMessage += "이 결재를 상신했습니다<br />제목 : <strong>\"" + getSnippetText() + "\"</strong>";
			break;	
			case ActivityType.APPROVAL_APPROBATOR_COMPLETE:
				retMessage += "이 상신한 결재가 완료되었습니다.<br />제목 : <strong>\"" + getSnippetText() + "\"</strong>";
			break;
			case ActivityType.APPROVAL_COOPERATION_COMPLETE:
				retMessage += "이 상신한 결재가 협조완료되었습니다.<br />제목 : <strong>\"" + getSnippetText() + "\"</strong>";
			break;
			case ActivityType.APPROVAL_REJECT:
				retMessage += "이 결재를 반려했습니다. <br />제목 : <strong>\"" + getSnippetText() + "\"</strong>";
			break;
			case ActivityType.REQUEST_SIGNUP:
				retMessage += "이 가입요청을 했습니다.";
			break;
			default:
				retMessage += "이 뭔가를 했습니다. ( 케이스 : " + getActivityType() + ")";
			break;
		}

		return retMessage; 
	}
	
	public void setTargetContent(Content content) {
		this.setTargetContentId( content.getId() );
		this.setTargetContentSnippet( content.getStrippedSnippetText() );
		this.setTargetContentType( content.getType() );
		
	}

	public void setActivateContent( Content content ){
		this.activateContentId = content.getId();
	}
	
	public Long getActivateContentId() {
		return activateContentId;
	}

	private void setActivateContentId(Long activateContentId) {
		this.activateContentId = activateContentId;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Long getActivatorId() {
		return activatorId;
	}

	public void setActivatorId(Long activatorId) {
		this.activatorId = activatorId;
	}

	public Long getExtra1Id() {
		return extra1Id;
	}

	public void setExtra1Id(Long extra1Id) {
		this.extra1Id = extra1Id;
	}

	public Long getExtra2Id() {
		return extra2Id;
	}

	public void setExtra2Id(Long extra2Id) {
		this.extra2Id = extra2Id;
	}

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	public String getActivatorEmail() {
		return activatorEmail;
	}

	public void setActivatorEmail(String activatorEmail) {
		this.activatorEmail = activatorEmail;
	}

	public NotifyDTO toNotificationDto() {

		NotifyDTO notificationDto = new NotifyDTO();
		
		notificationDto.setTypeName( MessageType.NOTIFICATION.getTypeName() );
		
		notificationDto.setId( this.id );
		notificationDto.setLink( this.getLink() );
		notificationDto.setName( this.getActivatorName() );
		notificationDto.setStrippedMessage( this.getStrippedMessage() );
		notificationDto.setThumbnail( this.getActivatorProfilePic() );
		notificationDto.setUpdateDate( this.getUpdateDate() );
		return notificationDto;
	}

//	public Long getSiteId() {
//		return siteId;
//	}
//
//	public void setSiteId(Long siteId) {
//		this.siteId = siteId;
//	}

	

//	public List<ReceiverRelation> getReceiverRelations() {
//		return receiverRelations;
//	}
//
//	public void setReceiverRelations(List<ReceiverRelation> receiverRelations) {
//		this.receiverRelations = receiverRelations;
//	}
//	
//	public void addReceiverRelation( ReceiverRelation receiver ){
//		if( this.receiverRelations == null ){
//			this.receiverRelations = new ArrayList<ReceiverRelation>();
//		}
//		
//		this.receiverRelations.add( receiver );
//	}

//	public void setNewActivatorCount(int newActivatorCount) {
//		this.newActivatorCount = newActivatorCount;
//	}

//	public int getNewActivatorCount() {
//		if( newActivatorCount == 0 )
//			return this.getActivatorCount();
//		
//		return newActivatorCount;
//	}
	
	
	
}
