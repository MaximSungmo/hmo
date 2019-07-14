package kr.co.sunnyvale.sunny.repository.hibernate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kr.co.sunnyvale.sunny.domain.ActivityType;
import kr.co.sunnyvale.sunny.domain.Content;
import kr.co.sunnyvale.sunny.domain.Notification;
import kr.co.sunnyvale.sunny.domain.ReceiverRelation;
import kr.co.sunnyvale.sunny.domain.Reply;
import kr.co.sunnyvale.sunny.domain.User;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
/**
 * {Hibernate}
 *	
 *<p>
 *	유저 정보를 관리하는 Repository
 *
 * @author mook
 *
 *	
 *	
 *
 */
@Repository(value = "receiverRelationRepository")
public class ReceiverRelationRepository extends HibernateGenericRepository<ReceiverRelation>{
	public ReceiverRelationRepository(){

	}

	public ReceiverRelation find(Notification notification, User receiver) {
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("notification", notification));
		criteria.add(Restrictions.eq("user", receiver));
		criteria.setMaxResults(1);
		return (ReceiverRelation) criteria.uniqueResult();
	}

//	public Number getNotificationUnreadCount(User user, Date lastRead) {
//		Criteria criteria = getCriteria();
//		criteria.createAlias( "notification", "notiAlias" );
//		criteria.add(Restrictions.eq("user", user));
//		criteria.add(Restrictions.ne("notiAlias.activityType", ActivityType.FOLLOW));
//		
//		if( lastRead != null )
//			criteria.add(Restrictions.gt("updateDate", lastRead));
//		
//		criteria.setProjection(Projections.rowCount());
//		return (Number)criteria.uniqueResult();
//	}
//
//	public List<ReceiverRelation> getNotifications(User user, Stream stream) {
//		Criteria criteria = getCriteria();
//		criteria.createAlias("notification", "notiAlias");
//		criteria.add(Restrictions.eq("user.id", user.getId()));
//		criteria.add(Restrictions.ne("notiAlias.activityType", ActivityType.FOLLOW));
//		criteria.addOrder(Order.desc("updateDate"));
//		return getPagedList(criteria, stream);
//	}

//	public void requestReceiverCheckUpdate(Notification notification, User user) {
//		String hqlUpdate = "update ReceiverRelation relation set updateDate = :currentDate where notification.id = :notificationId and user.id != :userId";
//		// or String hqlUpdate = "update Customer set name = :newName where name = :oldName";
//		int updatedEntities = getCurrentSession().createQuery( hqlUpdate )
//				.setLong( "notificationId", notification.getId() )
//		        .setString( "userId", user.getId() )
//		        .setTimestamp("currentDate", new Date() )
//		        .executeUpdate();
//		getCurrentSession().clear();
//		
//	}

	public Set<String> getUserIdsFromReplyNoti(Reply reply) {
		Criteria criteria = getCriteria();
//		criteria.createAlias("notification", "notiAlias");
//		criteria.add(Restrictions.eq("notiAlias.activityType", ActivityType.COMMENT));
//		criteria.add(Restrictions.eq("notiAlias.parentContentId", reply.getContent().getId()));
	//	criteria.add(Restrictions.ne("user", reply.getUser()));
		criteria.add(Restrictions.eq("content", reply.getContent()));
		criteria.add(Restrictions.eq("activityType", ActivityType.COMMENT));
		criteria.add(Restrictions.ne("user", reply.getUser()));
		criteria.setProjection( Projections.property("user.id"));
		
		return new HashSet(criteria.list());
		
	}

	public List<ReceiverRelation> getContentReceiver(Content content,
			int activityType) {
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("content", content));
		criteria.add(Restrictions.eq("activityType", activityType));
		return criteria.list();
	}

//	public List<ReceiverRelation> getMatchRelation(String[] queries, String userId, Stream stream) {
//		Criteria criteria = getCriteria();
//		criteria.createAlias("group", "groupAlias");
//		
//		if( queries != null ){
//			for( String query : queries ){
//				criteria.add(Restrictions.like("groupAlias.title", query, MatchMode.ANYWHERE));	
//			}
//		}
//		criteria.add(Restrictions.eq("user.id", userId));
//		
//		return getPagedList(criteria, stream);
//	}
//
//	public List<GroupUser> getJoinUsers(Long groupId, Stream stream) {
//		Criteria criteria = getCriteria();
//		criteria.createAlias("user", "userAlias");
//		criteria.add(Restrictions.eq("group.id", groupId));
//		return getPagedList(criteria, stream);
//	}
//
//	public GroupUser findRelation(Long groupId, String userId) {
//
//		Criteria criteria = getCriteria();
//		criteria.createAlias("user", "userAlias");
//		criteria.add(Restrictions.eq("group.id", groupId));
//		criteria.add(Restrictions.eq("user.id", userId));
//		criteria.setMaxResults(1);
//		return (GroupUser) criteria.uniqueResult();
//	}
//
//	public GroupUser findAdminRelation(Long groupId, String userId) {
//		Criteria criteria = getCriteria();
//		criteria.createAlias("user", "userAlias");
//		criteria.add(Restrictions.eq("group.id", groupId));
//		criteria.add(Restrictions.eq("user.id", userId));
//		criteria.add(Restrictions.eq("isAdmin", true));
//		criteria.setMaxResults(1);
//		return (GroupUser) criteria.uniqueResult();
//	}
//
//	public List<GroupUser> getGroupUsers(Long id, Stream stream) {
//
//		Criteria criteria = getCriteria();
//		
//		criteria.createAlias("user", "userAlias", Criteria.LEFT_JOIN);
//		criteria.add(Restrictions.eq("group.id", id));
//		
//		return getPagedList(criteria, stream);
//	}
//

}
