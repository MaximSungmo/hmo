package kr.co.sunnyvale.sunny.service.impl;

import static org.junit.Assert.fail;
import kr.co.sunnyvale.sunny.domain.Site;
import kr.co.sunnyvale.sunny.repository.hibernate.ContentRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.SmallGroupRepository;
import kr.co.sunnyvale.sunny.service.SiteService;

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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@RunWith(MockitoJUnitRunner.class)
public class SmallGroupServiceImplTest {

	@Mock
	private SiteService siteService; 
	
	@Mock
	private SmallGroupRepository smallGroupRepository;
	
	@Mock
	private ContentRepository storyRepository;
	
	@InjectMocks
	private SmallGroupServiceImpl smallGroupServiceImpl;
	
	
	

	/**
	 * save 뒤에 id가 생성되는 것은 Hibernate 관련된 부분이라 목업 테스트는 좀 힘듬.
	 * @throws Exception
	 */
//	@Test
//	public void testSavePlazaSmallGroup() throws Exception {
//		
//		Site site = null;
//				
//		try {
//			smallGroupServiceImpl.saveLobbySmallGroup(site);
//			fail("null 값 넘어감");
//		}catch(Exception ex){}
//		
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
		request.setServerName( "hmo.sunnyvale.co.kr");
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
