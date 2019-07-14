package kr.co.sunnyvale.sunny.repository.hibernate;

import kr.co.sunnyvale.sunny.domain.Template;

import org.springframework.stereotype.Repository;
/**
 * {Hibernate}
 *	
 *<p>
 *
 * @author mook
 *
 */
@Repository(value = "templateRepository")
public class TemplateRepository extends HibernateGenericRepository<Template>{
	public TemplateRepository(){

	}
}
