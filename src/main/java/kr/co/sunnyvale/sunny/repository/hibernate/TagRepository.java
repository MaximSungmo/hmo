package kr.co.sunnyvale.sunny.repository.hibernate;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.SmallGroup;
import kr.co.sunnyvale.sunny.domain.Tag;
import kr.co.sunnyvale.sunny.domain.extend.Stream;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
/**
 * {Hibernate}
 *	
 *<p>
 *	Tag 에 대한 작업을 하는 Repository
 *
 * @author mook
 *
 */
@Repository(value = "tagRepository")
public class TagRepository extends HibernateGenericRepository<Tag>{
	public TagRepository(){

	}

	public List<Tag> getTagsOrderByReferenceCount(Long smallGroupId, int type) {

		Criteria criteria = getCriteria();
		if( smallGroupId == null ){
			criteria.add( Restrictions.isNull("smallGroup.id"));	
		}else{
			criteria.add( Restrictions.eq("smallGroup.id", smallGroupId));
		}
		
		criteria.add( Restrictions.eq("contentType", type));
		criteria.addOrder(Order.desc("referenceCount"));
		
		return criteria.list();
		
	}

	public List<Tag> findMatchTitle(SmallGroup smallGroup, int contentType, String input, Stream stream) {
		Criteria criteria = getCriteria();
    	criteria.add( Restrictions.like("title", input, MatchMode.ANYWHERE) );
    	criteria.add( Restrictions.eq("smallGroup", smallGroup) );
    	criteria.add( Restrictions.eq("contentType", contentType));
		return getPagedList(criteria, stream);
	}

	public Tag findByTitle(SmallGroup smallGroup, int contentType, String title) {
		Criteria criteria = getCriteria();
    	criteria.add( Restrictions.eq("title", title) );
    	criteria.add( Restrictions.eq("smallGroup", smallGroup) );
    	criteria.add( Restrictions.eq("contentType", contentType));
    	criteria.setMaxResults(1);
		return (Tag) criteria.uniqueResult();
	}

	public List<Tag> getTopTags(Long smallGroupId) {
		
		Criteria criteria = getCriteria();
		criteria.add( Restrictions.eq("smallGroup.id", smallGroupId));
		criteria.addOrder(Order.desc("referenceCount"));
		criteria.setMaxResults(5);
		return criteria.list();
	}


	public List<Tag> getSmallGroupTags(Long smallGroupId) {
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("smallGroup.id", smallGroupId));
		criteria.addOrder(Order.desc("referenceCount"));
		return criteria.list();
	}

	public List<String> getSmallGroupTagNames(Long smallGroupId) {
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("smallGroup.id", smallGroupId));
		criteria.addOrder(Order.desc("referenceCount"));
		criteria.setProjection(Projections.property("title"));
		return criteria.list();
	}

	public void updateToNormalTags(Long smallGroupId) {
		String hqlUpdate = "update Tag tag set adminSelected = false where smallGroup.id = :smallGroupId and adminSelected = true";
		Query query = getCurrentSession().createQuery( hqlUpdate ).setLong( "smallGroupId", smallGroupId);
		query.executeUpdate();
		getCurrentSession().clear();
	}

	public List<Tag> getComposerTags(Long smallGroupId) {
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("smallGroup.id", smallGroupId));
		criteria.add(Restrictions.eq("adminSelected", true));
		criteria.addOrder(Order.asc("adminOrdering"));
		return criteria.list();
	}
	
//	public List<Tag> getTags(Long contentId, Stream stream) {
//		Criteria criteria = getCriteria();
//		Criteria contentCriteria = criteria.createCriteria( "contents" );
//		contentCriteria.add( Restrictions.eq( "id", contentId ) );
//		criteria.setProjection( Projections.projectionList()
//				.add(Projections.property("this.id"))
//				.add(Projections.property("this.title"))
//				.add(Projections.property("this.referenceCount"))
//						
//		);
//		return getPagedList(criteria, stream);
//	}

}
