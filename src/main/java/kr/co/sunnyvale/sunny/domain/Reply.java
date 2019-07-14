package kr.co.sunnyvale.sunny.domain;

import java.text.DecimalFormat;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "REPLY")
@PrimaryKeyJoinColumn(name="id")
public class Reply  extends Content {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4288129221096550076L;

	
//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "USER_id")
//	private User user;
	
//	public Comment(){
//		super(ContentType.COMMENT);
//	}
	private Reply(){
		super();
	}
	
	public Reply( Site site ){
		super( site, Content.TYPE_REPLY );
	}
	
	public Reply( Long id ){
		this.setId(id);
	}

	@ManyToOne( fetch = FetchType.LAZY )
	private Content content;

	@Column(name = "parent_type")
	private Integer parentType;

	
	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "media_id")
	private Media media ;
	
	
	@Column(name = "media_instant_id", columnDefinition="bigint(20)" )
	protected Long mediaId;
	
	@Column(name = "media_type", columnDefinition="integer default 0")
	protected int mediaType;
	
	@Column(name = "file_name")
	private String fileName;
	
	@Column(name = "ext_name", length = 8)
	private String extName;
	
	@Column(name = "width", columnDefinition="integer default 0")
	private int width;
	
	@Column(name = "height", columnDefinition="integer default 0" )
	private int height;
	
	@Column(name = "size", columnDefinition="integer default 0" )
	private int size;
	
	@Column(name = "path_before_size")
	private String pathBeforeSize;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public Integer getParentType() {
		return parentType;
	}

	public void setParentType(Integer parentType) {
		this.parentType = parentType;
	}

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	public Media getMedia() {
		return media;
	}

	public void setMedia(Media media) {
		this.media = media;
	}

	public int getMediaType() {
		return mediaType;
	}

	public void setMediaType(int mediaType) {
		this.mediaType = mediaType;
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

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getPathBeforeSize() {
		return pathBeforeSize;
	}

	public void setPathBeforeSize(String pathBeforeSize) {
		this.pathBeforeSize = pathBeforeSize;
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
	
	public String getHugePath() {
		return this.getPathBeforeSize() + "_h.jpg";
	}

	public String getOriginalPath() {
		return this.getPathBeforeSize()
				+ "_o." + this.extName;
	}

	public String getLargePath() {
		return this.getPathBeforeSize()
				+ "_l.jpg";

	}

	public String getMediumPath() {

		return this.getPathBeforeSize()
				+ "_m.jpg";
	}
	
	public String getSmallPath() {

		return this.getPathBeforeSize()
				+ "_s.jpg";
	}

	public Long getMediaId() {
		return mediaId;
	}

	public void setMediaId(Long mediaId) {
		this.mediaId = mediaId;
	}
	
}
