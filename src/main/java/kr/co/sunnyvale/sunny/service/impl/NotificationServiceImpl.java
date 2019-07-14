package kr.co.sunnyvale.sunny.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.co.sunnyvale.sunny.domain.ActivatorRelation;
import kr.co.sunnyvale.sunny.domain.ActivityType;
import kr.co.sunnyvale.sunny.domain.Approval;
import kr.co.sunnyvale.sunny.domain.Content;
import kr.co.sunnyvale.sunny.domain.Feel;
import kr.co.sunnyvale.sunny.domain.LastRead;
import kr.co.sunnyvale.sunny.domain.Notification;
import kr.co.sunnyvale.sunny.domain.ReceiverRelation;
import kr.co.sunnyvale.sunny.domain.Reply;
import kr.co.sunnyvale.sunny.domain.SiteInactiveUser;
import kr.co.sunnyvale.sunny.domain.SmallGroup;
import kr.co.sunnyvale.sunny.domain.Story;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.dto.NotifyInfoDTO;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.exception.SimpleSunnyException;
import kr.co.sunnyvale.sunny.redis.RedisPublisher;
import kr.co.sunnyvale.sunny.redis.notify.NotiNotify;
import kr.co.sunnyvale.sunny.repository.hibernate.ContentService;
import kr.co.sunnyvale.sunny.repository.hibernate.LastReadRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.NotificationRepository;
import kr.co.sunnyvale.sunny.service.NotificationService;
import kr.co.sunnyvale.sunny.service.ReceiverRelationService;
import kr.co.sunnyvale.sunny.service.ReplyService;
import kr.co.sunnyvale.sunny.service.SiteService;
import kr.co.sunnyvale.sunny.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( value="notificationService" )
@Transactional
public class NotificationServiceImpl implements NotificationService{
	
	// 피드가 합쳐지는 기준 날
	public static int DAY_AGO_FEED = 1;
	public static int DAY_AGO_EVALUATE = 1;
	public static int DAY_AGO_COMMENT_COMMENT = 1;
	public static int DAY_AGO_ANSWER = 1;
	public static int DEFAULT_RECEIVER_COUNT = 5;
	public static int DEFAULT_ACTIVATOR_COUNT = 3;
	
	
	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
	private ReplyService replyService; 
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private LastReadRepository lastReadRepository;
	
	@Autowired
	private ActivatorRelationServiceImpl activatorRelationService;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private ContentService contentService;
	
	@Autowired
	private ReceiverRelationService receiverRelationService;

    @Autowired
    private RedisPublisher redisPublisher;
    
    @Autowired
    private SiteService siteService;
	
	
	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.NotificationService#feed(kr.co.sunnyvale.sunny.domain.User, kr.co.sunnyvale.sunny.domain.User)
	 */
	@Override
	@Transactional
	public void feed(Sunny sunny, User feeder, User reader) {
		
		Notification notification = notificationRepository.findFeed(feeder, reader, DAY_AGO_FEED);
		
		if( notification != null ){
			notification.setUpdateDate( new Date() );
			notification.setRead(false);
			notificationRepository.update(notification);
			return;
		}

		saveUserRelationNoti( sunny, feeder , reader , ActivityType.FEED );
		
	}

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.NotificationService#follow(kr.co.sunnyvale.sunny.domain.User, kr.co.sunnyvale.sunny.domain.User)
	 */
	@Override
	@Transactional
	public void  friendRequest(Sunny sunny, User followedUser, User followerUser){

		Notification notification = notificationRepository.findFollow(followedUser, followerUser, DAY_AGO_FEED);
		
		if( notification != null ){
			notification.setUpdateDate( new Date());
			notification.setRead(false);
			notificationRepository.update(notification);
			return;
		}

		saveUserRelationNoti( sunny, followedUser , followerUser , ActivityType.FRIEND_REQUEST );
		
	}


