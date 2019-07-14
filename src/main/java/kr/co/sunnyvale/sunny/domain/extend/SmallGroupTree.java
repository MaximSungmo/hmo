package kr.co.sunnyvale.sunny.domain.extend;

import java.io.Serializable;
import java.util.List;

public class SmallGroupTree implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6653274596597006457L;

	private Long id;
	
	private String name;
	
	private List<SmallGroupTree> children;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	public List<SmallGroupTree> getChildren() {
		return children;
	}

	public void setChildren(List<SmallGroupTree> children) {
		this.children = children;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public String getText(){
		return this.name;
	}
	
}

