package kr.co.sunnyvale.sunny.repository.hibernate;

import kr.co.sunnyvale.sunny.domain.ContentDynamic;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 * {Hibernate}
 * 
 *<p>
 *	Story, StoryLine, Step 등의 부모 클래스가 되는 Content Repository
 *
 * @author mook
 *
 *	
 *
 */
@Repository(value = "contentDynamicRepository")
public class ContentDynamicRepository extends HibernateGenericRepository<ContentDynamic>{
	public ContentDynamicRepository(){

	}

	public ContentDynamic findByContentId(Long id) {
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("content.id", id));
		return (ContentDynamic) criteria.uniqueResult();		
		
	}

}
