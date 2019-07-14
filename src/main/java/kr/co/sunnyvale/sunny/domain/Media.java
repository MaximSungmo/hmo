package kr.co.sunnyvale.sunny.domain;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import kr.co.sunnyvale.sunnycdn.util.StringUtil;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "MEDIA")
public class Media extends Content implements Serializable{

	private static final long serialVersionUID = -2351146225736384706L;
	
	public static final int TYPE_OTHER_FILE = 1;
	public static final int TYPE_IMAGE = 2;
	public static final int TYPE_YOUTUBE = 3;
	public static final int TYPE_WORD = 4;
	public static final int TYPE_POWERPOINT = 5;
	public static final int TYPE_EXCEL = 6;
	public static final int TYPE_HWP = 7;
	public static final int TYPE_PDF = 8;

	public Media(){
		super();
	}
	
	public Media( Site site ){
		super(site, Content.TYPE_MEDIA);
		Date currentDate = new Date();
		Long currentTimestamp = currentDate.getTime();
		this.setPrefix(currentTimestamp.toString());
		this.setUsed(false);
	}
	
	public Media(Long contentId) {
		super(contentId);
		this.setId(contentId);
	}

	@JsonIgnore	
	@ManyToOne( fetch = FetchType.LAZY )
	@JoinColumn(name = "content_id")
	private Content content;
	
