package kr.co.sunnyvale.sunny.service;

import kr.co.sunnyvale.sunny.domain.BookMark;
import kr.co.sunnyvale.sunny.domain.Content;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.extend.Page;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.exception.SimpleSunnyException;
import kr.co.sunnyvale.sunny.repository.hibernate.BookMarkRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.ContentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( value="bookMarkService" )
@Transactional
public class BookMarkService {

	@Autowired
	private UserService userService;
	
	@Autowired
	private BookMarkRepository bookMarkRepository;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private ContentService contentService; 
	
	@Transactional
	public BookMark bookMark(Sunny sunny, Content content, Integer contentType, User user ){
		
		boolean isAlreadyBookMarked = bookMarkRepository.isAlreadyBookMarked(sunny, content.getId(), user.getId());
		if( isAlreadyBookMarked == true ){
			throw new SimpleSunnyException();
		}
		
		BookMark bookMark = new BookMark();
		

		if( contentType == null ){
			content = contentService.getContent(content.getId());
			contentType = content.getType();
		}
		
		bookMark.setContentType( contentType );	
		
		bookMark.setContent(content);
		bookMark.setUser(user);
		bookMark.setSite(sunny.getSite());
		
		bookMarkRepository.save(bookMark);
		return bookMark;
	}

	@Transactional
	public BookMark bookMark(Sunny sunny, BookMark bookmark) {
		BookMark newBookmark = new BookMark();
		newBookmark.setContent(bookmark.getContent());
		newBookmark.setUser(bookmark.getUser());
		newBookmark.setSite(sunny.getSite());
		
		
		if( bookmark.getContentType() == null ){
			Content content = contentService.getContent(bookmark.getContent().getId());
			bookmark.setContentType( content.getType() );	
		}
		
		newBookmark.setContentType( bookmark.getContentType() );
		
		
//		if( bookmark.getTitle() == null || bookmark.getTitle().isEmpty() ){
//			bookmark.setTitle( content.getTitle() );
//		}
		
		newBookmark.setTitle( bookmark.getTitle() );
		newBookmark.setComment( bookmark.getComment() );
		bookMarkRepository.save(newBookmark);
		return newBookmark;
	}

	@Transactional
	public void save(BookMark bookMark) {
		bookMarkRepository.save(bookMark);
	}
	@Transactional(readOnly = true)
	public BookMark select(Long bookMarkId) {
		return bookMarkRepository.select(bookMarkId);
	}

	@Transactional(readOnly = true)
	public boolean isAlreadyBookMark(Sunny sunny, Long contentId, Long userId) {
		return bookMarkRepository.isAlreadyBookMarked(sunny, contentId, userId);
	}

	public void removeBookMark(Sunny sunny, Content content, User user) {
		BookMark bookMark = bookMarkRepository.find( sunny, content, user );
		
		if( bookMark == null ){
			throw new SimpleSunnyException();
		}
		bookMarkRepository.delete(bookMark);
	}

	
	public Page<BookMark> getPagedBookMarks(Sunny sunny, Integer[] types, Long smallGroupId,
			User user, String query, String ordering, Integer page,
			int pageSize) {
		
		return bookMarkRepository.getPagedBookMarks( sunny, types, smallGroupId, user, query, ordering, page, pageSize );
	}

	
}
