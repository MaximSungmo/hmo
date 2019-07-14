package kr.co.sunnyvale.sunny.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import kr.co.sunnyvale.sunny.domain.Site;
import kr.co.sunnyvale.sunny.domain.SmallGroup;
import kr.co.sunnyvale.sunny.domain.SmallGroupInactiveUser;
import kr.co.sunnyvale.sunny.domain.SmallGroupSmallGroupAccess;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.UserSmallGroupAccess;
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
import kr.co.sunnyvale.sunny.exception.AlreadyJoinedException;
import kr.co.sunnyvale.sunny.exception.SimpleSunnyException;
import kr.co.sunnyvale.sunny.repository.hibernate.SmallGroupInactiveUserRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.SmallGroupRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.SmallGroupSmallGroupAccessRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.StoryRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.UserSmallGroupAccessRepository;
import kr.co.sunnyvale.sunny.service.SiteService;
import kr.co.sunnyvale.sunny.service.SmallGroupService;
import kr.co.sunnyvale.sunny.service.SmallGroupSmallGroupAccessService;
import kr.co.sunnyvale.sunny.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

@Service( value="smallGroupService" )
@Transactional
public class SmallGroupServiceImpl implements SmallGroupService {
	
	@Autowired
	private SmallGroupRepository smallGroupRepository;
	
	@Autowired
	private StoryRepository storyRepository;
	
	@Autowired
	private SiteService siteService;
	
	@Autowired
	private UserService userService; 
	
	@Autowired
	private UserSmallGroupAccessRepository userSmallGroupAccessRepository;
	
	@Autowired
	private SmallGroupSmallGroupAccessRepository smallGroupSmallGroupAccessRepository;

	@Autowired
	private SmallGroupSmallGroupAccessService smallGroupSmallGroupAccessService;

	@Autowired
	private SmallGroupInactiveUserRepository smallGroupInactiveUserRepository;
	
	
	
	
	@Override
	@Transactional
	public SmallGroup generateLobbySmallGroup(Site site, User user) {
		
		if( site == null ){
			throw new SimpleSunnyException();
		}
		SmallGroup smallGroup = new SmallGroup();
		smallGroup.setDescription("default small group");
		smallGroup.setSite(site);
		smallGroup.setType( SmallGroup.TYPE_LOBBY );
		//smallGroup.setCreator(user);
		smallGroupRepository.save(smallGroup);
		
		smallGroup.setWrapAbsolutePath( smallGroup.getId() );
		smallGroup.setThread(smallGroup.getId());
		smallGroupRepository.update(smallGroup);

		SmallGroupSmallGroupAccess smallGroupSmallGroupAccess = new SmallGroupSmallGroupAccess();
		smallGroupSmallGroupAccess.setSmallGroup(smallGroup);
		smallGroupSmallGroupAccess.setAccessSmallGroup(smallGroup);
		smallGroupSmallGroupAccessRepository.save(smallGroupSmallGroupAccess);
		
		addUserToSmallGroup(user.getId(), smallGroup.getId(), true);
//		generateDefaultAccessAndAccess(user, smallGroup);
		
		return smallGroup;
		
	}
	
	@Override
	@Transactional
	public SmallGroup generateMySmallGroup(Site site, User user) {
		
		SmallGroup smallGroup = new SmallGroup();
		smallGroup.setCreator(user);
		smallGroup.setDescription("my small group");
		smallGroup.setSite(site);
		//smallGroup.setName(user.getName());
		smallGroup.setOnlyMineUser(user);
//		smallGroup = saveSmallGroup(site, null, smallGroup, SmallGroup.TYPE_ME);
//		generateDefaultAccessAndAccess(user, smallGroup);

		smallGroup =  saveSmallGroupAndPermission(site, null, null, smallGroup, SmallGroup.TYPE_ME);
	
		addUserToSmallGroup(user.getId(), smallGroup.getId(), true);
		
		return smallGroup;
	
	}

	@Override
	@Transactional
	public SmallGroup generateFriendSmallGroup(Site site, User user) {
		SmallGroup smallGroup = new SmallGroup();
		smallGroup.setDescription("friend small group");
		smallGroup.setCreator(user);
		smallGroup.setSite(site);
		
		smallGroup =  saveSmallGroupAndPermission(site, null, null, smallGroup, SmallGroup.TYPE_FRIEND);
//		smallGroup =  saveSmallGroup(site, null, smallGroup, SmallGroup.TYPE_FRIEND);
//		
//		generateDefaultAccessAndAccess(user, smallGroup);
		addUserToSmallGroup(user.getId(), smallGroup.getId(), true);
		return smallGroup;
	}

	

	@Override
	@Transactional
	public SmallGroup generateUserDefineGroup(Sunny sunny, User user,
			User targetUser) {

		SmallGroup smallGroup = new SmallGroup();
		smallGroup.setDescription("user defined small group");
		smallGroup.setCreator(user);
		smallGroup.setSite(sunny.getSite());
		smallGroup =  saveSmallGroupAndPermission(sunny.getSite(), null, null, smallGroup, SmallGroup.TYPE_USER_DEFINE_GROUP);
		
		addUserToSmallGroup(targetUser.getId(), smallGroup.getId(), true);
		
		//generateDefaultAccessAndAccess(targetUser, smallGroup);
		
		return smallGroup ;
		
	}
	
