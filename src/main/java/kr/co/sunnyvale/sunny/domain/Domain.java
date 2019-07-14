package kr.co.sunnyvale.sunny.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
/**
 * Site 와 domain(ex:www.yacamp.com)를 매칭시켜주는 테이블이다.
 * 한 사이트에 여러개의 도메인이 있을 수 있다.( 1:N )
 * 예로 www.yacamp.com 과 yacamp.meet42.com 이 하나의 사이트에 연결될 수 있다.
 * @author mook
 *
 */
@Entity
@Table(name = "DOMAIN")
public class Domain  {

	public Domain(){
		
	}
	
	public Domain(String name){
		this.name = name;
	}
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "site_id")
	private Site site;
	
	@Column(name = "name")
	private String name;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


}
