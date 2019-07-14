package kr.co.sunnyvale.sunny.service;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.Story;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.dto.NoticeDTO;
import kr.co.sunnyvale.sunny.domain.dto.NotifyInfoDTO;
import kr.co.sunnyvale.sunny.domain.dto.StoryDTO;
import kr.co.sunnyvale.sunny.domain.dto.StoryModifyDTO;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.domain.post.StoryModifyPostDTO;
import kr.co.sunnyvale.sunny.domain.post.StoryPostDTO;

public interface StoryService{
	/**
	 * Controller 에 넘어온 StoryPostDTO를 Story 로 저장한다.
	 * @param storyPostDTO
	 */
	public Story postStory(Sunny sunny, StoryPostDTO storyPostDTO);
	

	public StoryDTO getStoryDTO(Sunny sunny, Long id, User user);
	/**
	 * 스토리 목록 가져오기
	 * @param smallGroupId
	 * @param user
	 * @return
	 */
	//public List<StoryDTO> fetchStories(Long smallGroupId, User user);


	public void delete(Long storyId);


	public List<StoryDTO> fetchLobbyStories(Sunny sunny, User user, String[] queries, Stream stream);


	public List<StoryDTO> fetchNewsfeedStories(Sunny sunny, User user, String[] queries, Stream stream);


	public List<StoryDTO> fetchSmallGroupStories(Sunny sunny, Long smallGroupId,	User user, Boolean isWantChildren, String[] queries, Stream stream);


	void plusReplyCount(Long storyId);


	void minusReplyCount(Long storyId);


	public List<StoryDTO> fetchBasecampStories(Sunny sunny, User basecampUser, User user, String[] queries, Stream stream);


		public List<NoticeDTO> getNotices(Sunny sunny, User user, Long smallGroupId, Stream stream);


	public List<StoryDTO> fetchNoticeStories(Sunny sunny, User user,
			String[] queries, Stream stream);


	public StoryModifyDTO getModifyStory(Sunny sunny, User user, Long id);


	public Story modifyStory(Sunny sunny, Long storyId, StoryModifyPostDTO storyModify);


	public Number getNoticeUnreadCount(Sunny sunny, User user);



	public List<NoticeDTO> getNoticesAndUpdate(Sunny sunny, User user,
			Long smallGroupId, Stream stream);


	public List<StoryDTO> fetchFeedback(User user, String[] queries,
			Stream stream);


	public Story geNoticeNewOne(Sunny sunny, User user);


	public NotifyInfoDTO getNoticeNotifyInfo(Sunny sunny, Long userId);


	public List<StoryDTO> getSuperAdminStories(Sunny sunny, Long siteId, User user,
			String[] queries, Stream stream);


	public int getSiteStoryCount(Long siteId);


	public List<StoryDTO> fetchSuperStories(Sunny sunny, User user,
			Stream stream);


	public Story postSuperStory(Sunny sunny, StoryPostDTO storyPost);



}
