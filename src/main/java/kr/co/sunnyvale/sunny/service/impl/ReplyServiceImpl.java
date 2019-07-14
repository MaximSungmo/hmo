package kr.co.sunnyvale.sunny.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import kr.co.sunnyvale.sunny.domain.Content;
import kr.co.sunnyvale.sunny.domain.Feel;
import kr.co.sunnyvale.sunny.domain.Reply;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.domain.post.ReplyPost;
import kr.co.sunnyvale.sunny.repository.hibernate.ContentService;
import kr.co.sunnyvale.sunny.repository.hibernate.ReplyRepository;
import kr.co.sunnyvale.sunny.service.AfterService;
import kr.co.sunnyvale.sunny.service.FeelService;
import kr.co.sunnyvale.sunny.service.MediaService;
import kr.co.sunnyvale.sunny.service.NotificationService;
import kr.co.sunnyvale.sunny.service.ReplyService;
import kr.co.sunnyvale.sunny.service.StoryService;
import kr.co.sunnyvale.sunny.service.UserService;
import kr.co.sunnyvale.sunny.util.HtmlUtil;
import kr.co.sunnyvale.sunny.util.ParsedText;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( value="replyService" )
@Transactional
public class ReplyServiceImpl implements ReplyService {

	@Autowired
	private NotificationService notificationService; 
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ReplyRepository replyRepository;
	
	@Autowired
	private StoryService storyService;
	
	@Autowired
	private ContentService contentService;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private FeelService feelService;
	
	@Autowired
	private OperationService operationService;
	
	@Autowired
	private ContentDynamicService contentDynamicService;
	
	@Autowired
	private AfterService afterService; 
	
	@Autowired
	private MediaService mediaService;
	
	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.ReplyService#save(kr.co.sunnyvale.sunny.domain.Reply)
	 */
	@Override
	@Transactional
	public void save(Sunny sunny, Reply reply) {
		replyRepository.save(reply);
		
		contentDynamicService.createDefault(reply);
		
		operationService.addReplyCount(reply.getContent().getId());
		
		afterService.reply( sunny, reply );

	}
	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.ReplyService#select(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public Reply select(Long replyId) {
		return replyRepository.select(replyId);
	}

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.ReplyService#getReplyList(java.lang.Long, kr.co.sunnyvale.sunny.domain.extend.Stream)
	 */
	
	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.ReplyService#getReplyList(kr.co.sunnyvale.sunny.domain.User, java.lang.Long, kr.co.sunnyvale.sunny.domain.extend.Stream)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Reply> getReplyList(User user, Long contentId, Stream stream) {
		List<Reply> replys = replyRepository.getListByContent(contentId, stream);
		
		if( user == null )
			return replys;
		
		for( Reply reply : replys ){
			Feel feel = feelService.getFeelFromContentUser(user, reply);
			if( feel != null ){
				reply.setFeeledId(feel.getId());
			}
		}
		return replys;
	}
	
	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.ReplyService#getCreateDate(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public Serializable getCreateDate(Long replyId) {
		return replyRepository.getCreateDate(replyId);
	}
	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.ReplyService#delete(kr.co.sunnyvale.sunny.domain.Reply)
	 */
	@Override
	@Transactional
	public void delete(Reply reply) {
		storyService.minusReplyCount(reply.getContent().getId());
		replyRepository.delete(reply);
	}
	
	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.ReplyService#getRecentReceiversReplys(kr.co.sunnyvale.sunny.domain.Content, kr.co.sunnyvale.sunny.domain.Reply, int)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Reply> getRecentReceiversReplys(Content content, Reply reply, 
			int receiverCount) {
		return replyRepository.getRecentReceiversReplys(content, reply, receiverCount);
	}
	
	@Override
	@Transactional
	public Reply save(Sunny sunny, ReplyPost replyPost) {
		Reply reply = new Reply(sunny.getSite());
		Content content = contentService.getContent( replyPost.getContentId() );
		reply.setUser( replyPost.getUser() );
		reply.setContent( content );
		reply.setIpAddress( replyPost.getIpAddress() );
		reply.setParentType( content.getType() );
		
		reply.setRawText( replyPost.getText() );
		
		ParsedText parsedText = HtmlUtil.parseRawText(replyPost.getText(), reply.getType());
		reply.setTaggedTextPrev(parsedText.getTaggedTextPrev());
		reply.setTaggedTextNext(parsedText.getTaggedTextNext());
		reply.setReturnCount(parsedText.getReturnCount());
		
		reply.setRequestBody( replyPost.getRequestBody());
		

		
		save(sunny, reply);
		
		Long mediaId = replyPost.getMediaId();
		if( mediaId != null ){
			reply = mediaService.setMediaToReply(reply.getId(), mediaId);
		}
		
		if( parsedText != null ){
			if( parsedText.getMentionReceivers() != null && parsedText.getMentionReceivers().size() > 0 ){
				Set<Long> userIds = parsedText.getMentionReceivers();
				for( Long userId : userIds ){
					notificationService.mentionReply(sunny, userId, reply);
				}	
			}
		}
		
		return reply;
	}
	
}
