package kr.co.sunnyvale.sunny.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "TAG")
public class Tag {
	
	private Tag(){
		this.referenceCount = 0;
		this.setCreateDate(new Date());
	}
	
	public Tag(SmallGroup smallGroup, int contentType){
		this();
		this.smallGroup = smallGroup;
		this.contentType = contentType;
		this.referenceCount = 0;
		this.setCreateDate(new Date());
	}

	public Tag(Long tagId) {
		this();
		this.setId(tagId);
	}

	public Tag(SmallGroup smallGroup, int contentType, String title) {
		this(smallGroup, contentType);
		this.setTitle( title );
	}

//	public Tag( Long tagId, String title ){
//		this();
//		this.setId( tagId );
//		this.setTitle( title );
//	}

	@Id
	@Column(name = "id", columnDefinition="bigint(20)" )
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "reference_count", columnDefinition="integer default 0" )
	private int referenceCount;

	@Column(name = "content_type", columnDefinition="integer default 0" )
	private int contentType;
	
	@Column(name = "is_admin_selected", columnDefinition="boolean default false" )
	private boolean adminSelected;
	
	@Column(name = "admin_ordering", columnDefinition="integer default 0" )
	private int adminOrdering;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "small_group_id")
	@JsonIgnore
	private SmallGroup smallGroup;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "site_id")
	@JsonIgnore
	private Site site;
	
	@Column
	private Date createDate; 

	
	//	
//	@ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
//	private List<ContentLine> contentLines;

	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "CONTENT_AND_TAG", joinColumns = { 
			@JoinColumn(name = "tag_id", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "content_id", nullable = false, updatable = false) })
	private List<Content> contents;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getReferenceCount() {
		return referenceCount;
	}

	public void setReferenceCount(Integer referenceCount) {
		this.referenceCount = referenceCount;
	}
//	@JsonIgnore
//	public List<ContentLine> getContentLines() {
//		return contentLines;
//	}
//	@JsonIgnore
//	public void setContentLines(List<ContentLine> contentLines) {
//		this.contentLines = contentLines;
//	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}


	public SmallGroup getSmallGroup() {
		return smallGroup;
	}

	public void setSmallGroup(SmallGroup smallGroup) {
		this.smallGroup = smallGroup;
	}

	
	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}
	public void tagged() {
		this.referenceCount += 1;
	}

	public void deTagged() {
		this.referenceCount -= 1;
	}
	@Override
	public String toString() {
		return "Tag [id=" + id + ", title=" + title + ", referenceCount="
				+ referenceCount + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tag other = (Tag) obj;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	public boolean isAdminSelected() {
		return adminSelected;
	}

	public void setAdminSelected(boolean adminSelected) {
		this.adminSelected = adminSelected;
	}

	public int getAdminOrdering() {
		return adminOrdering;
	}

	public void setAdminOrdering(int adminOrdering) {
		this.adminOrdering = adminOrdering;
	}
	

}
