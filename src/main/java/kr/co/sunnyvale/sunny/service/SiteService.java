package kr.co.sunnyvale.sunny.service;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.Site;
import kr.co.sunnyvale.sunny.domain.SiteInactiveUser;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.domain.post.SiteUpdateDTO;

public interface SiteService {

	/**
	 * 관리자가 사이트 작성 시 넣은 회사 도메인이 이미 존재하는지 여부를 판단한다.
	 * @param domain
	 * @return
	 */
	public boolean checkDomain(String domain);
	
	public Site getSiteInstalled();
	
//	public Site getSiteFromDomain(String domainName) ;
//	public Site findFromPathOrId(String pathUrl);
	
	public Site findById(Long id);

	public Site findByUserId(Long userId);

	public Page<Site> getOpenSites(Long userId, String query, String ordering, Boolean desc,
			Integer page, int pageSize);

	public void requestSignup(Sunny sunny, SiteInactiveUser siteInactiveUser);

	public void acceptInactiveUserAfterConfirm(Sunny sunny,
			Long siteInactiveUserId);

//	public void accept(Sunny sunny, Long siteInactiveUserId);

	public List<Site> getUserJoinedSites(Long id, Stream stream);

	public void update(Long id, SiteUpdateDTO siteUpdateDto);

	public User signupComplete(SiteInactiveUser extraSiteInactiveUser);

	public Page<Site> getAllFromSuper(Integer page, int pageSize);

//	public void accept(Sunny sunny, Long id);

}
