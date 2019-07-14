package kr.co.sunnyvale.sunny.service;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.Content;
import kr.co.sunnyvale.sunny.domain.Feel;
import kr.co.sunnyvale.sunny.domain.FeelAndContent;
import kr.co.sunnyvale.sunny.domain.FeelAndContentAndUser;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;

import org.springframework.transaction.annotation.Transactional;

import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.KeyGenerator;
import com.googlecode.ehcache.annotations.Property;

public interface FeelService {

	public List<Feel> getAll();

	public void save(Feel feel);

	public void delete(Integer id);

	public Feel getFeel(int id);

	public Object getColumnFromObject(String returnColumn, String whereColumn,
			Object whereParameter);

	public void feelContent(User user, Content content, Feel feel);

	public void undoFeelAndContent(User user, Content content, Feel alreadyFeel);

	public Long getUserFeel(User user, Content content);

	public List<FeelAndContent> getFeelAndContents(Long contentId, Stream stream);

	public Feel getFeelFromContentUser(User user, Content content);

	public void feel(Sunny sunny, Long userId, Long contentId, int evalId);

	public List<FeelAndContentAndUser> getFeelUsers(Long contentId,
			Stream stream);

	public List<FeelAndContentAndUser> getFeelUsers(Long contentId,
			Integer feelId, Stream stream);

	public List<String> getFeelNames(Long contentId, Stream stream);

	public List<String> getFeelNames(Long contentId, Integer feelId,
			Stream stream);

}