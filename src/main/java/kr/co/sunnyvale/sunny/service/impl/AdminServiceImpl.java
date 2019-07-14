package kr.co.sunnyvale.sunny.service.impl;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.Content;
import kr.co.sunnyvale.sunny.domain.DefaultTag;
import kr.co.sunnyvale.sunny.domain.Menu;
import kr.co.sunnyvale.sunny.domain.Role;
import kr.co.sunnyvale.sunny.domain.Site;
import kr.co.sunnyvale.sunny.domain.SiteMenu;
import kr.co.sunnyvale.sunny.domain.Tag;
import kr.co.sunnyvale.sunny.repository.hibernate.ChildContentRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.DefaultTagRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.MenuRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.RoleRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.SiteMenuRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.SiteRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.SmallGroupRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.UserRepository;
import kr.co.sunnyvale.sunny.service.AdminService;
import kr.co.sunnyvale.sunny.service.TagService;
import kr.co.sunnyvale.sunny.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( value="adminService" )
@Transactional
public class AdminServiceImpl implements AdminService {

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private SiteRepository siteRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DefaultTagRepository defaultTagRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private MenuRepository menuRepository;

	@Autowired
	private ChildContentRepository contentRepository;
	
	@Autowired
	private SmallGroupRepository smallGroupRepository;
	
	@Autowired
	private SiteMenuRepository siteMenuRepository;
	
	@Autowired
	private TagService tagService;
	
	@Override
	public void generateDefaults() {
		
		List<Role> allRoles = roleRepository.getAll(null);
		
		for( Role role : allRoles ){
			roleRepository.delete(role);
		}
		roleRepository.clear();
		
		roleRepository.checkAndGenerateDefault();
//		List<Feel> allEvals = feelRepository.getAll(null);
//		
//		for( Feel feel : allFeels ){
//			feelRepository.delete(feel);
//		}
//		evaluateRepository.clear();
//		
//		evaluateRepository.save( new Evaluate( -1, "negative", "나빠요", -1 ));
//		evaluateRepository.save( new Evaluate( 1, "Best", "좋아요", 1 ) );
//		evaluateRepository.save( new Evaluate( 2, "Good", "슬퍼요", 1 ) );
//		evaluateRepository.save( new Evaluate( 3, "Normal", "힘내요", 1 ) );
//		evaluateRepository.save( new Evaluate( 4, "Bad", "멋져요", 1 ) );
//		evaluateRepository.save( new Evaluate( 5, "Like", "좋아요", 1 ) );
	}
//	@Override
//	@Transactional
//	public void removeSiteFromAdminId(Long userId) {
//		User user = userService.findById(userId);
//		Site site = user.getSite();
//		siteRepository.delete(site);
//	}
	
	@Override
	@Transactional
	public void removeSite(Long siteId) {
//		List<User> users = userRepository.findListByObject("site.id", siteId, null);
//		for( User user : users ){
//			System.out.println("삭제 중");
//			User persistentUser = userRepository.select(user.getId());
//			userRepository.delete(persistentUser);
//			System.out.println("삭제됨");
//		}
		
		Site site = siteRepository.select(siteId);
		siteRepository.delete(site);
	}
	
//	@Override
//	@Transactional(readOnly = true)
//	public Admin findBySiteAndUser(Long id, Long userId) {
//		return adminRepository.findBySiteAndUser( id, userId);
//	}

