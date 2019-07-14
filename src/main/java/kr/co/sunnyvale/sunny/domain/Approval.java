package kr.co.sunnyvale.sunny.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "APPROVAL")
@PrimaryKeyJoinColumn(name="id")
public class Approval extends Content {
	
	public static final int STATUS_SENT = 0;
	public static final int STATUS_REJECT = 1;
	public static final int STATUS_APPROVED = 2;
	public static final int STATUS_REQUEST_REJECT = 3;
	

	private static final long serialVersionUID = -5024053625575547285L;

	public Approval(){
		super();
	}
	
	public Approval( Site site ){
		super(site, Content.TYPE_APPROVAL);
	}
	
	public Approval(Long contentId) {
		this.setId(contentId);
	}

	
	@Column(name = "status", columnDefinition="integer default 0" )
	private int status;
	
	@Column(name = "check_ordering", columnDefinition="boolean default 0")
	private boolean checkOrdering = false;
	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "user_id")
//	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reject_user_id")
	private User rejectUser;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "current_small_group_id")
	private SmallGroup currentSmallGroup;
	
	@Column(name = "current_ordering", columnDefinition="integer default 0")
	private int currentOrdering;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "current_small_group_approval_id")
	private SmallGroupApproval currentSmallGroupApproval;

	@OneToMany( mappedBy = "approval", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	protected List<SmallGroupApproval> smallGroupApprovals;
	
	@Column(name = "approbator_count", columnDefinition="integer default 0")
	private int approbatorCount;

	@Column(name = "approbator_ok_count", columnDefinition="integer default 0")
	private int approbatorOkCount;
	
	@Column(name = "approbator_reject_count", columnDefinition="integer default 0")
	private int approbatorRejectCount;
	
	@Column(name = "cooperation_count", columnDefinition="integer default 0")
	private int cooperationCount;
	
	@Column(name = "cooperation_ok_count", columnDefinition="integer default 0")
	private int cooperationOkCount;
	
	@Column(name = "receiver_count", columnDefinition="integer default 0")
	private int receiverCount;
	
	@Column(name = "receiver_ok_count", columnDefinition="integer default 0")
	private int receiverOkCount;
	
	@Column(name = "circulation_count", columnDefinition="integer default 0")
	private int circulationCount;

	@Column(name = "circulation_ok_count", columnDefinition="integer default 0")
	private int circulationOkCount;
	
	@Column(name = "in_request_reject", columnDefinition="boolean default 0")
	private boolean inRequestReject;
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public boolean isCheckOrdering() {
		return checkOrdering;
	}

	public void setCheckOrdering(boolean checkOrdering) {
		this.checkOrdering = checkOrdering;
	}

//	public User getUser() {
//		return user;
//	}
//
//	public void setUser(User user) {
//		this.user = user;
//	}

	public SmallGroup getCurrentSmallGroup() {
		return currentSmallGroup;
	}

	public void setCurrentSmallGroup(SmallGroup currentSmallGroup) {
		this.currentSmallGroup = currentSmallGroup;
	}

	public SmallGroupApproval getCurrentSmallGroupApproval() {
		return currentSmallGroupApproval;
	}

	public void setCurrentSmallGroupApproval(
			SmallGroupApproval currentSmallGroupApproval) {
		this.currentSmallGroupApproval = currentSmallGroupApproval;
	}

	public int getApprobatorCount() {
		return approbatorCount;
	}

	public void setApprobatorCount(int approbatorCount) {
		this.approbatorCount = approbatorCount;
	}

	public int getApprobatorOkCount() {
		return approbatorOkCount;
	}

	public void setApprobatorOkCount(int approbatorOkCount) {
		this.approbatorOkCount = approbatorOkCount;
	}

	public int getCooperationCount() {
		return cooperationCount;
	}

	public void setCooperationCount(int cooperationCount) {
		this.cooperationCount = cooperationCount;
	}

	public int getCooperationOkCount() {
		return cooperationOkCount;
	}

	public void setCooperationOkCount(int cooperationOkCount) {
		this.cooperationOkCount = cooperationOkCount;
	}

	public int getReceiverCount() {
		return receiverCount;
	}

	public void setReceiverCount(int receiverCount) {
		this.receiverCount = receiverCount;
	}

	public int getReceiverOkCount() {
		return receiverOkCount;
	}

	public void setReceiverOkCount(int receiverOkCount) {
		this.receiverOkCount = receiverOkCount;
	}

	public int getCirculationCount() {
		return circulationCount;
	}

	public void setCirculationCount(int circulationCount) {
		this.circulationCount = circulationCount;
	}

	public int getCirculationOkCount() {
		return circulationOkCount;
	}

	public void setCirculationOkCount(int circulationOkCount) {
		this.circulationOkCount = circulationOkCount;
	}

	public int getCurrentOrdering() {
		return currentOrdering;
	}

	public void setCurrentOrdering(int currentOrdering) {
		this.currentOrdering = currentOrdering;
	}

	public User getRejectUser() {
		return rejectUser;
	}

	public void setRejectUser(User rejectUser) {
		this.rejectUser = rejectUser;
	}

	public boolean isInRequestReject() {
		return inRequestReject;
	}

	public void setInRequestReject(boolean inRequestReject) {
		this.inRequestReject = inRequestReject;
	}

	public int getApprobatorRejectCount() {
		return approbatorRejectCount;
	}

	public void setApprobatorRejectCount(int approbatorRejectCount) {
		this.approbatorRejectCount = approbatorRejectCount;
	}

	public List<SmallGroupApproval> getSmallGroupApprovals() {
		return smallGroupApprovals;
	}

	public void setSmallGroupApprovals(List<SmallGroupApproval> smallGroupApprovals) {
		this.smallGroupApprovals = smallGroupApprovals;
	}
	
}
