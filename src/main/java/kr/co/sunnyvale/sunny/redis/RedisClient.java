package kr.co.sunnyvale.sunny.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisClient {

    private JedisPool jedisPool;
    private String passwd;

    public RedisClient(int maxActive, int maxIdle, int maxWait, String ip, int port, String passwd){
        try{
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxActive(maxActive);
            jedisPoolConfig.setMaxIdle(maxIdle);
            jedisPoolConfig.setMaxWait(maxWait);
            jedisPool = new JedisPool(jedisPoolConfig, ip, port, 100000);
            this.passwd = passwd;
        }catch(Exception ex){
            ex.printStackTrace();;
        }
    }

    public void returnResource(Jedis jedis){
        jedisPool.returnResource(jedis);
    }

    public Jedis getRedisClient(){
        Jedis redisClient = jedisPool.getResource();
        redisClient.auth(passwd);
        redisClient.select(1);
        return redisClient;
    }
}