	@JsonIgnore
	@ManyToOne( fetch = FetchType.LAZY )
	@JoinColumn(name = "revision_id")
	private Revision revision;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "site_id")
	private Site site;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "history_story_id")
	private HistoryContent historyContent;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "small_group_id")
	private SmallGroup smallGroup;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne( fetch = FetchType.LAZY )
	@JoinColumn(name = "draft_id")
	private Draft draft;
	
	@Column(name = "parent_type")
	private Integer parentType;
	
	@Column(name = "media_type", columnDefinition="integer default 0")
	protected int mediaType;

	
	@NotEmpty
	@Column(name = "file_name")
	private String fileName;
	
	
	/*
	 * JPG, JPEG, PJPG, GIF, PNG 등
	 */
	@Column(name = "ext_name", length = 8)
	private String extName;

	/*
	 * 이미지 서버 위치
	 * '/s0, /s1, /s2 등등
	 */
	@NotEmpty
	@Column(name = "position", length = 8)	
	private String position;						
	
	
	@Column(name = "relative_path")
	private String relativePath;
	/*
	 * 파일 이름의 앞부분. 여기선 timestamp 로 하고 있다.
	 */
	@NotEmpty
	@Column(name = "prefix")	
	private String prefix;
	
	@Column(name = "width", columnDefinition="integer default 0")
	private int width;
	
	@Column(name = "height", columnDefinition="integer default 0" )
	private int height;
	
	@Column(name = "size", columnDefinition="integer default 0" )
	private int size;

	@Column(name = "used", columnDefinition="boolean default 0")
	private boolean used;

	@Column(name = "content_type")
	private String contentType;

	
	@Transient
	private String jsonString;

	
	// YouTube, 이미지, 파일, word 파일 공통 컬럼 
	
	// word 파일과 관련된 컬럼 
	
	// YouTube 관련된 컬럼.
	
	// 이미지와 관련된 컬럼
	
	// 파일과 관련된 컬럼 
	
	public int getMediaType() {
		return mediaType;
	}
	public void setMediaType(int mediaType) {
		this.mediaType = mediaType;
	}

	public Content getContent() {
		return content;
	}
	public void setContent(Content content) {
		this.content = content;
	}
	public SmallGroup getSmallGroup() {
		return smallGroup;
	}
	public void setSmallGroup(SmallGroup smallGroup) {
		this.smallGroup = smallGroup;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Integer getParentType() {
		return parentType;
	}
	public void setParentType(Integer parentType) {
		this.parentType = parentType;
	}
	public Site getSite() {
		return site;
	}
	public void setSite(Site site) {
		this.site = site;
	}
	public HistoryContent getHistoryContent() {
		return historyContent;
	}
	public void setHistoryContent(HistoryContent historyContent) {
		this.historyContent = historyContent;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getExtName() {
		return extName;
	}
	public void setExtName(String extName) {
		this.extName = extName;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public boolean isUsed() {
		return used;
	}
	public void setUsed(boolean used) {
		this.used = used;
	}
	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}
//	public Media getMedia() {
//		return media;
//	}
//	public void setMedia(Media media) {
//		this.media = media;
//	}
	public String getHugePath() {
		return this.getRelativePath() + this.getConvertedFront()
				+ "_h.jpg";
	}

	public String getOriginalPath() {
		return this.getRelativePath() + this.getConvertedFront()
				+ "_o." + this.extName;
	}

	public String getLargePath() {
		return this.getRelativePath() + this.getConvertedFront()
				+ "_l.jpg";

	}

	public String getMediumPath() {

		return this.getRelativePath() + this.getConvertedFront()
				+ "_m.jpg";
	}
	
	public String getSmallPath() {

		return this.getRelativePath() + this.getConvertedFront()
				+ "_s.jpg";
	}
	
	public String getRelativePath() {
		
		if( relativePath == null ) {
			String parsedName = "/" + this.getPosition();
			parsedName += "/" + StringUtil.getFirstTwoFromLast( this.getPrefix() );
			parsedName += "/" + StringUtil.getSecondTwoFromLast( this.getPrefix() );
			this.setRelativePath( parsedName );
		}
		
		return relativePath;
	}
	
	public String getConvertedFront() {
		String parsedName = "/" + this.getPrefix() + "_" + this.getId();
		return parsedName;
	}


	public int getSize() {
		return size;
	}

	public String getFormatSize() {
		long lSize;
		String unit;
		int n = (int)Math.log10( (double)size );
		if( n < 9 ){
			lSize = (long)Math.ceil( (double)size / (double)1024. );
			unit = "KB";
		} else if( n < 15 ) {
			lSize = (long)Math.ceil( (double)size / (double)1024. / (double)1024. );
			unit = "MB";
		} else {
			lSize = (long)Math.ceil( (double)size / (double)1024. / (double)1024. / (double)1024. );
			unit = "GB";
		}
		return new DecimalFormat( "#,###" ).format( lSize ) + unit;
	}
	
	public void setSize(int size) {
		this.size = size;
	}


	public String getContentType() {
		return contentType;
	}


	public void setContentType(String contentType) {
		this.contentType = contentType;
	}


	public Revision getRevision() {
		return revision;
	}


	public void setRevision(Revision revision) {
		this.revision = revision;
	}


	public Draft getDraft() {
		return draft;
	}


	public void setDraft(Draft draft) {
		this.draft = draft;
	}

	@JsonIgnore
	public RevisionMedia copyToRevision() {
		
		RevisionMedia revisionMedia = new RevisionMedia();
		
		revisionMedia.setMediaId(this.id);
		revisionMedia.setThumbnail( this.getSmallPath() );
		return revisionMedia;
	}

	public String getRelativeConvertedPath( String type ) {

		String parsedName = "/" + this.getPosition();
		parsedName += "/" + StringUtil.getFirstTwoFromLast(this.getPrefix());
		parsedName += "/" + StringUtil.getSecondTwoFromLast(this.getPrefix());
		parsedName += "/" + this.getPrefix() + "_" + this.getId();
		
		String targetExtName = this.getExtName();
		
		if( type == null || type.isEmpty() ){
			parsedName +=  "." + targetExtName ;
		}else if(type.equals("o")){
			parsedName += "_" + type + "." + targetExtName ;
		}else{
			parsedName += "_" + type + ".jpg";
		}
		
		return parsedName;
	}
	
	public Map<String, Object> getDTOMap() {
		
		Map<String, Object> json = new HashMap<String, Object>();
		
		json.put("id", this.getId());
		json.put("fileName", this.getFileName());
		json.put("extName", this.getExtName());
		json.put("position", this.getPosition());
		json.put("prefix", this.getPrefix());
		json.put("createDate", this.getCreateDate());
		json.put("convertedFront", this.getConvertedFront());
		json.put("relativePath", this.getRelativePath());
		json.put("size", this.getSize());
		json.put("width", this.getWidth());
		json.put("height", this.getHeight());
		json.put("mediaType", this.getMediaType());
		
		return json;
	}	
}
