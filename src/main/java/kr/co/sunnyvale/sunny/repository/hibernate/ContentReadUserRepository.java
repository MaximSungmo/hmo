package kr.co.sunnyvale.sunny.repository.hibernate;

import kr.co.sunnyvale.sunny.domain.Content;
import kr.co.sunnyvale.sunny.domain.ContentReadUser;
import kr.co.sunnyvale.sunny.domain.LastRead;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
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

@Repository(value = "contentReadUserRepository")
public class ContentReadUserRepository extends HibernateGenericRepository<ContentReadUser>{
	public ContentReadUserRepository(){

	}

	public boolean alreadyRead(Long contentId, Long userId) {
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("content.id", contentId));
		criteria.add(Restrictions.eq("user.id", userId));
		
		criteria.setMaxResults(1);
		Object object = criteria.uniqueResult();
		
		if( object == null ){
			return false;	
		}else{
			return true;
		}
	}

	public boolean alreadyRead(Long contentId, User user) {
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("content.id", contentId));
		criteria.add(Restrictions.eq("user", user));
		
		criteria.setMaxResults(1);
		Object object = criteria.uniqueResult();
		
		if( object == null ){
			return false;	
		}else{
			return true;
		}
	}

//	public int getNoticeReadCount(Sunny sunny, Long userId, LastRead lastRead) {
//		Criteria criteria = getCriteria();
//		
//		criteria.createAlias("content", "contentAlias");
//		criteria.add(Restrictions.eq("user.id", userId));
//		
//		if( lastRead != null )
//			criteria.add(Restrictions.gt("createDate", lastRead.getNoticeLastRead()));
//		
//		criteria.setProjection(Projections.rowCount());
//		
//		Object rowCount = criteria.uniqueResult();
//		if( rowCount == null )
//			return 0;
//		
//		return ((Number) rowCount).intValue();
//	
//	}

	
}
