package com.example.distributelock.controller;

import com.example.distributelock.dao.DistributeLockMapper;
import com.example.distributelock.model.DistributeLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
public class DemoController {
    @Resource
    private DistributeLockMapper distributeLockMapper;

    /**
     * 优点:
     * 简单方便,易于理解,操作
     * 缺点:
     * 并发量大时,数据库压力大
     * 建议:
     * 作为锁的数据库与业务数据库分开
     * @return
     * @throws Exception
     */
    @RequestMapping("singleLock")
    //必须加事务  否则基于数据库的分布式锁在这里无意义
    @Transactional(rollbackFor = Exception.class)
    public String singleLock() throws Exception {
        log.info("我进入了方法！");
        DistributeLock distributeLock = distributeLockMapper.selectDistributeLock("demo");
        if (distributeLock==null) throw new Exception("分布式锁找不到");
        log.info("我进入了锁！");
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "我已经执行完成！";
    }
}
