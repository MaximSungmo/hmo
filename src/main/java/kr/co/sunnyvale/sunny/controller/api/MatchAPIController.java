package kr.co.sunnyvale.sunny.controller.api;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import kr.co.sunnyvale.sunny.annotation.AuthUser;
import kr.co.sunnyvale.sunny.annotation.ParseSunny;
import kr.co.sunnyvale.sunny.domain.Site;
import kr.co.sunnyvale.sunny.domain.SmallGroup;
import kr.co.sunnyvale.sunny.domain.dto.JsonResult;
import kr.co.sunnyvale.sunny.domain.dto.SmallGroupDTO;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.exception.NotFoundMessageSourceException;
import kr.co.sunnyvale.sunny.service.SiteService;
import kr.co.sunnyvale.sunny.service.SmallGroupService;
import kr.co.sunnyvale.sunny.service.UserService;
import kr.co.sunnyvale.sunny.util.LocaleUtils;
import kr.co.sunnyvale.sunny.util.LoginUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

@Controller
public class MatchAPIController {

	@Autowired
	private UserService userService;

	@Autowired
	private SiteService siteService;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private SmallGroupService smallGroupService; 
	
	
	
	@RequestMapping(value = "/{path}/match/department", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public List<SmallGroupDTO> matchDepartment(
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			String key) {

		LoginUtils.checkLogin(securityUser);
		Site site = sunny.getSite();
		Stream stream = null;
		//List<UserDTO> matchUsers = userService.getMatchUsers(site, securityUser.getUserId(), key, stream);
		List<SmallGroupDTO> matchSmallGroups = smallGroupService.getMatchList( site, securityUser.getUserId(), key, SmallGroup.TYPE_DEPARTMENT, stream);
		
		if( matchSmallGroups == null )
			matchSmallGroups = Lists.newArrayList();
		
//		SmallGroupDTO s = new SmallGroupDTO();
//		s.setValue("aaaabb ababbbba aaaaaaaaaaaaaaaaaaaa");
////		s.setId(3L);
//		matchSmallGroups.add(s);
//		
//		SmallGroupDTO s2 = new SmallGroupDTO();
//		s2.setValue("caBbbbbbbbbbbbbb");
////		s2.setId(4L);
//		matchSmallGroups.add(s2);
		
		System.out.println("사이즈 ? : " + matchSmallGroups.size());
		
		return matchSmallGroups;
		
		//return new JsonResult(true, messageSource.getMessage(
		//		"user.getMatchList", null, LocaleUtils.getLocale()), matchSmallGroups);
	}
	@RequestMapping(value = "/match/department", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public List<SmallGroupDTO> matchList(
			@ParseSunny(shouldExistsSite=false) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			String key) {
		return matchDepartment(null, sunny, securityUser, key);
	}
	
//	@ResponseBody
//	@RequestMapping(value = "/{domainName}/user/regist", method = RequestMethod.POST, headers = { "Accept=application/json" })
//	public JsonResult registUser( 
//			@PathVariable("domainName") String domainName,
//			@ParseSunny(shouldExistsSite=false) Sunny sunny,
//			@AuthUser SecurityUser securityUser,
//			@RequestBody User user){
//
//		if( securityUser == null ) {
//			throw new NotLoggedInUserException();
//		}
//		// 이메일 중복 
//		if( userService.existsEmail( user.getEmail() ) == true ) {
//			throw new SimpleSunnyException( "user.EmailExists" );
//		}
//		
//		Validator validator = Validation.buildDefaultValidatorFactory()
//				.getValidator();
//		
//		Set<ConstraintViolation<User>> validatorResults = validator
//				.validate(user);
//
//		if (validatorResults.isEmpty() == false) {
//			throw new NotValidSignupFormException();
//		}
//		Site site = siteService.getSiteFromDomain(domainName);
//		userService.registUser(site, user);
//		return new JsonResult(true, messageSource.getMessage("user.successJoined", null, LocaleUtils.getLocale()), null);
//	}

//	@ResponseBody
//	@RequestMapping(value = "/user/regist", method = RequestMethod.POST, headers = { "Accept=application/json" })
//	public JsonResult registUser( 
//			@AuthUser SecurityUser securityUser,
//			@RequestBody User user){
//		return registUser(RequestUtils.getCurrentServerName(), securityUser, user);
//	}
//	
	private <T> JsonResult userValidMessage(
			Set<ConstraintViolation<T>> validatorResults) {
		for (ConstraintViolation<T> validatorResult : validatorResults) {
			return new JsonResult(false, validatorResult.getMessage(), null);
		}

		throw new NotFoundMessageSourceException();
	}
}
