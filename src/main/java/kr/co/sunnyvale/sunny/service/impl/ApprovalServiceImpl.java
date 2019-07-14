package kr.co.sunnyvale.sunny.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import kr.co.sunnyvale.sunny.domain.Approval;
import kr.co.sunnyvale.sunny.domain.Content;
import kr.co.sunnyvale.sunny.domain.Draft;
import kr.co.sunnyvale.sunny.domain.DraftSmallGroupApproval;
import kr.co.sunnyvale.sunny.domain.Feel;
import kr.co.sunnyvale.sunny.domain.Media;
import kr.co.sunnyvale.sunny.domain.Reply;
import kr.co.sunnyvale.sunny.domain.Site;
import kr.co.sunnyvale.sunny.domain.SmallGroup;
import kr.co.sunnyvale.sunny.domain.SmallGroupApproval;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.repository.hibernate.ApprovalRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.ContentRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.ContentService;
import kr.co.sunnyvale.sunny.repository.hibernate.DraftSmallGroupApprovalRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.MediaRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.SmallGroupApprovalRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.SmallGroupContentAccessRepository;
import kr.co.sunnyvale.sunny.service.AfterService;
import kr.co.sunnyvale.sunny.service.ApprovalService;
import kr.co.sunnyvale.sunny.service.FeelService;
import kr.co.sunnyvale.sunny.service.MediaService;
import kr.co.sunnyvale.sunny.service.ReplyService;
import kr.co.sunnyvale.sunny.service.SiteService;
import kr.co.sunnyvale.sunny.service.SmallGroupApprovalService;
import kr.co.sunnyvale.sunny.service.SmallGroupService;
import kr.co.sunnyvale.sunny.service.TagService;
import kr.co.sunnyvale.sunny.service.UserService;
import kr.co.sunnyvale.sunny.util.HtmlUtil;
import kr.co.sunnyvale.sunny.util.ParsedText;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( value="approvalService" )
@Transactional
public class ApprovalServiceImpl implements ApprovalService {

	@Autowired
	private ApprovalRepository approvalRepository;

	
	@Autowired
	private ContentReadUserService contentViewUserService;
	
	@Autowired
	private ContentReadCountService contentViewCountService;
	
	@Autowired
	private DraftSmallGroupApprovalRepository draftSmallGroupApprovalRepository;
	
	@Autowired
	private SmallGroupApprovalService smallGroupApprovalService;
	
	@Autowired
	private SmallGroupApprovalRepository smallGroupApprovalRepository;

	@Autowired
	private SmallGroupContentAccessRepository smallGroupContentAccessRepository;
	
	@Autowired
	private UserService userService; 
	
	@Autowired
	private MediaService mediaService;
	
	@Autowired
	private FeelService feelService;
	
	@Autowired
	private ReplyService replyService;
	
	@Autowired
	private SiteService siteService;
	
	@Autowired
	private TagService tagService;
	
	@Autowired
	private ContentService contentService;
	
	@Autowired
	private ContentAndTagService tagRelationService;
	
	@Autowired
	private RevisionService revisionService;
	
	@Autowired
	private ContentRepository contentRepository;
	
	@Autowired
	private MediaRepository mediaRepository;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private OperationService operationService;
	
	@Autowired
	private ContentDynamicService contentDynamicService;
	
	@Autowired
	private SmallGroupService smallGroupService;
	
	@Autowired
	private AfterService afterService; 
	
	@Transactional
	private Approval createDefault(Site site, User user) {
		
		Approval approval = new Approval(site);
		approval.setUser(user);
		approvalRepository.save(approval);
		
		contentDynamicService.createDefault(approval);
		contentViewCountService.findOrCreateByContentId( approval.getId() );
		
		return approval;
	}
	
