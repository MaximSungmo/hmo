package kr.co.sunnyvale.sunny.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import kr.co.sunnyvale.sunny.util.HtmlUtil;
import kr.co.sunnyvale.sunny.util.StringEscapeUtils;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name="CONTENT")
@Inheritance(strategy = InheritanceType.JOINED)
public class Content implements Serializable{
	
	public static final String DELIMITER = "-";
	
	public static final int TYPE_STORY = 1;
	public static final int TYPE_REPLY = 2;
	public static final int TYPE_QUESTION = 3;
	public static final int TYPE_ANSWER = 4;
	public static final int TYPE_NOTE = 5;
	public static final int TYPE_PDS = 6;
	public static final int TYPE_MEDIA = 7;
	public static final int TYPE_APPROVAL = 8;
	public static final int TYPE_NONE = 100;
	public static final int TYPE_USER = 100;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -572249727127628337L;
	public static final int PERMISSION_ME = 0;
	public static final int PERMISSION_LOBBY = 1;
	public static final int PERMISSION_FRIEND= 2;
	
	public Content(){
		this.setCreateDate(new Date());
		this.setUpdateDate(new Date());
		this.setDeleteFlag(false);
	}
	
	public Content(Site site){
		this();
		this.setSite(site);
		if( site != null ){
			this.setSmallGroup(site.getLobbySmallGroup());
		}
	}
	public Content(Site site, int type){
		this(site);
		this.setType(type);
	}
	
	public Content(Long id){
		this();
		setId(id);
	}
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
	
	@OneToMany( mappedBy = "content", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	protected List<Revision> revisions;
	
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
	protected List<Notification> notifications;
	
	@OneToMany( mappedBy = "content", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	protected List<ReceiverRelation> contentReceivers;
	
	@OneToMany( mappedBy = "content", fetch=FetchType.LAZY)
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
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="parent", cascade={CascadeType.PERSIST,CascadeType.REMOVE})
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

	@Column(name = "is_notice", columnDefinition="boolean default false")
	private boolean isNotice;

//	@Transient
//	private String[] searchedTexts;
	
//	@Transient
//	private String strippedSnippetText;
	
	public boolean isNotice() {
		return isNotice;
	}

	public void setNotice(boolean isNotice) {
		this.isNotice = isNotice;
	}
	
//	@JsonIgnore
//	public String getNoticeText(){
//		
//		String tmpStrippedRawText = HtmlUtil.stripMention(this.getRawText());// getStrippedRawText();
//		tmpStrippedRawText = HtmlUtil.stripMention(tmpStrippedRawText);
//		//tmpStrippedRawText = HtmlUtil.stripHashTag(tmpStrippedRawText);
//		tmpStrippedRawText = HtmlUtil.removeHtmlTag(tmpStrippedRawText, null);
//		
//		if( tmpStrippedRawText.length() > 43 ){
//			tmpStrippedRawText = tmpStrippedRawText.substring(0, 43) + "···";
//		}
//		
//		return tmpStrippedRawText;
//	}	

	
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
		if( this.title == null ){			
			if( this.rawText != null && this.rawText.length() > 0){
				int length = this.rawText.length() > 30 ? 30 : this.rawText.length(); 
				return this.getRawText().substring(0, length - 1) ;
			}else{
				return "untitle";
			}
		}
		return title;
	}

	public void setTitle(String title) {
		title = StringEscapeUtils.escapeHtml(title);	
		this.title = title;
	}

	public String getRawText() {
		return rawText;
	}

	public void setRawText(String rawText) {
		this.rawText = rawText;
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
	
	public String getTaggedTextFull(){
		return this.taggedTextPrev + ( this.taggedTextNext != null ? this.taggedTextNext : "") ;
	}

	public int getReturnCount() {
		return returnCount;
	}

	public void setReturnCount(int returnCount) {
		this.returnCount = returnCount;
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


	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}


	@JsonIgnore
	public Set<Tag> getTags() {
		return tags;
	}
	
	void newTags(Set<Tag> newTags){
		detaggedTags(this.tags);
		taggedTags(newTags);
		this.tags = newTags;
	}
	static void detaggedTags(Set<Tag> originalTags) {
        for (Tag tag : originalTags) {
            tag.deTagged();
        }
    }

    static void taggedTags(Set<Tag> newTags) {
        for (Tag tag : newTags) {
            tag.tagged();
        }
    }
    
    
	@JsonIgnore
	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}
	
	public String getSearchedText() {
		return searchedText;
	}

	public void setSearchedText(String searchedText) {
		this.searchedText = searchedText;
	}

	public void fixSearchedText(String[] queries) {
		for( String query : queries ){
			if( this.getTaggedTextPrev() != null ){
				this.setTaggedTextPrev( HtmlUtil.highlighting(this.getTaggedTextPrev(), query, "<strong>", "</strong>") );
			}	
			if( this.getTaggedTextNext() != null ){
				this.setTaggedTextNext( HtmlUtil.highlighting(this.getTaggedTextNext(), query, "<strong>", "</strong>") );
			}
			if( this.getTitle() != null ){
				this.setTitle( HtmlUtil.highlighting( this.getTitle(), query, "<strong>", "</strong>") );
			}
		}
	}
	
