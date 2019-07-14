package kr.co.sunnyvale.sunny.repository.hibernate;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.Site;
import kr.co.sunnyvale.sunny.domain.SiteInactiveUser;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
/**
 * {Hibernate}
 *	
 *<p>
 *
 * @author mook
 *
 */
@Repository(value = "siteInactiveUserRepository")
public class SiteInactiveUserRepository extends HibernateGenericRepository<SiteInactiveUser>{
	public SiteInactiveUserRepository(){

	}

	public Page<SiteInactiveUser> getSiteInactiveUsers(Sunny sunny, Integer status, Integer type, Integer page, int pageSize ) {
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("site", sunny.getSite()));
		
		if( status != null ){
			criteria.add( Restrictions.eq("status", status) );	
		}
		if( type != null ){
			criteria.add( Restrictions.eq("type", type) );
		}
		return getPagedList(criteria, page, pageSize);
	}

	public SiteInactiveUser findBySiteAndUser(Long id, Long userId) {
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("site.id", id));
		criteria.add(Restrictions.eq("user.id", userId));
		
		criteria.setMaxResults(1);
		return (SiteInactiveUser) criteria.uniqueResult();

	}

	public SiteInactiveUser findByEmail(Site site, String email) {
		Criteria criteria = getCriteria();
		
		if( site != null )
			criteria.add(Restrictions.eq("site", site));
		
		criteria.add(Restrictions.eq("email", email));
		criteria.setMaxResults(1);
		return (SiteInactiveUser) criteria.uniqueResult();
	}
	
	public SiteInactiveUser findByEmailAndType(Site site, String email, Integer type) {
		Criteria criteria = getCriteria();
		
		if( site != null )
			criteria.add(Restrictions.eq("site", site));
		
		if( type != null )
			criteria.add(Restrictions.eq("type", type));
		
		criteria.add(Restrictions.eq("email", email));
		
		return (SiteInactiveUser) criteria.uniqueResult();
	}

	public SiteInactiveUser findAdminByEmail(String email) {
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("email", email));
		criteria.add(Restrictions.eq("isAdmin", true));
		
		criteria.setMaxResults(1);
		return (SiteInactiveUser) criteria.uniqueResult();
	}
}
