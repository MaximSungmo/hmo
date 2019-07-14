package kr.co.sunnyvale.sunny.service.impl;

import java.util.ArrayList;
import java.util.List;

import kr.co.sunnyvale.sunny.domain.AuthToken;
import kr.co.sunnyvale.sunny.domain.Media;
import kr.co.sunnyvale.sunny.domain.Menu;
import kr.co.sunnyvale.sunny.domain.Role;
import kr.co.sunnyvale.sunny.domain.Site;
import kr.co.sunnyvale.sunny.domain.SiteInactive;
import kr.co.sunnyvale.sunny.domain.SiteInactiveUser;
import kr.co.sunnyvale.sunny.domain.SiteMenu;
import kr.co.sunnyvale.sunny.domain.SmallGroup;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.UserInfo;
import kr.co.sunnyvale.sunny.domain.UserSmallGroupAccess;
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
import kr.co.sunnyvale.sunny.exception.AlreadyJoinedException;
import kr.co.sunnyvale.sunny.exception.NoExistsTokenException;
import kr.co.sunnyvale.sunny.exception.NoExistsUserException;
import kr.co.sunnyvale.sunny.exception.NotCorrectPasswordException;
import kr.co.sunnyvale.sunny.exception.SimpleSunnyException;
import kr.co.sunnyvale.sunny.repository.hibernate.AuthTokenRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.MediaRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.MenuRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.RoleRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.SequenceRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.SiteInactiveRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.SiteInactiveUserRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.SiteMenuRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.SiteRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.SmallGroupRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.SmallGroupSmallGroupAccessRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.UserInfoRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.UserRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.UserSmallGroupAccessRepository;
import kr.co.sunnyvale.sunny.service.AuthTokenService;
import kr.co.sunnyvale.sunny.service.LoginInfoService;
import kr.co.sunnyvale.sunny.service.MailService;
import kr.co.sunnyvale.sunny.service.SiteService;
import kr.co.sunnyvale.sunny.service.SmallGroupService;
import kr.co.sunnyvale.sunny.service.TagService;
import kr.co.sunnyvale.sunny.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

@Service( value="userService" )
@Transactional
public class UserServiceImpl implements UserService {
	@Autowired
	private AuthTokenService authTokenService; 
	
	@Autowired
	private SiteRepository siteRepository;
	
	@Autowired
	private MediaRepository mediaRepository;
	
	@Autowired
	private AuthTokenRepository authTokenRepository;
	
	@Autowired
	private SmallGroupRepository smallGroupRepository; 
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserInfoRepository userInfoRepository; 
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private SequenceRepository sequenceRepository;
	
	@Autowired
	private MessageSource messageSource;

	@Autowired
	LoginInfoService loginInfoService;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private SiteService siteService;
	
	@Autowired
	private SmallGroupService smallGroupService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserSmallGroupAccessRepository userSmallGroupAccessRepository;
	
	@Autowired
	private SmallGroupSmallGroupAccessRepository smallGroupSmallGroupAccessRepository;
	
	@Autowired
	private SiteMenuRepository siteMenuRepository;
	
	@Autowired
	private MenuRepository menuRepository;
	
	@Autowired
	private TagService tagService;
	
	@Autowired
	private SiteInactiveUserService siteInactiveUserService;
	
	@Autowired
	private SiteInactiveUserRepository siteInactiveUserRepository;
	
	@Autowired
	private SiteInactiveRepository siteInactiveRepository;
	
	@Transactional
	public void changePassword( Long userId, PasswordPost passwordPost ) {
		
		User user = userRepository.select( userId );

		// 관리자 부분에선 현재 패스워드를 보내지 않는다 .
		if( passwordPost.getCurrentPassword() != null && !passwordPost.getCurrentPassword().isEmpty() ){
			String currentPassword = passwordEncoder.encodePassword( passwordPost.getCurrentPassword(), user.getSalt() );	
			
			if( !user.getPassword().equals(currentPassword) ){
				throw new NotCorrectPasswordException();
			}
		}
		String changedPassword = passwordEncoder.encodePassword( passwordPost.getNewPassword(), user.getSalt() );
		user.setPassword( changedPassword );
	}

	@Transactional
	@CacheEvict( value = "sunnyUserCache", key="#id")
	public void deleteUser( Long id ){
		userRepository.delete( userRepository.select(id) );
		
	}

