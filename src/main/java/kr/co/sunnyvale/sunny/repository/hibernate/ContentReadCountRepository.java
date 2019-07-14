package kr.co.sunnyvale.sunny.repository.hibernate;

import kr.co.sunnyvale.sunny.domain.ContentReadCount;

import org.hibernate.Criteria;
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

@Repository(value = "contentViewCountRepository")
public class ContentReadCountRepository extends HibernateGenericRepository<ContentReadCount>{
	public ContentReadCountRepository(){

	}

	public ContentReadCount findByContentId(Long id) {
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("content.id", id));
		return (ContentReadCount) criteria.uniqueResult();	
	}

	
}
