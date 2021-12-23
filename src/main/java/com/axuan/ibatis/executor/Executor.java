package com.axuan.ibatis.executor;

import com.axuan.ibatis.cache.CacheKey;
import com.axuan.ibatis.cfg.Mapper;
import com.axuan.ibatis.mapping.MappedStatement;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 执行器
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/11/30 17:08
 */
public interface Executor {

    // 更新
    int update(MappedStatement ms, Object parameter) throws SQLException;

    // 查询
    <E> List <E> query(MappedStatement ms, Object parameter) throws SQLException, ClassNotFoundException, Exception;

    // 带缓存
    <E> List<E> query(MappedStatement ms, Object parameter, CacheKey key) throws Exception;

    // 提交和回滚,参数是否要强制要求
    void commit(boolean required) throws SQLException;

    void rollback(boolean required) throws SQLException;

    void setExecutorWrapper(Executor executor);


    // 创建CacheKey
    CacheKey createCacheKey(MappedStatement ms, Object parameter, String sql);

    // 清理Session缓存
    void clearLocalCache();


}
