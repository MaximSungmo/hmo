package kr.co.sunnyvale.sunny.service.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import kr.co.sunnyvale.sunny.builder.SiteBuilder;
import kr.co.sunnyvale.sunny.builder.UserBuilder;
import kr.co.sunnyvale.sunny.domain.AuthToken;
import kr.co.sunnyvale.sunny.domain.Site;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.post.AdminJoinDTO;
import kr.co.sunnyvale.sunny.exception.NoExistsUserException;
import kr.co.sunnyvale.sunny.repository.hibernate.AuthTokenRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.SiteRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.UserRepository;
import kr.co.sunnyvale.sunny.service.MailService;
import kr.co.sunnyvale.sunny.service.SiteService;
import kr.co.sunnyvale.sunny.service.SmallGroupService;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {
	
	Logger logger = Logger.getLogger(getClass());
	
	@Mock
	private SiteRepository siteRepository;
	
	@Mock
	private SiteService siteService; 
	
	@Mock
	private SmallGroupService smallGroupService;
	
	@Mock
	private UserRepository userRepository;
	
//	@Mock
//	private AdminRepository adminRepository; 
	
	@Mock
	private AuthTokenRepository authTokenRepository;
	
	@Mock
	private MailService mailService;
	
	@Mock
	private PasswordEncoder passwordEncoder;
	
	@InjectMocks
	private UserServiceImpl userServiceImpl;
	
	@Test
	public void testExistsEmail() throws Exception {
		
		when(userRepository.existsEmail("none")).thenReturn(false);
		when(userRepository.existsEmail(UserBuilder.USER1_EMAIL)).thenReturn(true);
		when(userRepository.existsEmail(UserBuilder.USER2_EMAIL)).thenReturn(false);
		
		assertThat(userServiceImpl.existsEmail(null), is(false));
		assertThat(userServiceImpl.existsEmail("none"), is(false));
		assertThat(userServiceImpl.existsEmail(UserBuilder.USER1_EMAIL), is(true));
		assertThat(userServiceImpl.existsEmail(UserBuilder.USER2_EMAIL), is(false));
	}

	@Test
	public void testRegistAdmin() throws Exception {
		
		AdminJoinDTO adminJoinDTO = null;
		
		try{
			userServiceImpl.registAdmin(adminJoinDTO);
			fail("널 체크 안됨");
		}catch(Exception ex){}
		
		
//		Admin admin = AdminBuilder.generator().test1().build();
		
		try{
			userServiceImpl.registAdmin(adminJoinDTO);
		}catch(Exception ex){
			ex.printStackTrace();
			logger.warn(ex);
			fail("잘 넣어짐");
		}
	}
	
//	@Test
//	public void testRegistUser() throws Exception{
//		UserRegistPostDTO userRegistDTO = null;
//		try{
//			userServiceImpl.registUser(site, userRegistDTO);
//			fail("널 체크 안됨");
//		}catch(Exception ex){}
//		
//		User user = UserBuilder.generator().test1().build();
//		
//		try{
//			userServiceImpl.registUser(site, userRegistDTO);
//		}catch(Exception ex){
//			ex.printStackTrace();
//			logger.warn(ex);
//			fail("잘 넣어짐");
//		}		
//	}

	@Test
	public void testConfirmAdmin() throws Exception {
		
		String authTokenValue = null;
		Long userId = null; 
		
		try{
			userServiceImpl.confirmAdmin(authTokenValue, userId);
			fail("널 체크 안됨");
		}catch(Exception ex){}
		
		authTokenValue = "correct";
		userId = 0L;
		when(userRepository.findUniqByObject("id", userId)).thenReturn(null);
		when(userRepository.findUniqByObject("id", UserBuilder.USER1_ID)).thenReturn(UserBuilder.generator().test1().build());
		
		try{
			userServiceImpl.confirmAdmin(authTokenValue, userId);
			fail("널 체크 안됨");
		}catch(NoExistsUserException ex){
			
		}catch(Exception ex){
			ex.printStackTrace();
			fail("이상힌 Exception 발생");
		}
		
		userId = UserBuilder.USER1_ID;
		AuthToken retToken = new AuthToken();
		retToken.setUser(UserBuilder.generator().test1().build());
		when(authTokenRepository.getCorrectAuthToken(userId, authTokenValue, AuthToken.TYPE_ACTIVATE_ADMIN)).thenReturn(retToken);
		
		
		User user = userServiceImpl.confirmAdmin(authTokenValue, userId);
		
//		List<Role> roles = user.getRoles();
//		assertThat(roles.size(), is(2));
		
	}
	
	

//	@Test
//	public void testRegistAndConfirmAdmin() throws Exception{
//		Site site = null;
//		User user = null;
//		
//		try{
//			userServiceImpl.registAndConfirmAdmin(site, user);
//			fail("널 체크 안됨");
//		}catch(Exception ex){}
//		
//		site = SiteBuilder.generator().test1().build();
//		user = UserBuilder.generator().test1().build();
//		
//		try{
//			userServiceImpl.registAndConfirmAdmin(site, user);
//		}catch(Exception ex){
//			ex.printStackTrace();
//			logger.warn(ex);
//			fail("잘 넣어짐");
//		}
//
//		site = SiteBuilder.generator().test1().build();
//		user = UserBuilder.generator().test1().build();
//		try{
//			userServiceImpl.registAndConfirmAdmin(site, user);
//			when(userRepository.findUniqByObject("id", UserBuilder.USER1_ID)).thenReturn(UserBuilder.generator().test1().build());
//		}catch(Exception ex){
//			System.out.println("exception :" + ex);
//		}
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
	
	
	Site site = SiteBuilder.generator().test1().build();
	@Before
	public void setUp()  {
		startRequest();
		Authentication auth = new UsernamePasswordAuthenticationToken("none",null);
		SecurityContextHolder.getContext().setAuthentication(auth);
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
