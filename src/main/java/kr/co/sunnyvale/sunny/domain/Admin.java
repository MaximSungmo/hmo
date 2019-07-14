//package kr.co.sunnyvale.sunny.domain;
//
//import java.io.Serializable;
//import java.util.Date;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.OneToOne;
//import javax.persistence.Table;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//
//import kr.co.sunnyvale.sunny.domain.validator.constraints.ValidEmail;
//
//@Entity
//@Table( name = "ADMIN" )
//public class Admin implements Serializable{
//
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = -6665490337116948331L;
//	public static final int PERMISSION_PUBLIC = 0;
//	public static final int PERMISSION_FRIEND = 1;
//	public static final int PERMISSION_PRIVATE = 2;
//
//	
//	/**
//	 * 
//	 */
//
//	/**
//	 * 
//	 */
//
//	@Id
//	@Column(name = "id", columnDefinition="bigint(20)" )
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	private Long id;
//
//	public Admin(){
//		this.setCreateDate(new Date());
//		this.setUpdateDate(new Date());
//	}
//
//	
//	public Admin(User user) {
//		this.setUser(user);
//	}
//
//	@OneToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "user_id")
//	private User user;
//
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "site_id")
//	private Site site;
//	
//	/**
//	 * 관리자가 가입할때 회사에서 사용하는 email 주소.
//	 */
////	@ValidEmail
//	@Column( name="email")
//	private String email;
//	
//	// 1 - 1명
//	// 2 - 2~9명
//	// 10 - 10~19명
//	// 20 - 20~49명
//	// 50 - 50~99명
//	// 100 - 100~ 199명
//	// 200 - 200~ 749명
//	// 750 - 750 ~ 999명
//	// 1000 - 1000 ~ 4999명
//	// 5000 - 5000명 이상
//	@Column( name="user_count_type")
//	private int userCountType;
//	
//	/**
//	 * 관리자 생성일. 한 사이트에 관리자는 여러명 있을 수 있기 때문에, 각 관리자가 생성하고 수정된 시간은 별도로 관리가 되어야 함.
//	 */
//	@Column(name = "create_date")
//	private Date createDate;
//	
//	@Column(name = "update_date")
//	protected Date updateDate;	
//	
//	public Long getId() {
//		return id;
//	}
//
//
//	public void setId(Long id) {
//		this.id = id;
//	}
//
//
//	public User getUser() {
//		return user;
//	}
//
//
//	public void setUser(User user) {
//		this.user = user;
//	}
//
//
//	public Site getSite() {
//		return site;
//	}
//
//
//	public void setSite(Site site) {
//		this.site = site;
//	}
//
//
//	public String getEmail() {
//		return email;
//	}
//
//
//	public void setEmail(String email) {
//		this.email = email;
//	}
//
//
//	public Date getCreateDate() {
//		return createDate;
//	}
//
//
//	public void setCreateDate(Date createDate) {
//		this.createDate = createDate;
//	}
//
//
//	public Date getUpdateDate() {
//		return updateDate;
//	}
//
//
//	public void setUpdateDate(Date updateDate) {
//		this.updateDate = updateDate;
//	}
//
//
//	public int getUserCountType() {
//		return userCountType;
//	}
//
//
//	public void setUserCountType(int userCountType) {
//		this.userCountType = userCountType;
//	}
//	
//	
//
//
//}