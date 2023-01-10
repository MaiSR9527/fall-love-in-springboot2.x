package com.msr.better.redis.limit;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 漏斗限流
 *
 * @author MaiShuRen
 * @site <a href="https://www.maishuren.top">maiBlog</a>
 * @since 2023-01-10 15:25
 **/
public class FunnelRateLimiter {

    private Map<String, Funnel> funnels = new ConcurrentHashMap<>();

    public boolean isAllowed(String userId, String key, int capacity, float leakingRate) {
        String redisKey = String.format("funnelLimiter:%s:%s", userId, key);
        Funnel funnel = funnels.get(redisKey);
        if (Objects.isNull(funnel) ){
            funnel = new Funnel(capacity, leakingRate);
            funnels.put(redisKey, funnel);
        }
        //
        return funnel.watering(1);
    }

    /**
     * 核心漏斗
     */
    static class Funnel {
        /**
         * 漏斗容量
         */
        protected int capacity;
        /**
         * 通过率
         */
        protected float leakingRate;
        /**
         * 剩余配额
         */
        protected int leftQuota;
        /**
         * 通过时间
         */
        protected long leakingTs;

        public Funnel(int capacity, float leakingRate) {
            this.capacity = capacity;
            this.leakingRate = leakingRate;
            this.leftQuota = capacity;
            this.leakingTs = System.currentTimeMillis();
        }

        protected void makeSpace() {
            long nowTs = System.currentTimeMillis();
            long deltaQuota = nowTs - leakingTs;
            // 间隔时间太溢出
            if (deltaQuota < 0) {
                this.leftQuota = capacity;
                this.leakingTs = nowTs;
                return;
            }

            // 腾出空间太小，最小单位是1
            if (deltaQuota < 1) {
                this.leftQuota += deltaQuota;
                this.leakingTs = nowTs;
                if (this.leftQuota > this.capacity) {
                    this.leftQuota = this.capacity;
                }
            }
        }

        protected boolean watering(int quota) {
            makeSpace();
            if (this.leftQuota > quota) {
                this.leftQuota -= quota;
                return true;
            }
            return false;
        }
    }
}
