package com.msr.better.common.db;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2022-06-15 23:35
 **/
public interface BatchPreparedStatementSupportIdSetter<T> extends BatchPreparedStatementSetter {
    T  getObject(int i);
}
