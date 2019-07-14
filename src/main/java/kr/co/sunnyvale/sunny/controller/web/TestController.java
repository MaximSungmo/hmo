package kr.co.sunnyvale.sunny.controller.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Qualifier;
import javax.servlet.http.HttpServletRequest;

import kr.co.sunnyvale.sunny.annotation.AuthUser;
import kr.co.sunnyvale.sunny.annotation.NoPathRedirect;
import kr.co.sunnyvale.sunny.annotation.ParseSunny;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.dto.StoryDTO;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.service.StoryService;
import kr.co.sunnyvale.sunny.service.UserService;
import kr.co.sunnyvale.sunny.util.SearchUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TestController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private StoryService storyService; 

	
	
//	@RequestMapping( value="/test/loginUsers")
//	public ModelAndView loginUsers( HttpServletRequest request ){
//		
//		ModelAndView modelAndView = new ModelAndView();
//		
//		List<Object> principals = sessionRegistry.getAllPrincipals();
//		
//		System.out.println(principals);
//		
//		modelAndView.setViewName( "/test/loginUsers");
//		return modelAndView;
//		
//	}
	
	@RequestMapping( value = "/{domainName}/testStory" )
	public ModelAndView testStory( 
			@PathVariable("domainName") String domainName,
			@ParseSunny(shouldExistsSite = true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestParam(value="q[]", required=false) String[] queries,
			@RequestParam(value="nq", required=false) String newQuery,
			@RequestParam(value="recursive", required=false) Boolean isRecursive,
			@RequestParam(value="last", required=false) String lastDateString ) throws ParseException {
		ModelAndView modelAndView = new ModelAndView();
		if( sunny.isGoRedirect() == true ){
			modelAndView.setViewName("redirect:" + sunny.getRedirectPath());
			return modelAndView;
		}
		User user = null;
		
		if( securityUser != null ){

			// 계정 비활성 사용자 ( Access Denied )
			if( securityUser.hasRole("ROLE_USER") != true ){
				modelAndView.setViewName("/error/403");
				return modelAndView;
			}

			user = userService.findById( securityUser.getUserId() );
		}

		Stream stream = new Stream(null, null, null, 10);
		if( lastDateString != null ){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date lastDate = format.parse(lastDateString);
			stream.setBaseColumn("createDate");
			stream.setGreaterThan(true);
			stream.setBaseData(lastDate);
			
		}
		
		if( queries != null ){
			queries = SearchUtils.checkAndFixQuery( queries, newQuery, isRecursive );
		}

//		Sunny sunny = sunnyService.parseCurrent();
		List<StoryDTO> plazaStories = storyService.fetchLobbyStories(sunny, user, queries, stream);
		
		modelAndView.addObject("stories",plazaStories);
		
		modelAndView.addObject("queries",  queries);

		modelAndView.addObject("sunny", sunny);
		modelAndView.setViewName("/test/story");
		return modelAndView;
	}
	
	@NoPathRedirect
	@RequestMapping( value = "/testStory" )
	public ModelAndView testStory( 
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
							@RequestParam(value="q[]", required=false) String[] queries,
							@RequestParam(value="nq", required=false) String newQuery,
							@RequestParam(value="recursive", required=false) Boolean isRecursive,
							@RequestParam(value="last", required=false) String lastDateString ) throws ParseException {
		return testStory( null, sunny, securityUser, queries, newQuery, isRecursive, lastDateString );
	}
}
