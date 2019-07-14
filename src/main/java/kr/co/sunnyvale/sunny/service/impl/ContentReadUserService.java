package kr.co.sunnyvale.sunny.service.impl;

import kr.co.sunnyvale.sunny.domain.Content;
import kr.co.sunnyvale.sunny.domain.ContentReadUser;
import kr.co.sunnyvale.sunny.domain.LastRead;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.repository.hibernate.ContentReadUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( value="contentReadUserService" )
@Transactional
public class ContentReadUserService {
	
	@Autowired
	private ContentReadUserRepository contentReadUserRepository;

	
	@Transactional(readOnly = true)
	public boolean alreadyRead(Long contentId, Long userId) {
		return contentReadUserRepository.alreadyRead(contentId, userId);
	}

	@Transactional(readOnly = true)
	public boolean alreadyRead(Long contentId, User user) {
		return contentReadUserRepository.alreadyRead(contentId, user);
	}
	
	@Transactional
	public void add(Content content, User user) {
		ContentReadUser contentViewUser = new ContentReadUser(content, user);
		contentReadUserRepository.save(contentViewUser);
	}

}
