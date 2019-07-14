package kr.co.sunnyvale.sunny.repository.hibernate;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.Approval;
import kr.co.sunnyvale.sunny.domain.Content;
import kr.co.sunnyvale.sunny.domain.SmallGroupApproval;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.UserSmallGroupAccess;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository(value = "approvalRepository")
public class ApprovalRepository extends HibernateGenericRepository<Approval>{
	@SuppressWarnings("unchecked")
	private List<Long> getUserJoinedSmallGroupIds( User user ){
		Criteria userSmallGroupAccessCriteria = getCurrentSession().createCriteria(UserSmallGroupAccess.class);
		userSmallGroupAccessCriteria.add(Restrictions.eq("user", user));
		userSmallGroupAccessCriteria.setProjection(Projections.projectionList().add(Projections.property("smallGroup.id"), "smallGroupId"));
		return userSmallGroupAccessCriteria.list();
	}
	
	
	public ApprovalRepository(){

	}

	public Approval getSequence(Sunny sunny, User authUser, int direct,
			Approval approval, String[] queries, Long smallGroupId,
			Long userId, String menu, String tagTitle) {
		
		/*
		 *	 사용자가 포함된 smallGroup 은 엄청나게 많지는 않을 것이기에 직접 List를 뽑아낸다.
		 *	추후 이 리스트를 캐시로 관리할 것. 
		 */
	
		List<Long> userJoinedSmallGroupIds = getUserJoinedSmallGroupIds(authUser);
		
		
		if( userJoinedSmallGroupIds == null || userJoinedSmallGroupIds.size() == 0  )
			return null;

		/*
		 * smallGroup 들 뽑아내기 끝
		 */
		Criteria criteria = getCurrentSession().createCriteria(Approval.class);
		
		criteria.createAlias("smallGroupContentAccesses", "smallGroupContentAccessesAlias");
//		
		criteria.add(Restrictions.in("smallGroupContentAccessesAlias.smallGroup.id", userJoinedSmallGroupIds));
		criteria.addOrder(Order.desc("smallGroupContentAccessesAlias.createDate"));
		
		criteria.add(Restrictions.eq("this.site", sunny.getSite()));

//		contentIdCriteria.add(Restrictions.or(Restrictions.eq("this.type", Content.TYPE_STORY),
//											Restrictions.eq("this.type", Content.TYPE_APPROVAL)));
		criteria.add(
		Restrictions.eq("this.type", Content.TYPE_APPROVAL));

		if( smallGroupId != null ){
			criteria.add(Restrictions.eq("this.smallGroup.id", smallGroupId));
		}
		
		
		Integer status = null, approvalType = null;
		User sentUser = null, exceptUser = null;
		
		if( menu != null ){
			switch(menu){
			case "index":
			
				break;
			case "process":
				status = Approval.STATUS_SENT;
				break;
				
			case "sent_process":
				status = Approval.STATUS_SENT;
				sentUser = authUser;
				break;
				
			case "receive_process":
				status = Approval.STATUS_SENT;
				exceptUser = authUser;
				break;
				
			case "approved":
				status = Approval.STATUS_APPROVED;
				break;
				
			case "sent_approved":
				status = Approval.STATUS_APPROVED;
				sentUser = authUser;
				break;
				
			case "receive_approved":
				status = Approval.STATUS_APPROVED;
				exceptUser = authUser;
				break;
				
			case "sent_reject":
				status = Approval.STATUS_REJECT;
				sentUser = authUser;
				break;
				
			case "receive_reject":
				status = Approval.STATUS_REJECT;
				exceptUser = authUser;
				break;
			
			case "receive":
				approvalType = SmallGroupApproval.TYPE_RECEIVER;
				exceptUser = authUser;
				
				break;
			case "circulation":
				approvalType = SmallGroupApproval.TYPE_CIRCULATION;
				exceptUser = authUser;
				break;
			}
		}
	
		if( status != null ){
			criteria.add(Restrictions.eq("this.status", status));
		}
		
		if( sentUser != null ){
			criteria.add(Restrictions.eq("this.user", sentUser));
		}
		
		if( approvalType != null ){
			criteria.createAlias("smallGroupApprovals", "smallGroupApprovalsAlias");
			criteria.add(Restrictions.eq("smallGroupApprovalsAlias.type", approvalType));
		}
		
		if( exceptUser != null ){
			criteria.add(Restrictions.ne("this.user", exceptUser));
		}
		
		criteria.add(Restrictions.eq("this.deleteFlag", false));
		
		if( queries != null ){
			for( String query : queries ){
				criteria.add(Restrictions.like("rawText", query, MatchMode.ANYWHERE));	
			}
		}
		if( direct > 0 ){
			criteria.addOrder( Order.asc("createDate") );
			criteria.add(Restrictions.gt("id", approval.getId()));
		}else{
			criteria.addOrder( Order.desc("createDate") );
			criteria.add(Restrictions.lt("id", approval.getId()));
		}
		criteria.setMaxResults(1);
		
		return (Approval) criteria.uniqueResult();

	}


}
