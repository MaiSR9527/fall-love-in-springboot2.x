package com.msr.better.redis.lock;

import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.params.SetParams;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Redis重入锁
 * @author MaiShuRen
 * @site <a href="https://www.maishuren.top">maiBlog</a>
 * @since 2023-01-08 16:35
 **/
public class RedisReentrantLock {
    private ThreadLocal<Map<String, Integer>> lockers = new ThreadLocal<>();

    private final JedisCluster jedisCluster;

    public RedisReentrantLock(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }

    private boolean lock(String key) {
        SetParams setParams = SetParams.setParams();
        setParams.nx();
        setParams.ex(5L);
        return jedisCluster.set(key, "", setParams) != null;
    }

    private void release(String key) {
        jedisCluster.del(key);
    }

    private Map<String, Integer> currentLockers() {
        Map<String, Integer> refs = lockers.get();
        if (Objects.nonNull(refs)) {
            return refs;
        }
        lockers.set(new ConcurrentHashMap<>(16));
        return lockers.get();
    }

    public boolean redisLock(String key) {
        Map<String, Integer> refs = currentLockers();
        Integer refCount = refs.get(key);
        if (Objects.nonNull(refCount)) {
            refs.put(key, refCount + 1);
            return true;
        }
        boolean isLockSuccess = this.lock(key);
        if (!isLockSuccess) {
            return false;
        }
        refs.put(key, 1);
        return true;
    }

    public boolean unLock(String key) {
        Map<String, Integer> refs = currentLockers();
        Integer refCount = refs.get(key);
        if (Objects.isNull(refCount)) {
            return false;
        }
        refCount-=1;
        if (refCount > 0) {
            refs.put(key, refCount);
        } else {
            refs.remove(key);
            this.release(key);
        }
        return true;
    }

}
