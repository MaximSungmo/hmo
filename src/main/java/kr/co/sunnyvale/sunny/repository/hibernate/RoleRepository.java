package kr.co.sunnyvale.sunny.repository.hibernate;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.Role;

import org.springframework.stereotype.Repository;
/**
 *
 * {Hibernate}
 *<p>
 *	아주좋아요, 좋아요, 보통이예요, 별로예요. 이 네 가지를 관리하는 Repository
 *
 * @author mook
 *
 *	
 *
 */
@Repository(value = "roleRepository")
public class RoleRepository extends HibernateGenericRepository<Role>{
	public RoleRepository(){

	}
	
	public void checkAndGenerateDefault(){
		List<Role> roles = getAll(null);
		if( roles != null && !roles.isEmpty() ){
			return;
		}
		
		save( new Role(Role.ID_SUPER_ADMIN, Role.ROLE_SUPER_ADMIN, "슈퍼관리자"));
		save( new Role(Role.ID_ADMIN, Role.ROLE_ADMIN, "어드민") );
		save( new Role(Role.ID_USER, Role.ROLE_USER, "일반유저") );
		save( new Role(Role.ID_INACTIVE_ADMIN, Role.ROLE_INACTIVE_ADMIN, "비활성 어드민"));
		save( new Role(Role.ID_INACTIVE_USER, Role.ROLE_INACTIVE_USER, "비활성 유저"));
		
		
		clear();
		flush();
	}

}
