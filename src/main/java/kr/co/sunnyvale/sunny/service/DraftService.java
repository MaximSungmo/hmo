package kr.co.sunnyvale.sunny.service;

import java.util.Date;
import java.util.List;

import kr.co.sunnyvale.sunny.domain.Content;
import kr.co.sunnyvale.sunny.domain.Draft;
import kr.co.sunnyvale.sunny.domain.DraftSmallGroupApproval;
import kr.co.sunnyvale.sunny.domain.Media;
import kr.co.sunnyvale.sunny.domain.Revision;
import kr.co.sunnyvale.sunny.domain.Site;
import kr.co.sunnyvale.sunny.domain.SmallGroup;
import kr.co.sunnyvale.sunny.domain.SmallGroupApproval;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.repository.hibernate.ChildContentRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.ContentService;
import kr.co.sunnyvale.sunny.repository.hibernate.DraftRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.DraftSmallGroupApprovalRepository;
import kr.co.sunnyvale.sunny.service.impl.RevisionService;
import kr.co.sunnyvale.sunny.util.HtmlUtil;
import kr.co.sunnyvale.sunny.util.ParsedText;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( value="draftService" )
@Transactional
public class DraftService {

	@Autowired
	private DraftRepository draftRepository;
	
	@Autowired
	private SmallGroupApprovalService smallGroupApprovalService;
	
	@Autowired
	private DraftSmallGroupApprovalRepository draftSmallGroupApprovalRepository;
	
	@Autowired
	private ChildContentRepository contentRepository;
	
	@Autowired
	private ContentService contentService;
	
	@Autowired
	private NoteService noteService;
	
	@Autowired
	private ApprovalService approvalService; 
	
//	@Autowired
//	private QnaService qnaService;
//	
	@Autowired
	private MediaService mediaService;
	
	@Autowired
	private RevisionService revisionService;

	@Autowired
	private SiteService siteService;
	
	
	@Transactional
	public Draft createDefault(Site site, int type, Long pushContentId, Long targetContentId, Long userId, Long smallGroupId) {
		Draft draft = new Draft(site, type);
		draft.setRawText("");
		draft.setRawTitle("");
		draft.setUser( new User( userId ) );
		if( smallGroupId != null )
			draft.setSmallGroup( new SmallGroup( smallGroupId ) );
//		else{
//			Site site = siteService.getSiteFromDomain(RequestUtils.getCurrentServerName());
//			draft.setSmallGroup( site.getDefaultSmallGroup() );
//		}
		if( targetContentId != null ){
			draft.setTargetContent( new Content(targetContentId) );
		}
		if( pushContentId != null ){
			Content persistentContent = contentService.getContent( pushContentId );
			draft.setRawTitle( persistentContent.getTitle() );
			draft.setRawText( persistentContent.getRawText() );
			draft.setRawTagsString( persistentContent.getRawTagsString() );
			draft.setPushContent( persistentContent );
		}
		draftRepository.save(draft);
		
		return draft;
	}

	@Transactional(readOnly = true)
	public boolean isSameUser(Long id, Long userId) {
		
		Long draftUserId = (Long) draftRepository.findColumnByObject("user.id", "id", id);
		if( userId.equals(draftUserId) )
			return true;
		
		return false; 
	}

	@Transactional
	public void delete(Long id) {
		Draft draft = draftRepository.select(id);
		draftRepository.delete( draft );
	}

	@Transactional(readOnly = true)
	public Draft select(Long id) {
		return draftRepository.select(id);
	}

