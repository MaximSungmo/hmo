package kr.co.sunnyvale.sunny.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "SEQUENCE")
public class Sequence  {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(name = "seq_id")
	private String seqId;
	
	@Column(name = "seq_count")
	private long seqCount;
	
	public int getId() {
		return this.id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getSeqId() {
		return seqId;
	}
	public void setSeqId(String seqId) {
		this.seqId = seqId;
	}
	
	public long getSeqCount() {
		return seqCount;
	}
	public void setSeqCount(long seq) {
		this.seqCount = seq;
	}
	
}
