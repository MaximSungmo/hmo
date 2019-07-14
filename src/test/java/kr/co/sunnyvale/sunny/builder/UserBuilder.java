package kr.co.sunnyvale.sunny.builder;

import kr.co.sunnyvale.sunny.domain.Site;
import kr.co.sunnyvale.sunny.domain.User;

/**
 * 테스트용도의 User 객체를 리턴해주는 빌더. Slipp.net 소스 참고
 * 
 * @author Mook
 *
 */
public class UserBuilder {
	
	public static final Long USER1_ID=1L;
	public static final String USER1_EMAIL="admin@sunnyvale.co.kr";
	public static final String USER1_NAME="써니베일관리자";
	public static final String USER1_PASSWORD="123456";
	
	public static final Long USER2_ID=2L;
	public static final String USER2_EMAIL="meet42@naver.com";
	public static final String USER2_NAME="밋사이 관리자";
	public static final String USER2_PASSWORD="123456";
	
	public static final Long USER3_ID=3L;
	public static final String USER3_EMAIL="hello@startup.com";
	public static final String USER3_NAME="스타트업관리자";
	public static final String USER3_PASSWORD="123456";


	public static final Long USER4_ID=4L;
	public static final String USER4_EMAIL="kickscar@sunnyvale.co.kr";
	public static final String USER4_NAME="써니베일넘버2";
	public static final String USER4_PASSWORD="123456";
	
	public static final Long USER5_ID=5L;
	public static final String USER5_EMAIL="ipes4579@gmail.com";
	public static final String USER5_NAME="써니베일구성원";
	public static final String USER5_PASSWORD="123456";
	private Long id;
	
	private String email;
	
	private String name;
	
	private String sunnyUrl;
	
	private String password;
	
	private Site site;
	
	public UserBuilder test1(){
		this.id=USER1_ID;
		this.email = USER1_EMAIL;
		this.name = USER1_NAME;
		this.password = USER1_PASSWORD;
		this.site = SiteBuilder.generator().test1().build();
		return this;
	}
	
	public UserBuilder test2(){
		this.id=USER2_ID;
		this.email = USER2_EMAIL;
		this.name = USER2_NAME;
		this.password = USER2_PASSWORD;
		this.site = SiteBuilder.generator().test1().build();
		return this;
	}
	
	public UserBuilder test3(){
		this.id=USER3_ID;
		this.email = USER3_EMAIL;
		this.name = USER3_NAME;
		this.password = USER3_PASSWORD;
		this.site = SiteBuilder.generator().test1().build();
		return this;
	}
	public UserBuilder test4(){
		this.id=USER4_ID;
		this.email = USER4_EMAIL;
		this.name = USER4_NAME;
		this.password = USER4_PASSWORD;
		this.site = SiteBuilder.generator().test2().build();
		return this;
	}
	public UserBuilder test5(){
		this.id=USER5_ID;
		this.email = USER5_EMAIL;
		this.name = USER5_NAME;
		this.password = USER5_PASSWORD;
		this.site = SiteBuilder.generator().test3().build();
		return this;
	}
	
	public static UserBuilder generator() {
		return new UserBuilder();
	}
	
	public User build() {
		User user = new User();
		user.setId(id);
//		user.setEmail(email);
		user.setPassword(password);
		user.setName(name);
//		user.setSunnyUrl(sunnyUrl);
//		user.setSite(site);
		return user;
	}
}
