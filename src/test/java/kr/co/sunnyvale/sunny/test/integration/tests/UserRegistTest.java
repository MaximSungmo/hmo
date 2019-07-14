package kr.co.sunnyvale.sunny.test.integration.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import kr.co.sunnyvale.sunny.builder.SecurityUserBuilder;
import kr.co.sunnyvale.sunny.builder.SiteBuilder;
import kr.co.sunnyvale.sunny.builder.UserBuilder;
import kr.co.sunnyvale.sunny.controller.api.UserAPIController;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.dto.JsonResult;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
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
public class UserRegistTest {

	Logger logger = Logger.getLogger(getClass());

	@Autowired
	private SiteService siteService; 
	
	@Autowired
	private UserAPIController userApiController;
	
	@Autowired
	private AuthTokenService authTokenService;

	ModelAndView mav;
	JsonResult jret;
	
	@Test
	@Transactional(noRollbackFor = Throwable.class)
	@Rollback(false)
	public void test사용자추가()  {
		
		/**
		 * 관리자가 유저를 추가
		 */
		System.out.println("사용자 추가");
		User user5 = UserBuilder.generator().test5().build();
		try{
//			jret = userApiController.registUser(Long.toString(SiteBuilder.SITE1_ID), null, securityUser1, user5);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		assertThat(jret.getResult(), is("success"));
		
		
//		SecurityUser securityUser2 = SecurityUserBuilder.generator().test2().build();
//		jret = userApiController.registUser(securityUser2, user4);
//		assertThat(jret.getResult(), is("fail"));
//	
//		User user3 = UserBuilder.generator().test3().build();
//		jret = userApiController.registUser(securityUser2, user4);
//		assertThat(jret.getResult(), is("fail"));

		/**
		 * 일반 사용자가 유저를 추가 Exception
		 */

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
