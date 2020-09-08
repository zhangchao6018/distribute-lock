package com.example.distributelock.redis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 描述:
 * @Author: zhangchao
 **/
@RestController
@RequestMapping("/redislock")
public class DistributeLockController {
    @Autowired
    private RedisTemplate redisTemplate;

    private static ThreadLocal<Map<String,Integer>> lokers = new ThreadLocal<>();
    /**
     * @return
     * @throws Exception
     */
    @GetMapping("/reentrant")
    public String lock() throws Exception {
        System.out.println(lock("skill1"));
        System.out.println(lock("skill1"));
        System.out.println(unlock("skill1"));
        System.out.println(unlock("skill1"));
        return "...";
    }

    private boolean _lock(String key){
        redisTemplate.opsForValue().set(key,"1", 5,TimeUnit.SECONDS);
        return true;
    }

    private boolean _unlock(String key){
        return redisTemplate.delete(key);
    }

    private Map<String,Integer> currentLockers(){
        Map<String, Integer> refs = lokers.get();
        if (refs !=null){
            return refs;
        }
        lokers.set(new HashMap<>());
        return lokers.get();
    }

    private boolean lock(String key){
        Map<String, Integer> refs = currentLockers();
        Integer refCnt = refs.get(key);
        if (refCnt !=null){
            refs.put(key,refCnt+1);
            return true;
        }
        boolean ok = this._lock(key);
        if (!ok){
            return false;
        }
        refs.put(key,1);

        return false;
    }

    private boolean unlock(String key){
        Map<String, Integer> refs = currentLockers();
        Integer refCnt = refs.get(key);
        if (refCnt == null){
            return false;
        }
        refCnt -= 1;
        if (refCnt > 0){
            refs.put(key,refCnt);
        }else {
            refs.put(key,refCnt);
            this._unlock(key);
        }
        return true;
    }
}
