package kr.co.sunnyvale.sunny.domain;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CdnJsonResult {

	public final static int RESULT_CODE_SUCCESS=0;
	public final static int RESULT_CODE_SESSION=1;
	public final static int RESULT_CODE_UNKNOWN_FAIL=100;
	
	
	private int resultCode;
	private String message;
	private Object data;
	
	public CdnJsonResult(int resultCode, String message, Object data) {
		super();
		this.resultCode = resultCode;
		this.message = message;
		this.data = data;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	public int getResultCode() {
		return resultCode;
	}


	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}


//	@JsonIgnore
//	public Map<String, Object> getMap(){
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("resultCode", getResultCode());
//		map.put("message", getMessage());
//		map.put("data", getData());
//		return map;
//	}

	public String toJsonString() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		String retval = objectMapper.writeValueAsString(this);
		
		System.out.println("결과값 : " + retval);
		return retval; 
	}
	
	
}

