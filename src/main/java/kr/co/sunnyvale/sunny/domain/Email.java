//package kr.co.sunnyvale.sunny.domain;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.Table;
//
//import kr.co.sunnyvale.sunny.domain.validator.constraints.ValidEmail;
//
//@Entity
//@Table(name = "EMAIL")
//public class Email  {
//
//	public Email(){
//		
//	}
//	
//	public Email(String email){
//		this.email = email;
//	}
//	
//	@Id
//	@Column(name = "id")
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	private Long id;
//
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "user_id")
//	private User user;
//
//	@ValidEmail
//	@Column(name = "email")
//	private String email;
//
//	public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}
//
//	public User getUser() {
//		return user;
//	}
//
//	public void setUser(User user) {
//		this.user = user;
//	}
//
//	public String getEmail() {
//		return email;
//	}
//
//	public void setEmail(String email) {
//		this.email = email;
//	}
//
//	
//	
//}
