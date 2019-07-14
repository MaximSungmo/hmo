package kr.co.sunnyvale.sunny.repository.hibernate;

import java.io.Serializable;
import java.util.Properties;

import kr.co.sunnyvale.sunny.domain.Sequence;

import org.hibernate.Criteria;
import org.hibernate.MappingException;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.type.Type;
import org.springframework.transaction.annotation.Transactional;
/**
 * {Hibernate}
 *	
 *<p>
 *	User 의 Sequence 를 자동으로 생성해주는 툴. 제네릭 Repository 를 상속받는 정식 Repository 는 아니지만
 *	Criteria 를 사용한다는 점에서 Repository 로 분류	
 *
 * @author mook
 *
 *
 */


public class ContentIdGenerator  implements IdentifierGenerator, Configurable {


    private String serviceSequenceId; 
    Long seq = 1L;
    @Override
    public void configure(Type arg0, Properties props, Dialect arg2 )  
            throws MappingException {  
    	setServiceSequenceId( props.getProperty( "serviceSequenceId" ) );
        
    }  
    @Override
	@Transactional
    public Serializable generate(SessionImplementor session, Object object){
		
		Session currentSession = (Session) session;
		
		Criteria criteria = currentSession.createCriteria(Sequence.class);
		
		criteria.add(Restrictions.eq("seqId", serviceSequenceId));
		
		Sequence sequence = (Sequence) criteria.uniqueResult();
		if( sequence != null) {
			seq = sequence.getSeqCount() + 1;
			sequence.setSeqCount(seq);
			currentSession.update(sequence);
		}else{
			sequence = new Sequence();
			sequence.setSeqId(serviceSequenceId);
			sequence.setSeqCount(seq);
			currentSession.save(sequence);
		}
		
		currentSession.flush();
      	return seq;
	
	}
	public String getServiceSequenceId() {
		return serviceSequenceId;
	}
	public void setServiceSequenceId(String serviceSequenceId) {
		this.serviceSequenceId = serviceSequenceId;
	}
    
}