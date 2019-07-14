package kr.co.sunnyvale.sunny.repository.hibernate;

import kr.co.sunnyvale.sunny.domain.Domain;

import org.springframework.stereotype.Repository;

/**
 * 	{Hibernate}
 *	
 *<p>
 *	
 *
 * @author mook
 *
 *
 */

@Repository(value = "domainRepository")
public class DomainRepository extends HibernateGenericRepository<Domain>{
	public DomainRepository(){

	}

}
