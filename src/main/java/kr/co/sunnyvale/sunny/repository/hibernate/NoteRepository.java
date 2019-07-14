package kr.co.sunnyvale.sunny.repository.hibernate;

import java.util.Date;
import java.util.List;

import kr.co.sunnyvale.sunny.domain.ContentAndTag;
import kr.co.sunnyvale.sunny.domain.Note;
import kr.co.sunnyvale.sunny.domain.SmallGroup;
import kr.co.sunnyvale.sunny.domain.Tag;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.stereotype.Repository;

/**
 * 
 *	{Hibernate}
 *	
 *<p>
 *	퀘스천 때 쓰는 카테고리 Repository
 *
 * @author mook
 *
 *
 */

@Repository(value = "noteRepository")
public class NoteRepository extends HibernateGenericRepository<Note>{
	
	
	public NoteRepository(){

	}


	public List<Note> getTaggedList(SmallGroup smallGroup, Tag tag, Stream stream) {
		Criteria criteria = getCriteria();
		
		DetachedCriteria tagCriteria = DetachedCriteria.forClass(ContentAndTag.class, "tag");
		tagCriteria.add(Restrictions.eq("tag", tag));
		tagCriteria.setProjection(Projections.projectionList().add(Projections.distinct(Projections.property("content.id")), "contentId"));

//		criteria.add( Restrictions.eq("modifying", false));
		criteria.add( Restrictions.eq("smallGroup", smallGroup));
		criteria.add( Restrictions.eq("deleteFlag", false));
		criteria.add(Subqueries.propertyIn("id", tagCriteria));
		criteria.addOrder( Order.desc("createDate"));
		
		
		return getPagedList(criteria, stream);
				
	}

	public List<Note> getList(User user, Integer categoryId, Stream stream) {
		Criteria criteria = getCriteria();
		if( categoryId != null && categoryId > 0 ){
			criteria.add(Restrictions.or(Restrictions.eq("category1.id", categoryId), Restrictions.eq("category2.id", categoryId)));
		}
		criteria.add( Restrictions.eq("modifying", false));
		criteria.addOrder( Order.desc("createDate"));
		criteria.add( Restrictions.eq("deleteFlag", false));
		return getPagedList(criteria, stream);
		
	}

	public List<Note> getPostingNotes(Stream stream) {
		Criteria criteria = getCriteria();
//		criteria.add( Restrictions.eq("modifying", false));
		criteria.addOrder( Order.desc("createDate"));
		return getPagedList(criteria, stream);
	}

	public Page<Note> getPagedPostingNotes(Sunny sunny, User authUser, String tab, String tagTitle, String queries, Long smallGroupId, String userId, Integer pageNum, int pageSize) {
//		Criteria criteria = getCriteria();

		Criteria criteria = getCommonSmallGroupCriteria(sunny, authUser, tab, tagTitle, queries, smallGroupId, userId);
		String reputationString = null;
		if( tab != null ){
			switch( tab ){
			case "hot":
				reputationString =  "dynamicAlias.reputation";
				break;
			case "week":
				reputationString =  "dynamicAlias.reputation";
				break;
			case "month":
				reputationString =  "dynamicAlias.reputation";
				break;
			default:
				break;
			}
		}
		
		if( reputationString != null )
			criteria.addOrder( Order.desc("dynamicAlias.reputation"));{
		}

		criteria.add( Restrictions.eq("deleteFlag", false));
		criteria.addOrder( Order.desc("createDate") );
		
		return getPagedList(criteria, pageNum, pageSize);
	}
	

	public Page<Note> getPagedList(User user, Integer categoryId, Integer pageNum, int pageSize) {
		Criteria criteria = getCriteria();
		if( categoryId != null && categoryId > 0 ){
			criteria.add(Restrictions.or(Restrictions.eq("category1.id", categoryId), Restrictions.eq("category2.id", categoryId)));
		}
//		criteria.add( Restrictions.eq("modifying", false));
		criteria.add( Restrictions.eq("deleteFlag", false));
		criteria.addOrder( Order.desc("createDate"));
		return getPagedList(criteria, pageNum, pageSize);
	}

	
	public Page<Note> getPagedHotPostingNotes(Sunny sunny, String[] queries, Long smallGroupId, String userId, Integer pageNum, int pageSize) {
		Criteria criteria = getCriteria();		
		
		if( smallGroupId != null ){
			criteria.add( Restrictions.eq("smallGroup.id", smallGroupId));
		}else{
			criteria.add( Restrictions.eq("smallGroup", sunny.getSite().getLobbySmallGroup()));
		}
		
		if( userId != null ){
			criteria.add( Restrictions.eq("user.id", userId ));
		}
		
		if( queries != null ){
			for( String query : queries ){
				Disjunction disjunction = Restrictions.disjunction();
				disjunction.add(Restrictions.like("title", query, MatchMode.ANYWHERE));
				disjunction.add(Restrictions.like("rawText", query, MatchMode.ANYWHERE));
				criteria.add(disjunction);
			}
		}
		criteria.add( Restrictions.eq("deleteFlag", false));
//		criteria.add( Restrictions.eq("modifying", false));
		criteria.createAlias("dynamic", "dynamicAlias", Criteria.LEFT_JOIN);
		criteria.addOrder( Order.desc("dynamicAlias.reputation"));
		/*
		 * TODO: 지금은 별로 글이 없어 기간설정하지 않았지만 추후 쌓이게 되면 기간 설정할 것
		 */
//		criteria.
		return getPagedList(criteria, pageNum, pageSize);
	}

