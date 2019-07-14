package kr.co.sunnyvale.sunny.repository.hibernate;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.SmallGroup;
import kr.co.sunnyvale.sunny.domain.UserSmallGroupAccess;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.util.CriteriaUtils;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository(value = "userSmallGroupAccessRepository")
public class UserSmallGroupAccessRepository extends HibernateGenericRepository<UserSmallGroupAccess>{
	public UserSmallGroupAccessRepository(){

	}
	
	/**
	 * smallGroupIdList에 해당하는 userSmallGroupAccess를 반환한다.
	 * @param smallGroupIdList
	 * @param searchName
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public Page<UserSmallGroupAccess> getUserSmallGroupAccessList( List<Long>smallGroupIdList, String searchName, Integer pageNum, int pageSize) {
		// smallGroupIdList에 해당하는 부서들에 포함되어 있는 사용자 목록을 반환한다.  SmallGroup과 User간에 관계 도메인이 아직 없는듯?
		Criteria criteria = getCriteria();
		//criteria.createAlias("smallGroups", "smallGroupsAlias");
		criteria.add(Restrictions.in("smallGroup.id", smallGroupIdList));
		if( searchName != null ){
			criteria.createAlias("user", "userAlias");
			criteria.add(CriteriaUtils.returnRestrictionAfterCheckInitial("userAlias.name", searchName));
		}		
		return getPagedList(criteria, pageNum, pageSize);
	}

	public Page<UserSmallGroupAccess> getUserSmallGroupAccessList(
			Long smallGroupId, 
			String searchName, 
			String ordering,
			Boolean desc,
			Integer page, 
			int pageSize) {
		
		Criteria criteria = getCriteria();
		//criteria.createAlias("smallGroups", "smallGroupsAlias");
		criteria.add(Restrictions.eq("smallGroup.id", smallGroupId));
		criteria.createAlias("user", "userAlias");
		if( searchName != null ){
			
			criteria.add(CriteriaUtils.returnRestrictionAfterCheckInitial("userAlias.name", searchName));
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
	public List<UserSmallGroupAccess> getUserSmallGroupAccessList(
			SmallGroup smallGroup, 
			String searchName, 
			String ordering,
			Boolean desc,
			Stream stream) {
		
		Criteria criteria = getCriteria();
		//criteria.createAlias("smallGroups", "smallGroupsAlias");
		criteria.add(Restrictions.eq("smallGroup", smallGroup));
		criteria.createAlias("user", "userAlias");
		if( searchName != null ){
			
			criteria.add(CriteriaUtils.returnRestrictionAfterCheckInitial("userAlias.name", searchName));
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
		return getPagedList(criteria, stream);
		
	}
	
	
	public void setPermission( Long userId,Long smallGroupId, String ptype){
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("user.id", userId));
		criteria.add(Restrictions.eq("smallGroup.id", smallGroupId));
		UserSmallGroupAccess sgaa = (UserSmallGroupAccess)criteria.uniqueResult();
		if(sgaa != null){
			if("a".equals(ptype)){
				sgaa.setAdmin(!sgaa.isAdmin());
			}else if("r".equals(ptype)){
				sgaa.setReadPermission(!sgaa.isReadPermission());
			}else if("w".equals(ptype)){
				sgaa.setWritePermission(!sgaa.isWritePermission());
			}else if("d".equals(ptype)){
				sgaa.setDeletePermission(!sgaa.isDeletePermission());
			}else if("fu".equals(ptype)){
				sgaa.setFileUploadPermission(!sgaa.isFileUploadPermission());
			}else if("fd".equals(ptype)){
				sgaa.setFileDownloadPermission(!sgaa.isFileDownloadPermission());
			}
			update(sgaa);
		} // sgaa != null
	}
	
	
	public UserSmallGroupAccess select(Long userId, Long smallGroupId){
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("user.id", userId));
		criteria.add(Restrictions.eq("smallGroup.id", smallGroupId));
		UserSmallGroupAccess sgaa = (UserSmallGroupAccess)criteria.uniqueResult();
		return sgaa;
	}
	
	public void delete(Long userId, Long smallGroupId){
//		Criteria criteria = getCriteria();
//		criteria.add(Restrictions.eq("user.id", userId));
//		criteria.add(Restrictions.eq("smallGroup.id", smallGroupId));
		String hqlUpdate = "delete UserSmallGroupAccess where user.id = :userId and smallGroup.id >= :smallGroupId";
		int updatedEntities = getCurrentSession().createQuery( hqlUpdate )
        .setLong( "userId", userId )
        .setLong( "smallGroupId", smallGroupId )
        .executeUpdate();
		
		getCurrentSession().clear();
		
	}

	public boolean isAlreadyJoined(Long smallGroupId, Long userId) {
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("smallGroup.id", smallGroupId));
		criteria.add(Restrictions.eq("user.id", userId));
		criteria.setMaxResults(1);
		List<Object> obj = criteria.list();
		if( obj == null || obj.size() == 0 ){
			return false;
		}
		
		return true; 
	}

	public UserSmallGroupAccess getUserSmallGroupAccessUser(Long smallGroupId,
			Long userId) {
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("smallGroup.id", smallGroupId));
		criteria.add(Restrictions.eq("user.id", userId));
		criteria.setMaxResults(1);
		
		return (UserSmallGroupAccess) criteria.uniqueResult();
	}

	
	
	
}