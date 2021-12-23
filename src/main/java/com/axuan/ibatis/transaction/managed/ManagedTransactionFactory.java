package com.axuan.ibatis.transaction.managed;

import com.axuan.ibatis.session.TransactionIsolationLevel;
import com.axuan.ibatis.transaction.Transaction;
import com.axuan.ibatis.transaction.TransactionFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;

/**
 * 托管事务工厂
 * 默认 情况下它会关闭连接。
 * 然后一些容器并不希望这样，因此如果你需要从连接中停止 它，将 closeConnection 属性设置为false》
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/12/12 15:31
 */
public class ManagedTransactionFactory implements TransactionFactory {

    private boolean closeConnection = true;

    @Override
    public void setProperties(Properties props) {
        // 设置 closeConnection
        if (props != null) {
            String closeConnectionProperty = props.getProperty("closeConnection");
            if (closeConnectionProperty != null) {
                closeConnection = Boolean.valueOf(closeConnectionProperty);
            }
        }
    }

    @Override
    public Transaction newTransaction(Connection conn) {
        return new ManagedTransaction(conn, closeConnection);
    }

    @Override
    public Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit) {
        return new ManagedTransaction(dataSource, level, closeConnection);
    }
}