	public Page<Note> getPagedUnansweredPostingNotes(Sunny sunny, String[] queries, Long smallGroupId, String userId, Integer pageNum, int pageSize) {
		Criteria criteria = getCriteria();
//		criteria.add( Restrictions.eq("modifying", false));
		if( smallGroupId != null ){
			criteria.add( Restrictions.eq("smallGroup.id", smallGroupId));
		}else{

			criteria.add( Restrictions.eq("smallGroup", sunny.getSite().getLobbySmallGroup()));
		}
		
		if( userId != null ){
			criteria.add( Restrictions.eq("user.id", userId));
		}
		
		if( queries != null ){
			for( String query : queries ){
				Disjunction disjunction = Restrictions.disjunction();
				disjunction.add(Restrictions.like("title", query, MatchMode.ANYWHERE));
				disjunction.add(Restrictions.like("rawText", query, MatchMode.ANYWHERE));
				criteria.add(disjunction);
			}
		}

		criteria.add( Restrictions.eq("deleteFlag", false));
		criteria.createAlias("dynamic", "dynamicAlias", Criteria.LEFT_JOIN);
		criteria.add( Restrictions.eq("dynamicAlias.evaluateCount", 0));
		criteria.addOrder( Order.desc("createDate"));
		return getPagedList(criteria, pageNum, pageSize);
	}

	public Page<Note> getPagedPeriodHotPostingNotes(Sunny sunny, String[] queries, Long smallGroupId, String userId, int period, Integer pageNum, int pageSize) {
		Criteria criteria = getCriteria();
//		criteria.add( Restrictions.eq("modifying", false));
		if( smallGroupId != null ){
			criteria.add( Restrictions.eq("smallGroup.id", smallGroupId));
		}else{
			criteria.add( Restrictions.eq("smallGroup", sunny.getSite().getLobbySmallGroup()));
		}
		
		if( userId != null ){
			criteria.add( Restrictions.eq("user.id", userId));
		}
		if( queries != null ){
			for( String query : queries ){
				Disjunction disjunction = Restrictions.disjunction();
				disjunction.add(Restrictions.like("title", query, MatchMode.ANYWHERE));
				disjunction.add(Restrictions.like("rawText", query, MatchMode.ANYWHERE));
				criteria.add(disjunction);
			}
		}
		criteria.add( Restrictions.eq("deleteFlag", false));
		Date today = new Date();
		long DAY_IN_MS = 1000 * 60 * 60 * 24;
		Date prevDate = new Date( today.getTime() - ( period * DAY_IN_MS));
		criteria.add( Restrictions.gt("createDate", prevDate));
		return getPagedList(criteria, pageNum, pageSize);
	}


