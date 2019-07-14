package kr.co.sunnyvale.sunny.repository.hibernate;

import kr.co.sunnyvale.sunny.domain.SmallGroup;
import kr.co.sunnyvale.sunny.domain.SmallGroupSmallGroupAccess;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.util.CriteriaUtils;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository(value = "smallGroupSmallGroupAccessRepository")
public class SmallGroupSmallGroupAccessRepository extends HibernateGenericRepository<SmallGroupSmallGroupAccess>{
	public SmallGroupSmallGroupAccessRepository(){

	}

	public Page<SmallGroupSmallGroupAccess> getPagedAccessibleList(Sunny sunny,
			Long smallGroupId, String queryName, String ordering, Boolean desc, Integer page, int pageSize) {
		
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("smallGroup.id", smallGroupId));
		

		criteria.createAlias("accessSmallGroup", "smallGroupAlias");
		if( queryName != null ){
			criteria.createAlias("smallGroupAlias.creator", "userAlias", Criteria.LEFT_JOIN);
			criteria.add(Restrictions.or(
				Restrictions.and(
						Restrictions.eq("smallGroupAlias.type", SmallGroup.TYPE_ME), 
						CriteriaUtils.returnRestrictionAfterCheckInitial("userAlias.name", queryName))
				, 
				CriteriaUtils.returnRestrictionAfterCheckInitial("smallGroupAlias.name", queryName))
				);
		}
		if( ordering != null ){
			if( desc == null || desc == true ){
				criteria.addOrder( Order.desc(ordering) );
			}else{
				criteria.addOrder( Order.asc(ordering) );
			}
		}else{
			criteria.addOrder( Order.desc("createDate") );
		}
		
		return getPagedList(criteria, page, pageSize);
		
	}

//	public SmallGroupSmallGroupAccess save(SmallGroup smallGroup, SmallGroup accessSmallGroup,
//			Boolean r, Boolean w, Boolean d) {
//		
//		SmallGroupSmallGroupAccess smallGroupSmallGroupAccess = new SmallGroupSmallGroupAccess();
//		
//		smallGroupSmallGroupAccess.setSmallGroup(smallGroup);
//		
//		if( w != null )
//			smallGroupSmallGroupAccess.setWritePermission(w);
//		
//		if( d != null )
//			smallGroupSmallGroupAccess.setDeletePermission(d);
//		
//		smallGroupSmallGroupAccess.setAccessSmallGroup(accessSmallGroup);
//		
//		save( smallGroupSmallGroupAccess );
//		
//		return smallGroupSmallGroupAccess;
//	}

	public SmallGroupSmallGroupAccess getAccess(Sunny sunny, Long smallGroupId, Long accessSmallGroupId) {
		
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("smallGroup.id", smallGroupId ));
		criteria.add(Restrictions.eq("accessSmallGroup.id", accessSmallGroupId));
		criteria.setMaxResults(1);
		
		return (SmallGroupSmallGroupAccess) criteria.uniqueResult();
		
	}
}