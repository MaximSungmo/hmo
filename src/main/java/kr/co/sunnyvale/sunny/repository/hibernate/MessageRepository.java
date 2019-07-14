package kr.co.sunnyvale.sunny.repository.hibernate;

import java.util.Date;
import java.util.List;

import kr.co.sunnyvale.sunny.domain.Message;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.extend.Stream;

import org.hibernate.Criteria;
import org.hibernate.Query;
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

@Repository(value = "messageRepository")
public class MessageRepository extends HibernateGenericRepository<Message>{
	public MessageRepository(){

	}

	public List<Message> getMessages(User user, Long channelId, Long messageInfoId, Stream stream) {
		Criteria criteria = getCriteria();
//		if( unread != null ){
//			criteria.add(Restrictions.eq("isRead", unread));
//		}
		criteria.add(Restrictions.eq("isHidden", false));
		criteria.add(Restrictions.eq("channel.id", channelId));
		criteria.add(Restrictions.eq("user", user));
		criteria.createAlias("info", "infoAlias");
		if( messageInfoId != null ){
			criteria.add(Restrictions.gt("infoAlias.id", messageInfoId));
		}
		
		criteria.addOrder(Order.desc("infoAlias.id"));
		
		return getPagedList(criteria, stream);
	}

	public Date getCreateDate(Long messageId) {
		Criteria criteria =  getCriteria();
		criteria.add(Restrictions.eq("id", messageId));
		criteria.createAlias("info", "infoAlias");
		criteria.setProjection(	Projections.projectionList().add( Projections.property("infoAlias.createDate") ));
		
		return (Date) criteria.uniqueResult();

	}

	public Message getLastMessage(Long channelId) {
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("channel.id", channelId));
		criteria.setMaxResults(1);
		criteria.addOrder(Order.desc("id"));
		
		return (Message) criteria.uniqueResult();
	}



	public Long getLastMessageInfoId(Long channelId) {
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("channel.id", channelId));
		criteria.setMaxResults(1);
		criteria.addOrder(Order.desc("id"));
		criteria.createAlias("info", "infoAlias");
		criteria.setProjection(Projections.property("infoAlias.id"));
		
		return (Long) criteria.uniqueResult();
	}

	
	public Long getNextFirstMessage(Date startDate) {
		Criteria criteria = getCriteria();
		criteria.createAlias("info", "infoAlias");
		criteria.add(Restrictions.ge("infoAlias.createDate", startDate));
		criteria.setMaxResults(1);
		criteria.setProjection(Projections.property("infoAlias.id"));
		return (Long) criteria.uniqueResult();
		
	}

	public Long getPrevLastMessage(Date startDate) {
		Criteria criteria = getCriteria();
		criteria.createAlias("info", "infoAlias");
		criteria.add(Restrictions.lt("infoAlias.createDate", startDate));
		criteria.setMaxResults(1);
		criteria.setProjection(Projections.property("infoAlias.id"));
		return (Long) criteria.uniqueResult();
	}

	
	public void removeChannelMessages(Long channelId, Long userId) {
			String hqlUpdate = "delete from Message message where channel.id = :channelId and user.id = :userId";
			Query query = getCurrentSession().createQuery( hqlUpdate ).setLong( "channelId", channelId).setLong( "userId", userId );
			query.executeUpdate();
			getCurrentSession().clear();
		
	}

}