	/**
	 * 그룹이 생성될 때 일반적으로 자기 자신은 그 그룹에 참여되고
	 * 그룹은 자기 자신에 대한 액세스 그룹을 갖게 된다.  
	 * @param user
	 * @param smallGroup
	 */
//	@Transactional
//	private void generateDefaultAccessAndAccess(User user, SmallGroup smallGroup) {
//		UserSmallGroupAccess userSmallGroupAccess = new UserSmallGroupAccess();
//		userSmallGroupAccess.setSmallGroup(smallGroup);
//		userSmallGroupAccess.setUser(user);
//		userSmallGroupAccessRepository.save(userSmallGroupAccess);
//		
//
//		SmallGroupSmallGroupAccess smallGroupSmallGroupAccess = new SmallGroupSmallGroupAccess();
//		smallGroupSmallGroupAccess.setSmallGroup(smallGroup);
//		smallGroupSmallGroupAccess.setAccessSmallGroup(smallGroup);
//		smallGroupSmallGroupAccessRepository.save(smallGroupSmallGroupAccess);
//	}
	

	@Override
	@Transactional
	public void addBothFriendAccessGroup(Long followerId, Long followedId) {
		List<SmallGroup> retSmallGroupList = smallGroupRepository.getUserCreateSmallGroup(followerId, SmallGroup.TYPE_FRIEND);
		
		SmallGroup followerFriendSmallGroup = retSmallGroupList.get(0);
		
		retSmallGroupList = smallGroupRepository.getUserCreateSmallGroup(followedId, SmallGroup.TYPE_FRIEND);
		SmallGroup followedFriendSmallGroup = retSmallGroupList.get(0);
		
		addUserToSmallGroup(followerId, followedFriendSmallGroup.getId(), false);
		addUserToSmallGroup(followedId, followerFriendSmallGroup.getId(), false);
	}

	@Override
	public void removeBothFriendAccessGroup(Long followerId, Long followedId) {
		List<SmallGroup> retSmallGroupList = smallGroupRepository.getUserCreateSmallGroup(followerId, SmallGroup.TYPE_FRIEND);
		
		SmallGroup followerFriendSmallGroup = retSmallGroupList.get(0);
		
		retSmallGroupList = smallGroupRepository.getUserCreateSmallGroup(followedId, SmallGroup.TYPE_FRIEND);
		SmallGroup followedFriendSmallGroup = retSmallGroupList.get(0);
		
		removeUserFromSmallGroup(followerId, followedFriendSmallGroup.getId());
		removeUserFromSmallGroup(followedId, followerFriendSmallGroup.getId());
	}


	
	@Override
	public SmallGroup saveSmallGroupAndPermission(Site site, SmallGroup parentSmallGroup, SmallGroup prevSmallGroup, 
			SmallGroup smallGroup, int smallGroupType ) {
		
		saveSmallGroup(site, parentSmallGroup, prevSmallGroup, smallGroup, smallGroupType);
		// 기본 퍼미션 주기
		if(parentSmallGroup != null){
			parentSmallGroup = smallGroupRepository.select(parentSmallGroup.getId());
			List<SmallGroup> accessSmallGroups = parentSmallGroup.getAccessSmallGroups();
			for (SmallGroup accessSmallGroup : accessSmallGroups) {
				SmallGroupSmallGroupAccess sga = new SmallGroupSmallGroupAccess();
				sga.setSmallGroup(smallGroup);
				sga.setAccessSmallGroup(accessSmallGroup);
				smallGroupSmallGroupAccessRepository.save(sga);	
			}
		}
		SmallGroupSmallGroupAccess smallGroupAccess = new SmallGroupSmallGroupAccess();
		smallGroupAccess.setSmallGroup(smallGroup);
		smallGroupAccess.setAccessSmallGroup(smallGroup);
		smallGroupSmallGroupAccessRepository.save(smallGroupAccess);
		
		return smallGroup;
	}

