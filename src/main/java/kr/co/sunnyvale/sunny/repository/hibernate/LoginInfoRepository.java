package kr.co.sunnyvale.sunny.repository.hibernate;

import java.io.Serializable;
import java.util.Date;

import kr.co.sunnyvale.sunny.domain.LoginInfo;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 * 	{Hibernate}
 *	
 *<p>
 *	
 *
 * @author mook
 *
 *
 */

@Repository(value = "loginInfoRepository")
public class LoginInfoRepository extends HibernateGenericRepository<LoginInfo>{
	public LoginInfoRepository(){

	}

	public Date getLastLoginDate( Long userId ){
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("user.id", userId));
		criteria.addOrder(Order.desc("createDate"));
		criteria.setMaxResults(1);
		criteria.setProjection(Projections.property("createDate"));
		return (Date) criteria.uniqueResult();
	}
	
	public Serializable getCreateDate(Long logId) {
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("id", logId));
		return (Serializable) criteria.uniqueResult();
	}
}
