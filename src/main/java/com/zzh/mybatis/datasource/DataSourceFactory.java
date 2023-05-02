package com.zzh.mybatis.datasource;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author: zzh
 * @description: 数据源工厂
 */
public interface DataSourceFactory {

    void setProperties(Properties props);

    DataSource getDataSource();
    
}
