package kr.co.sunnyvale.sunny.repository.hibernate;

import java.util.Date;
import java.util.List;

import kr.co.sunnyvale.sunny.domain.ActivityType;
import kr.co.sunnyvale.sunny.domain.LastRead;
import kr.co.sunnyvale.sunny.domain.SmallGroup;
import kr.co.sunnyvale.sunny.domain.Story;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.UserSmallGroupAccess;
import kr.co.sunnyvale.sunny.domain.dto.NoticeDTO;
import kr.co.sunnyvale.sunny.domain.dto.NotifyInfoDTO;
import kr.co.sunnyvale.sunny.domain.dto.StoryDTO;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;

@Repository(value = "storyRepository")
public class StoryRepository extends ContentRepository<Story> {


	@SuppressWarnings("unchecked")
	private List<Long> getUserJoinedSmallGroupIds( User user ){
		Criteria userSmallGroupAccessCriteria = getCurrentSession().createCriteria(UserSmallGroupAccess.class);
		userSmallGroupAccessCriteria.add(Restrictions.eq("user", user));
		userSmallGroupAccessCriteria.setProjection(Projections.projectionList().add(Projections.property("smallGroup.id"), "smallGroupId"));
		return userSmallGroupAccessCriteria.list();
	}




	public List<StoryDTO> fetchNoticeStories(Sunny sunny, User user,
			String[] queries, Stream stream) {
		/*
		 *	 사용자가 포함된 smallGroup 은 엄청나게 많지는 않을 것이기에 직접 List를 뽑아낸다.
		 *	추후 이 리스트를 캐시로 관리할 것. 
		 */

		List<Long> userJoinedSmallGroupIds = getUserJoinedSmallGroupIds(user);


		if( userJoinedSmallGroupIds == null || userJoinedSmallGroupIds.size() == 0  )
			return null;

		/*
		 * smallGroup 들 뽑아내기 끝
		 */

		Criteria criteria = getCriteria();

		criteria.createAlias("smallGroupContentAccesses", "smallGroupContentAccessesAlias");

		criteria.add(Restrictions.in("smallGroupContentAccessesAlias.smallGroup.id", userJoinedSmallGroupIds));

		criteria.add(Restrictions.eq("this.site", sunny.getSite()));
		criteria.add(Restrictions.eq("this.deleteFlag", false));
		criteria.add(Restrictions.eq("this.isNotice", true));
		if( queries != null ){
			for( String query : queries ){
				criteria.add(Restrictions.like("rawText", query, MatchMode.ANYWHERE));	
			}
		}

		criteria.addOrder(Order.desc("smallGroupContentAccessesAlias.createDate"));

		storyDtoCriteria(criteria, null);
		return getPagedDTOList(criteria, stream);

	}

	public List<StoryDTO> fetchSuperStories(User user, Stream stream) {


		Criteria criteria = getCriteria();

		criteria.add(Restrictions.eq("smallGroup.id", SmallGroup.SYSTEM_GROUP));
		criteria.add(Restrictions.eq("this.deleteFlag", false));

		criteria.addOrder(Order.desc("createDate"));
		storyDtoCriteria(criteria, null);
		return getPagedDTOList(criteria, stream);
	}



	public List<StoryDTO> fetchFeedback(User user, String[] queries,
			Stream stream) {

		Criteria criteria = getCriteria();

//		criteria.add(Restrictions.isNull("site"));

		criteria.add(Restrictions.eq("this.deleteFlag", false));
		if( queries != null ){
			for( String query : queries ){
				criteria.add(Restrictions.like("rawText", query, MatchMode.ANYWHERE));	
			}
		}
		criteria.add(Restrictions.eq("this.feedback", true));

		criteria.addOrder(Order.desc("createDate"));
		storyDtoCriteria(criteria, null);
		return getPagedDTOList(criteria, stream);
	}
	


