package kr.co.sunnyvale.sunny.service;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.Site;
import kr.co.sunnyvale.sunny.domain.SmallGroup;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.dto.SmallGroupDTO;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.SmallGroupIdName;
import kr.co.sunnyvale.sunny.domain.extend.SmallGroupTree;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.domain.post.ContentPermissionDTO;
import kr.co.sunnyvale.sunny.domain.post.SmallGroupRegistPostDTO;
import kr.co.sunnyvale.sunny.domain.post.TreePutDTO;
import kr.co.sunnyvale.sunny.domain.post.UpdateSmallGroupDTO;

public interface SmallGroupService {
	
	
	public SmallGroup saveSmallGroupAndPermission(Site site, SmallGroup parentSmallGroup, SmallGroup prevSmallGroup,
			SmallGroup smallGroup, int smallGroupType );
	
	/**
	 * 그룹 만들기. 부모 그룹 바로 밑에 새로운 그룹을 하나 만든다.
	 * 만약 부모가 없으면 가장 조상
	 * 
	 * @param site
	 * @param parentSmallGroup
	 * @param smallGroup
	 * @param smallGroupType
	 */
	public SmallGroup saveSmallGroup( Site site, SmallGroup parentSmallGroup, SmallGroup prevSmallGroup, SmallGroup smallGroup, int smallGroupType );
	
	
	/**
	 * 기본 나만보기 그룹을 만든다.
	 * 생성하면서 자동으로 UserSmallGroupAccess 에 생성한 유저를 넣어준다.
	 * @param site
	 * @param user
	 * @return
	 */
	public SmallGroup generateMySmallGroup( Site site, User user );
	
	/**
	 * 기본 친구공개 그룹을 만든다.
	 * 생성하면서 자동으로 UserSmallGroupAccess 에 생성한 유저를 넣어준다.
	 * @param site
	 * @param user
	 * @return
	 */
	public SmallGroup generateFriendSmallGroup( Site site, User user );
	
	/**
	 * 로비 그룹을 만든다. 사이트 관리자에 의해 최초 한번만 실행됨.
	 * 로비를 생성하면서 자동으로 UserSmallGroupAccess 에 생성한 유저를 넣어준다.
	 * @param site
	 * @param user
	 * @return
	 */
	public SmallGroup generateLobbySmallGroup( Site site, User user );
	
	
	/**
	 * 사용자 정의 그룹을 만든다. 
	 * 스토리 작성 시 포함된 사용자들을 하나의 그룹에 몰아넣는다.
	 * 만들기만 그렇게 해놓고 현재 기획은 1명만 넣고 있다. 
	 * @param sunny
	 * @param user
	 * @param targetUser
	 */
	public SmallGroup generateUserDefineGroup(Sunny sunny, User user, User targetUser);
	
	
	/**
	 * 특정 부서 아래의 스몰그룹 목록 구하기
	 * @param site
	 * @param parentSmallGroup
	 * @param getType SmallGroupService인터페이스의 상수 참고 
	 * @return
	 */
	public List<SmallGroup> getSmallGroupList(Site site, SmallGroup parentSmallGroup, int getType, Stream stream);
	
	/**
	 * 모든 스몰그룹 목록 구하기 - 조직도를 보일때 사용하는 목적임.
	 * @param site
	 * @param getType SmallGroupService인터페이스의 상수 참고 
	 * @return
	 */
	public List<SmallGroup> getSmallGroupList(Site site, int getType, Stream stream);	

	
	/**
	 * 사용자가 속해있는 스몰그룹 구하기
	 * @param site
	 * @param parentSmallGroup
	 * 만약 parentSmallGroup 이 null 이 아니면 특정 부서 아래의 스몰그룹만 가져온다. 
	 * @param user
	 * 만약 userr가 null 이면 사용자 상관없이 가져온다. 
	 * @param type
	 * 만약 type이 null 이면 모든 타입의 스몰그룹을 가져온다. 
	 * @param stream
	 * @return
	 */
	public List<SmallGroup> getSmallGroupList(Site site, SmallGroup parentSmallGroup, User user, Integer type, Stream stream);
	
	
	/**
	 * 사용자가 속해있는 스몰그룹 구하기
	 * @param site
	 * @param parentSmallGroup
	 * 만약 parentSmallGroup 이 null 이 아니면 특정 부서 아래의 스몰그룹만 가져온다. 
	 * @param user
	 * 만약 userr가 null 이면 사용자 상관없이 가져온다. 
	 * @param type
	 * 만약 type이 null 이면 모든 타입의 스몰그룹을 가져온다. 
	 * @param desc 
	 * @param queryName 
	 * @param stream
	 * @return
	 */
	public Page<SmallGroup> getSmallGroupPage(Site site, SmallGroup parentSmallGroup, User user, Integer type, String queryName, String ordering, Boolean desc, Integer pageNum, Integer pageSize);
	


	public Page<SmallGroup> getSmallGroupPage(Site site, SmallGroup parentSmallGroup, User user, Integer type, String queryName, boolean includePath, Integer pageNum, Integer pageSize);
	
	/**
	 * srcSmallGroup을 toSmallGroup 아래로 옮긴다. 
	 * 1) 기존 부모 퍼미션을 삭제할지 안할지 여부 결정 
	 * @param site
	 * @param toSmallGroup
	 * @param srcSmallGroup
	 * @param deleteExistingAncestorPermistionFlag 기존 조상 퍼미션을 삭제할지 여부 true : 삭제 false : 삭제안함
	 * @return
	 */
	public boolean moveSmallGroup(Site site, SmallGroup toSmallGroup, SmallGroup srcSmallGroup, boolean deleteExistingAncestorPermistionFlag);

