package kr.co.sunnyvale.sunny.repository.hibernate;

import kr.co.sunnyvale.sunny.domain.Draft;
import kr.co.sunnyvale.sunny.domain.DraftSmallGroupApproval;

import org.springframework.stereotype.Repository;


@Repository(value = "draftSmallGroupApprovalRepository")
public class DraftSmallGroupApprovalRepository extends HibernateGenericRepository<DraftSmallGroupApproval>{
	
	
	public DraftSmallGroupApprovalRepository(){

	}

	public void deleteFromDraft(Draft persistentDraft) {
		String hqlUpdate = "delete DraftSmallGroupApproval where draft.id = :draftId";
		int updatedEntities = getCurrentSession().createQuery( hqlUpdate )
		        .setLong( "draftId", persistentDraft.getId() )
		        .executeUpdate();
		getCurrentSession().clear(); 
		
	}


}
