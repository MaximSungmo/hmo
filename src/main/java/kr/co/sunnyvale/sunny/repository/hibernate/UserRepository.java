package kr.co.sunnyvale.sunny.repository.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import kr.co.sunnyvale.sunny.domain.Site;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.UserSmallGroupAccess;
import kr.co.sunnyvale.sunny.domain.dto.UserDTO;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.domain.post.RelationDTO;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;
/**
 * {Hibernate}
 *	
 *<p>
 *	유저 정보를 관리하는 Repository
 *
 * @author mook
 *
 *	
 *	
 *
 */
@Repository(value = "userRepository")
public class UserRepository extends HibernateGenericRepository<User>{
	public UserRepository(){

	}
	public List<User> findCandidatesToFellow(String userId, String input, Stream stream) {
		Criteria criteria = getCriteria();
		Criteria followCriteria = criteria.createCriteria( "followings" );
		followCriteria.add( Restrictions.eq( "id", userId) );
		Disjunction disjunction = Restrictions.disjunction();
		disjunction.add(Restrictions.like("name", input, MatchMode.ANYWHERE));
		disjunction.add(Restrictions.like("email", input, MatchMode.ANYWHERE));
		criteria.add(disjunction);
		return getPagedList(criteria, stream);
	}

	public List<String> getAllIds(Stream stream) {
		Criteria criteria = getCurrentSession().createCriteria(entityClass);
		criteria.add(Restrictions.eq("deleteFlag", false));
		criteria.setProjection( Projections.projectionList().add( Projections.property("id") ) );
		
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

	public User getNextUserById(String beforeId) {
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("deleteFlag", false));
		criteria.add( Restrictions.gt( "id", beforeId ) );
		criteria.setMaxResults(1);
		return (User) criteria.uniqueResult();
	}

