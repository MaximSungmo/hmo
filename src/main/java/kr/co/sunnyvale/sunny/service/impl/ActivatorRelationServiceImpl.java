package kr.co.sunnyvale.sunny.service.impl;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.ActivatorRelation;
import kr.co.sunnyvale.sunny.domain.Notification;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.repository.hibernate.ActivatorRelationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( value="activatorRelationService" )
@Transactional
public class ActivatorRelationServiceImpl {

	
	@Autowired
	ActivatorRelationRepository activatorRelationRepository;

	@Transactional
	public boolean alreadyJoinActivator(Notification notification,
			User activator) {
		return activatorRelationRepository.alreadyJoinActivator(notification, activator);
	}

	@Transactional
	public void addActivator(Notification notification, User activator) {
		ActivatorRelation activatorRelation = new ActivatorRelation();
		activatorRelation.setNotification(notification);
		activatorRelation.setUser(activator);
		activatorRelation.setName( activator.getName() );
		activatorRelationRepository.save( activatorRelation );
	}

	@Transactional(readOnly = true)
	public ActivatorRelation find(Notification notification, User user) {
		return activatorRelationRepository.find( notification, user);
	}
	
	@Transactional
	public void removeActivator(ActivatorRelation activatorRelation) {
		activatorRelationRepository.delete( activatorRelation );
		
	}
	
	@Transactional( readOnly = true)
	public List<ActivatorRelation> getCurrentActivators(Notification notification, int fetchCount) {
		return activatorRelationRepository.getCurrentActivators( notification, fetchCount );
	}

	
}
