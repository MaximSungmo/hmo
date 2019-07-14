package kr.co.sunnyvale.sunny.domain.extend;

import java.io.Serializable;

public class SmallGroupIdName implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6653274596597006457L;

	private Long id;
	
	private String name;
	
	

	public SmallGroupIdName(){
		
	}
	
	public SmallGroupIdName(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
	
}

