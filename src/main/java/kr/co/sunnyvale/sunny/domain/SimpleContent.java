package kr.co.sunnyvale.sunny.domain;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="CONTENT")
public class SimpleContent {
	@Id
	@Column(name = "id", columnDefinition="bigint(20)" )
	@GeneratedValue( generator="seqId" )
	@GenericGenerator( name="seqId", strategy="kr.co.sunnyvale.sunny.repository.hibernate.ContentIdGenerator",
			parameters = { 
			@Parameter(name="serviceSequenceId", value="contentId")
	} )
	// 상속을 사용한 클래스의 경우엔 AUTO 나 IDENTITY가 먹지 않는다. 
	//@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;

	@Column(name = "type")
	protected Integer type;
	
	@Column(name = "title")
	protected String title;
	
	@Column(name = "raw_text")
	@Type(type="text")
	protected String rawText;
	
	@Column(name = "request_body")
	@Type(type="text")
	protected String requestBody;

	@Column(name = "tagged_text_prev")
	@Type(type="text")
	protected String taggedTextPrev;

	@Column(name = "tagged_text_next")
	@Type(type="text")
	protected String taggedTextNext;

	@Column(name = "return_count", columnDefinition="integer default 0" )
	protected int returnCount;
	
	@Column(name = "media_count", columnDefinition="integer default 0" )
	protected int mediaCount; 
	
	@Column(name = "raw_tags_string")
	protected String rawTagsString;
	
	@Column(name = "create_date")
	private Date createDate;
	
	@Column(name = "update_date")
	protected Date updateDate;
	
	@Column(name = "ip_address")
	protected String ipAddress;
	
	@Column(name = "permission_type", columnDefinition="integer default 0")
	protected int permissionType;
	
	@Transient
	private String strippedRawText;
	
//
	
	@OneToOne( mappedBy = "content", fetch=FetchType.EAGER, cascade={CascadeType.PERSIST,CascadeType.REMOVE} )
	protected ContentDynamic dynamic;

