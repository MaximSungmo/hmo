package kr.co.sunnyvale.sunny.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "WORK_LIST")
@PrimaryKeyJoinColumn(name="id")
public class WorkList extends Content {
	

	private WorkList(){
		super();
	}
	
	public WorkList( Site site ){
		super(site, Content.TYPE_NONE);
	}
	public WorkList(Long contentId) {
		this.setId(contentId);
	}
	
	@OneToMany(mappedBy = "workList", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE} )
	private List<WorkCard> workCards;

}
