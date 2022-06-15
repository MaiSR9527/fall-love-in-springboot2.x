package com.msr.better.jdbc.db;

import com.msr.better.common.annotation.IdKey;
import com.msr.better.common.db.BatchPreparedStatementParamsSetter;
import com.msr.better.common.db.BatchPreparedStatementSupportIdSetter;
import com.msr.better.common.util.BeanUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.InterruptibleBatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterDisposer;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2022/4/25
 */
public abstract class AbstractDataSourceService {

    public static Logger log = LoggerFactory.getLogger(AbstractDataSourceService.class);
    //    public final int DEFAULT_LIMIT_SIZE = 100;
    public final int REFRESH_SIZE = 500;

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * PreparedStatement简单查询
     *
     * @param sql       sql
     * @param rowMapper 映射
     * @param values    参数
     * @param <T>       返回类型
     * @return 结果
     */
    public <T> List<T> query(String sql, final RowMapper<T> rowMapper, Object... values) {
        log.info("query-sql:{},param:{}", sql, values);
        final List<T> list = new ArrayList<>();
        jdbcTemplate.query(sql, rs -> {
            try {
                int i = 0;
                while (rs.next()) {
                    list.add(rowMapper.mapRow(rs, i));
                    i++;
                }
            } catch (Exception e) {
                log.error("RowMapper-query error", e);
                throw new RuntimeException("操作失败");
            }
            return list;
        }, replaceParamType(values));
        return list;
    }

    private Object[] replaceParamType(Object... args) {
        return Arrays.stream(args).map(org -> {
            if (org instanceof Date) {
                return new Timestamp(((Date) org).getTime());
            }
            return org;
        }).toArray();
    }

    /**
     * 数据库的时间是微秒的，上面查询使用了new Timestamp(((Date)org).getTime())
     * 导致时间的单位为毫秒了，查询的时候对于时间要求比较严格的需要使用微秒
     *
     * @param sql       sql
     * @param rowMapper 映射
     * @param values    参数
     * @param <T>       泛型
     * @return 结果
     */

    public <T> List<T> queryByStrictTime(String sql, final RowMapper<T> rowMapper, Object... values) {
        log.info("query-sql:{},param:{}", sql, values);
        final List<T> list = new ArrayList<T>();
        jdbcTemplate.query(sql, replaceParamTypeByStrictTime(values), rs -> {
            try {
                int i = 0;
                while (rs.next()) {
                    list.add(rowMapper.mapRow(rs, i));
                    i++;
                }
            } catch (SQLException e) {
                log.error("RowMapper-query error", e);
                throw new RuntimeException("操作失败");
            } catch (Exception e) {
                log.error("RowMapper-query error", e);
                throw new RuntimeException("操作失败");
            }
            return list;
        });

        return list;
    }


