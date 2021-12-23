package com.axuan.ibatis.session.defaults;

import com.axuan.ibatis.Exceptions.ExceptionFactory;
import com.axuan.ibatis.executor.Executor;
import com.axuan.ibatis.mapping.Environment;
import com.axuan.ibatis.session.*;
import com.axuan.ibatis.transaction.Transaction;
import com.axuan.ibatis.transaction.TransactionFactory;
import com.axuan.ibatis.transaction.managed.ManagedTransactionFactory;

/**
 * 接口的实现类
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/11/30 16:50
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    // 最终都会调用2种方法，openSessionFromDataSource,openSessionFromConnection
    
    // 目前只看openSessionFromDataSouce


    @Override
    public SqlSession openSession() {
        return openSessionFromDataSource(configuration.getDefaultExecutorType(), null, false);
    }

    @Override
    public SqlSession openSession(boolean autoCommit) {
        return openSessionFromDataSource(configuration.getDefaultExecutorType(), null, autoCommit);
    }

    private SqlSession openSessionFromDataSource(ExecutorType executorType, TransactionIsolationLevel level, boolean autoCommit) {
        Transaction tx = null;
        try {
            final Environment environment = configuration.getEnvironment();
            final TransactionFactory transactionFactory = getTransactionFactoryFromEnvironment(environment);
            // 通过事务工厂来产生一个事务
            tx = transactionFactory.newTransaction(environment.getDataSource(), level, autoCommit);
            // 生成一个执行器（事务包含在执行器里)
            final Executor executor = configuration.newExecutor(tx, executorType);
            // 然后生成一个DefaultSqlSession
            return new DefaultSqlSession(configuration, executor, autoCommit);
        } catch (Exception e) {
            // 如果打开事务出错，则关闭它
            //closeTransaction(tx);
            throw ExceptionFactory.wrapException("Error opening session. Cause: " + e, e);
        }
    }

    private TransactionFactory getTransactionFactoryFromEnvironment(Environment environment) {
        // 如果没有配置事务工厂，则返回托管事务工厂
        if (environment == null || environment.getTransactionFactory() == null) {
            return new ManagedTransactionFactory();
        }
        return environment.getTransactionFactory();
    }
}
