package kr.co.sunnyvale.sunny.repository.hibernate;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.Content;
import kr.co.sunnyvale.sunny.domain.Feel;
import kr.co.sunnyvale.sunny.domain.FeelAndContentAndUser;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.extend.Stream;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
/**
 * {Hibernate}
 *
 *<p>
 *	컨튼츠에 한 '개인'의 평가를 주관하는 Repository
 *
 * @author mook
 *
 *	
 *
 */
@Repository(value = "feelAndContentAndUserRepository")
public class FeelAndContentAndUserRepository extends HibernateGenericRepository<FeelAndContentAndUser>{
	public FeelAndContentAndUserRepository(){

	}

	public FeelAndContentAndUser findUniqFeelAndContentAndUser(User user, Content content) {
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("user", user));
    	criteria.add(Restrictions.eq("content", content));
    	FeelAndContentAndUser ealuateContentUser = (FeelAndContentAndUser) criteria.uniqueResult();
    	return ealuateContentUser;
		
	}

	public Long getUserFeel(User user, Content content) {
		Criteria criteria = getCriteria();
    	criteria.setProjection(Projections.property("id"));
		criteria.add(Restrictions.eq("user", user));
    	criteria.add(Restrictions.eq("content", content));
    	return (Long) criteria.uniqueResult();
	}

	public Feel getFeelFromContentUser(User user, Content content) {
		Criteria criteria = getCriteria();
    	criteria.setProjection(Projections.property("feel"));
		criteria.add(Restrictions.eq("user", user));
    	criteria.add(Restrictions.eq("content", content));
    	return (Feel) criteria.uniqueResult();
	}

	public List<FeelAndContentAndUser> getAllUsersByContent(Long contentId, Stream stream) {
		Criteria criteria = getCriteria();
    	criteria.add(Restrictions.eq("content.id", contentId));
    	return getPagedList(criteria, stream);

	}

	public List<FeelAndContentAndUser> getMatchUsersByContent(Long contentId, Integer feelId, Stream stream) {
		Criteria criteria = getCriteria();
    	criteria.add(Restrictions.eq("content.id", contentId));
    	criteria.add(Restrictions.eq("feel.id", feelId));
    	return getPagedList(criteria, stream);
	}

	public List<String> getAllNamesByContent(Long contentId, Stream stream) {
		Criteria criteria = getCriteria();
		criteria.createAlias("user", "userAlias");
    	criteria.add(Restrictions.eq("content.id", contentId));
    	
    	criteria.setProjection( Projections.projectionList().add( Projections.property("userAlias.name")) );
    	if( stream == null )
    		return  criteria.list();
    	
	   	if( stream.getSize() != null)
	   		criteria.setMaxResults(stream.getSize());
	   	
	   	if( stream.getGreaterThan() == null)
	   		return criteria.list();
	   	
    	if( stream.getGreaterThan() == true ){
    		criteria.add( Restrictions.gt(stream.getBaseColumn(), stream.getBaseData()));
    	}else{
    		criteria.add( Restrictions.lt(stream.getBaseColumn(), stream.getBaseData()));
    	}
    	
    	return criteria.list();
	}

	public List<String> getMatchNamesByContent(Long contentId,
			Integer feelId, Stream stream) {
		Criteria criteria = getCriteria();
    	criteria.add(Restrictions.eq("content.id", contentId));
    	criteria.add(Restrictions.eq("feel.id", feelId));
    	criteria.setProjection( Projections.projectionList().add( Projections.property("user.name")) );
    	if( stream == null )
    		return  criteria.list();
    	
	   	if( stream.getSize() != null)
	   		criteria.setMaxResults(stream.getSize());
	   	
	   	if( stream.getGreaterThan() == null)
	   		return criteria.list();
	   	
    	if( stream.getGreaterThan() == true ){
    		criteria.add( Restrictions.gt(stream.getBaseColumn(), stream.getBaseData()));
    	}else{
    		criteria.add( Restrictions.lt(stream.getBaseColumn(), stream.getBaseData()));
    	}
    	
    	return criteria.list();
	}

}
