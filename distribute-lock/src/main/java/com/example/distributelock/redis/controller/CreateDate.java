package com.example.distributelock.redis.controller;

import redis.clients.jedis.Jedis;

/**
 * 描述:
 *
 * @Author: zhangchao
 **/
public class CreateDate {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost");
        for (int j=1;j<=500000 ;j++) {
            jedis.sadd(j+"",j+"");
        }
        jedis.close();
    }
}