	@Transactional
	public void update(Sunny sunny, Draft draft) {
		
		Draft persistentDraft = null;
		
		if( draft.getId() == null ){
			Site site = sunny.getSite();
			persistentDraft = createDefault(site, draft);
		}else{
			persistentDraft = draftRepository.select(draft.getId());
		}

		if( draft.getRawTitle() != null && !draft.getRawTitle().equals(persistentDraft.getRawTitle())){
			persistentDraft.setRawTitle( draft.getRawTitle() );
		}
		
		if( draft.getRawText() != null && !draft.getRawText().equals(persistentDraft.getRawText())){
			persistentDraft.setRawText( draft.getRawText() );
		}
		
		if( draft.getRawTagsString() != null && !draft.getRawTagsString().equals( persistentDraft.getRawTagsString() )){
			persistentDraft.setRawTagsString( draft.getRawTagsString() );
		}
		if( draft.getMediaCount() != persistentDraft.getMediaCount() ){
			persistentDraft.setMediaCount( draft.getMediaCount() );
		}

		
		persistentDraft.setUpdateDate( new Date() );
		persistentDraft.setCheckOrdering( draft.isCheckOrdering() );
		if( draft.getDraftSmallGroupApprovals() != null && draft.getDraftSmallGroupApprovals().size() > 0 ){
			persistentDraft.setApprobatorCount(0);
			persistentDraft.setCooperationCount(0);
			persistentDraft.setReceiverCount(0);
			persistentDraft.setCirculationCount(0);
			for( DraftSmallGroupApproval approval : draft.getDraftSmallGroupApprovals()  ){
				switch( approval.getType() ){
				case SmallGroupApproval.TYPE_APPROBATOR:
					persistentDraft.setApprobatorCount( persistentDraft.getApprobatorCount() + 1 );
					break;
				case SmallGroupApproval.TYPE_COOPERATION:
					persistentDraft.setCooperationCount( persistentDraft.getCooperationCount() + 1 );
					break;
				case SmallGroupApproval.TYPE_RECEIVER:
					persistentDraft.setReceiverCount( persistentDraft.getReceiverCount() + 1 );
					break;
				case SmallGroupApproval.TYPE_CIRCULATION:
					persistentDraft.setCirculationCount( persistentDraft.getCirculationCount() + 1 );
					break;
				}
			}
		}
		
		draftRepository.update( persistentDraft );
		
		if( persistentDraft.getType() == Content.TYPE_APPROVAL ){
			
			List<DraftSmallGroupApproval> smallGroupApprovals = draft.getDraftSmallGroupApprovals();
//			if( smallGroupApprovals != null && smallGroupApprovals.size() > 0 ){
//				
//				for( SmallGroupApproval approval : smallGroupApprovals ){
//					smallGroupApprovalService.save(approval);
//				}
//			}
			draftSmallGroupApprovalRepository.deleteFromDraft( persistentDraft );
			if( smallGroupApprovals != null && smallGroupApprovals.size() > 0 ){
				
				for( DraftSmallGroupApproval approval : smallGroupApprovals ){
					draftSmallGroupApprovalRepository.save(approval);
				}
			}
			
		}
	}

	@Transactional
	private Draft createDefault(Site site, Draft draft) {
		Long pushContentId = null;
		Long targetContentId = null;
		int type = 0;
		if( draft.getPushContent() != null ){
			
			Draft persistentDraft = draftRepository.selectFromPush(draft.getPushContent().getId(), draft.getUser());
			if( persistentDraft != null ){
				return persistentDraft ;
			}
			Content persistentPushContent = contentRepository.select( draft.getPushContent().getId() );
			pushContentId = persistentPushContent.getId();
			
			if( persistentPushContent.getType() == Content.TYPE_QUESTION ){
				type = Draft.TYPE_QUESTION_MODIFYING;
			}else if( persistentPushContent.getType() == Content.TYPE_ANSWER ){
				type = Draft.TYPE_ANSWER_MODIFYING;
			}else if( persistentPushContent.getType() == Content.TYPE_NOTE ){
				type = Draft.TYPE_NOTE_MODIFYING;
			}
			
		}else if( draft.getTargetContent() != null ){
			
			Draft persistentDraft = draftRepository.selectFromTarget(draft.getTargetContent().getId(), draft.getUser());
			if( persistentDraft != null ){
				return persistentDraft ;
			}
			
			Content persistentTargetContent = contentRepository.select( draft.getTargetContent().getId() );
			targetContentId = persistentTargetContent.getId();
			
			if( persistentTargetContent.getType() == Content.TYPE_QUESTION ){
				type = Content.TYPE_ANSWER;
			}else{
				throw new RuntimeException("잘못된 draft 타입입니다.");
			}
		}else {
			throw new RuntimeException("draft 를 생성할 수 있는 요구조건이 부족합니다.");
		}

		return createDefault( site, type, pushContentId, targetContentId, draft.getUser().getId(), site.getLobbySmallGroup().getId() );
		
		
	}

	@Transactional( readOnly = true )
	public Page<Draft> getPagedList(Sunny sunny, Long smallGroupId, User user, int type, Integer pageNum, int pageSize ) {
		return draftRepository.getPagedList( sunny, smallGroupId, user, type, pageNum, pageSize );
	}

	@Transactional(readOnly = true)
	public Draft selectFromTarget(Long targetId, User user) {
		return draftRepository.selectFromTarget( targetId, user );
	}
	
