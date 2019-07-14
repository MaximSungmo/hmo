package kr.co.sunnyvale.sunny.repository.hibernate;


import java.util.List;

import kr.co.sunnyvale.sunny.domain.Site;
import kr.co.sunnyvale.sunny.domain.SmallGroup;
import kr.co.sunnyvale.sunny.domain.SmallGroupContentAccess;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.UserSmallGroupAccess;
import kr.co.sunnyvale.sunny.domain.dto.ContentDTO;
import kr.co.sunnyvale.sunny.domain.dto.SmallGroupDTO;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.SmallGroupIdName;
import kr.co.sunnyvale.sunny.domain.extend.SmallGroupTree;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.exception.SimpleSunnyException;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

@Repository(value = "smallGroupRepository")
public class SmallGroupRepository extends HibernateGenericRepository<SmallGroup>{
	public SmallGroupRepository(){

	}

	@SuppressWarnings("unchecked")
	public boolean isSmallGroupAccessIncludeUsersGroup(SmallGroup smallGroup,
			List<Long> usersAccessGroupIds) {
		Criteria criteria = getCriteria();
		
		criteria.createAlias("accessGroupPermissions", "accessGroupPermissionsAlias");
		criteria.add(Restrictions.eq("id", smallGroup.getId()));
		
		criteria.add(Restrictions.in("accessGroupPermissionsAlias.accessGroup.id", usersAccessGroupIds));
		
		criteria.setMaxResults(2);
		
		
		List<Object> list = criteria.list();
		
		if( list != null && list.size() > 0 ){
			return true;
		}
		return false;
	}

	public List<SmallGroup> getSmallGroupList(
			Site site,
			SmallGroup parentSmallGroup, 
			User user,
			Integer type, 
			Stream stream) {
		
		Criteria criteria = getSmallGroups(site, parentSmallGroup, user, type, null, null, null);
		
		return getPagedList(criteria, stream);
	}
	
	public Page<SmallGroup> getSmallGroupPage(Site site,
			SmallGroup parentSmallGroup, User user, Integer type,
			String queryName, String ordering, Boolean desc, Integer pageNum, Integer pageSize) {

		
		Criteria criteria = getSmallGroups(site, parentSmallGroup, user, type, queryName, ordering, desc );

		
//		criteria.addOrder(Order.asc("thread"));
//		criteria.addOrder(Order.asc("threadSeq"));
		
		criteria.addOrder(Order.asc("name"));
				
		Page<SmallGroup> page = getPagedList(criteria, pageNum, pageSize );
		page.setUniqueList();
		return page;
		
	}
	
	private Criteria getSmallGroups(Site site, SmallGroup parentSmallGroup,
			User user, Integer type, String queryName, String ordering, Boolean desc) {
		Criteria criteria = getCriteria();
		//criteria.setFetchMode("children", FetchMode.JOIN);
		criteria.add(Restrictions.eq("site", site));
		
		if( user != null ){
			DetachedCriteria smallGroupUserAccessCriteria = DetachedCriteria.forClass(UserSmallGroupAccess.class, "userSmallGroupAccess");
			smallGroupUserAccessCriteria.add(Restrictions.eq("user", user));
			smallGroupUserAccessCriteria.setProjection(Projections.projectionList().add(Projections.property("smallGroup.id")));
			criteria.add(Subqueries.propertyIn("this.id",smallGroupUserAccessCriteria));
//			criteria.createAlias("smallGroupUsers", "smallGroupUsersAlias", Criteria.LEFT_JOIN);
//			criteria.add(Restrictions.eq("smallGroupUsersAlias.user", user));
		}
		
		if( type != null ){
			criteria.add(Restrictions.eq("type", type));
		}
		// 부모 그룹이 존재하면 부모 그룹 밑에있는 자식들만 가져온다. 
		if( parentSmallGroup != null ){
			criteria.add(Restrictions.eq("parent", parentSmallGroup));
		}
		
		if( ordering == null ){
			//criteria.addOrder( Order.desc("updateDate") );
			criteria.addOrder(Order.asc("name"));
		}else if( desc == true ){
			criteria.addOrder( Order.desc(ordering) );
		}else{
			criteria.addOrder( Order.asc(ordering) );
		}
		
		
		if( queryName != null ){
			Criterion expression = returnRestrictionAfterCheckInitial("name", queryName );
			criteria.add(expression);
		}
		
		
		return criteria;
	}

	