	@Override
	@Transactional
	public void syncMenus() {
		
		
		List<Menu> menus = menuRepository.getAll(null);
//		int ordering = 0;
//		
//		Menu lobby = menuRepository.findUniqByObject("absoluteName", Menu.NAME_LOBBY);
//		if( lobby == null ){
//			lobby = new Menu();
//			lobby.setAbsoluteName(Menu.NAME_LOBBY);
//			menuRepository.save(lobby);
//		}
//		lobby.setName("모두의 소식");
//		lobby.setDescription("전체 공개 글, 내 글, 친구 글, 내가 속한 부서, 그룹에 대한 모든 글들이 나옵니다.");
//		lobby.setIconHtml("<i class='fa fa-users fa-1g mb-mgr'></i>");
//		lobby.setRelativeHref("/lobby");
//		lobby.setOrdering(ordering++);
//		menuRepository.update(lobby);
//		
////		Menu newsfeed = menuRepository.findUniqByObject("absoluteName", Menu.NAME_NEWSFEED);
////		newsfeed.setName("관심소식");
////		newsfeed.setDescription("내 글, 친구 글, 내가 속한 부서, 프로젝트, 그룹에 대한 모든 글들이 나옵니다.");
////		newsfeed.setIconHtml("<i class='fa fa-user fa-1g mb-mgr'></i>");
////		newsfeed.setRelativeHref("/newsfeed");
////		newsfeed.setOrdering(ordering++);
////		menuRepository.save(newsfeed);
//		
//		Menu sunnytalk = menuRepository.findUniqByObject("chat", Menu.NAME_CHAT);
//		if( sunnytalk == null ){
//			sunnytalk = new Menu();
//			sunnytalk.setAbsoluteName( Menu.NAME_CHAT );
//			menuRepository.save(sunnytalk);
//		}
//		sunnytalk.setName("써니톡");
//		sunnytalk.setDescription("써니톡!");
//		sunnytalk.setIconHtml("<i class='fa fa-comments fa-1g mb-mgr'></i>");
//		sunnytalk.setRelativeHref("/message/channels");
//		sunnytalk.setOrdering(ordering++);
//		menuRepository.update(sunnytalk);
//		
////		Menu pds = new Menu();
////		pds.setName("자료실");
////		pds.setDescription("사이트의 모두가 자료를 올리고 특정 IP만 접근하게 할 수 있습니다.");
////		pds.setIconHtml("<i class='fa fa-cloud fa-1g mb-mgr'></i>");
////		pds.setRelativeHref("/pds");
////		pds.setOrdering(ordering++);
////		menuRepository.save(pds);
////		
////		Menu note = new Menu();
////		note.setName("노트");
////		note.setDescription("긴 글을 최적화된 에디터를 통해 쓰고 태그를 통해 관리할 수 있습니다.");
////		note.setIconHtml("<i class='fa fa-pencil-square-o fa-1g mb-mgr'></i>");
////		note.setRelativeHref("/note");
////		note.setOrdering(ordering++);
////		menuRepository.save(note);
//		
//		Menu bookmark = menuRepository.findUniqByObject("absoluteName", Menu.NAME_BOOKMARK);
//		if( bookmark == null ){
//			bookmark = new Menu();
//			bookmark.setAbsoluteName(Menu.NAME_BOOKMARK);
//			menuRepository.save(bookmark);
//		}
//		bookmark.setName("북마크");
//		bookmark.setDescription("회원 각자가 즐겨찾는 글들을 북마크하고 나중에 따로 볼 수 있습니다.");
//		bookmark.setIconHtml("<i class='fa fa-bookmark fa-1g mb-mgr'></i>");
//		bookmark.setRelativeHref("/bookmark");
//		bookmark.setOrdering(ordering++);
//		menuRepository.update(bookmark);
//		
//		
//		Menu contact = menuRepository.findUniqByObject("absoluteName", Menu.NAME_CONTACT);
//		if( contact == null ){
//			contact = new Menu();
//			contact.setAbsoluteName(Menu.NAME_CONTACT);
//			menuRepository.save(contact);
//		}
//		contact.setName("주소록");
//		contact.setDescription("회사의 모든 구성원들을 조직도 혹은 주소록 형태로 볼 수 있습니다.");
//		contact.setIconHtml("<i class='fa fa-book fa-1g mb-mgr'></i>");
//		contact.setRelativeHref("/contact");
//		contact.setOrdering(ordering++);
//		menuRepository.update(contact);
//		
////		Menu approval = new Menu();
////		approval.setName("전자결재");
////		approval.setDescription("간편한 인터페이스, 막강한 기능을 통해 구성원간 결재를 진행할 수 있습니다.");
////		approval.setIconHtml("<i class='fa fa-folder-open fa-1g mb-mgr'></i>");
////		approval.setRelativeHref("/approval");
////		approval.setOrdering(ordering++);
////		menuRepository.save(approval);
//		
////		Menu work = new Menu();
////		work.setName("작업");
////		work.setDescription("아직 구현중입니다.");
////		work.setIconHtml("<i class='fa fa-tasks fa-1g mb-mgr'></i>");
////		work.setRelativeHref("/task");
////		work.setOrdering(ordering++);
////		menuRepository.save(work);
//		
////		Menu notice = new Menu();
////		notice.setName("공지사항");
////		notice.setDescription("현재 공지된 글들과 과거에 공지된 글들을 볼 수 있습니다.");
////		notice.setIconHtml("<i class='fa fa-volume-up fa-1g mb-mgr'></i>");
////		notice.setRelativeHref("/notice");
////		notice.setOrdering(ordering++);
////		menuRepository.save(notice);
//		
//		Menu department = menuRepository.findUniqByObject("absoluteName", Menu.NAME_DEPARTMENT);
//		if( department == null ){
//			department = new Menu();
//			department.setAbsoluteName(Menu.NAME_DEPARTMENT);
//			menuRepository.save(department);
//		}
//		department.setName("부서(팀)");
//		department.setDescription("부서 기능을 사용할 수 있습니다.");
//		department.setIconHtml("<i class='fa fa-sitemap fa-1g mb-mgr'></i>");
//		department.setRelativeHref("/department");
//		department.setType(Menu.TYPE_DEPARTMENT);
//		department.setOrdering(ordering++);
//		menuRepository.update(department);
//		
//		
//		Menu group = menuRepository.findUniqByObject("absoluteName", Menu.NAME_GROUP);
//		if( group == null ){
//			group = new Menu();
//			group.setAbsoluteName(Menu.NAME_GROUP);
//			menuRepository.save(group);
//		}
//		group.setName("소그룹");
//		group.setDescription("구성원간 자유롭게 소그룹을 생성할 수 있습니다.");
//		group.setIconHtml("<i class='fa fa-sitemap fa-1g mb-mgr'></i>");
//		group.setRelativeHref("/group");
//		group.setType(Menu.TYPE_GROUP);
//		group.setOrdering(ordering++);
//		menuRepository.update(group);
//		
//		
////		Menu project = new Menu();
////		project.setName("프로젝트");
////		project.setDescription("부서 내에, 혹은 따로 프로젝트를 구성하고 진행합니다.");
////		project.setIconHtml("<i class='fa fa-sitemap fa-1g mb-mgr'></i>");
////		project.setRelativeHref("/project");
////		project.setType(Menu.TYPE_PROJECT);
////		project.setOrdering(ordering++);
////		menuRepository.save(project);
//		
//		Menu media = menuRepository.findUniqByObject("absoluteName", Menu.NAME_MEDIA);
//		if( media == null ){
//			media = new Menu();
//			media.setAbsoluteName(Menu.NAME_MEDIA);
//			menuRepository.save(media);
//		}
//		media.setName("파일목록");
//		media.setDescription("스토리에 있는 파일들을 모아서 보여줍니다.");
//		media.setIconHtml("<i class='fa fa-cloud fa-1g mb-mgr'></i>");
//		media.setRelativeHref("/media");
//		media.setType(Menu.TYPE_NORMAL);
//		media.setOrdering(ordering++);
//		menuRepository.update(media);
//		
//		List<Menu> menus = menuRepository.getAll(null);

		// 전체는 나중에
		List<Site> sites = siteRepository.getAll(null);
//		List<Site> sites = Lists.newArrayList();
//		sites.add(new Site(1L));
		for( Site site : sites ){
			for( Menu menu : menus ){
				SiteMenu siteMenu = siteMenuRepository.findBySiteAndMenu(site.getId(), menu.getId());
				if( siteMenu != null )
					continue;
				
				System.out.println("사이트 없음. 생성하기");
				siteMenu = new SiteMenu();
				siteMenu.setSite(site);
				siteMenu.setMenu(menu);
				siteMenu.setOrdering(menu.getOrdering());
				siteMenuRepository.save(siteMenu);	
			}
		}
	}

	@Override
	@Transactional
	public void syncTags() {
		
		List<DefaultTag> defaultTags = defaultTagRepository.getAll(null);
		
		List<Site> sites = siteRepository.getAll(null);
//		List<Site> sites = Lists.newArrayList();
//		sites.add(new Site(1L));
		for( Site site : sites ){
			for( DefaultTag defaultTag : defaultTags ){
				Tag tag  = tagService.findByTitle(site.getLobbySmallGroup(), Content.TYPE_STORY, defaultTag.getTitle());
				if( tag != null ){
					tag.setAdminOrdering( defaultTag.getAdminOrdering() );
					tagService.update(tag);
					continue;
				}
				tag = new Tag(site.getLobbySmallGroup(), defaultTag.getContentType());
				tag.setTitle( defaultTag.getTitle() );
				tag.setAdminSelected(true);
				tag.setAdminOrdering(defaultTag.getAdminOrdering());
				tagService.save(tag);
			}
		}
	}

}