	@OneToMany( mappedBy = "content", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	protected List<Media> mediaes;

	@OneToMany( mappedBy = "content", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	protected List<Reply> replys;

	@OneToMany( mappedBy = "content", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	protected List<FeelAndContentAndUser> feelAndContentAndUsers;

	@OneToMany( mappedBy = "content", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	protected List<FeelAndContent> feelAndContents;
	
	@OneToMany( mappedBy = "content", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	protected List<ContentReadUser> contentReadUsers;
	
	@OneToOne( mappedBy = "content", fetch=FetchType.EAGER, cascade={CascadeType.PERSIST,CascadeType.REMOVE} )
	protected ContentReadCount contentReadCount;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_user_id")
	private User postUser;

	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "site_id")
	private Site site;
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "SMALL_GROUP_CONTENT_ACCESS", joinColumns = { 
			@JoinColumn(name = "content_id", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "small_group_id", nullable = false, updatable = false) })
	@BatchSize(size=10)
	private List<SmallGroup> smallGroups;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "CONTENT_AND_TAG", joinColumns = { 
			@JoinColumn(name = "content_id", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "tag_id", nullable = false, updatable = false) })
	private Set<Tag> tags;
	
	@OneToMany( mappedBy = "content", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	protected List<HistoryContent> historyContents;
	
	@OneToMany( mappedBy = "content", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	protected List<ContentReceiver> contentReceivers;
	
	@OneToMany( mappedBy = "content", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	protected List<SmallGroupContentAccess> smallGroupContentAccesses;
	
	@JsonIgnore
	@OneToMany(mappedBy = "content", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	protected List<BookMark> bookMarks;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "small_group_id")
	protected SmallGroup smallGroup;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	private Content parent;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="parent")
	private List<Content> children;
	
	@Column(name = "thread", columnDefinition="bigint(20) default 0")
	private Long thread;
	
	@Column(name = "depth", columnDefinition="integer default 0")
	private Integer depth;
	
	@Column(name = "thread_seq", columnDefinition="integer default 0")
	private Integer threadSeq;
	
	@Column(name = "absolute_path")
	private String absolutePath;
	
	@Column(name = "small_group_absolute_path")
	private String smallGroupAbsolutePath;
	
	@Column(name = "ordering", columnDefinition="integer default 0")
	private Integer ordering; 
	
	// 삭제여부 deleteFlag 가 true라면 삭제처리된 자료.
	@Column(name = "delete_flag", columnDefinition="boolean default false")
	private boolean deleteFlag;

	@Transient
	private int feeledId;

	@Transient
	protected String searchedText;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRawText() {
		return rawText;
	}

	public void setRawText(String rawText) {
		this.rawText = rawText;
	}

	public String getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	public String getTaggedTextPrev() {
		return taggedTextPrev;
	}

	public void setTaggedTextPrev(String taggedTextPrev) {
		this.taggedTextPrev = taggedTextPrev;
	}

	public String getTaggedTextNext() {
		return taggedTextNext;
	}

	public void setTaggedTextNext(String taggedTextNext) {
		this.taggedTextNext = taggedTextNext;
	}

	public int getReturnCount() {
		return returnCount;
	}

	public void setReturnCount(int returnCount) {
		this.returnCount = returnCount;
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

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public int getPermissionType() {
		return permissionType;
	}

	public void setPermissionType(int permissionType) {
		this.permissionType = permissionType;
	}

	public String getStrippedRawText() {
		return strippedRawText;
	}

	public void setStrippedRawText(String strippedRawText) {
		this.strippedRawText = strippedRawText;
	}

	public ContentDynamic getDynamic() {
		return dynamic;
	}

	public void setDynamic(ContentDynamic dynamic) {
		this.dynamic = dynamic;
	}

	public List<Media> getMediaes() {
		return mediaes;
	}

	public void setMediaes(List<Media> mediaes) {
		this.mediaes = mediaes;
	}

	public List<Reply> getReplys() {
		return replys;
	}

	public void setReplys(List<Reply> replys) {
		this.replys = replys;
	}

	public List<FeelAndContentAndUser> getFeelAndContentAndUsers() {
		return feelAndContentAndUsers;
	}

	public void setFeelAndContentAndUsers(
			List<FeelAndContentAndUser> feelAndContentAndUsers) {
		this.feelAndContentAndUsers = feelAndContentAndUsers;
	}

	public List<FeelAndContent> getFeelAndContents() {
		return feelAndContents;
	}

	public void setFeelAndContents(List<FeelAndContent> feelAndContents) {
		this.feelAndContents = feelAndContents;
	}

	public List<ContentReadUser> getContentReadUsers() {
		return contentReadUsers;
	}

	public void setContentReadUsers(List<ContentReadUser> contentReadUsers) {
		this.contentReadUsers = contentReadUsers;
	}

	public ContentReadCount getContentReadCount() {
		return contentReadCount;
	}

	public void setContentReadCount(ContentReadCount contentReadCount) {
		this.contentReadCount = contentReadCount;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getPostUser() {
		return postUser;
	}

	public void setPostUser(User postUser) {
		this.postUser = postUser;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public List<SmallGroup> getSmallGroups() {
		return smallGroups;
	}

	public void setSmallGroups(List<SmallGroup> smallGroups) {
		this.smallGroups = smallGroups;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	public List<HistoryContent> getHistoryContents() {
		return historyContents;
	}

	public void setHistoryContents(List<HistoryContent> historyContents) {
		this.historyContents = historyContents;
	}

	public List<ContentReceiver> getContentReceivers() {
		return contentReceivers;
	}

	public void setContentReceivers(List<ContentReceiver> contentReceivers) {
		this.contentReceivers = contentReceivers;
	}

	public List<SmallGroupContentAccess> getSmallGroupContentAccesses() {
		return smallGroupContentAccesses;
	}

	public void setSmallGroupContentAccesses(
			List<SmallGroupContentAccess> smallGroupContentAccesses) {
		this.smallGroupContentAccesses = smallGroupContentAccesses;
	}

	public List<BookMark> getBookMarks() {
		return bookMarks;
	}

	public void setBookMarks(List<BookMark> bookMarks) {
		this.bookMarks = bookMarks;
	}

	public SmallGroup getSmallGroup() {
		return smallGroup;
	}

	public void setSmallGroup(SmallGroup smallGroup) {
		this.smallGroup = smallGroup;
	}

	public Content getParent() {
		return parent;
	}

	public void setParent(Content parent) {
		this.parent = parent;
	}

	public List<Content> getChildren() {
		return children;
	}

	public void setChildren(List<Content> children) {
		this.children = children;
	}

	public Long getThread() {
		return thread;
	}

	public void setThread(Long thread) {
		this.thread = thread;
	}

	public Integer getDepth() {
		return depth;
	}

	public void setDepth(Integer depth) {
		this.depth = depth;
	}

	public Integer getThreadSeq() {
		return threadSeq;
	}

	public void setThreadSeq(Integer threadSeq) {
		this.threadSeq = threadSeq;
	}

	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}

	public String getSmallGroupAbsolutePath() {
		return smallGroupAbsolutePath;
	}

	public void setSmallGroupAbsolutePath(String smallGroupAbsolutePath) {
		this.smallGroupAbsolutePath = smallGroupAbsolutePath;
	}

	public Integer getOrdering() {
		return ordering;
	}

	public void setOrdering(Integer ordering) {
		this.ordering = ordering;
	}

	public boolean isDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(boolean deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public int getFeeledId() {
		return feeledId;
	}

	public void setFeeledId(int feeledId) {
		this.feeledId = feeledId;
	}

	public String getSearchedText() {
		return searchedText;
	}

	public void setSearchedText(String searchedText) {
		this.searchedText = searchedText;
	}
	
	
}