	public List<StoryDTO> getSuperAdminStories(Long siteId, User user,
			String[] queries, Stream stream) {


		Criteria criteria = getCriteria();

		criteria.add(Restrictions.eq("this.site.id", siteId));
		criteria.add(Restrictions.eq("this.deleteFlag", false));

		if( queries != null ){
			for( String query : queries ){
				criteria.add(Restrictions.like("rawText", query, MatchMode.ANYWHERE));	
			}
		}

		criteria.addOrder(Order.desc("createDate"));
		//newCriteria.addOrder(Order.desc("smallGroupContentAccessesAlias.createDate"));

		storyDtoCriteria(criteria, null);
		
		return getPagedDTOList(criteria, stream);
	}

	

	@SuppressWarnings({ "unused", "unchecked" })
	public List<StoryDTO> fetchLobbyStories(Sunny sunny, User user, String[] queries, Stream stream) {

		/*
		 *	 사용자가 포함된 smallGroup 은 엄청나게 많지는 않을 것이기에 직접 List를 뽑아낸다.
		 *	추후 이 리스트를 캐시로 관리할 것. 
		 */

		List<Long> userJoinedSmallGroupIds = getUserJoinedSmallGroupIds(user);
		
		if( userJoinedSmallGroupIds == null )
			userJoinedSmallGroupIds = Lists.newArrayList();

		userJoinedSmallGroupIds.add(SmallGroup.SYSTEM_GROUP);
		/*
		 * smallGroup 들 뽑아내기 끝
		 */

		Criteria criteria = getCriteria();

		criteria.createAlias("smallGroupContentAccesses", "smallGroupContentAccessesAlias");

		criteria.add(Restrictions.in("smallGroupContentAccessesAlias.smallGroup.id", userJoinedSmallGroupIds));

//		criteria.add(Restrictions.eq("this.site", sunny.getSite()));
		criteria.add(Restrictions.eq("this.deleteFlag", false));

		if( queries != null ){
			for( String query : queries ){
				criteria.add(Restrictions.like("rawText", query, MatchMode.ANYWHERE));	
			}
		}

		criteria.addOrder(Order.desc("smallGroupContentAccessesAlias.createDate"));

		criteria.setProjection(Projections.distinct(Projections.property("id")));
		List<Long> contentIds = getPagedIdList(criteria, stream);

		if( contentIds == null || contentIds.size() == 0 )
			return null;

		Criteria newCriteria = getCriteria();

		newCriteria.addOrder(Order.desc("createDate"));
		//newCriteria.addOrder(Order.desc("smallGroupContentAccessesAlias.createDate"));


		newCriteria.add(Restrictions.in("this.id", contentIds));
		storyDtoCriteria(newCriteria, null);
		return newCriteria.list();
	}


