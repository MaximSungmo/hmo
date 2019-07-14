package kr.co.sunnyvale.sunny.repository.hibernate;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.exception.SimpleSunnyException;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * {Hibernate}
 *	
 *<p>
 *	기본 기능들인 CRUD 들을 제네릭하게 쓸 수 있는 GenericRepository	
 *
 * @author mook
 *
 *	
 *
 */

public class HibernateGenericRepository<E> implements GenericRepository<E> {

	protected Class<E> entityClass;

	@Autowired
	protected SessionFactory sessionFactory;
    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
    protected Criteria getCriteria() {
    	return sessionFactory.getCurrentSession().createCriteria(entityClass);
    }

	// 현재 Repository 에 접근하는 Domain 클래스 가져오기. 봄싹 소스 참고
	@SuppressWarnings("unchecked")
	public HibernateGenericRepository() {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		Type type = genericSuperclass.getActualTypeArguments()[0];
		if (type instanceof ParameterizedType) {
			this.entityClass = (Class<E>) ((ParameterizedType) type).getRawType();
		} else {
			this.entityClass = (Class<E>) type;
		}
	}
	
    protected void flushAndClear(){
    	Session session = getCurrentSession();
    	session.flush();
    	session.clear();
    }

    @Override
	public void save(E entity) {
    	if( entity == null )
    		throw new SimpleSunnyException();
		getCurrentSession().save(entity);
		flushAndClear();
	}


	@SuppressWarnings("unchecked")
	@Override
	public E select(Serializable id){
		return (E) getCurrentSession().get(entityClass, id);
	}
    
	@Override
	public List<E> getAll(Stream page) {
		Criteria criteria = getCurrentSession().createCriteria(entityClass);
		return getPagedList(criteria, page);
	}

	@SuppressWarnings("unchecked")
	public List<E> getAll() {
		return getCurrentSession().createCriteria( entityClass ).list();
	}

	@Override
	public void delete(E entity){
		getCurrentSession().delete(entity);
		flushAndClear();
	}
	
	@Override
	public void update(E entity){
		getCurrentSession().update( entity );
		flushAndClear();
	}
	
	@Override
	public void flush(){
		getCurrentSession().flush();
	}
	
	@Override
    public void clear() {
        getCurrentSession().clear();
    }

	@SuppressWarnings( "unchecked" )
	@Override
	public E merge(E entity){
		return (E) getCurrentSession().merge(entity);
	}

    @SuppressWarnings("unchecked")
	@Override
	public E findUniqByObject(String columnName, Object object) {
    	Criteria criteria = getCurrentSession().createCriteria(this.entityClass);
    	criteria.add(Restrictions.eq(columnName, object));
    	criteria.setMaxResults(1);
		return (E) criteria.uniqueResult();
	}
	
    @Override
	public List<E> findListByObject(String columnName, Object object, Stream page){
    	Criteria criteria = getCurrentSession().createCriteria(this.entityClass);
    	criteria.add(Restrictions.eq(columnName, object));
		return  getPagedList(criteria, page);
	}

	@Override
	public Object findColumnByObject(String returnColumn, String whereColumn,
			Object whereParameter) {
    	Criteria criteria = getCurrentSession().createCriteria(this.entityClass);
    	criteria.add(Restrictions.eq(whereColumn, whereParameter));
		criteria.setProjection(Projections.property(returnColumn));
		return  criteria.uniqueResult();
	}
	

	public Object getNextObjectById(Object beforeId) {
    	Criteria criteria = getCurrentSession().createCriteria(this.entityClass);
		criteria.add(Restrictions.gt("id", beforeId));
		criteria.setMaxResults(1);
		return criteria.uniqueResult();

	}
	
	@Override
	public List<E> findCandidateListByString(String columnName, String input, Stream page) {
    	Criteria criteria = getCurrentSession().createCriteria(this.entityClass);
    	criteria.add( Restrictions.like(columnName, input, MatchMode.ANYWHERE) );

    	return (List<E>) getPagedList(criteria, page);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<E> getPagedList(Criteria criteria, Stream stream){
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
    	criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
    	
    	return criteria.list();
	}

	public Page<E> getAll(Integer pageNumber, int pageSize) {
		Criteria criteria = getCurrentSession().createCriteria(entityClass);

		return getPagedList(criteria, pageNumber, pageSize);
	}
	
    @SuppressWarnings("unchecked")
    public Page<E> getPagedList(Criteria criteria, Integer pageNumber, Integer pageSize){
    	//System.out.println("pageNumber : " + pageNumber);
    	if( pageNumber == null ) {
    		pageNumber = new Integer(1);
    	}
    	if( pageNumber < 1){
    		pageNumber = 1; 
    	}
		Criteria countCriteria = criteria;
		Criteria contentCriteria = criteria;
    	
		
		countCriteria.setProjection(Projections.distinct(Projections.rowCount()));
		Long count = 0L;
		try{
			count = (Long)countCriteria.uniqueResult();
		}catch(Exception ex){}
		//System.out.println("count : " + count);
		
		contentCriteria.setProjection(null);
		Collection<Long> ids = getIDs(contentCriteria, pageNumber, pageSize);
		
		contentCriteria.setProjection(null);
		contentCriteria.setMaxResults(10000);
		contentCriteria.setFirstResult(0);
		
		//System.out.println("ids:" + ids.size());
		 List<E> contents = new ArrayList<E>();
	    if(!ids.isEmpty()) {
	        	contentCriteria
	            .add(Restrictions.in("id", ids))
	            .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	        	contents = contentCriteria.list();
	    }   
	    
		return new Page<E>(contents, pageNumber, pageSize, count);
    	
    }
    
    private Collection<Long> getIDs(Criteria contentCriteria, int pageNumber, int pageSize) {
        contentCriteria.setProjection(Projections.distinct(Projections.id()));

        if(pageNumber >= 0 && pageSize > 0) {
        	contentCriteria.setMaxResults(pageSize);
        	contentCriteria.setFirstResult((pageNumber -1) * pageSize);
        }

        @SuppressWarnings("unchecked")
        Collection<Long> ids = contentCriteria.list();
        return ids;
    }

	/**
	 * setDeleteFlag메소드가 있는 엔티티의 경우 deleteFlag의 값을 true로 변경후 수정한다.
	 * @param e
	 */
	public void setDeleteFlag(E e) {
		try {
			Method setDeleteFlagMethod = e.getClass().getMethod("setDeleteFlag", new Class[]{Boolean.TYPE});
			setDeleteFlagMethod.invoke(e, new Object[]{new Boolean(true)});
			update(e);
		} catch (Exception e1) {
			throw new SimpleSunnyException("setDeleteFlag.message");
		} 
		
	}
    
}
