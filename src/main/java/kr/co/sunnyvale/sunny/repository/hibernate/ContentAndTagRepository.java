package kr.co.sunnyvale.sunny.repository.hibernate;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.ContentAndTag;
import kr.co.sunnyvale.sunny.domain.SmallGroup;
import kr.co.sunnyvale.sunny.domain.extend.Stream;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
/**
 * {Hibernate}
 *	
 *<p>
 *	Story 에 대한 태그들을 관리하는 tagRelation Repository
 *
 * @author mook
 *
 *	
 *	
 *
 */
@Repository(value = "contentAndTagRepository")
public class ContentAndTagRepository extends HibernateGenericRepository<ContentAndTag> {

	public List<ContentAndTag> getMatchRelation(
			Long contentId, Stream stream) {
		Criteria criteria = getCriteria();
		criteria.createAlias("tag", "tagAlias");
		criteria.add(Restrictions.eq("content.id", contentId));
		
		return getPagedList(criteria, stream);
	}

	public List<ContentAndTag> getContentNotice(SmallGroup smallGroup, int contentType, String tagTitle, Stream stream) {

		Criteria criteria = getCriteria();
		criteria.createAlias("tag", "tagAlias");
		criteria.createAlias("content", "contentAlias");
		criteria.add(Restrictions.eq("tagAlias.title", tagTitle));
		criteria.add(Restrictions.eq("tagAlias.contentType", contentType));
		criteria.add(Restrictions.eq("tagAlias.smallGroup", smallGroup));
		criteria.addOrder(Order.desc("contentAlias.createDate"));
		return getPagedList(criteria, stream);
	}
 	
		
}