	public List<StoryDTO> fetchNewsfeedStories(Sunny sunny, User user, String[] queries, Stream stream) {


		/*
		 * inQuery 버전
		 */

		List<Long> userJoinedSmallGroupIds = getUserJoinedSmallGroupIds(user);

		if( userJoinedSmallGroupIds == null )
			userJoinedSmallGroupIds = Lists.newArrayList();

		userJoinedSmallGroupIds.add(SmallGroup.SYSTEM_GROUP);


		Criteria friendSmallGroupAccessCriteria = getCurrentSession().createCriteria(UserSmallGroupAccess.class);
		friendSmallGroupAccessCriteria.add(Restrictions.eq("smallGroup", user.getFriendSmallGroup()));
		friendSmallGroupAccessCriteria.setProjection(Projections.projectionList().add(Projections.property("user.id"), "userId"));
		List<Long> friendIds  = friendSmallGroupAccessCriteria.list();


		Criteria criteria = getCriteria();

		criteria.createAlias("smallGroupContentAccesses", "smallGroupContentAccessesAlias");

		criteria.add(Restrictions.in("smallGroupContentAccessesAlias.smallGroup.id", userJoinedSmallGroupIds));
		criteria.add(Restrictions.eq("this.site", sunny.getSite()));
		criteria.add(Restrictions.in("user.id", friendIds));
		criteria.add(Restrictions.eq("this.deleteFlag", false));

		if( queries != null ){
			for( String query : queries ){
				criteria.add(Restrictions.like("rawText", query, MatchMode.ANYWHERE));	
			}
		}

		criteria.addOrder(Order.desc("smallGroupContentAccessesAlias.createDate"));

		storyDtoCriteria(criteria, null);
		return getPagedDTOList(criteria, stream);

	}
	public List<StoryDTO> fetchBasecampStories(Sunny sunny, User basecampUser, User authUser, String[] queries,
			Stream stream) {
		/*
		 * inQuery 버전
		 */

		List<Long> userJoinedSmallGroupIds = getUserJoinedSmallGroupIds(authUser);

		if( userJoinedSmallGroupIds  == null || userJoinedSmallGroupIds.size() == 0  )
			return null;

		Criteria criteria = getCriteria();

		criteria.createAlias("smallGroupContentAccesses", "smallGroupContentAccessesAlias");
		criteria.add(Restrictions.in("smallGroupContentAccessesAlias.smallGroup.id", userJoinedSmallGroupIds));
		criteria.add(Restrictions.eq("this.site", sunny.getSite()));
		criteria.add(Restrictions.eq("user", basecampUser));
		criteria.add(Restrictions.eq("this.deleteFlag", false));
		if( queries != null ){
			for( String query : queries ){
				criteria.add(Restrictions.like("rawText", query, MatchMode.ANYWHERE));	
			}
		}

		criteria.setProjection(Projections.distinct(Projections.property("id")));
		criteria.addOrder(Order.desc("smallGroupContentAccessesAlias.createDate"));
		List<Long> contentIds = getPagedIdList(criteria, stream);

		if( contentIds == null || contentIds.size() == 0 )
			return null;

		Criteria newCriteria = getCriteria();

		newCriteria.addOrder(Order.desc("createDate"));
		//newCriteria.addOrder(Order.desc("smallGroupContentAccessesAlias.createDate"));


		newCriteria.add(Restrictions.in("this.id", contentIds));
		storyDtoCriteria(newCriteria, null);
		return newCriteria.list();

	}


	@SuppressWarnings("unchecked")
	public List<StoryDTO> fetchSmallGroupStories(Sunny sunny, Long smallGroupId, User user, Boolean isWantChildren, String[] queries, Stream stream) {
		/*
		 *	 사용자가 포함된 smallGroup 은 엄청나게 많지는 않을 것이기에 직접 List를 뽑아낸다.
		 *	추후 이 리스트를 캐시로 관리할 것. 
		 */

		List<Long> userJoinedSmallGroupIds = getUserJoinedSmallGroupIds(user);


		if( userJoinedSmallGroupIds == null || userJoinedSmallGroupIds.size() == 0  )
			return null;

		/*
		 * smallGroup 들 뽑아내기 끝
		 */

		Criteria criteria = getCriteria();

		criteria.createAlias("smallGroupContentAccesses", "smallGroupContentAccessesAlias");

		criteria.add(Restrictions.in("smallGroupContentAccessesAlias.smallGroup.id", userJoinedSmallGroupIds));

		criteria.add(Restrictions.eq("this.site", sunny.getSite()));

		criteria.add(Restrictions.eq("this.smallGroup.id", smallGroupId));
		criteria.add(Restrictions.eq("this.deleteFlag", false));

		if( queries != null ){
			for( String query : queries ){
				criteria.add(Restrictions.like("rawText", query, MatchMode.ANYWHERE));	
			}
		}

		criteria.setProjection(  Projections.property("this.id") );

		//		storyIdCriteria.addOrder(Order.desc("thread"));
		//		storyIdCriteria.addOrder(Order.asc("threadSeq"));
		criteria.addOrder(Order.desc("smallGroupContentAccessesAlias.createDate"));

		//storyDtoCriteria(criteria, null);
		//return getPagedDTOList(criteria, stream);

		List<Long> contentIds = getPagedIdList(criteria, stream);

		if( contentIds == null || contentIds.size() == 0 )
			return null;

		Criteria newCriteria = getCriteria();

		newCriteria.addOrder(Order.desc("createDate"));
		//newCriteria.addOrder(Order.desc("smallGroupContentAccessesAlias.createDate"));


		newCriteria.add(Restrictions.in("this.id", contentIds));
		storyDtoCriteria(newCriteria, null);
		return newCriteria.list();
	}


