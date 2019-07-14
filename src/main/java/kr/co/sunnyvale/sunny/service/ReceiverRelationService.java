package kr.co.sunnyvale.sunny.service;

import java.util.List;
import java.util.Set;

import kr.co.sunnyvale.sunny.domain.Content;
import kr.co.sunnyvale.sunny.domain.ReceiverRelation;
import kr.co.sunnyvale.sunny.domain.Reply;

import org.springframework.transaction.annotation.Transactional;

public interface ReceiverRelationService {

	public void save(ReceiverRelation receiverRelation);

	public void update(ReceiverRelation receiverRelation);

	public ReceiverRelation find(Long receiverRelationId);

	public Set<String> getUserIdsFromReplyNoti(Reply reply);

	public List<ReceiverRelation> getContentsReceiver(Content content,
			int activityType);

}