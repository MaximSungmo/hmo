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

@Entity
@Table(name = "SMALL_GROUP_APPROVAL")
public class SmallGroupApproval {
	
	public static final int TYPE_APPROBATOR = 0;
	public static final int TYPE_COOPERATION = 1;
	public static final int TYPE_RECEIVER = 2;
	public static final int TYPE_CIRCULATION = 3;
	
//	public static final int REFER_STATUS_SHOW=0;
//	public static final int REFER_STATUS_OK=1;
//	public static final int REFER_STATUS_REJECT=2;

	public static final int STATUS_BEFORE_SHOW=0;
	public static final int STATUS_SHOW=1;
	public static final int STATUS_OK=2;
	public static final int STATUS_REJECT=3;
	
	
	public SmallGroupApproval(){
		this.setCreateDate(new Date());
	}
	
	@Id
	@Column(name = "id", columnDefinition="bigint(20)" )
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "small_group_id")
	private SmallGroup smallGroup;
	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approval_id")
	private Approval approval;
	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "draft_id")
//	private Draft draft;
	
	@Column(name = "create_date")
	private Date createDate;

	@Column(name = "complete_date")
	private Date completeDate;
	
	@Column(name = "type", columnDefinition="integer default 0")
	private int type;
	
	@Column(name = "ordering", columnDefinition="integer default 0")
	private int ordering;
	
//	@Column(name = "refer_status", columnDefinition = "integer default 0")
//	private int referStatus;
	
	@Column(name = "status", columnDefinition = "integer default 0")
	private int status;

	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SmallGroup getSmallGroup() {
		return smallGroup;
	}

	public void setSmallGroup(SmallGroup smallGroup) {
		this.smallGroup = smallGroup;
	}

	public Approval getApproval() {
		return approval;
	}

	public void setApproval(Approval approval) {
		this.approval = approval;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}

//	public int getReferStatus() {
//		return referStatus;
//	}
//
//	public void setReferStatus(int referStatus) {
//		this.referStatus = referStatus;
//	}
//
//	public int getApprovalStatus() {
//		return approvalStatus;
//	}
//
//	public void setApprovalStatus(int approvalStatus) {
//		this.approvalStatus = approvalStatus;
//	}

//	public Draft getDraft() {
//		return draft;
//	}
//
//	public void setDraft(Draft draft) {
//		this.draft = draft;
//	}
	
}