	private void storyDtoCriteria(Criteria criteria, String prefix) {

		if( prefix == null )
			prefix = "";

		criteria.createAlias(prefix + "user", "userAlias");
		criteria.createAlias(prefix + "postUser", "postUserAlias", Criteria.LEFT_JOIN);
		criteria.createAlias(prefix + "dynamic", "dynamicAlias", Criteria.LEFT_JOIN);
		criteria.createAlias(prefix + "smallGroup", "smallGroupAlias", Criteria.LEFT_JOIN);

		//		criteria.addOrder(Order.desc(prefix + "thread"));
		//		criteria.addOrder(Order.asc(prefix + "threadSeq"));
		criteria.setProjection(	Projections.projectionList()

				// Content
				.add(Projections.property(prefix + "id"), "id")
				.add(Projections.property(prefix + "taggedTextPrev"),"taggedTextPrev")
				.add(Projections.property(prefix + "taggedTextNext"),"taggedTextNext")
				.add(Projections.property(prefix + "returnCount"),"returnCount")
				.add(Projections.property(prefix + "mediaCount"), "mediaCount")
				.add(Projections.property(prefix + "ipAddress"), "ipAddress")
				.add(Projections.property(prefix + "createDate"), "createDate")
				.add(Projections.property(prefix + "updateDate"), "updateDate")
				.add(Projections.property(prefix + "createDate"), "arriveDate")
				.add(Projections.property(prefix + "type"), "type")
				.add(Projections.property(prefix + "feedback"), "feedback")
				.add(Projections.property(prefix + "permissionType"), "permissionType")
				.add(Projections.property(prefix + "smallGroupAlias.id"), "smallGroupId")
				.add(Projections.property(prefix + "smallGroupAlias.name"), "smallGroupName")
				.add(Projections.property(prefix + "smallGroupAlias.type"), "smallGroupType")
				.add(Projections.property("dynamicAlias.replyCount"), "replyCount")
				.add(Projections.property("dynamicAlias.feelScore"), "feelScore")
				.add(Projections.property("dynamicAlias.feelCount"), "feelCount")

				// User
				.add(Projections.property("userAlias.id"), "userId")
				.add(Projections.property("userAlias.name"), "userName")
				.add(Projections.property("userAlias.profilePic"), "userProfilePic")

				.add(Projections.property("postUserAlias.id"), "postUserId")
				.add(Projections.property("postUserAlias.name"), "postUserName")
				.add(Projections.property("postUserAlias.profilePic"), "postUserProfilePic")

				);

		criteria.setResultTransformer(new AliasToBeanResultTransformer(StoryDTO.class));
	}

	private void noticeDtoCriteria(Criteria criteria) {

		criteria.createAlias("user", "userAlias");
		criteria.createAlias("dynamic", "dynamicAlias", Criteria.LEFT_JOIN);
		criteria.createAlias("smallGroup", "smallGroupAlias", Criteria.LEFT_JOIN);

		//		criteria.addOrder(Order.desc(prefix + "thread"));
		//		criteria.addOrder(Order.asc(prefix + "threadSeq"));
		criteria.setProjection(	Projections.projectionList()

				// Content
				.add(Projections.property("id"), "id")
				.add(Projections.property("mediaCount"), "mediaCount")
				.add(Projections.property("createDate"), "createDate")
				.add(Projections.property("updateDate"), "updateDate")
				.add(Projections.property("createDate"), "arriveDate")
				.add(Projections.property("rawText"), "rawText")
				.add(Projections.property("type"), "type")
				.add(Projections.property("permissionType"), "permissionType")
				.add(Projections.property("smallGroupAlias.id"), "smallGroupId")
				.add(Projections.property("smallGroupAlias.name"), "smallGroupName")
				.add(Projections.property("smallGroupAlias.type"), "smallGroupType")
				.add(Projections.property("dynamicAlias.replyCount"), "replyCount")
				.add(Projections.property("dynamicAlias.feelScore"), "feelScore")
				.add(Projections.property("dynamicAlias.feelCount"), "feelCount")

				// User
				.add(Projections.property("userAlias.id"), "userId")
				.add(Projections.property("userAlias.name"), "userName")
				.add(Projections.property("userAlias.profilePic"), "userProfilePic")

				);

		criteria.setResultTransformer(new AliasToBeanResultTransformer(NoticeDTO.class));
	}


