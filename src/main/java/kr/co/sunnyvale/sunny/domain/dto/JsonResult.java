package kr.co.sunnyvale.sunny.domain.dto;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class JsonResult {

	private String result;
	private String message;
	private Object data;
	
	public JsonResult(boolean truefalse, String message, Object data) {
//		super();
		if( truefalse == true ){
			this.result = "success";
		}else{
			this.result = "fail";
		}
		this.message = message;
		this.data = data;
	}
	public JsonResult(String result, String message, Object data) {
//		super();
		this.result = result;
		this.message = message;
		this.data = data;
	}
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
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
	
	@JsonIgnore
	public Map<String, Object> getMap(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", getResult());
		map.put("message", getMessage());
		map.put("data", getData());
		return map;
	}
	
	
}