	@Transactional
	public Draft selectFromPush(Sunny sunny, Long pushContentId, User user) {
		Draft draft = draftRepository.selectFromPush( pushContentId, user );
		if( draft == null ){
			Content content = contentService.getContent( pushContentId );
			Site site = sunny.getSite();
			return createDefault( site, content.getType(), pushContentId, null, user.getId(), content.getSmallGroup().getId() );
		}
		return draft; 
	}

	
	@Transactional(readOnly = true)
	public List<Media> getTargetMedias(Content content, User user) {
		Draft draft = selectFromTarget( content.getId(), user );
		
		return mediaService.getFromDraft( draft );
	}
	
	@Transactional
	public void minusMediaCount(Long id) {
		Draft draft = draftRepository.select(id);
		draft.setMediaCount( draft.getMediaCount() - 1 );
		
	}
	
	@Transactional
	public void plusMediaCount(Long id) {
		Draft draft = draftRepository.select(id);
		draft.setMediaCount( draft.getMediaCount() + 1 );
	}

	@Transactional
	public Content publish(Sunny sunny, Draft draft, String ipAddress) throws Exception {
		Draft persistentDraft = null;
		System.out.println("드래프트 아이디 : " + draft.getId());
		if( draft.getId() != null ){
			persistentDraft = draftRepository.select( draft.getId() );	
			System.out.println("펄시스턴트 : " + persistentDraft.getSmallGroup().getId());
		}else if( draft.getTargetContent() != null ){
			persistentDraft = draftRepository.selectFromTarget(draft.getTargetContent().getId(), draft.getUser());
			if( persistentDraft == null ){
				persistentDraft = createDefault(sunny.getSite(), draft);
			}
		}else{
			throw new RuntimeException("Publish 할 수 없는 Draft 입니다.");
		}
		
		
		draft.setSmallGroup( new SmallGroup(persistentDraft.getSmallGroup().getId()) );
		Content content = null;
		switch( persistentDraft.getType() ){
//			case Content.TYPE_QUESTION:
//				content = qnaService.createQuestionFromDraft( site, draft,ipAddress );
//				break;
//			case Content.TYPE_ANSWER:
//				draft.setTargetContent(new Content( persistentDraft.getTargetContent().getId()));
//				content = qnaService.createAnswerFromDraft( site, draft, ipAddress );
//				break;
			case Content.TYPE_NOTE:
				content = noteService.createFromDraft(sunny.getSite(), draft, ipAddress);
				break;
			case Content.TYPE_APPROVAL:
				content = approvalService.createFromDraft(sunny, draft, ipAddress);
				break;
			default:								
				break;
		}
		
		draftRepository.delete(persistentDraft);
		return content;
	}

	/**
	 * Draft 를 해당 게시글에 반영한다.
	 * Draft -> PushContent 에 복사해서 넣는다.
	 * Draft -> new Revision 으로 해서 마지막 것에 현재 Draft 를 복사한다.
	 * 현재 Media 들은 Revision 에 위임한다.  
	 * @param draft
	 * @param ipAddress
	 */
	@Transactional
	public void push(Sunny sunny, Draft draft, String ipAddress) {
		Content persistPushContent = contentService.getContent(draft.getPushContent().getId());
		

		persistPushContent.setRawText( draft.getRawText() );
		
		ParsedText parsedText = HtmlUtil.parseRawText(draft.getRawText(), persistPushContent.getType());
		persistPushContent.setTaggedTextPrev(parsedText.getTaggedTextPrev());
		persistPushContent.setTaggedTextNext(parsedText.getTaggedTextNext());
		persistPushContent.setReturnCount(parsedText.getReturnCount());
		
		
		persistPushContent.setTitle( draft.getRawTitle() );
		persistPushContent.setRawTagsString( draft.getRawTagsString() );
	
		contentService.update(persistPushContent);
		List<Media> mediaIds = mediaService.getFromDraft(draft);	
		if( mediaIds != null && mediaIds.size() > 0 ){
			//int mediaCount = mediaRepository.updateMediaesBelongToContent(story, mediaIds);
			mediaService.appendMediaesToContent(persistPushContent.getId(), mediaIds);
			persistPushContent.setMediaCount(persistPushContent.getMediaCount() + mediaIds.size());
		}
		Revision revision = revisionService.createVersion(persistPushContent);
		
		revision.setMediaes( mediaService.getFromDraft( draft ) );
		
		revisionService.update( revision );
		
		Draft persistentDraft = select(draft.getId());
		persistentDraft.setMediaes(null);
		
		update(sunny, persistentDraft);
	}

	

}
