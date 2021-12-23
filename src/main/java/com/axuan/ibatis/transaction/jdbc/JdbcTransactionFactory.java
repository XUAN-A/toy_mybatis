package com.axuan.ibatis.transaction.jdbc;

import com.axuan.ibatis.session.TransactionIsolationLevel;
import com.axuan.ibatis.transaction.Transaction;
import com.axuan.ibatis.transaction.TransactionFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;

/**
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/12/10 15:33
 */
public class JdbcTransactionFactory implements TransactionFactory {
    @Override
    public void setProperties(Properties props) {

    }

    @Override
    public Transaction newTransaction(Connection conn) {
        return new JdbcTransaction(conn);
    }

    @Override
    public Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit) {
        return new JdbcTransaction(dataSource,level,autoCommit);
    }
}
