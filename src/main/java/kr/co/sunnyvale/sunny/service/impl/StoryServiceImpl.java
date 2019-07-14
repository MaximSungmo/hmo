package kr.co.sunnyvale.sunny.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import kr.co.sunnyvale.sunny.domain.ContentDynamic;
import kr.co.sunnyvale.sunny.domain.Feel;
import kr.co.sunnyvale.sunny.domain.LastRead;
import kr.co.sunnyvale.sunny.domain.Media;
import kr.co.sunnyvale.sunny.domain.Reply;
import kr.co.sunnyvale.sunny.domain.Site;
import kr.co.sunnyvale.sunny.domain.SmallGroup;
import kr.co.sunnyvale.sunny.domain.SmallGroupContentAccess;
import kr.co.sunnyvale.sunny.domain.Story;
import kr.co.sunnyvale.sunny.domain.Tag;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.dto.MediaDTO;
import kr.co.sunnyvale.sunny.domain.dto.NoticeDTO;
import kr.co.sunnyvale.sunny.domain.dto.NotifyInfoDTO;
import kr.co.sunnyvale.sunny.domain.dto.StoryDTO;
import kr.co.sunnyvale.sunny.domain.dto.StoryModifyDTO;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.domain.post.StoryModifyPostDTO;
import kr.co.sunnyvale.sunny.domain.post.StoryPermissionDTO;
import kr.co.sunnyvale.sunny.domain.post.StoryPostDTO;
import kr.co.sunnyvale.sunny.exception.PageNotFoundException;
import kr.co.sunnyvale.sunny.exception.SimpleSunnyException;
import kr.co.sunnyvale.sunny.repository.hibernate.ContentService;
import kr.co.sunnyvale.sunny.repository.hibernate.LastReadRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.MediaRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.SmallGroupContentAccessRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.SmallGroupRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.StoryRepository;
import kr.co.sunnyvale.sunny.service.AfterService;
import kr.co.sunnyvale.sunny.service.FeelService;
import kr.co.sunnyvale.sunny.service.MediaService;
import kr.co.sunnyvale.sunny.service.NotificationService;
import kr.co.sunnyvale.sunny.service.ReplyService;
import kr.co.sunnyvale.sunny.service.SiteService;
import kr.co.sunnyvale.sunny.service.SmallGroupService;
import kr.co.sunnyvale.sunny.service.StoryService;
import kr.co.sunnyvale.sunny.service.TagService;
import kr.co.sunnyvale.sunny.service.UserService;
import kr.co.sunnyvale.sunny.util.HtmlUtil;
import kr.co.sunnyvale.sunny.util.ParsedText;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

@Service( value="storyService" )
@Transactional
public class StoryServiceImpl implements StoryService {
	
	@Autowired
	private SiteService siteService;
	
	@Autowired
	private StoryRepository storyRepository;
	
	@Autowired
	private SmallGroupRepository smallGroupRepository;
	
	@Autowired
	private SmallGroupService smallGroupService;
	
	
	@Autowired
	private LastReadRepository lastReadRepository;

	
	@Autowired
	private SmallGroupContentAccessRepository smallGroupContentAccessRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ContentDynamicService contentDynamicService;
	
	@Autowired
	private ContentReadUserService contentReadUserService;
	
	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private MediaRepository mediaRepository;
	
	@Autowired
	private ReplyService replyService; 
	
	@Autowired
	private MediaService mediaService; 

	@Autowired
	private FeelService feelService; 
	
	@Autowired
	private ContentService contentService;
	
	@Autowired
	private RevisionService revisionService;
	
	@Autowired
	private AfterService afterService; 
	
	@Autowired
	private TagService tagService; 
	

