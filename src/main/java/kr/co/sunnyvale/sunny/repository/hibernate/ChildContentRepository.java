package kr.co.sunnyvale.sunny.repository.hibernate;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.Approval;
import kr.co.sunnyvale.sunny.domain.Content;
import kr.co.sunnyvale.sunny.domain.SimpleContent;
import kr.co.sunnyvale.sunny.domain.SmallGroupApproval;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.UserSmallGroupAccess;
import kr.co.sunnyvale.sunny.domain.dto.ContentDTO;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

@Repository(value = "contentRepository")
public class ChildContentRepository extends ContentRepository<Content> {

	public ChildContentRepository() {
	}
	
	@SuppressWarnings("unchecked")
	private List<Long> getUserJoinedSmallGroupIds( User user ){
		Criteria userSmallGroupAccessCriteria = getCurrentSession().createCriteria(UserSmallGroupAccess.class);
		userSmallGroupAccessCriteria.add(Restrictions.eq("user", user));
		userSmallGroupAccessCriteria.setProjection(Projections.projectionList().add(Projections.property("smallGroup.id"), "smallGroupId"));
		return userSmallGroupAccessCriteria.list();
	}
	
	public List<ContentDTO> fetchApprovals(Sunny sunny, String menu, Long smallGroupId,
			Boolean isWantChildren, User authUser, String[] queries, Stream stream) {

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
//		criteria.add(Restrictions.eq("this.type", Content.TYPE_APPROVAL));

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
		
		
		contentDtoCriteria(criteria, null);
		return getPagedDTOList(criteria, stream);
	
	}
	private List<ContentDTO> getPagedDTOList(Criteria criteria, Stream  stream) {

		if( stream == null )
    		return  criteria.list();
    	
	   	if( stream.getSize() != null)
	   		criteria.setMaxResults(stream.getSize());
	   	
	   	if( stream.getGreaterThan() == null)
	   		return criteria.list();
	   	
    	if( stream.getGreaterThan() == true ){
    		criteria.add( Restrictions.gt(stream.getBaseColumn(), stream.getBaseData()));
    	}else{
    		criteria.add( Restrictions.lt(stream.getBaseColumn(), stream.getBaseData()));
    	}
    	
    	return criteria.list();
    	
	}

	public List<Long> getPagedIdList(Criteria criteria, Stream stream){
    	if( stream == null )
    		return  criteria.list();
    	
	   	if( stream.getSize() != null)
	   		criteria.setMaxResults(stream.getSize());
	   	
	   	if( stream.getGreaterThan() == null)
	   		return criteria.list();
	   	
    	if( stream.getGreaterThan() == true ){
    		criteria.add( Restrictions.gt(stream.getBaseColumn(), stream.getBaseData()));
    	}else{
    		criteria.add( Restrictions.lt(stream.getBaseColumn(), stream.getBaseData()));
    	}
    	
    	return criteria.list();
	}
	
	
	private void contentDtoCriteria(Criteria criteria, String prefix) {
		
		if( prefix == null )
			prefix = "";
		
		criteria.createAlias(prefix + "user", "userAlias");
		criteria.createAlias(prefix + "postUser", "postUserAlias", Criteria.LEFT_JOIN);
		criteria.createAlias(prefix + "dynamic", "dynamicAlias", Criteria.LEFT_JOIN);
		criteria.createAlias(prefix + "smallGroup", "smallGroupAlias", Criteria.LEFT_JOIN);

//		criteria.addOrder(Order.desc(prefix + "thread"));
//		criteria.addOrder(Order.asc(prefix + "threadSeq"));
		criteria.setProjection(	Projections.projectionList()
				
				// Content
				.add(Projections.property(prefix + "id"), "id")
				.add(Projections.property(prefix + "title"),"title")
				.add(Projections.property(prefix + "taggedTextPrev"),"taggedTextPrev")
				.add(Projections.property(prefix + "taggedTextNext"),"taggedTextNext")
				.add(Projections.property(prefix + "returnCount"),"returnCount")
				.add(Projections.property(prefix + "mediaCount"), "mediaCount")
				.add(Projections.property(prefix + "ipAddress"), "ipAddress")
				.add(Projections.property(prefix + "createDate"), "createDate")
				.add(Projections.property(prefix + "updateDate"), "updateDate")
				.add(Projections.property(prefix + "smallGroupContentAccessesAlias.createDate"), "arriveDate")
				.add(Projections.property(prefix + "type"), "type")
				.add(Projections.property(prefix + "permissionType"), "permissionType")
				.add(Projections.property(prefix + "smallGroupAlias.id"), "smallGroupId")
				.add(Projections.property(prefix + "smallGroupAlias.name"), "smallGroupName")
				.add(Projections.property(prefix + "smallGroupAlias.type"), "smallGroupType")
				.add(Projections.property("dynamicAlias.replyCount"), "replyCount")
				.add(Projections.property("dynamicAlias.feelScore"), "feelScore")
				.add(Projections.property("dynamicAlias.feelCount"), "feelCount")
			
				// User
				.add(Projections.property("userAlias.id"), "userId")
				.add(Projections.property("userAlias.name"), "userName")
				.add(Projections.property("userAlias.profilePic"), "userProfilePic")

				.add(Projections.property("postUserAlias.id"), "postUserId")
				.add(Projections.property("postUserAlias.name"), "postUserName")
				.add(Projections.property("postUserAlias.profilePic"), "postUserProfilePic")
				
		);

		criteria.setResultTransformer(new AliasToBeanResultTransformer(ContentDTO.class));
	}
}
