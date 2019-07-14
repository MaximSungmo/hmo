package kr.co.sunnyvale.sunny.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "QUESTION")
@PrimaryKeyJoinColumn(name="id")
public class Question extends Content {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2956306490449394889L;

	private Question(){
		super();
	}
	
	public Question(Site site){
		super(site, Content.TYPE_QUESTION);
	}
	
	public Question(Long contentId) {
		this.setId(contentId);
	}

	@OneToMany(mappedBy = "question", fetch=FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.REMOVE} )
	private List<Answer> answers;

	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

}
