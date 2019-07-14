package kr.co.sunnyvale.sunny.service.impl;

import java.util.List;
import java.util.Set;

import kr.co.sunnyvale.sunny.domain.Content;
import kr.co.sunnyvale.sunny.domain.ContentAndTag;
import kr.co.sunnyvale.sunny.domain.DefaultTag;
import kr.co.sunnyvale.sunny.domain.SmallGroup;
import kr.co.sunnyvale.sunny.domain.Tag;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.repository.hibernate.ContentAndTagRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.DefaultTagRepository;
import kr.co.sunnyvale.sunny.repository.hibernate.TagRepository;
import kr.co.sunnyvale.sunny.service.TagService;
import kr.co.sunnyvale.sunny.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@Service( value="tagService" )
@Transactional
public class TagServiceImpl implements TagService{

	@Autowired
	UserService userService;

	@Autowired
	TagRepository tagRepository;

	@Autowired
	ContentAndTagRepository contentAndTagRepository;

	
	@Autowired
	private DefaultTagRepository defaultTagRepository ;
	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.TagService#save(kr.co.sunnyvale.sunny.domain.Tag)
	 */
	@Override
	@Transactional
	public void save(Tag tag){
		tagRepository.save(tag);
	}

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.TagService#update(kr.co.sunnyvale.sunny.domain.Tag)
	 */
	@Override
	@Transactional
	public void update(Tag tag){
		tagRepository.update(tag);
	}

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.TagService#find(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public Tag find(Long tagId){
		return tagRepository.select(tagId);
	}

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.TagService#getTags(java.lang.Long, kr.co.sunnyvale.sunny.domain.extend.Stream)
	 */
	@Override
	@Transactional(readOnly = true)
	public Set<Tag> getTags(Long contentId, Stream stream){
		List<ContentAndTag> contentAndTags = contentAndTagRepository.getMatchRelation(contentId, stream);
		Set<Tag> retTags = Sets.newHashSet();
		/*
		 * Tag 자체를 Join 하게 되면 많은 Content Join 때문에 
		 * contentAndTag 자체를 가져오는 방식으로 변경
		 */
		for( ContentAndTag contentAndTag : contentAndTags ){
			retTags.add( contentAndTag.getTag() );
		}
		return retTags ;
	}

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.TagService#findAndGenerateTags(kr.co.sunnyvale.sunny.domain.SmallGroup, int, java.util.Set)
	 */
	@Override
	@Transactional
	public Set<Tag> findAndGenerateTags( SmallGroup group, int contentType, Set<String> tagTitles ){
		Set<Tag> tags = Sets.newHashSet();

		for (String tagTitle : tagTitles) {
			Tag tag = tagRepository.findByTitle(group, contentType, tagTitle);
			if (tag == null) {
				tag = put( group, contentType, tagTitle);
			} 
			tags.add(tag);
		}
		return tags;
	}

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.TagService#getMatchTags(kr.co.sunnyvale.sunny.domain.SmallGroup, int, java.lang.String, kr.co.sunnyvale.sunny.domain.extend.Stream)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Tag> getMatchTags(SmallGroup group, int contentType, String input, Stream stream){
		return tagRepository.findMatchTitle(group, contentType, input, stream);
	}

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.TagService#put(kr.co.sunnyvale.sunny.domain.SmallGroup, int, java.lang.String)
	 */
	@Override
	@Transactional
	public Tag put(SmallGroup group, int contentType, String tagTitle) {
		Tag tag = new Tag(group, contentType);
		tag.setTitle(tagTitle);
		tagRepository.save(tag);
		return tag;
	}

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.TagService#findByTitle(kr.co.sunnyvale.sunny.domain.SmallGroup, int, java.lang.String)
	 */
	@Override
	@Transactional
	public Tag findByTitle(SmallGroup group, int contentType, String title) {
		return tagRepository.findByTitle( group, contentType, title );
	}

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.TagService#addReferenceCount(java.lang.Long)
	 */
	@Override
	@Transactional
	public void addReferenceCount(Long id) {
		Tag tag = tagRepository.select(id);
		tag.setReferenceCount( tag.getReferenceCount() + 1 );
	}

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.TagService#minusReferenceCount(java.lang.Long)
	 */
	@Override
	@Transactional
	public void minusReferenceCount(Long id) {
		Tag tag = tagRepository.select(id);
		tag.setReferenceCount( tag.getReferenceCount() - 1 );
	}

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.TagService#getTagsOrderByReferenceCount(java.lang.Long, int)
	 */
	@Override
	@Transactional
	public List<Tag> getTagsOrderByReferenceCount(Long groupId, int type) {
		return tagRepository.getTagsOrderByReferenceCount(groupId, type);
	}

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.TagService#detag(java.util.Set)
	 */
	@Override
	@Transactional
	public void detag(Set<Tag> existTags) {
		for( Tag tag : existTags ){
			minusReferenceCount(tag.getId());
		}
	}

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.TagService#tag(java.util.Set)
	 */
	@Override
	@Transactional
	public void tag(Set<Tag> persistentTags) {
		for( Tag tag : persistentTags ){
			addReferenceCount(tag.getId());
		}
	}

	/* (non-Javadoc)
	 * @see kr.co.sunnyvale.sunny.service.impl.TagService#getContentNotice(kr.co.sunnyvale.sunny.domain.SmallGroup, int, kr.co.sunnyvale.sunny.domain.extend.Stream)
	 */
	@Override
	@Transactional
	public List<Content> getContentNotice(SmallGroup group, int contentType, Stream stream) {
		List<ContentAndTag> contentAndTags = contentAndTagRepository.getContentNotice(group, contentType, "공지사항", stream);
		List<Content> retContents = Lists.newArrayList();
		/*
		 * Tag 자체를 Join 하게 되면 많은 Content Join 때문에 
		 * contentAndTag 자체를 가져오는 방식으로 변경
		 */
		for( ContentAndTag contentAndTag : contentAndTags ){
			retContents.add( contentAndTag.getContent() );
		}
		return retContents ;
	}

	@Override
	public List<Tag> getTopTags(Long smallGroupId) {


		return tagRepository.getTopTags( smallGroupId );
	}

	@Override
	public List<Tag> getSmallGroupTags(Long smallGroupId) {

		return tagRepository.getSmallGroupTags( smallGroupId );
	}

	@Override
	public List<String> getSmallGroupTagNames(Long smallGroupId) {
		return tagRepository.getSmallGroupTagNames( smallGroupId );
	}

	@Override
	@Transactional
	public void setSmallGroupComposerTags(Long smallGroupId, List<Tag> tags) {

		tagRepository.updateToNormalTags(smallGroupId);
		int orderingCount = 0;
		SmallGroup smallGroup = new SmallGroup(smallGroupId);
		for( Tag tag : tags ){
			Tag persistentTag = tagRepository.findByTitle(smallGroup, Content.TYPE_STORY, tag.getTitle());
			if (persistentTag == null) {
				persistentTag = put( smallGroup, Content.TYPE_STORY, tag.getTitle());
			} 
			persistentTag.setAdminSelected(true);
			persistentTag.setAdminOrdering(orderingCount++);
			update(persistentTag);
		}
	}

	@Override
	public List<Tag> getComposerTags(Long smallGroupId) {
		
		return tagRepository.getComposerTags( smallGroupId );
	}

	@Override
	@Transactional
	public void generateDefaultLobbyTags(Long smallGroupId) {
		
		List<DefaultTag> defaultTags = defaultTagRepository.getAll(null);
		SmallGroup smallGroup = new SmallGroup(smallGroupId);
		for( DefaultTag defaultTag : defaultTags ){
			Tag tag = new Tag(smallGroup, defaultTag.getContentType());
			tag.setTitle( defaultTag.getTitle() );
			tag.setAdminSelected(true);
			tag.setAdminOrdering(defaultTag.getAdminOrdering());
			save(tag);
		}
		
		
	}

}

