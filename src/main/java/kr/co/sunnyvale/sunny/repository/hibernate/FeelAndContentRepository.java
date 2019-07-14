package kr.co.sunnyvale.sunny.repository.hibernate;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.Content;
import kr.co.sunnyvale.sunny.domain.Feel;
import kr.co.sunnyvale.sunny.domain.FeelAndContent;
import kr.co.sunnyvale.sunny.domain.extend.Stream;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 * 
 * {Hibernate}
 *	
 *<p>
 *	컨텐츠에 대한 평가들에 대해 작업하는 Repository
 *
 * @author mook
 *
 *	
 */

@Repository(value = "feelAndContentRepository")
public class FeelAndContentRepository extends HibernateGenericRepository<FeelAndContent>{
	public FeelAndContentRepository(){

	}

	public FeelAndContent findUniqFeelAndContent(Content content, Feel feel) {
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("content", content));
    	criteria.add(Restrictions.eq("feel", feel));
    	FeelAndContent feelContent = (FeelAndContent) criteria.uniqueResult();
    	return feelContent;
		
	}

	public List<FeelAndContent> getListByContent(Long contentId, Stream stream) {
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("content.id", contentId));
		return getPagedList(criteria, stream);
	}

}
