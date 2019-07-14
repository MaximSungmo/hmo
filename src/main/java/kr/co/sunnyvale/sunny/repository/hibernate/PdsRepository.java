package kr.co.sunnyvale.sunny.repository.hibernate;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.Pds;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.UserSmallGroupAccess;
import kr.co.sunnyvale.sunny.domain.dto.ContentDTO;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.util.CriteriaUtils;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository(value = "pdsRepository")
public class PdsRepository extends ContentRepository<Pds> {

	
	@SuppressWarnings("unchecked")
	private List<Long> getUserJoinedSmallGroupIds( User user ){
		Criteria userSmallGroupAccessCriteria = getCurrentSession().createCriteria(UserSmallGroupAccess.class);
		userSmallGroupAccessCriteria.add(Restrictions.eq("user", user));
		userSmallGroupAccessCriteria.setProjection(Projections.projectionList().add(Projections.property("smallGroup.id"), "smallGroupId"));
		return userSmallGroupAccessCriteria.list();
	}
	
	
	public List<ContentDTO> getPagedDTOList(Criteria criteria, Stream stream){
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


	public Page<Pds> getPagedSmallGroupPds(Sunny sunny, Long smallGroupId,
			User basecampUser, String queryName, String ordering, Integer page, int pageSize) {

		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("deleteFlag", false));
		criteria.add(Restrictions.eq("smallGroup.id", smallGroupId));
		if( queryName != null ){
			criteria.add(
			Restrictions.or(
					CriteriaUtils.returnRestrictionAfterCheckInitial("title", queryName ),
					CriteriaUtils.returnRestrictionAfterCheckInitial("rawText", queryName )
				)
			);
		}
		criteria.addOrder(Order.desc("createDate"));
		if( basecampUser != null ){
			criteria.add(Restrictions.eq("user", basecampUser));	
		}
		
		return getPagedList(criteria, page, pageSize);
	}



}
