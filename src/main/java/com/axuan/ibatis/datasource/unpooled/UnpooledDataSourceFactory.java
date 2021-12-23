package com.axuan.ibatis.datasource.unpooled;

import com.axuan.ibatis.datasource.DataSourceFactory;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/12/10 19:06
 */
public class UnpooledDataSourceFactory implements DataSourceFactory {

    protected UnpooledDataSource dataSource;

    public UnpooledDataSourceFactory() {
        this.dataSource = new UnpooledDataSource();
    }

    @Override
    public void setProperties(Properties props) {
        for (Object key : props.keySet()) {
            String propertyName = (String)key;
            String propertyValue = (String) props.get(propertyName);
            if ("driver".equals(propertyName)) {
                dataSource.setDriver(propertyValue);
            } else if ("url".equals(propertyName)) {
                dataSource.setUrl(propertyValue);
            } else if ("username".equals(propertyName)) {
                dataSource.setUsername(propertyValue);
            } else if ("password".equals(propertyName)) {
                dataSource.setPassword(propertyValue);
            }
        }
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }
}
