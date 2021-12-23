package com.axuan.ibatis.executor;

import com.axuan.ibatis.cache.CacheKey;
import com.axuan.ibatis.cache.impl.PerpetualCache;
import com.axuan.ibatis.logging.Log;
import com.axuan.ibatis.logging.LogFactory;
import com.axuan.ibatis.mapping.MappedStatement;
import com.axuan.ibatis.session.Configuration;
import com.axuan.ibatis.session.LocalCacheScope;
import com.axuan.ibatis.transaction.Transaction;
import com.axuan.ibatis.utils.SqlUtil;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 执行器基类
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/12/12 15:50
 */
public abstract class BaseExecutor implements Executor{

    private static final Log log = LogFactory.getLog(BaseExecutor.class);

    protected Transaction transaction;
    protected Executor wrapper;

    // 本地缓存
    protected PerpetualCache localCache;

    protected Configuration configuration;

    // 查询堆栈
    protected int queryStack = 0;
    private boolean closed;


    protected Connection conn;


    public BaseExecutor(Configuration configuration, Transaction transaction) throws SQLException {
        this.transaction = transaction;
        this.localCache = new PerpetualCache("LocalCache");
        this.closed = false;
        this.configuration = configuration;
        this.wrapper = this;
        this.conn = transaction.getConnection();
    }

    // SqlSession.update/insert/delete会调用此方法
    @Override
    public int update(MappedStatement ms, Object parameter) throws SQLException {
        if (closed) {
            throw new ExecutorException("Executor was closed");
        }
        // 先清局部缓存，再更新，如何更新交由子类，模板方法模式
        clearLocalCache();
        return doUpdate(ms, parameter);
    }


    @Override
    public <E> List<E> query(MappedStatement ms, Object parameter) throws SQLException, ClassNotFoundException, Exception {
        // 得到sql
        String sql = ms.getSql();
        // 创建缓存key
        CacheKey key = createCacheKey(ms, parameter, sql);
        // 查询
        return query(ms, parameter, key);
    }

    @Override
    public <E> List<E> query(MappedStatement ms, Object parameter, CacheKey key) throws Exception {
        // 如果已经关闭，报错
        if (closed) {
            throw new ExecutorException("Executor was closed");
        }
        // 先清局部缓存，再查询，但仅查询堆栈为0，才清。为了处理递归调用
        if (queryStack == 0 && ms.isFlushCacheRequired()) {
            clearLocalCache();
        }
        List<E> list;
        try {
            // 加一，这样递归调用到上面的时候就不会再清局部缓存了
            queryStack++;
            // 先根据cacheKey从localCache去查
            // 从一级缓存中获取数据
            list = (List<E>) localCache.getObject(key);
            if (list != null) {
                // 若查到localCache缓存，处理localOutputParameterCache,这里是处理存储式的，因此不需要，直接返回数据
            } else {
                // 从数据库查
                list = queryFromDatabase(ms, parameter, key);
            }
        } finally {
            // 清空堆栈
            queryStack--;
        }
        if (queryStack == 0) {
            if (configuration.getLocalCacheScope() == LocalCacheScope.STATEMENT) {
                // 如果是Statement,清本地缓存
                clearLocalCache();
            }
        }
        return list;
    }

    private <E> List<E> queryFromDatabase(MappedStatement ms, Object parameter, CacheKey key) throws SQLException {
        List<E> list;
        // 先向缓存中放入占位符
        localCache.putObject(key, ExecutionPlaceholder.EXECUTION_PLACEHOLDER);
        try {
            list = doQuery(ms, parameter);
        } finally {
            // 最后删除占位符
            localCache.removeObject(key);
        }

        // 加入缓存
        localCache.putObject(key, list);

        return list;
    }

    protected abstract int doUpdate(MappedStatement ms, Object parameter) throws SQLException;

    protected abstract <E> List<E> doQuery(MappedStatement ms, Object parameter) throws SQLException;

    protected Connection getConnection(Log statementLog) throws SQLException {
        Connection connection = transaction.getConnection();
        //if (statementLog.isDebugEnabled()) {
        //    // 如果需要打印Connection的日志，返回一个ConnectionLogger（代理模式，AOP思想）
        //    return ConnectionLogger.
        //}
        return connection;
    }

    @Override
    public void commit(boolean required) throws SQLException {
        if (closed) {
            throw new ExecutorException("Cannot commit, transaction is already closed");
        }
        clearLocalCache();
        if (required) {
            transaction.commit();
        }
    }

    @Override
    public void rollback(boolean required) throws SQLException {

    }

    @Override
    public void setExecutorWrapper(Executor executor) {
        this.wrapper = wrapper;
    }

    @Override
    public CacheKey createCacheKey(MappedStatement ms, Object parameter, String sql) {
        if (closed) {
            throw new ExecutorException("Executor was closed");
        }
        CacheKey cacheKey = new CacheKey();
        // Mybatis对于其 Key 的生成采取规则为:[mappedStatementId + offset + limit + SQL + queryParams + environment] 生成一个哈希码
        cacheKey.update(ms.getId());
        cacheKey.update(sql);
        if (configuration.getEnvironment() != null) {
            cacheKey.update(configuration.getEnvironment().getId());
        }
        return cacheKey;
    }

    @Override
    public void clearLocalCache() {
        if (!closed) {
            localCache.clear();
        }
    }

    protected void closeStatement(Statement statement) {
        if (statement !=  null) {
            try {
                statement.close();
            } catch (SQLException e) {
                // ignore
            }
        }
    }
}
