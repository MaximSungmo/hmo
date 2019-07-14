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
@Table(name = "SITE_MENU")
public class SiteMenu  {

	@Id
	@Column(name = "id", columnDefinition="bigint(20)" )
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "site_id")
	private Site site;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_id")
	private Menu menu;
	
	@Column(name = "license_start_date")
	private Date licenseStartDate;
	
	@Column(name = "license_end_date")
	private Date licenseEndDate;

	@Column(name = "is_visible", columnDefinition="boolean default true")
	private boolean visible = true;

	@Column(name = "ordering", columnDefinition="integer default 0" )
	private int ordering;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public Date getLicenseStartDate() {
		return licenseStartDate;
	}

	public void setLicenseStartDate(Date licenseStartDate) {
		this.licenseStartDate = licenseStartDate;
	}

	public Date getLicenseEndDate() {
		return licenseEndDate;
	}

	public void setLicenseEndDate(Date licenseEndDate) {
		this.licenseEndDate = licenseEndDate;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public int getOrdering() {
		return ordering;
	}

	public void setOrdering(int ordering) {
		this.ordering = ordering;
	}
	
}
