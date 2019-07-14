package kr.co.sunnyvale.sunny.service.impl;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.Content;
import kr.co.sunnyvale.sunny.domain.Media;
import kr.co.sunnyvale.sunny.domain.Revision;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.repository.hibernate.RevisionRepository;
import kr.co.sunnyvale.sunny.service.MediaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( value="revisionService" )
@Transactional
public class RevisionService {
	
	@Autowired
	private RevisionRepository revisionRepository;
	
	@Autowired
	private MediaService mediaService; 
	
	
	@Transactional
	public void save(Revision revision){
		revisionRepository.save(revision);
	}
	
	
	@Transactional
	public Revision createVersion(Content content ){
		
		Revision revision = new Revision( content );
		revision.setUser( content.getUser() );
		
		List<Media> mediaes = mediaService.getFromContent( content );
		revisionRepository.save(revision);
		if( mediaes != null ){
			//int mediaCount = mediaRepository.updateMediaesBelongToContent(story, mediaIds);
			mediaService.appendMediaesToRevision(revision.getId(), mediaes);
			revision.setMediaCount(mediaes.size());
			revisionRepository.update(revision);
		}
		
		return revision;
	}


	@Transactional
	public List<Revision> getList(Long contentId, Stream stream) {
		return revisionRepository.findListByObject("content.id", contentId, stream);

	}

	@Transactional
	public void update(Revision revision) {
		revisionRepository.update(revision);
		
	}

	@Transactional(readOnly = true)
	public Revision findById(Long id) {
		return revisionRepository.select(id);
	}
}