	@Override
	@Transactional
	public Story postSuperStory(Sunny sunny, StoryPostDTO storyPost) {
		if( storyPost == null ){
			throw new SimpleSunnyException();
		}
		Story story = new Story();

		ParsedText parsedText = null;
		if(storyPost.getText() != null){
			story.setRawText( storyPost.getText() );
			
			parsedText = HtmlUtil.parseRawText(storyPost.getText(), story.getType());
			story.setTaggedTextPrev(parsedText.getTaggedTextPrev());
			story.setTaggedTextNext(parsedText.getTaggedTextNext());
			story.setReturnCount(parsedText.getReturnCount());
		}
		
		if( parsedText != null && parsedText.getTagTitles() != null && parsedText.getTagTitles().size() > 0 ){
			
			story.setRawTagsStringFromSet(parsedText.getTagTitles());
			contentService.changeTags(story,  parsedText.getTagTitles());
			
		}
		SmallGroup smallGroup = new SmallGroup(SmallGroup.SYSTEM_GROUP); 
		story.setSmallGroup( smallGroup );
		story.setPermissionType(storyPost.getPermissionType());
		story.setIpAddress(storyPost.getIpAddress());
		story.setRequestBody(storyPost.getRequestBody());
		story.setNotice(storyPost.isNotice());
		story.setPostUser(storyPost.getPostUser());
		story.setUser(storyPost.getPostUser());

		storyRepository.save(story);

		if( smallGroup != null ){
			smallGroupService.plusEventCount(smallGroup.getId());
		}
		
		List<Long> mediaIds = storyPost.getMediaIds();
		if( mediaIds != null ){
			//int mediaCount = mediaRepository.updateMediaesBelongToContent(story, mediaIds);
			mediaService.appendMediesToStory(story.getId(), mediaIds);
			story.setMediaCount(mediaIds.size());
		}
		
		story.setWrapAbsolutePath(story.getId());
		story.setThread(story.getId());
		storyRepository.update(story);
		
		List<StoryPermissionDTO> permissions = storyPost.getPermissions();
		if( permissions != null ){
			for( StoryPermissionDTO permission : permissions ){
				SmallGroup saveSmallGroup = null;
					saveSmallGroup = new SmallGroup(permission.getId());
					smallGroupContentAccessRepository.save( story, saveSmallGroup);
					continue;
			}
			
		}
		// 글을 쓴 당사자는 항상 존재한다. 
//		SmallGroupContentAccess mySmallGroupContentAccess = smallGroupContentAccessRepository.findByContentAndSmallGroup(story.getId(), storyPost.getPostUser().getMySmallGroup().getId() );
//		if( mySmallGroupContentAccess == null ){
//			smallGroupContentAccessRepository.save( story, storyPost.getPostUser().getMySmallGroup());
//		}
			
		
//		if( parsedText != null ){
//			if( parsedText.getMentionReceivers() != null && parsedText.getMentionReceivers().size() > 0 ){
//				Set<Long> userIds = parsedText.getMentionReceivers();
//				for( Long userId : userIds ){
//					notificationService.mentionStory(sunny, userId, story);
//				}	
//			}
//		}
		
		
		
//		if( story.isNotice() == true ){
//			afterService.notice( sunny, storyPost.getUser().getId() );
//			
//		}
		
//		User user = userService.findById(storyPost.getUserId());
		
		return story;
	}

	
	@Override
	@Transactional
	public Story postStory(Sunny sunny, StoryPostDTO storyPost) {
		
		if( storyPost == null ){
			throw new SimpleSunnyException();
		}
		
		Site site = sunny.getSite();
		ParsedText parsedText = null;
		Story story = new Story(sunny.getSite());
		
		if(storyPost.getText() != null){
			story.setRawText( storyPost.getText() );
			
			parsedText = HtmlUtil.parseRawText(storyPost.getText(), story.getType());
			story.setTaggedTextPrev(parsedText.getTaggedTextPrev());
			story.setTaggedTextNext(parsedText.getTaggedTextNext());
			story.setReturnCount(parsedText.getReturnCount());
		}
		
		if( parsedText != null && parsedText.getTagTitles() != null && parsedText.getTagTitles().size() > 0 ){
			story.setRawTagsStringFromSet(parsedText.getTagTitles());
			contentService.changeTags(story,  parsedText.getTagTitles());
		}
		
		SmallGroup smallGroup = null;
		
		if( storyPost.isFeedback() ){
			story.setFeedback(true);
		}else if( storyPost.getSmallGroupId() == null ){
			smallGroup = site.getLobbySmallGroup();
		}else{
			smallGroup = smallGroupRepository.select( storyPost.getSmallGroupId() );
		}
		
		story.setUser(storyPost.getUser());
		if( smallGroup != null ){
			story.setSmallGroup(smallGroup);
			story.setSmallGroupAbsolutePath(smallGroup.getAbsolutePath());
		}
		story.setPermissionType(storyPost.getPermissionType());
		story.setIpAddress(storyPost.getIpAddress());
		story.setRequestBody(storyPost.getRequestBody());
		story.setNotice(storyPost.isNotice());
		story.setPostUser(storyPost.getPostUser());
		
		if( storyPost.isFeedback() ) {
			story.setSite(null);
		}
		
		storyRepository.save(story);

		if( smallGroup != null ){
			smallGroupService.plusEventCount(smallGroup.getId());
		}
		List<Long> mediaIds = storyPost.getMediaIds();
		if( mediaIds != null ){
			//int mediaCount = mediaRepository.updateMediaesBelongToContent(story, mediaIds);
			mediaService.appendMediesToStory(story.getId(), mediaIds);
			story.setMediaCount(mediaIds.size());
		}
		
		story.setWrapAbsolutePath(story.getId());
		story.setThread(story.getId());
		storyRepository.update(story);
		

		if( storyPost.isFeedback() ){
			return story;
		}
		List<StoryPermissionDTO> permissions = storyPost.getPermissions();
		if( permissions != null ){
			for( StoryPermissionDTO permission : permissions ){
				
				
				SmallGroup saveSmallGroup = null;
				/*
				 * 자식이 없으면 "하위부서포함"을 체크 안한것임.
				 */
//				if( permission.getChildren() == null || permission.getChildren() == false ){
					saveSmallGroup = new SmallGroup(permission.getId());
					//smallGroupContentAccessRepository.save( story, saveSmallGroup, permission.getR(), permission.getW(), permission.getD());
					smallGroupContentAccessRepository.save( story, saveSmallGroup);
					continue;
//				}
				
//				saveSmallGroup = smallGroupService.getSmallGroup(permission.getId());
//				
//				List<Long> smallGroupIds = smallGroupRepository.getSmallGroupIdList(sunny.getSite().getId(), saveSmallGroup.getAbsolutePath());
//				
//				for( Long smallGroupId : smallGroupIds ){
//					//smallGroupContentAccessRepository.save( story, new SmallGroup( smallGroupId ), permission.getR(), permission.getW(), permission.getD());
//					smallGroupContentAccessRepository.save( story, new SmallGroup( smallGroupId ) );
//				}
			}
			
		}
		// 글을 쓴 당사자는 항상 존재한다. 
		SmallGroupContentAccess mySmallGroupContentAccess = smallGroupContentAccessRepository.findByContentAndSmallGroup(story.getId(), storyPost.getPostUser().getMySmallGroup().getId() );
		if( mySmallGroupContentAccess == null ){
			smallGroupContentAccessRepository.save( story, storyPost.getPostUser().getMySmallGroup());
		}
			
		
		if( parsedText != null ){
			if( parsedText.getMentionReceivers() != null && parsedText.getMentionReceivers().size() > 0 ){
				Set<Long> userIds = parsedText.getMentionReceivers();
				for( Long userId : userIds ){
					notificationService.mentionStory(sunny, userId, story);
				}	
			}
		}
		
		
		
		if( story.isNotice() == true ){
			afterService.notice( sunny, story.getId(), storyPost.getUser().getId() );
			
		}
		
		User user = userService.findById(storyPost.getUserId());
		
		return story;
	}

