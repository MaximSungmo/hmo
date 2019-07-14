package kr.co.sunnyvale.sunny.repository.hibernate;

import kr.co.sunnyvale.sunny.domain.Draft;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 * 
 *	{Hibernate}
 *	
 *<p>
 *	퀘스천 때 쓰는 카테고리 Repository
 *
 * @author mook
 *
 *
 */

@Repository(value = "draftRepository")
public class DraftRepository extends HibernateGenericRepository<Draft>{
	
	public DraftRepository(){

	}


	public Page<Draft> getPagedList(Sunny sunny, Long smallGroupId, User user, int type, Integer pageNum, int pageSize) {

		Criteria criteria = getCriteria();
		if( smallGroupId == null ){

			smallGroupId = sunny.getSite().getLobbySmallGroup().getId();
		}
		criteria.add( Restrictions.eq("smallGroup.id", smallGroupId));
		criteria.add( Restrictions.eq("type", type) );
		criteria.add( Restrictions.eq("user", user));
		criteria.addOrder( Order.desc("updateDate"));
		return getPagedList(criteria, pageNum, pageSize);
	}

	public Draft selectFromTarget(Long targetId, User user) {

		Criteria criteria = getCriteria();
		criteria.add( Restrictions.eq("targetContent.id", targetId) );
		criteria.add( Restrictions.eq("user", user));
		criteria.setMaxResults(1);
		return (Draft)criteria.uniqueResult();
	}


	public Draft selectFromPush(Long id, User user) {		
		Criteria criteria = getCriteria();
	criteria.add( Restrictions.eq("pushContent.id", id) );
	criteria.add( Restrictions.eq("user", user));
	criteria.setMaxResults(1);
	return (Draft)criteria.uniqueResult();
	}

}
