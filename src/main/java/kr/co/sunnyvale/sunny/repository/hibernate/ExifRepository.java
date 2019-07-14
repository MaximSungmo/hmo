package kr.co.sunnyvale.sunny.repository.hibernate;

import kr.co.sunnyvale.sunny.domain.Exif;
import kr.co.sunnyvale.sunny.repository.hibernate.HibernateGenericRepository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository(value = "exifRepository")
@Transactional
public class ExifRepository extends HibernateGenericRepository<Exif> {
	
	public ExifRepository() {	
	
	}
 	

}
