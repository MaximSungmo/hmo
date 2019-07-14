package kr.co.sunnyvale.sunny.service.impl;

import kr.co.sunnyvale.sunny.domain.Content;
import kr.co.sunnyvale.sunny.domain.ContentDynamic;
import kr.co.sunnyvale.sunny.repository.hibernate.ContentDynamicRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( value="contentDynamicService" )
@Transactional
public class ContentDynamicService {
	
	@Autowired
	private ContentDynamicRepository contentDynamicRepository;

	@Autowired
	private MessageSource messageSource;
	
	
	@Transactional(readOnly = true )
	public ContentDynamic select( Long id ){
		return contentDynamicRepository.select(id);
	}
	@Transactional
	public void update( ContentDynamic dynamic ){
		contentDynamicRepository.update(dynamic);
	}
	@Transactional
	public ContentDynamic findOrCreateByContentId(Long id) {
		ContentDynamic dynamic = contentDynamicRepository.findByContentId(id);
		
		if( dynamic == null ){
			dynamic = new ContentDynamic(new Content(id));
			contentDynamicRepository.save(dynamic);
		}
		return dynamic;
	}
	@Transactional
	public void createDefault(Content content) {
		ContentDynamic dynamic = new ContentDynamic(content);
		contentDynamicRepository.save(dynamic);
	}
	
	@Transactional
	public void increaseReputation(Long id) {
		ContentDynamic contentDynamic = findOrCreateByContentId(id);
		contentDynamic.setReputation( contentDynamic.getReputation() + 1 );
	}
	@Transactional
	public void decreaseReputation(Long id) {
		ContentDynamic contentDynamic = findOrCreateByContentId(id);
		contentDynamic.setReputation( contentDynamic.getReputation() - 1 );
	}
	
	@Transactional(readOnly = true)
	public ContentDynamic findByContentId(Long id) {
		return contentDynamicRepository.findByContentId(id);
	}


}
