package kr.co.sunnyvale.sunny.repository.hibernate;

import kr.co.sunnyvale.sunny.domain.UserAppleToken;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository(value = "userAppleTokenRepository")
public class UserAppleTokenRepository extends HibernateGenericRepository<UserAppleToken>{
	public UserAppleTokenRepository(){

	}


	public UserAppleToken findUserAndDeviceToken(Long userId, String deviceToken) {
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("user.id", userId));
		criteria.add(Restrictions.eq("deviceToken", deviceToken));
		criteria.setMaxResults(1);
		return (UserAppleToken) criteria.uniqueResult();
	}


	public void removeAllMatchedTokens(Long userId, String deviceToken) {
		String hqlUpdate = "delete from UserAppleToken appleToken where user.id = :userId and deviceToken = :deviceToken";
		Query query = getCurrentSession().createQuery( hqlUpdate ).setLong( "userId", userId).setString( "deviceToken", deviceToken );
		query.executeUpdate();
		getCurrentSession().clear();
	
	}

}
