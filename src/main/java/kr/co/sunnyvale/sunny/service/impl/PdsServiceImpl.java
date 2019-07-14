package kr.co.sunnyvale.sunny.service.impl;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.ContentDynamic;
import kr.co.sunnyvale.sunny.domain.Pds;
import kr.co.sunnyvale.sunny.domain.Site;
import kr.co.sunnyvale.sunny.domain.SmallGroup;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.domain.post.ContentPermissionDTO;
import kr.co.sunnyvale.sunny.domain.post.ContentPostDTO;
import kr.co.sunnyvale.sunny.exception.SimpleSunnyException;
import kr.co.sunnyvale.sunny.repository.hibernate.ContentService;
import kr.co.sunnyvale.sunny.repository.hibernate.MediaRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.PdsRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.SmallGroupContentAccessRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.SmallGroupRepository;
import kr.co.sunnyvale.sunny.service.FeelService;
import kr.co.sunnyvale.sunny.service.MediaService;
import kr.co.sunnyvale.sunny.service.NotificationService;
import kr.co.sunnyvale.sunny.service.PdsService;
import kr.co.sunnyvale.sunny.service.ReplyService;
import kr.co.sunnyvale.sunny.service.SiteService;
import kr.co.sunnyvale.sunny.service.SmallGroupService;
import kr.co.sunnyvale.sunny.service.UserService;
import kr.co.sunnyvale.sunny.util.HtmlUtil;
import kr.co.sunnyvale.sunny.util.ParsedText;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( value="pdsService" )
@Transactional
public class PdsServiceImpl implements
		PdsService {
	
	@Autowired
	private SiteService siteService;
	
	
	@Autowired
	private ContentService contentService; 

	@Autowired
	private PdsRepository pdsRepository;
	
	@Autowired
	private SmallGroupRepository smallGroupRepository;
	
	@Autowired
	private SmallGroupService smallGroupService;
	
	@Autowired
	private SmallGroupContentAccessRepository smallGroupContentAccessRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ContentDynamicService contentDynamicService; 
	
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
	
	@Override
	@Transactional
	public Pds postPds(Sunny sunny, ContentPostDTO contentPost) {
		
		if( contentPost == null ){
			throw new SimpleSunnyException();
		}
		Site site = sunny.getSite();
		ParsedText parsedText = null;
		Pds pds = new Pds(sunny.getSite());
		
		if(contentPost.getTitle() != null ){
			pds.setTitle(contentPost.getTitle());
		}
		
		if(contentPost.getText() != null){
			pds.setRawText( contentPost.getText() );
			
			parsedText = HtmlUtil.parseRawText(contentPost.getText(), pds.getType());
			pds.setTaggedTextPrev(parsedText.getTaggedTextPrev());
			pds.setTaggedTextNext(parsedText.getTaggedTextNext());
			pds.setReturnCount(parsedText.getReturnCount());
		}
		
		SmallGroup smallGroup = null;
		
		if( contentPost.getSmallGroupId() == null ){
			smallGroup = site.getLobbySmallGroup();
		}else{
			smallGroup = smallGroupRepository.select( contentPost.getSmallGroupId() );
		}
		
		pds.setUser(contentPost.getUser());
		pds.setSmallGroup(smallGroup);
		pds.setSmallGroupAbsolutePath(smallGroup.getAbsolutePath());
		pds.setPermissionType(contentPost.getPermissionType());
		pds.setIpAddress(contentPost.getIpAddress());
		pds.setRequestBody(contentPost.getRequestBody());
		pdsRepository.save(pds);
		
		smallGroupService.plusEventCount(smallGroup.getId());
		
		if( contentPost.getMediaIds() != null ){
			List<Long> mediaIdList = contentPost.getMediaIds();
			Long[] mediaIds = (Long[]) mediaIdList.toArray( new Long[mediaIdList.size()]);
			
			if( mediaIds != null ){
				//int mediaCount = mediaRepository.updateMediaesBelongToContent(pds, mediaIds);
				mediaService.appendMediaesToContent(pds.getId(), mediaIds);
				pds.setMediaCount(mediaIds.length);
			}	
		}
		
		
		pds.setWrapAbsolutePath(pds.getId());
		pds.setThread(pds.getId());
		pdsRepository.update(pds);
		
		
		List<ContentPermissionDTO> permissions = contentPost.getPermissions();
		if( permissions != null ){
			//User user = storyPost.getUser();
			for( ContentPermissionDTO permission : permissions ){
				
				
				SmallGroup saveSmallGroup = null;
				/*
				 * 사용자 한명 한명 선택했을 경우. 이때는 새로 smallGroup 을 만들어서 넣어줘야한다. 
				 */
//				if( permission.getSmallGroupType() == null ){
//					System.out.println("타입이 없다. 사람이다." + permission.getId());
//					User targetUser = new User(permission.getId());
//					saveSmallGroup  = smallGroupService.generateUserDefineGroup(sunny, contentPost.getUser(), targetUser );
//					smallGroupContentAccessRepository.save( pds, saveSmallGroup, permission.getR(), permission.getW(), permission.getD());
//					continue;
//				}
				
//				if( permission.getChildren() == null || permission.getChildren() == false ){
					System.out.println("자식이 없다. 체크 안한거다. " + permission.getId());
					saveSmallGroup = new SmallGroup(permission.getId());
					//smallGroupContentAccessRepository.save( pds, saveSmallGroup, permission.getR(), permission.getW(), permission.getD());
					smallGroupContentAccessRepository.save( pds, saveSmallGroup);
					continue;
//				}
//				
//				saveSmallGroup = smallGroupService.getSmallGroup(permission.getId());
//				
//				List<Long> smallGroupIds = smallGroupRepository.getSmallGroupIdList(sunny.getSite().getId(), saveSmallGroup.getAbsolutePath());
//				
//				for( Long smallGroupId : smallGroupIds ){
//					System.out.println("하위 아이디 : " + smallGroupId);
//					smallGroupContentAccessRepository.save( pds, new SmallGroup( smallGroupId ), permission.getR(), permission.getW(), permission.getD());
//				}
			}
			
		}
		
		
//		if( parsedText != null ){
//			if( parsedText.getMentionReceivers() != null && parsedText.getMentionReceivers().size() > 0 ){
//				Set<Long> userIds = parsedText.getMentionReceivers();
//				for( Long userId : userIds ){
//					notificationService.mentionContent(sunny, userId, pds);
//				}	
//			}
//		}
		
		
		
		User user = userService.findById(contentPost.getUserId());
		
		return pds;
	}

//	@Override
//	public StoryDTO getStoryDTO(Sunny sunny, Long id, User user) {
//		StoryDTO story = storyRepository.getStoryDTO(id);
//		operationAfterStory(sunny, story, null, user);
//		
//		return story;
//	}

	/**
	 * 가져온 스토리에 대한
	 * 평가, 댓글 등등에 대한 데이터 작업을 한다. 
	 * @param story
	 */
//	private void operationAfterStory( Sunny sunny, StoryDTO story, String[] queries, User user ){
//		Stream stream = new Stream(null, null, null, 3);
//		List<Reply> replys = replyService.getReplyList(story.getId(), stream);
//
//		Collections.reverse(replys);
//		story.setReplys(replys);
//
//		if( !sunny.getSite().getLobbySmallGroup().getId().equals( story.getGroupId() ) ){
//			story.setGroupStory(true);
//		}
//		
//		/*
//		 * 공유된 스토리의 경우엔 스트림에서 보여지는 사진을 공유한 스토리에 임시로 넣어놓는다.
//		 */
//		
//		if( story.getMediaCount() > 0 ){
//			stream.setSize(4);
//			story.setMediaes(mediaService.getMedies(story.getId(), stream) );
//		}	
//		
//		/*
//		 * 검색 중인 경우엔 하이라이트기능
//		 */
//		if( queries != null ){
//			story.fixSearchedText(queries);
//		}
//		
//		if( user == null )
//			return;
//		
//		for( Reply reply : replys ){
//			Feel feel = feelService.getFeelFromContentUser(user, reply);
//			if( feel != null ){
//				reply.setFeeledId(feel.getId());
//			}
//		}
//		
//		Feel feel = feelService.getFeelFromContentUser(user, new Story(story.getId()));
//		if( feel != null )
//			story.setFeeledId(feel.getId());
//	}
	

//	private List<StoryDTO> gatherStoryDatas(Sunny sunny,
//			List<StoryDTO> stories, String[] queries, User user) {
//		
//		if( stories == null || stories.size() == 0 )
//			return null;
//		
//		for( StoryDTO story : stories ){
//			operationAfterStory(sunny, story, queries, user);
//		}
//
//		return stories;
//	}
//

	@Override
	public void delete(Long pdsId) {
		Pds pds = pdsRepository.select(pdsId);
		// TODO: 삭제 부분 안되어있음
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
	public boolean checkPermission(Long id, User user) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Page<Pds> getPagedSmallGroupsPds(Sunny sunny, Long smallGroupId, User basecampUser, 
			User authUser, String query, String ordering, Integer page,
			int pageSize) {
		
		if( smallGroupId == null )
			smallGroupId = sunny.getSite().getLobbySmallGroup().getId();

		return pdsRepository.getPagedSmallGroupPds(sunny, smallGroupId, basecampUser, query, ordering, page, pageSize);
	}

//	@Override
//	public Pds findById(Sunny sunny, User user, Long pdsId) {
//		Pds pds = pdsRepository.select(pdsId);
//		
//		return pds;  
//	}
	
	@Override
	public Pds getPds(Sunny sunny, User user, Long pdsId) {
		Pds pds = pdsRepository.select(pdsId);
		
		contentService.operationAfterContent(sunny, pds, null, user);
		return pds;  
	}


	
}
