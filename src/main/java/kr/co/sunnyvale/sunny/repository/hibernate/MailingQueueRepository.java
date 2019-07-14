package kr.co.sunnyvale.sunny.repository.hibernate;

import kr.co.sunnyvale.sunny.domain.MailingQueue;
import kr.co.sunnyvale.sunny.domain.extend.Page;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

/**
 * 	{Hibernate}
 *	
 *<p>
 *	
 *
 * @author mook
 *
 *
 */

@Repository(value = "mailingQueueRepository")
public class MailingQueueRepository extends HibernateGenericRepository<MailingQueue>{
	public MailingQueueRepository(){

	}

	public void delete(Long id) {
		MailingQueue mq = select(id);
		delete(mq);
	}

	public Page<MailingQueue> getPagedList(String queryName, Integer page,
			int pageSize) {
		
		Criteria criteria = getCriteria();
		criteria.addOrder(Order.desc("createDate"));
		return getPagedList(criteria, page, pageSize);
	}

}