	public Note getSequenceNote(Sunny sunny, User authUser, int direct, Note note, String tab,
			String tagTitle, String queries, Long smallGroupId, String userId) {
		Criteria criteria = getCommonSmallGroupCriteria(sunny,authUser, tab, tagTitle, queries, smallGroupId, userId);

		
		String reputationString = null;
		if( tab != null ){
			switch( tab ){
			case "hot":
				reputationString =  "dynamicAlias.reputation";
				break;
			case "week":
				reputationString =  "dynamicAlias.reputation";
				break;
			case "month":
				reputationString =  "dynamicAlias.reputation";
				break;
			default:
				break;
			}
		}
		
		// reputation(평판) 이 같은 글의 경우엔 한번 더 쿼리를 날려줘야 하기 때문에 복잡함.
		if( reputationString != null ){
			int reputation = note.getDynamic().getReputation();
			criteria.add(Restrictions.eq(reputationString, reputation));

			if( direct > 0 ){
				criteria.setProjection(Projections.max("createDate"));
			}else{
				criteria.setProjection(Projections.min("createDate"));
			}
			Date createDate = (Date) criteria.uniqueResult();
			
			// 내가 해당 reputation 의 유일한 글이거나 끝에 있는 글인 경우엔 다음 reputation 을 가져온다. 
			if( createDate.equals( note.getCreateDate() ) ){

				criteria = getCommonSmallGroupCriteria(sunny, authUser, tab, tagTitle, queries, smallGroupId, userId);
				if( direct > 0 ){
					criteria.addOrder( Order.asc("dynamicAlias.reputation"));
					criteria.addOrder( Order.asc("createDate") );
					criteria.add(Restrictions.gt(reputationString, reputation));
				}else{
					criteria.addOrder( Order.desc("dynamicAlias.reputation"));
					criteria.addOrder( Order.desc("createDate") );
					criteria.add(Restrictions.lt(reputationString, reputation));
				}
				criteria.setMaxResults(1);
				return (Note) criteria.uniqueResult();
			}
			
			// 동일한 reputation 이 있고 내가 그 중간쯤에 있을 땐 다음것을 가져온다.  
			criteria = getCommonSmallGroupCriteria(sunny, authUser, tab, tagTitle, queries, smallGroupId, userId);
			if( direct > 0 ){
				criteria.addOrder( Order.asc("dynamicAlias.reputation"));
				criteria.addOrder( Order.asc("createDate") );
				criteria.add( Restrictions.gt("createDate", note.getCreateDate()) );
			}else{
				criteria.addOrder( Order.desc("dynamicAlias.reputation"));
				criteria.addOrder( Order.desc("createDate") );
				criteria.add( Restrictions.lt("createDate", note.getCreateDate()) );
			}
			criteria.setMaxResults(1);
			return (Note) criteria.uniqueResult();
		}

		if( direct > 0 ){
			criteria.addOrder( Order.asc("createDate") );
			criteria.add(Restrictions.gt("id", note.getId()));
		}else{
			criteria.addOrder( Order.desc("createDate") );
			criteria.add(Restrictions.lt("id", note.getId()));
		}
		criteria.setMaxResults(1);
		
		return (Note) criteria.uniqueResult();
	}

	private Criteria getCommonSmallGroupCriteria(Sunny sunny, User authUser, String tabTitle, String tagTitle, String queries, Long smallGroupId, String userId){
		Criteria criteria = getCriteria();

		criteria.add( Restrictions.eq("deleteFlag", false));
		if( smallGroupId != null ){
			criteria.add( Restrictions.eq("smallGroup.id", smallGroupId));
		}else{

			criteria.add( Restrictions.eq("smallGroup", sunny.getSite().getLobbySmallGroup()));
		}
		if( userId != null ){
			criteria.add( Restrictions.eq("user.id", userId));
		}
		
		
		if( queries != null ){
//			for( String query : queries ){
//				Disjunction disjunction = Restrictions.disjunction();
//				disjunction.add(Restrictions.like("title", query, MatchMode.ANYWHERE));
//				disjunction.add(Restrictions.like("rawText", query, MatchMode.ANYWHERE));
//				criteria.add(disjunction);
//			}
			Disjunction disjunction = Restrictions.disjunction();
			disjunction.add(Restrictions.like("title", queries, MatchMode.ANYWHERE));
			disjunction.add(Restrictions.like("rawText", queries, MatchMode.ANYWHERE));
			criteria.add(disjunction);
		}
		if( tagTitle != null ){
			criteria.createAlias("tags", "tagsAlias");
			criteria.add(Restrictions.eq("tagsAlias.title", tagTitle));
		}
		if( tabTitle == null ){
			return criteria;
		}
		
		switch( tabTitle ){
		case "hot":
			criteria.createAlias("dynamic", "dynamicAlias", Criteria.LEFT_JOIN);
//			criteria.addOrder( Order.desc("dynamicAlias.reputation"));
			break;
		case "unanswered":
			criteria.createAlias("dynamic", "dynamicAlias", Criteria.LEFT_JOIN);
			criteria.add( Restrictions.eq("dynamicAlias.evaluateCount", 0));
//			criteria.addOrder( Order.desc("dynamicAlias.reputation"));
			break;
		case "week":
			criteria.createAlias("dynamic", "dynamicAlias", Criteria.LEFT_JOIN);
//			criteria.addOrder( Order.desc("dynamicAlias.reputation"));
			break;
		case "month":
			criteria.createAlias("dynamic", "dynamicAlias", Criteria.LEFT_JOIN);
//			criteria.addOrder( Order.desc("dynamicAlias.reputation"));
			break;
		case "my":
			if( authUser != null ){
				criteria.add( Restrictions.eq("user", authUser));
			}
			break;
		default:
			break;
		}

		return criteria;
	}


	public Number getNoteCount(long smallGroupId) {
		Criteria criteria = getCriteria();
		criteria.add(
			Restrictions.eq( "smallGroup.id", smallGroupId)
		);
		criteria.setProjection(Projections.rowCount());
		return (Number) criteria.uniqueResult();
	}


}
