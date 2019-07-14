//package kr.co.sunnyvale.sunny.service;
//
//import static org.junit.Assert.assertTrue;
//import static org.mockito.Mockito.when;
//import kr.co.sunnyvale.sunny.builder.SiteBuilder;
//import kr.co.sunnyvale.sunny.domain.Site;
//import kr.co.sunnyvale.sunny.domain.User;
//import kr.co.sunnyvale.sunny.util.RequestUtils;
//
//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mock.web.MockHttpServletRequest;
//import org.springframework.mock.web.MockHttpSession;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.transaction.TransactionConfiguration;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.context.request.RequestAttributes;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@Transactional
//@ContextConfiguration(locations = { "classpath:root-context.xml", "classpath:security-context.xml" ,"classpath:persistence-context.xml"})
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback=true)
//public class UserServiceTransactionTest {
//	@Autowired
//	UserService userService;
//	
//	@Autowired
//	private SiteService siteService; 
//	
//	@Test
//	@Rollback(value =true)
//	public void testTest() throws Exception{
//		assertTrue(true);
//	}
//	
//	@Test
//	@Rollback(value =true)
//	public void registAndConfirmUser() throws Exception{
//		User user = new User();
//		user.setEmail("carami@sunnyvale.co.kr");
//		user.setName("강경미");
//		user.setPassword("1234");
//		
//		User returnUser = userService.registAndConfirmUser(user);
//		System.out.println("value = " + returnUser);
//	}
//
//	@Test
//	@Rollback(value =true)
//	public void registAndConfirmAdmin() throws Exception{
//		User user = new User();
//		user.setEmail("urstory@sunnyvale.co.kr");
//		user.setName("김성박");
//		user.setPassword("1234");
//		
//		Site site = new Site();
//		site.setCompanyName("써니베일");
//		site.setDomain("sunnyvale.co.kr");
//		
//		User returnUser = userService.registAndConfirmAdmin(site, user);
//		System.out.println("value = " + returnUser);
//		
//	}
//	
//	ServletRequestAttributes sra;
//	RequestAttributes requestAttributes;
//
//	protected static MockHttpSession session;
//	protected MockHttpServletRequest request;
//	
//	@BeforeClass
//	public static void onTimeSetUp(){
//		startSession();
//	}
//	
//	@AfterClass
//    public static void oneTimeTearDown() {
//		endSession();
//	}
//	
//	@Before
//	public void setUp()  {
//		startRequest();
//		Authentication auth = new UsernamePasswordAuthenticationToken("none",null);
//		SecurityContextHolder.getContext().setAuthentication(auth);
//		when(siteService.getSiteFromDomain( RequestUtils.getCurrentServerName() )).thenReturn(SiteBuilder.generator().test1().build());
//		
//	}
//
//	@After
//	public void tearDown(){
//		endRequest();
//	}
//	protected static void startSession() {
//		session = new MockHttpSession();
//	}
//
//	protected static void endSession() {
//		session.clearAttributes();
//		session = null;
//	}
//
//	protected void startRequest() {
//		request = new MockHttpServletRequest();
//		request.setSession(session);
//		request.setServerName( "redwood.test.com");
//		request.setRequestURI("/");
//		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(
//				request));
//	}
//
//	protected void endRequest() {
//		((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
//				.requestCompleted();
//		RequestContextHolder.resetRequestAttributes();
//		request = null;
//	}	
//}
