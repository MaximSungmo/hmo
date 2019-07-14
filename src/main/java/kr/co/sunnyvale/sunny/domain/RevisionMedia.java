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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "REVISION_MEDIA")
public class RevisionMedia {
	
	public RevisionMedia(){
		
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "revision_id")
	private Revision revision;
	
	@Column(name = "thumbnail")
	protected String thumbnail;

	@Column(name = "media_id")
	protected Long mediaId;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Revision getRevision() {
		return revision;
	}

	public void setRevision(Revision revision) {
		this.revision = revision;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public Long getMediaId() {
		return mediaId;
	}

	public void setMediaId(Long mediaId) {
		this.mediaId = mediaId;
	}

	
}