	public Number getAllUserCount() {
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("deleteFlag", false));
		criteria.setProjection(Projections.rowCount());		
		return (Number)criteria.uniqueResult();
	}

	public User findByEmail(String email) {
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("deleteFlag", false));
		criteria.add(Restrictions.eq("email", email));
		return (User) criteria.uniqueResult();
	}
	
	
	public boolean existsEmail(String email) {
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("deleteFlag", false));
		criteria.add(Restrictions.eq("email", email));
		criteria.setMaxResults(1);
		
		return criteria.uniqueResult() == null ? false : true;
	}
	
	public Page<User> getUserList(Long siteId, String searchName,  Integer pageNum, int pageSize) {
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("deleteFlag", false));
		criteria.add(Restrictions.eq("site.id", siteId));
		if( searchName != null ){
			criteria.add(Restrictions.like("name", searchName, MatchMode.ANYWHERE));
		}
		return getPagedList(criteria, pageNum, pageSize);
	}
	
	public Page<User> getPagedNotInSmallGroupUserList(Sunny sunny,
			Long smallGroupId, Long userId, 
			String queryName, Integer page, int pageSize) {
		
		Criteria userSmallGroupAccesscriteria = getCurrentSession().createCriteria(UserSmallGroupAccess.class);
		
		// 가입된 사용자 가져옴
		userSmallGroupAccesscriteria.add(Restrictions.eq("smallGroup.id", smallGroupId));
		userSmallGroupAccesscriteria.setProjection(Projections.property("user.id"));
		List <Long> joinedUserIds = userSmallGroupAccesscriteria.list();
		
		
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("deleteFlag", false));
		criteria.add(Restrictions.eq("site", sunny.getSite()));
		
		if( joinedUserIds != null &&  joinedUserIds.size() > 0 ){
			// 가입된 사용자를 제외한 사용자들
			criteria.add(Restrictions.not(Restrictions.in("id", joinedUserIds)));
		}

		
		if( queryName != null ){
				
			Criterion expression = returnRestrictionAfterCheckInitial("name", queryName );
			
			criteria.add(expression);
		}
		criteria.addOrder(Order.asc("name"));

		return getPagedList(criteria, page, pageSize);
		
	}
	public Page<User> getUserList(Sunny sunny, List<Long> smallGroupIds,
			Integer queryType, String queryName, Integer[] status, String range, String ordering, Long excludeUserId, Boolean onlyAdmin, Integer page,
			int pageSize) {
	
		Criteria criteria = getCriteria();
		
		
		applyRange(criteria, range);
		
		criteria.add(Restrictions.eq("deleteFlag", false));
		criteria.add(Restrictions.eq("site", sunny.getSite()));
		
		
		if( onlyAdmin != null && onlyAdmin == true ){
			criteria.add(Restrictions.eq("isAdmin", true));
			//criteria.createAlias("admin", "adminAlias", Criteria.INNER_JOIN);
		}
		
		if( excludeUserId != null ){
			criteria.add(Restrictions.ne("id", excludeUserId));
		}
		
		
		if( status != null && status.length > 0 ){
			
			if( status.length == 1 ){
				criteria.add(Restrictions.eq("status", status[0]));	
			}else if( status.length == 2 ){
				criteria.add(Restrictions.or(
						Restrictions.eq("status", status[0]), 
						Restrictions.eq("status", status[1])
						));
			}else if( status.length == 3 ){
				criteria.add(Restrictions.or(
						Restrictions.or(
							Restrictions.eq("status", status[0]), 
							Restrictions.eq("status", status[1])
						), Restrictions.eq("status", status[2])
						));
			}
			
			
		}
		
		if( smallGroupIds != null && smallGroupIds.size() > 0 ){
			criteria.createAlias("smallGroups", "smallGroupsAlias");
			criteria.add(Restrictions.in("smallGroupsAlias.id", smallGroupIds));
		}
		
		if( queryName != null ){
			if( queryType == null )
				queryType = 0;
			
			if( queryType == 0 ){
				// 자음 검색
				
				
				Criterion expression = returnRestrictionAfterCheckInitial("name", queryName );
				
				criteria.add(expression);
			}else if( queryType == 1){
				criteria.add(Restrictions.like("email", queryName, MatchMode.ANYWHERE));
			}else if( queryType == 2){
				criteria.add(
					Restrictions.or(
						returnRestrictionAfterCheckInitial("jobTitle1", queryName ),
						Restrictions.or(
							returnRestrictionAfterCheckInitial("jobTitle2", queryName ),
							returnRestrictionAfterCheckInitial("jobTitle3", queryName )
						)
					));
			}
		}

		if( ordering == null ){
			criteria.addOrder(Order.asc("name"));
		}else if( ordering != null && !ordering.isEmpty() ){
			criteria.addOrder(Order.asc(ordering));
		}
		
		return getPagedList(criteria, page, pageSize);
	}
	
	private Criterion returnRestrictionAfterCheckInitial( String column, String queryName ){
		
		if( queryName.length() != 1 ){
			return Restrictions.like(column, queryName, MatchMode.ANYWHERE);
		}
		String start = null;
		String end = null;
		
		switch(queryName){
		case "ㄱ":
			start = "가";
			end = "나";
			break;
		case "ㄴ":
			start = "나";
			end = "다";
			break;
		case "ㄷ":
			start = "다";
			end = "라";
			break;
		case "ㄹ":
			start = "라";
			end = "마";
			break;
		case "ㅁ":
			start = "마";
			end = "바";
			break;
		case "ㅂ":
			start = "바";
			end = "사";
			break;
		case "ㅅ":
			start = "사";
			end = "아";
			break;
		case "ㅇ":
			start = "아";
			end = "자";
			break;
		case "ㅈ":
			start = "자";
			end = "차";
			break;
		case "ㅊ":
			start = "차";
			end = "카";
			break;
		case "ㅋ":
			start = "카";
			end = "타";
			break;
		case "ㅌ":
			start = "타";
			end = "파";
			break;
		case "ㅍ":
			start = "파";
			end = "하";
			break;
		case "ㅎ":
			start = "하";
			end = "힣";
			break;
		default:
			break;
		}
		
		
		if( start != null && end != null ){
			return Restrictions.and(
					Restrictions.ge(column, start),
					Restrictions.lt(column, end)
					);
		}else{
			return Restrictions.like(column, queryName, MatchMode.ANYWHERE);
		}
		
	}
	
	private void applyRange(Criteria criteria, String range) {
		if( range == null )
			return; 
		
		boolean isKorean = false;
		String start = null;
		String end = null;
		
		switch(range){
		case "ㄱ":
			start = "가";
			end = "나";
			isKorean = true;
			break;
		case "ㄴ":
			start = "나";
			end = "다";
			isKorean = true;
			break;
		case "ㄷ":
			start = "다";
			end = "라";
			isKorean = true;
			break;
		case "ㄹ":
			start = "라";
			end = "마";
			isKorean = true;
			break;
		case "ㅁ":
			start = "마";
			end = "바";
			isKorean = true;
			break;
		case "ㅂ":
			start = "바";
			end = "사";
			isKorean = true;
			break;
		case "ㅅ":
			start = "사";
			end = "아";
			isKorean = true;
			break;
		case "ㅇ":
			start = "아";
			end = "자";
			isKorean = true;
			break;
		case "ㅈ":
			start = "자";
			end = "차";
			isKorean = true;
			break;
		case "ㅊ":
			start = "차";
			end = "카";
			isKorean = true;
			break;
		case "ㅋ":
			start = "카";
			end = "타";
			isKorean = true;
			break;
		case "ㅌ":
			start = "타";
			end = "파";
			isKorean = true;
			break;
		case "ㅍ":
			start = "파";
			end = "하";
			isKorean = true;
			break;
		case "ㅎ":
			start = "하";
			end = "힣";
			isKorean = true;
			break;
		default:
			isKorean = false;
			break;
		}
		
		if( isKorean ){
			criteria.add(Restrictions.ge("name", start));
			criteria.add(Restrictions.lt("name", end));
		}else{
			criteria.add(Restrictions.like("name", range, MatchMode.START));
		}
		
	}
	public Page<User> getUserList( List<Long>smallGroupIdList, String searchName, Integer pageNum, int pageSize) {
		// smallGroupIdList에 해당하는 부서들에 포함되어 있는 사용자 목록을 반환한다.  SmallGroup과 User간에 관계 도메인이 아직 없는듯?
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("deleteFlag", false));
		criteria.createAlias("smallGroups", "smallGroupsAlias");
		criteria.add(Restrictions.in("smallGroupsAlias.id", smallGroupIdList));
		if( searchName != null ){
			criteria.add(Restrictions.like("name", searchName, MatchMode.ANYWHERE));
		}		
		return getPagedList(criteria, pageNum, pageSize);
	}

	
	public int getUserCount(Long siteId, String searchName) {
		Criteria criteria = getCriteria();
	
		criteria.add(Restrictions.eq("site.id", siteId));
		criteria.setProjection(Projections.rowCount());
		Object rowCount = criteria.uniqueResult();
		if( rowCount == null )
			return 0;

		return ((Number) rowCount).intValue();
	}

	public Number getUserCount(List<Long>smallGroupIdList, String searchName) {
		// smallGroupIdList에 해당하는 부서들에 포함되어 있는 사용자의 수를 반환한다. 
		return 0;
	}

	public void deleteUser(Long userId, boolean realDelete) {
		if(realDelete){
			delete(select(userId));
		}else{
			User user = select(userId);
			// user의 deleteFlag값을 삭제로 바꾼다.
		}
	}
	
	public List<UserDTO> findMatchUsersByName(Site site, String key, Stream stream) {
    	Criteria criteria = getCriteria();
    	criteria.add(Restrictions.eq("deleteFlag", false));
    	criteria.add( Restrictions.like("this.name", key, MatchMode.ANYWHERE) );
    	criteria.add(Restrictions.eq("this.site", site));
    	
    	criteria.setProjection( Projections.projectionList()
    			.add(Projections.property("id"), "id")
    			.add(Projections.property("profilePic"), "profilePic")
    			.add(Projections.property("name"), "name")
    	);
    	
    	criteria.setResultTransformer(new AliasToBeanResultTransformer(UserDTO.class));
    	return getPagedDTOList(criteria, stream);
	}
	public List<UserDTO> getPagedDTOList(Criteria criteria, Stream stream){
		criteria.add(Restrictions.eq("deleteFlag", false));
		
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
	public List<RelationDTO> getUserList(Long siteId, Long userId, String searchName,
			Stream stream) {
		
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("site.id", siteId));
		criteria.add(Restrictions.ne("id", userId));
		
		if( stream != null && stream.getBaseColumn() != null && stream.getBaseColumn().equals("name") ){
			criteria.addOrder(Order.asc("name"));	
			
		}else{
			criteria.addOrder(Order.desc("id"));
		}
		criteria.setProjection(			
				Projections.projectionList()
				//.add(Projections.property("id"), "id")
				//.add(Projections.property("type"), "type")
				
				.add(Projections.property("id"), "userId")
				.add(Projections.property("name"), "userName")
				//.add(Projections.property("userAlias.statusMessage"), "userStatusMessage")
				.add(Projections.property("profilePic"), "userProfilePic")
				//.add(Projections.property("infoAlias.friendCount"), "userFriendCount")

			);
		criteria.setResultTransformer(new AliasToBeanResultTransformer(RelationDTO.class));
		
		return getRelationPagedList(criteria, stream);
		
		
	}
	

	public Page<User> getUserList(Long siteId, Long userId,
			String queryName, Integer[] status, Integer page, int pageSize) {
	
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("site.id", siteId));
		criteria.add(Restrictions.ne("id", userId));
		criteria.addOrder(Order.asc("name"));	
		if( status != null && status.length > 0 ){
			
			if( status.length == 1 ){
				criteria.add(Restrictions.eq("status", status[0]));	
			}else if( status.length == 2 ){
				criteria.add(Restrictions.or(
						Restrictions.eq("status", status[0]), 
						Restrictions.eq("status", status[1])
						));
			}else if( status.length == 3 ){
				criteria.add(Restrictions.or(
						Restrictions.or(
							Restrictions.eq("status", status[0]), 
							Restrictions.eq("status", status[1])
						), Restrictions.eq("status", status[2])
						));
			}
			
			
		}
		
	
		criteria.setResultTransformer(new AliasToBeanResultTransformer(RelationDTO.class));
		return getPagedList( criteria, page, pageSize );
	}



	public List<User> getSimpleUserList(Long siteId, Long authUserId,
			Long lineUserId, String queryName, Boolean top, Integer size) {
		
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("site.id", siteId));
		criteria.add(Restrictions.ne("id", authUserId));
		criteria.addOrder(Order.asc("name"));
		
		criteria.add(Restrictions.ne("status", User.STATUS_LEAVE));
		
		if( lineUserId != null ){
			Criteria nameCriteria = getCriteria();
			nameCriteria.add(Restrictions.eq("id", lineUserId));
			nameCriteria.setProjection(Projections.property("name"));
			String name = (String) nameCriteria.uniqueResult();
			
			if( top == true ){
				criteria.add(Restrictions.and(
								Restrictions.ne("id", lineUserId),
								Restrictions.le("name", name)));
			}else{
				criteria.add(Restrictions.and(
						Restrictions.ne("id", lineUserId),
						Restrictions.ge("name", name)));
			}
			
		}
		
		if( queryName != null ){
			Disjunction disjunction = Restrictions.disjunction();
			disjunction.add(Restrictions.like("name", queryName, MatchMode.ANYWHERE));
			disjunction.add(Restrictions.like("email", queryName, MatchMode.ANYWHERE));
			criteria.add(disjunction);
		}
		

		if( size != null)
	   		criteria.setMaxResults(size);
		
		return criteria.list();
	}
	
	public List<User> getSimpleUserList(Long siteId, Long authUserId, String queryName,
			Stream stream) {

		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("site.id", siteId));
		criteria.add(Restrictions.ne("id", authUserId));
		criteria.addOrder(Order.asc("name"));
		
		criteria.add(Restrictions.ne("status", User.STATUS_LEAVE));
		
		if( queryName != null ){
			Disjunction disjunction = Restrictions.disjunction();
			disjunction.add(Restrictions.like("name", queryName, MatchMode.ANYWHERE));
			disjunction.add(Restrictions.like("email", queryName, MatchMode.ANYWHERE));
			criteria.add(disjunction);
		}
		
		
		return getPagedList(criteria, stream);
		
	}
	
	
	

	public Page<User> getFavoriteUsers(Sunny sunny, User user, Integer page,
			int pageSize) {

		Criteria criteria = getCriteria();

		criteria.add(Restrictions.eq("deleteFlag", false));
		criteria.add(Restrictions.eq("site", sunny.getSite()));
		criteria.createAlias("favoritedUsers", "favoritedUsersAlias");
		criteria.add(Restrictions.eq("favoritedUsersAlias.favoriter", user));
		
		return getPagedList(criteria, page, pageSize);
	}
	
	private List<RelationDTO> getRelationPagedList(Criteria criteria, Stream stream){
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
	 public Page<RelationDTO> getRelationPagedList(Criteria criteria, Integer pageNumber, Integer pageSize){
	    	//System.out.println("pageNumber : " + pageNumber);
	    	if( pageNumber == null ) {
	    		pageNumber = new Integer(1);
	    	}
	    	if( pageNumber < 1){
	    		pageNumber = 1; 
	    	}
			Criteria countCriteria = criteria;
			Criteria contentCriteria = criteria;
			
			countCriteria.setProjection(Projections.distinct(Projections.rowCount()));
			Long count = 0L;
			try{
				count = (Long)countCriteria.uniqueResult();
			}catch(Exception ex){}
			//System.out.println("count : " + count);
			
			contentCriteria.setProjection(null);
			
			contentCriteria.setProjection(null);
			contentCriteria.setMaxResults(10000);
			contentCriteria.setFirstResult(0);
		    
			return new Page<RelationDTO>(contentCriteria.list(), pageNumber, pageSize, count);
	    	
	    }
	
	public List<User> findDefaultSiteUsers(Long siteId) {
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("defaultSite.id", siteId));
		return criteria.list();
	}
	public boolean isJoined(Long siteId, Long userId) {
		
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("site.id", siteId));
		criteria.add(Restrictions.eq("id", userId));
		criteria.setMaxResults(1);
		
		Object obj = criteria.uniqueResult();
		
		if( obj == null )
			return false;
		else
			return true; 
	}
	
	
	public List<Long> getSiteAdminIds(Long siteId) {
		
		Criteria criteria = getCriteria();
				
		criteria.add(Restrictions.eq("site.id", siteId));
		criteria.add(Restrictions.eq("isAdmin", true));
		criteria.setProjection(Projections.property("id"));
		
		
		return criteria.list();
	}
	public List<Long> findAllJoinedUsers(Long siteId, Long exceptUserId) {
		
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("site.id", siteId));
		if( exceptUserId != null )
			criteria.add(Restrictions.ne("id", exceptUserId));
		
//		criteria.add(Restrictions.ne("status", User.STATUS_LEAVE));
		criteria.setProjection(Projections.property("id"));
		return criteria.list();
	}
	public Page<User> getAllFromSuper(Integer page, int pageSize) {

		Criteria criteria = getCriteria();
		criteria.addOrder(Order.desc("createDate"));
		return getPagedList(criteria, page, pageSize);
	}
	
}