	@Override
	public SmallGroup saveSmallGroup(Site site, SmallGroup parentSmallGroup, SmallGroup prevSmallGroup,
			SmallGroup smallGroup, int smallGroupType ) {
		
		if( site == null || smallGroup == null || smallGroupType == SmallGroup.TYPE_LOBBY){
			throw new SimpleSunnyException();
		}
		
		if( parentSmallGroup != null ) {
			// 부서인데 부모가 부서가 아닌경우.
			if( smallGroupType == SmallGroup.TYPE_DEPARTMENT && parentSmallGroup.getType() != SmallGroup.TYPE_DEPARTMENT ){
				throw new SimpleSunnyException("smallGroupType == SmallGroup.TYPE_DEPARTMENT && parentSmallGroup.getType() != SmallGroup.TYPE_DEPARTMENT ");	
			}
			// 소그룹인데 부모가 소그룹이 아닌경우 
//			if( smallGroupType == SmallGroup.TYPE_GROUP && parentSmallGroup.getType() != SmallGroup.TYPE_GROUP ){
//				throw new SimpleSunnyException();
//			}
			// 과제의 부모는 소그룹이 아니어야 한다.
			if( smallGroupType == SmallGroup.TYPE_PROJECT && parentSmallGroup.getType() == SmallGroup.TYPE_GROUP){
				throw new SimpleSunnyException("smallGroupType == SmallGroup.TYPE_PROJECT && parentSmallGroup.getType() != SmallGroup.TYPE_GROUP");
			}
			// 과제의 부모는 광장이 아니어야 한다.
//			if( smallGroupType == SmallGroup.TYPE_PROJECT && parentSmallGroup.getType() != SmallGroup.TYPE_LOBBY){
//				throw new SimpleSunnyException("smallGroupType == SmallGroup.TYPE_PROJECT && parentSmallGroup.getType() != SmallGroup.TYPE_GROUP");
//			}			
		}
		
		smallGroup.setType(smallGroupType);
		smallGroup.setSite(site);
		smallGroupRepository.save(smallGroup);
		

		if( parentSmallGroup == null ){
			smallGroup.setWrapAbsolutePath( smallGroup.getId() );
			smallGroup.setThread(smallGroup.getId());
			smallGroup.setThreadSeq(0);
			smallGroupRepository.update(smallGroup);
			return smallGroup;
		}
		
		smallGroup.setDepth( parentSmallGroup.getDepth() + 1 );
		smallGroup.setParent(parentSmallGroup);
		smallGroup.setParentSmallGroupId( parentSmallGroup.getId() );
		smallGroup.setAbsolutePath( parentSmallGroup.getAbsolutePath(), smallGroup.getId());
		smallGroup.setThread(parentSmallGroup.getThread()); // 쓰레드번호는 같은 가문이니 부모의 Thread값으로 지정해야한다.
		// ThreadSeq에 해당하는 값을 구하고, 해당 ThreadSeq보다 크거나 같은 녀석들에게 +1을 한다. 그리고 나서 해당 SmallGroup의 ThreadSeq의 값을 구한 값으로 변경한다.
		// 현재 그룹의 부모 그룹의 자식 SmallGroup중에서 가장 threadSeq가 큰값을 구한다 없을 경우 부모의 ThreadSeq + 1값을 설정한다.
		
		if( smallGroupType == SmallGroup.TYPE_PROJECT )
			plusChildrenProjectCount(parentSmallGroup.getId());
		else if( smallGroupType == SmallGroup.TYPE_DEPARTMENT )
			plusChildrenDepartmentCount(parentSmallGroup.getId());
		
		
		int parentChildrenFirstThreadSeq = smallGroupRepository.getMaxThreadSeq(parentSmallGroup, SmallGroup.TYPE_DEPARTMENT).intValue();
		if(parentChildrenFirstThreadSeq == -1){ // 부모의 형제중 가장 큰 형제가 없을 경우.
			smallGroupRepository.updateThreadSeqPlugOne(smallGroup.getThread(), parentSmallGroup.getThreadSeq() );
			smallGroup.setThreadSeq( parentSmallGroup.getThreadSeq() + 1 );
			
		}else{
			smallGroupRepository.updateThreadSeqPlugOne(smallGroup.getThread(), parentChildrenFirstThreadSeq );
			smallGroup.setThreadSeq(parentChildrenFirstThreadSeq + 1);
		}
		smallGroupRepository.update(smallGroup);
		
		return smallGroup;
	}



	@Override
	public List<SmallGroup> getSmallGroupList(Site site,
			SmallGroup parentSmallGroup, int getType, Stream stream) {
		/**
		 * 만약 smallGroup 의 id 기준으로 10번 스몰그룹의 다음 10개를 가져오고 싶다면 
		 * 스트림은 컨트롤러에서 다음의 형식으로 넘어와야됩니다.
		 * 
		 * stream.setBaseColumn("id");
		 * stream.setBaseData(10L);
		 * stream.setNext(true);
		 * stream.setSize(10); 
		 */
		
		return smallGroupRepository.getSmallGroupList(site, parentSmallGroup, null, getType, stream);
	}

	@Override
	public List<SmallGroup> getSmallGroupList(Site site, int getType, Stream stream) {
		return smallGroupRepository.getSmallGroupList(site, null, null, getType, stream);
	}

	@Override
	public boolean moveSmallGroup(Site site, SmallGroup toSmallGroup,
			SmallGroup srcSmallGroup,
			boolean deleteExistingAncestorPermistionFlag) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@Transactional
	public void deleteSmallGroup(Site site, Long smallGroupId,
			boolean realDeleteFlag) {
		
		SmallGroup smallGroup = smallGroupRepository.select(smallGroupId);
		
		smallGroupRepository.updateThreadSeqMinusOne(smallGroup.getThread(), smallGroup.getThreadSeq());
		
		smallGroupRepository.delete(smallGroup);
		
	}

