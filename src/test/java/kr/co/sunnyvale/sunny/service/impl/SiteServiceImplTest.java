package kr.co.sunnyvale.sunny.service.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import kr.co.sunnyvale.sunny.builder.SiteBuilder;
import kr.co.sunnyvale.sunny.repository.hibernate.SiteRepository;
import kr.co.sunnyvale.sunny.service.SiteService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;


@RunWith(MockitoJUnitRunner.class)
public class SiteServiceImplTest {
	
	@Mock
	private SiteRepository siteRepository;
	
	@InjectMocks
	private SiteServiceImpl siteService;
	
	@Test
	@Rollback(true)
	public void checkDomain() throws Exception{
		
		when(siteRepository.findColumnByObject("id", "domain", "false")).thenReturn(null);
		when(siteRepository.findColumnByObject("id", "domain", SiteBuilder.SITE1_DOMAIN)).thenReturn(1L);
		
		assertThat(siteService.checkDomain("false"), is(false));
		
		assertThat(siteService.checkDomain(null), is(false));
		
		assertThat(siteService.checkDomain(SiteBuilder.SITE1_DOMAIN), is(true));
	}
}
