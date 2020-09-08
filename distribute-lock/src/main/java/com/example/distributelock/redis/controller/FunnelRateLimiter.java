package com.example.distributelock.redis.controller;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述:
 *
 * @Author: zhangchao
 **/
public class FunnelRateLimiter {
    static class Funnel{
        int capacity;       //漏斗容量
        float leakingRate;  //漏嘴流水速度
        int leftQuota;      //漏斗剩余空间
        long leakingTs;     //上一次漏水时间

        public Funnel(int capacity, float leakingRate) {
            this.capacity = capacity;
            this.leakingRate = leakingRate;
            this.leftQuota = capacity;
            this.leakingTs = System.currentTimeMillis();
        }

        void makeSpace(){
            long nowTs = System.currentTimeMillis();
            //距离上次漏水过去了多久
            long deltaTs = nowTs -leakingTs;
            //又可以腾出多少空间了
            int deltaQuota = (int) (deltaTs * leakingRate);

            //间隔时间太长,整数数字过大溢出
            if (deltaQuota < 0){
                this.leftQuota = capacity;
                this.leakingTs = nowTs;
                return;
            }
            // 腾出空间大小,最小单位是1
            if (deltaQuota < 1){
                return;
            }
            this.leftQuota += deltaQuota;
            this.leakingTs = nowTs;
            if (this.leftQuota > this.capacity){
                this.leftQuota = this.capacity;
            }
        }

        boolean watering(int quota){
            makeSpace();
            if (this.leftQuota >= quota){
                this.leftQuota -= quota;
                return true;
            }
            return false;
        }

        private Map<String,Funnel> funnels = new HashMap<>();

        public boolean isActionAllowed(String userId,String actionKey, int capacity, float leakingRate) {
            String key = String.format("hist:%s:%s:",userId,actionKey);
            Funnel funnel = funnels.get(key);
            if (funnel == null){
                funnel = new Funnel(capacity,leakingRate);
                funnels.put(key,funnel);
            }
            //需要一个 quota
            return funnel.watering(1);
        }
    }
}
