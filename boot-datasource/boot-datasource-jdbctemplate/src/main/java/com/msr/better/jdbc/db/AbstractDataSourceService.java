package com.msr.better.jdbc.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2022/4/25
 */
public abstract class AbstractDataSourceService {

    public static Logger logger = LoggerFactory.getLogger(AbstractDataSourceService.class);
    public final int DEFAULT_LIMIT_SIZE = 100;
    public final int REFRESH_SIZE = 500;

    @Resource
    private JdbcTemplate jdbcTemplate;

    public abstract DataSourceNameEnum initDataSourceGroupName();

    private  void slaveDataSource(){
        DataSourceNameEnum groupName = initDataSourceGroupName();
//        DataChooseParam dcp = new DataChooseParam(groupName.name(),DataSourceEnum.SLAVE);
//        DataSourceHolder.putDataSource(dcp);
    }
    private  void masterDataSource(){
        DataSourceNameEnum groupName = initDataSourceGroupName();
//        DataChooseParam dcp = new DataChooseParam(groupName.name(),DataSourceEnum.MASTER);
//        DataSourceHolder.putDataSource(dcp);
    }
}
