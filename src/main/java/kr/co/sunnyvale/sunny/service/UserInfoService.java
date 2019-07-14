package kr.co.sunnyvale.sunny.service;

import kr.co.sunnyvale.sunny.domain.UserInfo;

public interface UserInfoService {

	/**
	 * 이메일을 통한 사용자 가져오기(캐시)
	 * @param email
	 * @return
	 */
	UserInfo findByUserId( Long userId );
	
	
}
