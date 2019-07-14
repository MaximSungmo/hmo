package kr.co.sunnyvale.sunny.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import kr.co.sunnyvale.sunny.domain.Content;
import kr.co.sunnyvale.sunny.domain.Feel;
import kr.co.sunnyvale.sunny.domain.Reply;
import kr.co.sunnyvale.sunny.domain.SmallGroupApproval;
import kr.co.sunnyvale.sunny.domain.Story;
import kr.co.sunnyvale.sunny.domain.Tag;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.dto.ContentDTO;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.exception.SimpleSunnyException;
import kr.co.sunnyvale.sunny.repository.hibernate.ChildContentRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.ContentService;
import kr.co.sunnyvale.sunny.service.ApprovalService;
import kr.co.sunnyvale.sunny.service.FeelService;
import kr.co.sunnyvale.sunny.service.MediaService;
import kr.co.sunnyvale.sunny.service.ReplyService;
import kr.co.sunnyvale.sunny.service.SmallGroupApprovalService;
import kr.co.sunnyvale.sunny.service.SmallGroupService;
import kr.co.sunnyvale.sunny.service.TagService;
import kr.co.sunnyvale.sunny.util.HtmlUtil;
import kr.co.sunnyvale.sunny.util.ParsedText;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Sets;

@Service( value="contentService" )
@Transactional
public class ContentServiceImpl implements ContentService{
	

	@Autowired
	private ChildContentRepository contentRepository;
	
	@Autowired
	private ApprovalService approvalService; 
	
	@Autowired
	private OperationService operationService;
	
	@Autowired
	private SmallGroupService smallGroupService;
	
	@Autowired
	private SmallGroupApprovalService smallGroupApprovalService;
	
	@Autowired
	private MediaService mediaService; 
	
//	@Autowired
//	private ReadonlyContentRepository readonlyContentRepository;
	
	@Autowired
	private TagService tagService; 
	
	@Autowired
	private ContentAndTagService tagRelationService;
	
	@Autowired
	private FeelService feelService;
	
	@Autowired
	private ReplyService replyService;
	
	@Autowired
	private ContentReadUserService contentViewUserService;
	
	@Autowired
	private ContentReadCountService contentViewCountService;
	
	@Autowired
	private RevisionService revisionService;
	
	@Override
	@Transactional(readOnly = true)
	public Content getContent(Long contentId) {
		Content content = contentRepository.select(contentId);
		return content;
	}

	@Override
	@Transactional
	public void update(Content content) {
		contentRepository.update(content);		
	}
	
	@Override
	@Transactional(readOnly = true)
	public Integer getContentType(Long id) {
		// TODO 캐시
		return (Integer) contentRepository.findColumnByObject("type", "id", id);
	}
	
	@Override
	@Transactional
	public void delete(Sunny sunny, Long id) {
		Content content = contentRepository.select(id);
		if( content == null ){
			throw new SimpleSunnyException("story.noExist");
		}
		
		Set<Tag> tags = content.getTags();
		
		tagService.detag(tags);
		
		switch( content.getType() ){
			case kr.co.sunnyvale.sunny.domain.Content.TYPE_REPLY:
				Reply reply = replyService.select( id );
				operationService.minusReplyCount( reply.getContent().getId() );
				break;
			case Content.TYPE_NOTE:
				if( content.getSmallGroup() != null ){
					smallGroupService.minusNoteCount(content.getSmallGroup().getId());
				}
			case Content.TYPE_STORY:
				if( content.getSmallGroup() != null ){
					smallGroupService.minusEventCount(content.getSmallGroup().getId());
				}
		}
		//contentRepository.delete(content);
		content.setDeleteFlag(true);
	}

	@Override
	@Transactional( readOnly = true )
	public boolean isSameUser(Long contentId, Long userId) {
		
		String contentUserId = (String) contentRepository.findColumnByObject("user.id", "id", contentId);
		System.out.println();
		if( userId.equals(contentUserId) )
			return true;
				
		return false; 
	}
	
	@Override
	@Transactional
	public Content changeTags( Content content, Set<String> tagTitles ){
		
		
		Set<Tag> existTags = tagService.getTags(content.getId(), null);
		
		if( existTags != null ){
			tagService.detag( existTags );
		}
		
		Set<Tag> persistentTags = Sets.newHashSet();
		persistentTags = tagService.findAndGenerateTags(content.getSmallGroup(), content.getType(), tagTitles);
		
		if( persistentTags != null ){
			tagService.tag( persistentTags );
		}
		
		content.setTags(persistentTags);
		
		return content;
	}
	
	@Override
	public Set<String> parseStringifyToSet( String tagString ){
		StringTokenizer tokenizer = new StringTokenizer( tagString, ",");
		
		Set<String> tagStrings = Sets.newHashSet();
		while( tokenizer.hasMoreTokens() ){
			tagStrings.add(tokenizer.nextToken());
		}
		return tagStrings;
	}
	
