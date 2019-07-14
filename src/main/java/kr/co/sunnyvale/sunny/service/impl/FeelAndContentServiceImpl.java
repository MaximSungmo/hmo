package kr.co.sunnyvale.sunny.service.impl;

import kr.co.sunnyvale.sunny.domain.FeelAndContent;
import kr.co.sunnyvale.sunny.repository.hibernate.FeelAndContentAndUserRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.FeelAndContentRepository;
import kr.co.sunnyvale.sunny.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( value="feelAndContentService" )
@Transactional
public class FeelAndContentServiceImpl{

	@Autowired
	UserService userService;
	
	@Autowired
	FeelAndContentRepository feelAndContentRepository;
	
	@Autowired
	FeelAndContentAndUserRepository feelAndContentAndUserRepository;
	
	@Transactional
	public void save(FeelAndContent feelAndContent) {
		feelAndContentRepository.save(feelAndContent);
	}
	
}
