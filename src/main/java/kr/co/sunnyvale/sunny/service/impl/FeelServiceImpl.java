package kr.co.sunnyvale.sunny.service.impl;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.Content;
import kr.co.sunnyvale.sunny.domain.Feel;
import kr.co.sunnyvale.sunny.domain.FeelAndContent;
import kr.co.sunnyvale.sunny.domain.FeelAndContentAndUser;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.exception.AlreadyEvalutedException;
import kr.co.sunnyvale.sunny.exception.FeelFailException;
import kr.co.sunnyvale.sunny.repository.hibernate.ContentService;
import kr.co.sunnyvale.sunny.repository.hibernate.FeelAndContentAndUserRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.FeelAndContentRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.FeelRepository;
import kr.co.sunnyvale.sunny.service.AfterService;
import kr.co.sunnyvale.sunny.service.FeelService;
import kr.co.sunnyvale.sunny.service.ReplyService;
import kr.co.sunnyvale.sunny.service.StoryService;
import kr.co.sunnyvale.sunny.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.KeyGenerator;
import com.googlecode.ehcache.annotations.Property;

@Service( value="feelService" )
@Transactional
public class FeelServiceImpl implements FeelService {

	@Autowired
	private ContentService contentService;
	
	@Autowired
	private AfterService afterService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private StoryService storyService;
	
	@Autowired
	private FeelRepository feelRepository;

	@Autowired
	private FeelAndContentRepository feelContentRepository;
	
	@Autowired
	private FeelAndContentAndUserRepository feelContentUserRepository;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private ReplyService replyService;
	
