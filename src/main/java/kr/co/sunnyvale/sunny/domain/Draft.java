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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import kr.co.sunnyvale.sunny.util.StringEscapeUtils;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

@Entity
@Table( name = "DRAFT" )
public class Draft implements Serializable {

//	public static final int TYPE_QUESTION = 10;
//	public static final int TYPE_ANSWER = 20;
//	public static final int TYPE_NOTE = 30;
	
	public static final int TYPE_QUESTION_MODIFYING = 11;
	public static final int TYPE_ANSWER_MODIFYING = 21;
	public static final int TYPE_NOTE_MODIFYING = 31;
	
	public static final int TYPE_QUESTION_WAIT_APPROVAL = 12;
	public static final int TYPE_ANSWER_WAIT_APPROVAL = 22;
	public static final int TYPE_NOTE_WAIT_APPROVAL = 32;


	/**
	 * 
	 */
	private static final long serialVersionUID = 526823155035074870L;
	
	public Draft(){
		this.createDate = new Date();
		this.updateDate = new Date();
	}
	
	public Draft(Site site){
		this();
		this.setSmallGroup( site.getLobbySmallGroup() );
	}
	
	public Draft(Site site, int type) {
		this(site);
		this.type = type;
	}

	
	public Draft(Long id) {
		this.id = id;
	}


	@Id
	@Column(name = "id", columnDefinition="bigint(20)" )
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column( name="type",nullable = false, columnDefinition = "int default 0") 
	private int type;

	@Column(name = "title")
	private String rawTitle;
	
	@Column(name = "raw_text")
	@Type(type="text")
	private String rawText;
	
	@Column(name = "media_count", columnDefinition="integer default 0" )
	private int mediaCount; 
	
	@Column(name = "raw_tags_string")
	private String rawTagsString;
	
	@Column(name = "create_date")
	private Date createDate;
	
	@Column(name = "update_date")
	private Date updateDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "push_content_id")
	private Content pushContent;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "target_content_id")
	private Content targetContent;
	
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "small_group_id")
	private SmallGroup smallGroup;

	
	@OneToMany( mappedBy = "draft", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	protected List<Media> mediaes;
	
	@Column(name = "approbator_count", columnDefinition="integer default 0 ")
	private int approbatorCount;
	
	@Column(name = "cooperation_count", columnDefinition="integer default 0 ")
	private int cooperationCount;
	
	@Column(name = "receiver_count", columnDefinition="integer default 0 ")
	private int receiverCount;
	
	@Column(name = "circulation_count", columnDefinition="integer default 0 ")
	private int circulationCount;
	
	
	@OneToMany( mappedBy = "draft", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	protected List<DraftSmallGroupApproval> draftSmallGroupApprovals;

	@Column(name = "check_ordering", columnDefinition="boolean default 0")
	private boolean checkOrdering = false;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getRawTitle() {
		return rawTitle;
	}

	public void setRawTitle(String rawTitle) {
		this.rawTitle = rawTitle;
	}

	public String getRawText() {
		return rawText;
	}

	public void setRawText(String rawText) {
		this.rawText = rawText;
	}

	public int getMediaCount() {
		return mediaCount;
	}

	public void setMediaCount(int mediaCount) {
		this.mediaCount = mediaCount;
	}

	public String getRawTagsString() {
		return rawTagsString;
	}

	public void setRawTagsString(String rawTagsString) {
		this.rawTagsString = rawTagsString;
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

	public SmallGroup getSmallGroup() {
		return smallGroup;
	}

	public void setSmallGroup(SmallGroup smallGroup) {
		this.smallGroup = smallGroup;
	}

	public Content getPushContent() {
		return pushContent;
	}

	public void setPushContent(Content pushContent) {
		this.pushContent = pushContent;
	}

	public Content getTargetContent() {
		return targetContent;
	}

	public void setTargetContent(Content targetContent) {
		this.targetContent = targetContent;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Media> getMediaes() {
		return mediaes;
	}

	public void setMediaes(List<Media> mediaes) {
		this.mediaes = mediaes;
	}

	public String getSnippetText(){
		return StringEscapeUtils.escapeHtml(getRawText());
	}


	public List<DraftSmallGroupApproval> getDraftSmallGroupApprovals() {
		return draftSmallGroupApprovals;
	}

	public void setDraftSmallGroupApprovals(
			List<DraftSmallGroupApproval> draftSmallGroupApprovals) {
		this.draftSmallGroupApprovals = draftSmallGroupApprovals;
	}

	public boolean isCheckOrdering() {
		return checkOrdering;
	}

	public void setCheckOrdering(boolean checkOrdering) {
		this.checkOrdering = checkOrdering;
	}

	@Override
	public String toString() {
		return "Draft [id=" + id + ", type=" + type + ", rawTitle=" + rawTitle
				+ ", rawText=" + rawText + ", mediaCount=" + mediaCount
				+ ", rawTagsString=" + rawTagsString + ", createDate="
				+ createDate + ", updateDate=" + updateDate + ", user=" + user
				+ ", pushContent=" + pushContent + ", targetContent="
				+ targetContent + ", smallGroup=" + smallGroup
				+ ", checkOrdering=" + checkOrdering + "]";
	}

	public int getApprobatorCount() {
		return approbatorCount;
	}

	public void setApprobatorCount(int approbatorCount) {
		this.approbatorCount = approbatorCount;
	}

	public int getCooperationCount() {
		return cooperationCount;
	}

	public void setCooperationCount(int cooperationCount) {
		this.cooperationCount = cooperationCount;
	}

	public int getReceiverCount() {
		return receiverCount;
	}

	public void setReceiverCount(int receiverCount) {
		this.receiverCount = receiverCount;
	}

	public int getCirculationCount() {
		return circulationCount;
	}

	public void setCirculationCount(int circulationCount) {
		this.circulationCount = circulationCount;
	}
	
}
