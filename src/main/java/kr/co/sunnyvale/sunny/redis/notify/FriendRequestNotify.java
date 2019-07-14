package kr.co.sunnyvale.sunny.redis.notify;

import kr.co.sunnyvale.sunny.redis.Message;
import kr.co.sunnyvale.sunny.redis.MessageType;

public class FriendRequestNotify extends Message {
	
	private Integer type;
	
	private Long[] userIds;
	
	public FriendRequestNotify(Integer type, Long ... userIds ){
    	super( MessageType.FRIEND_REQUEST.getTypeName() );
		this.type = type;
		this.userIds = userIds;
	}
	@Override
	public String toJsonString() {
		
		
//		FriendRequestNotifyDTO frmDTO = new FriendRequestNotifyDTO(this.type, this.userIds);
//		
//		
//		
//		
//		ObjectMapper mapper = new ObjectMapper();
//		try {
//			return mapper.writeValueAsString(frmDTO);
//		} catch (JsonProcessingException e) {
//			// 메시지가 가지 않는 것이기 때문에 그렇게 심각한 문제는 아님. 
//			e.printStackTrace();
//		}
		return "";
	}

}