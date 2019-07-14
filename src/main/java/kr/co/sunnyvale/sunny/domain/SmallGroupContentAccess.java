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
@Table(name = "SMALL_GROUP_CONTENT_ACCESS")
public class SmallGroupContentAccess  {
	
	
	public SmallGroupContentAccess(){
		this.setCreateDate(new Date());
		this.setUpdateDate(new Date());
//		this.setReadPermission(true);
//		this.setWritePermission(false);
//		this.setDeletePermission(false);
//		this.setFileUploadPermission(false);
//		this.setFileDownloadPermission(true);		
	}
	
	@Id
	@Column(name = "id", columnDefinition="bigint(20)" )
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "small_group_id")
	private SmallGroup smallGroup;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "content_id")
	private Content content;
	
	@Column(name = "create_date")
	private Date createDate;

	@Column(name = "update_date")
	private Date updateDate;
	
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

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	
	
}
