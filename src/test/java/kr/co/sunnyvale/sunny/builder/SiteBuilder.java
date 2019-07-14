package kr.co.sunnyvale.sunny.builder;

import java.util.ArrayList;
import java.util.List;

import kr.co.sunnyvale.sunny.domain.Domain;
import kr.co.sunnyvale.sunny.domain.Site;

/**
 * 테스트용도의 Site 객체를 리턴해주는 빌더. Slipp.net 소스 참고
 * 
 * @author Mook
 *
 */
public class SiteBuilder {
	
	public static final Long SITE1_ID=1L;
	public static final String SITE1_COMPANY_NAME="써니베일";
	public static final String SITE1_DOMAIN="sunnyvale.co.kr";
	public static final String SITE1_URL_NAME="sunnyvale";
	
	public static final Long SITE2_ID=2L;
	public static final String SITE2_COMPANY_NAME="밋사이";
	public static final String SITE2_DOMAIN="meet42.com";
	public static final String SITE2_URL_NAME="meet42";
	
	public static final Long SITE3_ID=3L;
	public static final String SITE3_COMPANY_NAME="스타트업";
	public static final String SITE3_DOMAIN=null;
	public static final String SITE3_URL_NAME="startup";
	
	private Long id;
	private String companyName;
	private String companyDomain;
	private String path;
	
	public SiteBuilder test1(){
		this.id = SITE1_ID;
		this.companyName = SITE1_COMPANY_NAME;
		this.companyDomain = SITE1_DOMAIN;
		this.path = SITE1_URL_NAME;
		return this;
	}
	
	public SiteBuilder test2(){
		this.id = SITE2_ID;
		this.companyName = SITE2_COMPANY_NAME;
		this.companyDomain = SITE2_DOMAIN;
		this.path = SITE2_URL_NAME;
		return this;
	}
	
	public SiteBuilder test3(){
		this.id = SITE3_ID;
		this.companyName = SITE3_COMPANY_NAME;
		this.companyDomain = SITE3_DOMAIN;
		this.path = SITE3_URL_NAME;
		return this;
	}
	
	public static SiteBuilder generator() {
		return new SiteBuilder();
	}
	
	
	public Site build() {
		Site site = new Site();
		site.setId(id);
		site.setCompanyName(companyName);
		site.setCompanyDomain(companyDomain);
		site.setPath(path);
		return site;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}


}
