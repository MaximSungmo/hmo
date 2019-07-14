//package kr.co.sunnyvale.sunny.service.impl;
//
//import static org.junit.Assert.fail;
//import static org.mockito.Mockito.when;
//
//import java.util.List;
//import java.util.Set;
//
//import kr.co.sunnyvale.sunny.builder.SiteBuilder;
//import kr.co.sunnyvale.sunny.builder.SmallGroupBuilder;
//import kr.co.sunnyvale.sunny.builder.UserBuilder;
//import kr.co.sunnyvale.sunny.domain.AccessGroup;
//import kr.co.sunnyvale.sunny.domain.Site;
//import kr.co.sunnyvale.sunny.domain.Story;
//import kr.co.sunnyvale.sunny.domain.User;
//import kr.co.sunnyvale.sunny.domain.dto.StoryDTO;
//import kr.co.sunnyvale.sunny.domain.post.StoryPostDTO;
//import kr.co.sunnyvale.sunny.exception.SunnyException;
//import kr.co.sunnyvale.sunny.repository.hibernate.SmallGroupRepository;
//import kr.co.sunnyvale.sunny.repository.hibernate.StoryRepository;
//import kr.co.sunnyvale.sunny.repository.hibernate.UserRepository;
//import kr.co.sunnyvale.sunny.service.SiteService;
//import kr.co.sunnyvale.sunny.util.RequestUtils;
//
//import org.apache.log4j.Logger;
//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.runners.MockitoJUnitRunner;
//import org.springframework.mock.web.MockHttpServletRequest;
//import org.springframework.mock.web.MockHttpSession;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.context.request.RequestAttributes;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import com.google.common.collect.Sets;
//
//
//@RunWith(MockitoJUnitRunner.class)
//public class StoryServiceImplTest {
//
//	Logger logger = Logger.getLogger(getClass());
//
//	@Mock
//	private SiteService siteService; 
//	
//	@Mock
//	private UserRepository userRepository;
//	
//	@Mock
//	private StoryRepository storyRepository;
//
//	@Mock
//	private SmallGroupRepository smallGroupRepository;
//	
//	@InjectMocks
//	private StoryServiceImpl storyServiceImpl;
//	
//	
//	
//	
//	@Test
//	public void testPostStory() throws Exception {
//		StoryPostDTO storyPostDTO = null;
//		Site site = SiteBuilder.generator().test1().build();
//		try{
//			storyServiceImpl.postStory(site, storyPostDTO);
//			fail("널 체크 안됨");
//		}catch(Exception ex){}
//		
//		storyPostDTO = new StoryPostDTO();
//		try{
//			storyServiceImpl.postStory(site,storyPostDTO);
//			fail("필수 요소들이 안채워져있음");
//		}catch(Exception ex){};
//		
//		Set<AccessGroup> accessGroups = Sets.newHashSet();
//		storyPostDTO.setAccessGroups(accessGroups);
//		storyPostDTO.setUserId(UserBuilder.USER1_ID);
//		storyServiceImpl.postStory(site, storyPostDTO);
//		
//		
//	}
//	
//	@Test
//	public void testFetchStories() throws Exception {
//		
//		Long smallGroupId = null;
//		User user = null;
//		try{
//			storyServiceImpl.fetchLobbyStories(smallGroupId, user);
//			fail("널 체크 안됨");
//		}catch(Exception ex){}
//		
//		smallGroupId = 0L;
//		user = UserBuilder.generator().test1().build();
//	
//		List<StoryDTO> stories = null;
//		
//		try{
//			stories = storyServiceImpl.fetchStories(smallGroupId, user);
//			fail("안됨");
//		}catch(SunnyException ex){}
//		catch(Exception ex){fail("여긴 들어오면 안됨");}
//				
//		when(smallGroupRepository.select(SmallGroupBuilder.SMALL_GROUP_1_ID)).thenReturn(SmallGroupBuilder.generator().test1().build());
//		
//		smallGroupId = SmallGroupBuilder.SMALL_GROUP_1_ID;
//		stories = storyServiceImpl.fetchStories(smallGroupId, user);
//		
//	}
//	
//	
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
//
//	
//
//}
