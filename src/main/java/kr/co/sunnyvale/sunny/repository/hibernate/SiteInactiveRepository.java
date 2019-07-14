package kr.co.sunnyvale.sunny.repository.hibernate;

import kr.co.sunnyvale.sunny.domain.SiteInactive;

import org.springframework.stereotype.Repository;
/**
 * {Hibernate}
 *	
 *<p>
 *
 * @author mook
 *
 */
@Repository(value = "siteInactiveRepository")
public class SiteInactiveRepository extends HibernateGenericRepository<SiteInactive>{
	public SiteInactiveRepository(){

	}
}
