package kr.co.sunnyvale.sunny.service.impl;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.UserAppleToken;
import kr.co.sunnyvale.sunny.redis.RedisPublisher;
import kr.co.sunnyvale.sunny.repository.hibernate.UserAppleTokenRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Splitter;

@Service( value="deviceRegisterService" )
@Transactional
public class DeviceRegisterService {

	@Autowired
	private UserAppleTokenRepository userAppleTokenRepository;


	@Autowired
	private RedisPublisher redisPublisher;
	
	
	

	@Transactional
	public void unregisterAppleDevice(Long userId,
			String deviceToken) {
		
		if( userId == null || deviceToken == null ){
			throw new RuntimeException("잘못된 인자가 전달되었습니다. 로그인 하시거나 제대로 된 토큰을 보내주세요");
		}
		userAppleTokenRepository.removeAllMatchedTokens(userId, deviceToken);
		
		redisPublisher.removeAppleDevice( userId, deviceToken );
		
	}
	
	@Transactional
	public UserAppleToken registerAppleDevice(Long userId,
			UserAppleToken appleToken) {
		
		if( userId == null || appleToken == null || appleToken.getDeviceToken() == null ){
			throw new RuntimeException("잘못된 인자가 전달되었습니다. 로그인 하시거나 제대로 된 토큰을 보내주세요");
		}
		if( appleToken.getDeviceToken() != null ){
			if( appleToken.getDeviceToken().indexOf("<") == -1 ){
				String lastString = "<";
				for(final String token :
				    Splitter
				        .fixedLength(8)
				        .split(appleToken.getDeviceToken())){
					lastString = lastString + token +" ";
				}
				lastString = lastString.substring(0, lastString.length() - 1);
				lastString = lastString + ">";
				appleToken.setDeviceToken(lastString);
			}
		}
		UserAppleToken persistentToken = userAppleTokenRepository.findUserAndDeviceToken(userId, appleToken.getDeviceToken());
		
		if( persistentToken == null ){
			appleToken.setUser(new User(userId));
			
			userAppleTokenRepository.save(appleToken);
			if( appleToken.getProduction() == null || appleToken.getProduction() == true ){
				redisPublisher.registerAppleProductionDevice( userId, appleToken );
			}else{
				redisPublisher.registerAppleDevelopmentDevice( userId, appleToken );
			}
			return appleToken;
		}
		
		boolean isChanged = false;
		if( appleToken.getAppVersion() != null && !persistentToken.getAppVersion().equals( appleToken.getAppVersion() )){
			persistentToken.setAppVersion( appleToken.getAppVersion() );
			isChanged = true;
		}
		if( appleToken.getDeviceFamily() != null && !persistentToken.getDeviceFamily().equals( appleToken.getDeviceFamily() )){
			persistentToken.setDeviceFamily( appleToken.getDeviceFamily() );
			isChanged = true;
		}
		if( appleToken.getDeviceModel() != null && !persistentToken.getDeviceModel().equals( appleToken.getDeviceModel() )){
			persistentToken.setDeviceModel( appleToken.getDeviceModel() );
			isChanged = true;
		}
		if( appleToken.getDeviceName() != null && !persistentToken.getDeviceName().equals( appleToken.getDeviceName() )){
			persistentToken.setDeviceName( appleToken.getDeviceName() );
			isChanged = true;
		}
		if( appleToken.getDeviceToken() != null && !persistentToken.getDeviceToken().equals( appleToken.getDeviceToken() )){

			redisPublisher.removeAppleDevice( userId, persistentToken.getDeviceToken() );
			persistentToken.setDeviceToken( appleToken.getDeviceToken() );
			if( appleToken.getProduction() == null || appleToken.getProduction() == true ){
				redisPublisher.registerAppleProductionDevice( userId, appleToken );
			}else{
				redisPublisher.registerAppleDevelopmentDevice( userId, appleToken );
			}
			isChanged = true;
		}
		
		userAppleTokenRepository.update(persistentToken);

		if( appleToken.getProduction() == null || appleToken.getProduction() == true ){
			redisPublisher.registerAppleProductionDevice( userId, appleToken );
		}else{
			redisPublisher.registerAppleDevelopmentDevice( userId, appleToken );
		}
		if( isChanged ){
			redisPublisher.updateAppleDevice( userId, appleToken );
		}
		return appleToken;
		
		
	}
	
	
}
