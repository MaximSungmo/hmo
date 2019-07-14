package kr.co.sunnyvale.sunny.repository.hibernate;


import java.util.List;

import kr.co.sunnyvale.sunny.domain.Site;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.util.CriteriaUtils;

import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository(value = "siteRepository")
public class SiteRepository extends HibernateGenericRepository<Site>{
	public SiteRepository(){

	}

	public Site getCurrentSite() {
		Criteria criteria = getCriteria();
		
		return (Site) criteria.uniqueResult();
	}

	public Site findFromDomainName(String domainName) {
		Criteria criteria = getCriteria();
		
		criteria.createAlias("domains", "domainsAlias");
		criteria.add(Restrictions.eq("domainsAlias.name", domainName));
		criteria.setMaxResults(1);
		return (Site) criteria.uniqueResult();
	}

	public Site findFromSiteId(Long domainName) {
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("id", domainName));
		return (Site) criteria.uniqueResult();
	}

	public Site findFromPathOrId(String pathUrl) {
		Criteria criteria = getCriteria();
		
		try{
			Long siteId = Long.parseLong(pathUrl);
			criteria.add(Restrictions.eq("id", siteId));
		}catch(NumberFormatException e){
			criteria.add(Restrictions.eq("path", pathUrl));
		}
		
		criteria.setMaxResults(1);
		return (Site) criteria.uniqueResult();
	}

	public Site findByUserId(Long userId) {
		Criteria criteria = getCriteria();
		
		criteria.createAlias("users", "usersAlias");
		criteria.add(Restrictions.eq("usersAlias.id", userId));
		criteria.setMaxResults(1);
		return (Site) criteria.uniqueResult();
	}

	public Page<Site> getOpenSites(String query, String ordering, Boolean desc,
			Integer page, int pageSize) {
		
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("deleteFlag", false));
		if( query != null ){
			
			Disjunction disjunction = Restrictions.disjunction();
			disjunction.add(CriteriaUtils.returnRestrictionAfterCheckInitial("companyName", query ));
			disjunction.add(CriteriaUtils.returnRestrictionAfterCheckInitial("companyDomain", query ));
			disjunction.add(CriteriaUtils.returnRestrictionAfterCheckInitial("companyPhone", query ));
			criteria.add(disjunction);
		}
		if( ordering != null ){
			
			Order order = desc == null || desc == true ? Order.desc(ordering) : Order.asc(ordering);
			criteria.addOrder(order);
		}else{
			criteria.addOrder(Order.desc("createDate"));	
		}
		
		return getPagedList(criteria, page, pageSize);
		
	}

	public List<Site> getUserJoinedSites(Long userId, Stream stream) {
		Criteria criteria = getCriteria();
		
		criteria.createAlias("siteUsers", "siteUsersAlias");
		criteria.add(Restrictions.eq("siteUsersAlias.user.id", userId));
	
		return getPagedList(criteria, stream);
	}

	public Page<Site> getAllFromSuper(Integer page, int pageSize) {
		Criteria criteria = getCriteria();
		criteria.addOrder(Order.desc("createDate"));
		return getPagedList(criteria, page, pageSize);
	}

//	public Module getIndexModule() {
//		Criteria criteria = getCriteria();
//		
//		criteria.setMaxResults(1);
//		criteria.setProjection( Projections.projectionList().add(Projections.property("indexModule")));
//		
//		criteria.setResultTransformer( Transformers.aliasToBean(Module.class));
//		
//		return (Module) criteria.uniqueResult();
//				
//	}


}
