package com.axuan.ibatis.transaction.managed;

import com.axuan.ibatis.logging.Log;
import com.axuan.ibatis.logging.LogFactory;
import com.axuan.ibatis.session.TransactionIsolationLevel;
import com.axuan.ibatis.transaction.Transaction;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 托管事务,交给容器来管理事务
 * MANAGED - 这个配置几乎没做什么
 * 它从来不提交或回滚一个连接。
 * 而它会让 容器来管理事务的整个声明周期（比如 Spring 或 JEE 应用服务器的上下文）
 * 默认 情况下它会关闭连接。
 * 然后一些容器并不希望这样，因此如果你需要从连接中停止 它， 将 closeConnection 属性设置为 false。
 * 如果使用mybatis-spring 的话，不需要配置transactionManager，因此 mybatis-spring覆盖了mybatis里的逻辑
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/12/12 15:37
 */
public class ManagedTransaction implements Transaction {

    private static final Log log = LogFactory.getLog(ManagedTransaction.class);

    private DataSource dataSource;
    private TransactionIsolationLevel level;
    private Connection connection;
    private boolean closeConnection;

    public ManagedTransaction(Connection connection, boolean closeConnection) {
        this.connection = connection;
        this.closeConnection = closeConnection;
    }

    public ManagedTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean closeConnection) {
        this.dataSource = dataSource;
        this.level = level;
        this.closeConnection = closeConnection;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return null;
    }

    @Override
    public void commit() throws SQLException {

    }

    @Override
    public void rollback() throws SQLException {

    }

    @Override
    public void close() throws SQLException {

    }
}
