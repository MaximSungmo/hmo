package kr.co.sunnyvale.sunny.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtil {
	public static void main(String args[]){
		ImageUtil.convertToJpegFromGif("c:/temp/Bww5215d0eb17d8b.gif", "c:/temp/Bww5215d0eb17d8b.jpeg");
	}
	
	public static void convertToJpegFromGif(String gifSource, String jpegDest) {
		File input = new File(gifSource);
		try {
			BufferedImage image = ImageIO.read(input);
			// BufferedImage bi = getMyImage(); // retrieve image 
			File outputfile = new File(jpegDest);
			ImageIO.write(image, "jpg", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
