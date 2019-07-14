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

import org.hibernate.annotations.Type;

@Entity
@Table(name = "HISTORY_CONTENT")
public class HistoryContent {
	
	public HistoryContent(){
		this.setCreateDate(new Date());
	}
	
	public HistoryContent(Content content) {
		this();
		this.setTitle( content.getTitle() );
		this.setRawText( content.getRawText() );
		this.setTaggedText( content.getTaggedTextFull() );
		this.setContent(content);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "create_date")
	private Date createDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "content_id")
	private Content content;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "raw_text")
	@Type(type="text")
	protected String rawText;
	
	@Column(name = "tagged_text")
	@Type(type="text")
	protected String taggedText;

//	@OneToMany( mappedBy = "hicontentContent", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
//	protected List<Media> mediaes;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getRawText() {
		return rawText;
	}

	public void setRawText(String rawText) {
		this.rawText = rawText;
	}

	public String getTaggedText() {
		return taggedText;
	}

	public void setTaggedText(String taggedText) {
		this.taggedText = taggedText;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

//	public List<Media> getMediaes() {
//		return mediaes;
//	}
//
//	public void setMediaes(List<Media> mediaes) {
//		this.mediaes = mediaes;
//	}

	
}
