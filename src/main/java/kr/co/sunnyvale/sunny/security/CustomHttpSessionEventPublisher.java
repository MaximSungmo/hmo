package kr.co.sunnyvale.sunny.security;

/* Copyright 2004, 2005, 2006 Acegi Technology Pty Limited
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/


import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.web.session.HttpSessionCreatedEvent;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.context.support.WebApplicationContextUtils;


/**
* Declared in web.xml as
* <pre>
* &lt;listener&gt;
*     &lt;listener-class&gt;org.springframework.security.web.session.HttpSessionEventPublisher&lt;/listener-class&gt;
* &lt;/listener&gt;
* </pre>
*
* Publishes <code>HttpSessionApplicationEvent</code>s to the Spring Root WebApplicationContext. Maps
* javax.servlet.http.HttpSessionListener.sessionCreated() to {@link HttpSessionCreatedEvent}. Maps
* javax.servlet.http.HttpSessionListener.sessionDestroyed() to {@link HttpSessionDestroyedEvent}.
*
* @author Ray Krueger
*/
public class CustomHttpSessionEventPublisher implements HttpSessionListener {
   //~ Static fields/initializers =====================================================================================

   private static final String LOGGER_NAME = HttpSessionEventPublisher.class.getName();

   //~ Methods ========================================================================================================

   ApplicationContext getContext(ServletContext servletContext) {
       return WebApplicationContextUtils.getWebApplicationContext(servletContext);
   }

   /**
    * Handles the HttpSessionEvent by publishing a {@link HttpSessionCreatedEvent} to the application
    * appContext.
    *
    * @param event HttpSessionEvent passed in by the container
    */
   public void sessionCreated(HttpSessionEvent event) {
       HttpSessionCreatedEvent e = new HttpSessionCreatedEvent(event.getSession());
       Log log = LogFactory.getLog(LOGGER_NAME);

       if (log.isDebugEnabled()) {
           log.debug("Publishing event: " + e);
       }
       System.out.println("세션이 생성되었습니다. " + event.getSession().getId());
       getContext(event.getSession().getServletContext()).publishEvent(e);
   }

   /**
    * Handles the HttpSessionEvent by publishing a {@link HttpSessionDestroyedEvent} to the application
    * appContext.
    *
    * @param event The HttpSessionEvent pass in by the container
    */
   public void sessionDestroyed(HttpSessionEvent event) {
       HttpSessionDestroyedEvent e = new HttpSessionDestroyedEvent(event.getSession());
       Log log = LogFactory.getLog(LOGGER_NAME);

       if (log.isDebugEnabled()) {
           log.debug("Publishing event: " + e);
       }

       getContext(event.getSession().getServletContext()).publishEvent(e);
   }
}
