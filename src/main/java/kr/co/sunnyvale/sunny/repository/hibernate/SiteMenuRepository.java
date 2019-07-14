package kr.co.sunnyvale.sunny.repository.hibernate;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.SiteMenu;
import kr.co.sunnyvale.sunny.exception.SimpleSunnyException;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;


@Repository(value = "siteMenuRepository")
public class SiteMenuRepository extends HibernateGenericRepository<SiteMenu>{
	public SiteMenuRepository(){

	}

	public SiteMenu findBySiteAndMenu(Long siteId, Integer menuId) {

		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("site.id", siteId));
		criteria.add(Restrictions.eq("menu.id", menuId));

		return (SiteMenu) criteria.uniqueResult();
			
	}

	public List<SiteMenu> getSiteMenus(Long siteId) {
		
		Criteria criteria = getCriteria();
		criteria.createAlias("menu", "menuAlias");
		criteria.add(Restrictions.eq("site.id", siteId));
		criteria.addOrder(Order.asc("ordering"));
		return criteria.list();
		
		
	}

	public int move(Long siteId, Long id, Integer beforePrevOrdering,
			Integer targetPrevOrdering) {
		


		String hqlUpdate = null;
		int changeOrdering = 0;
		// 타겟이 맨 처음인 경우
		if( targetPrevOrdering == null ){
			hqlUpdate = "update SiteMenu siteMenu set ordering = ordering + 1 where site.id = :siteId and ordering <= :beforePrevOrdering";
			changeOrdering = 0;
			// 그리고 현재꺼는 0으로 변경
		}
		// 비포가 맨 처음인 경우
		else if( beforePrevOrdering == null ){
			hqlUpdate = "update SiteMenu siteMenu set ordering = ordering + 1 where site.id = :siteId and ordering > :targetPrevOrdering";
			changeOrdering = targetPrevOrdering + 1;
			// 그리고 현재꺼는 targetPrevOrdering + 1 로 변경
		}
		// 타겟이 중간인 경우( 뒤에서 -> 앞으로 )
		else if( beforePrevOrdering > targetPrevOrdering ){
			hqlUpdate = "update SiteMenu siteMenu set ordering = ordering + 1 where site.id = :siteId and ordering <= :beforePrevOrdering and ordering > :targetPrevOrdering ";
			changeOrdering = targetPrevOrdering + 1;
			// 그리고 현재꺼는 targetPrevOrdering + 1 로 변경
		}
		// 타겟이 중간인 경우( 앞에서 -> 뒤로 )
		else if( targetPrevOrdering > beforePrevOrdering ){
			hqlUpdate = "update SiteMenu siteMenu set ordering = ordering - 1 where site.id = :siteId and ordering > :beforePrevOrdering and ordering <= :targetPrevOrdering ";
			changeOrdering = targetPrevOrdering ;
			// 그리고 현재꺼는 targetPrevOrdering 으로 변경
		}else{
			// 여기에 안걸리는 상환은 없어
			throw new SimpleSunnyException();
		}

		Query query = getCurrentSession().createQuery( hqlUpdate ).setLong( "siteId", siteId );
		
		if( beforePrevOrdering != null ){
			query.setInteger( "beforePrevOrdering", beforePrevOrdering );
		}
		if( targetPrevOrdering != null ){
			query.setInteger( "targetPrevOrdering", targetPrevOrdering );
		}
		query.executeUpdate();
		getCurrentSession().clear();
		
		return changeOrdering;
		
	}

	public void changeOrdering(Long id, int changedOrdering) {
		String hqlUpdate = "update SiteMenu siteMenu set ordering = :changedOrdering where id = :id ";

		Query query = getCurrentSession().createQuery( hqlUpdate ).setLong( "id", id ).setInteger("changedOrdering", changedOrdering);
		query.executeUpdate();
		getCurrentSession().clear();
		
	}

	
	
	
}
