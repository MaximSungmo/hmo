package kr.co.sunnyvale.sunny.service.impl;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.ActivityType;
import kr.co.sunnyvale.sunny.domain.Approval;
import kr.co.sunnyvale.sunny.domain.Content;
import kr.co.sunnyvale.sunny.domain.Feel;
import kr.co.sunnyvale.sunny.domain.Reply;
import kr.co.sunnyvale.sunny.domain.SiteInactiveUser;
import kr.co.sunnyvale.sunny.domain.SmallGroup;
import kr.co.sunnyvale.sunny.domain.Story;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.UserSmallGroupAccess;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.redis.RedisPublisher;
import kr.co.sunnyvale.sunny.redis.notify.FriendRequestNotify;
import kr.co.sunnyvale.sunny.redis.notify.NotiNotify;
import kr.co.sunnyvale.sunny.redis.notify.NoticeNotify;
import kr.co.sunnyvale.sunny.repository.hibernate.UserRepository;
import kr.co.sunnyvale.sunny.service.AfterService;
import kr.co.sunnyvale.sunny.service.NotificationService;
import kr.co.sunnyvale.sunny.service.ReceiverRelationService;
import kr.co.sunnyvale.sunny.service.SiteService;
import kr.co.sunnyvale.sunny.service.UserService;
import kr.co.sunnyvale.sunny.service.UserSmallGroupAccessService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( value="afterService" )
@Transactional
public class AfterServiceImpl implements AfterService {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository; 
//	
//	@Autowired
//	private ActivityService activityService;
//	
	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private ReceiverRelationService receiverRelationService;

	@Autowired
	private RedisPublisher redisPublisher;
	
	@Autowired
	private SiteService siteService;
	
	@Autowired
	private UserSmallGroupAccessService userSmallGroupAccessService;
	
	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.AfterService#follow(kr.co.sunnyvale.sunny.domain.User, kr.co.sunnyvale.sunny.domain.User)
	 */
	@Override
	@Transactional
	public void friendRequest(Sunny sunny, User followed, User follower) {
//		activityService.follow( following, follower  );
		notificationService.friendRequest( sunny, followed, follower );
		redisPublisher.publish(new FriendRequestNotify( ActivityType.FRIEND_REQUEST, followed.getId()));
		
	}

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.AfterService#feel(kr.co.sunnyvale.sunny.domain.extend.Sunny, kr.co.sunnyvale.sunny.domain.User, kr.co.sunnyvale.sunny.domain.Content, kr.co.sunnyvale.sunny.domain.Feel)
	 */
	@Override
	@Transactional
	public void feel(Sunny sunny, User user, Content content, Feel feel) {
//		activityService.feel(user, content, feel);
		
		notificationService.feel(sunny, user, content, feel);

		redisPublisher.publish(new NotiNotify(user.getId(), content.getId(), ActivityType.EVALUATE, content.getUser().getId()));
//		if( content.getType() == ContentType.STORY ||
//				content.getType() == ContentType.NOTE ||
//				content.getType() == ContentType.QUESTION ){
//			notificationService.feelParent(user, content, feel);
//			
//		}else if( content.getType() == ContentType.COMMENT ){
//			notificationService.feelReply(user, content, feel);		
//		}else if( content.getType() == ContentType.ANSWER ){
//			notificationService.feelAnswer(user, content, feel);	
//		}else{
//			throw new RuntimeException(messageSource.getMessage("error.notification.feel.type", new String[]{ "알수없는컨텐츠타입입니다."}, Locale.getDefault()));
//		}
	}

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.AfterService#unFeel(kr.co.sunnyvale.sunny.domain.User, kr.co.sunnyvale.sunny.domain.Content, kr.co.sunnyvale.sunny.domain.Feel)
	 */
	@Override
	@Transactional
	public void unFeel(User user, Content content, Feel alreadyFeel) {
	//	activityService.unFeel(user, content, alreadyFeel);
		notificationService.unFeel( user, content, alreadyFeel );
	}