	public void fixSearchedText(String keyword, String detailKeyword) {
		// 태그 삭제
		
		String parsedText = this.getRawText();
		parsedText = parsedText.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
		
		if( keyword != null )
			parsedText = HtmlUtil.highlighting(parsedText, keyword, "<strong>", "</strong>");
		
		if( detailKeyword != null )
			parsedText = HtmlUtil.highlighting(parsedText, detailKeyword, "<strong>", "</strong>");
		
		this.setSearchedText( parsedText );
	}
	
	
	public void initLongTextTitle(){
		if( this.title != null && !this.title.equals("") ){
			return;
		}
		int titleLength = 50;
		String parseText = this.rawText;
		/* 파싱 하려는 길이가 너무 길면 대충 잘라준다.
		 * Html 이 섞여있을 경우 원본 텍스트보다 길 수도 있으니 넉넉히 잡아줌
		 */
		if( this.getRawText().length() > 300 ){
			parseText = this.rawText.substring(0, 299);
		}
		
		// 모든 태그를 제거
		String noHtmlString = HtmlUtil.removeHtmlTag(parseText, null);
		
		int length = noHtmlString.length();
		
		this.setTitle( noHtmlString.substring(0, length > titleLength ? titleLength : length ) ) ; 
		
	}


	public String getRawTagsString() {
		return rawTagsString;
	}

	public List<String >getRawTagsStringList() {
		
		List<String> stringList = new ArrayList<String>();
		
		if( this.rawTagsString == null || this.rawTagsString.length() == 0 )
			return null;
		
		StringTokenizer tokenizer = new StringTokenizer(this.rawTagsString, ",");
		while( tokenizer.hasMoreTokens()){
			stringList.add( tokenizer.nextToken() );
		}
		
		return stringList;
	}
	
	public void setRawTagsString(String rawTagsString) {
		this.rawTagsString = rawTagsString;
	}

	@JsonIgnore
	public String getStrippedRawText() {
		if( this.strippedRawText == null || this.strippedRawText.length() < 1 ){
			
			if( this.getRawText() == null || this.getRawText().isEmpty()){
				return strippedRawText;
			}
			
			this.strippedRawText = HtmlUtil.stripMention(this.getRawText());
			this.strippedRawText = HtmlUtil.removeHtmlTag(this.strippedRawText, null);
			this.strippedRawText = this.strippedRawText.replaceAll("  ","");
			System.out.println("파싱버전");
			System.out.println(this.strippedRawText);
		}
		return strippedRawText;
	}

	
	@JsonIgnore
	public String getStrippedSnippetText() {
		
		if( this.title != null )
			return this.title;
		
		if( this.strippedRawText == null || this.strippedRawText.length() < 1 ){
			
			if( this.getRawText() == null || this.getRawText().isEmpty()){
				return strippedRawText;
			}
			
			this.strippedRawText = HtmlUtil.stripMention(this.getRawText());
			
			if( this.strippedRawText.length() > Notification.DEFAULT_SNIPPET_SIZE ){
				this.strippedRawText = this.strippedRawText.substring(0, Notification.DEFAULT_SNIPPET_SIZE + 1)  ;
			}
			this.strippedRawText = HtmlUtil.removeHtmlTag(this.strippedRawText, null);
			this.strippedRawText = HtmlUtil.stripBold(this.strippedRawText);
		}
		return strippedRawText;
	}

	public void setStrippedRawText(String strippedRawText) {
		this.strippedRawText = strippedRawText;
	}

	public int getMediaCount() {
		return mediaCount;
	}

	public void setMediaCount(int mediaCount) {
		this.mediaCount = mediaCount;
	}


	public ContentDynamic getDynamic() {
		return dynamic;
	}

	public void setDynamic(ContentDynamic dynamic) {
		this.dynamic = dynamic;
	}

	public List<HistoryContent> getHistoryContents() {
		return historyContents;
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

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public SmallGroup getSmallGroup() {
		return smallGroup;
	}

	public void setSmallGroup(SmallGroup smallGroup) {
		this.smallGroup = smallGroup;
	}

	public static int getTypeContent() {
		return TYPE_STORY;
	}

	public static int getTypeReply() {
		return TYPE_REPLY;
	}

	public static int getTypeUser() {
		return TYPE_USER;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public boolean isDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(boolean deleteFlag) {
		this.deleteFlag = deleteFlag;
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

	public User getPostUser() {
		return postUser;
	}

	public void setPostUser(User postUser) {
		this.postUser = postUser;
	}

	public List<HistoryContent> getHicontentContents() {
		return historyContents;
	}

	public void setHistoryContents(List<HistoryContent> historyContents) {
		this.historyContents = historyContents;
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



	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}
	
	public void setAbsolutePath(String absolutePath, Long id) {
		this.absolutePath = (absolutePath == null ? "" : absolutePath) + DELIMITER + id + DELIMITER;
	}
	
	public void setWrapAbsolutePath(Long id) {
		if( id == null )
			return;
		this.absolutePath = DELIMITER + id + DELIMITER;
	}

	public String getSmallGroupAbsolutePath() {
		return smallGroupAbsolutePath;
	}

	public void setSmallGroupAbsolutePath(String smallGroupAbsolutePath) {
		this.smallGroupAbsolutePath = smallGroupAbsolutePath;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	// id가 같으면 같은 content로 판단한다.
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Content other = (Content) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public int getFeeledId() {
		return feeledId;
	}

	public void setFeeledId(int feeledId) {
		this.feeledId = feeledId;
	}

	public String getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	public int getPermissionType() {
		return permissionType;
	}

	public void setPermissionType(int permissionType) {
		this.permissionType = permissionType;
	}

	public List<SmallGroup> getSmallGroups() {
		return smallGroups;
	}

	public void setSmallGroups(List<SmallGroup> smallGroups) {
		this.smallGroups = smallGroups;
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

	public Integer getOrdering() {
		return ordering;
	}

	public void setOrdering(Integer ordering) {
		this.ordering = ordering;
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

	public void setRawTagsStringFromSet(Set<String> tagTitles) {
		
		this.rawTagsString = "";
		for( String tagTitle : tagTitles ){
			rawTagsString = rawTagsString + "," + tagTitle;
		}
		rawTagsString = rawTagsString.substring(1);
	}

	
}