	@Override
	public boolean deleteAncestorGroupPermistion(Site site,
			SmallGroup toSmallGroup) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean appendGroupPermistion(Site site, SmallGroup toSmallGroup,
			SmallGroup[] permitionSmallGroups, boolean readPermition,
			boolean updatePermition, boolean deletePermition) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@Transactional
	public void addUserToSmallGroup(Long userId, Long smallGroupId, boolean isAdmin) {
		
		boolean isAlreadyJoined = userSmallGroupAccessRepository.isAlreadyJoined(smallGroupId, userId);
		
		if( isAlreadyJoined ){
			throw new AlreadyJoinedException();
		}
		

		SmallGroup smallGroup = smallGroupRepository.select(smallGroupId);
		if( smallGroup == null ){
			throw new SimpleSunnyException();
		}
		
		UserSmallGroupAccess userSmallGroupAccess = new UserSmallGroupAccess();
		userSmallGroupAccess.setSmallGroup(smallGroup);
		userSmallGroupAccess.setUser(new User(userId));
		userSmallGroupAccess.setAdmin(isAdmin);
		userSmallGroupAccessRepository.save(userSmallGroupAccess);

		smallGroup.setUserCount( smallGroup.getUserCount() + 1);
		smallGroupRepository.update(smallGroup);
		
		if( smallGroup.getType() == SmallGroup.TYPE_DEPARTMENT )
			userService.plusDepartmentCount(userId);
		else if( smallGroup.getType() == SmallGroup.TYPE_GROUP )
			userService.plusGroupCount(userId);
		else if( smallGroup.getType() == SmallGroup.TYPE_PROJECT )
			userService.plusProjectCount(userId);
	}


	@Override
	@Transactional
	public void removeUserFromSmallGroup(Long userId, Long smallGroupId) {
		
		
		UserSmallGroupAccess userSmallGroupAccess = userSmallGroupAccessRepository.getUserSmallGroupAccessUser(smallGroupId, userId);
		

		System.out.println("삭제하기");
		if( userSmallGroupAccess == null ){
			
			System.out.println("널???");
			return;
		
		}
		
		
		SmallGroup smallGroup = userSmallGroupAccess.getSmallGroup();
		
		if( smallGroup.getType() == SmallGroup.TYPE_DEPARTMENT ){
			userService.minusDepartmentCount(userId);
		}
		else if( smallGroup.getType() == SmallGroup.TYPE_GROUP )
			userService.minusGroupCount(userId);
		else if( smallGroup.getType() == SmallGroup.TYPE_PROJECT )
			userService.minusProjectCount(userId);

		minusUserCount(smallGroupId);
		
		userSmallGroupAccessRepository.delete(userSmallGroupAccess); 
		
	}
	
	

	@Override
	public void removeInactiveUserFromSmallGroup(Long userId, Long smallGroupId) {
		SmallGroupInactiveUser inactiveUser = smallGroupInactiveUserRepository.findRelation(smallGroupId, userId);
		
		if( inactiveUser == null ){
			return ;
		}
		

		SmallGroup smallGroup = inactiveUser.getSmallGroup();
		
		if( smallGroup.getType() == SmallGroup.TYPE_DEPARTMENT )
			userService.minusDepartmentCount(userId);
		else if( smallGroup.getType() == SmallGroup.TYPE_GROUP )
			userService.minusGroupCount(userId);
		else if( smallGroup.getType() == SmallGroup.TYPE_PROJECT )
			userService.minusProjectCount(userId);

		minusInactiveUserCount(smallGroupId);
		
		smallGroupInactiveUserRepository.delete(inactiveUser);
	}
	
	
	@Override
	public List<SmallGroup> getSmallGroupList(Site site,
			SmallGroup parentSmallGroup, User user, Integer type, Stream stream) {
		return smallGroupRepository.getSmallGroupList(site, parentSmallGroup, user, type, stream);
	}
	
	@Override
	public Page<SmallGroup> getSmallGroupPage(Site site,
			SmallGroup parentSmallGroup, User user, Integer type, String queryName, String ordering, Boolean desc, Integer pageNum, Integer pageSize ) {
		return smallGroupRepository.getSmallGroupPage(site, parentSmallGroup, user, type, queryName, ordering, desc, pageNum, pageSize );
	}
	