	@Override
	@Transactional
	public void requesetSignup(Sunny sunny, SiteInactiveUser siteInactiveUser,
			Long[] userIds) {
		
		for( Long userId : userIds ){
			Notification notification = notificationRepository.findRequestSignup(userId, siteInactiveUser.getEmail(), DAY_AGO_FEED);
			
			if( notification != null ){
				notification.setUpdateDate(new Date());
				notification.setRead(false);
				notificationRepository.update(notification);
			}
			
			saveRequestSignupNoti( sunny, userId, siteInactiveUser, ActivityType.REQUEST_SIGNUP);
		}
		
		
	}
	
	@Transactional
	private void saveRequestSignupNoti(Sunny sunny, Long userId,
			SiteInactiveUser siteInactiveUser, int activityType) {

		Notification notification = new Notification();
		notification.setActivityType(activityType);
		notification.setActivator(siteInactiveUser);
		notification.setReceiver(new User( userId ));
//		notification.setSiteId(sunny.getSite().getId());
		notificationRepository.save(notification);
		
	}

	@Transactional
	private void saveUserRelationNoti( Sunny sunny, User receiver, User activator, int activityType ){

		Notification notification = new Notification();
		notification.setActivityType(activityType);
		notification.setActivator(activator);
		notification.setReceiver(receiver);
//		notification.setSiteId(sunny.getSite().getId());
		notificationRepository.save(notification);

//		receiverRelationService.addReceiver( notification, receiver );
	}
	
	
	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.NotificationService#feel(kr.co.sunnyvale.sunny.domain.User, kr.co.sunnyvale.sunny.domain.Content, org.apache.velocity.runtime.directive.Feel)
	 */
	@Override
	@Transactional
	public void feel( Sunny sunny, User activator, Content content, Feel feel ){
		
//		내가 내 글을 평가했을 경우엔 따로 알림을 보내지 않는다.		
		if( activator.getId().equals( content.getUser().getId() ) ){
			return; 
		}
		
		Notification notification = null;
		if( content.getType() == Content.TYPE_REPLY){
			notification = notificationRepository.findReplyFeel( content , DAY_AGO_EVALUATE );
		}else{
			notification = notificationRepository.findFeel(content, /*feel.getId(), */DAY_AGO_EVALUATE);
		}
		
		if( notification != null ){
			updateActivator( notification, activator );
			return;
		}
		
		notification = new Notification();
		notification.setActivityType( content.getType() == kr.co.sunnyvale.sunny.domain.Content.TYPE_REPLY ? ActivityType.COMMENT_EVALUATE : ActivityType.EVALUATE );
		
		notification.addActivatorCount();
		notification.generateActivator(activator);
		
		notification.setTargetContent( content );
//		notification.setSiteId( content.getSite().getId() );
		
		/*
		 * Reply 를 평가했을 때는 Parent 는 부모 컨텐츠(Story, Question 등)가 되고 Target 은 해당 Reply 가 된다.
		 * 나머지는 Parent 와 Target 이 동일
		 */
		if( kr.co.sunnyvale.sunny.domain.Content.TYPE_REPLY == content.getType() ){
			Reply reply = replyService.select( content.getId() );
			notification.setParentContent( reply.getContent() );
			notification.setContent( reply.getContent() );
			notification.setOwner( reply.getContent().getUser() );
		}else{
			notification.setContent( content );
			notification.setParentContent( content );
		}
		
		notification.setReceiver( content.getUser() );
		
		notification.setSnippetText( notification.getTargetContentSnippet() );
		notificationRepository.save(notification);
		activatorRelationService.addActivator( notification, activator );
//		receiverRelationService.addReceiver( notification, content.getUser() );
	}
	

