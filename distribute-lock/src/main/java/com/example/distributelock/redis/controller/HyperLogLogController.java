package com.example.distributelock.redis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HyperLogLogOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

/**
 * 描述:
 * 用HyperLogLog 实现粗略计数
 *
 * 比如页面的UV,用set数据结构,当用户量特比大,很耗费内存
 *
 * pf1的内存占用是12K(适合数据量大,但是精准不高的去重场景)
 * @Author: zhangchao
 **/
@RestController
@RequestMapping("/loglog")
public class HyperLogLogController {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 计算某个页面的UV
     * @return
     * @throws Exception
     */
    @GetMapping("/uv")
    public String uv() throws Exception {
        String uvKey = "20200908UV";
        HyperLogLogOperations ops = redisTemplate.opsForHyperLogLog();
        for (int j=1;j<=100000 ;j++) {
            ops.add(uvKey,"user"+j);
        }
        Long size = ops.size(uvKey);
        System.out.println(size);
        return size+"";
    }

    /**
     * 业务:合并多个页面的总UV
     * @return
     * @throws Exception
     */
    @GetMapping("/uv2")
    public String uv2() throws Exception {
        String uvKey = "20200902UV";
        String uvKey2 = "20200901UV";
        HyperLogLogOperations ops = redisTemplate.opsForHyperLogLog();
        for (int j=1;j<=50000 ;j++) {
            ops.add(uvKey,"user"+j);
            ops.add(uvKey2,"user"+j);
        }
        Long size = ops.size(uvKey);
        Long size2 = ops.size(uvKey2);
        Long union = ops.union(uvKey, uvKey2);
        System.out.println(size);
        return union+"";
    }

    private static void jedisApi() {
        Jedis jedis = new Jedis("localhost");
        for (int j=1;j<=100000 ;j++) {
            jedis.pfadd("20200907UV","user"+j);
        }
        long total = jedis.pfcount("20200907UV");
        System.out.printf("%d %d\n",100000,total);
        jedis.close();

    }

}