    public <T> List<T> queryByInToList(String sql, final RowMapper<T> rowMapper, Collection<?> coll) {
        try {

            String ids = StringUtils.join(coll, ",");
            String inSql = replaceByInToIds(sql, ids);

            log.info("query-sql:{},param:{}", inSql, ids);
            return jdbcTemplate.query(inSql, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            log.error("query error", e);
            throw new RuntimeException("操作失败");
        }
    }

    public <T> List<T> queryByStrInList(String sql, final RowMapper<T> rowMapper, Collection<?> coll) {
        try {

            String ids = StringUtils.join(coll, "','");
            String inSql = replaceByInToIds(sql, String.format("'%s'", ids));

            log.info("query-sql:{},param:{}", inSql, ids);
            return jdbcTemplate.query(inSql, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            log.error("query error", e);
            throw new RuntimeException("操作失败");
        }
    }

    public <T> T queryT(String sql, final RowMapper<T> rowMapper, Object... values) {
        log.info("query-sql:{},param:{}", sql, values);
        try {
            List<T> list = jdbcTemplate.query(sql, rowMapper, replaceParamType(values));
            if (!list.isEmpty()) {
                return list.get(0);
            }
            return null;
        } catch (Exception e) {
            log.error("query error", e);
            throw new RuntimeException("操作失败");
        }
    }

    public <T> List<T> queryForList(String sql, Class<T> elementType, Object... args) {
        log.info("query-sql:{},param:{}", sql, args);
        try {
            return jdbcTemplate.queryForList(sql, elementType, replaceParamType(args));
        } catch (Exception e) {
            log.error("query error", e);
            throw new RuntimeException("操作失败");
        }
    }


    public List<Map<String, Object>> queryList(String sql, Object... args) {
        log.info("query-sql:{},param:{}", sql, args);
        try {
            return jdbcTemplate.queryForList(sql, replaceParamType(args));
        } catch (DataAccessException e) {
            log.error("query error", e);
            throw new RuntimeException("操作失败");
        }
    }

    public int queryInt(String sql, Object... value) {
        log.info("query-sql:{},param:{}", sql, value);
        try {
            return jdbcTemplate.queryForObject(sql, replaceParamType(value), Integer.class);
        } catch (EmptyResultDataAccessException e) {
            log.error("query error", e);
            throw new RuntimeException("操作失败");
        }
    }

    public int queryForInt(String sql) {
        log.info("query-sql:{}", sql);
        try {
            Integer num = jdbcTemplate.queryForObject(sql, Integer.class);
            if (Objects.isNull(num)) {
                return 0;
            }
            return num;
        } catch (EmptyResultDataAccessException e) {
            log.error("query error", e);
            throw new RuntimeException("操作失败");
        }
    }


    public Long queryLong(String sql, Object... value) {
        log.info("query-sql:{},param:{}", sql, value);
        try {
            return jdbcTemplate.queryForObject(sql, replaceParamType(value), Long.class);
        } catch (Exception e) {
            log.error("query error", e);
            throw new RuntimeException("操作失败");
        }
    }


    public String queryString(String sql, Object... value) {
        log.info("query-sql:{},param:{}", sql, value);
        try {
            return jdbcTemplate.queryForObject(sql, replaceParamType(value), String.class);
        } catch (EmptyResultDataAccessException e) {
            log.error("query error", e);
            throw new RuntimeException("操作失败");
        }
    }

    /**
     * 执行update / insert 语句
     *
     * @param sql  sql 语句
     * @param args 参数数组
     * @return 结果
     */

    public boolean update(String sql, Object... args) {
        log.info("update-sql:{},param:{}", sql, args);
        try {

            int c = jdbcTemplate.update(sql, replaceParamType(args));
            return c > 0;
        } catch (Exception dke) {
            log.error("update error", dke);
            throw new RuntimeException("操作失败");
        }
    }

    /**
     * 执行update / insert 语句
     *
     * @param sql  sql 语句
     * @param args 参数数组
     * @return 影响行数
     */

    public int updateReturnAffectedRows(String sql, Object... args) {
        log.info("update-sql:{},param:{}", sql, args);
        try {

            return jdbcTemplate.update(sql, replaceParamType(args));
        } catch (Exception dke) {
            log.error("update error", dke);
            throw new RuntimeException("操作失败");
        }
    }

    /**
     * 执行update / insert 语句
     *
     * @param sql  sql 语句
     * @param args 参数数组
     * @return 结果
     */

    public Object updateReturnValue(String sql, Object... args) {
        log.info("update-sql:{}, params={}", sql, Arrays.toString(args));
        try {

            final Object[] params = replaceParamType(args);
            PreparedStatementCallback<Object> prepared = ps -> {
                if (params.length > 0) {
                    for (int i = 0, length = params.length; i < length; i++) {
                        ps.setObject(i + 1, params[i]);
                    }
                }
                ResultSet rs = null;
                try {
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        return rs.getObject(1);
                    }
                    return null;
                } finally {
                    JdbcUtils.closeResultSet(rs);
                }
            };
            return jdbcTemplate.execute(sql, prepared);
        } catch (Exception dke) {
            log.error("update error", dke);
            throw new RuntimeException("操作失败");
        }
    }

    /**
     * 执行update / insert 语句
     *
     * @param sql  sql 语句
     * @param args 参数数组
     * @return 结果
     */

    public <T> List<T> updateReturnValue(String sql, final RowMapper<T> rowMapper, Object... args) {
        log.info("update-sql:{}, params={}", sql, Arrays.toString(args));
        try {


            final Object[] params = replaceParamType(args);
            CallableStatementCallback<List<T>> prepared = cs -> {
                if (params.length > 0) {
                    for (int i = 0, length = params.length; i < length; i++) {
                        cs.setObject(i + 1, params[i]);
                    }
                }
                ResultSet rs = null;
                try {
                    rs = cs.executeQuery();
                    return new RowMapperResultSetExtractor<T>(rowMapper).extractData(rs);
                } finally {
                    JdbcUtils.closeResultSet(rs);
                }
            };
            return jdbcTemplate.execute(sql, prepared);
        } catch (Exception e) {
            log.error("update error", e);
            throw new RuntimeException("操作失败");
        }
    }


    public <T> T updateReturnObjectValue(String sql, final RowMapper<T> rowMapper, Object... args) {
        return Optional.ofNullable(this.updateReturnValue(sql, rowMapper, args))
                .filter(CollectionUtils::isNotEmpty).map(list -> list.get(0)).orElse(null);
    }


    /**
     * 插入数据,返回自增id
     *
     * @param sql
     * @param key  主键字段名
     * @param args 参数列表
     * @return -1-异常
     */

    public long insert(final String sql, final String key, final Object... args) {
        log.info("insert-sql:{},param:{}", sql, args);
        try {

            long result = -1L;
            KeyHolder keyHolder = new GeneratedKeyHolder();
            PreparedStatementCreator psc = connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{key});
                if (args != null) {
                    for (int i = 0; i < args.length; i++) {
                        Object param = args[i];
                        if (param instanceof Date) {
                            param = new Timestamp(((Date) param).getTime());
                        }
                        ps.setObject(i + 1, param);
                    }
                }
                return ps;
            };
            result = jdbcTemplate.update(psc, keyHolder);
            long id = result > 0 ? Objects.requireNonNull(keyHolder.getKey()).longValue() : -1;
            if (id < 0) {
                log.warn("insert-sql:{}, ,param:{}", sql, args);
            }
            log.info("insert-sql success:{},id:{}", sql, id);
            return id;
        } catch (Exception e) {
            log.error("insert error", e);
            throw new RuntimeException("操作失败");
        }
    }


    /**
     * 翻页计算
     *
     * @param page     0-第一页 1-第二页
     * @param pagesize 每页数量
     * @return 结果
     */

    public int calStart(int page, int pagesize) {
        if (page < 1) {
            page = 1;
        }
        return (page - 1) * pagesize;
    }

    /**
     * 批量执行update / insert 语句
     *
     * @param sql sql
     * @return 结果
     */

    public boolean batchUpdate(String sql, List<Object[]> batchArgs) {
        log.info("update-sql:{},param:{}", sql, batchArgs);
        try {

            int[] ids = jdbcTemplate.batchUpdate(sql, batchArgs);
            return ids.length > 0;

        } catch (DataAccessException dae) {
            log.error("batch update error", dae);
            throw new RuntimeException("操作失败");
        }
    }

    /**
     * 批量更新
     *
     * @param sql sql语句数组
     * @return 每一条sql语句对应影响的行数
     * @author ruan
     */

    public boolean batchUpdate(String[] sql) {
        log.info("update-sql:{}", String.join(",", sql));
        try {

            int[] ids = jdbcTemplate.batchUpdate(sql);
            return ids.length > 0;
        } catch (DataAccessException e) {
            log.error("batch update error", e);
            throw new RuntimeException("操作失败");
        }
    }


    public int[] batchUpdate(String sql, BatchPreparedStatementSetter setter) {
        log.info("update-sql:[{}],param:{}", sql, setter.toString());
        try {


            int[] ids = jdbcTemplate.batchUpdate(sql, setter);
            return ids;

        } catch (DataAccessException dae) {
            dae.printStackTrace();
            log.error("batch update-sql error", dae);
            throw new RuntimeException("操作失败");
        }
    }


    public <T> int batchUpdate(final String sql, final List<T> list, final BatchPreparedStatementParamsSetter<T> batchPreparedStatementParamsSetter) {

        if (StringUtils.isBlank(sql) || CollectionUtils.isEmpty(list) || batchPreparedStatementParamsSetter == null) {
            return 0;
        }


        int[] rowsAffected = this.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                T t = list.get(i);
                final List<Object> paramsList = new ArrayList<>();
                batchPreparedStatementParamsSetter.row(paramsList, t);

                log.info("Preparing: sql=[{}],  sql params=[{}]", sql,
                        paramsList.stream().map(obj -> String.format("%s(%s)", obj, ClassUtils.getSimpleName(obj, "")))
                                .collect(Collectors.joining(",")));

                for (int j = 0; j < paramsList.size(); j++) {
                    Object o = paramsList.get(j);
                    if (o instanceof Date) {
                        ps.setTimestamp(j + 1, conversionToTimeStamp((Date) o));
                    } else {
                        ps.setObject(j + 1, o);
                    }

                }

            }

            @Override
            public int getBatchSize() {
                return list.size();
            }
        });


        return rowsAffected != null ? IntStream.of(rowsAffected).sum() : 0;


    }


    public <T> int batchUpdateReturnId(final String sql, final List<T> list, final BatchPreparedStatementParamsSetter<T> batchPreparedStatementParamsSetter) {

        if (StringUtils.isBlank(sql) || CollectionUtils.isEmpty(list) || batchPreparedStatementParamsSetter == null) {
            return 0;
        }


        return this.batchUpdate(sql, new BatchPreparedStatementSupportIdSetter() {
            @Override
            public Object getObject(int i) {
                return list.get(i);
            }

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                T t = list.get(i);
                final List<Object> paramsList = new ArrayList<>();
                batchPreparedStatementParamsSetter.row(paramsList, t);

                log.info("Preparing: sql=[{}] , sql params=[{}]", sql,
                        paramsList.stream().map(obj -> String.format("%s(%s)", obj, ClassUtils.getSimpleName(obj, "")))
                                .collect(Collectors.joining(",")));

                for (int j = 0; j < paramsList.size(); j++) {
                    Object o = paramsList.get(j);
                    if (o instanceof Date) {
                        ps.setTimestamp(j + 1, conversionToTimeStamp((Date) o));
                    } else {
                        ps.setObject(j + 1, o);
                    }

                }

            }

            @Override
            public int getBatchSize() {
                return list.size();
            }
        });


    }

    public Timestamp conversionToTimeStamp(Date date) {
        return Optional.ofNullable(date)
                .map(t -> new Timestamp(t.getTime()))
                .orElse(null);
    }


    public int batchUpdate(final String sql, final BatchPreparedStatementSupportIdSetter pss) throws DataAccessException {

        log.info("update-sql:[{}]", sql);

        Assert.notNull(pss, "BatchPreparedStatementSupportIdSetter must not be null");
        return jdbcTemplate.execute(
                conn -> conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS), (PreparedStatementCallback<Integer>) ps -> {
                    try {
                        int rowsAffected = 0;
                        int batchSize = pss.getBatchSize();
                        InterruptibleBatchPreparedStatementSetter ipss =
                                (pss instanceof InterruptibleBatchPreparedStatementSetter ?
                                        (InterruptibleBatchPreparedStatementSetter) pss : null);

                        if (JdbcUtils.supportsBatchUpdates(ps.getConnection())) {
                            List<Integer> indexs = new ArrayList<Integer>();
                            for (int i = 0; i < batchSize; i++) {
                                pss.setValues(ps, i);
                                if (ipss != null && ipss.isBatchExhausted(i)) {
                                    break;
                                }
                                ps.addBatch();
                                indexs.add(i);
                                if (i >= 0 && (i % this.REFRESH_SIZE == 0 || i == batchSize - 1)) {
                                    rowsAffected = rowsAffected + Arrays.stream(ps.executeBatch()).sum();
                                    if (indexs.size() > 0) {
                                        ResultSet rs = ps.getGeneratedKeys();
                                        for (int index = 0; rs.next() && index < indexs.size(); index++) {
                                            generatedKey(pss.getObject(indexs.get(index)), rs.getInt(1));
                                        }
                                        indexs.clear();
                                    }
                                }

                            }


                        } else {
                            //List<Integer> rowsAffected = new ArrayList<Integer>();
                            for (int i = 0; i < batchSize; i++) {
                                pss.setValues(ps, i);
                                if (ipss != null && ipss.isBatchExhausted(i)) {
                                    break;
                                }
                                rowsAffected = rowsAffected + ps.executeUpdate();

                                generatedKey(ps, pss.getObject(i));

                            }

                        }

                        return rowsAffected;
                    } finally {
                        if (pss instanceof ParameterDisposer) {
                            ((ParameterDisposer) pss).cleanupParameters();
                        }
                    }
                });

    }

    private void generatedKey(PreparedStatement ps, Object entity) throws SQLException {
        if (entity == null || ps == null) {
            return;
        }

        ResultSet rs = ps.getGeneratedKeys();
        while (rs.next()) {
            generatedKey(entity, rs.getInt(1));

        }

    }

    private void generatedKey(Object entity, Object value) {
        try {
            IdKey head = AnnotationUtils.findAnnotation(entity.getClass(), IdKey.class);
            if (head != null && head.key() != null && !head.key().trim().equals("")) {
                String key = head.key();
                BeanUtil.setProperty(entity, key, value);
            }
        } catch (Exception e) {
            log.error("", e);
            throw new RuntimeException("操作失败");
        }
    }


    private String replaceByInToIds(String sql, String replacement) {
        final String searchString = "?";
        final String searchIn = " IN ";
        int beginIndex = sql.toUpperCase().indexOf(searchIn);
        if (beginIndex > 0 && (sql.indexOf(searchString) > 0)) {
            return StringUtils.replace(sql, searchString, replacement);
        }
        return sql;
    }

    /**
     * 时间单位的按本来时间的值，而不是转为毫秒，再转为Timestamp
     *
     * @param args 参数
     * @return 参数数组
     */
    private Object[] replaceParamTypeByStrictTime(Object... args) {
        return Arrays.stream(args).map(org -> {
            if (org instanceof Date) {
                return org;
            }
            return org;
        }).toArray();
    }
}
