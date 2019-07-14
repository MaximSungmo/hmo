package kr.co.sunnyvale.sunny.repository.hibernate;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.SmallGroupApproval;
import kr.co.sunnyvale.sunny.domain.extend.Stream;

import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;


@Repository(value = "smallGroupApprovalRepository")
public class SmallGroupApprovalRepository extends HibernateGenericRepository<SmallGroupApproval>{
	
	
	public SmallGroupApprovalRepository(){

	}

	public SmallGroupApproval find(Long userId, Long approvalId, Integer type) {
		
		Criteria criteria = getCriteria();
		
		if( type != null ){
			criteria.add(Restrictions.eq("type", type));
		}
		
		criteria.createAlias("smallGroup", "smallGroupAlias");
		criteria.createAlias("smallGroupAlias.smallGroupUsers", "smallGroupUsersAlias");
		
		criteria.add(Restrictions.eq("smallGroupUsersAlias.user.id", userId));
		
//		criteria.add(Restrictions.eq("smallGroup.id", smallGroupId));
		
		
		criteria.add(Restrictions.eq("approval.id", approvalId));
		
		
		criteria.setMaxResults(1);
		return (SmallGroupApproval) criteria.uniqueResult();
	}

//	public List<SmallGroupApproval> getUsers(Long approvalId, Stream stream, Integer ... type
//			) {
//		
//		Criteria criteria = getCriteria();
//		criteria.createAlias("smallGroup", "smallGroupAlias");
//		
//		if( type != null ){
//			criteria.add(Restrictions.eq("type", type));
//		}
//
//		criteria.add(Restrictions.eq("approval.id", approvalId));
//		
//		return getPagedList(criteria, stream);
//		
//	}
	public List<SmallGroupApproval> getUsers(Long approvalId, Stream stream, Integer ... type
			) {
	
		Criteria criteria = getCriteria();
		criteria.createAlias("smallGroup", "smallGroupAlias");
		
		if( type != null && type.length > 0 ){
			
			Disjunction disjunctionType = Restrictions.disjunction();
			
			for( Integer typeEach : type ){
		    	disjunctionType.add(Restrictions.eq("type", typeEach));
			}
			criteria.add(disjunctionType);
		}

		criteria.add(Restrictions.eq("approval.id", approvalId));
		
		return getPagedList(criteria, stream);
	}
	
	public SmallGroupApproval findApprobatorByOrdering(Long approvalId,
			int ordering) {

		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("approval.id", approvalId));
		criteria.add(Restrictions.eq("ordering", ordering ));
		criteria.setMaxResults(1);
		return (SmallGroupApproval) criteria.uniqueResult();
		
	}

	public List<SmallGroupApproval> getMiscUsers(Long approvalId, Stream stream) {
		
		Criteria criteria = getCriteria();
		criteria.createAlias("smallGroup", "smallGroupAlias");
		
		criteria.add(Restrictions.ge("type", SmallGroupApproval.TYPE_COOPERATION));
		criteria.add(Restrictions.le("type", SmallGroupApproval.TYPE_CIRCULATION));
		criteria.add(Restrictions.eq("approval.id", approvalId));
		return getPagedList(criteria, stream);
	}

	
	

//	@SuppressWarnings("unchecked")
//	private List<Long> getUserJoinedSmallGroupIds( Long userId ){
//		Criteria userSmallGroupAccessCriteria = getCurrentSession().createCriteria(UserSmallGroupAccess.class);
//		userSmallGroupAccessCriteria.add(Restrictions.eq("user.id", userId));
//		userSmallGroupAccessCriteria.setProjection(Projections.projectionList().add(Projections.property("smallGroup.id"), "smallGroupId"));
//		return userSmallGroupAccessCriteria.list();
//	}

}
