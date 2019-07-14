package kr.co.sunnyvale.sunny.repository.hibernate;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import kr.co.sunnyvale.sunny.domain.ActivityType;
import kr.co.sunnyvale.sunny.domain.Approval;
import kr.co.sunnyvale.sunny.domain.Content;
import kr.co.sunnyvale.sunny.domain.LastRead;
import kr.co.sunnyvale.sunny.domain.Notification;
import kr.co.sunnyvale.sunny.domain.Question;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.dto.NotifyInfoDTO;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.util.DateUtils;

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

@Repository(value = "notificationRepository")
public class NotificationRepository extends HibernateGenericRepository<Notification>{
	public NotificationRepository(){

	}

	public Number getNotificationUnreadCount(User user, Date lastRead) {
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("receiver", user));
		criteria.add(Restrictions.ne("activityType", ActivityType.FRIEND_REQUEST));
		criteria.add(Restrictions.eq("read", false));
		if( lastRead != null )
			criteria.add(Restrictions.gt("updateDate", lastRead));

		criteria.setProjection(Projections.rowCount());		
		return (Number)criteria.uniqueResult();
	}

	//	public List<Notification> getNotifications(User user, Stream stream) {
	//		Criteria criteria = getCriteria();
	//		criteria.createAlias("receiverRelations", "receiverAlias");
	//		criteria.add(Restrictions.eq("receiverAlias.user.id", user.getId()));
	//		criteria.add(Restrictions.ne("activityType", ActivityType.FOLLOW));
	//		criteria.addOrder(Order.desc("updateDate"));
	//		return getPagedList(criteria, stream);
	//	}

	public List<Notification> getFollowNotifications(User user, Stream stream) {
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("receiver.id", user.getId()));
		criteria.add(Restrictions.eq("activityType", ActivityType.FRIEND_REQUEST));	
		criteria.addOrder(Order.desc("updateDate"));	
		return getPagedList(criteria, stream);
	}


	public Notification findRequestSignup(Long userId, String email,
			int dayAgo) {

		Criteria criteria = getCriteria();

		criteria.add(Restrictions.eq("activityType", ActivityType.REQUEST_SIGNUP));
		criteria.add(Restrictions.eq("receiver.id", userId));
		criteria.add(Restrictions.eq("activatorEmail", email));
		criteria.add(Restrictions.gt("createDate", DateUtils.prevDateByDay(dayAgo)));

		criteria.addOrder(Order.desc("createDate"));
		List<Notification> notifications = criteria.list();

		if( notifications == null || notifications.size() == 0 )
			return null;

		return (Notification) notifications.get(0);

	}



	public Notification findFeed(User feeder, User reader, int dayAgo) {
		Criteria criteria = getCriteria();

		criteria.add(Restrictions.eq("activityType", ActivityType.FEED));
		criteria.add(Restrictions.eq("receiver.id", feeder.getId()));
		criteria.add(Restrictions.eq("activatorId", reader.getId()));
		criteria.add(Restrictions.gt("createDate", DateUtils.prevDateByDay(dayAgo)));

		return (Notification) criteria.uniqueResult();
	}

	public Notification findFollow(User followedUser, User followerUser,
			int dayAgo) {

		Criteria criteria = getCriteria();

		criteria.add(Restrictions.eq("activityType", ActivityType.FRIEND_REQUEST));
		criteria.add(Restrictions.eq("receiver.id", followedUser.getId()));
		criteria.add(Restrictions.eq("activatorId", followerUser.getId()));
		criteria.add(Restrictions.gt("createDate", DateUtils.prevDateByDay(dayAgo)));

		return (Notification) criteria.uniqueResult();
	}

	public Notification findFeel(Content content/*, Integer feelId*/, int dayAgo) {

		Criteria criteria = getCriteria();

		criteria.add(Restrictions.eq("activityType", ActivityType.EVALUATE));
		criteria.add(Restrictions.eq("targetContentId", content.getId()));
		//		criteria.add(Restrictions.eq("feelId", feelId));
		criteria.add(Restrictions.gt("createDate", DateUtils.prevDateByDay(dayAgo)));
		criteria.setMaxResults(1);
		return (Notification) criteria.uniqueResult();
	}

	public Notification findApproval(Approval approval, int dayAgo) {
		Criteria criteria = getCriteria();

		criteria.add(Restrictions.eq("activityType", ActivityType.REQUEST_APPROVAL));
		criteria.add(Restrictions.eq("targetContentId", approval.getId()));
		//		criteria.add(Restrictions.eq("feelId", feelId));
		criteria.add(Restrictions.gt("createDate", DateUtils.prevDateByDay(dayAgo)));
		criteria.setMaxResults(1);
		return (Notification) criteria.uniqueResult();
	}



	public Notification findApprovalReject(Approval approval, User user,
			int dayAgo) {
		Criteria criteria = getCriteria();

		criteria.add(Restrictions.eq("activityType", ActivityType.APPROVAL_REJECT));
		criteria.add(Restrictions.eq("targetContentId", approval.getId()));
		//		criteria.add(Restrictions.eq("feelId", feelId));
		criteria.add(Restrictions.gt("createDate", DateUtils.prevDateByDay(dayAgo)));
		criteria.setMaxResults(1);
		return (Notification) criteria.uniqueResult();
	}
	public Notification findReplyFeel(Content content,
			int dayAgo) {

		Criteria criteria = getCriteria();

		criteria.add(Restrictions.eq("activityType", ActivityType.COMMENT_EVALUATE));
		criteria.add(Restrictions.eq("targetContentId", content.getId()));
		criteria.add(Restrictions.gt("createDate", DateUtils.prevDateByDay(dayAgo)));

		return (Notification) criteria.uniqueResult();
	}

	public Notification findMentionReply(Content content, Long receiverId, int dayAgo) {

		Criteria criteria = getCriteria();

		criteria.add(Restrictions.eq("activityType", ActivityType.MENTION_REPLY));
		criteria.add(Restrictions.eq("parentContentId", content.getId()));
		criteria.add(Restrictions.eq("receiver.id", receiverId));
		criteria.add(Restrictions.gt("createDate", DateUtils.prevDateByDay(dayAgo)));
		criteria.setMaxResults(1);
		return (Notification) criteria.uniqueResult();

	}



	//	public Notification findAnswerFeel(Content content, int dayAgo) {
	//		Criteria criteria = getCriteria();
	//		
	//		criteria.add(Restrictions.eq("activityType", ActivityType.ANSWER_EVALUATE));
	//		criteria.add(Restrictions.eq("targetContentId", content.getId()));
	//		criteria.add(Restrictions.gt("createDate", DateUtils.prevDateByDay(dayAgo)));
	//		
	//		return (Notification) criteria.uniqueResult();
	//	}

	public Notification findContentReply(Content content, User receiver, int dayAgo) {
		Criteria criteria = getCriteria();

		criteria.add(Restrictions.eq("activityType", ActivityType.COMMENT));
		criteria.add(Restrictions.eq("parentContentId", content.getId()));
		criteria.add(Restrictions.eq("receiver", receiver));
		criteria.add(Restrictions.gt("createDate", DateUtils.prevDateByDay(dayAgo)));
		criteria.setMaxResults(1);
		return (Notification) criteria.uniqueResult();
	}

	//	public Notification findReplyReply(User user, Content parentContent, int dayAgo) {
	//		Criteria criteria = getCriteria();
	//		/*
	//		 * 스토리A에서 B가 쓴 댓글 뒤에 C라는 사람이 댓글을 썼다고 가정.
	//		 * A란 스토리 중 내 위에 글쓴 사람인 receiver 가 이미 노티를 받았는지 확인한 뒤 받았으면 update 하는 부분이다.
	//		 */
	//		criteria.add(Restrictions.eq("activityType", ActivityType.COMMENT_COMMENT));
	//		criteria.add(Restrictions.eq("parentContentId", parentContent.getId()));
	//		criteria.add(Restrictions.eq("receiverId", user.getId()));
	//		criteria.add(Restrictions.gt("createDate", DateUtils.prevDateByDay(dayAgo)));
	//		criteria.setMaxResults(1);
	//		return (Notification) criteria.uniqueResult();
	//	}

	//	public Notification findAnswer(Answer answer, int dayAgo) {
	//		Criteria criteria = getCriteria();
	//		
	//		criteria.add(Restrictions.eq("activityType", ActivityType.ANSWER));
	//		criteria.add(Restrictions.eq("targetContentId", answer.getQuestion().getId()));
	//		criteria.add(Restrictions.gt("createDate", DateUtils.prevDateByDay(dayAgo)));
	//		
	//		return (Notification) criteria.uniqueResult();
	//	}


	public Notification findQuestionAnswer(Question question, User user,
			int dayAgo) {
		Criteria criteria = getCriteria();

		criteria.add(Restrictions.eq("activityType", ActivityType.ANSWER));
		criteria.add(Restrictions.eq("targetContentId", question.getId()));
		criteria.add(Restrictions.eq("receiver", user));
		criteria.add(Restrictions.gt("createDate", DateUtils.prevDateByDay(dayAgo)));

		return (Notification) criteria.uniqueResult();
	}
	public Serializable getUpdateDate(Long notiId) {
		Criteria criteria =  getCriteria();
		criteria.add(Restrictions.eq("id", notiId));
		criteria.setProjection(	Projections.projectionList().add( Projections.property("updateDate") ));

		return (Date) criteria.uniqueResult();
	}

	public List<Notification> getUsersList(User user, Stream stream) {
		Criteria criteria = getCriteria();

		criteria.add(Restrictions.eq("receiver", user));
		criteria.add(Restrictions.ne("activityType", ActivityType.FRIEND_REQUEST));
		criteria.addOrder(Order.desc("updateDate"));

		return getPagedList(criteria, stream);
	}
	public Page<Notification> getUsersPagedList(User user, Integer page,
			int pageSize) {
		Criteria criteria = getCriteria();

		criteria.add(Restrictions.eq("receiver", user));
		criteria.add(Restrictions.ne("activityType", ActivityType.FRIEND_REQUEST));
		criteria.addOrder(Order.desc("updateDate"));
		
		return getPagedList(criteria, page, pageSize);
	}
	

	public Notification getNewOne(User user, LastRead lastRead) {
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("receiver", user));
		criteria.add(Restrictions.ne("activityType", ActivityType.FRIEND_REQUEST));
		criteria.add(Restrictions.eq("read", false));

		if( lastRead != null )
			criteria.add(Restrictions.gt("updateDate", lastRead.getNotificationLastRead()));

		criteria.addOrder(Order.desc("updateDate"));
		criteria.setMaxResults(1);		
		return (Notification) criteria.uniqueResult();
	}


	public NotifyInfoDTO getNotifyInfo(Long userId, LastRead lastRead) {

		NotifyInfoDTO notifyInfoDto = new NotifyInfoDTO();		

		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("receiver.id", userId));
		criteria.add(Restrictions.ne("activityType", ActivityType.FRIEND_REQUEST));
		criteria.add(Restrictions.eq("read", false));
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
			criteria.add(Restrictions.gt("updateDate", lastRead.getNotificationLastRead()));


		criteria.setProjection(Projections.rowCount());
		Object count = criteria.uniqueResult();

		if( count == null ){
			notifyInfoDto.setUnreadCount(0);
		}else{
			notifyInfoDto.setUnreadCount( ((Number) count).intValue() );
		}

		return notifyInfoDto; 
	}

	public void makeReadAssociatedContents(Long contentId, User user) {
		String hqlUpdate = "update Notification notification set read = true where parentContentId = :parentContentId and receiver.id = :receiverId ";
		Query query = getCurrentSession().createQuery( hqlUpdate ).setLong( "parentContentId", contentId).setLong( "receiverId", user.getId() );
		query.executeUpdate();
		getCurrentSession().clear();
	}

	




}