	public List<StoryDTO> getPagedDTOList(Criteria criteria, Stream stream){
		if( stream == null )
			return  criteria.list();

		if( stream.getSize() != null)
			criteria.setMaxResults(stream.getSize());

		if( stream.getGreaterThan() == null)
			return criteria.list();

		if( stream.getGreaterThan() == true ){
			criteria.add( Restrictions.gt(stream.getBaseColumn(), stream.getBaseData()));
		}else{
			criteria.add( Restrictions.lt(stream.getBaseColumn(), stream.getBaseData()));
		}

		return criteria.list();
	}

	public List<NoticeDTO> getNoticePagedDTOList(Criteria criteria, Stream stream){
		if( stream == null )
			return  criteria.list();

		if( stream.getSize() != null)
			criteria.setMaxResults(stream.getSize());

		if( stream.getGreaterThan() == null)
			return criteria.list();

		if( stream.getGreaterThan() == true ){
			criteria.add( Restrictions.gt(stream.getBaseColumn(), stream.getBaseData()));
		}else{
			criteria.add( Restrictions.lt(stream.getBaseColumn(), stream.getBaseData()));
		}

		return criteria.list();
	}


	public List<Long> getPagedIdList(Criteria criteria, Stream stream){
		if( stream == null )
			return  criteria.list();

		if( stream.getSize() != null)
			criteria.setMaxResults(stream.getSize());

		if( stream.getGreaterThan() == null)
			return criteria.list();

		if( stream.getGreaterThan() == true ){
			criteria.add( Restrictions.gt(stream.getBaseColumn(), stream.getBaseData()));
		}else{
			criteria.add( Restrictions.lt(stream.getBaseColumn(), stream.getBaseData()));
		}

		return criteria.list();
	}

	public StoryDTO getStoryDTO(Long id, User user) {
		
		List<Long> userJoinedSmallGroupIds = getUserJoinedSmallGroupIds(user);


		if( userJoinedSmallGroupIds == null || userJoinedSmallGroupIds.size() == 0  )
			return null;

		/*
		 * smallGroup 들 뽑아내기 끝
		 */

		Criteria criteria = getCriteria();

		criteria.createAlias("smallGroupContentAccesses", "smallGroupContentAccessesAlias");

		criteria.add(Restrictions.in("smallGroupContentAccessesAlias.smallGroup.id", userJoinedSmallGroupIds));
		
		criteria.add(Restrictions.eq("this.id", id));

		//criteria.createAlias("smallGroupContentAccesses", "smallGroupContentAccessesAlias");
		storyDtoCriteria(criteria, null);
		criteria.setMaxResults(1);
		return (StoryDTO) criteria.uniqueResult();

	}
	public StoryDTO getSuperStoryDTO( Long id ){
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("this.id", id));

