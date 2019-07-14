package kr.co.sunnyvale.sunny.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "ACTIVITY")
@PrimaryKeyJoinColumn(name="id")
public class Activity extends Content {
	

	private Activity(){
		super();
	}
	
	public Activity( Site site ){
		super(site, Content.TYPE_NONE);
	}
	public Activity(Long contentId) {
		this.setId(contentId);
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "work_card_id")
	private WorkList workCard;	
	
}
