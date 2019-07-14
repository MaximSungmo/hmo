package kr.co.sunnyvale.sunny.test.integration.suite;


import junit.framework.Test;
import junit.framework.TestSuite;
import kr.co.sunnyvale.sunny.test.integration.tests.AdminSignupTest;
import kr.co.sunnyvale.sunny.test.integration.tests.StoryListTest;
import kr.co.sunnyvale.sunny.test.integration.tests.StoryWriteTest;
import kr.co.sunnyvale.sunny.test.integration.tests.SuperAdminCreateServiceTest;
import kr.co.sunnyvale.sunny.test.integration.tests.UserRegistTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
			SuperAdminCreateServiceTest.class, 
			AdminSignupTest.class,
			UserRegistTest.class,
			StoryWriteTest.class,
			StoryListTest.class
			})
public class SunnyIntegrationSuiteTests {
	public static Test suite(){
		TestSuite suite = new TestSuite("시나리오 1 테스트");
		
		return suite;
	}
}
