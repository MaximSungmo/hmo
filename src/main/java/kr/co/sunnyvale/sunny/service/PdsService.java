package kr.co.sunnyvale.sunny.service;

import kr.co.sunnyvale.sunny.domain.Pds;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.domain.post.ContentPostDTO;

public interface PdsService{
	/**
	 * Controller 에 넘어온 StoryPostDTO를 Story 로 저장한다.
	 * @param storyPostDTO
	 */
	public Pds postPds(Sunny sunny, ContentPostDTO contentPostDTO);

	public void delete(Long pdsId);

	void plusReplyCount(Long pdsId);

	void minusReplyCount(Long pdsId);

	public boolean checkPermission(Long id, User user);

	/**
	 * 스몰그룹에 해당하는 자료들을 가져온다.
	 * 만약 스몰그룹이 없으면 로비에 있는거 가져옴.
	 * 
	 * @param sunny
	 * @param smallGroupId
	 * @param targetUser
	 * @param user
	 * @param query
	 * @param ordering
	 * @param page
	 * @param defaultPageSize
	 * @return
	 */
	public Page<Pds> getPagedSmallGroupsPds(Sunny sunny, Long smallGroupId, User targetUser, User user,
			String query, String ordering, Integer page, int defaultPageSize);

//	public Pds findById(Long pdsId);
	
	public Pds getPds( Sunny sunny, User user, Long pdsId );

}
