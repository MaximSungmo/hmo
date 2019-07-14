package kr.co.sunnyvale.sunny.service;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.SmallGroupApproval;
import kr.co.sunnyvale.sunny.domain.extend.Stream;

public interface SmallGroupApprovalService {

	public SmallGroupApproval save(SmallGroupApproval smallGroupApproval);

	public SmallGroupApproval find(Long userId, Long approvalId, Integer type);

//	public List<SmallGroupApproval> getUsers(Long approvalId, Integer type,
//			Stream stream);

	public SmallGroupApproval findApprobatorByOrdering(Long approvalId, int ordering);

	public List<SmallGroupApproval> getMiscUsers(Long approvalId, Stream stream);

	public void update(SmallGroupApproval smallGroupApproval);

	public List<SmallGroupApproval> getUsers(Long approvalId, 
			Stream stream, Integer ... type);
		
}
