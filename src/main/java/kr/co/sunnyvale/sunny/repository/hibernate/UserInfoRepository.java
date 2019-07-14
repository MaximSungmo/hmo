package kr.co.sunnyvale.sunny.repository.hibernate;

import kr.co.sunnyvale.sunny.domain.UserInfo;

import org.springframework.stereotype.Repository;

@Repository(value = "userInfoRepository")
public class UserInfoRepository extends HibernateGenericRepository<UserInfo>{
	public UserInfoRepository(){

	}

	public UserInfo findByUserId(Long id) {
		return null;
	}


}
