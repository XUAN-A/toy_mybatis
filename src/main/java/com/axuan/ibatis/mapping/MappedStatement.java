package com.axuan.ibatis.mapping;

import com.axuan.ibatis.cache.Cache;
import com.axuan.ibatis.session.Configuration;

/**
 * 映射的语句
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/12/2 16:13
 */
public class MappedStatement {

    private String resource;
    private Configuration configuration;
    private String id;



    // SQL源码
    private String sql;
    private SqlCommandType sqlCommandType;
    private String databaseId;

    private Cache cache;

    private Class<?> parameterType;
    private Class<?> resultType;
    private boolean flushCacheRequired;
    private boolean useCache;

    MappedStatement() {

    }

    public String getId() {
        return id;
    }

    public String getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(String databaseId) {
        this.databaseId = databaseId;
    }


    public Configuration getConfiguration() {
        return configuration;
    }

    public String getSql() {
        return sql;
    }

    public SqlCommandType getSqlCommandType() {
        return sqlCommandType;
    }

    public Cache getCache() {
        return cache;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }

    public String getResource() {
        return resource;
    }

    public Class<?> getParameterType() {
        return parameterType;
    }

    public Class<?> getResultType() {
        return resultType;
    }

    //  静态内部类 建造者模式
    public static class Builder {
        private MappedStatement mappedStatement = new MappedStatement();


        public Builder(Configuration configuration, String id, String sql,SqlCommandType sqlCommandType)  {
            mappedStatement.configuration = configuration;
            mappedStatement.id = id;
            mappedStatement.sql = sql;
            mappedStatement.sqlCommandType = sqlCommandType;
        }

        public MappedStatement build() {
            return mappedStatement;
        }

        public Builder parameterType(Class<?> parameterType) {
            mappedStatement.parameterType = parameterType;
            return this;
        }

        public Builder resultType(Class<?> resultType) {
            mappedStatement.resultType = resultType;
            return this;
        }

        public Builder cache(Cache cache) {
            mappedStatement.cache = cache;
            return this;
        }

        public Builder flushCacheRequired(boolean flushCacheRequired) {
            mappedStatement.flushCacheRequired = flushCacheRequired;
            return this;
        }

        public Builder useCache(boolean useCache) {
            mappedStatement.useCache = useCache;
            return this;
        }
    }

    public boolean isFlushCacheRequired() {
        return flushCacheRequired;
    }

    public boolean isUseCache() {
        return useCache;
    }

}
