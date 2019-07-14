package kr.co.sunnyvale.sunny.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "FEEL")
public class Feel {
	
	
	public final static int ID_NEGATIVE=-1;
	public final static int ID_BEST=1;
	public final static int ID_SAD=2;
	public final static int ID_CHEER=3;
	public final static int ID_NICE=4;
	public final static int ID_LIKE=5;
	
	
	
	@Id
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "name_eng")
	private String nameEng;
	
	@Column(name = "name_kr")
	private String nameKr;
	
	@Column(name = "score")
	private Integer score;
	
	public Feel() {

	}
	public Feel(Integer evaluateId) {
		this.id = evaluateId;
	}

	public Feel(int id, String nameEng, String nameKr, int score) {
		this.id = id;
		this.nameEng = nameEng;
		this.nameKr = nameKr;
		this.score = score;
	}
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNameEng() {
		return nameEng;
	}

	public void setNameEng(String nameEng) {
		this.nameEng = nameEng;
	}

	public String getNameKr() {
		return nameKr;
	}

	public void setNameKr(String nameKr) {
		this.nameKr = nameKr;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}
	
	@Override
	public String toString() {
		return "Evaluate ["              +
				"  id: "             + id + 
				" nameEng : " 		+ nameEng +
				" nameKr : " 		+ nameKr +
				" score : " 		+ score;
	}


}
