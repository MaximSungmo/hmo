package kr.co.sunnyvale.sunny.domain.extend;

public enum MediaType {
	
	OTHER(1),
	IMAGE(2),
	YOUTUBE(3),
	WORD(4),
	POWERPOINT(5),
	EXCEL(6),
	HWP(7),
	PDF(8);
	
	private int mediaType;
	MediaType(int mediaType){
		this.mediaType = mediaType;
	}
	
	public int getType(){
		return mediaType;
	}
}
