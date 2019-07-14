package kr.co.sunnyvale.sunnycdn.resolver;

import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;

import org.apache.commons.fileupload.FileUpload;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

public class CustomCommonsMultipartResolver extends CommonsMultipartResolver {

	/**
	 * 로그인 되었을 시 만약 uploadSizeMax 가 설정되어 있으면 uploadMaxSize 에 설정해준다. 
	 */
	@Override
	protected FileUpload prepareFileUpload(String encoding) {
		
		FileUpload fileUpload = super.prepareFileUpload(encoding);
		
		try{
			SecurityUser securityUser = null;
			if (SecurityContextHolder.getContext().getAuthentication() != null) {
				Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				if (principal != null && principal instanceof UserDetails) {
					securityUser = (SecurityUser) principal;
					if( securityUser.getUploadMaxSize() != null && securityUser.getUploadMaxSize() > 0 ){
						fileUpload.setSizeMax(securityUser.getUploadMaxSize());
					}		
					
				}
			}
		
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return fileUpload; 
	}

}