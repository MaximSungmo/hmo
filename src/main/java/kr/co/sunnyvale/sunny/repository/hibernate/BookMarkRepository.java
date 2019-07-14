package kr.co.sunnyvale.sunny.repository.hibernate;

import kr.co.sunnyvale.sunny.domain.BookMark;
import kr.co.sunnyvale.sunny.domain.Content;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.util.CriteriaUtils;

import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
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

@Repository(value = "bookMarkRepository")
public class BookMarkRepository extends HibernateGenericRepository<BookMark>{
	public BookMarkRepository(){

	}

	public boolean isAlreadyBookMarked(Sunny sunny, Long contentId,
			Long userId) {
		Criteria criteria = getCriteria();

		criteria.add(Restrictions.eq("site", sunny.getSite()));
		criteria.add(Restrictions.eq("content.id", contentId));
		criteria.add(Restrictions.eq("user.id", userId));
		
		criteria.setMaxResults(1);
		
		Object obj = criteria.uniqueResult();
		if( obj == null ){
			return false;
		}
		return true;
		
	}

	public BookMark find(Sunny sunny, Content content, User user) {

		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("site", sunny.getSite()));
		criteria.add(Restrictions.eq("content", content));
		criteria.add(Restrictions.eq("user", user));

		criteria.setMaxResults(1);
		
		return (BookMark) criteria.uniqueResult();
	}

	public Page<BookMark> getPagedBookMarks(Sunny sunny, Integer[] types,
			Long smallGroupId, User user, String query, String ordering,
			Integer page, int pageSize) {

		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("user", user));
		criteria.add(Restrictions.eq("site", sunny.getSite()));
		
		if( types != null || query != null ){
			criteria.createAlias("content", "contentAlias");
		}
		
		if( types != null ){
			Disjunction disjunction = Restrictions.disjunction();
			for( Integer type : types ){
				disjunction.add(Restrictions.eq("contentAlias.type", type));	
			}
			criteria.add(disjunction);
		}
		
		if( query != null ){
			Disjunction disjunction = Restrictions.disjunction();
			disjunction.add(CriteriaUtils.returnRestrictionAfterCheckInitial("contentAlias.title", query ));
			disjunction.add(CriteriaUtils.returnRestrictionAfterCheckInitial("contentAlias.rawText", query ));
			disjunction.add(CriteriaUtils.returnRestrictionAfterCheckInitial("this.title", query ));
			disjunction.add(CriteriaUtils.returnRestrictionAfterCheckInitial("this.comment", query ));
			criteria.add(disjunction);
		}
		
		criteria.addOrder(Order.desc("createDate"));
		
		return getPagedList(criteria, page, pageSize);
		
	}

}
