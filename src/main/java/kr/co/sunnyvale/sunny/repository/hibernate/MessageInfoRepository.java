package kr.co.sunnyvale.sunny.repository.hibernate;

import kr.co.sunnyvale.sunny.domain.MessageInfo;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;


@Repository(value = "messageInfoRepository")
public class MessageInfoRepository extends HibernateGenericRepository<MessageInfo>{
	public MessageInfoRepository(){

	}

	public void readMessages(Long channelId, Long prevReadMessageInfoId,
			Long lastReadMessageInfoId) {
		if( prevReadMessageInfoId == null ){
			String hqlUpdate = "update MessageInfo messageInfo set unreadCount = unreadCount - 1 where unreadCount > 0 and channel.id = :channelId and id <= :lastReadMessageInfoId";
			Query query = getCurrentSession().createQuery( hqlUpdate ).setLong( "channelId", channelId).setLong( "lastReadMessageInfoId", lastReadMessageInfoId );
			query.executeUpdate();
			getCurrentSession().clear();
		}else{
			String hqlUpdate = "update MessageInfo messageInfo set unreadCount = unreadCount - 1 where unreadCount > 0 and channel.id = :channelId and id > :prevReadMessageInfoId and id <= :lastReadMessageInfoId";
			Query query = getCurrentSession().createQuery( hqlUpdate ).setLong( "channelId", channelId).setLong( "prevReadMessageInfoId", prevReadMessageInfoId ).setLong( "lastReadMessageInfoId", lastReadMessageInfoId );
			query.executeUpdate();
			getCurrentSession().clear();
		}
		
		
		
		
	}

	public MessageInfo getLastFromChannel(Long channelId) {
		
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("channel.id", channelId));
		criteria.addOrder(Order.desc("id"));
		criteria.setMaxResults(1);
		return (MessageInfo) criteria.uniqueResult();
	}

}
