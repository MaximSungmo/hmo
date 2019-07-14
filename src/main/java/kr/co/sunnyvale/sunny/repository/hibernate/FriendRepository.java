package kr.co.sunnyvale.sunny.repository.hibernate;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kr.co.sunnyvale.sunny.domain.Friend;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.exception.SimpleSunnyException;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository(value = "friendRepository")
public class FriendRepository extends HibernateGenericRepository<Friend>{
	public FriendRepository(){
		
	}

	public Friend find(Long followedId, Long followerId) {
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("followed.id", followedId));
		criteria.add(Restrictions.eq("follower.id", followerId));
		List<Friend> friendRequests = criteria.list();
		
		if( friendRequests == null || friendRequests.size() == 0 )
			return null;
		
		// 여기서 뭔가 처리해줘야함
		if( friendRequests.size() > 1 ){
			throw new SimpleSunnyException();
		}
		return friendRequests.get(0);
		
	}

	public Set<Long> getFriendRequestIds(Long userId) {
		Criteria criteria = getCriteria();
	 	criteria.add(Restrictions.eq( "follower.id", userId) );
		criteria.setProjection(Projections.property("followed.id"));
   
 		return new HashSet(criteria.list()); 
	}

	public List<Friend> getNonAcceptedFriendRequests(Long userId,
			Stream stream) {
		Criteria criteria = getCriteria();
		criteria.setFetchMode("follower", FetchMode.JOIN);
		criteria.add(
				Restrictions.and(
						Restrictions.eq( "followed.id", userId),
						Restrictions.eq( "sendFriendRequest", true)
						)
		);
		criteria.addOrder(Order.desc("friendRequestDate"));		
		return getPagedList(criteria, stream);
	}

	public Number getFriendRequestUnreadCount(User user, Date lastRead) {
		Criteria criteria = getCriteria();
		criteria.add(
				Restrictions.and(
						Restrictions.eq( "followed.id", user.getId()),
						Restrictions.eq( "sendFriendRequest", true)
						)
		);
		
		if( lastRead != null )
			criteria.add(Restrictions.gt("createDate", lastRead));
		
		criteria.setProjection(Projections.rowCount());		
		return (Number)criteria.uniqueResult();
	}
	
	public Page<Friend> getFriends(Sunny sunny, Long authUserId, Long userId,
			String query, Integer page, Integer pageSize) {

		Criteria criteria = getCurrentSession().createCriteria(Friend.class);
		
		criteria.createAlias("follower", "userAlias");
		criteria.createAlias("follower.info", "infoAlias");
		criteria.add(
				Restrictions.and(
						Restrictions.eq( "followed.id", userId),
						Restrictions.eq( "this.friend", true)
						)
		);
		
		if( query != null ){
			Disjunction disjunction = Restrictions.disjunction();
			disjunction.add(Restrictions.like("userAlias.name", query, MatchMode.ANYWHERE));
			criteria.add(disjunction);
		}

		criteria.addOrder(Order.asc("userAlias.name"));	
		
		return getPagedList(criteria, page, pageSize);
	}
}
