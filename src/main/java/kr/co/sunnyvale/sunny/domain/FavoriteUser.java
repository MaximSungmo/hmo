package kr.co.sunnyvale.sunny.domain;


import java.util.Date;

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
@Table(name = "FAVORITE_USER")
public class FavoriteUser {
	

	public FavoriteUser(){
		this.setCreateDate(new Date());
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "comment")
	private String comment;
	
	@Column(name = "ordering")
	private int ordering;
	
	@Column(name = "create_date")
	private Date createDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "favoriter_id")
	private User favoriter;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "favorited_id")
	private User favorited;
	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "site_id")
	private Site site;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}



	public User getFavoriter() {
		return favoriter;
	}

	public void setFavoriter(User favoriter) {
		this.favoriter = favoriter;
	}

	public User getFavorited() {
		return favorited;
	}

	public void setFavorited(User favorited) {
		this.favorited = favorited;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getOrdering() {
		return ordering;
	}

	public void setOrdering(int ordering) {
		this.ordering = ordering;
	}

}
