package com.msr.better.util;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * jedis客户端压测
 *
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-09-01 21:17:10
 */
public class JedisBenchmark {
    private static final int TOTAL_OPERATIONS = 100000;

    public static void main(String[] args) throws Exception {
        long t = System.currentTimeMillis();
        dotest();
        long elapsed = System.currentTimeMillis() - t;
        System.out.println(((1000 * TOTAL_OPERATIONS) / elapsed) + " ops");
    }

    private static void dotest() throws Exception {
        GenericObjectPoolConfig<Jedis> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMinIdle(0);
        poolConfig.setMaxIdle(5);
        poolConfig.setMaxTotal(5);
        final JedisPool pool = new JedisPool(poolConfig, "127.0.0.1", 6379, 2000, "weimob123");

        List<Thread> tds = new ArrayList<>();
        final Integer temp = 0;
        final AtomicInteger ind = new AtomicInteger();
        for (int i = 0; i < 50; i++) {
            Thread hj = new Thread(() -> {
                for (int i1; (i1 = ind.getAndIncrement()) < TOTAL_OPERATIONS; ) {

                    Jedis j = pool.getResource();
                    final String key = "foo" + i1;
                    // redisUtil.set(key, key);
                    j.set(key.getBytes(), ConvertUtil.serialize(temp));
                    j.close();
                }
            });
            tds.add(hj);
            hj.start();
        }

        for (Thread t : tds)
            t.join();
        pool.close();
        pool.destroy();
    }
}
