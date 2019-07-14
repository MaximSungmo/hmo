package kr.co.sunnyvale.sunny.repository.hibernate;

import kr.co.sunnyvale.sunny.domain.ExceptionLog;

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

@Repository(value = "exceptionLogRepository")
public class ExceptionLogRepository extends HibernateGenericRepository<ExceptionLog>{
	public ExceptionLogRepository(){

	}
}
