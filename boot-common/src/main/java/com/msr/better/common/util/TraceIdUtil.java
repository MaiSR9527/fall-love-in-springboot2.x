package com.msr.better.common.util;

import java.util.UUID;

/**
 * @author maisrcn@qq.com
 * @since 2024-01-30
 */
public class TraceIdUtil {
    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    public static String uuid_timestamp() {
        return UUID.randomUUID().toString() + "_" + System.currentTimeMillis();
    }
}
