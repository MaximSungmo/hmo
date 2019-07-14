package kr.co.sunnyvale.sunny.service;

import kr.co.sunnyvale.sunny.domain.SmallGroup;
import kr.co.sunnyvale.sunny.domain.SmallGroupSmallGroupAccess;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;

public interface SmallGroupSmallGroupAccessService {
	
	public Page<SmallGroupSmallGroupAccess> getPagedAccessibleList(Sunny sunny, Long smallGroupId, String queryName, String ordering, Boolean desc, Integer page, int pageSize );

	
	/**
	 * 이미 존재하는지 체크한 뒤 저장한다. 
	 * @param smallGroup
	 * @param saveSmallGroup
	 * @param r
	 * @param w
	 * @param d
	 */
	public void save(Sunny sunny, SmallGroup smallGroup, SmallGroup saveSmallGroup, boolean checkExist);


	public void updatePermission(Long smallGroupSmallGroupId);


	public void remove(Long smallGroupSmallGroupId);

}
