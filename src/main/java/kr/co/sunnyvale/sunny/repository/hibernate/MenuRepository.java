package kr.co.sunnyvale.sunny.repository.hibernate;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.Menu;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;


@Repository(value = "menuRepository")
public class MenuRepository extends HibernateGenericRepository<Menu>{
	public MenuRepository(){

	}

	public List<Menu> getMenus() {
		
		Criteria criteria = getCriteria();
		
		criteria.addOrder(Order.asc("ordering"));
		
		return criteria.list();
	}

	public List<Menu> findBySite(Long siteId) {
		Criteria criteria = getCriteria();
		
		criteria.createAlias("siteMenus", "siteMenusAlias");
		criteria.add(Restrictions.eq("siteMenusAlias.site.id", siteId));
		criteria.add(Restrictions.eq("siteMenusAlias.visible", true));
		criteria.addOrder(Order.asc("siteMenusAlias.ordering"));
		return criteria.list();
	}
}
