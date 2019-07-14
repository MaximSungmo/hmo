package kr.co.sunnyvale.sunny.repository.hibernate;

import kr.co.sunnyvale.sunny.domain.DefaultTag;

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

@Repository(value = "defaultTagRepository")
public class DefaultTagRepository extends HibernateGenericRepository<DefaultTag>{
	public DefaultTagRepository(){

	}
}
