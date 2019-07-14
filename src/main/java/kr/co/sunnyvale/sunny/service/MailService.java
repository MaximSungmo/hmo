package kr.co.sunnyvale.sunny.service;


import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import kr.co.sunnyvale.sunny.domain.AuthToken;
import kr.co.sunnyvale.sunny.domain.SiteInactive;
import kr.co.sunnyvale.sunny.domain.SiteInactiveUser;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.repository.hibernate.AuthTokenRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.velocity.VelocityEngineFactory;
import org.springframework.ui.velocity.VelocityEngineUtils;

@Service
public class MailService {
	
private final String FROM = "support@sunnyvale.co.kr";
	
	@Autowired 
    private JavaMailSender mailSender;
	
	@Autowired
	private AuthTokenRepository authTokenRepository;
	
	@Autowired
	private SiteService siteService;
	
	@Autowired
    private VelocityEngineFactory velocityEngineFactory;

	private void send( final String sendTo, final String subject, final String templateLocation, final Map<String, Object> model ) {

		String text;
        
		try {
			text = VelocityEngineUtils.mergeTemplateIntoString( velocityEngineFactory.createVelocityEngine(), templateLocation, model );
		
			MimeMessage mimeMessage = mailSender.createMimeMessage();
	
			mimeMessage.addRecipient( RecipientType.TO, new InternetAddress( sendTo ) );
			mimeMessage.addFrom( new InternetAddress[] { new InternetAddress( FROM ) } );
	
	        mimeMessage.setContent(text, "text/html; charset=utf-8");
	        mimeMessage.setSubject( subject, "UTF-8");
	        mimeMessage.setText( text, "UTF-8", "html" );
	        
	        mailSender.send( mimeMessage );
		
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	

	@Transactional
    public void sendResetPasswordMail( final Sunny sunny, final User user, final String email ){
    	
    	String templateLocation = "mail/resetPassword-confirm.vm";

    	
    	authTokenRepository.removeAllValues(user.getId(), AuthToken.TYPE_PASSWORD);

    	Map<String, Object> model = new HashMap<String, Object>();
    	model.put( "sunny", sunny );
    	model.put( "user", user );
    	
    	AuthToken authToken = new AuthToken(user, AuthToken.TYPE_PASSWORD);
		authTokenRepository.save(authToken);
		
		model.put( "key", authToken );

		
		this.send( email,  " 비밀번호 변경안내 메일입니다.", templateLocation, model );
    }

	@Transactional
    public void sendConfirmAdminMail( final Sunny sunny, final SiteInactive siteInactive, final SiteInactiveUser siteInactiveUser ){
    	
    	String templateLocation = "mail/registration-confirm-admin.vm";
    	
    	Map<String, Object> model = new HashMap<String, Object>();
    	model.put( "siteInactiveUser", siteInactiveUser );
		model.put( "sunny", sunny );

		authTokenRepository.removeAllValues(siteInactiveUser.getId(), AuthToken.TYPE_ACTIVATE_ADMIN);
		AuthToken authToken = new AuthToken(siteInactiveUser, AuthToken.TYPE_ACTIVATE_ADMIN);
		authToken.setSiteInactive(siteInactive);
		authToken.setSiteInactiveUser(siteInactiveUser);
		authTokenRepository.save(authToken);
		
    	model.put( "key", authToken );
    	this.send( siteInactiveUser.getEmail(), sunny.getSite().getCompanyName() + "의 계정인증 메일입니다", templateLocation, model );
    }


	@Transactional
    public void sendConfirmSiteInactiveUserMail( final Sunny sunny, final SiteInactiveUser siteInactiveUser ){
    	
    	String templateLocation = "mail/site-inactive-user-confirm.vm";
    	
    	Map<String, Object> model = new HashMap<String, Object>();
    	model.put( "siteInactiveUser", siteInactiveUser );
		model.put( "sunny", sunny );

		authTokenRepository.removeAllValues(siteInactiveUser, AuthToken.TYPE_SITE_INACTIVE_USER_CONFIRM);
		AuthToken authToken = new AuthToken(siteInactiveUser, AuthToken.TYPE_SITE_INACTIVE_USER_CONFIRM);
		authTokenRepository.save(authToken);
		
    	model.put( "key", authToken );
    	this.send( siteInactiveUser.getEmail(), sunny.getSite().getCompanyName() + " 계정인증 메일입니다", templateLocation, model );
    }


	public void sendInviteSiteInactiveUserMail( final Sunny sunny, SiteInactiveUser siteInactiveUser ) {
		
		String templateLocation = "mail/site-inactive-user-invite.vm";
		
    	Map<String, Object> model = new HashMap<String, Object>();
    	model.put( "siteInactiveUser", siteInactiveUser );
		model.put( "sunny", sunny );

		authTokenRepository.removeAllValues(siteInactiveUser, AuthToken.TYPE_SITE_INACTIVE_USER_INVITE);
		AuthToken authToken = new AuthToken(siteInactiveUser, AuthToken.TYPE_SITE_INACTIVE_USER_INVITE);
		authTokenRepository.save( authToken );
    	model.put( "key", authToken );

    	this.send( siteInactiveUser.getEmail(), sunny.getSite().getCompanyName() + "에서 초대 메일을 보냈습니다.", templateLocation, model );
	}

}
