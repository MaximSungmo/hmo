package kr.co.sunnyvale.sunny.service.impl;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.Content;
import kr.co.sunnyvale.sunny.domain.Draft;
import kr.co.sunnyvale.sunny.domain.Media;
import kr.co.sunnyvale.sunny.domain.Reply;
import kr.co.sunnyvale.sunny.domain.Revision;
import kr.co.sunnyvale.sunny.domain.RevisionMedia;
import kr.co.sunnyvale.sunny.domain.Story;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.extend.MediaType;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.exception.SimpleSunnyException;
import kr.co.sunnyvale.sunny.repository.hibernate.ChildContentRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.ContentService;
import kr.co.sunnyvale.sunny.repository.hibernate.MediaRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.ReplyRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.RevisionMediaRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.StoryRepository;
import kr.co.sunnyvale.sunny.service.DraftService;
import kr.co.sunnyvale.sunny.service.MediaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( value="mediaService" )
@Transactional
public class MediaServiceImpl implements MediaService {
	@Autowired
	MediaRepository mediaRepository;
	
	@Autowired
	StoryRepository storyRepository;

	@Autowired
	private ChildContentRepository contentRepository;
	
	@Autowired
	private ContentService contentService;
	
	@Autowired
	private DraftService draftService;
	
	
	@Autowired
	private ReplyRepository replyRepository;

	@Override
	@Transactional
	public Reply setMediaToReply(Long id, Long mediaId) {
		Reply reply = replyRepository.select(id);
		
		Media media = mediaRepository.select(mediaId);
				
		if( media == null )
			throw new SimpleSunnyException();
		
		
		media.setParent(reply);
		media.setParentType(reply.getType());
		media.setContent(reply);
		media.setUsed(true);
		if( media.getDraft() != null )
			media.setDraft(null);
		mediaRepository.update(media);
		
		reply.setMedia(media);
		reply.setMediaId( media.getId() );
		reply.setWidth( media.getWidth() );
		reply.setHeight( media.getHeight() );
		reply.setSize( media.getSize() );
		reply.setMediaType( media.getMediaType() );
		reply.setFileName( media.getFileName() );
		reply.setExtName( media.getExtName() );
		reply.setPathBeforeSize( media.getRelativePath() + media.getConvertedFront());
		reply.setMediaCount(1);
		replyRepository.update(reply);
		
		return reply;
	}

	
	
	@Override
	@Transactional
	public void appendMediaesToContent(Long contentId, Long... mediaIds) {
		Content content = contentRepository.select(contentId);
		
		for( Long mediaId : mediaIds ){
			Media media = mediaRepository.select(mediaId);
			
			// 미디어에 스토리의 모든 퍼미션을 지정한다.
			
			// 미디어에 부모 스토리와 부모Story의 타입을 지정한다.
			media.setParent(content);
			media.setParentType(content.getType());
			media.setContent(content);
			media.setUsed(true);
			if( media.getDraft() != null )
				media.setDraft(null);
			
			mediaRepository.update(media);
		}
		
	}
	
	@Override
	@Transactional
	public void appendMediaesToContent(Long contentId, List<Media> mediaes) {
		Content content = contentRepository.select(contentId);
		
		for( Media media : mediaes ){
			Media persistentMedia = mediaRepository.select(media.getId());
			
			// 미디어에 스토리의 모든 퍼미션을 지정한다.
			
			// 미디어에 부모 스토리와 부모Story의 타입을 지정한다.
			persistentMedia.setParent(content);
			persistentMedia.setParentType(content.getType());
			persistentMedia.setContent(content);
			
			
			
			if( persistentMedia.getDraft() != null ){
				System.out.println("draft 는 널로!");
				persistentMedia.setDraft(null);
			}
			
			mediaRepository.update(persistentMedia);
		}
		
	}
	
	
	@Override
	@Transactional
	public void appendMediaesToRevision(Long id, List<Media> medias) {
		// null 처리를 어떻게 할 것인가. null이면 그냥 return 할 것인가? exception을 발생시킬 것인가.
		if(medias == null)
			throw new IllegalArgumentException("mediaList is null");
		for (Media media : medias) {
			appendMediaToRevision(id, media);
		}
	}
	
	@Autowired
	private RevisionService revisionService; 
	
	@Autowired
	private RevisionMediaRepository revisionMediaRepository; 
	
	
	@Transactional
	public void appendMediaToRevision(Long id, Media media) {
		Revision revision = revisionService.findById(id);
		RevisionMedia revisionMedia = media.copyToRevision();
		revisionMedia.setRevision(revision);
		
		// 미디어에 스토리의 모든 퍼미션을 지정한다.
		
		// 미디어에 부모 스토리와 부모Story의 타입을 지정한다.
//		media.setParent(toStory);
//		media.setParentType(toStory.getType());
//		media.setContent(toStory);
		revisionMediaRepository.save( revisionMedia );
	}

