package kr.co.sunnyvale.sunny.domain.post;


public class SiteUpdateDTO {

	private String companyName;
	
	private String companyDomain;
	
	private String companyPhone;
	
	private String companyIntroduce;
	
	private String homepage;
	
	private Integer privacy;
	
	private Integer noticeDuration;
	
	private String accessIpPds;

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

	public Integer getPrivacy() {
		return privacy;
	}

	public void setPrivacy(Integer privacy) {
		this.privacy = privacy;
	}

	
	public String getAccessIpPds() {
		return accessIpPds;
	}

	public void setAccessIpPds(String accessIpPds) {
		this.accessIpPds = accessIpPds;
	}

	
	
	public Integer getNoticeDuration() {
		return noticeDuration;
	}

	public void setNoticeDuration(Integer noticeDuration) {
		this.noticeDuration = noticeDuration;
	}

	@Override
	public String toString() {
		return "SiteUpdateDTO [companyName=" + companyName + ", companyDomain="
				+ companyDomain + ", companyPhone=" + companyPhone
				+ ", homepage=" + homepage + ", privacy=" + privacy + "]";
	}

	public String getCompanyIntroduce() {
		return companyIntroduce;
	}

	public void setCompanyIntroduce(String companyIntroduce) {
		this.companyIntroduce = companyIntroduce;
	}

	
	
}