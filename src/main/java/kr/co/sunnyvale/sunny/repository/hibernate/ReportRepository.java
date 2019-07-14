package kr.co.sunnyvale.sunny.repository.hibernate;

import kr.co.sunnyvale.sunny.domain.Report;

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

@Repository(value = "reportRepository")
public class ReportRepository extends HibernateGenericRepository<Report>{
	public ReportRepository(){

	}
}