	@Override
	@Transactional
	public void requesetApprove(Sunny sunny,  Approval approval,
			Long ... userIds ) {
		
		User user = userService.findById(approval.getUser().getId());
		
		for( Long userId : userIds ){
		
			Notification notification = new Notification();
			notification.setActivityType( ActivityType.REQUEST_APPROVAL);
			
			notification.addActivatorCount();
			notification.generateActivator(user);
			
			notification.setTargetContent( approval );
	
			
			/*
			 * Reply 를 평가했을 때는 Parent 는 부모 컨텐츠(Story, Question 등)가 되고 Target 은 해당 Reply 가 된다.
			 * 나머지는 Parent 와 Target 이 동일
			 */
			notification.setContent( approval );
			notification.setParentContent( approval );
	
			notification.setReceiver( new User(userId) );
//			notification.setSiteId(sunny.getSite().getId());
			notification.setSnippetText( approval.getTitle() );
			notificationRepository.save(notification);
			activatorRelationService.addActivator( notification, user);
		}
	}


	
	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.NotificationService#unFeel(kr.co.sunnyvale.sunny.domain.User, kr.co.sunnyvale.sunny.domain.Content, org.apache.velocity.runtime.directive.Feel)
	 */
	@Override
	@Transactional
	public void unFeel(User user, Content content, Feel alreadyFeel) {
		Notification notification  = notificationRepository.findFeel(content, /*alreadyFeel.getId(), */DAY_AGO_EVALUATE);
		if( notification != null ){
			ActivatorRelation activatorRelation = activatorRelationService.find( notification, user );
					
			if( activatorRelation != null  ){
				
				//throw new RuntimeException(messageSource.getMessage("error.already.activated", new String[]{ "잘못된 접근입니다. 이전의 이 행동을 하셨는데 반영이 안되어있네요."}, Locale.getDefault()));
				activatorRelationService.removeActivator( activatorRelation );
				notification.minusActivatorCount();
				notification.regenerateActivators( activatorRelationService.getCurrentActivators(notification, DEFAULT_ACTIVATOR_COUNT));
				notificationRepository.update(notification);
				return;
			}
		}
	}
	@Override
	@Transactional
	public Long[] approveApprobatorComplete(Sunny sunny, Approval approval) {

		List<ReceiverRelation> receivers = receiverRelationService.getContentsReceiver( approval, ActivityType.APPROVAL_APPROBATOR_COMPLETE );
		
		if( receivers == null || receivers.size() == 0 ){
			ReceiverRelation receiverRelation = new ReceiverRelation();
			receiverRelation.setActivityType( ActivityType.APPROVAL_APPROBATOR_COMPLETE );
			receiverRelation.setContent(approval);
			receiverRelation.setUser( approval.getUser() );
			receiverRelationService.save( receiverRelation );
			receivers = new ArrayList<ReceiverRelation>();
			receivers.add(receiverRelation);
		}
		
		
		ReceiverRelation myReceiverRelation = null;


		/*
		 * 팔로우하고 있는 사람을 순회하며 노티를 하나씩 생성해서 보낸다. 
		 */
		for( ReceiverRelation receiver : receivers ){
			
			if( receiver.getUser().equals( approval.getUser() )){
				myReceiverRelation = receiver;
			}
			
			Notification notification = new Notification();
			
			notification.setActivityType( ActivityType.APPROVAL_APPROBATOR_COMPLETE );
			
			notification.addActivatorCount();
			notification.generateActivator(approval.getUser());
			
			notification.setContent( approval );
			notification.setParentContent( approval );
			notification.setTargetContent( approval );
			notification.setReceiver( receiver.getUser() );
			notification.setOwner( approval.getUser() );
			notification.setSnippetText( approval.getTitle() );
//			notification.setSiteId( sunny.getSite().getId() );
			notificationRepository.save(notification);
			//activatorRelationService.addActivator( notification, user );
		}
		
		if( myReceiverRelation == null ){
			ReceiverRelation receiverRelation = new ReceiverRelation();
			receiverRelation.setActivityType( ActivityType.APPROVAL_APPROBATOR_COMPLETE );
			receiverRelation.setContent(approval);
			receiverRelation.setUser( approval.getUser() );
			receiverRelationService.save( receiverRelation );
			
			Notification notification = new Notification();
			
			notification.setActivityType( ActivityType.APPROVAL_APPROBATOR_COMPLETE );
			
			notification.addActivatorCount();
			notification.generateActivator(approval.getUser());
			
			notification.setContent(approval);
			notification.setParentContent( approval );
			notification.setTargetContent( approval );
			notification.setReceiver( receiverRelation.getUser() );
			notification.setOwner( approval.getUser() );
			notification.setSnippetText( approval.getTitle() );
//			notification.setSiteId( sunny.getSite().getId() );
			notificationRepository.save(notification);
			
			receivers.add(receiverRelation);
		}
		
		Long[] userIds = new Long[receivers.size()];

		for (int i = 0; i < receivers.size(); i++) {
			userIds[i] = receivers.get(i).getUser().getId();
		}
		return userIds;
	}