	@Override
	@Transactional
	public void requestApprove(Sunny sunny, Approval approval,
			SmallGroup smallGroup) {
		
		List<UserSmallGroupAccess> userSmallGroupAccesses = userSmallGroupAccessService.getJoined(sunny, smallGroup, null, null, null, null, null);
		Long[] userIds = new Long[userSmallGroupAccesses.size()];
		int i = 0;
		for( UserSmallGroupAccess usga : userSmallGroupAccesses){
			System.out.println(i + " 는 " +  usga.getUser().getId());
			userIds[i++] = usga.getUser().getId(); 
		}

		notificationService.requesetApprove(sunny, approval, userIds);
		redisPublisher.publish(new NotiNotify(approval.getUser().getId(), approval.getId(), ActivityType.REQUEST_APPROVAL, userIds));
		
	}

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.AfterService#reply(kr.co.sunnyvale.sunny.domain.Reply)
	 */
	@Override
	@Transactional
	public void reply(Sunny sunny, Reply reply) {
//		activityService.reply(reply.getUser(), reply.getContent(), reply);
		notificationService.reply(sunny, reply);
	}


	@Override
	public void requestSignup(Sunny sunny, SiteInactiveUser siteInactiveUser) {
		
		List<Long> siteAdminIds = userRepository.getSiteAdminIds( siteInactiveUser.getSite().getId() );
		
		Long[] userIds = siteAdminIds.toArray(new Long[siteAdminIds.size()]);

		notificationService.requesetSignup(sunny, siteInactiveUser, userIds);
		redisPublisher.publish(new NotiNotify(siteInactiveUser.getId(), null, ActivityType.REQUEST_SIGNUP, userIds));
		
	}


	@Override
	@Transactional
	public void approveApprobatorComplete(Sunny sunny, Approval approval) {
		Long[] receiverIds = notificationService.approveApprobatorComplete(sunny, approval);
		redisPublisher.publish(new NotiNotify(approval.getUser().getId(), approval.getId(), ActivityType.APPROVAL_APPROBATOR_COMPLETE, receiverIds));
//		redisPublisher.publish(new NotiNotify(ActivityType.APPROVAL_APPROBATOR_COMPLETE, receiverIds));
	}

	@Override
	@Transactional
	public void approveCooperationComplete(Sunny sunny, Approval approval) {
		Long[] receiverIds = notificationService.approveCooperationComplete(sunny, approval);
		redisPublisher.publish(new NotiNotify(approval.getUser().getId(), approval.getId(), ActivityType.APPROVAL_COOPERATION_COMPLETE, receiverIds));
//		redisPublisher.publish(new NotiNotify(ActivityType.APPROVAL_COOPERATION_COMPLETE, receiverIds));
	}
	@Override
	@Transactional
	public void approveReject(Sunny sunny, Long rejectorId, Approval approval) {
		Long[] receiverIds = notificationService.approveReject( sunny, rejectorId, approval );
		redisPublisher.publish(new NotiNotify(rejectorId, approval.getId(), ActivityType.APPROVAL_REJECT, receiverIds));
//		redisPublisher.publish(new NotiNotify(ActivityType.APPROVAL_REJECT, receiverIds));
	}
	

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.AfterService#story(kr.co.sunnyvale.sunny.domain.Story)
	 */
	@Override
	@Transactional
	public void story(Sunny sunny, Story story) {
		//receiverRelationService.addReceiver( notification, story.getUser() );
	}

	@Override
	public void notice(Sunny sunny, Long storyId,  Long userId) {
		List<Long> allUserIds = userService.findAllJoinedUsers( sunny.getSite().getId(), userId );
		
		Long[] allUserIdArrays = allUserIds.toArray( new Long[allUserIds.size()] );
		//for( Long eachUserId : allUserIds ){
			redisPublisher.publish(new NoticeNotify(userId, storyId, allUserIdArrays ));
		//}
	}



}
