package kr.co.sunnyvale.sunny.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class PublishTest {
	public static void main(String args[]) throws Exception{
		JedisPoolConfig jedisConfigPool = new JedisPoolConfig();
		jedisConfigPool.setMaxActive(20);
		jedisConfigPool.setMaxIdle(10);
		jedisConfigPool.setMaxWait(1000);
		JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), "www.meet42.com", 6379, 100000);
		Jedis redisClient = jedisPool.getResource();
		redisClient.auth("redis4185");
		
		// redisPublisher.publish(new Message( siteService.getCurrentSite(Site.DEFAULT_ID).getDomain() + "_friend-notify", "1000000002,1000000020"));
	    
		redisClient.publish( "chat-notify","sns.sunnyvale.co.kr_1_1000000002,1000000020");
		redisClient.publish("chat-notify", "www.yacamp.com_1_1000000000,1000000002");
		
		jedisPool.returnResource(redisClient);	
	}
}
