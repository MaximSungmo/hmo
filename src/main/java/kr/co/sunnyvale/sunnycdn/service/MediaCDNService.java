package kr.co.sunnyvale.sunnycdn.service;

import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import kr.co.sunnyvale.sunny.domain.Draft;
import kr.co.sunnyvale.sunny.domain.Media;
import kr.co.sunnyvale.sunny.domain.Site;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.util.StringUtils;
import kr.co.sunnyvale.sunny.repository.hibernate.MediaRepository;
import kr.co.sunnyvale.sunnycdn.util.CdnUtil;
import kr.co.sunnyvale.sunnycdn.util.NumericUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.ConfigurableMimeFileTypeMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service(value = "mediaCDNService")
@Transactional
public class MediaCDNService {

	private static final String uploadServerPrefix = "s";
	private static final int uploadServerList[] = { 0, 1, 2 };


	@Autowired
	MediaRepository mediaRepository;

	@Autowired
	ExifService exifService;

	@Autowired
	FileService fileService;

	@Autowired
	private MessageSource messageSource;

	@Transactional(readOnly = true)
	public List<Media> getAllList() {
		return mediaRepository.getAll();
	}

	@Transactional
	public void save(Media media) {
		mediaRepository.save(media);
	}

	/**
	 * 업로드 된 파일에 대한 정보들을 취합해서 Media 에 넣어준다.
	 * 파일이 저장되거나 하진 않고 그냥 domain 파싱만 함. 
	 * @param site
	 * @param draftId 
	 * @param file
	 * @param user
	 * @param type
	 * @return
	 */
	private Media parseToMedia(Site site, Long draftId, MultipartFile file, User user) {

		String contentType = file.getContentType();

		// http Content-Type format "type/sub-type"
		StringTokenizer tokenizer = new StringTokenizer(contentType, "/");

		// 사진은 image 나머지는??
		String bigType = tokenizer.nextToken();
		Media media = new Media(site);

		media.setMediaType(Media.TYPE_OTHER_FILE);
		
		if (bigType.equals("image")) {
			media.setMediaType(Media.TYPE_IMAGE);

			//String extName = tokenizer.nextToken();
			// errors.rejectValue("unsupported", "error.file.unsupported");
		}

		String fileName = file.getOriginalFilename();
		String serverPosition = uploadServerPrefix
				+ uploadServerList[NumericUtil.randomLimit(2)];
		// Media media = new Media(site);
		media.setSize( (int) file.getSize() );
		media.setFileName(fileName);
		media.setExtName(StringUtils.getExtention(fileName));
		
		if( media.getExtName() != null ){
			if(Pattern.compile("doc|docx").matcher(media.getExtName()).matches() ){
				media.setMediaType(Media.TYPE_WORD);
			}else if( Pattern.compile("xls|xlsx").matcher(media.getExtName()).matches() ){
				media.setMediaType(Media.TYPE_EXCEL);
			}else if( Pattern.compile("ppt|pptx").matcher(media.getExtName()).matches() ){
				media.setMediaType(Media.TYPE_POWERPOINT);
			}else if( Pattern.compile("hwp").matcher(media.getExtName()).matches() ){
				media.setMediaType(Media.TYPE_HWP);
			}else if( Pattern.compile("pdf").matcher(media.getExtName()).matches() ){
				media.setMediaType(Media.TYPE_PDF);
			}
		}
		
		if( media.getExtName() == null ){
			media.setContentType(file.getContentType());
		}else if( CdnUtil.extToContentType.get(media.getExtName()) != null ){
			media.setContentType(CdnUtil.extToContentType.get(media.getExtName()));
		}else{
			//if(Pattern.compile("doc|docx").matcher(media.getExtName()).matches() ){
			ConfigurableMimeFileTypeMap mimeMap = new ConfigurableMimeFileTypeMap();
			media.setContentType(mimeMap.getContentType(fileName));
			
		}
//			media.setContentType("application/msword");	
//		}
		
		
		media.setUser(user);
		media.setPosition(serverPosition);
		media.setRelativePath(media.getRelativePath());
		
		if( draftId != null ){
			media.setDraft( new Draft(draftId) );
		}
		return media;
	}

	@Transactional(readOnly = true)
	private Media select(Media media) {
		return select(media.getId());
	}

	@Transactional(readOnly = true)
	private Media select(long mediaId) {
		return mediaRepository.select(mediaId);
	}

	@Transactional(readOnly = true)
	public String getRelativeConvertedPath(long mediaId, String size) {
		Media media = select(mediaId);
		return media.getRelativeConvertedPath(size);
	}

	

	// @Transactional
	// public Media generateProfileMedias(MultipartFile file, String userId,
	// String domain){
	//
	// Media media = generate(file, userId, Media.TYPE_PROFILE);
	// media.setDomain(domain);
	//
	// BufferedImage convertedMedia = convertMedia(file, media);
	//
	// boolean successResize = retouchService.resizeProfiles(convertedMedia,
	// media);
	//
	// if(successResize == false){
	// return null;
	// }
	//
	//
	// // return getFrontPath(media);
	// return media;
	//
	// }

	@Transactional
	public Media save(Site site, Long draftId, MultipartFile file, User user) {
		
		Media media = parseToMedia(site, draftId, file, user);
		
		mediaRepository.save(media);
		mediaRepository.flush();
		mediaRepository.clear();
		
		return media;

	}

	@Transactional
	public Media findById(Long mediaId) {
		return mediaRepository.select(mediaId);
	}

	// @Transactional
	// public Media generateCoverMedias(MultipartFile file, String userId,
	// String domain) {
	// Media media = generate(file, userId, Media.TYPE_COVER);
	// media.setDomain(domain);
	//
	// BufferedImage convertedMedia = convertMedia(file, media);
	//
	// boolean successResize = retouchService.resizeCovers(convertedMedia,
	// media);
	//
	// if(successResize == false){
	// return null;
	// }
	// return media;
	//
	// }


}
