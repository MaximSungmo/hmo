package kr.co.sunnyvale.sunny.repository.hibernate;

import kr.co.sunnyvale.sunny.domain.LastRead;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;

import org.springframework.stereotype.Repository;

/**
 * 
 *	{Hibernate}
 *	
 *<p>
 *	스토리라인을 등록할 때 쓰는 카테고리 Repository
 *
 * @author mook
 *
 *
 */

@Repository(value = "lastReadRepository")
public class LastReadRepository extends HibernateGenericRepository<LastRead>{

	public LastReadRepository(){

	}

	public void updateMessageDate( User user ){
		LastRead lastRead = findUniqByObject("user", user);
		if( lastRead == null ){
			lastRead = new LastRead(user);
			lastRead.updateMessageDate();
			save(lastRead);
		}else{
			lastRead.updateMessageDate();
			update(lastRead);
		}
	}

	public void updateQuestionNoteCount(SecurityUser securityUser) {
		// TODO Auto-generated method stub
		
	}
	

}