	@Override
	public Content createFromDraft(Sunny sunny, Draft draft, String ipAddress) {
		
		Approval persistentApproval = createDefault(sunny.getSite(), draft.getUser());
		
		persistentApproval.setRawText( draft.getRawText() );
		
		ParsedText parsedText = HtmlUtil.parseRawText(draft.getRawText(), persistentApproval.getType());
		persistentApproval.setTaggedTextPrev(parsedText.getTaggedTextPrev());
		persistentApproval.setTaggedTextNext(parsedText.getTaggedTextNext());
		persistentApproval.setReturnCount(parsedText.getReturnCount());

		if( parsedText != null && parsedText.getTagTitles() != null && parsedText.getTagTitles().size() > 0 ){
			persistentApproval.setRawTagsStringFromSet(parsedText.getTagTitles());
			contentService.changeTags(persistentApproval,  parsedText.getTagTitles());
		}

		
		persistentApproval.setTitle( draft.getRawTitle() );
		persistentApproval.initLongTextTitle();
		persistentApproval.setCreateDate( new Date() );
		persistentApproval.setUpdateDate( new Date() );
		persistentApproval.setIpAddress(ipAddress);
		persistentApproval.setSmallGroup( draft.getSmallGroup() );

		persistentApproval.setRawTagsString( draft.getRawTagsString());
		//contentService.changeTags(persistentApproval, draft.getRawTagsString());
		List<Media> mediaIds = mediaService.getFromDraft(draft);	
		if( mediaIds != null && mediaIds.size() > 0 ){
			//int mediaCount = mediaRepository.updateMediaesBelongToContent(story, mediaIds);
			mediaService.appendMediaesToContent(persistentApproval.getId(), mediaIds);
			persistentApproval.setMediaCount(mediaIds.size());
		}
		
		persistentApproval.setWrapAbsolutePath(persistentApproval.getId());
		persistentApproval.setThread(persistentApproval.getId());
		
		//draftSmallGroupApprovalRepository.deleteFromDraft( draft );
		persistentApproval.setCheckOrdering( draft.isCheckOrdering() );
		
		List<DraftSmallGroupApproval> draftSmallGroupApprovals = draft.getDraftSmallGroupApprovals();//draftSmallGroupApprovalRepository.findListByObject("draft", draft, null);
		
		if( draftSmallGroupApprovals != null && draftSmallGroupApprovals.size() > 0 ){
			
			if( draft.isCheckOrdering() ){
				persistentApproval.setCurrentSmallGroup(new SmallGroup(draftSmallGroupApprovals.get(0).getSmallGroupId()));
			}
			/*
			 * 각 승인자에 대한 카운트를 넣어주는 부분
			 */
			for( DraftSmallGroupApproval draftApproval : draftSmallGroupApprovals ){
				if( draftApproval.getType() == SmallGroupApproval.TYPE_APPROBATOR ){
					persistentApproval.setApprobatorCount( persistentApproval.getApprobatorCount() + 1);
				}else if( draftApproval.getType() == SmallGroupApproval.TYPE_COOPERATION ){
					persistentApproval.setCooperationCount( persistentApproval.getCooperationCount() + 1);
				}else if( draftApproval.getType() == SmallGroupApproval.TYPE_RECEIVER ){
					persistentApproval.setReceiverCount( persistentApproval.getReceiverCount() + 1);
				}else if( draftApproval.getType() == SmallGroupApproval.TYPE_CIRCULATION ){
					persistentApproval.setCirculationCount( persistentApproval.getCirculationCount() + 1);
				}
			}
		}
		
		approvalRepository.update( persistentApproval );
		
		User user = draft.getUser();
		user = userService.findById(user.getId());

		//SiteUser siteUser = siteUserService.findBySiteAndUser(sunny.getSite().getId(), user.getId());
		
		//smallGroupContentAccessRepository.save( persistentApproval, user.getMySmallGroup(), true, true, true);
		smallGroupContentAccessRepository.save( persistentApproval, user.getMySmallGroup());
		
		if( draftSmallGroupApprovals != null && draftSmallGroupApprovals.size() > 0 ){
			
			/*
			 * 승인자들을 돌면서 하나하나 저장해준다. 
			 */
			for( DraftSmallGroupApproval draftApproval : draftSmallGroupApprovals ){
				SmallGroupApproval smallGroupApproval = new SmallGroupApproval();
				smallGroupApproval.setOrdering(draftApproval.getOrdering());
				smallGroupApproval.setSmallGroup(new SmallGroup(draftApproval.getSmallGroupId()));
				smallGroupApproval.setType(draftApproval.getType());
				smallGroupApproval.setApproval(persistentApproval);
				
				
				/*
				 * 결재라인이 활성화 되었을 시, 승인자가 아니면 그냥 넣어준다.
				 */
				if( draftApproval.getType() != SmallGroupApproval.TYPE_APPROBATOR  ){

					smallGroupApproval.setStatus( SmallGroupApproval.STATUS_BEFORE_SHOW );
					smallGroupApprovalRepository.save(smallGroupApproval);
//					smallGroupContentAccessRepository.save( persistentApproval, smallGroupApproval.getSmallGroup(), true, false, false);
					continue;
				}
				
				/*
				 * 결재라인을 활성화 시키지 않았으면 모든 승인자를 한번에 넣어준다.
				 */
				if( draft.isCheckOrdering() == false ){
					
					smallGroupApproval.setStatus( SmallGroupApproval.STATUS_SHOW );

					smallGroupApprovalRepository.save(smallGroupApproval);
					smallGroupContentAccessRepository.save( persistentApproval, smallGroupApproval.getSmallGroup());
					afterService.requestApprove( sunny, persistentApproval, smallGroupApproval.getSmallGroup() );
					continue;
				}
				
				
				/*
				 * 결재라인이 활성화 되었을 시, 첫번째 승인자만 넣어준다. 
				 */
				if( draftApproval.getOrdering() == 0 ){

					smallGroupApproval.setStatus( SmallGroupApproval.STATUS_SHOW );
					smallGroupContentAccessRepository.save( persistentApproval, smallGroupApproval.getSmallGroup());
					afterService.requestApprove( sunny, persistentApproval, smallGroupApproval.getSmallGroup() );

				}else{
					smallGroupApproval.setStatus( SmallGroupApproval.STATUS_BEFORE_SHOW );	
				}
				smallGroupApprovalRepository.save(smallGroupApproval);
			}
		}
		
		
		persistentApproval = approvalRepository.select( persistentApproval.getId() );
		revisionService.createVersion(persistentApproval);

		// 부서에 쓴 결재면 결재 카운트를 늘려줌
		if( draft.getSmallGroup() != null ){
			smallGroupService.plusApprovalCount(draft.getSmallGroup().getId());	
		}
		
		return persistentApproval;
	}

//	@Override
//	public List<ContentDTO> fetchApprovals(Sunny sunny, Long smallGroupId, Boolean isWantChildren, User user,
//			String[] queries, Stream stream) {
//
//		List<ContentDTO> contentDTOs = contentService.fetchApprovals(sunny, smallGroupId, isWantChildren, user, queries, stream);
//		return contentDTOs;
//	}

