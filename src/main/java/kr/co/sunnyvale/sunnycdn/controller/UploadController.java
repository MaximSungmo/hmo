package kr.co.sunnyvale.sunnycdn.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import kr.co.sunnyvale.sunny.annotation.AuthUser;
import kr.co.sunnyvale.sunny.domain.CdnJsonResult;
import kr.co.sunnyvale.sunny.domain.Media;
import kr.co.sunnyvale.sunny.domain.Site;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.service.SiteService;
import kr.co.sunnyvale.sunny.service.UserService;
import kr.co.sunnyvale.sunny.util.StringUtils;
import kr.co.sunnyvale.sunnycdn.service.FileService;
import kr.co.sunnyvale.sunnycdn.service.MediaCDNService;
import kr.co.sunnyvale.sunnycdn.service.RetouchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;

import com.fasterxml.jackson.core.JsonProcessingException;

@Controller
public class UploadController {

	@Autowired
	RetouchService retouchService;

	@Autowired
	MediaCDNService mediaCDNService;

	@Autowired
	FileService fileService;

	@Autowired
	private UserService userService;

	@Autowired
	private SiteService siteService;

	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public String upload( @AuthUser SecurityUser securityUser, Model model ) {

		if (securityUser == null ) {
			System.out.println("로그인 되지 않았음");
		} 
		
		return "/upload/index";
	}
	
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String upload(@AuthUser SecurityUser securityUser,
		@RequestParam("file") List<MultipartFile> files, 
		@RequestParam(value = "documentDomain", required=false) String documentDomain,
		@RequestParam(value = "upid", required=false) String upid,
		@RequestParam(value = "did", required=false) Long draftId,
		Model model ) throws JsonProcessingException  {

		if( documentDomain == null ) {
			ServletRequestAttributes servletRequestAttributes = ( ServletRequestAttributes ) RequestContextHolder.currentRequestAttributes();       
			documentDomain = StringUtils.getBaseDomain( servletRequestAttributes.getRequest().getServerName() );
		}

		model.addAttribute( "upid", upid );
		model.addAttribute( "documentDomain", documentDomain );
		
		try{

			if (securityUser == null ) {
				model.addAttribute("cdnJsonResult", new CdnJsonResult(CdnJsonResult.RESULT_CODE_SESSION, "실패", null).toJsonString());
				return "/upload/failure";
			}
			
			if (files.isEmpty() == true) {
				model.addAttribute("cdnJsonResult", new CdnJsonResult(CdnJsonResult.RESULT_CODE_UNKNOWN_FAIL, "실패", null).toJsonString());
				return "/upload/failure";
			}
	
			// 테스트용도임
			Site site = siteService.findById(securityUser.getSiteId());
			User user = userService.findById(securityUser.getUserId());
	
			List<Media> mediaes = new ArrayList<Media>();
			
			for( MultipartFile file : files ){
				if( file.isEmpty() == false ){
					
					Media media = mediaCDNService.save(site, draftId, file, user);
					fileService.save(media, file, user);	
					mediaes.add( media );
				}
			}
			
			List<Map<String, Object>> retMaps = new ArrayList<Map<String, Object>>();
			for( Media media : mediaes ){
				retMaps.add(media.getDTOMap());	
			}
			
			model.addAttribute("cdnJsonResult", new CdnJsonResult(CdnJsonResult.RESULT_CODE_SUCCESS, "성공", retMaps).toJsonString());
		
		}catch(Exception ex){
			ex.printStackTrace();
			model.addAttribute("cdnJsonResult", new CdnJsonResult(CdnJsonResult.RESULT_CODE_UNKNOWN_FAIL, "실패", null).toJsonString());
			return "/upload/failure";
			
		}
		
		return "/upload/success";
	}

	@RequestMapping(value = "/upload/profile", method = RequestMethod.GET)
	public String uploadProfileForm(Model model) {
		return "/upload/profile/index";
	}

	@RequestMapping(value = "/upload/profile", method = RequestMethod.POST)
	public String uploadProfile(
		@AuthUser SecurityUser securityUser,
		@RequestParam("profilePic") MultipartFile file, 
		@RequestParam(value = "documentDomain", required=false) String documentDomain,
		@RequestParam(value = "temporary-iframe-id", required=false) String temporaryIframeId,
		Model model) throws JsonProcessingException  {

		if( documentDomain == null ) {
			ServletRequestAttributes servletRequestAttributes = ( ServletRequestAttributes ) RequestContextHolder.currentRequestAttributes();       
			documentDomain = StringUtils.getBaseDomain( servletRequestAttributes.getRequest().getServerName() );
		}

		model.addAttribute("temporaryIframeId", temporaryIframeId);
		model.addAttribute( "documentDomain", documentDomain );
		
		try{

			if ( securityUser == null ) {
				model.addAttribute("cdnJsonResult", new CdnJsonResult(CdnJsonResult.RESULT_CODE_SESSION, "실패1", null).toJsonString());
				return "/upload/profile/failure";
			}
			
			if ( file.isEmpty() == true ) {
				model.addAttribute("cdnJsonResult", new CdnJsonResult(CdnJsonResult.RESULT_CODE_UNKNOWN_FAIL, "실패2", null).toJsonString());
				return "/upload/profile/failure";
			}

			Site site = siteService.findById( securityUser.getSiteId() );
			User user = userService.findById( securityUser.getUserId() );
	
			Media media = mediaCDNService.save( site, null, file, user );

			fileService.saveProfile( media, file, user );	
			
			model.addAttribute( "cdnJsonResult", new CdnJsonResult(CdnJsonResult.RESULT_CODE_SUCCESS, "성공", media ).toJsonString());
			
		} catch( Exception ex ) {
			
			//ex.printStackTrace();
			model.addAttribute("cdnJsonResult", new CdnJsonResult( CdnJsonResult.RESULT_CODE_UNKNOWN_FAIL, "실패3", null ).toJsonString());
			
			return "/upload/profile/failure";
			
		}
		
		return "/upload/profile/success";
	}

}