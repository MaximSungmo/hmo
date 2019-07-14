
package kr.co.sunnyvale.sunny.domain.dto;

import java.util.Date;

public class NotifyDTO {
	
	public NotifyDTO(){
		
	}
	
	private long id;
	private String name;
	private String strippedMessage;
	private String thumbnail;
	private Date updateDate;
	private String link;
	private String typeName;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getStrippedMessage() {
		return strippedMessage;
	}
	public void setStrippedMessage(String strippedMessage) {
		this.strippedMessage = strippedMessage;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	
}
