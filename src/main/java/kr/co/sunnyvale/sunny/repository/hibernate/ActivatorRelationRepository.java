package kr.co.sunnyvale.sunny.repository.hibernate;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.ActivatorRelation;
import kr.co.sunnyvale.sunny.domain.Notification;
import kr.co.sunnyvale.sunny.domain.User;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 * 	{Hibernate}
 *	
 *<p>
 *	활동 로그인 액티비티를 관리하는 Repository
 *
 * @author mook
 *
 *
 */

@Repository(value = "activatorRelationRepository")
public class ActivatorRelationRepository extends HibernateGenericRepository<ActivatorRelation>{
	public ActivatorRelationRepository(){

	}

	public boolean alreadyJoinActivator(Notification notification,
			User activator) {
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("notification", notification));
		criteria.add(Restrictions.eq("user", activator));
		
		Object result = criteria.uniqueResult();
		if( result == null ){
			return false;
		}else{
			return true;
		}
	}

	public ActivatorRelation find(Notification notification, User user) {
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("notification", notification));
		criteria.add(Restrictions.eq("user", user));
		
		return (ActivatorRelation) criteria.uniqueResult();
	}

	public List<ActivatorRelation> getCurrentActivators(Notification notification, int fetchCount) {
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("notification", notification ));
		criteria.addOrder( Order.desc("createDate"));
		criteria.setMaxResults( fetchCount );
		
		return criteria.list();
	}
	
	

	
}
