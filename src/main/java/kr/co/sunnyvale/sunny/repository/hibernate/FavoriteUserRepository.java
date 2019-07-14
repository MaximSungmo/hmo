package kr.co.sunnyvale.sunny.repository.hibernate;

import java.io.Serializable;

import kr.co.sunnyvale.sunny.domain.FavoriteUser;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
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

@Repository(value = "favoriteUserRepository")
public class FavoriteUserRepository extends HibernateGenericRepository<FavoriteUser>{
	public FavoriteUserRepository(){

	}

	public boolean isAlreadyFavorited(Sunny sunny, Long favoritedUserId,
			Long favoriterUserId) {
		Criteria criteria = getCriteria();

		criteria.add(Restrictions.eq("site", sunny.getSite()));
		criteria.add(Restrictions.eq("favoriter.id", favoriterUserId));
		criteria.add(Restrictions.eq("favorited.id", favoritedUserId));
		
		criteria.setMaxResults(1);
		
		Object obj = criteria.uniqueResult();
		if( obj == null ){
			return false;
		}
		return true;
		
	}

	public FavoriteUser find(Sunny sunny, User favoritedUser, User favoriterUser) {

		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("site", sunny.getSite()));
		criteria.add(Restrictions.eq("favoriter", favoriterUser));
		criteria.add(Restrictions.eq("favorited", favoritedUser));

		criteria.setMaxResults(1);
		
		return (FavoriteUser) criteria.uniqueResult();
	}

}
