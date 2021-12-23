package com.axuan.ibatis.transaction;

import com.axuan.ibatis.session.TransactionIsolationLevel;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;

/**
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/12/10 14:41
 */
public interface TransactionFactory {

    void setProperties(Properties props);

    Transaction newTransaction(Connection conn);

    Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit);
}
