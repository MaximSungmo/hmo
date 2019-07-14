package kr.co.sunnyvale.sunny.domain.post;

public class RequestFriendDTO {
	
	public static final int REQUEST_TYPE_FRIEND=0;
	public static final int REQUEST_TYPE_CANCLE=1;
	public static final int REQUEST_TYPE_ACCEPT=2;
	public static final int REQUEST_TYPE_DISCONNECT=3;
	public static final int REQUEST_TYPE_DENY=4;
	

	private Long userId;

	private Integer requestType;
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getRequestType() {
		return requestType;
	}

	public void setRequestType(Integer requestType) {
		this.requestType = requestType;
	}
	
}
