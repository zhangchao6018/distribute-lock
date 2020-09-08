package com.example.distributelock.redis.controller;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

/**
 * 描述:
 * 简单限流
 * @Author: zhangchao
 **/
public class RedisLimiter {
    private static Jedis jedis = new Jedis("localhost");

    /**
     * period秒内仅允许maxCount个请求
     * @param userId
     * @param actionKey
     * @param period
     * @param maxCount
     * @return
     */
    public static boolean isActionAllowed(String userId, String actionKey, int period, int maxCount){
        String key = String.format("hist:%s:%s:",userId,actionKey);
        long now = System.currentTimeMillis();
        Pipeline pipelined = jedis.pipelined();
        pipelined.multi();
        pipelined.zadd(key,now,""+now);
        pipelined.zremrangeByScore(key, 0, now - period * 1000);
        Response<Long> count = pipelined.zcard(key);
        pipelined.expire(key,period+1);
        pipelined.exec();
        pipelined.close();
        return count.get() <= maxCount;

    }
    public static void main(String[] args) {
        for (int j=1;j<=50 ;j++) {
            System.out.println(isActionAllowed("miaosha","createOrder",60,5));
        }
    }
}
