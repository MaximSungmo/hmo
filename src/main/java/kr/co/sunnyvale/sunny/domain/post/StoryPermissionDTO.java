package kr.co.sunnyvale.sunny.domain.post;

public class StoryPermissionDTO {
	
	private Long id;
	
	/**
	 * null = 사용자
	 * not null = 해당 smallGroup 타입
	 */
	private Integer smallGroupType;
	
//	private Boolean r;
//	private Boolean w;
//	private Boolean d;
//	
//	private Boolean children;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getSmallGroupType() {
		return smallGroupType;
	}
	public void setSmallGroupType(Integer smallGroupType) {
		this.smallGroupType = smallGroupType;
	}
//	public Boolean getR() {
//		return r;
//	}
//	public void setR(Boolean r) {
//		this.r = r;
//	}
//	public Boolean getW() {
//		return w;
//	}
//	public void setW(Boolean w) {
//		this.w = w;
//	}
//	public Boolean getD() {
//		return d;
//	}
//	public void setD(Boolean d) {
//		this.d = d;
//	}
//	
//	public Boolean getChildren() {
//		return children;
//	}
//	public void setChildren(Boolean children) {
//		this.children = children;
//	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StoryPermissionDTO other = (StoryPermissionDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
