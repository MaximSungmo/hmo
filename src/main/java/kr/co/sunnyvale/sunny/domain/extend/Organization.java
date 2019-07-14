package kr.co.sunnyvale.sunny.domain.extend;

import java.util.List;

public class Organization {
	
	private long thread;
	
	private int depth;
	
	private Long parentSmallGroupId;
	
	private String absolutePath;
	
	private int ordering; 
	
	private Object parent;
	
	private List<Object> children;

	public long getThread() {
		return thread;
	}

	public void setThread(long thread) {
		this.thread = thread;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public Long getParentSmallGroupId() {
		return parentSmallGroupId;
	}

	public void setParentSmallGroupId(Long parentSmallGroupId) {
		this.parentSmallGroupId = parentSmallGroupId;
	}

	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}

	public int getOrdering() {
		return ordering;
	}

	public void setOrdering(int ordering) {
		this.ordering = ordering;
	}

	public Object getParent() {
		return parent;
	}

	public void setParent(Object parent) {
		this.parent = parent;
	}

	public List<Object> getChildren() {
		return children;
	}

	public void setChildren(List<Object> children) {
		this.children = children;
	}
	
	
	
}
