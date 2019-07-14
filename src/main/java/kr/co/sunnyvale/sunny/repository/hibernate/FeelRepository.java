package kr.co.sunnyvale.sunny.repository.hibernate;

import kr.co.sunnyvale.sunny.domain.Feel;
import kr.co.sunnyvale.sunny.domain.Role;

import org.springframework.stereotype.Repository;
/**
 *
 * {Hibernate}
 *<p>
 *	아주좋아요, 좋아요, 보통이예요, 별로예요. 이 네 가지를 관리하는 Repository
 *
 * @author mook
 *
 *	
 *
 */
@Repository(value = "feelRepository")
public class FeelRepository extends HibernateGenericRepository<Feel>{
	public FeelRepository(){

	}

	public void generateDefault() {
		save( new Feel(Feel.ID_NEGATIVE, "negative", "별로예요", -1));
		save( new Feel(Feel.ID_BEST, "like", "좋아요", 1));
		save( new Feel(Feel.ID_SAD, "sad", "슬퍼요", 1));
		save( new Feel(Feel.ID_CHEER, "cheer", "힘내요", 1));
		save( new Feel(Feel.ID_NICE, "nice", "멋져요", 1));
		save( new Feel(Feel.ID_LIKE, "like", "좋아요", 1));
		clear();
		flush();
		
	}

}
