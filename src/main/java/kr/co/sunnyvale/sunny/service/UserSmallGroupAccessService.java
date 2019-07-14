package kr.co.sunnyvale.sunny.service;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.SmallGroup;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.UserSmallGroupAccess;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;

public interface UserSmallGroupAccessService {

	
	Page<UserSmallGroupAccess> getJoined(Sunny sunny, Long smallGroupId,
			User user, String searchName, String ordering, Boolean desc, Integer page,
			int pageSize);
	
	List<UserSmallGroupAccess> getJoined(Sunny sunny, SmallGroup smallGroup,
			User user, String searchName, String ordering, Boolean desc, Stream stream);
	
	UserSmallGroupAccess save(Sunny sunny, Long smallGroupId, User user);

	UserSmallGroupAccess findBySmallGroupAndUser(Long smallGroupId, Long userId);

}
