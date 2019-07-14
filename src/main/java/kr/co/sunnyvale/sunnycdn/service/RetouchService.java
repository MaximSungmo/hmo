package kr.co.sunnyvale.sunnycdn.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;

import kr.co.sunnyvale.sunny.domain.Media;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Rotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service( value="retouchService" )
public class RetouchService {

	@Autowired
	FileService fileService;


	@Autowired
	MediaCDNService mediaService;

	
	private int profileHugeWidth;
	private int profileHugeHeight;
	private int profileLargeWidth;
	private int profileLargeHeight;
	private int profileMiddleWidth;
	private int profileMiddleHeight;
	private int profileSmallWidth;
	private int profileSmallHeight;
	
	private int storyHugeWidth;
	private int storyHugeHeight;
	private int storyLargeWidth;
	private int storyLargeHeight;
	private int storyMiddleWidth;
	private int storyMiddleHeight;
	private int storySmallWidth;
	private int storySmallHeight;
	
	private String originalPostFix;
	private String hugePostFix;
	private String largePostFix;
	private String middlePostFix;
	private String smallPostFix;
	

	@Autowired
	public RetouchService (
			@Value("#{ imageProperties['retouch.profileHugeWidth']}")int profileHugeWidth,
			@Value("#{ imageProperties['retouch.profileHugeHeight']}")int profileHugeHeight,
			@Value("#{ imageProperties['retouch.profileLargeWidth']}")int profileLargeWidth,
			@Value("#{ imageProperties['retouch.profileLargeHeight']}")int profileLargeHeight,
			@Value("#{ imageProperties['retouch.profileMiddleWidth']}")int profileMiddleWidth,
			@Value("#{ imageProperties['retouch.profileMiddleHeight']}")int profileMiddleHeight,
			@Value("#{ imageProperties['retouch.profileSmallWidth']}")int profileSmallWidth,
			@Value("#{ imageProperties['retouch.profileSmallHeight']}")int profileSmallHeight,

			@Value("#{ imageProperties['retouch.storyHugeWidth']}")int storyHugeWidth,
			@Value("#{ imageProperties['retouch.storyHugeHeight']}")int storyHugeHeight,
			@Value("#{ imageProperties['retouch.storyLargeWidth']}")int storyLargeWidth,
			@Value("#{ imageProperties['retouch.storyLargeHeight']}")int storyLargeHeight,
			@Value("#{ imageProperties['retouch.storyMiddleWidth']}")int storyMiddleWidth,
			@Value("#{ imageProperties['retouch.storyMiddleHeight']}")int storyMiddleHeight,
			@Value("#{ imageProperties['retouch.storySmallWidth']}")int storySmallWidth,
			@Value("#{ imageProperties['retouch.storySmallHeight']}")int storySmallHeight,
			
			@Value("#{ imageProperties['retouch.originalPostFix']}")String originalPostFix,
			@Value("#{ imageProperties['retouch.hugePostFix']}")String hugePostFix,
			@Value("#{ imageProperties['retouch.largePostFix']}")String largePostFix,
			@Value("#{ imageProperties['retouch.middlePostFix']}")String middlePostFix,
			@Value("#{ imageProperties['retouch.smallPostFix']}")String smallPostFix
			)
	{
		this.profileHugeWidth = profileHugeWidth;
		this.profileHugeHeight = profileHugeHeight;
		this.profileLargeWidth = profileLargeWidth;
		this.profileLargeHeight = profileLargeHeight;
		this.profileMiddleWidth = profileMiddleWidth;
		this.profileMiddleHeight = profileMiddleHeight;
		this.profileSmallWidth = profileSmallWidth;
		this.profileSmallHeight = profileSmallHeight;
		
		this.storyHugeWidth = storyHugeWidth;
		this.storyHugeHeight = storyHugeHeight;
		this.storyLargeWidth = storyLargeWidth;
		this.storyLargeHeight = storyLargeHeight;
		this.storyMiddleWidth = storyMiddleWidth;
		this.storyMiddleHeight = storyMiddleHeight;
		this.storySmallWidth = storySmallWidth;
		this.storySmallHeight = storySmallHeight;
		
		this.originalPostFix = originalPostFix;
		this.hugePostFix = hugePostFix;
		this.largePostFix = largePostFix;
		this.middlePostFix = middlePostFix;
		this.smallPostFix = smallPostFix;
		
		
		
//		this.mediaService = mediaService;
	}

	public RetouchService(){
		
	}

