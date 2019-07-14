package kr.co.sunnyvale.sunny.repository.hibernate;

import java.io.Serializable;
import java.util.Properties;

import kr.co.sunnyvale.sunny.domain.Sequence;

import org.apache.commons.lang.StringUtils;
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
 *	각종 Sequence 를 자동으로 생성해주는 툴. 제네릭 Repository 를 상속받는 정식 Repository 는 아니지만
 *	Criteria 를 사용한다는 점에서 Repository 로 분류	
 *
 * @author mook
 *
 *
 */


public class UserIdGenerator  implements IdentifierGenerator, Configurable  {

	
    private String prefix; // = User.PREFIX_NUMBER_USERID;
    private String serviceUserId; // = "yacamp.userId";
    Long seq = 1L;

    @Override
    public void configure(Type arg0, Properties props, Dialect arg2 )  
            throws MappingException {  
    	setPrefix( props.getProperty( "prefix" ) );  
    	setServiceUserId( props.getProperty( "serviceUserId" ) );
        
    }  
    @Override
	@Transactional
    public Serializable generate(SessionImplementor session, Object object){
		
    	System.out.println("SEQUENCE::: " + prefix + " serverUSerId : " + serviceUserId );
    	
		Session currentSession = (Session) session;
		Criteria criteria = currentSession.createCriteria(Sequence.class);
		criteria.add(Restrictions.eq("seqId", serviceUserId));
		Sequence sequence = (Sequence) criteria.uniqueResult();
		if( sequence != null) {
			seq = sequence.getSeqCount() + 1;
			sequence.setSeqCount(seq);
			currentSession.update(sequence);
		}else{
			sequence = new Sequence();
			sequence.setSeqId(serviceUserId);
			sequence.setSeqCount(seq);
			currentSession.save(sequence);
		}
		
		currentSession.flush();
      	String paddedSequence = StringUtils.leftPad(seq.toString(), 9, '0');
        return prefix + paddedSequence;
	
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getServiceUserId() {
		return serviceUserId;
	}
	public void setServiceUserId(String serviceUserId) {
		this.serviceUserId = serviceUserId;
	}
}