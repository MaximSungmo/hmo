package kr.co.sunnyvale.sunny.service;

import java.io.Serializable;
import java.util.Date;

import kr.co.sunnyvale.sunny.domain.LoginInfo;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.redis.RedisClient;
import kr.co.sunnyvale.sunny.redis.RedisPublisher;
import kr.co.sunnyvale.sunny.repository.hibernate.LoginInfoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( value="loginInfoService" )
@Transactional
public class LoginInfoService {

	@Autowired
	UserService userService;
	
	@Autowired
	private LoginInfoRepository loginInfoRepository;
	
	@Autowired
	private MessageSource messageSource;

	@Autowired
	private RedisPublisher redisPublisher;

	
	@Transactional
	public LoginInfo successLogin(Sunny sunny, User user){
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.setType(LoginInfo.SUCCESS);
		loginInfo.setUser(user);
		loginInfo.setIpAddress( sunny.getIpAddress() );
		loginInfo.setSessionId(sunny.getSessionId());
		loginInfo.setSite(sunny.getSite());
		
		loginInfoRepository.save(loginInfo);

		redisPublisher.login( sunny, user );

		return loginInfo;
	}
	
	@Transactional
	public void save(LoginInfo loginLog) {
		loginInfoRepository.save(loginLog);
	}
	@Transactional(readOnly = true)
	public LoginInfo select(Long logId) {
		return loginInfoRepository.select(logId);
	}

	@Transactional(readOnly = true)
	public Date getLastLoginDateDate(Long userId) {
		return loginInfoRepository.getLastLoginDate(userId);
	}
	
	@Transactional(readOnly = true)
	public Serializable getCreateDate(Long logId) {
		return loginInfoRepository.getCreateDate(logId);
	}
	@Transactional
	public void delete(LoginInfo loginLog) {
		loginInfoRepository.delete(loginLog);
	}
}
