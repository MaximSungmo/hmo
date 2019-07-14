package kr.co.sunnyvale.sunny.repository.hibernate;

import kr.co.sunnyvale.sunny.domain.Sequence;

import org.springframework.stereotype.Repository;
/**
 * {Hibernate}
 *	
 *<p>
 *	유저 회원가입 시 PrimaryKey 인 ID의 Sequence 를 책임지는 Sequence 테이블 Repository
 *
 * @author mook
 *
 *	
 *
 */
@Repository(value = "sequenceRepository")
public class SequenceRepository extends HibernateGenericRepository<Sequence>{
	public SequenceRepository(){

	}
}
