package kr.co.sunnyvale.sunny.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import kr.co.sunnyvale.sunny.domain.Channel;
import kr.co.sunnyvale.sunny.domain.Message;
import kr.co.sunnyvale.sunny.domain.MessageInfo;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.dto.MessageDTO;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;

public interface MessageService {

	public void save(Message message);

	public void update(Message message);

	public Message find(Long messageId);

	public void removeChannelMessages(Long channelId, Long userId);

	public List<Message> getMessages(Sunny sunny, User user, Boolean unread,
			Long lastReadMessageInfoId,
			Long channelId, Boolean updateLastDate, Stream stream);

	public Date getCreateDate(Long messageId);

	public Message save(Sunny sunny, User user, MessageDTO messageDto);

	
//	public void systemSave(Sunny sunny, Channel channel, int typeCreate, User user, Set<Long> userIds);
	
	/**
	 * 
	 * @param sunny
	 * @param channel
	 * @param type
	 * @param subjectUser
	 * 메시지를 보내는 주체. 예를 들면 "subject"님이 "object"님을 초대했습니다. 하는 메시지이다.
	 * @param objectUser
	 */
	public MessageInfo systemSave(Sunny sunny, Channel channel, int type, User subjectUser, Long ... objectUserIds);

	public Long getLastMessageInfoId(Long channelId);

	void readMessages(Sunny sunny, Long channelId, Long userId, Long prevReadMessageInfoId,
			Long lastReadMessageInfoId);

	

}