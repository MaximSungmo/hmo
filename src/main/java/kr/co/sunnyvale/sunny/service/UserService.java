package kr.co.sunnyvale.sunny.service;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.AuthToken;
import kr.co.sunnyvale.sunny.domain.Site;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.dto.UserDTO;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.SecurityUser;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.domain.post.AdminJoinDTO;
import kr.co.sunnyvale.sunny.domain.post.PasswordPost;
import kr.co.sunnyvale.sunny.domain.post.RelationDTO;
import kr.co.sunnyvale.sunny.domain.post.UpdateProfileUser;
import kr.co.sunnyvale.sunny.domain.post.UpdateUserFromAdminDTO;
import kr.co.sunnyvale.sunny.domain.post.UserInvitePostDTO;

public interface UserService {

	/**
	 * 이메일을 통한 사용자 가져오기(캐시)
	 * @param email
	 * @return
	 */
	User findByEmail( String email );
	
	
	/**
	 * 사용자 userId 를 통해 사용자 가져옴.(캐시)
	 * @param id
	 * @return
	 */
	User findById( Long id );

	/**
	 * 사용자 userId 를 통해 캐시되지 않은 사용자 가져옴.
	 * @param id
	 * @return
	 */
	User findPersistentUserById( Long id );
	
	/**
	 * 같은 패스워드를 입력했는지 본다.
	 * @param user
	 * @param currentPassword
	 * @return
	 */
	public boolean checkEqualsPassword(User user, String currentPassword) ;
	/**
	 * 이메일 존재 여부
	 * @param email
	 * @return
	 */
	boolean existsEmail( String email );

	/**
	 * 일차적으로 관리자를 추가하고 토큰 생성 후 인증 메일을 보낸다.  
	 * @param site
	 * @param user
	 * @return
	 * 생성된 Persistent 한 ADmin
	 */
	AuthToken registAdmin( AdminJoinDTO adminJoinDTO );
	
	/**
	 * 관리자가 회원가입 후 이메일 인증메일이 왔을 때의 링크를 클릭했을 때 들어오는 부분
	 * @param authTokenValue
	 * @param userId
	 */
	User confirmAdmin( String authTokenValue, Long userId );

//	/**
//	 * 관리자가 회원 추가시. 확인 이메일을 보내지 않고 바로 회원 가입시킬 경우 사용.
//	 * @param site
//	 * @param user
//	 * @return TODO
//	 */
//	User registAndConfirmUser(Site site, User user);
//	
	/**
	 * 관리자가 회원 정보를 생성 후, 사용자에게 이메일을 보내 confirm을 기다린다.
	 * @param userRegistDto
	 */
//	void registUser(Site site, UserRegistPostDTO userRegistDto);
	
	/**
	 * super super 관리자가 사이트와 관리자를 생성하고 싶을 경우 사용한다.
	 * @param site
	 * @param user
	 * @return
	 */
//	User registAndConfirmAdmin(Site site, User user);


	/**
	 * 비밀번호 변경
	 * @param id
	 * @param passwordPost
	 */
	void changePassword(Long id, PasswordPost passwordPost);
	
	
	
	/**
	 * siteId에 해당하는 사이트에 있는 모든 회원 목록을 구함. searchName이 있을 경우 like검색, null이면 모든 사용자 검색.
	 * 
	 * @param sunny
	 * @param smallGroup
	 * 존재하면 해당 스몰그룹에 속해있는 사용자만 가져온다.
	 * @param queryType
	 * 0 - 이름
	 * 1 - email
	 * 2 - 직책
	 * @param queryName
	 * @param status
	 * 0 - 일하는중
	 * 1 - 휴가중
	 * 2 - 퇴사
	 * @param onlyAdmin 
	 * @param exclusiveUserId 
	 * @param page
	 * @param defaultPageSize
	 * @return
	 */
	public Page<User> getUserList(Sunny sunny, List<Long> smallGroupIds,
			Integer queryType, String queryName, Integer[] status, String range, String ordering, Long excludeUserId, Boolean onlyAdmin, Integer page,
			int defaultPageSize);

	
	
