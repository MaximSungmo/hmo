package kr.co.sunnyvale.sunny.repository.hibernate;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.ChannelUser;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.dto.ChannelUserDTO;
import kr.co.sunnyvale.sunny.domain.dto.ContentDTO;
import kr.co.sunnyvale.sunny.domain.extend.Stream;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

/**
 * {Hibernate}
 *	
 *<p>
 *	Story 에 대한 태그들을 관리하는 tagRelation Repository
 *
 * @author mook
 *
 *	
 *	
 *
 */
@Repository(value = "channelUserRepository")
public class ChannelUserRepository extends HibernateGenericRepository<ChannelUser> {

	public List<ChannelUser> getUsersShownChannelUsers(Long userId, Stream stream){
		Criteria criteria = getCriteria();
		criteria.createCriteria("channel", "channelAlias");
		
		criteria.add(Restrictions.eq("user.id", userId));
		criteria.add(Restrictions.eq("isHidden", false));
		
		criteria.addOrder(Order.desc("channelAlias.updateDate"));
		return getPagedList(criteria, stream);
	}
	
	public List<Long> getJoinUserIds(Long channelId) {
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("channel.id", channelId));
		criteria.setProjection( Projections.distinct( Projections.property("user.id") ) );
		return criteria.list();
	}

	public List<ChannelUser> findUsersOrderByReadASC(Long channelId) {
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("channel.id", channelId));
		criteria.addOrder( Order.desc( "lastReadMessageInfoId" ));
		criteria.createAlias("user", "userAlias");
		return criteria.list();
	}

	public ChannelUser findByUserChannel(Long userId, Long channelId) {
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("user.id", userId));
		criteria.add(Restrictions.eq("channel.id", channelId));
		criteria.setMaxResults(1);
		return (ChannelUser) criteria.uniqueResult();
	}

	public List<User> getJoinUsers(Long id, Integer userMax) {
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("channel.id", id));
		if( userMax != null ){
			criteria.setMaxResults(userMax);
		}
		criteria.addOrder(Order.asc("createDate"));
		criteria.setProjection( Projections.property("user.id") );
		List<Long> userIds = criteria.list();
		
		Criteria userCriteria = getCurrentSession().createCriteria(User.class);
		userCriteria.add(Restrictions.in("id", userIds));
		return userCriteria.list();
	}

	public List<User> getJoinUsersExceptMe(User user, Long id, Integer userMax) {
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("channel.id", id));
		criteria.add(Restrictions.ne("user", user));
		if( userMax != null ){
			criteria.setMaxResults(userMax);
		}
		criteria.addOrder(Order.asc("createDate"));
		criteria.setProjection( Projections.distinct(Projections.property("user.id")) );
		List<Long> userIds = criteria.list();
		if( userIds == null || userIds.size() == 0 )
			return null;
		
		Criteria userCriteria = getCurrentSession().createCriteria(User.class);
		userCriteria.add(Restrictions.in("id", userIds));
//		userCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return userCriteria.list();
	}

	public List<Long> getAlreadyReadUserIds(Long channelId,
			Long lastReadMessageInfoId) {
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("channel.id", channelId));
		criteria.add(Restrictions.ge("lastReadMessageInfoId", lastReadMessageInfoId));
		
		criteria.setProjection( Projections.distinct( Projections.property("user.id") ) );
		return criteria.list();
	}

	public List<ChannelUser> getLastReadUsers(Long channelId, Long lastMessageInfoId, Long userId ) {
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("channel.id", channelId));
		criteria.createAlias("user", "userAlias");
		criteria.add(Restrictions.eq("lastReadMessageInfoId", lastMessageInfoId));
		criteria.add(Restrictions.ne("user.id", userId));
		return criteria.list();
		
	}

	public List<ChannelUserDTO> getJoinChannelUserDTOs(Long channelId, Integer userMax) {
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("channel.id", channelId));
		criteria.createAlias("user", "userAlias");
		if( userMax != null ){
			criteria.setMaxResults(userMax);
		}
		
		criteria.setProjection(	Projections.projectionList()
				// Content
				.add(Projections.property("userAlias.id"), "id")
				.add(Projections.property("userAlias.name"), "name")
				.add(Projections.property("userAlias.profilePic"), "profilePic")
				.add(Projections.property("lastReadMessageInfoId"), "lastReadMessageInfoId")

		);
		criteria.setResultTransformer(new AliasToBeanResultTransformer(ChannelUserDTO.class));
		
		return criteria.list();
		
	}

	public void cancleHiddenAll(Long channelId) {

		String hqlUpdate = "update ChannelUser channelUser set isHidden = false where channel.id = :channelId and isHidden = true";
		Query query = getCurrentSession().createQuery( hqlUpdate ).setLong( "channelId", channelId);
		query.executeUpdate();
		getCurrentSession().clear();
	}
		
}
