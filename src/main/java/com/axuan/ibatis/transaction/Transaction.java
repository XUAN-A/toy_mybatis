package com.axuan.ibatis.transaction;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/12/10 15:35
 */
public interface Transaction {

    Connection getConnection() throws SQLException;

    void commit() throws SQLException;

    void rollback() throws SQLException;

    void close() throws SQLException;
}