	@Override
	@Transactional(readOnly = true)
	public Approval findById(Long id) {
		return approvalRepository.select(id);
	}

	@Override
	@Transactional
	public void ok(Sunny sunny, Long approvalId, Long smallGroupApprovalId, Long userId) {
		
		
		Approval approval = approvalRepository.select(approvalId);
		SmallGroupApproval smallGroupApproval = smallGroupApprovalRepository.select(smallGroupApprovalId);
		
		switch( smallGroupApproval.getType() ){
		case SmallGroupApproval.TYPE_APPROBATOR:
			
			smallGroupApproval.setStatus( SmallGroupApproval.STATUS_OK);
			
			approval.setApprobatorOkCount( approval.getApprobatorOkCount() + 1 );
			
			// 마지막 승인자였을 때
			if( approval.getApprobatorOkCount() >= approval.getApprobatorCount() ){
				approval.setStatus(Approval.STATUS_APPROVED);

				afterService.approveApprobatorComplete(sunny, approval);
				// 최종 승인이 되면 협조처, 수신처, 회람에게 모두 보여진다.
				changeMiscToShow( sunny, approval );
				
				break;
			}
			if( approval.isCheckOrdering() ){
				SmallGroupApproval nextSmallGroupApproval = smallGroupApprovalService.findApprobatorByOrdering( approval.getId(), smallGroupApproval.getOrdering() + 1 );
				approval.setCurrentOrdering( smallGroupApproval.getOrdering() + 1 );
				approval.setCurrentSmallGroup( nextSmallGroupApproval.getSmallGroup() );
				approval.setCurrentSmallGroupApproval(nextSmallGroupApproval);
				nextSmallGroupApproval.setStatus(SmallGroupApproval.STATUS_SHOW);
				smallGroupApprovalService.update(nextSmallGroupApproval);
				smallGroupContentAccessRepository.save( approval, nextSmallGroupApproval.getSmallGroup());
				afterService.requestApprove( sunny,  approval, nextSmallGroupApproval.getSmallGroup() );
			}
			
			
			break;
			
		case SmallGroupApproval.TYPE_COOPERATION:
			smallGroupApproval.setStatus( SmallGroupApproval.STATUS_OK);
			
			approval.setCooperationOkCount( approval.getCooperationOkCount() + 1 );
			
			if( approval.getCooperationOkCount() >= approval.getCooperationCount() ){
				// 협조완료 상태를 넣어야하나?
				//approval.setStatus(Approval.STATUS_APPROVED);

				afterService.approveCooperationComplete(sunny, approval);
			}
			
			break;
			
		case SmallGroupApproval.TYPE_RECEIVER:

			smallGroupApproval.setStatus( SmallGroupApproval.STATUS_OK);
			approval.setReceiverOkCount( approval.getReceiverOkCount() + 1 );
			break;
		}
		smallGroupApproval.setCompleteDate(new Date());
		approvalRepository.update(approval);
		smallGroupApprovalRepository.update(smallGroupApproval);
			
	}

