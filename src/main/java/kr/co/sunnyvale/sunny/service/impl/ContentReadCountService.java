package kr.co.sunnyvale.sunny.service.impl;

import kr.co.sunnyvale.sunny.domain.Content;
import kr.co.sunnyvale.sunny.domain.ContentReadCount;
import kr.co.sunnyvale.sunny.repository.hibernate.ContentReadCountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( value="contentReadCountService" )
@Transactional
public class ContentReadCountService {
	
	@Autowired
	private ContentReadCountRepository contentReadCountRepository;

	
	
	@Transactional
	public Integer addCount(Content content) {
		ContentReadCount contentReadCount = contentReadCountRepository.findUniqByObject("content", content);
		if( contentReadCount == null ){
			contentReadCount = new ContentReadCount(content);
			contentReadCount.setCount(1);
			contentReadCountRepository.save(contentReadCount);
		}else{
			contentReadCount.setCount( contentReadCount.getCount() + 1 );
		}
		return contentReadCount.getCount();
	}


	@Transactional
	public void save(ContentReadCount contentReadCount) {
		contentReadCountRepository.save(contentReadCount);
	}


	@Transactional
	public ContentReadCount findOrCreateByContentId(Long id) {
		ContentReadCount contentReadCount = contentReadCountRepository.findByContentId(id);
		
		if( contentReadCount == null ){
			contentReadCount = new ContentReadCount( new Content(id));
			contentReadCountRepository.save(contentReadCount);			
		}
		return contentReadCount;
	}
	
	
}
