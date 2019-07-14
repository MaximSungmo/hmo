package kr.co.sunnyvale.sunny.repository.hibernate;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import kr.co.sunnyvale.sunny.domain.Channel;
import kr.co.sunnyvale.sunny.domain.LastRead;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.dto.NotifyInfoDTO;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
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

@Repository(value = "channelRepository")
public class ChannelRepository extends HibernateGenericRepository<Channel>{
	public ChannelRepository(){

	}

	public List<Channel> getChannel(User user, Stream stream) {
		
		Criteria criteria = getCriteria();
		
		/*
		 * Distinct 를 이용하기 위한 DetachedCriteria
		 */
//		DetachedCriteria idsOnlyCriteria = DetachedCriteria.forClass(ChannelUser.class);
//		idsOnlyCriteria.add(Restrictions.eq("user", user));
//		idsOnlyCriteria.setProjection( Projections.distinct( Projections.property("channel.id") ));
//		criteria.add(Subqueries.propertyIn("id", idsOnlyCriteria));

		/*
		 * 간단한 Join 을 이용한 Criteria. distinct 가 안됨
		 */
//		criteria.createAlias("user1", "user1Alias", Criteria.LEFT_JOIN);
//		criteria.createAlias("user2", "user2Alias", Criteria.LEFT_JOIN);
		Criteria usersCriteria = criteria.createCriteria("users");
		usersCriteria.add(Restrictions.eq("id", user.getId()));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		
		criteria.addOrder(Order.desc("updateDate"));

		return  getPagedList(criteria, stream);
	}

	public Channel getOneToOneRoom(Long user1Id, Long user2Id) {
		Criteria criteria = getCriteria();
		//add other joins and query params here
		criteria.add( 
				Restrictions.or(
						Restrictions.and(Restrictions.eq("user1.id", user1Id), Restrictions.eq("user2.id", user2Id)), 
						Restrictions.and(Restrictions.eq("user2.id", user1Id), Restrictions.eq("user1.id", user2Id))
						)
					);
		criteria.add(Restrictions.eq("type", Channel.ONETOONE));
		criteria.setMaxResults( 1 );
		return (Channel) criteria.uniqueResult();
	}

	public List<User> getJoinUsers(Long channelId) {
		Criteria criteria = getCriteria();
		Criteria usersCriteria = criteria.createCriteria("users");
		usersCriteria.setProjection( Projections.property( "id" ));
		
		return criteria.list();
	}


	public Serializable getUpdateDate(Long channelId) {
		Criteria criteria =  getCriteria();
		criteria.add(Restrictions.eq("id", channelId));
		criteria.setProjection(	Projections.projectionList().add( Projections.property("updateDate") ));
		
		return (Date) criteria.uniqueResult();
	}

	public List<Channel> getUsersShownChannels(Long userId, Stream stream) {
		Criteria criteria = getCriteria();
		//criteria.createAlias("users", "usersAlias");
		criteria.createAlias("userRelation", "userRelationAlias");
		criteria.add(Restrictions.eq("userRelationAlias.user.id", userId));
		criteria.add(Restrictions.eq("userRelationAlias.isHidden", false));
		criteria.add(Restrictions.eq("isHidden", false));
		
		criteria.addOrder(Order.desc("updateDate"));
		return getPagedList(criteria, stream);
	
	}

	public Page<Channel> getUsersShownChannels(Long userId, Integer pageNum,
			int pageSize) {
		Criteria criteria = getCriteria();
		//criteria.createAlias("users", "usersAlias");
		criteria.createAlias("userRelation", "userRelationAlias");
		criteria.add(Restrictions.eq("userRelationAlias.user.id", userId));
		criteria.add(Restrictions.eq("userRelationAlias.isHidden", false));
		criteria.add(Restrictions.eq("isHidden", false));
		
		criteria.addOrder(Order.desc("updateDate"));
		
		return getPagedList(criteria, pageNum, pageSize);
	}

	public Channel getNotifynewOne(Sunny sunny, User user, LastRead lastRead) {
		Criteria criteria = getCriteria();
		criteria.createAlias("userRelation", "userRelationAlias");
		criteria.add(Restrictions.eq("userRelationAlias.user", user));
		criteria.add(Restrictions.eq("userRelationAlias.isHidden", false));
		criteria.add(Restrictions.eq("isHidden", false));
		

		if( lastRead != null )
			criteria.add(Restrictions.gt("updateDate", lastRead.getMessageLastRead()));
		
		criteria.addOrder(Order.desc("updateDate"));
		criteria.setMaxResults(1);
		return (Channel) criteria.uniqueResult();
		
	}
	public Number getChannelUnreadCount(User user, Date lastRead) {
		Criteria criteria = getCriteria();
		
		criteria.createAlias("userRelation", "userRelationAlias");
		criteria.add(Restrictions.eq("userRelationAlias.user", user));
		criteria.add(Restrictions.eq("userRelationAlias.isHidden", false));
		criteria.add(Restrictions.eq("isHidden", false));
		if( lastRead != null ){
			criteria.add(Restrictions.gt("updateDate", lastRead));
			//criteria.add(Restrictions.neProperty("userRelationAlias.lastReadDate", "updateDate"));
		}
		criteria.setProjection(Projections.rowCount());	
		return (Number)criteria.uniqueResult();
	}

	public NotifyInfoDTO getNotifyInfo(Sunny sunny, Long userId,
			LastRead lastRead) {
		
		NotifyInfoDTO notifyInfoDto = new NotifyInfoDTO();		
		
		Criteria criteria = getCriteria();
		criteria.createAlias("userRelation", "userRelationAlias");
		criteria.add(Restrictions.eq("userRelationAlias.user.id", userId));
		criteria.add(Restrictions.eq("userRelationAlias.isHidden", false));
		criteria.add(Restrictions.eq("isHidden", false));
		
		criteria.addOrder(Order.desc("updateDate"));
		criteria.setMaxResults(1);		
		
		
		criteria.setProjection(Projections.property("updateDate"));

		Object updateDate = criteria.uniqueResult();

		if( updateDate == null )
			notifyInfoDto.setUpdateDate(null);
		else{
			notifyInfoDto.setUpdateDate( (Date) updateDate );	
		}

		criteria.setProjection(null);

		if( lastRead != null )
			criteria.add(Restrictions.gt("updateDate", lastRead.getMessageLastRead()));


		criteria.setProjection(Projections.rowCount());
		Object count = criteria.uniqueResult();

		if( count == null ){
			notifyInfoDto.setUnreadCount(0);
		}else{
			notifyInfoDto.setUnreadCount( ((Number) count).intValue() );
		}

		return notifyInfoDto; 
		
	}


}
