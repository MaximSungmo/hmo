package kr.co.sunnyvale.sunny.controller.api;

import java.util.List;

import kr.co.sunnyvale.sunny.annotation.AuthUser;
import kr.co.sunnyvale.sunny.annotation.ParseSunny;
import kr.co.sunnyvale.sunny.domain.SmallGroup;
import kr.co.sunnyvale.sunny.domain.dto.JsonResult;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.repository.hibernate.ContentService;
import kr.co.sunnyvale.sunny.service.SmallGroupService;
import kr.co.sunnyvale.sunny.util.LoginUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * {SpringSecurity}
 * 
 * 
 * @author kickscar
 *
 */
@Controller
public class ContentAPIController {
	
	@Autowired
	private ContentService contentService;
	
	@Autowired
	private SmallGroupService smallGroupService; 
	
	
	
	@RequestMapping( value = "/{path}/content/delete" , method = RequestMethod.GET, headers = { "Accept=application/json" } )
	@ResponseBody
	public JsonResult delete( 
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestParam(value="id", required=false) Long id) {

		LoginUtils.checkLogin(securityUser);
		contentService.delete(sunny, id);
		return new JsonResult(true, "success", null);
	}

	@RequestMapping( value = "/content/delete" , method = RequestMethod.GET, headers = { "Accept=application/json" } )
	@ResponseBody
	public JsonResult delete( 
			@ParseSunny(shouldExistsSite=true) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestParam(value="id", required=false) Long id) {
		return delete( null, sunny, securityUser, id );
	}
	
	
	@RequestMapping(value = "/content/permissions", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult contentPermissions(
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@RequestParam(value = "contentId", required = true) Long contentId,
			@RequestParam(value = "sgid", required = false) Long smallGroupId,
			@RequestParam(value = "top", required = false) Boolean top,
			@RequestParam(value = "size", required = false) Integer size) {

		List<SmallGroup> smallGroups = null;

		Stream stream;

		if (top == null)
			top = true;

		if (smallGroupId == null)
			stream = new Stream();
		else
			stream = new Stream(top == true ? true : false, "id", smallGroupId,
					size);

		smallGroups = smallGroupService.getContentAssignedSmallGroups(sunny, contentId, stream);

		// if( feelUsers.size() == 0 ){
		// return new JsonResult(true, "평가한 사용자가 없습니다.", null);
		// }

		return new JsonResult(true, "유저 리스트를 가져왔습니다.", smallGroups);

	}

}