	@Override
	@Transactional
	public Long[] approveCooperationComplete(Sunny sunny, Approval approval) {

		List<ReceiverRelation> receivers = receiverRelationService.getContentsReceiver( approval, ActivityType.APPROVAL_COOPERATION_COMPLETE );
		
		if( receivers == null || receivers.size() == 0 ){
			ReceiverRelation receiverRelation = new ReceiverRelation();
			receiverRelation.setActivityType( ActivityType.APPROVAL_COOPERATION_COMPLETE );
			receiverRelation.setContent(approval);
			receiverRelation.setUser( approval.getUser() );
			receiverRelationService.save( receiverRelation );
			receivers = new ArrayList<ReceiverRelation>();
			receivers.add(receiverRelation);
		}
		
		ReceiverRelation myReceiverRelation = null;


		/*
		 * 팔로우하고 있는 사람을 순회하며 노티를 하나씩 생성해서 보낸다. 
		 */
		for( ReceiverRelation receiver : receivers ){
			if( receiver.getUser().equals( approval.getUser() )){
				myReceiverRelation = receiver;
			}
			
			Notification notification = new Notification();
			
			notification.setActivityType( ActivityType.APPROVAL_COOPERATION_COMPLETE );
			
			notification.addActivatorCount();
			notification.generateActivator(approval.getUser());
			
			notification.setContent( approval);
			notification.setParentContent( approval );
			notification.setTargetContent( approval );
			notification.setReceiver( receiver.getUser() );
			notification.setOwner( approval.getUser() );
			notification.setSnippetText( approval.getTitle() );
//			notification.setSiteId( sunny.getSite().getId() );
			notificationRepository.save(notification);
			//activatorRelationService.addActivator( notification, user );
		}
		
		if( myReceiverRelation == null ){
			ReceiverRelation receiverRelation = new ReceiverRelation();
			receiverRelation.setActivityType( ActivityType.APPROVAL_COOPERATION_COMPLETE );
			receiverRelation.setContent(approval);
			receiverRelation.setUser( approval.getUser() );
			receiverRelationService.save( receiverRelation );
			
			Notification notification = new Notification();
			
			notification.setActivityType( ActivityType.APPROVAL_COOPERATION_COMPLETE );
			
			notification.addActivatorCount();
			notification.generateActivator(approval.getUser());
			
			notification.setContent( approval );
			notification.setParentContent( approval );
			notification.setTargetContent( approval );
			notification.setReceiver( receiverRelation.getUser() );
			notification.setOwner( approval.getUser() );
			notification.setSnippetText( approval.getTitle() );
//			notification.setSiteId( sunny.getSite().getId() );
			notificationRepository.save(notification);
			
			receivers.add(receiverRelation);
		}
		
		Long[] userIds = new Long[receivers.size()];

		for (int i = 0; i < receivers.size(); i++) {
			userIds[i] = receivers.get(i).getUser().getId();
		}
		return userIds;
	}
	@Override
	@Transactional
	public Long[] approveReject(Sunny sunny, Long rejectorId, Approval approval) {

		List<ReceiverRelation> receivers = receiverRelationService.getContentsReceiver( approval, ActivityType.APPROVAL_REJECT );
		User rejector = userService.findById(rejectorId );
		if( receivers == null || receivers.size() == 0 ){
			ReceiverRelation receiverRelation = new ReceiverRelation();
			receiverRelation.setActivityType( ActivityType.APPROVAL_REJECT );
			receiverRelation.setContent(approval);
			receiverRelation.setUser( approval.getUser() );
			receiverRelationService.save( receiverRelation );
			receivers = new ArrayList<ReceiverRelation>();
			receivers.add(receiverRelation);
		}
		
		ReceiverRelation myReceiverRelation = null;


		/*
		 * 팔로우하고 있는 사람을 순회하며 노티를 하나씩 생성해서 보낸다. 
		 */
		for( ReceiverRelation receiver : receivers ){
			if( receiver.getUser().equals( rejector )){
				
				continue;
			}
			if( receiver.getUser().equals( approval.getUser() )){
				myReceiverRelation = receiver;
			}
			Notification notification = notificationRepository.findApprovalReject(approval, receiver.getUser(), DAY_AGO_EVALUATE);	
			
			// 노티가 존재하면 업데이트 하고 종료한다.		
			if( notification != null ){
				this.updateActivator( notification, rejector );
				notification.setSnippetText( approval.getTitle() );
				notification.setRead(false);
				notificationRepository.update(notification);
				continue ;
			}
			notification = new Notification();
			
			notification.setActivityType( ActivityType.APPROVAL_REJECT );
			
			notification.addActivatorCount();
			notification.generateActivator(rejector);
			
			notification.setContent( approval );
			notification.setParentContent( approval );
			notification.setTargetContent( approval );
			notification.setReceiver( receiver.getUser() );
			notification.setOwner( approval.getUser() );
			notification.setSnippetText( approval.getTitle() );
//			notification.setSiteId( sunny.getSite().getId() );
			notificationRepository.save(notification);
			activatorRelationService.addActivator( notification, rejector );
		}
		
		if( myReceiverRelation == null ){
			ReceiverRelation receiverRelation = new ReceiverRelation();
			receiverRelation.setActivityType( ActivityType.APPROVAL_REJECT );
			receiverRelation.setContent(approval);
			receiverRelation.setUser( approval.getUser() );
			receiverRelationService.save( receiverRelation );
			
			Notification notification = new Notification();
			
			notification.setActivityType( ActivityType.APPROVAL_REJECT );
			
			notification.addActivatorCount();
			notification.generateActivator(rejector);
			
			notification.setParentContent( approval );
			notification.setTargetContent( approval );
			notification.setReceiver( receiverRelation.getUser() );
			notification.setOwner( approval.getUser() );
			notification.setSnippetText( approval.getTitle() );
//			notification.setSiteId( sunny.getSite().getId() );
			notificationRepository.save(notification);
			receivers.add(receiverRelation);
			
		}
		
		Long[] userIds = new Long[receivers.size()];

		for (int i = 0; i < receivers.size(); i++) {
			userIds[i] = receivers.get(i).getUser().getId();
		}
		return userIds;
	}