	@Override
	@Transactional
	@CacheEvict( value = "sunnyUserCache", key="#id")
	public void update(Long id, User user) {
		userRepository.update(user);
	}
	
	@Transactional(readOnly = true)
	public Object getColumnFromObject( String returnColumn, String whereColumn, Object whereParameter ) {
		return (Object) userRepository.findColumnByObject(returnColumn, whereColumn, whereParameter);
	}
	
	@Transactional(readOnly = true)
	public String getUserIdFromEmail( String email ) {
		return (String) userRepository.findColumnByObject( "id", "email", email );
	}
	

	@Transactional(readOnly = true)
	public Page<User> getAllUser(Integer page, Integer defaultPageSize){
		return userRepository.getAll( page, defaultPageSize);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean existsEmail(String email) {
		
		if( email == null ) {
			return false;
		}
		
		return userRepository.existsEmail(email);
	}
	
	@Override
	@Transactional
	public AuthToken registAdmin( AdminJoinDTO adminJoinDto ) {
		
		SiteInactive siteInactive = adminJoinDto.parseToSiteInactive();
		
		List<SiteInactiveUser> existSiteInactiveUsers = siteInactiveUserRepository.findListByObject( "email", adminJoinDto.getUserEmail(), null );
		
		for( SiteInactiveUser siteInactiveUser : existSiteInactiveUsers ) {
			siteInactiveUserRepository.delete(siteInactiveUser);
		}

		//
		SiteInactiveUser siteInactiveUser = adminJoinDto.parseToSiteInactiveUser();
		siteInactiveUser.setAdmin(true);
		if( siteInactive == null || siteInactiveUser == null ) {
			throw new SimpleSunnyException();
		}		
		siteInactiveRepository.save( siteInactive );

		//
		siteInactiveUser.setSiteInactive( siteInactive );
		siteInactiveUser.setSalt( siteInactiveUser.getSalt() );
		siteInactiveUser.setPassword( passwordEncoder.encodePassword( siteInactiveUser.getPassword(), siteInactiveUser.getSalt() ) );
		siteInactiveUserRepository.save( siteInactiveUser );

		//
		// install-version 변경
		// mailService.sendConfirmAdminMail( siteInactive,  siteInactiveUser );
		// 대신에 바로 AuthToken을 생성한다.
		//
		authTokenRepository.removeAllValues( siteInactiveUser.getId(), AuthToken.TYPE_ACTIVATE_ADMIN );
		AuthToken authToken = new AuthToken( siteInactiveUser, AuthToken.TYPE_ACTIVATE_ADMIN );
		authToken.setSiteInactive( siteInactive );
		authToken.setSiteInactiveUser( siteInactiveUser );
		authTokenRepository.save( authToken );
		
		return authToken;
	}
	
	@Override
	@Transactional
	public User confirmAdmin( String authTokenValue, Long userId ) {

		if( authTokenValue == null || userId == null )
			throw new SimpleSunnyException();
		
		SiteInactiveUser siteInactiveUser = siteInactiveUserRepository.findUniqByObject( "id", userId );
		if( siteInactiveUser == null ) {
			throw new NoExistsUserException();
		}
		
		boolean isExistEmail = existsEmail(siteInactiveUser.getEmail());
		if( siteInactiveUser.getStatus() == siteInactiveUser.STATUS_COMPLETE || isExistEmail ){
			throw new AlreadyJoinedException();
		}

		AuthToken correctKey = authTokenRepository.getCorrectAuthToken(new SiteInactiveUser(userId), authTokenValue, AuthToken.TYPE_ACTIVATE_ADMIN);
		
		// 토큰이 없음
		if( correctKey == null ) {
			throw new NoExistsTokenException();
		}
		
		// 토큰 주인과 파라미터 주인이 맞지 않음
		if( siteInactiveUser.getId() != correctKey.getSiteInactiveUser().getId() ) {
			throw new SimpleSunnyException();
		}

		SiteInactive siteInactive = correctKey.getSiteInactive();
		
		Site site = siteInactive.parseToSite();
		
		User user = siteInactiveUser.parseToUser();
		
		if( site == null || user == null ) {
			throw new SimpleSunnyException();
		}	
		
		// CDN 통합
		site.setCdn( "" );
		
		siteRepository.save(site);
		List<Menu> menus = menuRepository.getAll(null);
		for( Menu menu : menus ){
			SiteMenu siteMenu = new SiteMenu();
			siteMenu.setSite(site);
			siteMenu.setMenu(menu);
			siteMenu.setOrdering(menu.getOrdering());
			siteMenuRepository.save(siteMenu);
		}

		/*
		 * 사이트에 대한 최초 롤들을 생성
		 */
		roleRepository.checkAndGenerateDefault();
		
		
//		user.setDefaultSite(site);
		user.setSite(site);
		user.setAdmin(true);

		changeUserToAdmin(user);
		
		userRepository.save(user);
		
		/*
		 * 검증 완료. 컨펌 처리
		 */
		
		authTokenRepository.removeAllValues(new SiteInactiveUser(userId), AuthToken.TYPE_ACTIVATE_ADMIN);
		

		//나만의 스몰그룹을 생성
		SmallGroup mySmallGroup = smallGroupService.generateMySmallGroup(site, user);
		//친구 스몰그룹을 생성.
		SmallGroup friendSmallGroup = smallGroupService.generateFriendSmallGroup(site, user);
		// 광장생성.
		SmallGroup generatedSmallGroup = setLobbySmallGroupAndAccessGroupForAdmin(site, user);

		site = siteRepository.select(site.getId());
		site.setLobbySmallGroup(generatedSmallGroup);	
		siteRepository.update(site);
		
		tagService.generateDefaultLobbyTags( generatedSmallGroup.getId() );
		
		user = userRepository.select(user.getId());
		user.setMySmallGroup(mySmallGroup);
		user.setFriendSmallGroup(friendSmallGroup);
		userRepository.update(user);
		
		siteInactiveUser.setStatus(SiteInactiveUser.STATUS_COMPLETE);
		
//		siteInactiveUserRepository.delete(siteInactiveUser);
		
		return user;
	}
	

	/**
	 * 첫 관리자 생성 시( 모든 사이트 생성시 딱 한번만 실행됨 ) 광장 등 기본 그룹을 사이트에 저장한다.  
	 * @param site
	 * @param user
	 */
	@Transactional
	private SmallGroup setLobbySmallGroupAndAccessGroupForAdmin(Site site, User user) {
		
		// 기본 그룹 생성(광장)
		SmallGroup defaultSmallGroup = smallGroupService.generateLobbySmallGroup(site, user);
		
		site = siteRepository.select(site.getId());
		site.setLobbySmallGroup(defaultSmallGroup);
		siteRepository.update(site);
		
		return defaultSmallGroup;
	}

	
	@Override
	@Transactional
	@Cacheable(	value="sunnyUserCache", key="#id")
	public User findById(Long id) {
		User user = userRepository.select(id);
		if( user.getInfo() == null ){
			UserInfo info = new UserInfo();
			info.setUser(user);
			userInfoRepository.save(info);
			user.setInfo(info);
		}
		return user;
	}
	
	@Override
	@Cacheable(	value="sunnyUserCache", key="#email")
	public User findByEmail(String email) {
		
		return userRepository.findByEmail(email);
	}
	
	@Override
	@Transactional
	@CacheEvict( value = "sunnyUserCache", key="#id")
	public User findPersistentUserById(  Long id ) {
		User user = userRepository.select(id);
		if( user.getInfo() == null ){
			UserInfo info = new UserInfo();
			info.setUser(user);
			userInfoRepository.save(info);
		}
		return user;
	}

	@Override
	public boolean checkEqualsPassword(User user, String currentPassword) {
		String repositoryPassword = user.getPassword();
		String encodedPassword = passwordEncoder.encodePassword( currentPassword, user.getSalt() ) ;
		return repositoryPassword.equals(encodedPassword);
	}
	

	/**
	 * 아무 사용자나 Security 관련된 Role 을  Super 와  Admin 으로 격상시킨다. *주의* 이전의 Role 관계 들은 모두 사라짐
	 * @param user
	 */
	private void changeUserToAdmin(User user){
		
		Role superRole = new Role(Role.ID_SUPER_ADMIN, Role.ROLE_SUPER_ADMIN, null);
		Role adminRole = new Role(Role.ID_ADMIN, Role.ROLE_ADMIN, null);
		Role userRole = new Role(Role.ID_USER, Role.ROLE_USER, null);
		
		List<Role> roles = Lists.newArrayList();
		roles.add(superRole);
		roles.add(adminRole);
		roles.add(userRole);
		
		user.setRoles(roles);
		user.setAdmin(true);
	}


	/**
	 * 아무 사용자나 Security 관련된 Role 을 User 로 낮춘다. *주의* 이전의 Role 들은 모두 사라짐
	 * @param user
	 */
	private void changeAdminToUser(User user){
		Role userRole = new Role(Role.ID_USER, Role.ROLE_USER, null);
		List<Role> roles = Lists.newArrayList();
		roles.add(userRole);
		user.setRoles(roles);
		user.setAdmin(false);
	}
	
	
	/**
	 * 롤을 다 없애버림
	 * @param user
	 */
	private void changeUserToAnonymous(User user) {
		user.setRoles(null);
	}
	
	/**
	 * 퇴사 처리된 사용자의 롤은 User이다.
	 * @param user
	 */
	private void changeUserToRetirement(User user) {
		Role userRole = new Role(Role.ID_USER, Role.ROLE_USER, null);
		List<Role> roles = Lists.newArrayList();
		roles.add(userRole);
		user.setRoles(roles);
		user.setAdmin(false);
	}	
	
//	@Override
//	public Page<UserSmallGroupAccess> getUserSmallGroupAccessList(Long smallGroupId, boolean descendantFlag,String searchName,  Integer pageNum, int pageSize) {
//		
//		//smallGroupId에 해당하는 SmallGroup의 경로를 구하고, descendantFlag가 true라면 like검색을 그렇지 않을 경우는 path와 일치하는 SmallGroup을 한건 읽어온다.
//		SmallGroup smallGroup = smallGroupRepository.select(smallGroupId);
//		String smallGroupStartPath = smallGroup.getAbsolutePath();
//		List<Long> smallGroupIdList = new ArrayList<Long>();
//		if(descendantFlag){
//			smallGroupIdList = smallGroupRepository.getSmallGroupIdList(smallGroup.getSite().getId(), smallGroupStartPath);
//		}else{
//			SmallGroup findSmallGroup = smallGroup;
//			if(findSmallGroup != null){
//				smallGroupIdList.add(findSmallGroup.getId());
//			}
//		}
//		
//		Page<UserSmallGroupAccess> list = userSmallGroupAccessRepository.getUserSmallGroupAccessList(smallGroupIdList, searchName, pageNum, pageSize);
//		return list;
//	}	

	@Override
	public Page<User> getUserList(Long smallGroupId, boolean descendantFlag,String searchName,  Integer pageNum, int pageSize) {
		
		//smallGroupId에 해당하는 SmallGroup의 경로를 구하고, descendantFlag가 true라면 like검색을 그렇지 않을 경우는 path와 일치하는 SmallGroup을 한건 읽어온다.
		SmallGroup smallGroup = smallGroupRepository.select(smallGroupId);
		String smallGroupStartPath = smallGroup.getAbsolutePath();
		List<Long> smallGroupIdList = new ArrayList<Long>();
		if(descendantFlag){
			smallGroupIdList = smallGroupRepository.getSmallGroupIdList(smallGroup.getSite().getId(), smallGroupStartPath);
		}else{
			SmallGroup findSmallGroup = smallGroup;
			if(findSmallGroup != null){
				smallGroupIdList.add(findSmallGroup.getId());
			}
		}
		Page<User> list = userRepository.getUserList(smallGroupIdList, searchName, pageNum, pageSize);
		return list;
	}


	
	@Override
	public List<RelationDTO> getUserList(Long siteId, Long userId, String searchName,
			Stream stream) {
		return userRepository.getUserList(siteId, userId, searchName, stream);
		
	}
	

	@Override
	public Page<User> getUserList(Long siteId, Long userId,
			String queryName, Integer[] status, Integer page, int pageSize) {
		Page<User> pagedResult = userRepository.getUserList(siteId, userId ,queryName, status, page, pageSize);
		System.out.println("사용자 갖고오기 성공");
		return pagedResult;
		
	}
	
	
	@Override
	public int getUserCount(Long siteId, String searchName) {
		int count = userRepository.getUserCount(siteId, searchName);
		return count;
	}

	@Override
	public int getUserCount(Long smallGroupId, boolean descendantFlag,
			String searchName) {
		SmallGroup smallGroup = smallGroupRepository.select(smallGroupId);
		String smallGroupStartPath = smallGroup.getAbsolutePath();
		
		List<Long> smallGroupIdList = new ArrayList<Long>();
		if(descendantFlag){
			smallGroupIdList = smallGroupRepository.getSmallGroupIdList(smallGroup.getSite().getId(), smallGroupStartPath);
		}else{
			SmallGroup findSmallGroup = smallGroupRepository.getSmallGroup(smallGroup.getSite().getId(), smallGroupStartPath);
			if(findSmallGroup != null){
				smallGroupIdList.add(findSmallGroup.getId());
			}
		}
		int count = userRepository.getUserCount(smallGroupIdList, searchName).intValue();
		return count;
	}

	@Override
	@Transactional
	public void deleteUser( Long userId, boolean realDelete ) {
		
		List<UserSmallGroupAccess> userSmallGroupAccesses = userSmallGroupAccessRepository.findListByObject("inviteUser.id", userId, null);
		for( UserSmallGroupAccess usga : userSmallGroupAccesses ){
			usga.setInviteUser(null);
			userSmallGroupAccessRepository.update(usga);
		}
		
		List<SmallGroup> createdSmallGroups = smallGroupRepository.findListByObject("creator.id", userId, null);
		for( SmallGroup smallGroup : createdSmallGroups){
			smallGroup.setCreator(null);
			smallGroupRepository.update(smallGroup);
		}
		
		userRepository.deleteUser(userId, realDelete);
	}



//	@Override
//	@CacheEvict( value = "sunnyUserCache", key="#id")
//	public void updateUser(Site site, Long id, SecurityUser securityUser, UserRegistPostDTO userRegistDto) {
//		
//		User user = userRepository.select(id);
//		if( user == null ){
//			throw new SimpleSunnyException();
//		}
//		
//		user.setName( userRegistDto.getUserName() );
//		securityUser.setName( user.getName() );
//		
//		if( userRegistDto.getUserPassword() != null && userRegistDto.getUserPassword().isEmpty() == false ){
//			user.setPassword( passwordEncoder.encodePassword( userRegistDto.getUserPassword(), user.getSalt()));
//		}
//		
//		user.setJobTitle1( userRegistDto.getJobTitle1() );
//		user.setJobTitle2( userRegistDto.getJobTitle2() );
//		user.setJobTitle3( userRegistDto.getJobTitle3() );
//		user.setStatus( userRegistDto.getUserStatus());
//		user.setDescription( userRegistDto.getUserDescription() );
//		
//		SiteUser siteUser = siteUserRepository.findBySiteAndUser(site.getId(), user.getId());
//		boolean isChangedRoles = false;
//		if( userRegistDto.getUserStatus() == User.STATUS_LEAVE ){
//			changeUserToAnonymous(siteUser);
//			isChangedRoles = true;
//		}else if( userRegistDto.isAdmin() == true && siteUser.isAdmin() == false ){
//			changeUserToAdmin(siteUser);
//			
//			Admin admin = new Admin();
//			admin.setUser(user);
//			admin.setSite(site);
//			admin.setEmail(user.getEmail());
//			adminRepository.save(admin);
//			
//			isChangedRoles = true;
//		}else if( userRegistDto.isAdmin() == false && siteUser.isAdmin() == true ){
//			changeAdminToUser(siteUser);
//			Admin admin = adminRepository.findBySiteAndUser(site.getId(), user.getId());
//			adminRepository.delete(admin);
//			
//			isChangedRoles = true;
//		}
//		
//		if( isChangedRoles == true && securityUser.getUserId().equals(user.getId() )){
//			Collection<GrantedAuthority> roles = SecurityUtils.parseSecurityRoles(siteUser.getRoles());
//			securityUser.setAuthorities(roles);	
//		}
//		
//		
//		userRepository.update(user);
//	}

	@Override
	public List<UserDTO> getMatchUsers(Site site, Long userId, String key,
			Stream stream) {
		List<UserDTO> userDtos = userRepository.findMatchUsersByName(site, key, stream);
		
		for( UserDTO userDto : userDtos ){
			List<String> departs = smallGroupService.getDepartmentStrings(site, userId);
			userDto.setDepartmentStrings(departs);
		}
		return userDtos; 
	}

	@Override
	@Transactional
	public void setSmallGroup(Long siteId, Long userId, Long smallGroupId) {
		User user = userRepository.select(userId);
		SmallGroup smallGroup = smallGroupRepository.select(smallGroupId);
		
		if(user == null || smallGroup == null){
			throw new SimpleSunnyException();
		}
		
//		if(!siteId.equals(user.getSite().getId()) || !siteId.equals(smallGroup.getSite().getId())){
//			throw new SimpleSunnyException();
//		}

		UserSmallGroupAccess usga = userSmallGroupAccessRepository.select(userId, smallGroupId);
		if(usga == null){
			UserSmallGroupAccess usga3 = new UserSmallGroupAccess();
			usga3.setSmallGroup(smallGroup);
			usga3.setUser(user);
			userSmallGroupAccessRepository.save(usga3);		
		}

	}

	@Override
	@Transactional
	public void delSmallGroup(Long siteId, Long userId, Long smallGroupId, boolean deleteContentPermission) {
		User user = userRepository.select(userId);
		SmallGroup smallGroup = smallGroupRepository.select(smallGroupId);
		
		if(user == null || smallGroup == null){
			throw new SimpleSunnyException();
		}
		
//		if(!siteId.equals(user.getSite().getId()) || !siteId.equals(smallGroup.getSite().getId())){
//			throw new SimpleSunnyException();
//		}
//		
		userSmallGroupAccessRepository.delete(userId, smallGroupId);
		
		//TODO smallGroupId 이하의 모든 부서에서 userId가 작성한 Type.ME 인 것을 모두 삭제한다.
	}

	@Override
	@CacheEvict( value = "sunnyUserCache", key="#id")
	public void updateUser(Long id, UpdateProfileUser updateUser, SecurityUser securityUser) {
		User user = userRepository.select(id);
		
		if( updateUser.getName() != null && updateUser.getName().isEmpty() == false ){
			user.setName( updateUser.getName().trim() );
			securityUser.setName(updateUser.getName().trim());
		}
		
//		if( updateUser.getEmail() != null ){
//			user.setEmail( updateUser.getEmail() );
//			securityUser.setEmail( updateUser.getEmail() );
//		}
//		
//		if( updateUser.getStatus() != null ){
//			user.setStatus( updateUser.getStatus() );
//			securityUser.setStatus(updateUser.getStatus());
//		}
		
		if( updateUser.getPhone() != null  ){
			user.setPhone( updateUser.getPhone().trim() );
		}
		
		if( updateUser.getInnercall() != null  ){
			user.setInnercall( updateUser.getInnercall().trim() );
		}
		
		if( updateUser.getStatusMessage() != null ){
			user.setStatusMessage(updateUser.getStatusMessage().trim());
			securityUser.setStatusMessage(updateUser.getStatusMessage().trim());
		}
		
		if( updateUser.getJobTitle1() != null ){
			user.setJobTitle1( updateUser.getJobTitle1().trim() );	
		}
		if( updateUser.getJobTitle2() != null ){
			user.setJobTitle2( updateUser.getJobTitle2().trim() );	
		}
		if( updateUser.getJobTitle3() != null ){
			user.setJobTitle3( updateUser.getJobTitle3().trim() );	
		}
		
		if( updateUser.getProfilePicId() != null ){

			Media media = mediaRepository.select(updateUser.getProfilePicId());
			if( media == null ){
				throw new SimpleSunnyException();
			}
			media.setUser(user);
			media.setUsed(true);
			
			user.setProfilePic( "" + media.getSmallPath() );
			user.setProfilePhoto( media );
			userRepository.update(user);
			securityUser.setProfilePic( user.getProfilePic() );
			
		}
	}



	@Override
	public Page<User> getUserList(Sunny sunny, List<Long> smallGroupIds,
			Integer queryType, String queryName, Integer[] status, String range, String ordering, Long excludeUserId, Boolean onlyAdmin, Integer page,
			int pageSize) {
		
		Page<User> pagedUser = userRepository.getUserList(sunny, smallGroupIds, queryType, queryName, status, range, ordering, excludeUserId, onlyAdmin, page, pageSize);
		
		
		return pagedUser;
	}

	@Override
	public Page<User> getFavoriteUsers(Sunny sunny, User user, Integer page,
			int pageSize) {
		
		Page<User> pagedUser = userRepository.getFavoriteUsers(sunny, user, page, pageSize);
		
		return pagedUser;
	}
	
	@Override
	@Transactional
	@CacheEvict( value = "sunnyUserCache", key="#id")
	public void plusFriendCount(Long id) {
		
		UserInfo userInfo = findById(id).getInfo();
		userInfo.setFriendCount( userInfo.getFriendCount() + 1 );
	}

	@Override
	@Transactional
	@CacheEvict( value = "sunnyUserCache", key="#id")
	public void minusFriendCount(Long id) {
		UserInfo userInfo = findById(id).getInfo();
		userInfo.setFriendCount( userInfo.getFriendCount() - 1 );
	}
	
	@Override
	@Transactional
	@CacheEvict( value = "sunnyUserCache", key="#id")
	public void plusDepartmentCount(Long id) {
		UserInfo userInfo = findById(id).getInfo();
		userInfo.setDepartmentCount( userInfo.getDepartmentCount() + 1 );
	}

	@Override
	@Transactional
	@CacheEvict( value = "sunnyUserCache", key="#id")
	public void minusDepartmentCount(Long id) {
		UserInfo userInfo = findById(id).getInfo();
		userInfo.setDepartmentCount( userInfo.getDepartmentCount() - 1 );
	}

	@Override
	@Transactional
	@CacheEvict( value = "sunnyUserCache", key="#id")
	public void plusGroupCount(Long id) {
		UserInfo userInfo = findById(id).getInfo();
		userInfo.setGroupCount( userInfo.getGroupCount() + 1 );
	}

	@Override
	@Transactional
	@CacheEvict( value = "sunnyUserCache", key="#id")
	public void minusGroupCount(Long id) {
		UserInfo userInfo = findById(id).getInfo();
		System.out.println("카운트 : " + userInfo.getGroupCount());
		userInfo.setGroupCount( userInfo.getGroupCount() - 1 );
	}

	@Override
	@Transactional
	@CacheEvict( value = "sunnyUserCache", key="#id")
	public void plusProjectCount(Long id) {
		UserInfo userInfo = findById(id).getInfo();
		userInfo.setProjectCount( userInfo.getProjectCount() + 1 );
	}

	@Override
	@Transactional
	@CacheEvict( value = "sunnyUserCache", key="#id")
	public void minusProjectCount(Long id) {
		UserInfo userInfo = findById(id).getInfo();
		userInfo.setProjectCount( userInfo.getProjectCount() - 1 );
	}

	@Override
	public Page<User> getPagedNotInSmallGroupUserList(Sunny sunny,
			Long smallGroupId, Long userId,
			String queryName, Integer page, int pageSize) {
		
		Page<User> pagedResult = userRepository.getPagedNotInSmallGroupUserList(sunny, smallGroupId, userId, queryName, page, pageSize);
		
		return pagedResult;
	}

	@Override
	@Transactional
	public void save(User user) {
		userRepository.save(user);
	}

	@Override
	@Transactional
	public void inviteUser( Sunny sunny, User inviteUser, UserInvitePostDTO userInviteDto ) {
		
		List<SiteInactiveUser> existSiteInactiveUsers = siteInactiveUserRepository.findListByObject("email", userInviteDto.getEmail(), null);
		
		for( SiteInactiveUser siteInactiveUser : existSiteInactiveUsers ){
			siteInactiveUserRepository.delete(siteInactiveUser);
		}
		
		SiteInactiveUser siteInactiveUser = new SiteInactiveUser();
		siteInactiveUser.setType(SiteInactiveUser.TYPE_INVITE);
		siteInactiveUser.setJobTitle1(userInviteDto.getJobTitle1());
		siteInactiveUser.setJobTitle2(userInviteDto.getJobTitle2());
		siteInactiveUser.setJobTitle3(userInviteDto.getJobTitle3());
		siteInactiveUser.setInviteMessage(userInviteDto.getInviteMessage());
		siteInactiveUser.setEmail(userInviteDto.getEmail());
		siteInactiveUser.setName(userInviteDto.getName());
		siteInactiveUser.setSite( sunny.getSite() );
		
		siteInactiveUser.setInviteUser( inviteUser );
		siteInactiveUserService.save( siteInactiveUser );
		
		mailService.sendInviteSiteInactiveUserMail( sunny, siteInactiveUser );
	}


	@Override
	public void update( Long userId, UpdateUserFromAdminDTO updateSiteUserFromAdminDto ) {
		
		User user = userRepository.select(userId);
		
//		if( updateSiteUserFromAdminDto.getJobTitle1() != null ){
		user.setJobTitle1( updateSiteUserFromAdminDto.getJobTitle1() );
//		}
//		if( updateSiteUserFromAdminDto.getJobTitle2() != null ){
		user.setJobTitle2( updateSiteUserFromAdminDto.getJobTitle2() );
//		}
//		if( updateSiteUserFromAdminDto.getJobTitle3() != null ){
		user.setJobTitle3( updateSiteUserFromAdminDto.getJobTitle3() );
//		}
		
		user.setStatus( updateSiteUserFromAdminDto.getStatus());
			
		
		// admin true -> false 로 바뀔 때
//		if( (updateSiteUserFromAdminDto.getAdmin() == null || updateSiteUserFromAdminDto.getAdmin() == false ) ){
//			user.setAdmin(false);
//		}else if( updateSiteUserFromAdminDto.getAdmin() != null && updateSiteUserFromAdminDto.getAdmin() == true ){
//			user.setAdmin( true );
//		}
		
//		boolean isChangedRoles = false;
		if( updateSiteUserFromAdminDto.getStatus() == User.STATUS_LEAVE ){
//			changeUserToAnonymous(user);
//			isChangedRoles = true;
			// 퇴사 처리 롤은 기본 유저로....
			changeUserToRetirement( user );
		}else if( user.isAdmin() == true && (updateSiteUserFromAdminDto.getAdmin() == null || updateSiteUserFromAdminDto.getAdmin() == false ) ){
			changeAdminToUser(user);
//			isChangedRoles = true;
			
		}else if( user.isAdmin() == false && ( updateSiteUserFromAdminDto.getAdmin() != null && updateSiteUserFromAdminDto.getAdmin() == true)){
			changeUserToAdmin(user);
//			user.setAdmin(false);
//			isChangedRoles = true;
		}
		
//		if( updateSiteUserFromAdminDto.getAdminComment() != null ){
			user.setAdminComment( updateSiteUserFromAdminDto.getAdminComment() );
//		}
		userRepository.update(user);
	}

	@Override
	public boolean isJoined(Long siteId, Long userId) {
		return userRepository.isJoined(siteId, userId);
	}

	@Override
	@Transactional
	public void activate(String key, Long siteInactiveUserId) {
		
		SiteInactiveUser siteInactiveUser = siteInactiveUserService.findById(siteInactiveUserId);
		
		AuthToken authToken = authTokenService.getCorrectAuthToken(siteInactiveUser, key, AuthToken.TYPE_SITE_INACTIVE_USER_CONFIRM);
		if( authToken == null ){
			throw new SimpleSunnyException();
		}
		
		Site site = siteRepository.select(siteInactiveUser.getSite().getId());
		
		User user = siteInactiveUser.parseToUser();

		List<Role> roles = Lists.newArrayList();
		
		Role role = new Role(Role.ID_USER);
		roles.add(role);
		user.setRoles(roles);
		
		userRepository.save(user);
		
		//나만의 스몰그룹을 생성
		SmallGroup mySmallGroup = smallGroupService.generateMySmallGroup(site, user);
		
		//친구 스몰그룹을 생성.
		SmallGroup friendSmallGroup = smallGroupService.generateFriendSmallGroup(site, user);
		
		user = userRepository.select(user.getId());
		user.setMySmallGroup(mySmallGroup);
		user.setFriendSmallGroup(friendSmallGroup);
		userRepository.update( user);
				
		smallGroupService.addUserToSmallGroup(user.getId(), site.getLobbySmallGroup().getId(), false);

		siteInactiveUser.setStatus(SiteInactiveUser.STATUS_COMPLETE);
		siteInactiveUserRepository.update(siteInactiveUser);
		
	}

	@Override
	@Transactional(readOnly = true)
	public List<Long> findAllJoinedUsers(Long siteId, Long exceptUserId) {
		return userRepository.findAllJoinedUsers( siteId , exceptUserId);
	}

	@Override
	@Transactional
	public Page<User> getAllFromSuper(Integer page, int pageSize) {
		Page<User> pagedResult = userRepository.getAllFromSuper(page, pageSize);
		
		for( User user : pagedResult.getContents() ){
			user.setLastLogin( loginInfoService.getLastLoginDateDate(user.getId()));
		}
		return pagedResult; 
	}

	@Override
	@Transactional
	public List<User> getSimpleUserList(Long siteId, Long authUserId, Long lineUserId, String queryName, Boolean top, Integer size){
		return userRepository.getSimpleUserList( siteId, authUserId, lineUserId, queryName, top, size);
	}


}
