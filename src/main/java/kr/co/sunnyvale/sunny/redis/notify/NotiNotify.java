package kr.co.sunnyvale.sunny.redis.notify;

import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.redis.Message;
import kr.co.sunnyvale.sunny.redis.MessageType;
import kr.co.sunnyvale.sunny.redis.notify.dto.NotifyWrapperDTO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NotiNotify extends Message {


	private Long senderId;
	
	private Integer type;
	
	private Long[] userIds;

	private Long storyId;
	
	public NotiNotify(Long senderId, Long storyId, Integer type, Long ... userIds ){
    	super( MessageType.NOTIFICATION.getTypeName() );
    	this.senderId = senderId;
    	this.storyId = storyId;
		this.type = type;
		this.userIds = userIds;
	}
	
	@Override
	public String toJsonString() {

		
		NotifyWrapperDTO notifyWrapperDto = NotifyWrapperDTO.makeNotiDTo( Sunny.SERVICE_ID, this.userIds, this.senderId, this.storyId, this.type );
//		NotiNotifyDTO dto = new NotiNotifyDTO(this.type, this.userIds);
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(notifyWrapperDto);
		} catch (JsonProcessingException e) {
			// 메시지가 가지 않는 것이기 때문에 그렇게 심각한 문제는 아님. 
			e.printStackTrace();
		}
		return "";
	}

}