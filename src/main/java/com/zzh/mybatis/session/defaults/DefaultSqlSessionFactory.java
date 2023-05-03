package com.zzh.mybatis.session.defaults;

import com.zzh.mybatis.executor.Executor;
import com.zzh.mybatis.mapping.Environment;
import com.zzh.mybatis.session.Configuration;
import com.zzh.mybatis.session.SqlSession;
import com.zzh.mybatis.session.SqlSessionFactory;
import com.zzh.mybatis.session.TransactionIsolationLevel;
import com.zzh.mybatis.transaction.Transaction;
import com.zzh.mybatis.transaction.TransactionFactory;

import java.sql.SQLException;

/**
 * @author: zzh
 * @description: 默认的 DefaultSqlSessionFactory
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private final Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        Transaction tx = null;
        try {
            final Environment environment = configuration.getEnvironment();
            TransactionFactory transactionFactory = environment.getTransactionFactory();
            tx = transactionFactory.newTransaction(configuration.getEnvironment().getDataSource(), TransactionIsolationLevel.READ_COMMITTED, false);
            // 创建执行器
            final Executor executor = configuration.newExecutor(tx);
            // 创建DefaultSqlSession
            return new DefaultSqlSession(configuration, executor);
        } catch (Exception e) {
            try {
                assert tx != null;
                tx.close();
            } catch (SQLException ignore) {
            }
            throw new RuntimeException("Error opening session.  Cause: " + e);
        }
    }
}