	@Override
	@Transactional
	public void reply(Sunny sunny, Reply reply) {

		Reply persistentReply = replyService.select(reply.getId());
		User activator = persistentReply.getUser();
		
		Content parentContent = contentService.getContent( persistentReply.getContent().getId() );

		
		List<ReceiverRelation> receivers = receiverRelationService.getContentsReceiver( parentContent, ActivityType.COMMENT );
		/*
		 * 만약 Receiver 가 존재하지 않으면 새로 생성해준다.
		 * 일반적으로 Receiver 가 없다는 것은 Story 을 올리고 난 뒤 하나도 Reply 가 달리지 않았을때이다.
		 * 즉, 첫 Reply 하는 타이밍에 Story 에 Receiver 로 등록한다는 의미. 
		 */
		if( receivers == null || receivers.size() == 0 ){
			ReceiverRelation receiverRelation = new ReceiverRelation();
			receiverRelation.setActivityType( ActivityType.COMMENT );
			receiverRelation.setContent(parentContent);
			receiverRelation.setUser( parentContent.getUser() );
			receiverRelationService.save( receiverRelation );
			receivers = new ArrayList<ReceiverRelation>();
			receivers.add(receiverRelation);
		}
		
		/*
		 *	현재 행동을 한 당사자가 Receiver 테이블에 존재하지 않으면 나도 추가해준다.
		 */
		ReceiverRelation myReceiverRelation = null;


		/*
		 * 팔로우하고 있는 사람을 순회하며 노티를 하나씩 생성해서 보낸다. 
		 */
		for( ReceiverRelation receiver : receivers ){
			/*
			 * 내가 이미 댓글을 달았을 경우엔 continue
			 */
			if( receiver.getUser().equals( persistentReply.getUser() )){
				myReceiverRelation = receiver;
				continue;
			}
			Notification notification = notificationRepository.findContentReply(parentContent, receiver.getUser(), DAY_AGO_EVALUATE);	
			
			// 노티가 존재하면 업데이트 하고 종료한다.		
			if( notification != null ){
				this.updateActivator( notification, activator );
				notification.setSnippetText( reply.getStrippedSnippetText() );
				notification.setRead(false);
				notificationRepository.update(notification);
//				notification.setReceiver( receiver.getUser() );
				// 내 코멘트 이전 사람들을 receiver 에 넣는 부분인데, 코멘트를 생성했을 때 이미 receiver 에 추가되기 때문에 필요 없음.
//				List<Reply> receiveReplys = replyService.getRecentReceiversReplys(parentContent, persistentReply, 1);
//				if( receiveReplys != null ){
//					for( Reply replyItem : receiveReplys ){
//						receiverRelationService.addReceiver( notification, replyItem.getUser() );
//					}
//				}
//				receiverRelationService.requestReceiverCheckUpdate( notification, reply.getUser() );
				continue ;
			}
			notification = new Notification();
			
			notification.setActivityType( ActivityType.COMMENT );
			
			notification.addActivatorCount();
			notification.generateActivator(activator);
			
			notification.setContent(reply.getContent());
			notification.setParentContent( reply.getContent() );
			notification.setTargetContent( parentContent );
			notification.setReceiver( receiver.getUser() );
			notification.setOwner( reply.getContent().getUser() );
			notification.setSnippetText( reply.getStrippedSnippetText() );
//			notification.setSiteId(sunny.getSite().getId());
			notificationRepository.save(notification);
			activatorRelationService.addActivator( notification, activator );
		}
		
		if( myReceiverRelation == null ){
			System.out.println("없어서 새로 생성");
			ReceiverRelation receiverRelation = new ReceiverRelation();
			receiverRelation.setActivityType( ActivityType.COMMENT );
			receiverRelation.setContent(parentContent);
			receiverRelation.setUser( persistentReply.getUser() );
			receiverRelationService.save( receiverRelation );
		}
		
	}
	
	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.NotificationService#mentionStory(java.lang.String, kr.co.sunnyvale.sunny.domain.Story)
	 */
	@Override
	@Transactional
	public void mentionStory( Sunny sunny, final Long userId, Story story ){
		
		User activator = story.getUser();
		Notification notification = new Notification();
		notification.setActivityType( ActivityType.MENTION_STORY );
		
		notification.addActivatorCount();
		notification.generateActivator(activator);
		
		notification.setTargetContent( story );

		notification.setContent( story );
		notification.setParentContent( story );
		
		notification.setReceiver( new User(userId) );
//		notification.setSiteId( sunny.getSite().getId() );
		notification.setSnippetText( notification.getTargetContentSnippet() );
		notificationRepository.save(notification);
		activatorRelationService.addActivator( notification, activator );


		redisPublisher.publish(new NotiNotify(activator.getId(), story.getId(), ActivityType.MENTION_STORY, userId));
//        new Thread(){
//            public void run(){
//		        redisPublisher.publish(new Message( "user-notify", domain + "_1_" + userId));
//            }
//        }.start();
        
	}
	


