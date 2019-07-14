package kr.co.sunnyvale.sunny.builder;

import kr.co.sunnyvale.sunny.domain.SmallGroup;


public class SmallGroupBuilder {
	
	public static final Long SMALL_GROUP_1_ID=1L;
	public static final int SMALL_GROUP_1_TYPE=SmallGroup.TYPE_LOBBY;
	

	public static final Long SMALL_GROUP_2_ID=2L;
	public static final int SMALL_GROUP_2_TYPE=SmallGroup.TYPE_DEPARTMENT;
	
	private Long id;
	private int type;
	
	public SmallGroupBuilder test1(){
		this.id = SMALL_GROUP_1_ID;
		this.type = SMALL_GROUP_1_TYPE;
		return this;
	}
	
	public SmallGroupBuilder test2(){
		this.id = SMALL_GROUP_2_ID;
		this.type = SMALL_GROUP_2_TYPE;
		return this;
	}
	
	public static SmallGroupBuilder generator() {
		return new SmallGroupBuilder();
	}
	
	public SmallGroup build() {
		SmallGroup smallGroup = new SmallGroup();
		smallGroup.setId(id);
		smallGroup.setType(type);
		return smallGroup;
	}
}
