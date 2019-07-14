package kr.co.sunnyvale.sunnycdn.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Date;

import kr.co.sunnyvale.sunny.domain.Exif;
import kr.co.sunnyvale.sunny.repository.hibernate.ExifRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import com.drew.metadata.icc.IccDirectory;
import com.drew.metadata.iptc.IptcDirectory;


@Service( value="exifService" )
public class ExifService {
	
	@Autowired
	ExifRepository exifRepository;
	public ExifService(){
		
	}
	
	public Exif save(long id, BufferedInputStream bgInputStream) {
		Exif exif = new Exif();
		try {
			Metadata metadata = ImageMetadataReader.readMetadata(bgInputStream, false);
		
			exif.setId(id);
			// obtain the Exif directory
			ExifSubIFDDirectory subIFDDirectory = metadata.getDirectory(ExifSubIFDDirectory.class);
			if( subIFDDirectory != null ){
				
				Date date = subIFDDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
				if( date != null )
					exif.setTime(date);
				
				String aperture = subIFDDirectory.getString(ExifSubIFDDirectory.TAG_APERTURE);
				if( aperture != null )
					exif.setAperture(aperture);
				
				String focus = subIFDDirectory.getString(ExifSubIFDDirectory.TAG_FOCAL_LENGTH);
				if( focus != null )
					exif.setFocusDistance(focus);
				
				String exposureTime = subIFDDirectory.getString(ExifSubIFDDirectory.TAG_EXPOSURE_TIME);
				if( exposureTime != null )
					exif.setExposeTime(exposureTime);
				
				String iso = subIFDDirectory.getString(ExifSubIFDDirectory.TAG_ISO_EQUIVALENT);
				if(iso != null){
					exif.setIso(iso);
				}
				
				
			}
			ExifIFD0Directory iFD0Directory = metadata.getDirectory(ExifIFD0Directory.class);
			if( iFD0Directory != null ){
				String make = iFD0Directory.getString(ExifIFD0Directory.TAG_MAKE) ;
				String model = iFD0Directory.getString(ExifIFD0Directory.TAG_MODEL);
				String orientation = iFD0Directory.getString(ExifIFD0Directory.TAG_ORIENTATION);
				
				String cameraModel = null;
				if(make != null ){
					cameraModel = make;
				}
				if(model != null){
					cameraModel += " " + model;
				}
				
				if(cameraModel != null ){
					exif.setCameraModel(cameraModel);
				}
				if( orientation != null )
					exif.setOrientation(Integer.parseInt(orientation));
			}
			
			
			GpsDirectory gpsDirectory = metadata.getDirectory(GpsDirectory.class);
			if(gpsDirectory != null ){
				String latitude = gpsDirectory.getString(GpsDirectory.TAG_GPS_LATITUDE);
				String latitudeRef = gpsDirectory.getString(GpsDirectory.TAG_GPS_LATITUDE_REF);
				String longtitude = gpsDirectory.getString(GpsDirectory.TAG_GPS_LONGITUDE) ;
				String longtitudeRef = gpsDirectory.getString(GpsDirectory.TAG_GPS_LONGITUDE_REF);
				if(latitude != null){
					exif.setLatitude(latitude + "_ref_" + latitudeRef);
				}
				if(longtitude != null ){
					exif.setLongitude(longtitude + "_ref_" + longtitudeRef); 
				}
			}
			IccDirectory iccDirectory = metadata.getDirectory(IccDirectory.class);
			
			IptcDirectory iptcDirectory = metadata.getDirectory(IptcDirectory.class);
			if(iptcDirectory != null ){
				String creator = iptcDirectory.getString(IptcDirectory.TAG_CAPTION_WRITER);
				if( creator != null ){
					exif.setCreator(creator);
				}
				
				
				String copyright = iptcDirectory.getString(IptcDirectory.TAG_COPYRIGHT_NOTICE);
				if( copyright != null ){
					exif.setCopyright(copyright);
				}
			}
			
			
			exifRepository.save(exif);
			
		} catch (ImageProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return exif;
	}
	public Exif getExif(long id){
		return exifRepository.select(id);
	}

}
