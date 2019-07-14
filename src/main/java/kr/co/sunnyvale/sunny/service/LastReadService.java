package kr.co.sunnyvale.sunny.service;

import kr.co.sunnyvale.sunny.domain.LastRead;
import kr.co.sunnyvale.sunny.domain.Site;
import kr.co.sunnyvale.sunny.domain.SmallGroup;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.repository.hibernate.LastReadRepository;
import kr.co.sunnyvale.sunny.util.RequestUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( value="lastReadService" )
@Transactional
public class LastReadService {
	
	@Autowired
	private LastReadRepository lastReadRepository;

	@Autowired
	private SmallGroupService smallGroupService;

	@Autowired
	private SiteService siteService;
	
//	@Transactional
//	public int updateNoteCount(Sunny sunny, User user) {
//		LastRead lastRead = lastReadRepository.findUniqByObject("user", user);
//
//		Site site = siteService.getSiteFromDomain( RequestUtils.getCurrentServerName() );
//		SmallGroup smallGroup = smallGroupService.find(site.getDefaultSmallGroup().getId());
//		
//		
//		if( lastRead.getNoteLastReadCount() != smallGroup.getNoteCount() ){
//			lastRead.setNoteLastReadCount( smallGroup.getNoteCount() );
//		}
//		return lastRead.getNoteLastReadCount();
//		
//	}
//	@Transactional
//	public int updateQuestionCount(User user) {
//		LastRead lastRead = lastReadRepository.findUniqByObject("user", user);
//
//		Site site = siteService.getSiteFromDomain( RequestUtils.getCurrentServerName() );
//		
//		SmallGroup smallGroup = smallGroupService.find(site.getDefaultSmallGroup().getId());
//		
//		if( lastRead.getQuestionLastReadCount() != smallGroup.getQuestionCount() ){
//			lastRead.setQuestionLastReadCount( smallGroup.getQuestionCount() );
//		}
//		return lastRead.getQuestionLastReadCount();
//	}
//	
}