	@Autowired
	private OperationService operationService;
	
	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.FeelService#getAll()
	 */
	@Override
	@Transactional(readOnly = true)
	@Cacheable( cacheName="sunnyFeelCache", keyGenerator=@KeyGenerator( name="", properties=@Property( name="includeMethod", value="false" ) ) )
	public List<Feel> getAll() {
		return feelRepository.getAll(null);
	}

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.FeelService#save(kr.co.sunnyvale.sunny.domain.Feel)
	 */
	@Override
	@Transactional
	public void save(Feel feel) {
		feelRepository.save(feel);
		
	}
	
	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.FeelService#delete(java.lang.Integer)
	 */
	@Override
	@Transactional
	public void delete(Integer id) {
		feelRepository.delete(new Feel(id));
		
	}

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.FeelService#getFeel(int)
	 */
	@Override
	@Transactional(readOnly = true)
	@Cacheable( cacheName="sunnyFeelCache", keyGenerator=@KeyGenerator( name="", properties=@Property( name="includeMethod", value="false" ) ) )
	public Feel getFeel(int id) {
		Feel feel = feelRepository.select(id);
		if( feel == null ){
			feelRepository.generateDefault();
			feel = feelRepository.select(id);
		}
		return feel;
	}
	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.FeelService#getColumnFromObject(java.lang.String, java.lang.String, java.lang.Object)
	 */
	@Override
	@Transactional(readOnly = true)
	public Object getColumnFromObject( String returnColumn, String whereColumn, Object whereParameter ) {
		return (Object) feelRepository.findColumnByObject(returnColumn, whereColumn, whereParameter);
	}
	
	
	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.FeelService#feelContent(kr.co.sunnyvale.sunny.domain.User, kr.co.sunnyvale.sunny.domain.Content, kr.co.sunnyvale.sunny.domain.Feel)
	 */
	@Override
	@Transactional
	public void feelContent(User user, Content content, Feel feel) {
		
		// 해당 유저가 이미 평가를 했으면 리턴
		Long feelContentUserId = feelContentUserRepository.getUserFeel(user, content);
		if(feelContentUserId != null ){
			throw new AlreadyEvalutedException();		
		}
		
		
		// 이미 스토리에 사용자가 클릭한 평가를 했는지 여부를 판단해 평가가 존재하면 plus 만 하고 없으면 생성한다.  
		FeelAndContentAndUser feelContentUser = new FeelAndContentAndUser(feelContentUserId);
		FeelAndContent feelContent = feelContentRepository.findUniqFeelAndContent(content, feel);
		if( feelContent == null ){
			feelContent = new FeelAndContent();
			 
			feelContent.setContent(content);
			feelContent.setFeel(feel);
			feelContentRepository.save(feelContent);
			if( content == null){
				System.out.println("컨텐츠 널");
			}
			if( feel == null ){
				System.out.println("평가 널");
			}
			operationService.changeScore(content.getId(), feel.getScore(), 1);
			feelContentUser.setFeel(feel);
			feelContentUser.setUser(user);
			feelContentUser.setContent(content);
			feelContentUserRepository.save(feelContentUser);
			
		}else{
			if( feelContentUserRepository.findUniqFeelAndContentAndUser(user, content) == null ){


				operationService.changeScore(content.getId(), feel.getScore(), 1);
				
				feelContentUser.setFeel(feel);
				feelContentUser.setUser(user);
				feelContentUser.setContent(content);
				feelContentUserRepository.save(feelContentUser);
			}
		}
	}

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.FeelService#undoFeelAndContent(kr.co.sunnyvale.sunny.domain.User, kr.co.sunnyvale.sunny.domain.Content, kr.co.sunnyvale.sunny.domain.Feel)
	 */
	@Override
	@Transactional
	public void undoFeelAndContent(User user, Content content, Feel alreadyFeel) {
		// 해당 사용자의 평가가 존재하지 않으면 아무것도 하지 않고 리턴
		Long feelContentUserId = feelContentUserRepository.getUserFeel(user, content);
		if(feelContentUserId == null ){
			throw new AlreadyEvalutedException();			
		}
		FeelAndContent feelContent = feelContentRepository.findUniqFeelAndContent(content, alreadyFeel);
		if( feelContent != null ){			

			operationService.changeScore(content.getId(),  -alreadyFeel.getScore(), -1);

			feelContentRepository.update(feelContent);
			feelContentUserRepository.delete(new FeelAndContentAndUser(feelContentUserId));			
		}
		
		afterService.unFeel( user, content, alreadyFeel );
	}

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.FeelService#getUserFeel(kr.co.sunnyvale.sunny.domain.User, kr.co.sunnyvale.sunny.domain.Content)
	 */
	@Override
	public Long getUserFeel(User user, Content content) {
		return feelContentUserRepository.getUserFeel(user, content);
	}

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.FeelService#getFeelAndContents(java.lang.Long, kr.co.sunnyvale.sunny.domain.extend.Stream)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<FeelAndContent> getFeelAndContents(Long contentId, Stream stream) {
		return feelContentRepository.getListByContent(contentId, stream);
	}

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.FeelService#getFeelFromContentUser(kr.co.sunnyvale.sunny.domain.User, kr.co.sunnyvale.sunny.domain.Content)
	 */
	@Override
	@Transactional(readOnly = true)
	public Feel getFeelFromContentUser(User user, Content content) {
		return feelContentUserRepository.getFeelFromContentUser(user, content);
	}

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.FeelService#feel(kr.co.sunnyvale.sunny.domain.extend.Sunny, java.lang.Long, java.lang.Long, int)
	 */
	@Override
	@Transactional
	public void feel(Sunny sunny, Long userId, Long contentId, int feelId) {
		try{
			User user = userService.findById(userId);
			Feel feel = getFeel(feelId);
			Content content = contentService.getContent(contentId);
			
			/*
			 * 해당 유저가 해당 콘텐츠를 이미 평가하지 않았으면 평가하고 리턴
			 */
			Feel alreadyFeel = this.getFeelFromContentUser(user, content);
			if( alreadyFeel == null ){
				this.feelContent(user, content, feel);
				// 내가 내 글에 평가한 경우엔 노티나 액티비티를 생성하지 않는다. 
				if( userId.equals(content.getUser().getId()) )
					return; 

				afterService.feel( sunny, user, content, feel );	
				
				return ;
			}
			
			this.undoFeelAndContent(user, content, alreadyFeel);
			
			// 동일한 것을 누른 경우엔 평가를 취소하고 리턴
			if( alreadyFeel.getId().equals(feelId) ){
				return;
			}
			
			this.feelContent(user, content, feel);

			if( userId.equals(content.getUser().getId()) )
				return; 
			
			afterService.feel( sunny, user, content, feel );	

		}catch(Exception ex){
			System.out.println("평가 익셉션");
			ex.printStackTrace();
			
			throw new FeelFailException();		
		}
	}

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.FeelService#getFeelUsers(java.lang.Long, kr.co.sunnyvale.sunny.domain.extend.Stream)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<FeelAndContentAndUser> getFeelUsers(Long contentId, Stream stream) {
		List<FeelAndContentAndUser> feelUser = feelContentUserRepository.getAllUsersByContent(contentId, stream);
		return feelUser;
	}

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.FeelService#getFeelUsers(java.lang.Long, java.lang.Integer, kr.co.sunnyvale.sunny.domain.extend.Stream)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<FeelAndContentAndUser> getFeelUsers(Long contentId, Integer feelId, Stream stream) {
		List<FeelAndContentAndUser> feelUser = feelContentUserRepository.getMatchUsersByContent(contentId, feelId, stream);
		return feelUser;
	}

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.FeelService#getFeelNames(java.lang.Long, kr.co.sunnyvale.sunny.domain.extend.Stream)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<String> getFeelNames(Long contentId, Stream stream) {
		return feelContentUserRepository.getAllNamesByContent(contentId, stream);
	}
	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.FeelService#getFeelNames(java.lang.Long, java.lang.Integer, kr.co.sunnyvale.sunny.domain.extend.Stream)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<String> getFeelNames(Long contentId, Integer feelId,
			Stream stream) {
		return feelContentUserRepository.getMatchNamesByContent(contentId, feelId, stream);
		
	}

	
}
