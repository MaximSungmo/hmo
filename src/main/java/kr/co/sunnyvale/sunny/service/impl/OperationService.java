package kr.co.sunnyvale.sunny.service.impl;

import kr.co.sunnyvale.sunny.domain.Content;
import kr.co.sunnyvale.sunny.domain.ContentDynamic;
import kr.co.sunnyvale.sunny.domain.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( value="operationService" )
@Transactional
public class OperationService {
	
	/*
	 * 평판을 조절하는데 사용하는 기준점들
	 */
	public static final int DEFAULT_DIVIDE_COUNT=1;
	public static final int DEFAULT_DIVIDE_SCORE=1;
	public static final int DEFAULT_VIEW_COUNT=1;
	public static final int DEFAULT_DIVIDE_ANSWER_COUNT=1;
	public static final int DEFAULT_DIVIDE_COMMENT_COUNT=1000000;
	
	@Autowired
	private ContentDynamicService contentDynamicService;
	
	@Autowired
	private ContentReadCountService contentViewCountService;
	
	@Autowired
	private ContentReadUserService contentViewUserService;
	
	@Autowired
	private MessageSource messageSource;

	@Transactional
	public void addViewCount(Long contentId, Long userId) {

		boolean alreadyRead = contentViewUserService.alreadyRead( contentId, userId );
		
		if( alreadyRead == true ){
			return;
		}
		
		Content content = new Content( contentId );
		Integer viewCount = contentViewCountService.addCount(content);
		
		contentViewUserService.add(content, new User( userId ));
		
		if( (viewCount % DEFAULT_DIVIDE_COUNT) == 0 ){
			contentDynamicService.increaseReputation( contentId );
		}
		
	}

	
	@Transactional
	public void addViewCount(Content content, User user) {

		
		addViewCount( content.getId(), user.getId() );

	
		
	}

	@Transactional
	public void changeScore(Long id, Integer score, int count) {
		ContentDynamic dynamic = contentDynamicService.findOrCreateByContentId(id);
		int beforeFeelScore = dynamic.getFeelScore();
		int beforeFeelCount = dynamic.getFeelCount();
		int changedScore = beforeFeelScore + score;
		int changedCount = beforeFeelCount + count; 
		
		dynamic.setFeelScore( changedScore );
		dynamic.setFeelCount( changedCount );

		/*
		 * DEVIDE_SCORE 가 10 일 경우
		 * 만약 19 -> 20 으로 스코어가 바뀌면 평판을 1 올려주고
		 * 10 -> 9 로 바뀔 경우 1 내려준다.
		 */
		int beforeFrontDevideScore = 0, afterFrontDevideScore = 0;
		beforeFrontDevideScore = beforeFeelScore / DEFAULT_DIVIDE_SCORE;
		afterFrontDevideScore = changedScore / DEFAULT_DIVIDE_SCORE;
		
		if( beforeFrontDevideScore < afterFrontDevideScore ){
			dynamic.setReputation( dynamic.getReputation() + 1 );
		}else if( beforeFrontDevideScore > afterFrontDevideScore ){
			dynamic.setReputation( dynamic.getReputation() - 1 );
		}
	
		/*
		 * DEVIDE_COUNT 도 동일
		 */
		
		int beforeFrontDevideCount = 0, afterFrontDevideCount = 0;
		beforeFrontDevideCount = beforeFeelCount / DEFAULT_DIVIDE_COUNT;
		afterFrontDevideCount = changedCount / DEFAULT_DIVIDE_COUNT;
		
		if( beforeFrontDevideCount < afterFrontDevideCount ){
			dynamic.setReputation( dynamic.getReputation() + 1 );
		}else if( beforeFrontDevideCount > afterFrontDevideCount ){
			dynamic.setReputation( dynamic.getReputation() - 1 );
		}
		contentDynamicService.update(dynamic);
	}

	@Transactional
	public void addAnswerCount(Long questionId) {

		ContentDynamic dynamic = contentDynamicService.findOrCreateByContentId(questionId);
		
		int changedCount = dynamic.getAnswerCount() + 1;
		dynamic.setAnswerCount( changedCount );
		
		if( (changedCount % DEFAULT_DIVIDE_ANSWER_COUNT) == 0 ){
			dynamic.setReputation( dynamic.getReputation() + 1 );
		}
		
	}
	@Transactional
	public void minusAnswerCount(Long questionId) {

		ContentDynamic dynamic = contentDynamicService.findByContentId(questionId);
		
		int changedCount = dynamic.getAnswerCount() - 1;
		dynamic.setAnswerCount( changedCount );
		// 만약 default 가 10이면 원래 10개되면 reputation 이 올라가는데 9개로 줄게되면 reputation 이 줄어야하니까 이렇게 해줌
		if( (changedCount % DEFAULT_DIVIDE_ANSWER_COUNT) == 1 ){
			dynamic.setReputation( dynamic.getReputation() - 1 );
		}
	}

	@Transactional
	public void addReplyCount(Long id) {
		ContentDynamic dynamic = contentDynamicService.findOrCreateByContentId(id);
		
		int changedCount = dynamic.getReplyCount() + 1;
		dynamic.setReplyCount( changedCount );
		
		if( (changedCount % DEFAULT_DIVIDE_COMMENT_COUNT) == 0 ){
			dynamic.setReputation( dynamic.getReputation() + 1 );
		}
		contentDynamicService.update(dynamic);
	}

	public void minusReplyCount(Long id) {
		ContentDynamic dynamic = contentDynamicService.findByContentId( id );
		
		int changedCount = dynamic.getReplyCount() - 1;
		dynamic.setReplyCount( changedCount );
		
		if( (changedCount % DEFAULT_DIVIDE_COMMENT_COUNT) == 1 ){
			dynamic.setReputation( dynamic.getReputation() - 1 );
		}
		
	}

	
}