	/**
	 * toSmallGroup과 그 이하의 모든 부서 자료들을 삭제한다. 
	 * realDeleteFlag가 true면 진짜 삭제!, false이면 삭제되었다는 flag만 지정하도록 한다.
	 * @param iste
	 * @param toSmallGroup
	 * @param realDeleteFlag
	 * @return
	 */
	public void deleteSmallGroup(Site site, Long smallGroupId, boolean realDeleteFlag);
	
	/**
	 * toSmallGroup과 그 하위 모든 자료들에게서 조상과 관련된 퍼미션을 삭제한다.
	 * @param site
	 * @param toSmallGroup
	 * @return
	 */
	public boolean deleteAncestorGroupPermistion(Site site, SmallGroup toSmallGroup);
	
	/**
	 * toSmallGroup과 그 하위 모든 자료들에게 permitionSmallGroups에 대한 퍼미션을 부여한다.
	 * @param site
	 * @param toSmallGroup
	 * @param permitionSmallGroups
	 * @param readPermition
	 * @param updatePermition
	 * @param deletePermition
	 * @return
	 */
	public boolean appendGroupPermistion(Site site, SmallGroup toSmallGroup, SmallGroup[] permitionSmallGroups, boolean readPermition, boolean updatePermition, boolean deletePermition);

	/**
	 * smallGroupId에 해당하는 스몰그룹에 사용자를 추가한다.
	 * @param userId
	 * @param smallGroup
	 * @return
	 */
	public void addUserToSmallGroup(Long userId, Long smallGroupId, boolean isAdmin);
	
	/**
	 * smallGroupId에서 사용자를 뺀다. 
	 * @param userId
	 * @param smallGroup
	 * @return
	 */
	public void removeUserFromSmallGroup(Long userId, Long smallGroupId);
	
	public SmallGroup getSmallGroup(Long smallGroupId);
	
	public List<String> getDepartmentStrings(Site site, Long userId);

	
	
	public List<SmallGroupDTO> getMatchList(Site site, Long userId, String key,
			int typeDepartment, Stream stream);
	
	/**
	 * userId, smallGroupId 에 해당하는 ptype(a, r, w, d, fu, fd)에 해당하는 퍼미션을 반대로 바꾼다.
	 * allDataFlag 가 true일 경우 userId가 smallGroupId부서 이하에서 만든 모든 자료들에게서 userId퍼미션을 삭제한다.
	 * @param userId
	 * @param smallGroupId
	 * @param ptype
	 * @param allDataFlag
	 */
	public void setPermission( Long userId, Long smallGroupId, String ptype, boolean allDataFlag);

	public Page<SmallGroup> pagedList(Sunny sunny, Long userId,
			Integer queryType, String queryName, Integer type,
			String ordering, Integer page, int pagesize);

	
	/**
	 * 친구맺은 뒤 양쪽의 친구공개 스몰그룹에 서로를 넣어준다. 
	 * @param followerId
	 * @param followedId
	 */
	public void addBothFriendAccessGroup(Long followerId, Long followedId);
	
	/**
	 * 친구끼리의 accessGroup 을 뺀다. 
	 * @param followerId
	 * @param followedId
	 */
	public void removeBothFriendAccessGroup(Long followerId, Long followedId);

	
	/**
	 * 해당 컨텐츠에 퍼미션 설정된 그룹 리스트를 가져온다. 
	 * @param sunny
	 * @param id
	 * @param stream
	 * @return
	 */
	public List<SmallGroup> getContentAssignedSmallGroups(Sunny sunny, Long id,
			Stream stream);

	public Page<SmallGroup> getContactSmallGroupPage(Site site, 
			User user, Integer page, int defaultPageSize);

	
	/**
	 * DTO 로 들어온 smallGroup 을 저장한다.
	 * @param registDto
	 * @return
	 */
	public SmallGroup save(Sunny sunny, User user, SmallGroupRegistPostDTO registDto);

	public void inviteUsers(Sunny sunny, Long smallGroupId, User user,
			List<Long> userIds, boolean forceAdd);

	public void update(SmallGroup smallGroup);

	public void addAccesses(Sunny sunny, Long smallGroupId, User user,
			List<ContentPermissionDTO> permissionDtos);

	public void removeInactiveUserFromSmallGroup(Long userId, Long smallGroupId);

	public void plusUserCount( Long smallGroupId );
	
	public void minusUserCount( Long smallGroupId );
	
	public void plusInactiveUserCount( Long smallGroupId );
	
	public void minusInactiveUserCount( Long smallGroupId );

	public void acceptUser(Sunny sunny, Long smallGroupId, User user,
			Long userId);

	public void plusEventCount( Long smallGroupId );
	
	public void minusEventCount( Long smallGroupId );

	public void joinAdmin(Sunny sunny, Long smallGroupId, User user, Long userId);

	public void removeAdmin(Long userId, Long smallGroupId);

	public void plusNoteCount(Long id);
	
	public void minusNoteCount(Long id);

	public void plusApprovalCount(Long id);
	
	public void minusApprovalCount(Long id);

	public List<SmallGroupIdName> getChildrenSmallGroupIdNames(Site site, Long parentId, int ... type);

	public List<SmallGroupTree> getTreeFirst(Site site, int smallGroupType);

	public void fillChildren(Sunny sunny, SmallGroupTree parent);

	public void move(TreePutDTO treePutDTO);

	public void update(Long smallGroupId,
			UpdateSmallGroupDTO updateSmallGroupDto);

	public void remove(Sunny sunny, Long smallGroupId);

}