package kr.co.sunnyvale.sunny.controller.api;

import java.util.List;

import kr.co.sunnyvale.sunny.annotation.AuthUser;
import kr.co.sunnyvale.sunny.annotation.ParseSunny;
import kr.co.sunnyvale.sunny.domain.FeelAndContentAndUser;
import kr.co.sunnyvale.sunny.domain.dto.JsonResult;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.service.FeelService;
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
 * @author kickscar
 * 
 */
@Controller
public class FeelAPIController {

	@Autowired
	private FeelService feelService;

	/*
	 * *************************************************
	 * MultiSite *************************************************
	 */

	@RequestMapping(value = "/{path}/feel/names", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult feelNames(
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@RequestParam(value = "contentId", required = true) Long contentId,
			@RequestParam(value = "feelId", required = false) Integer feelId,
			@RequestParam(value = "feelUserId", required = false) Long feelUserId,
			@RequestParam(value = "top", required = false) Boolean top,
			@RequestParam(value = "size", required = false) Integer size) {

		List<String> feelNames = null;

		Stream stream;

		if (top == null)
			top = true;

		if (feelUserId == null)
			stream = new Stream();
		else
			stream = new Stream(top == true ? true : false, "id", feelUserId,
					size);

		if (feelId == null) {
			feelNames = feelService.getFeelNames(contentId, stream);
		} else {
			feelNames = feelService.getFeelNames(contentId, feelId, stream);
		}

		// if( feelNames.size() == 0 ){
		// return new JsonResult(true, "평가한 사용자가 없습니다.", null);
		// }

		return new JsonResult(true, "유저 리스트를 가져왔습니다.", feelNames);
	}

	@RequestMapping(value = "/{path}/feel/users", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult feelUsers(
			@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@RequestParam(value = "contentId", required = true) Long contentId,
			@RequestParam(value = "feelId", required = false) Integer feelId,
			@RequestParam(value = "feelUserId", required = false) Long feelUserId,
			@RequestParam(value = "top", required = false) Boolean top,
			@RequestParam(value = "size", required = false) Integer size) {

		List<FeelAndContentAndUser> feelUsers = null;

		Stream stream;

		if (top == null)
			top = true;

		if (feelUserId == null)
			stream = new Stream();
		else
			stream = new Stream(top == true ? true : false, "id", feelUserId,
					size);

		if (feelId == null) {
			feelUsers = feelService.getFeelUsers(contentId, stream);
		} else {
			feelUsers = feelService.getFeelUsers(contentId, feelId, stream);
		}

		// if( feelUsers.size() == 0 ){
		// return new JsonResult(true, "평가한 사용자가 없습니다.", null);
		// }

		return new JsonResult(true, "유저 리스트를 가져왔습니다.", feelUsers);

	}

	@RequestMapping(value = "/{path}/feel/okay", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult okay(@PathVariable("path") String path,
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value = "feelId", required = false) int feelId,
			Long contentId) {

		LoginUtils.checkLogin(securityUser);

		feelService.feel(sunny, securityUser.getUserId(), contentId, feelId);

		return new JsonResult(true, "평가가 성공적으로 수행되었습니다.", null);

	}

	/*
	 * MultiSiteProxy
	 */
	@RequestMapping(value = "/feel/names", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult feelNames(
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@RequestParam(value = "contentId", required = true) Long contentId,
			@RequestParam(value = "feelId", required = false) Integer feelId,
			@RequestParam(value = "feelUserId", required = false) Long feelUserId,
			@RequestParam(value = "top", required = false) Boolean top,
			@RequestParam(value = "size", required = false) Integer size) {

		return feelNames(null, sunny, contentId, feelId, feelUserId, top, size);

	}

	@RequestMapping(value = "/feel/users", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult feelUsers(
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@RequestParam(value = "contentId", required = true) Long contentId,
			@RequestParam(value = "feelId", required = false) Integer feelId,
			@RequestParam(value = "feelUserId", required = false) Long feelUserId,
			@RequestParam(value = "top", required = false) Boolean top,
			@RequestParam(value = "size", required = false) Integer size) {

		return feelUsers(null, sunny, contentId, feelId, feelUserId, top, size);

	}

	@RequestMapping(value = "/feel/okay", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult okay(@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@AuthUser SecurityUser securityUser,
			@RequestParam(value = "feelId", required = false) int feelId,
			Long contentId) {
		return okay(null, sunny, securityUser, feelId, contentId);

	}
}
