package kr.co.sunnyvale.sunny.controller.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.sunnyvale.sunny.annotation.AuthUser;
import kr.co.sunnyvale.sunny.annotation.ParseSunny;
import kr.co.sunnyvale.sunny.domain.Report;
import kr.co.sunnyvale.sunny.domain.dto.JsonResult;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.service.impl.ReportService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ReportAPIController {
	
	@Autowired
	private ReportService reportService; 
	
	@RequestMapping( value = "/report/post", method = RequestMethod.POST, headers = { "Accept=application/json" })
	@ResponseBody
	public JsonResult postReport(
			@ParseSunny(shouldExistsSite = false) Sunny sunny,
			@AuthUser SecurityUser securityUser, 
			@RequestBody Report report,
			HttpServletRequest request,
			HttpServletResponse response){
		
		if( securityUser != null ){
			report.setUserId( securityUser.getUserId() );
		}
		
		reportService.save(report);
		return new JsonResult(true, "리포트를 저장했습니다.",  null);
	}
	
}