	public boolean resizeProfiles(BufferedImage originalImage, Media image) {
		
		//write(originalImage, "jpeg", fileService.newImageFile(image, originalPostFix));
		
		write(resizeProfile(originalImage, profileHugeWidth, profileHugeHeight), "jpeg", fileService.newImageFile(image, hugePostFix));
		
		write(resizeProfile(originalImage, profileLargeWidth, profileLargeHeight), "jpeg", fileService.newImageFile(image, largePostFix));
		
		write(resizeProfile(originalImage, profileMiddleWidth, profileMiddleHeight), "jpeg", fileService.newImageFile(image, middlePostFix));
		
		write(resizeProfile(originalImage, profileSmallWidth, profileSmallHeight), "jpeg", fileService.newImageFile(image, smallPostFix));
		
		return true;

	}

	public boolean resizeStories(BufferedImage originalImage, Media image) {

		//write(originalImage, "jpeg", fileService.newImageFile(image, originalPostFix));
		
		write(resizeStory(originalImage, storyHugeWidth, storyHugeHeight), "jpeg", fileService.newImageFile(image, hugePostFix));
		
		write(resizeStory(originalImage, storyLargeWidth, storyLargeHeight), "jpeg", fileService.newImageFile(image, largePostFix));
		
		write(resizeStory(originalImage, storyMiddleWidth, storyMiddleHeight), "jpeg", fileService.newImageFile(image, middlePostFix));
		
		write(resizeStory(originalImage, storySmallWidth, storySmallHeight), "jpeg", fileService.newImageFile(image, smallPostFix));
		
		return true;
	}

	public boolean resizeCovers(BufferedImage originalImage, Media image) {
		
		//write(originalImage, "jpeg", fileService.newImageFile(image, originalPostFix));
		
		write(resizeCover(originalImage, storyHugeWidth, storyHugeHeight), "jpeg", fileService.newImageFile(image, hugePostFix));
		
		write(resizeCover(originalImage, storyLargeWidth, storyLargeHeight), "jpeg", fileService.newImageFile(image, largePostFix));
		
		write(resizeCover(originalImage, storyMiddleWidth, storyMiddleHeight), "jpeg", fileService.newImageFile(image, middlePostFix));
		
		write(resizeCover(originalImage, storySmallWidth, storySmallHeight), "jpeg", fileService.newImageFile(image, smallPostFix));
		
		return true;
	}


	private BufferedImage resizeProfile(BufferedImage originalImage,
			int width, int height) {
		if( originalImage.getWidth() > originalImage.getHeight()){
			return resize(cropRatioToHeight(originalImage, width,height), width, height);
		}else{
			return resize(cropRatioToWidth(originalImage, width,height), width, height);
		}
	}

	private BufferedImage resizeStory(BufferedImage originalImage,
			int width, int height) {

	
		if( originalImage.getWidth() > originalImage.getHeight() ){
			
			if( width > originalImage.getWidth() )
				return originalImage;
			
			return resize(originalImage, width, height);
			
		}else{
			
			if( height > originalImage.getHeight() )
				return originalImage;
			
			return resize(originalImage, width, height);
			
		}
		
	}

	private BufferedImage resizeCover(BufferedImage originalImage,
			int width, int height) {
		if( originalImage.getWidth() > originalImage.getHeight() ){
			
			if( width > originalImage.getWidth() )
				return originalImage;
			
			return resize(originalImage, width, height);
			
		}else{
			
			if( height > originalImage.getHeight() )
				return originalImage;
			
			return resize(originalImage, width, height);
			
		}
	}
	
	public BufferedImage resize(BufferedImage originalImage, int width, int height) {
		
		return Scalr.resize(originalImage, Scalr.Method.ULTRA_QUALITY, width, height);
	}
	
	public BufferedImage fixRatio(BufferedImage originalImage, int width, int height) {
		float originalWidth = (float) originalImage.getWidth() ;
		float originalHeight = (float) originalImage.getHeight() ;
		float originalRatio = originalWidth / originalHeight;
		float targetRatio = (float) width / (float) height;
		System.out.println("o_width : " + originalWidth + " o_height :" + originalHeight + " width :" + width + " height : " + height);
		System.out.println("o_ratio : " + originalRatio + " t_ratio : " + targetRatio);
		if( originalRatio != targetRatio ){
			
			if( targetRatio >= 1){
				float fixedRatio = originalRatio / targetRatio;
				
				if( fixedRatio > 1 ){
					return originalImage;
				}else{
					int fixedHeight = (int) (fixedRatio * originalHeight);
					 
					return crop(originalImage, 0, (int)((originalHeight / 2) - ( fixedHeight / 2 )), (int)originalWidth, (int)fixedHeight);
					
				}
			
			}else if ( targetRatio < 1 ){
				float fixedRatio = ( originalHeight / originalWidth ) / ((float)height / (float)width );
				if( fixedRatio > 1 ){
					return originalImage;
				}else{
					int fixedWidth = (int) (fixedRatio * originalWidth);
					return crop(originalImage, (int)((originalWidth / 2) - ( fixedWidth / 2 )), 0, (int)fixedWidth, (int)originalHeight); 
				}
			}
		}else{
			return originalImage;
		}
		return originalImage;
	}