	public Page<SmallGroup> getContactSmallGroupPage(Site site, User user,
			Integer page, int pageSize) {
		Criteria criteria = getCriteria();
		//criteria.setFetchMode("children", FetchMode.JOIN);
		criteria.add(Restrictions.eq("site", site));
		
		if( user != null ){
			criteria.createAlias("smallGroupUsers", "smallGroupUsersAlias", Criteria.LEFT_JOIN);
			criteria.add(Restrictions.eq("smallGroupUsersAlias.user", user));
		}
		
		criteria.add(Restrictions.or(
							Restrictions.eq("type", SmallGroup.TYPE_DEPARTMENT),
							Restrictions.or(
									Restrictions.eq("type", SmallGroup.TYPE_GROUP),
									Restrictions.eq("type", SmallGroup.TYPE_DEPARTMENT)
									)
						)
					);
		criteria.addOrder(Order.asc("name"));
		// 부모 그룹이 존재하면 부모 그룹 밑에있는 자식들만 가져온다. 
		return getPagedList(criteria, page, pageSize);
	
	}
	
	
	/**
	 * smallGroupId에 해당하는 스몰그룹의 자식들중 가장 큰 Thread_seq값을 구한다.
	 * 자식이 없을 경우 -1을 반환한다.
	 * @param smallGroupId
	 * @param smallGroupType
	 * @return
	 */
	public Number getMaxThreadSeq(SmallGroup parentSmallGroup,Integer smallGroupType){
		// smallGroupId의 자식 SmallGroup중에서 가장 Max값이 큰 경우. 
		
		if(parentSmallGroup == null)
			throw new SimpleSunnyException();
		Criteria criteria = getCriteria();
		
		criteria.setProjection(Projections.max("threadSeq"));
		//criteria.add(Restrictions.eq("thread", parentSmallGroup.getThread()));
		criteria.add(Restrictions.eq("parent", parentSmallGroup));
		
		Number maxThread = (Number)criteria.uniqueResult();

		if(maxThread == null)
			return -1;
		return maxThread;
	}
	
	public Number getMaxThreadSeqAtSameThread(SmallGroup parentSmallGroup,Integer smallGroupType){
		if(parentSmallGroup == null)
			throw new SimpleSunnyException();
		Criteria criteria = getCriteria();
		
		criteria.setProjection(Projections.max("threadSeq"));
		criteria.add(Restrictions.eq("thread", parentSmallGroup.getThread()));
		criteria.add(Restrictions.eq("depth", parentSmallGroup.getDepth()));
		
		Number maxThread = (Number)criteria.uniqueResult();

		if(maxThread == null)
			return -1;
		return maxThread;
	}	

	/**
	 * thread 가 같은 SmallGroup중에서 threadSeq의 값이 파라미터 threadSeq보다 크거나 값을 경우 모두 +1
	 * update smallGroup
	 * set threadSeq = threadSeq + 1
	 * where thread = ? and threadSeq >= ?
	 * 
	 * @param thread
	 * @param threadSeq
	 */
	public void updateThreadSeqPlugOne(Long thread, Integer threadSeq){
		String hqlUpdate = "update SmallGroup smallGroup set threadSeq = threadSeq + 1 where thread = :thread and threadSeq > :threadSeq";
		int updatedEntities = getCurrentSession().createQuery( hqlUpdate )
        .setLong( "thread", thread )
        .setInteger( "threadSeq", threadSeq )
        .executeUpdate();
		getCurrentSession().clear();
	}


	public void updateThreadSeqMinusOne(long thread, int threadSeq) {
		String hqlUpdate = "update SmallGroup smallGroup set threadSeq = threadSeq - 1 where thread = :thread and threadSeq > :threadSeq";
		int updatedEntities = getCurrentSession().createQuery( hqlUpdate )
        .setLong( "thread", thread )
        .setInteger( "threadSeq", threadSeq )
        .executeUpdate();
		getCurrentSession().clear();
	}
	
	
	
