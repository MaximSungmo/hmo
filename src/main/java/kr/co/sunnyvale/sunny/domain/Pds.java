package kr.co.sunnyvale.sunny.domain;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "PDS")
@PrimaryKeyJoinColumn(name="id")
public class Pds extends Content {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1227181457931149537L;
	
	public Pds(){
		super();
	}
	
	public Pds( Site site ){
		super(site, Content.TYPE_PDS);
	}
	
	public Pds(Long contentId) {
		this.setId(contentId);
	}

	
	
}
