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
import javax.persistence.Table;

/**
 * @author Administrator
 *
 */
@Entity
@Table(name = "DRAFT_SMALL_GROUP_APPROVAL")
public class DraftSmallGroupApproval {
	
	public DraftSmallGroupApproval(){
		this.setCreateDate(new Date());
	}
	
	@Id
	@Column(name = "id", columnDefinition="bigint(20)" )
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "small_group_id")
	private Long smallGroupId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "draft_id")
	private Draft draft;
	
	@Column(name = "create_date")
	private Date createDate;

	@Column(name = "name")
	private String name;

	@Column(name = "type", columnDefinition="integer default 0")
	private int type;
	
	@Column(name = "ordering", columnDefinition="integer default 0")
	private int ordering;

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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getOrdering() {
		return ordering;
	}

	public void setOrdering(int ordering) {
		this.ordering = ordering;
	}


	public Draft getDraft() {
		return draft;
	}

	public void setDraft(Draft draft) {
		this.draft = draft;
	}

	public Long getSmallGroupId() {
		return smallGroupId;
	}

	public void setSmallGroupId(Long smallGroupId) {
		this.smallGroupId = smallGroupId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
