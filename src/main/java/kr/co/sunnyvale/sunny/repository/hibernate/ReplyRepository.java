package kr.co.sunnyvale.sunny.repository.hibernate;

import java.util.Date;
import java.util.List;

import kr.co.sunnyvale.sunny.domain.Content;
import kr.co.sunnyvale.sunny.domain.Reply;
import kr.co.sunnyvale.sunny.domain.extend.Stream;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 * 	{Hibernate}
 *	
 *<p>
 *	컨텐츠에 등록되는 커멘트 Repository
 *
 * @author mook
 *
 *
 */

@Repository(value = "replyRepository")
public class ReplyRepository extends HibernateGenericRepository<Reply>{
	public ReplyRepository(){

	}

	public Date getCreateDate(Long replyId) {
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("id", replyId));
		criteria.setProjection(	Projections.projectionList().add( Projections.property("createDate") ));
		
		return (Date) criteria.uniqueResult();
	}

	public List<Reply> getListByContent(Long contentId, Stream stream) {
		Criteria criteria = getCriteria();
//		criteria.setFetchMode("content", FetchMode.SELECT);
//		criteria.createAlias("content", "storyAlias");
//		criteria.setFetchMode("user", FetchMode.JOIN);
		criteria.add(Restrictions.eq("content.id", contentId));
		criteria.addOrder(Order.desc("createDate"));
		
		return getPagedList(criteria, stream);
	}

	public List<Reply> getRecentReceiversReplys(Content content, Reply reply, int receiverCount) {
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("content", content));
		criteria.add(Restrictions.ne("user", content.getUser()));
		criteria.add(Restrictions.ne("user", reply.getUser()));
		criteria.addOrder(Order.desc("createDate"));
		criteria.setMaxResults(receiverCount);
		return criteria.list();
		
	}
}
