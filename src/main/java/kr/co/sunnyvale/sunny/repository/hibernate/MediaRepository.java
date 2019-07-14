package kr.co.sunnyvale.sunny.repository.hibernate;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.Content;
import kr.co.sunnyvale.sunny.domain.Draft;
import kr.co.sunnyvale.sunny.domain.Media;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.UserSmallGroupAccess;
import kr.co.sunnyvale.sunny.domain.extend.MediaType;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;

import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
/**
 * {Hibernate}
 *	
 *<p>
 *	Media를 관리하는 Repository	
 *
 */
@Repository(value = "mediaRepository")
public class MediaRepository extends ContentRepository<Media>{
	public MediaRepository(){

	}

	@SuppressWarnings("unchecked")
	public List<Media> getMedies(Long fromContentId, Stream stream){
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("content.id", fromContentId));
		criteria.add(Restrictions.eq("deleteFlag", false));
		criteria.addOrder(Order.asc("id"));
		return getPagedList(criteria, stream);
	}

	public List<Media> getFromDraft(Draft draft) {
		Criteria criteria = getCriteria();
		criteria.add( Restrictions.eq("draft", draft) );
		criteria.add(Restrictions.eq("deleteFlag", false));
		return criteria.list();
	}

	public List<Media> getFromContent(Content content) {
		Criteria criteria = getCriteria();
		criteria.add( Restrictions.eq("content", content) );
		criteria.add(Restrictions.eq("deleteFlag", false));
		return criteria.list();
	}

	public Page<Media> getPagedResult(Sunny sunny, Long smallGroupId,
			User user, String query, Integer isMy, MediaType[] types, Integer page, int pageSize) {

		
		
		List<Long> userJoinedSmallGroupIds = getUserJoinedSmallGroupIds(user);

		if( userJoinedSmallGroupIds == null || userJoinedSmallGroupIds.size() == 0  )
			return null;
		/*
		 * smallGroup 들 뽑아내기 끝
		 */

		Criteria criteria = getCriteria();

		criteria.createAlias("content", "contentAlias");
		
		if( isMy != null && isMy == 1 ){
			criteria.add(Restrictions.eq("contentAlias.user", user));
			criteria.addOrder(Order.desc("contentAlias.createDate"));
		}else{
			criteria.createAlias("contentAlias.smallGroupContentAccesses", "smallGroupContentAccessesAlias");

			criteria.add(Restrictions.in("smallGroupContentAccessesAlias.smallGroup.id", userJoinedSmallGroupIds));

			criteria.addOrder(Order.desc("smallGroupContentAccessesAlias.createDate"));
		}
		
		if( types != null ){
			Disjunction disjunction = Restrictions.disjunction();
			for( MediaType mediaType : types ){
				disjunction.add(Restrictions.eq("mediaType", mediaType.getType()));	
			}
			criteria.add(disjunction);
		}

		criteria.add(Restrictions.eq("contentAlias.site", sunny.getSite()));
		criteria.add(Restrictions.eq("contentAlias.deleteFlag", false));
		if( query != null ){
			criteria.add(Restrictions.like("fileName", query, MatchMode.ANYWHERE));	
		}


		criteria.setProjection(Projections.distinct(Projections.property("id")));
		
		return getPagedList(criteria, page, pageSize);
	}

	
	@SuppressWarnings("unchecked")
	private List<Long> getUserJoinedSmallGroupIds( User user ){
		Criteria userSmallGroupAccessCriteria = getCurrentSession().createCriteria(UserSmallGroupAccess.class);
		userSmallGroupAccessCriteria.add(Restrictions.eq("user", user));
		userSmallGroupAccessCriteria.setProjection(Projections.projectionList().add(Projections.property("smallGroup.id"), "smallGroupId"));
		return userSmallGroupAccessCriteria.list();
	}

//	public int updateMediaesBelongToContent(Content content, List<Long> mediaIds) {
//		
//		/**
//		 * 이상하게 안된다.. HT_ 요 테이블을 하나 임시로 만드는 것 같은데 얘땜에 Column 'id' in where clause is ambiguous 이런 오류남
//		 */
////		String hqlUpdate = "update Media media set media.content = :content where media.id in (:mediaIds)";
////		int updatedEntities = getCurrentSession().createQuery( hqlUpdate )
////		        .setParameter("content", content)
////		        .setParameterList("mediaIds", mediaIds)
////		        .executeUpdate();
////		getCurrentSession().clear();
//		
//		Session session = getCurrentSession();
//		int mediaCount = 0;
//		for( Long mediaId : mediaIds ){
//			Media media = select(mediaId);
//			media.setContent(content);
//			session.update(media);
//			mediaCount++;
//		}
//		session.flush();
//		session.clear();
//		return mediaCount;
//		
//	}
	
//	/**
//	 * deleteFlag가 true일 경우 실제로 삭제, 그게 아니라면 삭제 플래그만 삭제.
//	 * @param mediaId
//	 * @param realDeleteFlag
//	 * @return
//	 */
//	public boolean deleteMedia(Long mediaId, boolean realDeleteFlag){
//		if(realDeleteFlag)
//	}
//	
	
	
}
