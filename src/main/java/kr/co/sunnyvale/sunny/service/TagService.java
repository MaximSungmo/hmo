package kr.co.sunnyvale.sunny.service;

import java.util.List;
import java.util.Set;

import kr.co.sunnyvale.sunny.domain.Content;
import kr.co.sunnyvale.sunny.domain.SmallGroup;
import kr.co.sunnyvale.sunny.domain.Tag;
import kr.co.sunnyvale.sunny.domain.extend.Stream;

import org.springframework.transaction.annotation.Transactional;

public interface TagService {

	public void save(Tag tag);

	public void update(Tag tag);

	public Tag find(Long tagId);

	public Set<Tag> getTags(Long contentId, Stream stream);

	public Set<Tag> findAndGenerateTags(SmallGroup group, int contentType,
			Set<String> tagTitles);

	public List<Tag> getMatchTags(SmallGroup group, int contentType,
			String input, Stream stream);

	public Tag put(SmallGroup group, int contentType, String tagTitle);

	public Tag findByTitle(SmallGroup group, int contentType, String title);

	public void addReferenceCount(Long id);

	public void minusReferenceCount(Long id);

	public List<Tag> getTagsOrderByReferenceCount(Long groupId, int type);

	public void detag(Set<Tag> existTags);

	public void tag(Set<Tag> persistentTags);

	public List<Content> getContentNotice(SmallGroup group, int contentType,
			Stream stream);

	public List<Tag> getTopTags(Long id);

	public List<Tag> getSmallGroupTags(Long smallGroupId );

	public List<String> getSmallGroupTagNames(Long smallGroupId);

	public void setSmallGroupComposerTags(Long smallGroupId, List<Tag> tags);

	public List<Tag> getComposerTags(Long smallGroupId);

	public void generateDefaultLobbyTags(Long smallGroupId);

}