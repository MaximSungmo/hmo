package kr.co.sunnyvale.sunny.repository.hibernate;

import java.util.List;
import java.util.Set;

import kr.co.sunnyvale.sunny.domain.Content;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.dto.ContentDTO;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;

public interface ContentService {

	//	@Cacheable( cacheName="yacampContentCache", keyGenerator=@KeyGenerator( name="", properties=@Property( name="includeMethod", value="false" ) ) )
	public Content getContent(Long contentId);

	//	@Transactional(readOnly = true)
	//	public List<StoryDTO> getApiBothNewsfeedList(User user, Stream stream) {
	//		List<StoryDTO> newsfeeds = contentRepository.getBothNewsfeedList(user, stream);
	//		
	//		for( StoryDTO newsfeed : newsfeeds ){
	//			if( newsfeed.getType() != kr.co.sunnyvale.sunny.domain.Content.TYPE_STORY)
	//				continue;
	//			
	//			Feel feel = feelService.getFeelFromContentUser(user, new Story(newsfeed.getId()));
	//			if( feel != null )
	//				newsfeed.setFeeldId(feel.getId());
	//			List<Reply> replys = replyService.getReplyList(newsfeed.getId(), new Stream(null, null, null, 3));
	//			Collections.reverse(replys);
	//			newsfeed.setReplys(replys);
	////			newsfeed.setTags(tagService.getTags(newsfeed.getId(), null));
	//		}
	//		return newsfeeds;	
	//	}
	//	@Transactional(readOnly = true)
	//	public List<StoryDTO> getWebBothNewsfeedList(User user, Stream stream) {
	//		List<StoryDTO> newsfeeds = contentRepository.getBothNewsfeedList(user, stream);
	//		
	//		for( StoryDTO newsfeed : newsfeeds ){
	//			if( newsfeed.getType() != kr.co.sunnyvale.sunny.domain.Content.TYPE_STORY)
	//				continue;
	//			
	//			Feel feel = feelService.getFeelFromContentUser(user, new Story(newsfeed.getId()));
	//			if( feel != null )
	//				newsfeed.setFeeldId(feel.getId());
	//			List<Reply> replys = replyService.getReplyList(newsfeed.getId(), new Stream(null, null, null, 3));
	//			Collections.reverse(replys);
	//			newsfeed.setReplys(replys);
	////			newsfeed.setTags(tagService.getTags(newsfeed.getId(), null));
	//		}
	//		return newsfeeds;	
	//	}
	//	@Transactional(readOnly = true)
	//	public Date getCreateDate(Long contentId) {
	//		return contentRepository.getCreateDate(contentId);
	//	}
	//	
	public void update(Content content);

	//	@Transactional(readOnly = true)
	//	public List<Content> searchWhole(String keyword, Stream stream) {
	//		return contentRepository.searchWhole(keyword, stream);
	//	}
	//	
	//	@Transactional(readOnly = true)
	//	public List<Content> searchCategory(Integer categoryId, String keyword, Stream stream) {
	//		return contentRepository.searchCategory(categoryId, keyword, stream);
	//	}
	//	
	public Integer getContentType(Long id);

	public void delete(Sunny sunny, Long id);

	public boolean isSameUser(Long contentId, Long userId);

	public Content changeTags(Content content, Set<String> tagTitles);

	public Set<String> parseStringifyToSet(String tagString);

	public Content changeTags(Content content, String rawTagsString);

	public void versionUpdate(Content content);

	public void plusMediaCount(Long id);

	public void minusMediaCount(Long id);

	
	public List<ContentDTO> fetchApprovals(Sunny sunny, String menu, Long smallGroupId, Boolean isWantChildren, User authUser, String[] queries, Stream stream) ;

	public void operationAfterContent(Sunny sunny, ContentDTO content,
			String[] queries, User user);

	public void operationAfterContent(Sunny sunny, Content content, String[] queries,
			User user);

}