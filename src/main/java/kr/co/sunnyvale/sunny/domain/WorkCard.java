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
@Table(name = "WORK_CARD")
@PrimaryKeyJoinColumn(name="id")
public class WorkCard extends Content {
	

	private WorkCard(){
		super();
	}
	
	public WorkCard( Site site ){
		super(site, Content.TYPE_NONE);
	}
	public WorkCard(Long contentId) {
		this.setId(contentId);
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "work_list_id")
	private WorkList workList;	
	
	@OneToMany(mappedBy = "workCard", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE} )
	private List<Activity> activities;

}
