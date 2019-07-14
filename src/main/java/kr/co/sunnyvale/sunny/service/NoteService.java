package kr.co.sunnyvale.sunny.service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import kr.co.sunnyvale.sunny.domain.Content;
import kr.co.sunnyvale.sunny.domain.Draft;
import kr.co.sunnyvale.sunny.domain.Feel;
import kr.co.sunnyvale.sunny.domain.Media;
import kr.co.sunnyvale.sunny.domain.Note;
import kr.co.sunnyvale.sunny.domain.Reply;
import kr.co.sunnyvale.sunny.domain.Site;
import kr.co.sunnyvale.sunny.domain.SmallGroup;
import kr.co.sunnyvale.sunny.domain.Tag;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.repository.hibernate.ContentRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.ContentService;
import kr.co.sunnyvale.sunny.repository.hibernate.MediaRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.NoteRepository;
import kr.co.sunnyvale.sunny.service.impl.ContentDynamicService;
import kr.co.sunnyvale.sunny.service.impl.ContentReadCountService;
import kr.co.sunnyvale.sunny.service.impl.ContentReadUserService;
import kr.co.sunnyvale.sunny.service.impl.OperationService;
import kr.co.sunnyvale.sunny.service.impl.RevisionService;
import kr.co.sunnyvale.sunny.service.impl.ContentAndTagService;
import kr.co.sunnyvale.sunny.util.HtmlUtil;
import kr.co.sunnyvale.sunny.util.ParsedText;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( value="noteService" )
@Transactional
public class NoteService {
	
	@Autowired
	private NoteRepository noteRepository;
	
	@Autowired
	private ContentReadUserService contentViewUserService;
	
	@Autowired
	private ContentReadCountService contentReadCountService;
	
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
	
	
	@Transactional
	public void delete(Long id) {
		Note note= noteRepository.select(id);
		noteRepository.delete( note );	
		
	}
	

	@Transactional
	public void updateDraftToPost(Long contentId) {
		Note persistentNote = noteRepository.select(contentId);
		contentReadCountService.findOrCreateByContentId(persistentNote.getId());
//		persistentNote.setModifying( false );
		persistentNote.initLongTextTitle();
		persistentNote.setCreateDate( new Date() );
		persistentNote.setUpdateDate( new Date() );

		noteRepository.update( persistentNote);

		revisionService.createVersion(persistentNote);
	}


	@Transactional(readOnly = true)
	public Note select(Long id, User user ) {
		
		Note note = noteRepository.select(id);
		
		List<Reply> replys = replyService.getReplyList(user, id, null);
		Collections.reverse(replys);
		note.setReplys(replys);
		
		
		if( user == null )
			return note;
		
		for( Reply reply : replys ){
			Feel feel = feelService.getFeelFromContentUser(user, reply);
			if( feel != null ){
				reply.setFeeledId(feel.getId());
			}
		}
		
		
		Feel feel = feelService.getFeelFromContentUser( user , note);
		if( feel != null )
			note.setFeeledId(feel.getId());
		
		return note	;
		
	}

	@Transactional
	private Note createDefault(Site site, User user) {
		
		Note note = new Note(site);
		note.setUser(user);
		noteRepository.save(note);
		
		contentDynamicService.createDefault(note);
		contentReadCountService.findOrCreateByContentId( note.getId() );
		
		return note;
	}


	@Transactional
	public Page<Note> getPagedPostingNotes(Sunny sunny, User authUser, String queries, Long smallGroupId, String userId, String tabTitle, String tagTitle, Integer pageNum, int pageSize) {
		return noteRepository.getPagedPostingNotes(sunny, authUser, tabTitle, tagTitle, queries, smallGroupId, userId, pageNum, pageSize);
	}

	
	@Transactional( readOnly = true )
	public Page<Note> getPagedList(User user, Integer categoryId, Integer pageNum, int pageSize) {
		return noteRepository.getPagedList( user, categoryId, pageNum, pageSize );
	}
	
	@Transactional(readOnly = true )
	public List<Note> getTaggedList(SmallGroup smallGroup, String tagTitle, Stream stream) {
		
		Tag tag = tagService.findByTitle(smallGroup, Content.TYPE_NONE, tagTitle);
		if( tag == null )
			return null; 
		
		return noteRepository.getTaggedList( smallGroup, tag, stream );
	}

	@Transactional
	public Content createFromDraft(Site site, Draft draft, String ipAddress) throws Exception {

		Note persistentNote = createDefault(site, draft.getUser());
		
		persistentNote.setRawText( draft.getRawText() );
		
		ParsedText parsedText = HtmlUtil.parseRawText(draft.getRawText(), persistentNote.getType());
		persistentNote.setTaggedTextPrev(parsedText.getTaggedTextPrev());
		persistentNote.setTaggedTextNext(parsedText.getTaggedTextNext());
		persistentNote.setReturnCount(parsedText.getReturnCount());
		
		persistentNote.setTitle( draft.getRawTitle() );
		persistentNote.initLongTextTitle();
		persistentNote.setCreateDate( new Date() );
		persistentNote.setUpdateDate( new Date() );
		persistentNote.setIpAddress(ipAddress);
		persistentNote.setSmallGroup( draft.getSmallGroup() );

		if( parsedText != null && parsedText.getTagTitles() != null && parsedText.getTagTitles().size() > 0 ){
			persistentNote.setRawTagsStringFromSet(parsedText.getTagTitles());
			contentService.changeTags(persistentNote,  parsedText.getTagTitles());
		}
		
		persistentNote.setRawTagsString( draft.getRawTagsString());
		//contentService.changeTags(persistentNote, draft.getRawTagsString());
		List<Media> mediaIds = mediaService.getFromDraft(draft);	
		if( mediaIds != null && mediaIds.size() > 0 ){
			//int mediaCount = mediaRepository.updateMediaesBelongToContent(story, mediaIds);
			mediaService.appendMediaesToContent(persistentNote.getId(), mediaIds);
			persistentNote.setMediaCount(mediaIds.size());
		}
		
		noteRepository.update( persistentNote );
		persistentNote = noteRepository.select( persistentNote.getId() );
		revisionService.createVersion(persistentNote);

		smallGroupService.plusNoteCount(draft.getSmallGroup().getId());
		return persistentNote;
		
	}


	@Transactional( readOnly = true )
	public Note getSequenceNote(Sunny sunny, User authUser, int direct, Note note, String queries,
			Long smallGroupId, String userId, String tabTitle, String tagTitle, Long id) {
		return noteRepository.getSequenceNote(sunny, authUser, direct, note, tabTitle, tagTitle, queries, smallGroupId, userId);
	}



}
