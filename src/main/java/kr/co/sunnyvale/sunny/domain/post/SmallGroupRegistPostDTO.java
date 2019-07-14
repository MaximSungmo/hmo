package kr.co.sunnyvale.sunny.domain.post;


public class SmallGroupRegistPostDTO {

	private Long parentSmallGroupId;
	
	private Long prevSmallGroupId;
	
	private String name;
	
	private String description;

	private int privacy;
	
	private int type;
	
	public Long getParentSmallGroupId() {
		return parentSmallGroupId;
	}

	public void setParentSmallGroupId(Long parentSmallGroupId) {
		this.parentSmallGroupId = parentSmallGroupId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPrivacy() {
		return privacy;
	}

	public void setPrivacy(int privacy) {
		this.privacy = privacy;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Long getPrevSmallGroupId() {
		return prevSmallGroupId;
	}

	public void setPrevSmallGroupId(Long prevSmallGroupId) {
		this.prevSmallGroupId = prevSmallGroupId;
	}

	
	
}
