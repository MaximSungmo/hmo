package kr.co.sunnyvale.sunny.test.integration.tests;

import kr.co.sunnyvale.sunny.builder.SecurityUserBuilder;
import kr.co.sunnyvale.sunny.builder.UserBuilder;
import kr.co.sunnyvale.sunny.controller.web.SuperAdminController;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
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
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration({
	"file:src/main/webapp/WEB-INF/sunny-servlet.xml",
	"classpath:root-context.xml", 
	"classpath:security-context.xml",
	"file:src/test/resources/testPersistenceContext.xml",
	"file:src/test/resources/testContext.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback=true)
public class SuperAdminCreateServiceTest {

	Logger logger = Logger.getLogger(getClass());

	@Autowired
	private SiteService siteService; 
	
	@Autowired
	private SuperAdminController superAdminController;

//	@Test
//	@Transactional(noRollbackFor = Throwable.class)
//	@Rollback(false)
//	public void testGenerateDefaults()  {
//		superAdminController.generateDefaultRoleAndEvaluate();
//	}
	
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
