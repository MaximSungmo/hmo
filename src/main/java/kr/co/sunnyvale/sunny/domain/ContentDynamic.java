package kr.co.sunnyvale.sunny.domain;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/*
 * 글 작성자가 아닌 글을 보는 주체가 하는 행동에 대한 값들
 * 예: 평가점수, 코멘트 개수 등등
 * 단, 조회수는 Batch 에 의해서 자동화될 수 없는 중요한 자료이기 때문에 자료를 다른 테이블에 넣음
 */


@Entity
@Table(name = "CONTENT_DYNAMIC")
public class ContentDynamic implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3507570273416098162L;

	public ContentDynamic(){
		this.setAnswerCount(0);
		this.setReplyCount(0);
		this.setShareCount(0);
		this.setFeelCount(0);
		this.setFeelScore(0);
		this.setReputation(0);
	}
	
	public ContentDynamic(Content content) {
		this();
		this.content = content;
	}

	@Id
	@Column(name = "id", columnDefinition="bigint(20)" )
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "content_id")
	private Content content;

	@Column(name = "reply_count", columnDefinition="integer default 0")
	private Integer replyCount;
	
	@Column(name = "feel_score", columnDefinition="integer default 0")
	private Integer feelScore;
	
	@Column(name = "feel_count", columnDefinition="integer default 0")
	private Integer feelCount;

	@Column(name = "share_count", columnDefinition="integer default 0")
	private Integer shareCount;

	@Column( name = "answer_count",columnDefinition="integer default 0"  )
	private Integer answerCount;

	@Column( name = "reputation", columnDefinition="integer default 0" )
	private Integer reputation;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@JsonIgnore
	public Content getContent() {
		return content;
	}
	@JsonIgnore
	public void setContent(Content content) {
		this.content = content;
	}


	public Integer getReplyCount() {
		return replyCount;
	}

	public void setReplyCount(Integer replyCount) {
		this.replyCount = replyCount;
	}

	public Integer getFeelScore() {
		if( this.feelScore == null ){
			this.feelScore = 0;
		}
		return feelScore;
	}

	public void setFeelScore(Integer feelScore) {
		this.feelScore = feelScore;
	}

	public Integer getFeelCount() {
		if( this.feelCount == null ){
			this.feelCount = 0;
		}
		return feelCount;
	}

	public void setFeelCount(Integer feelCount) {
		this.feelCount = feelCount;
	}


	public Integer getShareCount() {
		if( shareCount == null ){
			shareCount = new Integer(0);
		}
		return shareCount;
	}

	public void setShareCount(Integer shareCount) {
		this.shareCount = shareCount;
	}

	public Integer getAnswerCount() {
		if( answerCount == null )
			answerCount = new Integer(0);
		return answerCount ;
	}

	public void setAnswerCount(Integer answerCount) {
		this.answerCount = answerCount;
	}


	public Integer getReputation() {
		return reputation;
	}

	public void setReputation(Integer reputation) {
		this.reputation = reputation;
	}
}
