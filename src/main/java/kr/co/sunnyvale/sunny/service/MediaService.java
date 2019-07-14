package kr.co.sunnyvale.sunny.service;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.Content;
import kr.co.sunnyvale.sunny.domain.Draft;
import kr.co.sunnyvale.sunny.domain.Media;
import kr.co.sunnyvale.sunny.domain.Reply;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.extend.MediaType;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;


public interface MediaService{
	
	/**
	 * media는 toStory에 해당하는 모든 AGP를 그대로 설정한다.
	 * toStory에 media를 추가시킨다.
	 * @param storyId
	 * @param mediaId
	 */
	public void appendMediaToStory(Long storyId, Long mediaId);
	
	/**
	 * toStory에 mediaList의 미디어들을 추가한다. toStory에 해당하는 모든 AGP를 그대로 설정한다.
	 * @param toStory
	 * @param mediaList
	 */
	public void appendMediesToStory(Long storyId, List<Long> mediaIdList);
	
	/**
	 * toStory에 mediaList의 미디어들을 추가한다. toStory에 해당하는 모든 AGP를 그대로 설정한다.
	 * @param toStory
	 * @param mediaList
	 */
	public void appendMediaesToContent(Long contentId, Long ... list);
	

	public void appendMediaesToContent(Long id, List<Media> mediaes);
	
	/**
	 * fromStory가 가지고 있는 모든 Media 목록을 반환한다. 
	 * @param fromStoryId
	 * @return
	 */
	public List<Media> getMedies(Long fromStoryId, Stream stream);
	
	/**
	 * toStory에서 Media를 삭제한다. 
	 * @param toStory
	 * @param mediaId
	 */
	public boolean deleteMediaFromStory(Long mediaId);
	
	/**
	 * 스토리의 퍼미션이 바뀌면 스토리가 가지는 미디어들의 퍼미션도 같게 변경해야한다.
	 * @return
	 */
	public boolean updateMediesPermitionWithStoryPermition(Long storyId);

	
	public List<Media> getFromDraft(Draft draft);

	public void save(Media media);

	public Media saveToTargetDraft(Media media);

	public void delFromContent(Long mediaId);

	public void del(Long mediaId);

	public void delFromDraft(Long mediaId);

	public List<Media> getFromContent(Content content);

	public void appendMediaesToRevision(Long id, List<Media> mediaes);

	public Reply setMediaToReply(Long id, Long mediaId);

	public Page<Media> getPagedResult(Sunny sunny, Long smallGroupId, User user,
			String query, Integer isMy, MediaType[] types, Integer page, int pageSize);


}
