package kr.co.sunnyvale.sunny.service;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.AuthToken;
import kr.co.sunnyvale.sunny.domain.SiteInactiveUser;
import kr.co.sunnyvale.sunny.domain.User;

public interface AuthTokenService {
	
	/**
	 * 사용자에게 맞는 토큰을 새로 하나 생성한다.
	 * @param user
	 * @param type
	 * @return
	 */
	public AuthToken generateAuthToken(User user, String type);
	
	/**
	 * 사용자의 type에 대한 최근 토큰을 하나 가져옴
	 * @param userId
	 * @param type
	 * @return
	 */
	public AuthToken getRecentAuthToken(Long  userId, String type);
	
	/**
	 * 사용자의 type에 해당하는 모든 토큰을 가져온다.
	 * @param userId
	 * @param type
	 * @return
	 */
	public List<AuthToken> getAllAuthTokens(Long  userId, String type);
	
	/**
	 * 사용자의 type 에 대한 모든 토큰을 삭제한다. 
	 * @param userId
	 * @param type
	 */
	public void removeAllValues(Long  userId, String type);
	
	/**
	 * 해당하는 토큰을 가져온다. 이것으로 존재하는지 여부를 null 체크 한다.
	 * @param userId
	 * @param keyValue
	 * @param type
	 * @return
	 */
	public AuthToken getCorrectAuthToken(Long userId, String keyValue, String type);

	/**
	 * Key 에 해당하는 value 를 삭제한다. 
	 * @param ownKey
	 */
	public void removeValue(AuthToken ownAuthToken) ;

	public AuthToken getCorrectAuthToken(SiteInactiveUser siteInactiveUser,
			String key, String typeSiteInactiveUserConfirm);
}
