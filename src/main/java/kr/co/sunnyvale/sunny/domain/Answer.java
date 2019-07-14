package kr.co.sunnyvale.sunny.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "ANSWER")
@PrimaryKeyJoinColumn(name="id")
public class Answer extends Content {
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = -2956306490449394889L;



//	public Answer(){
//		super(ContentType.ANSWER);
//		
//	}
	
	private Answer(){
		super();
	}
	public Answer(Site site){
		super(site, Content.TYPE_ANSWER);
	}
	public Answer(Site site, Long contentId) {
		super(site, Content.TYPE_ANSWER);
		this.setId(contentId);
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "question_id")
	private Question question;
	
	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	
	
}
