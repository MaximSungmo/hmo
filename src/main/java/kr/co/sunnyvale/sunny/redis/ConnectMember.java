package kr.co.sunnyvale.sunny.redis;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConnectMember {
	private String userId;
	private long connectTime;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public long getConnectTime() {
		return connectTime;
	}
	public void setConnectTime(long connectTime) {
		this.connectTime = connectTime;
	}
	
	public String getConnectTimeStr(){
		Date now = new Date(connectTime);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
		return format.format(now);
	}

	@Override
	public String toString() {
		return "ConnectMember [userId=" + userId + ", connectTime="
				+ connectTime + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConnectMember other = (ConnectMember) obj;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}
	
	
}
