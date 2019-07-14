package kr.co.sunnyvale.sunny.domain.dto;

public class MediaDTO {
	
	
	
	public MediaDTO(Long id, String fileName, int mediaType, String extName,
			int width, int height, int size, String thumbnail) {
		super();
		this.id = id;
		this.fileName = fileName;
		this.mediaType = mediaType;
		this.extName = extName;
		this.width = width;
		this.height = height;
		this.size = size;
		this.thumbnail = thumbnail;
	}

	private Long id;
	
	private String fileName;
	
	private int mediaType;
	
	private String extName;
	
	private int width;
	
	private int height;
	
	private int size;
	
	private String thumbnail;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getMediaType() {
		return mediaType;
	}

	public void setMediaType(int mediaType) {
		this.mediaType = mediaType;
	}

	public String getExtName() {
		return extName;
	}

	public void setExtName(String extName) {
		this.extName = extName;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	
	
	
}