	@Override
	public Page<SmallGroup> getSmallGroupPage(Site site, SmallGroup parentSmallGroup, User user,
			Integer type, String queryName, boolean includePath, Integer pageNum, Integer pageSize) {
		
		
		Page<SmallGroup> pagedResult = smallGroupRepository.getSmallGroupPage(site, parentSmallGroup, user, type, queryName, null, null, pageNum, pageSize); 
		
		for( SmallGroup smallGroup : pagedResult.getContents() ){
			
			if( smallGroup.getParent() != null && smallGroup.getDepth() > 0 ){
				List<SmallGroupIdName> smallGroupPathIdNames = Lists.newArrayList();
				
				SmallGroup parent = smallGroup.getParent();
				while( parent != null ){
					smallGroupPathIdNames.add(new SmallGroupIdName(parent.getId(), parent.getName()));
					parent = parent.getParent();
				}
				
				Collections.reverse(smallGroupPathIdNames);
				smallGroup.setSmallGroupPathIdNames(smallGroupPathIdNames);
			}
		}
		
		return pagedResult;
	}
	
	
	@Override
	public Page<SmallGroup> getContactSmallGroupPage(Site site, User user,
			Integer page, int pageSize) {
		return smallGroupRepository.getContactSmallGroupPage( site, user, page, pageSize );
	}

	@Override
	public SmallGroup getSmallGroup(Long smallGroupId) {
		return smallGroupRepository.select(smallGroupId);
	}

	@Override
	public List<String> getDepartmentStrings(Site site, Long userId) {
		return smallGroupRepository.getDepartmentStrings(site, userId);
	}

	@Override
	public List<SmallGroupDTO> getMatchList(Site site, Long userId, String key,
			int type, Stream stream) {
		List<SmallGroupDTO> results = smallGroupRepository.getMatchList(site, userId, key, type, stream);
		return results;
	}

	@Override
	public void setPermission( Long userId, Long smallGroupId, String ptype,
			boolean allDataFlag) {
		
		userSmallGroupAccessRepository.setPermission( userId, smallGroupId, ptype);
		
		// allDataFlag가 true일 경우 userId가 smallGroupId부서 이하에 작성한 모든 Content의 접근 권한을 삭제한다.
		if(allDataFlag){
			
		}
	}

	@Override
	public Page<SmallGroup> pagedList(Sunny sunny, Long userId,
			Integer queryType, String queryName, Integer type,
			String ordering, Integer page, int pageSize) {
		
		return smallGroupRepository.pagedList(sunny, userId, queryType, queryName, type, ordering, page, pageSize);
	}

	@Override
	public List<SmallGroup> getContentAssignedSmallGroups(Sunny sunny, Long id,
			Stream stream) {
		
		return smallGroupRepository.getContentAssignedSmallGroups(sunny, id, stream);
		
	}

	@Override
	@Transactional
	public SmallGroup save(Sunny sunny, User user, SmallGroupRegistPostDTO registDto) {
		
		SmallGroup smallGroup = new SmallGroup();
		
		smallGroup.setName(registDto.getName());
		smallGroup.setDescription(registDto.getDescription());
		smallGroup.setType(registDto.getType());
//		smallGroup.setPrivacy(registDto.getPrivacy());
		smallGroup.setCreator(user);
			

		SmallGroup parentSmallGroup = null; 
		
		if( registDto.getParentSmallGroupId() != null ){
			parentSmallGroup = smallGroupRepository.select(registDto.getParentSmallGroupId());
			smallGroup.setParent(parentSmallGroup);
		}
		
		SmallGroup prevSmallGroup = null;
		if( registDto.getPrevSmallGroupId() != null ){
			prevSmallGroup = smallGroupRepository.select(registDto.getPrevSmallGroupId());
			
			if( parentSmallGroup == null )
				parentSmallGroup = prevSmallGroup.getParent();
		}
		smallGroup = saveSmallGroupAndPermission(sunny.getSite(), parentSmallGroup, prevSmallGroup, smallGroup, smallGroup.getType());
		addUserToSmallGroup(user.getId(), smallGroup.getId(), true);
		return smallGroup;
	}

	@Override
	@Transactional
	public void inviteUsers(Sunny sunny, Long smallGroupId, User user,
			List<Long> userIds, boolean forceAdd) {
		
		
		SmallGroup smallGroup = smallGroupRepository.select(smallGroupId);
		int saveCount = 0, inactiveSaveCount = 0;
		// 부서이면서 운영자가 초대한 것일 때는 무조건 넣어버린다.
		if( forceAdd == true && smallGroup.getType() == SmallGroup.TYPE_DEPARTMENT && user.isAdmin() == true ){
			for( Long userId : userIds ){
				UserSmallGroupAccess guser = userSmallGroupAccessRepository.getUserSmallGroupAccessUser( smallGroupId,  userId );
				if( guser == null ){
					guser = new UserSmallGroupAccess();
					guser.setSmallGroup(smallGroup);
					guser.setInviteUser(user);
					guser.setUser(new User(userId));
					userSmallGroupAccessRepository.save(guser);
					saveCount++;
				}
				// 이미 초대된 사용자가 있을 경우엔 삭제한다. 
				SmallGroupInactiveUser gInactiveUser = smallGroupInactiveUserRepository.findRelation(smallGroupId, userId);
				if( gInactiveUser != null ){
					smallGroupInactiveUserRepository.delete(gInactiveUser);
					smallGroup.setInactiveUserCount( smallGroup.getInactiveUserCount() - 1 );
				}
			}
			smallGroup.setUserCount( smallGroup.getUserCount() + saveCount );
			
			
		}
		// 부서가 아니면 무조건 InactiveUser에 들어간다. 
		else{
			for( Long userId : userIds ){
				UserSmallGroupAccess guser = userSmallGroupAccessRepository.getUserSmallGroupAccessUser( smallGroupId,  userId );
				if( guser == null ){
					SmallGroupInactiveUser gInactiveUser = smallGroupInactiveUserRepository.findRelation( smallGroupId, userId);
					if( gInactiveUser == null ){
						gInactiveUser = new SmallGroupInactiveUser();
						gInactiveUser.setSmallGroup(smallGroup);
						gInactiveUser.setInviteUser(user);
						gInactiveUser.setUser(new User(userId));
						smallGroupInactiveUserRepository.save(gInactiveUser);	
						inactiveSaveCount++;
					}
				}
			}
			smallGroup.setInactiveUserCount( smallGroup.getInactiveUserCount() + inactiveSaveCount );
		}
		smallGroupRepository.update(smallGroup);
	}