		//criteria.createAlias("smallGroupContentAccesses", "smallGroupContentAccessesAlias");
		storyDtoCriteria(criteria, null);
		criteria.setMaxResults(1);
		return (StoryDTO) criteria.uniqueResult();

	}

	
	public boolean isFeedback(Long id) {
		
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("this.id", id));
		criteria.add(Restrictions.eq("this.feedback", true));
		criteria.setMaxResults(1);
		Object obj = criteria.uniqueResult();
		if( obj == null )
			return false; 
		
		return true;
	}

	public StoryDTO getFeedbackStoryDTO(Long id) {
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("this.id", id));

		//criteria.createAlias("smallGroupContentAccesses", "smallGroupContentAccessesAlias");
		storyDtoCriteria(criteria, null);
		criteria.setMaxResults(1);
		return (StoryDTO) criteria.uniqueResult();

	}

	public List<NoticeDTO> getNotices(Sunny sunny, User user, Long smallGroupId, Stream stream) {
		Criteria criteria = getCriteria();

		criteria.add(Restrictions.eq("this.site", sunny.getSite()));
		criteria.add(Restrictions.eq("this.isNotice", true));
		criteria.add(Restrictions.eq("this.deleteFlag", false));

		if( smallGroupId != null ){
			criteria.add(Restrictions.eq("this.smallGroup.id", smallGroupId));
		}
		criteria.addOrder(Order.desc("this.createDate"));

		noticeDtoCriteria(criteria);
		return getNoticePagedDTOList(criteria, stream);
	}




	public Number getNoticeUnreadCount(Sunny sunny, User user, LastRead lastRead) {

		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("this.site", sunny.getSite()));
		criteria.add(Restrictions.eq("this.isNotice", true));
		criteria.add(Restrictions.eq("this.deleteFlag", false));

		//		if( smallGroupId != null ){
		//			criteria.add(Restrictions.eq("this.smallGroup.id", smallGroupId));
		//		}
		criteria.addOrder(Order.desc("this.createDate"));

		if( lastRead != null )
			criteria.add(Restrictions.gt("createDate", lastRead.getNoticeLastRead()));

		criteria.setProjection(Projections.rowCount());		
		return (Number)criteria.uniqueResult();
	}


	public int getNoticeReadCount(Sunny sunny, Long userId, LastRead lastRead) {
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("this.site", sunny.getSite()));
		criteria.add(Restrictions.eq("this.isNotice", true));
		criteria.add(Restrictions.eq("this.deleteFlag", false));
		
		criteria.createAlias("contentReadUsers", "contentReadUsersAlias");
		
		criteria.add(Restrictions.eq("contentReadUsersAlias.user.id", userId));

		if( lastRead != null )
			criteria.add(Restrictions.gt("createDate", lastRead.getNoticeLastRead()));

		criteria.setProjection(Projections.rowCount());

		Object rowCount = criteria.uniqueResult();
		if( rowCount == null )
			return 0;

		return ((Number) rowCount).intValue();

	}


	public Story getNoticeNewOne(Sunny sunny, User user, LastRead lastRead) {
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("this.site", sunny.getSite()));
		criteria.add(Restrictions.eq("this.isNotice", true));
		criteria.add(Restrictions.eq("this.deleteFlag", false));

		//		if( smallGroupId != null ){
		//			criteria.add(Restrictions.eq("this.smallGroup.id", smallGroupId));
		//		}
		criteria.addOrder(Order.desc("this.createDate"));

		if( lastRead != null )
			criteria.add(Restrictions.gt("createDate", lastRead.getNoticeLastRead()));

		criteria.setMaxResults(1);

		return (Story) criteria.uniqueResult();
	}




	public NotifyInfoDTO getNoticeNotifyDto(Sunny sunny, Long userId, LastRead lastRead) {
		NotifyInfoDTO notifyInfoDto = new NotifyInfoDTO();		

		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("this.site", sunny.getSite()));
		criteria.add(Restrictions.eq("this.isNotice", true));
		criteria.add(Restrictions.eq("this.deleteFlag", false));

		//		if( smallGroupId != null ){
		//			criteria.add(Restrictions.eq("this.smallGroup.id", smallGroupId));
		//		}
		criteria.addOrder(Order.desc("this.createDate"));

		criteria.setProjection(Projections.property("createDate"));
		criteria.setMaxResults(1);		

		Object updateDate = criteria.uniqueResult();

		if( updateDate == null )
			notifyInfoDto.setUpdateDate(null);
		else{
			notifyInfoDto.setUpdateDate( (Date) updateDate );	
		}

		criteria.setProjection(null);

		if( lastRead != null )
			criteria.add(Restrictions.gt("createDate", lastRead.getNoticeLastRead()));

		criteria.setProjection(Projections.rowCount());		

		Object count = criteria.uniqueResult();

		if( count == null ){
			notifyInfoDto.setUnreadCount(0);
		}else{
			notifyInfoDto.setUnreadCount( ((Number) count).intValue() );
		}

		return notifyInfoDto; 

	}




	public int getSiteStoryCount(Long siteId) {
		
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("site.id", siteId));
		criteria.setProjection(Projections.rowCount());
		
		Number number = (Number) criteria.uniqueResult();
		if( number == null )
			return 0;
		
		return number.intValue();
	}










}
