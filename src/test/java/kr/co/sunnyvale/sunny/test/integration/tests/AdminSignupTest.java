package kr.co.sunnyvale.sunny.test.integration.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import kr.co.sunnyvale.sunny.builder.SecurityUserBuilder;
import kr.co.sunnyvale.sunny.builder.UserBuilder;
import kr.co.sunnyvale.sunny.controller.api.AdminAPIController;
import kr.co.sunnyvale.sunny.controller.web.AdminController;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.dto.JsonResult;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.repository.hibernate.RoleRepository;
import kr.co.sunnyvale.sunny.service.AuthTokenService;
import kr.co.sunnyvale.sunny.service.SiteService;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration({
	"file:src/main/webapp/WEB-INF/sunny-servlet.xml",
	"classpath:root-context.xml", 
	"classpath:security-context.xml",
	"file:src/test/resources/testPersistenceContext.xml",
	"file:src/test/resources/testContext.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback=true)
public class AdminSignupTest {

	Logger logger = Logger.getLogger(getClass());

	@Autowired
	private SiteService siteService; 
	
	@Autowired
	private AdminController adminController;
	
	@Autowired
	private AdminAPIController adminApiController;

	@Autowired
	private AuthTokenService authTokenService;
	
	@Autowired
	private RoleRepository roleRepository;

	ModelAndView mav;
	JsonResult jret; 
	
	/**
		관리자 가입, 승인

		관리자는 이름, 현재 직장에서 사용하는 email(로그인용 아님),
		회사이름, 직원수, 국가/지역, 전화번호를 입력한다.
		
		실제 사용할 email(로그인용), 비밀번호만들기, 비밀번호재입력
		회원가입을 한다.
		
		Admin 도메인은 Site정보와 User정보를 가진다. 그리고 Admin만의 등록날짜, 수정날짜, 별도의 email정보를 가진다. 
		
		관리자는 이메일을 받은 후, 승인을 한다.
		
		승인을 하면 메인화면으로 이동. 메인화면에서 로그인한다. 로그인한 후 관리자 화면으로 이동.
	 */
	@Test
	@Transactional(noRollbackFor = Throwable.class)
	@Rollback(false)
	public void test관리자회원가입()  {
		
		/**
		 * 1. /admin/signup 들어왔을 때 액션
		 */
		mav = adminController.getSignup(null, null);
		assertNotNull(mav);
		assertThat( mav.getViewName(), is("/a/signup"));
		
		
		// 로그인 돼 있을 때
		// mav = adminController.getSignup( securityUser1);
		// assertThat( mav.getViewName(), is("redirect:/"));
		
		/**
		 * 유저들을 생성하고 인증 메일을 보낸다. 
		 */
//		Admin admin1 = AdminBuilder.generator().test1().build();
//		AdminJoinDTO joinDto = new AdminJoinDTO();
////		joinDto.setAdmin(admin1);
//		adminApiController.signup(null, joinDto);
//		
//		Admin admin2 = AdminBuilder.generator().test2().build();
//		joinDto = new AdminJoinDTO();
////		joinDto.setAdmin(admin2);
//		adminApiController.signup(null, joinDto);
//		
//		Admin admin3 = AdminBuilder.generator().test3().build();
//		joinDto = new AdminJoinDTO();
////		joinDto.setAdmin(admin3);
//		adminApiController.signup(null, joinDto);

		
		/**
		 * 메일이 왔다는 가정 하에 인증을 한다.
		 */
		// 왜 여기 문제가?
		
//		mav = adminController.activate(null, 1234L, "1234");
//		assertThat( mav.getViewName(), is("redirect:/a/error_activate"));
//		
//		AuthToken authToken = authTokenService.getRecentAuthToken(admin1.getUser().getId(), AuthToken.TYPE_ACTIVATE_ADMIN);
//		assertNotNull(authToken);
//		adminController.activate(null, admin1.getUser().getId(), authToken.getValue());
//		
//		// 인증 성공한 뒤 해당 keygen 이 사라졌나 확인한다. 
//		AuthToken authToken2 = authTokenService.getRecentAuthToken(admin1.getUser().getId(), AuthToken.TYPE_ACTIVATE_ADMIN);
//		assertThat(authToken, is(not(authToken2)));
//		
//		authToken = authTokenService.getRecentAuthToken(admin2.getId(), AuthToken.TYPE_ACTIVATE_ADMIN);
//		adminController.activate(null, admin2.getUser().getId(), authToken.getValue());
//		
//		authToken = authTokenService.getRecentAuthToken(admin3.getId(), AuthToken.TYPE_ACTIVATE_ADMIN);
//		adminController.activate(null, admin3.getId(), authToken.getValue());

		

//		mav = adminController.sendPassword(null, "1234");
//		assertThat( mav.getViewName(), is("redirect:/a/find_password?fail=ne"));
//		
//		// 패스워드 보내기 전이니 keygen 은 없어야함
//		authToken = authTokenService.getRecentAuthToken(admin1.getUser().getId(), AuthToken.TYPE_PASSWORD);
//		assertNull(authToken);
//		
//		/**
//		 * 4. 패스워드 찾기
//		 */
//		mav = adminController.sendPassword(null, admin1.getUser().getEmail());
//		assertThat( mav.getViewName(), is( "redirect:/a/sent_password" ));
//		
//		// 패스워드 인증한 뒤니 키젠이 생겨야함.
//		authToken = authTokenService.getRecentAuthToken(admin1.getUser().getId(), AuthToken.TYPE_PASSWORD);
//		assertNotNull(authToken);
//		
//		
//		/**
//		 * 
//		 * 패스워드 키 체크
//		 */
//		Map<String, Object> retMap = null;
//		mav = adminController.resetPassword(admin1.getUser().getId(), "이상한값");
//		retMap = mav.getModel();
//		assertThat((boolean) retMap.get("isValidKey"), is(false));
//		
//		mav = adminController.resetPassword(admin1.getUser().getId(), authToken.getValue());
//		retMap = mav.getModel();
//		assertThat((boolean) retMap.get("isValidKey"), is(true));
//		
//		/**
//		 * 패스워드 변경
//		 */
//		// 이상한값
//		mav = adminController.alterPassword(1234L, "ㅏㅏㅏ", "1234");
//		assertThat( mav.getViewName(), is("/a/reset_password"));
//
//		// 제대로 변경
//		mav = adminController.alterPassword(admin1.getUser().getId(), authToken.getValue(), "1234");
//		assertThat( mav.getViewName(), is("redirect:/a/finish_password"));
//		authToken = authTokenService.getRecentAuthToken(admin1.getUser().getId(), AuthToken.TYPE_PASSWORD);
//		assertThat(authToken.getValue(), is(""));
//		
//		/**
//		 * 비밀번호 되돌리기
//		 */
//		mav = adminController.sendPassword(null, admin1.getUser().getEmail());
//		authToken = authTokenService.getRecentAuthToken(admin1.getUser().getId(), AuthToken.TYPE_PASSWORD);
//		mav = adminController.alterPassword(admin1.getUser().getId(), authToken.getValue(), UserBuilder.USER1_PASSWORD);
//		assertThat( mav.getViewName(), is("redirect:/a/finish_password"));
//		
	}
	
	ServletRequestAttributes sra;
	RequestAttributes requestAttributes;

	protected static MockHttpSession session;
	protected MockHttpServletRequest request;
	
	@BeforeClass
	public static void onTimeSetUp(){
		startSession();
	}
	
	@AfterClass
    public static void oneTimeTearDown() {
		endSession();
	}

	protected SecurityUser securityUser1;
	User user1;
	
	@Before
	public void setUp()  {
		startRequest();
		securityUser1 = SecurityUserBuilder.generator().test1().build();
		Authentication auth = new UsernamePasswordAuthenticationToken("none",null);
		SecurityContextHolder.getContext().setAuthentication(auth);

		user1 = UserBuilder.generator().test1().build();
		
//		insert into ROLE values(0, 'SUPER_ADMIN', 'SUPER_ADMIN');
//		insert into ROLE values(1, 'ADMIN', 'ADMIN');
//		insert into ROLE values(2, 'USER', 'USER');
//		insert into ROLE values(100, 'INACTIVE_ADMIN', 'INACTIVE_ADMIN');
//		insert into ROLE values(101, 'INACTIVE_USER', 'INACTIVE_USER');
		
		// 매번 새로운 테이블이 만들어지는 것 같아서 Role객체를 저장함.
		// 기본 값을 저장하는 방법이 필요할듯. 
		// db unit인가 몬가로 했던거 같은데??
//		Role role1 = new Role(0);
//		role1.setDescription("SUPER_ADMIN");
//		role1.setName("SUPER_ADMIN");
//		roleRepository.save(role1);
//
//		Role role2 = new Role(1);
//		role1.setDescription("ADMIN");
//		role1.setName("ADMIN");
//		roleRepository.save(role2);
//		
//		Role role3 = new Role(2);
//		role1.setDescription("USER");
//		role1.setName("USER");
//		roleRepository.save(role3);
//		
//		Role role4 = new Role(100);
//		role1.setDescription("INACTIVE_ADMIN");
//		role1.setName("INACTIVE_ADMIN");
//		roleRepository.save(role4);
//		
//		Role role5 = new Role(101);
//		role1.setDescription("INACTIVE_USER");
//		role1.setName("INACTIVE_USER");
//		roleRepository.save(role5);	
	}

	@After
	public void tearDown(){
		endRequest();
	}
	
	public <T> BindingResult filterModelAttribute( T object, String targetName ){

		BindingResult bindingResult = new BeanPropertyBindingResult(object, targetName);
		
		Validator validator = Validation.buildDefaultValidatorFactory()
				.getValidator();
		Set<ConstraintViolation<T>> validatorResults = validator
				.validate(object);

		if (validatorResults.isEmpty() == false) {
			for (ConstraintViolation<T> validatorResult : validatorResults) {
				bindingResult.rejectValue(validatorResult.getPropertyPath()
						.toString(), validatorResult.getMessage());
			}
		}
		return bindingResult;
	}
	
	protected static void startSession() {
		session = new MockHttpSession();
	}

	protected static void endSession() {
		session.clearAttributes();
		session = null;
	}

	protected void startRequest() {
		request = new MockHttpServletRequest();
		request.setSession(session);
		request.setServerName( "redwood.test.com");
		request.setRequestURI("/");
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(
				request));
	}

	protected void endRequest() {
		((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.requestCompleted();
		RequestContextHolder.resetRequestAttributes();
		request = null;
	}

	
	
}
