//package kr.co.sunnyvale.sunny.repository.hibernate;
//
//import kr.co.sunnyvale.sunny.domain.Email;
//
//import org.hibernate.Criteria;
//import org.hibernate.criterion.Restrictions;
//import org.springframework.stereotype.Repository;
///**
// * {Hibernate}
// *	
// *<p>
// *	Tag 에 대한 작업을 하는 Repository
// *
// * @author mook
// *
// */
//@Repository(value = "emailRepository")
//public class EmailRepository extends HibernateGenericRepository<Email>{
//	public EmailRepository(){
//
//	}
//
//	public boolean existsEmail(String email) {
//		Criteria criteria = getCriteria();
//		criteria.add(Restrictions.eq("email", email));
//		criteria.setMaxResults(1);
//		
//		return criteria.uniqueResult() == null ? false : true;
//	
//	}
//
//}
