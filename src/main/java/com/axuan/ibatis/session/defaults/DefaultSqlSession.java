package com.axuan.ibatis.session.defaults;

import com.axuan.ibatis.Exceptions.ExceptionFactory;
import com.axuan.ibatis.Exceptions.TooManyResultsException;
import com.axuan.ibatis.executor.Executor;
import com.axuan.ibatis.mapping.MappedStatement;
import com.axuan.ibatis.session.Configuration;
import com.axuan.ibatis.session.SqlSession;

import java.sql.Connection;
import java.util.List;

/**
 * 默认SqlSession实现
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/11/30 16:52
 */
public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;
    private Executor executor;


    // 是否自动提交
    private boolean autoCommit;
    private boolean dirty;

    public DefaultSqlSession(Configuration configuration, Executor executor, boolean autoCommit) {
        this.configuration = configuration;
        this.executor = executor;
        this.dirty = false;
        this.autoCommit = autoCommit;
    }

    @Override
    public <T> T selectOne(String statement) {
        return null;
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        // 转而去调用selectList,得到0条则返回null，得到1条则返回1条，得到多条报TooManyResultsException错
        // 特别需要注意的是当没有查询到结果的时候就会返回null。因此一般建议在mapper中编写resultType的时候使用包装类型
        // 而不是基本类型，比如推荐使用Integer而不是int。这样就可以避免NPE
        List<T> list = this.<T>selectList(statement, parameter);
        if (list.size() == 1) {
            return list.get(0);
        } else if (list.size() > 1) {
            throw new TooManyResultsException("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
        }
        return null;
    }

    @Override
    public <T> List<T> selectList(String statement) {
        return null;
    }



    // 核心selectList
    public <E> List<E> selectList(String statement, Object parameter) {
        try {
            // 根据statement id找到对应的MappedStatement
            MappedStatement ms = configuration.getMappedStatement(statement);
            // 转而用执行器来查询结果，注意这里传入的ResultHandler是null
            return executor.query(ms, parameter);
        } catch (Exception e) {
            throw ExceptionFactory.wrapException("Error querying database. Cause: " + e, e);
        }
    }

    @Override
    public int insert(String statement, Object parameter) {
        // insert也是调用update
        return update(statement, parameter);
    }

    // 核心update
    @Override
    public int update(String statement, Object parameter) {
        try {
            // 每次更新之前，dirty标志设为true
            dirty = true;
            MappedStatement ms = configuration.getMappedStatement(statement);
            // 转而用执行器来update结果
            return executor.update(ms,parameter);
        } catch (Exception e) {
            throw ExceptionFactory.wrapException("Error updating database. Cause: " + e, e);
        }
    }

    @Override
    public int delete(String statement, Object parameter) {
        // delete也是调用update
        return update(statement, parameter);
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        // 最后会去调用MapperRegistry.getMapper
        return configuration.<T>getMapper(type,this);
    }

    @Override
    public void commit() {
        commit(false);
    }

    // 核心commit
    @Override
    public void commit(boolean force) {
        try {
            // 转而用执行器来commit
            executor.commit(isCommitOrRollbackRequired(force));
            // 每次commit之后，dirty标志设为false
            dirty = false;
        } catch (Exception e) {
            throw ExceptionFactory.wrapException("Error committing transaction. Cause: " + e, e);
        }
    }

    // 检查是否强制commit或rollback
    private boolean isCommitOrRollbackRequired(boolean force) {
        return (!autoCommit && dirty) || force;
    }

    @Override
    public void rollback() {

    }

    @Override
    public void rollback(boolean force) {

    }
}