	@Override
	@Transactional
	public void update(SmallGroup smallGroup) {
		SmallGroup persistentGroup = smallGroupRepository.select(smallGroup.getId());
		
		persistentGroup.setName( smallGroup.getName() );
//		persistentGroup.setJoinPower( smallGroup.getJoinPower() );
		persistentGroup.setDescription( smallGroup.getDescription() );
//		persistentGroup.setPrivacy( smallGroup.getPrivacy() );
		smallGroupRepository.update(persistentGroup);
		
	}

	@Override
	@Transactional
	public void addAccesses(Sunny sunny, Long smallGroupId, User user,
			List<ContentPermissionDTO> permissionDtos) {

		if( permissionDtos == null || permissionDtos.size() < 1 ){
			return;
		}
		
		SmallGroup smallGroup = new SmallGroup(smallGroupId);
		
		for( ContentPermissionDTO permission : permissionDtos ){
			
			
			SmallGroup saveSmallGroup = null;
			/*
			 * 사용자 한명 한명 선택했을 경우. 이때는 유저의 meSmallGroup 을 가져와서 넣어줘야한다.  
			 */
//			if( permission.getSmallGroupType() == null ){
//				User targetUser = userService.findById(permission.getId());
////				saveSmallGroup  = smallGroupService.generateUserDefineGroup(sunny, storyPost.getUser(), targetUser );
//				saveSmallGroup  = targetUser.getMySmallGroup();
//				smallGroupSmallGroupAccessService.save(sunny, smallGroup, saveSmallGroup, permission.getR(), permission.getW(), permission.getD(), true);
//				continue;
//			}

			/*
			 * 자식이 없으면 "하위부서포함"을 체크 안한것임.
			 */
			if( permission.getChildren() == null || permission.getChildren() == false ){
				saveSmallGroup = new SmallGroup(permission.getId());
				smallGroupSmallGroupAccessService.save( sunny, smallGroup, saveSmallGroup, true);
				continue;
			}
			
			saveSmallGroup = smallGroupRepository.select(permission.getId());
			
			List<Long> smallGroupIds = smallGroupRepository.getSmallGroupIdList(sunny.getSite().getId(), saveSmallGroup.getAbsolutePath());
			
			for( Long eachSmallGroupId : smallGroupIds ){
				System.out.println("하위 아이디 : " + eachSmallGroupId);
				smallGroupSmallGroupAccessService.save( sunny, smallGroup, new SmallGroup( eachSmallGroupId ), true);
			}
		}
		
	}

	@Override
	@Transactional
	public void plusUserCount(Long smallGroupId) {
		SmallGroup smallGroup = smallGroupRepository.findUniqByObject("id", smallGroupId);
		smallGroup.setUserCount( smallGroup.getUserCount() + 1 );
	}

	@Override
	@Transactional
	public void minusUserCount(Long smallGroupId) {
		SmallGroup smallGroup = smallGroupRepository.findUniqByObject("id", smallGroupId);
		smallGroup.setUserCount( smallGroup.getUserCount() - 1 );
	}

	@Override
	@Transactional
	public void plusInactiveUserCount(Long smallGroupId) {
		SmallGroup smallGroup = smallGroupRepository.findUniqByObject("id", smallGroupId);
		smallGroup.setInactiveUserCount( smallGroup.getInactiveUserCount() + 1 );
	}

	@Override
	@Transactional
	public void minusInactiveUserCount(Long smallGroupId) {
		SmallGroup smallGroup = smallGroupRepository.findUniqByObject("id", smallGroupId);
		smallGroup.setInactiveUserCount( smallGroup.getInactiveUserCount() + 1 );
	}

	@Override
	@Transactional
	public void plusEventCount(Long smallGroupId) {
		SmallGroup smallGroup = smallGroupRepository.findUniqByObject("id", smallGroupId);
		smallGroup.setEventCount( smallGroup.getEventCount() + 1 );
		smallGroup.setUpdateDate(new Date());
	}
	
