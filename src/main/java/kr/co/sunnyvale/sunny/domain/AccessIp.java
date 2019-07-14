package kr.co.sunnyvale.sunny.domain;

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
@Table(name = "ACCESS_IP")
public class AccessIp {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "site_id")
	private Site site;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "small_group_id")
	private SmallGroup smallGroup;
	
	@Column(name = "ip_address")
	private String ipAddress;
}
