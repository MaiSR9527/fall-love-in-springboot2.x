package com.msr.better.common.db;


import java.util.List;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2022-06-15 23:32
 **/
public interface BatchPreparedStatementParamsSetter<T> {
    void row(List<Object> list, T t);
}
