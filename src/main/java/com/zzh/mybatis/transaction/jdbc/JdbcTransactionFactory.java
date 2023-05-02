package com.zzh.mybatis.transaction.jdbc;

import com.zzh.mybatis.session.TransactionIsolationLevel;
import com.zzh.mybatis.transaction.Transaction;
import com.zzh.mybatis.transaction.TransactionFactory;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @author: zzh
 * @description: JdbcTransaction 工厂
 */
public class JdbcTransactionFactory implements TransactionFactory {
    @Override
    public Transaction newTransaction(Connection conn) {
        return new JdbcTransaction(conn);
    }

    @Override
    public Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit) {
        return new JdbcTransaction(dataSource, level, autoCommit);
    }
}
