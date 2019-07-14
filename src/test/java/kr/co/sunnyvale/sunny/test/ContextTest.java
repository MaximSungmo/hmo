package kr.co.sunnyvale.sunny.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration({
	"file:src/main/webapp/WEB-INF/sunny-servlet.xml",
	"classpath:root-context.xml", 
	"classpath:security-context.xml",
	"file:src/test/resources/testPersistenceContext.xml",
	"file:src/test/resources/testContext.xml"})
public class ContextTest {
    @Test
    public void initialize() throws Exception {
    }
}
