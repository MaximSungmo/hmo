package kr.co.sunnyvale.sunny.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SITE_INACTIVE")
public class SiteInactive  {

	public SiteInactive(){
		this.createDate = new Date();
	}
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "create_date")
	private Date createDate;
	// ex) 회사 이름
	@Column(name = "company_name")
	private String companyName;
	
	// ex) sunnyvale.co.kr 과 같은 도메인
	@Column(name = "company_domain")
	private String companyDomain;
	

	// 국가/지역
	@Column(name = "contury_code")
	private String conturyCode;
	
	// 폰/번호
	@Column(name = "company_phone")
	private String companyPhone;
	

	// 홈페이지
	@Column(name = "homepage")
	private String homepage;
	
	@Column(name = "employee_size", columnDefinition="integer default 0" )
	private int employeeSize;

	@Column(name = "company_region")
	private String companyRegion;

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

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyDomain() {
		return companyDomain;
	}

	public void setCompanyDomain(String companyDomain) {
		this.companyDomain = companyDomain;
	}

	public String getConturyCode() {
		return conturyCode;
	}

	public void setConturyCode(String conturyCode) {
		this.conturyCode = conturyCode;
	}

	public String getCompanyPhone() {
		return companyPhone;
	}

	public void setCompanyPhone(String companyPhone) {
		this.companyPhone = companyPhone;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public int getEmployeeSize() {
		return employeeSize;
	}

	public void setEmployeeSize(int employeeSize) {
		this.employeeSize = employeeSize;
	}

	public String getCompanyRegion() {
		return companyRegion;
	}

	public void setCompanyRegion(String companyRegion) {
		this.companyRegion = companyRegion;
	}
	
	
	public Site parseToSite(){
		Site site = new Site();
		site.setCompanyName( this.getCompanyName() );
		site.setCompanyDomain(this.getCompanyDomain());
		site.setCompanyPhone(this.getCompanyPhone());
		site.setCompanyRegion(this.getCompanyRegion());
		site.setConturyCode(this.getConturyCode());
		site.setEmployeeSize(this.getEmployeeSize());
		site.setHomepage(this.getHomepage());
		
		return site; 
	}
	
	
	
}
