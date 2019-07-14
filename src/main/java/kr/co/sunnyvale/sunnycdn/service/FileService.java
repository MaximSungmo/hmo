package kr.co.sunnyvale.sunnycdn.service;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

import javax.swing.ImageIcon;

import kr.co.sunnyvale.sunny.domain.Exif;
import kr.co.sunnyvale.sunny.domain.Media;
import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.repository.hibernate.MediaRepository;
import kr.co.sunnyvale.sunnycdn.service.exception.FileServiceException;
import kr.co.sunnyvale.sunnycdn.service.exception.MediaServiceException;

import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service( value="fileService" )
public class FileService {

	private String fileAbsolutePath;
	
	@Autowired
	private MessageSource messageSource;

	@Autowired
	private MediaRepository mediaRepository;

	@Autowired
	ExifService exifService;

	@Autowired
	RetouchService retouchService;
	
	@Autowired
	public FileService(@Value("#{ imageProperties['cdn.fileAbsolutePath']}")String fileAbsolutePath){
		this.fileAbsolutePath  = fileAbsolutePath;
	}
	
	public FileService(){
		
	}
	
	public boolean createRealFile(File outputFile){
		if( outputFile.exists() == false ){
			
			File parentDir = new File(outputFile.getParent());
			if(parentDir.exists() == false ){
				
				parentDir.mkdirs();
			}
			
			try {
				outputFile.createNewFile();
				
			} catch (IOException e) {
				throw new FileServiceException(messageSource.getMessage("error.file.create", new String[]{ "서버에서 파일을 저장하는 중 문제가 발생했습니다."}, Locale.getDefault()));
			}
			return true;
		}else{
			throw new FileServiceException(messageSource.getMessage("error.file.exist", new String[]{ "CDN 파일 저장 서비스에서 문제가 발생했습니다."}, Locale.getDefault()));
		}
	}
	
	public File copyOriginalFile(MultipartFile file, Media image){
		String originalRelativePath = image.getRelativeConvertedPath(retouchService.getOriginalPostFix());
		
		File outputFile = new File(fileAbsolutePath + originalRelativePath);
		createRealFile(outputFile);
		
		try {
			file.transferTo(outputFile);
		} catch (IllegalStateException e) {
			throw new MediaServiceException(messageSource.getMessage(
					"error.media", new String[] { "이미지를 저장하는 중 문제가 발생했습니다." },
					Locale.getDefault()));
		} catch (IOException e) {
			throw new MediaServiceException(messageSource.getMessage(
					"error.media", new String[] { "이미지를 저장하는 중 문제가 발생했습니다." },
					Locale.getDefault()));
		}
		return outputFile;
	}
	
	public File newImageFile(Media image, String type) {
		String relativePath = image.getRelativeConvertedPath(type);
		File outputFile = new File(fileAbsolutePath + relativePath);
		createRealFile(outputFile);
		return outputFile;

	}

	@Transactional
	public boolean  save(Media media, MultipartFile file, User user) throws IOException {
		
		
		BufferedInputStream bfInputStream = new BufferedInputStream(file.getInputStream());
		
		File originalFile = copyOriginalFile(file, media);
		
		if( media.getMediaType() == Media.TYPE_IMAGE ){
			BufferedImage convertedImage = convertBufferedImage(file, originalFile, bfInputStream,media);
			boolean successResize = retouchService.resizeStories(convertedImage,media);
			return successResize;
//			if (successResize == false) {
//				//return null;
//			}
		}
		return true;
	}
	

	public void saveProfile(Media media, MultipartFile file, User user) throws IOException {
		
		if( media.getMediaType() != Media.TYPE_IMAGE ){
			throw new RuntimeException();
		}
		BufferedInputStream bfInputStream = new BufferedInputStream(file.getInputStream());
		
		File originalFile = copyOriginalFile(file, media);
		
		BufferedImage convertedImage = convertBufferedImage(file, originalFile, bfInputStream, media);
		boolean successResize = retouchService.resizeProfiles(convertedImage,media);
		if (successResize == false) {
			throw new RuntimeException();
			//return null;
		}
		
	}
	
	@Transactional
	private BufferedImage convertBufferedImage(MultipartFile multipartFile, File originalFile, BufferedInputStream bfInputStream, Media media) {
		java.awt.Image imageIcon = null;

		imageIcon = new ImageIcon(originalFile.getAbsolutePath()).getImage();
		
		media.setWidth(imageIcon.getWidth(null));
		media.setHeight(imageIcon.getHeight(null));

		// Change Color Profile
		BufferedImage convertedImg = new BufferedImage(
				media.getWidth(), media.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = convertedImg.createGraphics();
		graphics.drawImage(imageIcon, 0, 0, convertedImg.getWidth(),
				convertedImg.getHeight(), Color.WHITE, null);
		//

		if (media.getExtName().equals("jpg") ||
				media.getExtName().equals("jpeg")) {
			
			Exif exif;
			exif = exifService.save(media.getId(), bfInputStream);
			convertedImg = fixOrientationRotate(convertedImg,
					exif.getOrientation());
			media.setWidth(convertedImg.getWidth());
			media.setHeight(convertedImg.getHeight());
		}

		mediaRepository.update(media);

		return convertedImg;

	}
	
	// 자동 회전. http://www.impulseadventure.com/photo/exif-orientation.html 참고
	public BufferedImage fixOrientationRotate(BufferedImage bfMedia,
			int orientation) {
		if (orientation <= 1) {
			return bfMedia;
		}
		switch (orientation) {
		case 6:
			return retouchService.rotate(bfMedia, Scalr.Rotation.CW_90);
		case 3:
			return retouchService.rotate(bfMedia, Scalr.Rotation.CW_180);
		case 8:
			return retouchService.rotate(bfMedia, Scalr.Rotation.CW_270);
		}
		return bfMedia;

	}

}