	/**
	 * site에서 smallGroupStartPath에 해당하는 SmallGroupId목록을 반환한다.
	 * @param siteId
	 * @param smallGroupStartPath
	 * @return
	 */
	public List<Long> getSmallGroupIdList(Long siteId, String smallGroupStartPath){
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("site.id", siteId));
		criteria.add(Restrictions.like("absolutePath", smallGroupStartPath, MatchMode.START));
		criteria.setProjection( Projections.projectionList().add( Projections.property("id") ) );
		return criteria.list();		
	}

	/**
	 * siteId에 해당하는 사이트에서 smallGroupPath와 같은 이름의 SmallGroup을 반환한다.
	 * @param siteId
	 * @param smallGroupPath
	 * @return
	 */
	public SmallGroup getSmallGroup(Long siteId, String smallGroupPath){
		Criteria criteria = getCriteria();	
		return (SmallGroup)criteria.uniqueResult();
	}

	public List<String> getDepartmentStrings(Site site, Long userId) {
		Criteria criteria = getCriteria();
		criteria.createAlias("users", "usersAlias");
		criteria.add(Restrictions.eq("site", site));
		criteria.add(Restrictions.eq("usersAlias.id", userId));
		criteria.add(Restrictions.eq("type", SmallGroup.TYPE_DEPARTMENT));
		criteria.setProjection(Projections.property("name"));
		return criteria.list();
				
	}

	public List<SmallGroupDTO> getMatchList(Site site, Long userId, String key,
			int type, Stream stream) {
		Criteria criteria = getCriteria();
    	criteria.add( Restrictions.like("this.name", key, MatchMode.ANYWHERE) );
    	criteria.add( Restrictions.eq( "this.site", site ) );
    	
    	criteria.setProjection( Projections.projectionList()
    			.add(Projections.property("id"), "id")
    			.add(Projections.property("name"), "value")
    	);
    	
    	criteria.setResultTransformer(new AliasToBeanResultTransformer(SmallGroupDTO.class));
    	return getPagedDTOList(criteria, stream);
	}
	
	public Page<SmallGroup> pagedList(Sunny sunny, Long userId,
			Integer queryType, String queryName, Integer type, String ordering,
			Integer page, int pageSize) {
		

		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("deleteFlag", false));
		criteria.add(Restrictions.eq("site", sunny.getSite()));
		
		if( type == null ){
			type = SmallGroup.TYPE_DEPARTMENT;
		}
		criteria.add(Restrictions.eq("type", type));
		
		if( queryName != null ){
			if( queryType == null )
				queryType = 0;
			Criterion expression = returnRestrictionAfterCheckInitial("name", queryName );
			
			criteria.add(expression);
		}
		
		if( ordering == null ){
			criteria.addOrder(Order.asc("name"));
		}else if( ordering != null && !ordering.isEmpty() ){
			criteria.addOrder(Order.asc(ordering));
		}
		
		return getPagedList(criteria, page, pageSize);
		
	}
	
	private Criterion returnRestrictionAfterCheckInitial(String column,
			String queryName) {
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

	public List<SmallGroupDTO> getPagedDTOList(Criteria criteria, Stream stream){
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

	public List<SmallGroup> getUserCreateSmallGroup(Long userId, int type) {
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("creator.id", userId));
		criteria.add(Restrictions.eq("type", type));
		
		return criteria.list();
	}

	public List<SmallGroup> getContentAssignedSmallGroups(Sunny sunny, Long id,
			Stream stream) {
		
		Criteria smallGroupContentAccessCriteria =  getCurrentSession().createCriteria(SmallGroupContentAccess.class);
		smallGroupContentAccessCriteria.add(Restrictions.eq("content.id", id));
		smallGroupContentAccessCriteria.setProjection(Projections.distinct(Projections.property("smallGroup.id")));
		
		List<Long> smallGroupIds = smallGroupContentAccessCriteria.list();
		
		if( smallGroupIds == null || smallGroupIds.size() == 0 )
			return null;
		
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.in("this.id", smallGroupIds));
		criteria.addOrder(Order.asc("type"));
		return getPagedList(criteria, stream);
	}

	public List<SmallGroupIdName> getChildrenSmallGroupIdNames(Site site,
			Long parentId, int ... types) {
		
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("site", site));
		
		if( parentId == null ){
			criteria.add(Restrictions.eq("depth", 0));
		}else{
			criteria.add(Restrictions.eq("parent.id", parentId));
		}
		
		if( types != null ){
			
			Disjunction disjunction = Restrictions.disjunction();
			for( int type : types ){
				disjunction.add(Restrictions.eq("type", type));	
			}
			criteria.add(disjunction);
		}
		
		criteria.setProjection(
				Projections.projectionList().add( Projections.property("id"), "id").add( Projections.property("name"), "name")
		);
		criteria.setResultTransformer(new AliasToBeanResultTransformer(SmallGroupIdName.class));
		return criteria.list();
	}
	
	public List<SmallGroupTree> getTreeFirst(Site site, int smallGroupType) {
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("site", site));
		criteria.add(Restrictions.eq("type", smallGroupType));
		criteria.add(Restrictions.eq("depth", 0));
		
		criteria.setProjection(	Projections.projectionList()
				.add(Projections.property("id"), "id")
				.add(Projections.property("name"), "name")
		);

		criteria.addOrder(Order.asc("thread"));
		criteria.addOrder(Order.asc("threadSeq"));
		
		criteria.setResultTransformer(new AliasToBeanResultTransformer(SmallGroupTree.class));
		
		return criteria.list();
	}
	
	public List<SmallGroupTree> getChildrenTree(Sunny sunny, SmallGroupTree parent) {
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("site", sunny.getSite()));
		criteria.add(Restrictions.eq("parent.id", parent.getId()));
		
		criteria.setProjection(	Projections.projectionList()
				.add(Projections.property("id"), "id")
				.add(Projections.property("name"), "name")
		);
		criteria.addOrder(Order.asc("thread"));
		criteria.addOrder(Order.asc("threadSeq"));
		
		criteria.setResultTransformer(new AliasToBeanResultTransformer(SmallGroupTree.class));
		
		return criteria.list();
	}

	

	
	

}	
