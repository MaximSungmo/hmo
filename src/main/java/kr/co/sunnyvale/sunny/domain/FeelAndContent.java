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
@Entity
@Table(name = "FEEL_AND_CONTENT")
public class FeelAndContent implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3507570273416098162L;

	@Id
	@Column(name = "id", columnDefinition="bigint(20)" )
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Content content;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "feel_id")
	private Feel feel;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	public Feel getFeel() {
		return feel;
	}

	public void setFeel(Feel feel) {
		this.feel = feel;
	}



	
}
