package kr.co.sunnyvale.sunny.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ResourceController {
    @RequestMapping( "/favicon.ico" )
    public ModelAndView favIconForward(){
    	ModelAndView modelAndView = new ModelAndView("forward:/assets/sunny/2.0/ico/favicon.png");
		return modelAndView;
    }    
    @RequestMapping( "/googled02306225bbc8cdb.html" )
    public ModelAndView googleWebmaster(){    	
    	ModelAndView modelAndView = new ModelAndView("forward:/assets/html/googled02306225bbc8cdb.html");
    	return modelAndView;
    }
//    @RequestMapping("/image.posco")
//    public @ResponseBody byte[] image(String fileName, HttpServletRequest request) throws IOException {
//    	String imageFileName = "C:/fileUpload/" + fileName;
//    	InputStream in = new FileInputStream(imageFileName);
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        
//        byte[] buffer = new byte[1024];
//        int readCount = 0;
//        while((readCount = in.read(buffer)) != -1){
//        	bos.write(buffer,0,readCount);
//        }
//        
//        return bos.toByteArray();
//    }  
}