	@Override
	@Transactional
	public void mentionReply(Sunny sunny, Long userId, Reply reply) {
		
		Reply persistentReply = replyService.select(reply.getId());
		User activator = persistentReply.getUser();
		Notification notification = notificationRepository.findMentionReply( persistentReply.getContent(), userId, DAY_AGO_EVALUATE );
		
		if( notification != null ){
			this.updateActivator(notification, activator);
			notification.setSnippetText( persistentReply.getStrippedSnippetText() );
			notification.setRead(false);
			notificationRepository.update(notification);
			return ;
		}
		
		notification = new Notification();
		notification.setActivityType( ActivityType.MENTION_REPLY);
		
		notification.addActivatorCount();
		notification.generateActivator(activator);
		
		notification.setContent( persistentReply );
		notification.setParentContent( persistentReply.getContent() );
		notification.setTargetContent( persistentReply.getContent() );
//		notification.setSiteId( sunny.getSite().getId() );
		notification.setReceiver( new User(userId) );
		
		notification.setOwner( persistentReply.getContent().getUser() );
		notification.setSnippetText( persistentReply.getStrippedSnippetText());
		notificationRepository.save(notification);
		activatorRelationService.addActivator( notification, activator );
		redisPublisher.publish(new NotiNotify(activator.getId(), persistentReply.getContent().getId(), ActivityType.MENTION_REPLY, userId));
	}
	