	@Override
	@Transactional
	public void appendMediaToStory(Long toStoryId, Long mediaId) {
		Story toStory = storyRepository.select(toStoryId);
		Media media = mediaRepository.select(mediaId);
		
		// 미디어에 스토리의 모든 퍼미션을 지정한다.
		
		// 미디어에 부모 스토리와 부모Story의 타입을 지정한다.
		media.setParent(toStory);
		media.setParentType(toStory.getType());
		media.setContent(toStory);
		media.setUsed(true);
		mediaRepository.update(media);
		
//		List<Media> medies = toStory.getMediaes();
//		if(medies == null)
//			toStory.setMediaes(new ArrayList<Media>());
//		toStory.getMediaes().add(media);
//		storyRepository.update(toStory);
	}

	@Override
	@Transactional
	public void appendMediesToStory(Long toStoryId, List<Long> mediaIdList) {
		// null 처리를 어떻게 할 것인가. null이면 그냥 return 할 것인가? exception을 발생시킬 것인가.
		if(mediaIdList == null)
			throw new IllegalArgumentException("mediaList is null");
		for (Long mediaId : mediaIdList) {
			appendMediaToStory(toStoryId, mediaId);
		}
	}

	// Stream형식으로 범위만큼 가지고 오도록 수정.
	@Override
	@Transactional(readOnly = true)
	public List<Media> getMedies(Long fromStoryId, Stream stream) {
		//Story fromStory = storyRepository.select(toStoryId);
		List<Media> medies = mediaRepository.getMedies(fromStoryId, stream);
		return medies;
	}

	// 삭제한 것이 없다면 null반환 
	@Override
	@Transactional
	public boolean deleteMediaFromStory(Long mediaId) {
		//mediaRepository.delete(mediaRepository.select(mediaId));
		return true;
	}

	// 수정한 것이 없다면 null반환 
	@Override
	@Transactional
	public boolean updateMediesPermitionWithStoryPermition(Long storyId) {
		Story story = storyRepository.select(storyId);
		List<Media> medies = story.getMediaes();
		if(medies == null || medies.size() == 0)
			return false;
		
		// 스토리의 AGP를 꺼내서 스토리의 모든 미디어의 퍼미션을 같게 설정한다. 

		storyRepository.update(story);
		
		return true;
	}
	@Override
	@Transactional
	public List<Media> getFromDraft(Draft draft) {
		return mediaRepository.getFromDraft( draft );
	}
	
	@Override
	public List<Media> getFromContent(Content content) {
		return mediaRepository.getFromContent( content ); 
	}

	@Override
	public void save(Media media) {
		mediaRepository.save(media);
		
		if( media.getContent() != null ){
			contentService.plusMediaCount( media.getContent().getId() );
		}
		if( media.getDraft() != null ){
			draftService.plusMediaCount( media.getDraft().getId() );
		}
	}

	@Override
	public Media saveToTargetDraft(Media media) {
		Draft draft = draftService.selectFromTarget(media.getDraft().getTargetContent().getId(), media.getUser());
		media.setDraft(draft);
		mediaRepository.save(media);
		
		if( media.getDraft() != null ){
			draftService.plusMediaCount( media.getDraft().getId() );
		}
		return media;
	}

	@Override
	public void delFromContent(Long mediaId) {
		Media media = mediaRepository.select(mediaId);

		contentService.minusMediaCount( media.getContent().getId() );
		
		//mediaRepository.delete( media );

		media.setDeleteFlag(true);
		mediaRepository.update(media);
	}

	@Override
	public void del(Long mediaId) {
		Media media = mediaRepository.select(mediaId);
		if( media.getContent() != null ){
			contentService.minusMediaCount( media.getContent().getId() );
		}
		if( media.getDraft() != null ){
			draftService.minusMediaCount( media.getDraft().getId() );
		}
		//mediaRepository.delete(media);

		media.setDeleteFlag(true);
		mediaRepository.update(media);
	}

	@Override
	@Transactional
	public void delFromDraft(Long mediaId) {
		Media media = mediaRepository.select(mediaId);
		
		draftService.minusMediaCount(media.getDraft().getId());
		
		media.setDeleteFlag(true);
		mediaRepository.update(media);
		//mediaRepository.delete(media);
	}



	@Override
	@Transactional(readOnly = true)
	public Page<Media> getPagedResult(Sunny sunny, Long smallGroupId,
			User user, String query, Integer isMy, MediaType[] types,
			Integer page, int pageSize) {
		
		return mediaRepository.getPagedResult( sunny, smallGroupId, user, query, isMy, types, page, pageSize);
	}




	


}
