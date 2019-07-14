package kr.co.sunnyvale.sunny.domain;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "NOTE")
@PrimaryKeyJoinColumn(name="id")
public class Note extends Content {
	

	private Note(){
		super();
	}
	
	public Note( Site site ){
		super(site, Content.TYPE_NOTE);
	}
	public Note(Long contentId) {
		this.setId(contentId);
	}

}
