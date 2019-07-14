package kr.co.sunnyvale.sunny.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "SMALL_GROUP_SMALL_GROUP_ACCESS")
public class SmallGroupSmallGroupAccess  {
	
	
	public SmallGroupSmallGroupAccess(){
//		this.setReadPermission(true);
//		this.setWritePermission(true);
//		this.setDeletePermission(true);
//		this.setFileUploadPermission(true);
//		this.setFileDownloadPermission(true);
		this.setCreateDate(new Date());
	}
	
	@Id
	@Column(name = "id", columnDefinition="bigint(20)" )
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "small_group_id")
	private SmallGroup smallGroup;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "access_small_group_id")
	private SmallGroup accessSmallGroup;
	
	@Column(name = "create_date")
	private Date createDate;

//	@Column(name = "read_permission")
//	private boolean readPermission;
//
//	@Column(name = "write_permission")
//	private boolean writePermission;
//
//	@Column(name = "delete_permission")
//	private boolean deletePermission;
//
//	@Column(name = "file_upload_permission")
//	private boolean fileUploadPermission;
//
//	@Column(name = "file_download_permission")
//	private boolean fileDownloadPermission;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public SmallGroup getSmallGroup() {
		return smallGroup;
	}

	public void setSmallGroup(SmallGroup smallGroup) {
		this.smallGroup = smallGroup;
	}

//	public boolean isReadPermission() {
//		return readPermission;
//	}
//
//	public void setReadPermission(boolean readPermission) {
//		this.readPermission = readPermission;
//	}
//
//	public boolean isWritePermission() {
//		return writePermission;
//	}
//
//	public void setWritePermission(boolean writePermission) {
//		this.writePermission = writePermission;
//	}
//
//	public boolean isDeletePermission() {
//		return deletePermission;
//	}
//
//	public void setDeletePermission(boolean deletePermission) {
//		this.deletePermission = deletePermission;
//	}
//
//	public boolean isFileUploadPermission() {
//		return fileUploadPermission;
//	}
//
//	public void setFileUploadPermission(boolean fileUploadPermission) {
//		this.fileUploadPermission = fileUploadPermission;
//	}
//
//	public boolean isFileDownloadPermission() {
//		return fileDownloadPermission;
//	}
//
//	public void setFileDownloadPermission(boolean fileDownloadPermission) {
//		this.fileDownloadPermission = fileDownloadPermission;
//	}

	public SmallGroup getAccessSmallGroup() {
		return accessSmallGroup;
	}

	public void setAccessSmallGroup(SmallGroup accessSmallGroup) {
		this.accessSmallGroup = accessSmallGroup;
	}

	
}
