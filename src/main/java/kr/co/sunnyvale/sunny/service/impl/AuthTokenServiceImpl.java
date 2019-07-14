package kr.co.sunnyvale.sunny.service.impl;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.AuthToken;
import kr.co.sunnyvale.sunny.domain.SiteInactiveUser;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.repository.hibernate.AuthTokenRepository;
import kr.co.sunnyvale.sunny.service.AuthTokenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( value="authTokenService" )
@Transactional
public class AuthTokenServiceImpl implements AuthTokenService {

	@Autowired
	private AuthTokenRepository authTokenRepository;
	
	@Override
	public AuthToken generateAuthToken(User user, String type) {
		AuthToken authToken = new AuthToken(user, type);
		authTokenRepository.save(authToken);
		return authToken;
	}

	@Override
	public AuthToken getRecentAuthToken(Long  userId, String type) {
		return authTokenRepository.getRecentAuthToken( userId, type );
	}

	@Override
	public List<AuthToken> getAllAuthTokens(Long  userId, String type) {
		return authTokenRepository.getAllAuthTokens( userId, type, null );
	}

	@Override
	public void removeAllValues(Long userId, String type) {
		authTokenRepository.removeAllValues( userId, type );
	}

	@Override
	public AuthToken getCorrectAuthToken(Long userId, String keyValue,
			String type) {
		return authTokenRepository.getCorrectAuthToken( userId, keyValue, type );
	}

	@Override
	public AuthToken getCorrectAuthToken(SiteInactiveUser siteInactiveUser,
			String key, String type) {
		return authTokenRepository.getCorrectAuthToken( siteInactiveUser, key, type );
	}
	@Override
	public void removeValue(AuthToken ownAuthToken) {
		ownAuthToken.setValue("");
		authTokenRepository.update(ownAuthToken);
	}


}