	@Override
	@Transactional
	public Content changeTags( Content content, String rawTagsString ){
		
		changeTags(content, parseStringifyToSet(rawTagsString));
		return content;
		
	}
	@Override
	@Transactional
	public void versionUpdate(Content content) {
		
		Content persistentContent = contentRepository.select(content.getId());
		
		
		/*
		 * 태그 X -> Answer Skip
		 * 태그 O -> DB 태그들과 다를 시 -> 변경
		 */
		if( persistentContent.getRawTagsString() == null || content.getRawTagsString() != null && !persistentContent.getRawTagsString().equals( content.getRawTagsString() )){
			changeTags( persistentContent, content.getRawTagsString() );
		}
		

		persistentContent.setRawText( content.getRawText() );
		
		ParsedText parsedText = HtmlUtil.parseRawText(content.getRawText(), persistentContent.getType());
		persistentContent.setTaggedTextPrev(parsedText.getTaggedTextPrev());
		persistentContent.setTaggedTextNext(parsedText.getTaggedTextNext());
		persistentContent.setReturnCount(parsedText.getReturnCount());
		
		persistentContent.setTitle(content.getTitle());
		
		persistentContent.initLongTextTitle();
		
		persistentContent.setIpAddress( content.getIpAddress() );

		persistentContent.setUpdateDate( new Date() );
		
		contentRepository.update( persistentContent );

		revisionService.createVersion(persistentContent);
	}
	

	@Override
	@Transactional
	public void plusMediaCount(Long id) {
		Content content = contentRepository.select(id);
		content.setMediaCount( content.getMediaCount() + 1 );
	}

	@Override
	@Transactional
	public void minusMediaCount(Long id) {
		Content content = contentRepository.select(id);
		content.setMediaCount( content.getMediaCount() - 1 );
	}

	@Override
	public List<ContentDTO> fetchApprovals(Sunny sunny, String menu, Long smallGroupId, Boolean isWantChildren, User authUser,  String[] queries, Stream stream) {
		List<ContentDTO> contentDTOs = contentRepository.fetchApprovals(sunny,menu,smallGroupId, isWantChildren, authUser, queries, stream);
		return gatherContentDatas(sunny, contentDTOs, queries, authUser);
	}
	private List<ContentDTO> gatherContentDatas(Sunny sunny,
			List<ContentDTO> contentDTOs, String[] queries, User user) {
		if( contentDTOs == null || contentDTOs.size() == 0 )
			return null;
		
		for( ContentDTO content : contentDTOs ){
			operationAfterContent(sunny, content, queries, user);
		}

		return contentDTOs;
	}
	

	/**
	 * 가져온 스토리에 대한
	 * 평가, 댓글 등등에 대한 데이터 작업을 한다. 
	 * @param content
	 */
	@Override
	public void operationAfterContent( Sunny sunny, ContentDTO content, String[] queries, User user ){
		Stream stream = new Stream(null, null, null, 3);
		List<Reply> replys = replyService.getReplyList(user, content.getId(), stream);

		Collections.reverse(replys);
		content.setReplys(replys);

		
		if( !sunny.getSite().getLobbySmallGroup().getId().equals( content.getSmallGroupId() ) ){
			content.setGroupStory(true);
		}
		
		/*
		 * 공유된 스토리의 경우엔 스트림에서 보여지는 사진을 공유한 스토리에 임시로 넣어놓는다.
		 */
		
		if( content.getMediaCount() > 0 ){
			stream.setSize(10);
			content.setMediaes(mediaService.getMedies(content.getId(), stream) );
		}	
		
		/*
		 * 검색 중인 경우엔 하이라이트기능
		 */
		if( queries != null ){
			content.fixSearchedText(queries);
		}
		
		if( content.getType() == Content.TYPE_APPROVAL ){
			content.setApproval( approvalService.findById( content.getId() ));
		}
		
		
		if( user == null )
			return;
		/*
		 * 로그인 되어있는 사람들만 따로 체크부분
		 */
//		for( Reply reply : replys ){
//			Feel feel = feelService.getFeelFromContentUser(user, reply);
//			if( feel != null ){
//				reply.setFeeledId(feel.getId());
//			}
//		}
		
		Feel feel = feelService.getFeelFromContentUser(user, new Story(content.getId()));
		if( feel != null )
			content.setFeeledId(feel.getId());
		
		if( content.getType() == Content.TYPE_APPROVAL ){
			content.setSmallGroupApproval( smallGroupApprovalService.find( user.getId(), content.getId(), null ));
			
			if( content.getApproval() != null && content.getApproval().getApprobatorCount() > 0 )
				content.setApprobators( smallGroupApprovalService.getUsers(content.getId(), null, SmallGroupApproval.TYPE_APPROBATOR));
			
			if( content.getApproval() != null && content.getApproval().getCooperationCount() > 0 )
				content.setCooperations( smallGroupApprovalService.getUsers(content.getId(), null, SmallGroupApproval.TYPE_COOPERATION) );
		}
	}
	
	
	@Override
	public void operationAfterContent( Sunny sunny, Content content, String[] queries, User user ){
		Stream stream = new Stream(null, null, null, 3);
		List<Reply> replys = replyService.getReplyList(user, content.getId(), stream);

		Collections.reverse(replys);
		content.setReplys(replys);

		
		
		/*
		 * 공유된 스토리의 경우엔 스트림에서 보여지는 사진을 공유한 스토리에 임시로 넣어놓는다.
		 */
		
		if( content.getMediaCount() > 0 ){
			stream.setSize(4);
			content.setMediaes(mediaService.getMedies(content.getId(), stream) );
		}	
		
		/*
		 * 검색 중인 경우엔 하이라이트기능
		 */
		if( queries != null ){
			content.fixSearchedText(queries);
		}
		
		
		if( user == null )
			return;
		/*
		 * 로그인 되어있는 사람들만 따로 체크부분
		 */
//		for( Reply reply : replys ){
//			Feel feel = feelService.getFeelFromContentUser(user, reply);
//			if( feel != null ){
//				reply.setFeeledId(feel.getId());
//			}
//		}
		
		Feel feel = feelService.getFeelFromContentUser(user, new Story(content.getId()));
		if( feel != null )
			content.setFeeledId(feel.getId());
		
	}
	
	
}