	@Transactional
	private void updateActivator(Notification notification, User activator) {
		// 이미 노티가 가있는 상태면  업데이트 날짜만 변경한
		if( !activatorRelationService.alreadyJoinActivator( notification, activator ) ){
			activatorRelationService.addActivator( notification, activator );
			notification.addActivatorCount();
		}
		
		notification.generateActivator(activator);
		notification.setUpdateDate( new Date() );
		notification.setRead(false);
		notificationRepository.update(notification);
		return ;
	}

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.NotificationService#update(kr.co.sunnyvale.sunny.domain.Notification)
	 */
	@Override
	@Transactional
	public void update(Notification notification){
		notificationRepository.update(notification);
	}

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.NotificationService#delete(long)
	 */
	@Override
	@Transactional
	public void delete(long notificationId) {
		notificationRepository.delete(new Notification(notificationId));
	}

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.NotificationService#find(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public Notification find(Long notificationId){
		return notificationRepository.select(notificationId);
	}
	
	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.NotificationService#getNotificationLastRead(kr.co.sunnyvale.sunny.domain.User)
	 */
	@Override
	@Transactional(readOnly = true)
	public Date getNotificationLastRead(User user) {
		return (Date) lastReadRepository.findColumnByObject("notificationLastRead", "user", user);
	}

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.NotificationService#getMessageLastRead(kr.co.sunnyvale.sunny.domain.User)
	 */
	@Override
	@Transactional(readOnly = true)
	public Date getMessageLastRead(User user) {
		return (Date) lastReadRepository.findColumnByObject("messageLastRead", "user", user);
	}
	
	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.NotificationService#getNotificationUnreadCount(kr.co.sunnyvale.sunny.domain.User)
	 */
	@Override
	@Transactional(readOnly = true)
	public Number getNotificationUnreadCount(User user) {
		Date lastRead = getNotificationLastRead(user);

		return notificationRepository.getNotificationUnreadCount(user, lastRead);
//		return receiverRelationService.getNotificationUnreadCount(user, lastRead);
	}
	
	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.NotificationService#getFollowNotifications(kr.co.sunnyvale.sunny.domain.User, kr.co.sunnyvale.sunny.domain.extend.Stream)
	 */
	@Override
	@Transactional
	public List<Notification> getFollowNotifications(User user, Stream stream) {
		LastRead lastRead = lastReadRepository.findUniqByObject("user", user);
		if( lastRead == null ){
			lastRead = new LastRead(user);
			lastRead.updateFriendRequestDate();
			lastReadRepository.save(lastRead);
		}else{
			lastRead.updateFriendRequestDate();
			lastReadRepository.update(lastRead);
		}
		return notificationRepository.getFollowNotifications(user, stream);		
	}

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.NotificationService#getNotifications(kr.co.sunnyvale.sunny.domain.User, kr.co.sunnyvale.sunny.domain.extend.Stream)
	 */
	@Override
	@Transactional	
	public List<Notification> getNotifications(User user, Stream stream) {
//		List<ReceiverRelation> relations = receiverRelationService.getNotifications(user, stream);
//		List<Notification> retNoties = new ArrayList<Notification>();
//		
//		for( ReceiverRelation relation : relations ){
//			Notification noti = relation.getNotification();
//			noti.setNewActivatorCount( noti.getActivatorCount() - relation.getLastActivatorCount() );
//			retNoties.add(noti);
//		}
		List<Notification> retNoties = notificationRepository.getUsersList( user, stream );
		
		return retNoties;
	}
	@Override
	@Transactional(readOnly = true )
	public Notification geNewOne(User user) {
		LastRead lastRead = lastReadRepository.findUniqByObject("user", user);
		return notificationRepository.getNewOne( user, lastRead );
		
	}

	@Override
	@Transactional(readOnly = true )
	public NotifyInfoDTO getNotifyInfo(Long userId) {
		LastRead lastRead = lastReadRepository.findUniqByObject("user.id", userId);
		return notificationRepository.getNotifyInfo(userId, lastRead);
		
	}
	
	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.NotificationService#getNotificationsAndUpdate(kr.co.sunnyvale.sunny.domain.User, kr.co.sunnyvale.sunny.domain.extend.Stream)
	 */
	@Override
	@Transactional
	public List<Notification> getNotificationsAndUpdate(User user, Stream stream) {

		LastRead lastRead = lastReadRepository.findUniqByObject("user", user);
		if( lastRead == null ){
			lastRead = new LastRead(user);
			lastRead.updateNotificationDate();
			lastReadRepository.save(lastRead);
		}else{
			lastRead.updateNotificationDate();
			lastReadRepository.update(lastRead);
		}

		return getNotifications(user, stream);		
	}

	@Override
	@Transactional
	public Page<Notification> getNotificationsAndUpdate(User user,
			Integer page, int pageSize) {
		LastRead lastRead = lastReadRepository.findUniqByObject("user", user);
		if( lastRead == null ){
			lastRead = new LastRead(user);
			lastRead.updateNotificationDate();
			lastReadRepository.save(lastRead);
		}else{
			lastRead.updateNotificationDate();
			lastReadRepository.update(lastRead);
		}

		return getNotifications(user, page, pageSize);		
	}
	

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.NotificationService#getUpdateDate(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly=true)
	public Serializable getUpdateDate(Long notiId) {

		return notificationRepository.getUpdateDate(notiId);
	}

	@Override
	@Transactional
	public void makeRead(Sunny sunny, Long userId, Long notificationId) {
		Notification notification = notificationRepository.select(notificationId);
		if( !notification.getReceiver().getId().equals(userId) ){
			throw new SimpleSunnyException();
		}
		
		notification.setRead(true);
		
	}

	@Override
	@Transactional
	public void makeReadStoryNoties(Long contentId, User user) {
		notificationRepository.makeReadAssociatedContents( contentId, user );
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Notification> getNotifications(User user, Integer page, int pageSize) {
		return notificationRepository.getUsersPagedList(user, page, pageSize);
	}

	

	


}