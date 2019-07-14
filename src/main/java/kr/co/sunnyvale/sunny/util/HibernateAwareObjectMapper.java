package kr.co.sunnyvale.sunny.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate3.Hibernate3Module;
 
public class HibernateAwareObjectMapper extends ObjectMapper {
 
    /**
	 * 
	 */
	private static final long serialVersionUID = 459853763970724041L;

	public HibernateAwareObjectMapper() {
        Hibernate3Module hm = new Hibernate3Module();
        registerModule(hm);
    }
}