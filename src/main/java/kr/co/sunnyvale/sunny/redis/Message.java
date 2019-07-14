package kr.co.sunnyvale.sunny.redis;

import com.fasterxml.jackson.core.JsonProcessingException;

public abstract class Message {
	
//    private String content;
    private String notiType;

    protected Message(String notiType){
    	this.notiType = notiType;
    }
    
//    protected Message(String type, String content) {
//        this.content = content;
//        this.type = type;
//    }

//    protected String getContent() {
//        return content;
//    }

    protected String getNotiType() {
        return notiType;
    }

    public abstract String toJsonString() throws JsonProcessingException;
    
    @Override
    public String toString() {
        return "Message{" +
//                "content=" + content +
                ", notiType='" + notiType + '\'' +
                '}';
    }
    
    
}