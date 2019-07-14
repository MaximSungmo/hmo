package kr.co.sunnyvale.sunny.repository.hibernate;

import java.io.Serializable;
import java.util.List;

import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.Stream;

import org.hibernate.Criteria;


public interface GenericRepository<E> {
	
	void save(E entity);

	E select(Serializable id);
	
	void update(E entity);

	E merge(E entity);
	
	void delete(E entity);
	
	void flush();

    void clear();
    
	List<E> getAll(Stream page);
	
	E findUniqByObject(String columnName, Object object);
	
	List<E> findListByObject(String columnName, Object object, Stream page);
	
	Object findColumnByObject(String returnColumn, String whereColumn, Object whereParameter);

	List<E> findCandidateListByString(String columnName, String input, Stream page);

	List<E> getPagedList(Criteria criteria, Stream stream);

	Page<E> getPagedList(Criteria criteria, Integer pageNumber, Integer pageSize);
}