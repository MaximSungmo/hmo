package kr.co.sunnyvale.sunny.repository.hibernate;

import kr.co.sunnyvale.sunny.domain.Revision;

import org.springframework.stereotype.Repository;
/**
 * {Hibernate}
 *	
 *<p>
 *	User와 User 를 연결해주는 FeedRelation 테이블을 직접 관리하는 Repository
 *
 * @author mook
 *
 *	
 */
@Repository(value = "revisionRepository")
public class RevisionRepository extends HibernateGenericRepository<Revision> {
	
	

}