	@Transactional
	private void changeMiscToShow(Sunny sunny, Approval approval) {
		List<SmallGroupApproval> smallGroupApprovals = smallGroupApprovalService.getMiscUsers( approval.getId(), new Stream(1000) );
		for( SmallGroupApproval smallGroupApproval : smallGroupApprovals ){
			smallGroupApproval.setStatus(SmallGroupApproval.STATUS_SHOW);
			smallGroupApprovalService.update(smallGroupApproval);
			smallGroupContentAccessRepository.save( approval, smallGroupApproval.getSmallGroup());
			afterService.requestApprove( sunny, approval, smallGroupApproval.getSmallGroup() );
		}
		
	}

	@Override
	public void reject(Sunny sunny, Long approvalId, Long smallGroupApprovalId, Long userId) {
		
		Approval approval = approvalRepository.select(approvalId);
		SmallGroupApproval smallGroupApproval = smallGroupApprovalRepository.select(smallGroupApprovalId);
		
		switch( smallGroupApproval.getType() ){
		case SmallGroupApproval.TYPE_APPROBATOR:
			
			smallGroupApproval.setStatus( SmallGroupApproval.STATUS_REJECT);
			approval.setStatus( Approval.STATUS_REJECT );
			approval.setApprobatorRejectCount(approval.getApprobatorRejectCount() + 1);
			approvalRepository.update(approval);
			break;
			
		}
		smallGroupApproval.setCompleteDate(new Date());
		smallGroupApprovalRepository.update(smallGroupApproval);
		afterService.approveReject(sunny, userId, approval);
	}

	@Override
	@Transactional
	public void requestReject(Long approvalId, Long userId) {

		Approval approval = approvalRepository.select(approvalId);
		approval.setInRequestReject(true);
	}

	@Override
	@Transactional
	public void cancelRequestReject(Long approvalId, Long userId) {
		Approval approval = approvalRepository.select(approvalId);
		approval.setInRequestReject(false);
	}

	@Override
	@Transactional(readOnly = true)
	public Approval findForView(Long id, User user) {
		
		Approval approval = approvalRepository.select(id);
		
		List<Reply> replys = replyService.getReplyList(user, id, null);
		Collections.reverse(replys);
		approval.setReplys(replys);
		
		List<SmallGroupApproval> smallGroupApproval = smallGroupApprovalService.getUsers(id, null, null);
		
		approval.setSmallGroupApprovals(  smallGroupApproval );
		
		if( user == null )
			return approval;
		
		for( Reply reply : replys ){
			Feel feel = feelService.getFeelFromContentUser(user, reply);
			if( feel != null ){
				reply.setFeeledId(feel.getId());
			}
		}
		
		
		Feel feel = feelService.getFeelFromContentUser( user , approval);
		if( feel != null )
			approval.setFeeledId(feel.getId());
		
		return approval	;
		
	}

	@Override
	@Transactional(readOnly = true)
	public Approval getSequence(Sunny sunny, User authUser, int direct,
			Approval approval, String[] queries, Long smallGroupId,
			Long userId, String menu, String tagTitle) {
		
		return approvalRepository.getSequence(sunny, authUser, direct, approval, queries, smallGroupId, userId, menu, tagTitle );
		
	}

}
