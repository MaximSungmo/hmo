package kr.co.sunnyvale.sunny.service.impl;

import java.util.List;
import java.util.Set;

import kr.co.sunnyvale.sunny.domain.Content;
import kr.co.sunnyvale.sunny.domain.ReceiverRelation;
import kr.co.sunnyvale.sunny.domain.Reply;
import kr.co.sunnyvale.sunny.repository.hibernate.ReceiverRelationRepository;
import kr.co.sunnyvale.sunny.service.ReceiverRelationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( value="receiverRelationService" )
@Transactional
public class ReceiverRelationServiceImpl implements ReceiverRelationService{
	
	@Autowired
	private ReceiverRelationRepository receiverRelationRepository;
	
	
	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.ReceiverRelationService#save(kr.co.sunnyvale.sunny.domain.ReceiverRelation)
	 */
	@Override
	@Transactional
	public void save(ReceiverRelation receiverRelation){
		receiverRelationRepository.save(receiverRelation);
	}
	
	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.ReceiverRelationService#update(kr.co.sunnyvale.sunny.domain.ReceiverRelation)
	 */
	@Override
	@Transactional
	public void update(ReceiverRelation receiverRelation){
		receiverRelationRepository.update(receiverRelation);
	}
	
	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.ReceiverRelationService#find(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public ReceiverRelation find(Long receiverRelationId){
		return receiverRelationRepository.select(receiverRelationId);
	}

//	@Transactional
//	public void addReceiver(Notification notification, User receiver) {
//		
//		ReceiverRelation relation = receiverRelationRepository.find( notification, receiver );
//		
//		if( relation == null ){
//			relation = new ReceiverRelation( receiver, notification );
//			relation.setLastActivatorCount( notification.getActivatorCount() );
//			save( relation  );
//			return;
//		}
//		
//		// 내가 쓴 글에 내가 댓글을 단 경우 시간 업데이트를 하지 않는다. 
//		if( receiver.getId().equals( notification.getOwnerId()) ){
//			return;
//		}
//		
//		relation.setLastActivatorCount( notification.getActivatorCount() );
//		relation.setUpdateDate( new Date() );
//		receiverRelationRepository.update(relation);
//	}
	
//	@Transactional(readOnly = true)
//	public Number getNotificationUnreadCount(User user, Date lastRead) {
//		return receiverRelationRepository.getNotificationUnreadCount( user, lastRead );
//	}
//
//	@Transactional(readOnly = true)
//	public List<ReceiverRelation> getNotifications(User user, Stream stream) {
//		return receiverRelationRepository.getNotifications( user, stream );
//	}

//	@Transactional
//	public void requestReceiverCheckUpdate(Notification notification, User user) {
//		receiverRelationRepository.requestReceiverCheckUpdate( notification, user );
//	}

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.ReceiverRelationService#getUserIdsFromReplyNoti(kr.co.sunnyvale.sunny.domain.Reply)
	 */
	@Override
	@Transactional(readOnly = true )
	public Set<String> getUserIdsFromReplyNoti(Reply reply) {
		return receiverRelationRepository.getUserIdsFromReplyNoti( reply );
	}

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.ReceiverRelationService#getContentsReceiver(kr.co.sunnyvale.sunny.domain.Content, int)
	 */
	@Override
	@Transactional(readOnly = true )
	public List<ReceiverRelation> getContentsReceiver(Content content,
			int activityType) {
		return receiverRelationRepository.getContentReceiver( content, activityType );
	}

}