	@Override
	@Transactional
	public StoryDTO getStoryDTO(Sunny sunny, Long id, User user) {
		
		
		//boolean isFeedback = storyRepository.isFeedback( id );		
		
		Story persistentStory = storyRepository.select(id);
		
		StoryDTO story = null;
		

		if( persistentStory.isFeedback() ) {
			story = storyRepository.getFeedbackStoryDTO(id);
		}else if( persistentStory.getSmallGroup() != null && persistentStory.getSmallGroup().getId() == SmallGroup.SYSTEM_GROUP ){
			story = storyRepository.getSuperStoryDTO( id );
		}else{
			story = storyRepository.getStoryDTO( id, user );
		}
		
		if( story == null ){
			throw new PageNotFoundException();
		}
		operationAfterStory(sunny, story, null, user);
		
		return story;
	}

	/**
	 * 가져온 스토리에 대한
	 * 평가, 댓글 등등에 대한 데이터 작업을 한다. 
	 * @param story
	 */
	private void operationAfterStory( Sunny sunny, StoryDTO story, String[] queries, User user ){
		Stream stream = new Stream(null, null, null, 3);
		List<Reply> replys = replyService.getReplyList(user, story.getId(), stream);

		Collections.reverse(replys);
		story.setReplys(replys);

		
		if( sunny != null && !sunny.getSite().getLobbySmallGroup().getId().equals( story.getSmallGroupId() ) ){
			story.setGroupStory(true);
		}
		
		/*
		 * 공유된 스토리의 경우엔 스트림에서 보여지는 사진을 공유한 스토리에 임시로 넣어놓는다.
		 */
		
		if( story.getMediaCount() > 0 ){
			stream.setSize(12);
			story.setMediaes(mediaService.getMedies(story.getId(), stream) );
		}	
		
		/*
		 * 검색 중인 경우엔 하이라이트기능
		 */
		if( queries != null ){
			story.fixSearchedText(queries);
		}
		
		if( user == null )
			return;
		
//		for( Reply reply : replys ){
//			Feel feel = feelService.getFeelFromContentUser(user, reply);
//			if( feel != null ){
//				reply.setFeeledId(feel.getId());
//			}
//		}
		
		Feel feel = feelService.getFeelFromContentUser(user, new Story(story.getId()));
		if( feel != null )
			story.setFeeledId(feel.getId());
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public List<StoryDTO> getSuperAdminStories(Sunny sunny, Long siteId, User user,
			String[] queries, Stream stream) {
		return storyRepository.getSuperAdminStories(siteId, user, queries, stream);
	}
	
	@Override
	@Transactional
	public List<StoryDTO> fetchLobbyStories(Sunny sunny, User user, String[] queries, Stream stream) {
		List<StoryDTO> storyDTOs = storyRepository.fetchLobbyStories(sunny, user, queries, stream);
		return gatherStoryDatas(sunny, storyDTOs, queries, user);
	}




	@Override
	@Transactional(readOnly = true)
	public List<StoryDTO> fetchSuperStories(Sunny sunny, User user,
			Stream stream) {
		
		
		List<StoryDTO> storyDTOs = storyRepository.fetchSuperStories(user, stream);
		
		return gatherStoryDatas(null, storyDTOs, null, user);
	}
	
	@Override
	public List<StoryDTO> fetchFeedback(User user, String[] queries,
			Stream stream) {
		
		List<StoryDTO> storyDTOs = storyRepository.fetchFeedback(user, queries, stream);
		
		return gatherStoryDatas(null, storyDTOs, queries, user);
	}

	
	@Override
	public List<StoryDTO> fetchNoticeStories(Sunny sunny, User user,
			String[] queries, Stream stream) {
		
		
		LastRead lastRead = lastReadRepository.findUniqByObject("user", user);
		if( lastRead == null ){
			lastRead = new LastRead(user);
			lastRead.updateNoticeDate();
			lastReadRepository.save(lastRead);
		}else{
			lastRead.updateNoticeDate();
			lastReadRepository.update(lastRead);
		}
		
		List<StoryDTO> storyDTOs = storyRepository.fetchNoticeStories(sunny, user, queries, stream);
		return gatherStoryDatas(sunny, storyDTOs, queries, user);
	}
	@Override
	@Transactional
	public List<StoryDTO> fetchNewsfeedStories(Sunny sunny, User user, String[] queries, Stream stream) {
		List<StoryDTO> storyDTOs = storyRepository.fetchNewsfeedStories(sunny, user, queries, stream);
		return gatherStoryDatas(sunny, storyDTOs, queries, user);
		
	}

	private List<StoryDTO> gatherStoryDatas(Sunny sunny,
			List<StoryDTO> stories, String[] queries, User user) {
		
		if( stories == null || stories.size() == 0 )
			return null;
		
		for( StoryDTO story : stories ){
			operationAfterStory(sunny, story, queries, user);
		}

		return stories;
	}


	@Override
	public List<StoryDTO> fetchBasecampStories(Sunny sunny, User basecampUser, User authUser,
			String[] queries, Stream stream) {
		List<StoryDTO> storyDTOs = storyRepository.fetchBasecampStories(sunny, basecampUser, authUser, queries, stream);
		return gatherStoryDatas(sunny, storyDTOs, queries, authUser);
	}

	
	@Override
	@Transactional
	public List<StoryDTO> fetchSmallGroupStories(Sunny sunny, Long smallGroupId, User user, Boolean isWantChildren, String[] queries, Stream stream) {
		SmallGroup smallGroup = smallGroupRepository.select(smallGroupId);

		if( smallGroup.getType() == SmallGroup.TYPE_LOBBY ){
			throw new SimpleSunnyException();
		}
		List<StoryDTO> storyDTOs = storyRepository.fetchSmallGroupStories(sunny, smallGroupId, user, isWantChildren, queries, stream); 
		return gatherStoryDatas(sunny, storyDTOs, queries, user);
		
	}
	

	
	@Override
	@Transactional
	public void delete(Long storyId) {
		
		Story story = storyRepository.select(storyId);
		
		Set<Tag> existTags = tagService.getTags(story.getId(), null);
		
		if( existTags != null ){
			tagService.detag( existTags );
		}
		
		storyRepository.delete(story);
		
//		Story story = storyRepository.select(storyId);
//		story.setDeleteFlag(true);
//		storyRepository.update(story);
	}
	
	@Override
	@Transactional
	public void plusReplyCount(Long storyId) {
		ContentDynamic dynamic = contentDynamicService.findOrCreateByContentId(storyId);
		dynamic.setReplyCount( dynamic.getReplyCount() + 1 );
	}
	
	@Override
	@Transactional
	public void minusReplyCount(Long storyId) {
		ContentDynamic dynamic = contentDynamicService.findOrCreateByContentId(storyId);
		dynamic.setReplyCount( dynamic.getReplyCount() - 1 );
	}


	
	@Override
	public StoryModifyDTO getModifyStory(Sunny sunny, User user, Long id) {

		
		Story story = storyRepository.select(id);
		
		if( story == null)
			return null;
		
		StoryModifyDTO storyModifyDto = new StoryModifyDTO();
		
		storyModifyDto.setRequestBody( story.getRequestBody() );
		
		List<Media> medias = mediaService.getFromContent(story);
		
		List<MediaDTO> mediaDtos = Lists.newArrayList();
		for( Media media : medias ){
			mediaDtos.add( new MediaDTO( media.getId(), media.getFileName(), media.getMediaType(), media.getExtName(), media.getWidth(), media.getHeight(), media.getSize(), media.getMediumPath() ));
		}
		
		storyModifyDto.setMedias(mediaDtos);
		
		return storyModifyDto;
	}

	@Override
	@Transactional
	public Story modifyStory(Sunny sunny, Long storyId, StoryModifyPostDTO storyModify) {
		
		Story story = storyRepository.select(storyId);
		
		revisionService.createVersion(story);

		ParsedText parsedText = null;
		
		if(storyModify.getText() != null){
			story.setRawText( storyModify.getText() );
			
			parsedText = HtmlUtil.parseRawText(storyModify.getText(), story.getType());
			story.setTaggedTextPrev(parsedText.getTaggedTextPrev());
			story.setTaggedTextNext(parsedText.getTaggedTextNext());
			story.setReturnCount(parsedText.getReturnCount());
		}
		
		if( parsedText != null && parsedText.getTagTitles() != null && parsedText.getTagTitles().size() > 0 ){
			
			story.setRawTagsStringFromSet(parsedText.getTagTitles());
			contentService.changeTags(story,  parsedText.getTagTitles());
			
		}
		
		
		List<Media> medias = mediaService.getFromContent(story);
		for( Media media : medias ){
			media.setUsed(false);
			media.setContent(null);
			mediaRepository.update(media);
		}
		
		story.setMediaCount(0);
		
		List<Long> mediaIds = storyModify.getMediaIds();
		if( mediaIds != null ){
			//int mediaCount = mediaRepository.updateMediaesBelongToContent(story, mediaIds);
			mediaService.appendMediesToStory(story.getId(), mediaIds);
			story.setMediaCount(mediaIds.size());
			
			story.setMediaes(mediaService.getMedies(story.getId(), new Stream(12)) );
		}
		story.setUpdateDate(new Date());
		story.setRequestBody( storyModify.getRequestBody() );
		storyRepository.update(story);
		
		return story;
	}

	
		
	@Override
	@Transactional(readOnly = true)
	public Number getNoticeUnreadCount(Sunny sunny, User user) {

		LastRead lastRead = lastReadRepository.findUniqByObject("user", user);
		//NotifyInfoDTO notifyInfoDto = storyRepository.getNoticeNotifyDto(sunny, user.getId(), lastRead);
		Number unreadCount = storyRepository.getNoticeUnreadCount(sunny, user, lastRead);
		int noticeReadCount = storyRepository.getNoticeReadCount( sunny, user.getId(), lastRead );
		int retVal = unreadCount.intValue() - noticeReadCount;
		return retVal >= 0 ? retVal : 0;
	}
	
	@Override
	public List<NoticeDTO> getNotices(Sunny sunny, User user, Long smallGroupId, Stream stream) {
		
		List<NoticeDTO> notices = storyRepository.getNotices(sunny, user, smallGroupId, stream);
		
		if( user == null ){
			return notices;
		}
		
		for(NoticeDTO notice : notices ){
			notice.setRead( contentReadUserService.alreadyRead(notice.getId(), user));
		}
		
		return notices; 
	}

	@Override
	@Transactional
	public List<NoticeDTO> getNoticesAndUpdate(Sunny sunny, User user,
			Long smallGroupId, Stream stream) {
		
		LastRead lastRead = lastReadRepository.findUniqByObject("user", user);
		if( lastRead == null ){
			lastRead = new LastRead(user);
			lastRead.updateNoticeDate();
			lastReadRepository.save(lastRead);
		}else{
			lastRead.updateNoticeDate();
			lastReadRepository.update(lastRead);
		}

		return getNotices(sunny, user, smallGroupId, stream);		
	}

	@Override
	@Transactional(readOnly = true)
	public Story geNoticeNewOne(Sunny sunny, User user) {
		LastRead lastRead = lastReadRepository.findUniqByObject("user", user);
		
		return storyRepository.getNoticeNewOne( sunny, user, lastRead );
	}

	@Override
	@Transactional(readOnly = true)
	public NotifyInfoDTO getNoticeNotifyInfo(Sunny sunny, Long userId) {
		
		LastRead lastRead = lastReadRepository.findUniqByObject("user.id", userId);
		NotifyInfoDTO notifyInfoDto = storyRepository.getNoticeNotifyDto(sunny, userId, lastRead);
		int noticeReadCount = storyRepository.getNoticeReadCount( sunny, userId, lastRead );
		int retVal = notifyInfoDto.getUnreadCount() - noticeReadCount;
		
		notifyInfoDto.setUnreadCount( retVal >= 0 ? retVal : 0 );
		return notifyInfoDto; 
	}

	@Override
	public int getSiteStoryCount(Long siteId) {
		return storyRepository.getSiteStoryCount(siteId);
	}


	
}
