package kr.co.sunnyvale.sunny.builder;

import kr.co.sunnyvale.sunny.domain.Site;
import kr.co.sunnyvale.sunny.domain.User;

/**
 * 테스트용도의 User 객체를 리턴해주는 빌더. Slipp.net 소스 참고
 * 
 * @author Mook
 *
 */
public class AdminBuilder {
	
	public static final Long ADMIN1_ID=1L;
	public static final String ADMIN1_EMAIL="urstory@sunnyvale.co.kr";
	
	public static final Long ADMIN2_ID=2L;
	public static final String ADMIN2_EMAIL="meet42@gmail.com";
	
	public static final Long ADMIN3_ID=3L;
	public static final String ADMIN3_EMAIL="dev@startup.com";
	
	public static final Long ADMIN4_ID=4L;
	public static final String ADMIN4_EMAIL="kickscar@sunnyvale.co.kr";

	
	private Long id;
	private String email;
	private User user;
	private Site site;
	
	public AdminBuilder test1(){
		this.id = ADMIN1_ID;
		this.email = ADMIN1_EMAIL;
		this.user = UserBuilder.generator().test1().build();
		this.site = SiteBuilder.generator().test1().build();
		return this;
	}
	
	public AdminBuilder test2(){
		this.id = ADMIN2_ID;
		this.email = ADMIN2_EMAIL;
		this.user = UserBuilder.generator().test2().build();
		this.site = SiteBuilder.generator().test2().build();
		return this;
	}
	
	public AdminBuilder test3(){
		this.id = ADMIN3_ID;
		this.email = ADMIN3_EMAIL;
		this.user = UserBuilder.generator().test3().build();
		this.site = SiteBuilder.generator().test3().build();
		return this;
	}
	public AdminBuilder test4(){
		this.id = ADMIN4_ID;
		this.email = ADMIN4_EMAIL;
		this.user = UserBuilder.generator().test4().build();
		this.site = SiteBuilder.generator().test1().build();
		return this;
	}
	
	public static AdminBuilder generator() {
		return new AdminBuilder();
	}
//	
//	public Admin build() {
//		Admin admin = new Admin();
//		admin.setId(id);;
//		admin.setEmail(email);
//		admin.setUser(user);
//		admin.setSite(site);
//		return admin;
//	}
}
