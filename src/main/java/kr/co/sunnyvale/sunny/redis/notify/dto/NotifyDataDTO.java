package kr.co.sunnyvale.sunny.redis.notify.dto;

public class NotifyDataDTO {
	

	public static final int TYPE_MESSAGE_SEND=0;
	public static final int TYPE_MESSAGE_READ=1;
	
	/**
	 *  sender Id
	 */
	private Long uid;

	/**
	 * story Id
	 */
	private Long sid;
	
	/**
	 * channel id (optional)
	 */
	private Long cid;
	
	/**
	 * type (optional)
	 */
	private Integer t;
	
	/**
	 * read user id (optional) - 누군가 읽었을 때 읽은 유저 아이디
	 */
	private Long ruid;
	
	/**
	 * message id (optional) - 읽은 noti 일 경우엔 마지막 읽은 메시지의 아이디가 감.
	 */
	private Long mid;

	public Long getUid() {
		return uid;
	}

	/**
	 *  sender Id
	 */
	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Long getSid() {
		return sid;
	}

	/**
	 * story Id
	 */
	public void setSid(Long sid) {
		this.sid = sid;
	}

	public Long getCid() {
		return cid;
	}

	
	/**
	 * channel id (optional)
	 */
	public void setCid(Long cid) {
		this.cid = cid;
	}

	public Integer getT() {
		return t;
	}

	/**
	 * type (optional)
	 */
	public void setT(Integer t) {
		this.t = t;
	}

	public Long getRuid() {
		return ruid;
	}

	/**
	 * read user id (optional) - 누군가 읽었을 때 읽은 유저 아이디
	 */
	public void setRuid(Long ruid) {
		this.ruid = ruid;
	}

	public Long getMid() {
		return mid;
	}

	/**
	 * message id (optional) - 읽은 noti 일 경우엔 마지막 읽은 메시지의 아이디가 감.
	 */
	public void setMid(Long mid) {
		this.mid = mid;
	}
	
	
}
