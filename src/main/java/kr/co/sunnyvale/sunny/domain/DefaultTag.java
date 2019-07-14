package kr.co.sunnyvale.sunny.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DEFAULT_TAG")
public class DefaultTag {
	
	private DefaultTag(){
	}

	@Id
	@Column(name = "id" )
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "content_type", columnDefinition="integer default 0" )
	private int contentType;
	
	@Column(name = "admin_ordering", columnDefinition="integer default 0" )
	private int adminOrdering;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getContentType() {
		return contentType;
	}

	public void setContentType(int contentType) {
		this.contentType = contentType;
	}

	public int getAdminOrdering() {
		return adminOrdering;
	}

	public void setAdminOrdering(int adminOrdering) {
		this.adminOrdering = adminOrdering;
	}
	
}
