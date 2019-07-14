package kr.co.sunnyvale.sunny.repository.hibernate;

import kr.co.sunnyvale.sunny.domain.Content;
import kr.co.sunnyvale.sunny.domain.SmallGroup;
import kr.co.sunnyvale.sunny.domain.SmallGroupContentAccess;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;


@Repository(value = "smallGroupContentAccessRepository")
public class SmallGroupContentAccessRepository extends HibernateGenericRepository<SmallGroupContentAccess>{

	public SmallGroupContentAccessRepository() {
		super();
	}

	public SmallGroupContentAccess save(Content content, SmallGroup eachSmallGroup ) {
		
		SmallGroupContentAccess smallGroupContentAccess = new SmallGroupContentAccess();
		
		smallGroupContentAccess.setContent(content);
		
//		if( w != null )
//			smallGroupContentAccess.setWritePermission(w);
//		
//		if( d != null )
//			smallGroupContentAccess.setDeletePermission(d);
		
		smallGroupContentAccess.setSmallGroup(eachSmallGroup);
		
		save( smallGroupContentAccess );
		
		return smallGroupContentAccess;
	}

	public SmallGroupContentAccess findByContentAndSmallGroup(Long contentId, Long smallGroupId) {
		
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("content.id", contentId));
		criteria.add(Restrictions.eq("smallGroup.id", smallGroupId));
		
		criteria.setMaxResults(1);
		return (SmallGroupContentAccess) criteria.uniqueResult();
	}

}
