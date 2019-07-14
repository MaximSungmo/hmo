package kr.co.sunnyvale.sunny.redis.notify;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.redis.Message;
import kr.co.sunnyvale.sunny.redis.MessageType;
import kr.co.sunnyvale.sunny.redis.notify.dto.NotifyWrapperDTO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MessageNotify extends Message {


	private String serviceId;
	
	private Long channelId;
	
	private Integer type;
	
	private Long[] userIds;
	
	private Long senderId;
	
	private Long readerId;
	
	private Long lastReadMessageInfoId;
	
//	private Integer unreadCount;
	
	public MessageNotify(Long channelId, Integer type, Long senderId, Long ... userIds ){
    	super( MessageType.CHAT.getTypeName() );
		this.serviceId = Sunny.SERVICE_ID;
		this.channelId = channelId;
		this.senderId = senderId;
		this.type = type;
		this.userIds = userIds;
	}
	
	// 읽은 경우 생성자
	public MessageNotify(Long channelId, Integer type, Long readerId, Long lastReadMessageInfoId, Long ... userIds ){
    	super( MessageType.CHAT.getTypeName() );
		this.serviceId = Sunny.SERVICE_ID;
		this.channelId = channelId;
		this.type = type;
		this.readerId = readerId;
		this.lastReadMessageInfoId = lastReadMessageInfoId;
		this.userIds = userIds;
	}
	
	@Override
	public String toJsonString() {

		NotifyWrapperDTO notifyWrapperDto = NotifyWrapperDTO.makeMessageDTo( this.serviceId, this.userIds, this.channelId, this.type, this.senderId, this.readerId, this.lastReadMessageInfoId);
		
		//MessageNotifyDTO dto = new MessageNotifyDTO(this.channelId, this.type, this.userIds, this.senderId, this.readerId, this.lastReadMessageInfoId);
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(notifyWrapperDto);
		} catch (JsonProcessingException e) {
			// 메시지가 가지 않는 것이기 때문에 그렇게 심각한 문제는 아님. 
			e.printStackTrace();
		}
		return "";
	}

	public Long getReaderId() {
		return readerId;
	}

	public void setReaderId(Long readerId) {
		this.readerId = readerId;
	}

	public Long getLastReadMessageInfoId() {
		return lastReadMessageInfoId;
	}

	public void setLastReadMessageInfoId(Long lastReadMessageInfoId) {
		this.lastReadMessageInfoId = lastReadMessageInfoId;
	}

	
	public Long getSenderId() {
		return senderId;
	}

	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}

	@Override
	public String toString() {
		return "ChatMessage [serviceId=" + serviceId + ", channelId=" + channelId
				+ ", type=" + type + ", userIds=" + Arrays.toString(userIds)
				+ ", toJsonString()=" + toJsonString() + "";
	}

}