package kr.co.sunnyvale.sunny.service.impl;

import kr.co.sunnyvale.sunny.domain.ContentAndTag;
import kr.co.sunnyvale.sunny.repository.hibernate.ContentAndTagRepository;
import kr.co.sunnyvale.sunny.service.TagService;
import kr.co.sunnyvale.sunny.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( value="contentAndTagService" )
public class ContentAndTagService {

	@Autowired
	private UserService userService;

	@Autowired
	private ContentAndTagRepository contentAndTagRepository;
	
	@Autowired
	private TagService tagService;
	
	@Transactional
	public void save(ContentAndTag tagRelation) {
		contentAndTagRepository.save( tagRelation );
		tagService.addReferenceCount(tagRelation.getTag().getId());
	}

	@Transactional
	public void remove(ContentAndTag tagRelation) {
		contentAndTagRepository.delete(tagRelation);
		tagService.minusReferenceCount(tagRelation.getTag().getId());
		
	}
	

}