	public BufferedImage crop(BufferedImage originalImage, int width, int height){
		
		int widthStart = (originalImage.getWidth() / 2) - (width / 2);
		int heightStart = (originalImage.getHeight() / 2) - (height / 2);
	
		return crop(originalImage, widthStart, heightStart, width, height);
	}


	public BufferedImage crop(BufferedImage originalImage, int x, int y, int width, int height){
		return Scalr.crop(originalImage, x, y, width, height);
	}
	
	public BufferedImage cropRatioToWidth(BufferedImage originalImage, int width, int height){
		float originalWidth = (float) originalImage.getWidth() ;
		float originalHeight = (float) originalImage.getHeight() ;
		
		float originalRatio = 0.0f;
		float targetRatio = 0.0f;
		int fixedHeight = 0;
		originalRatio = originalHeight / originalWidth;
		targetRatio = (float) height  / (float) width;
		if( originalRatio == targetRatio )
			return originalImage;
		
		fixedHeight = (int) (originalWidth * targetRatio);
		return crop(originalImage, 0, (int)((originalHeight / 2) - ( fixedHeight / 2 )), (int)originalWidth, (int)fixedHeight); 

	
	}
	
	
	public BufferedImage cropRatioToHeight(BufferedImage originalImage, int width, int height){
		float originalWidth = (float) originalImage.getWidth() ;
		float originalHeight = (float) originalImage.getHeight() ;
		
		float originalRatio = 0.0f;
		float targetRatio = 0.0f;
		int fixedWidth = 0;
		originalRatio = originalWidth / originalHeight;
		targetRatio = (float) width / (float) height;
//			System.out.println("O_ratio : " + originalRatio + " t_ratio : " + targetRatio);
		if( originalRatio == targetRatio )
			return originalImage;
		
		fixedWidth = (int) (originalHeight * targetRatio);
		return crop(originalImage, (int)((originalWidth / 2) - ( fixedWidth / 2 )), 0, (int)fixedWidth, (int)originalHeight); 
		
	}
	
	
	public boolean write(BufferedImage bfImage, String extName, File file) {
		if(bfImage == null ){
			return false;
		}
		
		System.out.println("Format name  : " + extName);
		
		ImageWriter writer = (ImageWriter)ImageIO.getImageWritersByFormatName(extName).next();
		ImageWriteParam iwp = writer.getDefaultWriteParam();
		
		iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		
		iwp.setCompressionQuality(1); 
	
		try {
			FileImageOutputStream output = new FileImageOutputStream(file);

		writer.setOutput(output);
		IIOImage iioImage = new IIOImage(bfImage, null, null);
		writer.write(iioImage);
		writer.dispose();
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
		
	}


	public BufferedImage rotate(BufferedImage originalImage, Rotation rotation) {
		return Scalr.rotate(originalImage, rotation);
	}

	public int getProfileHugeWidth() {
		return profileHugeWidth;
	}


	public int getProfileHugeHeight() {
		return profileHugeHeight;
	}


	public int getProfileLargeWidth() {
		return profileLargeWidth;
	}


	public int getProfileLargeHeight() {
		return profileLargeHeight;
	}


	public int getProfileMiddleWidth() {
		return profileMiddleWidth;
	}


	public int getProfileMiddleHeight() {
		return profileMiddleHeight;
	}


	public int getProfileSmallWidth() {
		return profileSmallWidth;
	}


	public int getProfileSmallHeight() {
		return profileSmallHeight;
	}


	public int getStoryHugeWidth() {
		return storyHugeWidth;
	}


	public int getStoryHugeHeight() {
		return storyHugeHeight;
	}


	public int getStoryLargeWidth() {
		return storyLargeWidth;
	}


	public int getStoryLargeHeight() {
		return storyLargeHeight;
	}


	public int getStoryMiddleWidth() {
		return storyMiddleWidth;
	}


	public int getStoryMiddleHeight() {
		return storyMiddleHeight;
	}


	public int getStorySmallWidth() {
		return storySmallWidth;
	}


	public int getStorySmallHeight() {
		return storySmallHeight;
	}


	public String getOriginalPostFix() {
		return originalPostFix;
	}


	public String getHugePostFix() {
		return hugePostFix;
	}


	public String getLargePostFix() {
		return largePostFix;
	}


	public String getMiddlePostFix() {
		return middlePostFix;
	}


	public String getSmallPostFix() {
		return smallPostFix;
	}

	public void setOriginalPostFix(String originalPostFix) {
		this.originalPostFix = originalPostFix;
	}

}
