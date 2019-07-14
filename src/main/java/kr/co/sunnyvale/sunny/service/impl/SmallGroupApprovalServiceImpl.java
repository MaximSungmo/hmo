package kr.co.sunnyvale.sunny.service.impl;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.SmallGroupApproval;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.repository.hibernate.SmallGroupApprovalRepository;
import kr.co.sunnyvale.sunny.service.SmallGroupApprovalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( value="smallGroupApprovalService" )
@Transactional
public class SmallGroupApprovalServiceImpl implements SmallGroupApprovalService {
	
	@Autowired
	private SmallGroupApprovalRepository smallGroupApprovalRepository;
	
//	@Autowired
//	private ApprovalService approvalService; 
	
	@Override
	@Transactional
	public SmallGroupApproval save(SmallGroupApproval smallGroupApproval){
		smallGroupApprovalRepository.save(smallGroupApproval);
		return smallGroupApproval;
	}

	@Override
	@Transactional(readOnly = true)
	public SmallGroupApproval find(Long userId, Long approvalId, Integer type) {
		return smallGroupApprovalRepository.find(userId, approvalId, type);
	}

	@Override
	@Transactional(readOnly = true)
	public List<SmallGroupApproval> getUsers(Long approvalId, 
			Stream stream, Integer ... type) {
		return smallGroupApprovalRepository.getUsers( approvalId,stream , type);
	}


//	@Override
//	public List<SmallGroupApproval> getUsers(Long approvalId, Integer[] types,
//			Stream stream) {
//		return smallGroupApprovalRepository.getUsers( approvalId, stream, types);
//	}
//	
	@Override
	@Transactional(readOnly = true)
	public SmallGroupApproval findApprobatorByOrdering(Long approvalId,
			int ordering) {
		return smallGroupApprovalRepository.findApprobatorByOrdering( approvalId, ordering );
	}

	@Override
	@Transactional(readOnly = true)
	public List<SmallGroupApproval> getMiscUsers(Long approvalId, Stream stream) {
		return smallGroupApprovalRepository.getMiscUsers( approvalId, stream);
	}

	@Override
	@Transactional
	public void update(SmallGroupApproval smallGroupApproval) {
		smallGroupApprovalRepository.update(smallGroupApproval);
	}


}
