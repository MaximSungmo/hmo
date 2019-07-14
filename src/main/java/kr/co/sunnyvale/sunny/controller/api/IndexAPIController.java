package kr.co.sunnyvale.sunny.controller.api;

import kr.co.sunnyvale.sunny.domain.dto.JsonResult;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexAPIController {
	
	@RequestMapping( value = "/ping", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult pring(){
		return new JsonResult(true, "pong", null);
	}
	
}
