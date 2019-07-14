package kr.co.sunnyvale.sunny.redis;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component(value = "redisPublisher")
//public class RedisPublisher implements Publisher{
//    @Autowired
//    private org.springframework.data.redis.core.RedisTemplate<String, Message> publishTemplate;
//
//    @Override
//    public void publish(Message message) {
//        // TODO Auto-generated method stub
//        publishTemplate.convertAndSend(message.getType(), message.getContent());
//    }
//}
//

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kr.co.sunnyvale.sunny.domain.User;
import kr.co.sunnyvale.sunny.domain.UserAppleToken;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

@Component(value = "redisPublisher")
public class RedisPublisher {
	@Autowired
	private RedisClient redisClient;

	public void publish(final Message message) {
	
		new Thread() {
			public void run() {
				Jedis jedis = redisClient.getRedisClient();
				try {
					jedis.publish(message.getNotiType(), message.toJsonString());
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					try {
						redisClient.returnResource(jedis);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	} // publish

	/**
	 * time시간만큼 사용자가 사이트를 이용하지 않았다면 접속정보를 삭제한다.
	 * 
	 * @param domain
	 * @param time
	 */
	public void delUserConnectInfo(String domain, long time) {

		// redisClient.lpush(domain + "_userList", userId);
		// redisClient.lpush(domain + "_socketList", socketId);
		// redisClient.lpush(domain + "_id_" + userId , socketId);
		// redisClient.hset(domain + "_socketAndUserHset", socketId, userId);

		String connectUserListChannelName = domain + "_userList";
		String socketIdListChannelName = domain + "_socketList";
		// String userIdsSocketIdListChannelName =
		Jedis jedis = redisClient.getRedisClient();
		try {
			long connectUserListCount = jedis.llen(connectUserListChannelName);
			List<String> connectUserIdList = jedis.lrange(
					connectUserListChannelName, 0, connectUserListCount);
			int connectUserIdListCount = connectUserIdList.size();
			for (int i = 0; i < connectUserIdListCount; i++) {
				String connectUserId = connectUserIdList.get(i);
				String connectTime = jedis.get(domain + "_lastConnectTime_"
						+ connectUserId);
				long longConnectTime = Long.parseLong(connectTime);
				long betweenTime = System.currentTimeMillis() - longConnectTime;
				if (betweenTime > time) {
					jedis.lrem(connectUserListChannelName, 0, connectUserId);
					jedis.del(domain + "_id_" + connectUserId);
				}
			} // for
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				redisClient.returnResource(jedis);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public Set<ConnectMember> getConnectUserList(String domain) {

		String connectUserListChannelName = domain + "_userList";
		Set<ConnectMember> connectUserSet = new HashSet<ConnectMember>();
		Jedis jedis = redisClient.getRedisClient();
		try {
			long connectUserListCount = jedis.llen(connectUserListChannelName);
			List<String> connectUserIdList = jedis.lrange(
					connectUserListChannelName, 0, connectUserListCount);
			int connectUserIdListCount = connectUserIdList.size();
			for (int i = 0; i < connectUserIdListCount; i++) {
				String connectTime = jedis.get(domain + "_lastConnectTime_"
						+ connectUserIdList.get(i));
				ConnectMember cm = new ConnectMember();
				cm.setUserId(connectUserIdList.get(i));
				cm.setConnectTime(Long.parseLong(connectTime));
				connectUserSet.add(cm);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				redisClient.returnResource(jedis);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return connectUserSet;
	}


	public void registerAppleDevelopmentDevice(Long userId,
			UserAppleToken userAppleToken) {
		Jedis jedis = redisClient.getRedisClient();

		try {
			jedis.sadd(Sunny.SERVICE_ID + ":user:" + userId + ":apple-development-tokens", userAppleToken.getDeviceToken());
		} catch(Exception ex){
			ex.printStackTrace();
		} finally{
			try{
				redisClient.returnResource(jedis);
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		//jedis.hset(Sunny.SERVICE_ID + ":apple-token:" + appleDeviceRegisterDTO.getDeviceToken(), );
	}
	
	
	public void registerAppleProductionDevice(Long userId,
			UserAppleToken userAppleToken) {
		Jedis jedis = redisClient.getRedisClient();

		try {
			jedis.sadd(Sunny.SERVICE_ID + ":user:" + userId + ":apple-production-tokens", userAppleToken.getDeviceToken());
		} catch(Exception ex){
			ex.printStackTrace();
		} finally{
			try{
				redisClient.returnResource(jedis);
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		//jedis.hset(Sunny.SERVICE_ID + ":apple-token:" + appleDeviceRegisterDTO.getDeviceToken(), );
	}
	


	public void removeAppleDevice(Long userId, String deviceToken) {
		Jedis jedis = redisClient.getRedisClient();

		try {
			long remCount = jedis.srem(Sunny.SERVICE_ID + ":user:" + userId + ":apple-production-tokens", deviceToken);
			if( remCount < 1 ){
				jedis.srem(Sunny.SERVICE_ID + ":user:" + userId + ":apple-development-tokens", deviceToken);
			}
		} catch(Exception ex){
			ex.printStackTrace();
		} finally{
			try{
				redisClient.returnResource(jedis);
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}

	public void login(Sunny sunny, User user) {
		Jedis jedis = redisClient.getRedisClient();
		try{
			jedis.sadd(Sunny.SERVICE_ID + ":user:" + user.getId() + ":sessions", sunny.getSessionId());
			jedis.hset(Sunny.SERVICE_ID + ":session:" + sunny.getSessionId(),"device", sunny.getDevice().isNormal() ? "PC" : "Mobile");
			jedis.hset(Sunny.SERVICE_ID + ":session:" + sunny.getSessionId(),"date", new Date().toString());
			jedis.hset(Sunny.SERVICE_ID + ":session:" + sunny.getSessionId(),"ipAddress", sunny.getIpAddress());
		} catch(Exception ex){
			ex.printStackTrace();
		} finally {
			try {
				redisClient.returnResource(jedis);
			} catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}

	public void updateAppleDevice(Long userId, UserAppleToken appleToken) {
		// TODO Auto-generated method stub
		
	}
}
