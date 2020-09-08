package com.example.distributelock.redis.controller;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;

/**
 * 描述:
 *
 * @Author: zhangchao
 **/
public class TransactionDemo {
    public static void main(String[] args) {

        Jedis jedis = new Jedis();
        String userId = "abc";
        String key = keyFor(userId);
        jedis.setnx(key,String.valueOf(5));   //setnx 做初始化
        System.out.println(doubleAccount(jedis,userId));
        jedis.close();
    }
    public static int doubleAccount(Jedis jedis,String userId){
        String key = keyFor(userId);
        while (true){
            jedis.watch(key);
            int value = Integer.parseInt(jedis.get(key));
            value *=2;//数据计算
            Transaction tx = jedis.multi();
            tx.set(key,String.valueOf(value));
            List<Object> res = tx.exec();
            if (res !=null) {
                break; //修改成功
            }
        }
        return Integer.parseInt(jedis.get(key));//重新获取金额
    }

    private static String keyFor(String userId) {
        return String.format("account_%s:",userId);
    }
}
