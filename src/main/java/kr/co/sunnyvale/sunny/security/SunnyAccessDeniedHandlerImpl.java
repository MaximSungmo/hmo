package kr.co.sunnyvale.sunny.security;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.sunnyvale.sunny.domain.dto.JsonResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SunnyAccessDeniedHandlerImpl implements AccessDeniedHandler {

	//~ Static fields/initializers =====================================================================================

    protected static final Log logger = LogFactory.getLog(AccessDeniedHandlerImpl.class);

    //~ Instance fields ================================================================================================

    private String errorPage;

    //~ Methods ========================================================================================================

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
    	System.out.println("일루 들어왔는감?");
    	
        if (!response.isCommitted()) {
        	System.out.println("커밋 안되었지?");
        	
    		
    		String accept = request.getHeader("accept");
    		if (accept.matches(".*application/json.*") == true) {
    			//response.setStatus(HttpResponseType);
    			//response.sendError(HttpResponseType);
    			JsonResult jsonResult = new JsonResult(false, "Access Denied", null);
    			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    			OutputStream out = response.getOutputStream();
    			
    			ObjectMapper converter = new ObjectMapper();
    		
    			out.write(converter.writeValueAsString(jsonResult).getBytes());
    			out.flush();
    			out.close();
    			return;
//    			allResult.setData(null);
    			
    		}
        	
            if (errorPage != null) {
                // Put exception into request scope (perhaps of use to a view)
                request.setAttribute(WebAttributes.ACCESS_DENIED_403, accessDeniedException);

                // Set the 403 status code.
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);

                // forward to error page.
                RequestDispatcher dispatcher = request.getRequestDispatcher(errorPage);
                dispatcher.forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, accessDeniedException.getMessage());
            }
        }
    }

    /**
     * The error page to use. Must begin with a "/" and is interpreted relative to the current context root.
     *
     * @param errorPage the dispatcher path to display
     *
     * @throws IllegalArgumentException if the argument doesn't comply with the above limitations
     */
    public void setErrorPage(String errorPage) {
        if ((errorPage != null) && !errorPage.startsWith("/")) {
            throw new IllegalArgumentException("errorPage must begin with '/'");
        }

        this.errorPage = errorPage;
    }
}
