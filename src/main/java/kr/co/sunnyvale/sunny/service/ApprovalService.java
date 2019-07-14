package kr.co.sunnyvale.sunny.service;

import kr.co.sunnyvale.sunny.domain.Approval;
import kr.co.sunnyvale.sunny.domain.Content;
import kr.co.sunnyvale.sunny.domain.Draft;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;

public interface ApprovalService {

	public Content createFromDraft(Sunny sunny, Draft draft, String ipAddress);
	
	
//	public List<ContentDTO> fetchApprovals(Sunny sunny, Long smallGroupId, Boolean isWantChildren, User user,
//			String[] queries, Stream stream);


	public Approval findById(Long id);


	public void ok(Sunny sunny, Long approvalId, Long smallGroupApprovalId, Long userId);


	public void reject(Sunny sunny, Long approvalId, Long smallGroupApprovalId, Long userId);


	public void requestReject(Long approvalId, Long userId);


	public void cancelRequestReject(Long approvalId, Long userId);


	public Approval findForView(Long id, User user);


	public Approval getSequence(Sunny sunny, User authUser, int direct,
			Approval approval, String[] queries, Long smallGroupId,
			Long userId, String menu, String tagTitle);


}