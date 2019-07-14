package kr.co.sunnyvale.sunny.repository.hibernate;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.AuthToken;
import kr.co.sunnyvale.sunny.domain.SiteInactiveUser;
import kr.co.sunnyvale.sunny.domain.extend.Stream;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
/**
 * {Hibernate}
 *	
 *<p>
 *	회원가입시 인증메일, 비밀번호 찾기시 인증메일을 보낼 때 사용하는 키젠을 관리하는 Repository
 *
 * @author mook
 *
 *	
 *
 */
@Repository(value = "authTokenRepository")
public class AuthTokenRepository extends HibernateGenericRepository<AuthToken>{
	public AuthTokenRepository(){

	}

//	public void removeAllValues(String userId, String type) {
//		String hqlUpdate = "update AuthToken authToken set value = ' ' where user.id = :userId and type = :type";
//		// or String hqlUpdate = "update Customer set name = :newName where name = :oldName";
//		int updatedEntities = getCurrentSession().createQuery( hqlUpdate )
//		        .setString( "userId", userId )
//		        .setString( "type", type )
//		        .executeUpdate();
//		getCurrentSession().clear();
//	}

	public void removeAllValues(Long id, String type) {
		//String hqlUpdate = "update AuthToken authToken set value = ' ' where user.id = :id and type = :type";
		String hqlUpdate = "delete AuthToken authToken where authToken.type = :type AND authToken.user.id = :id";
		int updatedEntities = getCurrentSession().createQuery( hqlUpdate )
		        .setLong( "id", id )
		        .setString( "type", type )
		        .executeUpdate();
		getCurrentSession().clear();
	}
	public void removeAllValues(SiteInactiveUser siteInactiveUser,
			String type) {
		String hqlUpdate = "delete AuthToken authToken where authToken.type = :type AND authToken.siteInactiveUser.id = :id";
		int updatedEntities = getCurrentSession().createQuery( hqlUpdate )
		        .setLong( "id", siteInactiveUser.getId() )
		        .setString( "type", type )
		        .executeUpdate();
		getCurrentSession().clear();
		
	}

//	public void removeMatch(Long userId, String type) {
//		String hqlUpdate = "delete AuthToken authToken where authToken.type = :type AND authToken.user.id = :userId";
//		int updatedEntities = getCurrentSession().createQuery( hqlUpdate )
//		        .setLong( "userId", userId )
//		        .setString( "type", type )
//		        .executeUpdate();
//		getCurrentSession().clear();
//		
//	}

	
	public AuthToken getRecentAuthToken(Long userId, String type) {
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("user.id", userId));
		criteria.add(Restrictions.eq("type", type));
		criteria.addOrder(Order.desc("createDate"));
		
		List<AuthToken> authTokens = criteria.list();
		if( authTokens != null && authTokens.size() > 0 ){
			return authTokens.get(0);	
		}else{
			return null;
		}
		
	}

	public List<AuthToken> getAllAuthTokens(Long userId, String type, Stream page) {
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("user.id", userId));
		criteria.add(Restrictions.eq("type", type));
		criteria.addOrder(Order.desc("createDate"));
		return getPagedList(criteria, page);
	}

	public AuthToken getCorrectAuthToken(Long id, String key, String type) {
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("user.id", id));
		criteria.add(Restrictions.eq("type", type));
		criteria.add(Restrictions.eq("value", key));
		return  (AuthToken) criteria.uniqueResult();
	}

	public AuthToken getCorrectAuthToken(SiteInactiveUser siteInactiveUser,
			String key, String type) {
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("siteInactiveUser", siteInactiveUser));
		criteria.add(Restrictions.eq("type", type));
		criteria.add(Restrictions.eq("value", key));
		return  (AuthToken) criteria.uniqueResult();
	}

	


}