	@Override
	@Transactional
	public void minusEventCount(Long smallGroupId) {
		SmallGroup smallGroup = smallGroupRepository.findUniqByObject("id", smallGroupId);
		smallGroup.setEventCount( smallGroup.getEventCount() - 1 );
		// TODO: 마지막 글 읽어와서 updateDate 에 넣는 작업
	}
	

	@Transactional
	private void plusChildrenDepartmentCount(Long id) {
		SmallGroup smallGroup = smallGroupRepository.findUniqByObject("id", id);
		smallGroup.setChildrenDepartmentCount( smallGroup.getChildrenDepartmentCount() + 1 );
	}

	@Transactional
	private void minusChildrenDepartmentCount(Long id) {
		SmallGroup smallGroup = smallGroupRepository.findUniqByObject("id", id);
		smallGroup.setChildrenDepartmentCount( smallGroup.getChildrenDepartmentCount() - 1 );
	}

	@Transactional
	private void plusChildrenProjectCount(Long id) {
		SmallGroup smallGroup = smallGroupRepository.findUniqByObject("id", id);
		smallGroup.setChildrenProjectCount( smallGroup.getChildrenProjectCount() + 1 );
	}
	
	@Transactional
	private void minusChildrenProjectCount(Long id) {
		SmallGroup smallGroup = smallGroupRepository.findUniqByObject("id", id);
		smallGroup.setChildrenProjectCount( smallGroup.getChildrenProjectCount() - 1 );
	}
	
	@Override
	@Transactional
	public void acceptUser(Sunny sunny, Long smallGroupId, User user,
			Long userId) {
		SmallGroup smallGroup = smallGroupRepository.select(smallGroupId);
		SmallGroupInactiveUser gInactiveUser = smallGroupInactiveUserRepository.findRelation( smallGroupId, userId);
		
		if( gInactiveUser == null ){
			return;
		}
		
		UserSmallGroupAccess guser = userSmallGroupAccessRepository.getUserSmallGroupAccessUser( smallGroupId,  userId );
		if( guser != null ){
			smallGroupInactiveUserRepository.delete(gInactiveUser);
			minusInactiveUserCount( smallGroup.getId());
			return;
		}
		
		guser = new UserSmallGroupAccess();
		guser.setSmallGroup( smallGroup );
		guser.setUser( gInactiveUser.getUser() );
		guser.setInviteUser( gInactiveUser.getInviteUser() );
		
		plusUserCount( smallGroup.getId() );
		userSmallGroupAccessRepository.save(guser);
		
		smallGroupInactiveUserRepository.delete(gInactiveUser);
		minusInactiveUserCount( smallGroup.getId());
		return;
		
	}

	@Override
	@Transactional
	public void joinAdmin(Sunny sunny, Long smallGroupId, User user, Long userId) {
		UserSmallGroupAccess guser = userSmallGroupAccessRepository.getUserSmallGroupAccessUser( smallGroupId,  userId );
		guser.setAdmin(true);
		
	}

	@Override
	@Transactional
	public void removeAdmin(Long userId, Long smallGroupId) {
		UserSmallGroupAccess guser = userSmallGroupAccessRepository.getUserSmallGroupAccessUser( smallGroupId,  userId );
		guser.setAdmin(false);
		
	}

	@Override
	@Transactional
	public void plusNoteCount(Long id) {
		SmallGroup smallGroup = smallGroupRepository.findUniqByObject("id", id);
		smallGroup.setNoteCount( smallGroup.getNoteCount() + 1 );		
	}

	@Override
	@Transactional
	public void minusNoteCount(Long id) {
		SmallGroup smallGroup = smallGroupRepository.findUniqByObject("id", id);
		smallGroup.setNoteCount( smallGroup.getNoteCount() - 1 );		
	}

	@Override
	@Transactional
	public void plusApprovalCount(Long id) {
		SmallGroup smallGroup = smallGroupRepository.findUniqByObject("id", id);
		smallGroup.setApprovalCount( smallGroup.getApprovalCount() + 1 );		
	}

	@Override
	@Transactional
	public void minusApprovalCount(Long id) {
		SmallGroup smallGroup = smallGroupRepository.findUniqByObject("id", id);
		smallGroup.setApprovalCount( smallGroup.getApprovalCount() - 1 );		
	}

	@Override
	@Transactional
	public List<SmallGroupIdName> getChildrenSmallGroupIdNames(Site site,
			Long parentId, int ... types) {
		
		List<SmallGroupIdName> smallGroupIdNames = smallGroupRepository.getChildrenSmallGroupIdNames(site, parentId, types);
		
		return smallGroupIdNames;
		
	}

	@Override
	@Transactional(readOnly = true)
	public List<SmallGroupTree> getTreeFirst(Site site, int smallGroupType) {
		return smallGroupRepository.getTreeFirst( site, smallGroupType );
	}
	