	/**
	 * 관리자메뉴에서 부서에 해당하는 사용자 정보만 읽어오기.
	 * @param smallGroupId
	 * @param descendantFlag
	 * @param searchName
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
//	public Page<UserSmallGroupAccess> getUserSmallGroupAccessList(Long smallGroupId, boolean descendantFlag,String searchName,  Integer pageNum, int pageSize);
	
	/**
	 * smallGroupId에 해당하는 부서에 포함된 회원 목록을 구함.
	 * descendantFlag 가 true일 경우는 후손 부서의 회원까지 포함하여 구함
	 * searchName이 null이 아닐경우 like검색
	 * @param siteId
	 * @param smallGroupId
	 * @param descendantFlag
	 * @param searchName
	 * @param stream
	 * @return
	 */
	public Page<User> getUserList(Long smallGroupId, boolean descendantFlag, String searchName,  Integer pageNum, int pageSize);
	
	
	/**
	 * 
	 * @param id
	 * @param object
	 * @param stream
	 * @return
	 */
	public List<RelationDTO> getUserList(Long siteId, Long userId, String searchName, Stream stream);
	

	public Page<User> getUserList(Long siteId, Long userId, String queryName, Integer[] status, Integer page, int pageSize);


	/**
	 * siteId에 해당하는 사이트의 회원 수를 구함.
	 * @param siteId
	 * @param searchName
	 * @return
	 */
	public int getUserCount(Long siteId, String searchName);
	
	/**
	 * smallGroupId에 해당하는 사이트의 회원 수를 구함.
	 * @param siteId
	 * @param smallGroupId
	 * @param descendantFlag
	 * @param searchName
	 * @return
	 */
	public int getUserCount(Long smallGroupId, boolean descendantFlag, String searchName);
	
	
	/**
	 * userId에 해당하는 회원 삭제
	 * realDelete가 true면 실제로 삭제. 
	 * 실제로 삭제될 경우의 문제점과 실제 삭제안했을때의 문제점에 대하여 좀더 깊은 고민이 필요함. 
	 * @param userId
	 * @param realDelete
	 */
	public void deleteUser(Long userId, boolean realDelete);
	
//	/**
//	 * 사용자의 기본정보를 수정한다.
//	 * @param user
//	 */
//	public void updateUser(Site site, Long userId, SecurityUser securityUser, UserRegistPostDTO userRegistDto);


	void update(Long id, User user);


	List<UserDTO> getMatchUsers(Site site, Long userId, String key, Stream stream);

	/**
	 * userId에 해당하는 회원에게 smallGroup을 설정함. 사이트 Id가 모두 같을 경우에만 가능 하나라도 틀리다면 오류발생함.
	 * @param siteId
	 * @param userId
	 * @param SmallGroupId
	 */
	public void setSmallGroup(Long siteId, Long userId, Long smallGroupId);

	
	/**
	 * userId에 해당하는 회원에게서 smallGroup과 관련된 정보를 삭제함.
	 * @param siteId
	 * @param userId
	 * @param smallGroupId
	 */
	public void delSmallGroup(Long siteId, Long userId, Long smallGroupId, boolean deleteContentPermission);


	void updateUser(Long id, UpdateProfileUser updateUser,
			SecurityUser securityUser);


	void plusFriendCount(Long userId);
	
	void minusFriendCount(Long userId);
	
	void plusDepartmentCount( Long userId);
	
	void minusDepartmentCount( Long userId );
	
	void plusGroupCount( Long userId );
	
	void minusGroupCount( Long userId );
	
	void plusProjectCount( Long userId );
	
	void minusProjectCount( Long userId);

	Page<User> getFavoriteUsers(Sunny sunny, User user, Integer page, int pageSize);


	/**
	 * 그룹에 해당되지 않는 사용자 리스트를 가져온다. 
	 * @param sunny
	 * @param smallGroupId
	 * @param user
	 * @param queryName
	 * @param page
	 * @param pageSize
	 * @return
	 */
	Page<User> getPagedNotInSmallGroupUserList(Sunny sunny, Long smallGroupId,
			Long userId, String queryName, Integer page,
			int pageSize);


	void save(User user);


	void inviteUser( Sunny sunny, User inviteUser, UserInvitePostDTO userInviteDto );


	void update(Long userId, UpdateUserFromAdminDTO updateSiteUserFromAdminDto);


	boolean isJoined(Long id, Long userId);


	void activate(String key, Long siteInactiveUserId);


	List<Long> findAllJoinedUsers(Long siteId, Long exceptUserId);


	Page<User> getAllFromSuper(Integer page, int pageSize);


	List<User> getSimpleUserList(Long siteId, Long authUserId, Long lineUserId, String queryName, Boolean top, Integer size);


	
}
