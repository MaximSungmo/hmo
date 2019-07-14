package kr.co.sunnyvale.sunny.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "EXIF")
public class Exif  {

	/**
	 * 
	 */
	public final static String PREFIX_NUMBER_USERID = "1";

	/*** Basic ***/

	@Id
	@Column(name = "id", columnDefinition="bigint(20)" )
	private long id;

	@Column(name = "camera_model")
	private String cameraModel;
	
	@Column(name = "creator")
	private String creator;
	
	@Column(name = "copyright")
	private String copyright;
	
	@Column(name = "time")
	private Date time;
	
	@Column(name = "iso")
	private String iso;
	
	@Column(name = "expose_time")
	private String exposeTime;
	
	@Column(name = "orientation")
	private int orientation;
	
	@Column(name = "focus_distance")
	private String focusDistance;
	
	@Column(name = "aperture")
	private String aperture;
	
	@Column(name = "latitude")
	private String latitude;
	
	@Column(name = "longitude")
	private String longitude;

	/** Mapping **/
	
	
	public Exif(){
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	public String getCameraModel() {
		return cameraModel;
	}

	public void setCameraModel(String cameraModel) {
		this.cameraModel = cameraModel;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getIso() {
		return iso;
	}

	public void setIso(String iso) {
		this.iso = iso;
	}

	public String getExposeTime() {
		return exposeTime;
	}

	public void setExposeTime(String exposeTime) {
		this.exposeTime = exposeTime;
	}

	public int getOrientation() {
		return orientation;
	}

	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}

	public String getFocusDistance() {
		return focusDistance;
	}

	public void setFocusDistance(String focusDistance) {
		this.focusDistance = focusDistance;
	}

	public String getAperture() {
		return aperture;
	}

	public void setAperture(String aperture) {
		this.aperture = aperture;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

}