	@Transactional(readOnly = true)
	@Override
	public void fillChildren( Sunny sunny, SmallGroupTree parent ){
		List<SmallGroupTree> children = smallGroupRepository.getChildrenTree(sunny, parent);
		if( children != null && children.size() > 0 ){
			for( SmallGroupTree child : children ){
				fillChildren(sunny, child);
			}
		}
		parent.setChildren(children);
	}

	@Override
	@Transactional
	public void move(TreePutDTO treePutDTO) {
		
		System.out.println( treePutDTO.getId() + " <- 요놈이 옮겨진다. " );
		System.out.println( treePutDTO.getTargetId() + " 의 " + treePutDTO.getPosition() + " 로 ");

		
		SmallGroup smallGroup = smallGroupRepository.select( treePutDTO.getId() );
		
		// 문제는 자식들이다. 자식들을 어떻게 해야하지??? 우선은 하나씩만 이동한다. 
		smallGroupRepository.updateThreadSeqMinusOne(smallGroup.getThread(), smallGroup.getThreadSeq());
		
		switch( treePutDTO.getPosition() ){
		
		// 맨 처음에 넣을 때 즉, 부모가 없을 때 
		case "before":
			smallGroup.setWrapAbsolutePath( smallGroup.getId() );
			smallGroup.setThread(smallGroup.getId());
			smallGroup.setThreadSeq(0);
			smallGroup.setDepth(0);
			smallGroup.setParent(null);
			smallGroup.setParentSmallGroupId(null);
			break;
		
		// 중간에 넣을 때
		case "after":
			SmallGroup prevSmallGroup = smallGroupRepository.select( treePutDTO.getTargetId() );
			
			// 부모가 있을경우. 즉, 형제일 경우.
			if( prevSmallGroup.getParent() != null ){
				smallGroup.setAbsolutePath( prevSmallGroup.getParent().getAbsolutePath(), smallGroup.getId());
				smallGroupRepository.updateThreadSeqPlugOne(prevSmallGroup.getThread(), prevSmallGroup.getThreadSeq());
				smallGroup.setThread( prevSmallGroup.getThread() );
				smallGroup.setThreadSeq( prevSmallGroup.getThreadSeq() + 1 );
				smallGroup.setDepth(prevSmallGroup.getDepth());
				smallGroup.setParent(prevSmallGroup.getParent());
				smallGroup.setParentSmallGroupId(prevSmallGroup.getParent().getId());
			}else{
				smallGroup.setWrapAbsolutePath(smallGroup.getId());
				smallGroup.setThread( smallGroup.getId() );
				smallGroup.setThreadSeq( 0 );
				smallGroup.setParent(null);
				smallGroup.setDepth(0);
				smallGroup.setParent(null);
				smallGroup.setParentSmallGroupId(null);
			}
			
			break;
			
		// 어느 자식인데, 처음에 넣을 때
		case "inside":
			
			SmallGroup parentSmallGroup = smallGroupRepository.select( treePutDTO.getTargetId() );
			smallGroup.setAbsolutePath( parentSmallGroup.getAbsolutePath(), smallGroup.getId());
			smallGroup.setThread( parentSmallGroup.getThread() );
			smallGroup.setParent( parentSmallGroup );
			smallGroupRepository.updateThreadSeqPlugOne(parentSmallGroup.getThread(), parentSmallGroup.getThreadSeq());
			smallGroup.setThreadSeq( parentSmallGroup.getThreadSeq() + 1 );
			smallGroup.setParent(parentSmallGroup);
			smallGroup.setParentSmallGroupId(parentSmallGroup.getId());
			smallGroup.setDepth( parentSmallGroup.getDepth() + 1 );
			break;

		default:
			throw new SimpleSunnyException();
		}
		
		smallGroupRepository.update(smallGroup);
		
		
	}

	@Override
	@Transactional
	public void update(Long smallGroupId,
			UpdateSmallGroupDTO updateSmallGroupDto) {
		SmallGroup smallGroup = smallGroupRepository.select(smallGroupId);
		
		
		if( updateSmallGroupDto.getName() != null ){
			smallGroup.setName(updateSmallGroupDto.getName());	
		}
		
		if( updateSmallGroupDto.getDescription() != null ){
			smallGroup.setDescription( updateSmallGroupDto.getDescription() );
		}
		
		if( updateSmallGroupDto.getStatus() != null ){
			smallGroup.setStatus( updateSmallGroupDto.getStatus() );	
		}
		
		
		if( updateSmallGroupDto.getType() != null ){
			smallGroup.setType( updateSmallGroupDto.getType() );
		}
		
//		if( updateSmallGroupDto.getPrivacy() != null ){
//			smallGroup.setPrivacy( updateSmallGroupDto.getPrivacy() );
//		}
	
		smallGroupRepository.update(smallGroup);
	}

	@Override
	@Transactional
	public void remove(Sunny sunny, Long smallGroupId) {
		SmallGroup smallGroup = smallGroupRepository.select(smallGroupId);
		smallGroupRepository.delete(smallGroup);
	}


}