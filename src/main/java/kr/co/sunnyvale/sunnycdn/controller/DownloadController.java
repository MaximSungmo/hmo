package kr.co.sunnyvale.sunnycdn.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.sunnyvale.sunny.annotation.AuthUser;
import kr.co.sunnyvale.sunny.domain.CdnJsonResult;
import kr.co.sunnyvale.sunny.domain.Media;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.service.impl.OperationService;
import kr.co.sunnyvale.sunnycdn.service.MediaCDNService;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Handles requests for the application home page.
 */
@Controller
public class DownloadController {
	
	@Autowired
	private OperationService operationService;
	
	@Autowired
	private MediaCDNService mediaCDNService;
	
	@RequestMapping(value = "/download/{id}/{filename}", method = RequestMethod.GET  )
	public HttpEntity<byte[]> downloadFromPath(
			@AuthUser SecurityUser securityUser,
			@PathVariable("id") Long mediaId, 
			@PathVariable("filename") String filename,
			HttpServletRequest request,
			HttpServletResponse response ) throws UnsupportedEncodingException {
		
		HttpHeaders header = null;
		byte[] bytes = null;
		
		try{
			if( securityUser == null ){
				System.out.println("로그인 되지 않았음요");
				return null;
			}
			
			Media media = mediaCDNService.findById(mediaId);
			
			User user = new User( securityUser.getUserId() );
			operationService.addViewCount( media, user);
	
			FileInputStream fileInputStream = null;
			try {
				fileInputStream = new FileInputStream( media.getRelativeConvertedPath("o") );
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			
	        String fileName = media.getFileName().replace(" ", "_");
	        String userAgent = request.getHeader("User-Agent");
	         
	        boolean filefox = userAgent.indexOf("Firefox") > -1;
		        
	        if( filefox == true ){
	           	fileName = new String(fileName.getBytes("UTF-8"), "8859_1");
	        }else{
	        	fileName = URLEncoder.encode(fileName, "utf-8");
	        }
			header = new HttpHeaders();
			String contentTypeString = media.getContentType();
			if( contentTypeString != null ){
				String[] contentType = media.getContentType().split("/");
			    if( contentType.length > 1 ){
			    	header.setContentType(new MediaType(contentType[0], contentType[1]));
			    }
			}
			header.set("Content-Disposition",
		                   "attachment; filename=\"" + fileName +"\";");
		    
		    header.setContentLength(media.getSize());
		    
		    
			try {
				bytes = IOUtils.toByteArray(fileInputStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
//			try {
//				response.getOutputStream().close();
//			} catch (IOException e) {
//				System.out.println("아웃풋 스트림 에러");
//				e.printStackTrace();
//			}
//			
			if( bytes == null || header == null ){
				throw new RuntimeException();
			}
		}catch(Exception ex){
			System.out.println("문제발생");
			ex.printStackTrace();
		}
		return new HttpEntity<byte[]>(bytes, header);
	}	
	
	@RequestMapping(value = "/download", method = RequestMethod.GET  )
	public HttpEntity<byte[]> download(
			@AuthUser SecurityUser securityUser,
			@RequestParam(value="id", required=false) Long mediaId, 
			@RequestParam(value="type", required=false) String type,
			HttpServletRequest request,
			HttpServletResponse response ) throws UnsupportedEncodingException {
		
		
		Media media = mediaCDNService.findById(mediaId);
		
		User user = new User( securityUser.getUserId() );
		operationService.addViewCount( media, user);
		
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream( media.getRelativeConvertedPath("o") );
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
        String fileName = media.getFileName().replace(" ", "_");
        String userAgent = request.getHeader("User-Agent");
         
        boolean filefox = userAgent.indexOf("Firefox") > -1;
	        
        if( filefox == true ){
           	fileName = new String(fileName.getBytes("UTF-8"), "8859_1");
        }else{
        	fileName = URLEncoder.encode(fileName, "utf-8");
        }
		
		HttpHeaders header = new HttpHeaders();
		String contentTypeString = media.getContentType();
		if( contentTypeString != null ){
			String[] contentType = media.getContentType().split("/");
		    if( contentType.length > 1 ){
		    	header.setContentType(new MediaType(contentType[0], contentType[1]));
		    }
		}
		header.set("Content-Disposition",
	                   "attachment; filename=\"" + fileName +"\";");
	    
	    header.setContentLength(media.getSize());
		
	    byte[] bytes = null;
		try {
			bytes = IOUtils.toByteArray(fileInputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
		return new HttpEntity<byte[]>(bytes, header);
	}	
}
