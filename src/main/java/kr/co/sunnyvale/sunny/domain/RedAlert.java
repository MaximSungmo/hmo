package kr.co.sunnyvale.sunny.domain;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "RED_ALERT")
@PrimaryKeyJoinColumn(name="id")
public class RedAlert extends Content {
	

	private RedAlert(){
		super();
	}
	
	public RedAlert( Site site ){
		super(site, Content.TYPE_NONE);
	}
	public RedAlert(Long contentId) {
		this.setId(contentId);
	}

}
