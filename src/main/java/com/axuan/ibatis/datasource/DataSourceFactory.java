package com.axuan.ibatis.datasource;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/12/10 15:15
 */
public interface DataSourceFactory {

    void setProperties(Properties props);

    DataSource getDataSource();
}
