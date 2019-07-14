//package kr.co.sunnyvale.sunny.repository.hibernate;
//
//import java.util.List;
//
//import kr.co.sunnyvale.sunny.domain.Site;
//import kr.co.sunnyvale.sunny.domain.SiteUser;
//import kr.co.sunnyvale.sunny.domain.extend.Page;
//import kr.co.sunnyvale.sunny.domain.extend.Sunny;
//import kr.co.sunnyvale.sunny.util.CriteriaUtils;
//
//import org.hibernate.Criteria;
//import org.hibernate.criterion.Criterion;
//import org.hibernate.criterion.MatchMode;
//import org.hibernate.criterion.Order;
//import org.hibernate.criterion.Restrictions;
//import org.springframework.stereotype.Repository;
//
//@Repository(value = "siteUserRepository")
//public class SiteUserRepository extends HibernateGenericRepository<SiteUser>{
//	public SiteUserRepository(){
//
//	}
//
//	public SiteUser findBySiteAndUser(Long siteId, Long userId) {
//		Criteria criteria = getCriteria();
//		
//		criteria.add(Restrictions.eq("site.id", siteId));
//		criteria.add(Restrictions.eq("user.id", userId));
//		
//		criteria.setMaxResults(1);
//		return (SiteUser) criteria.uniqueResult();
//		
//	}
//
//	public SiteUser getUserJoinedSite(Long id) {
//		Criteria criteria = getCriteria();
//		
//		criteria.add(Restrictions.eq("user.id", id));
//		criteria.setMaxResults(1);
//		
//		return (SiteUser) criteria.uniqueResult();
//	}
//
//	public Page<SiteUser> getUserList(Sunny sunny, List<Long> smallGroupIds,
//			Integer queryType, String queryName, Integer[] status,
//			String range, String ordering, Long excludeUserId,
//			Boolean onlyAdmin, Integer page, int pageSize) {
//		
//		Criteria criteria = getCriteria();
//		
//		
//		criteria.add(Restrictions.eq("deleteFlag", false));
//		criteria.add(Restrictions.eq("site", sunny.getSite()));
//		criteria.createAlias("user", "userAlias");
//		
//		if( onlyAdmin != null && onlyAdmin == true ){
//			//criteria.createAlias("admin", "adminAlias", Criteria.INNER_JOIN);
//			criteria.add(Restrictions.eq("admin", true));
//		}
//		
//		
//		
//		if( excludeUserId != null ){
//			criteria.add(Restrictions.ne("userAlias.id", excludeUserId));
//		}
//		
//		
//		if( status != null && status.length > 0 ){
//			
//			if( status.length == 1 ){
//				criteria.add(Restrictions.eq("status", status[0]));	
//			}else if( status.length == 2 ){
//				criteria.add(Restrictions.or(
//						Restrictions.eq("status", status[0]), 
//						Restrictions.eq("status", status[1])
//						));
//			}else if( status.length == 3 ){
//				criteria.add(Restrictions.or(
//						Restrictions.or(
//							Restrictions.eq("status", status[0]), 
//							Restrictions.eq("status", status[1])
//						), Restrictions.eq("status", status[2])
//						));
//			}
//		}
//		
//		// 이건 어디에 쓰는 것인고? 
////		if( smallGroupIds != null && smallGroupIds.size() > 0 ){
////			criteria.createAlias("smallGroups", "smallGroupsAlias");
////			criteria.add(Restrictions.in("smallGroupsAlias.id", smallGroupIds));
////		}
//		
//		
//		if( queryName != null ){
//			if( queryType == null )
//				queryType = 0;
//			
//			if( queryType == 0 ){
//				// 자음 검색
//				
//				
//				Criterion expression = CriteriaUtils.returnRestrictionAfterCheckInitial("userAlias.name", queryName );
//				
//				criteria.add(expression);
//			}else if( queryType == 1){
//				criteria.add(Restrictions.like("email", queryName, MatchMode.ANYWHERE));
//			}else if( queryType == 2){
//				criteria.add(
//					Restrictions.or(
//							CriteriaUtils.returnRestrictionAfterCheckInitial("jobTitle1", queryName ),
//						Restrictions.or(
//								CriteriaUtils.returnRestrictionAfterCheckInitial("jobTitle2", queryName ),
//								CriteriaUtils.returnRestrictionAfterCheckInitial("jobTitle3", queryName )
//						)
//					));
//			}
//		}
//
//		if( ordering != null && !ordering.isEmpty() ){
//			criteria.addOrder(Order.asc(ordering));
//		}
//		
//		return getPagedList(criteria, page, pageSize);
//	}
//
//	public SiteUser findBySiteAndEmail(Site site, String email) {
//
//		Criteria criteria = getCriteria();
//		
//		criteria.add(Restrictions.eq("deleteFlag", false));
//		criteria.add(Restrictions.eq("site", site));
//		
//		criteria.createAlias("user", "userAlias");
//		criteria.createAlias("userAlias.emails", "emailAlias");
//		
//		criteria.add(Restrictions.eq("emailAlias.email", email));
//		
//		criteria.setMaxResults(1);
//		return (SiteUser) criteria.uniqueResult();
//	}
//}