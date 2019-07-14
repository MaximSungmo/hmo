package kr.co.sunnyvale.sunny.service;

import kr.co.sunnyvale.sunny.domain.FavoriteUser;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.exception.SimpleSunnyException;
import kr.co.sunnyvale.sunny.repository.hibernate.FavoriteUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( value="favoriteUserService" )
@Transactional
public class FavoriteUserService {

	@Autowired
	private UserService userService;
	
	@Autowired
	private FavoriteUserRepository favoriteUserRepository;
	
	@Autowired
	private MessageSource messageSource;
	
	@Transactional
	public FavoriteUser favorite(Sunny sunny, User favoritedUser, User favoriterUser ){
		
		boolean isAlreadyFavorited = favoriteUserRepository.isAlreadyFavorited(sunny, favoritedUser.getId(), favoriterUser.getId());
		if( isAlreadyFavorited == true ){
			throw new SimpleSunnyException();
		}
		
		FavoriteUser favoriteUser = new FavoriteUser();
		favoriteUser.setFavorited(favoritedUser);
		favoriteUser.setFavoriter(favoriterUser);
		favoriteUser.setSite(sunny.getSite());
		
		favoriteUserRepository.save(favoriteUser);
		return favoriteUser;
	}
	
	@Transactional
	public void save(FavoriteUser favoriteUser) {
		favoriteUserRepository.save(favoriteUser);
	}
	@Transactional(readOnly = true)
	public FavoriteUser select(Long favoriteUserId) {
		return favoriteUserRepository.select(favoriteUserId);
	}

	@Transactional(readOnly = true)
	public boolean isAlreadyFavorited(Sunny sunny, Long favoritedUserId, Long favoriterUserId) {
		return favoriteUserRepository.isAlreadyFavorited(sunny, favoritedUserId, favoriterUserId);
	}

	public void removeFavorite(Sunny sunny, User favoritedUser, User favoriterUser) {
		FavoriteUser favoriteUser = favoriteUserRepository.find( sunny, favoritedUser, favoriterUser );
		
		if( favoriteUser == null ){
			throw new SimpleSunnyException();
		}
		favoriteUserRepository.delete(favoriteUser);
	}

	
}
