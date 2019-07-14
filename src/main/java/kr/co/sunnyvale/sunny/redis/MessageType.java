package kr.co.sunnyvale.sunny.redis;

public enum MessageType {
	
	NOTIFICATION("noti-notify"), 
	CHAT("chat-notify"), 
	FRIEND_REQUEST("friend-notify"),
	NOTICE("notice-notify");
	
	private String typeName;
	
	MessageType(String typeName){
		this.typeName = typeName;
	}
	
	public String getTypeName(){
		return this.typeName;
	}
}
